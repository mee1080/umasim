package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState

fun SimulationState.updateMujintoStatus(update: MujintoStatus.() -> MujintoStatus): SimulationState {
    val mujintoStatus = this.mujintoStatus ?: return this
    return copy(scenarioStatus = mujintoStatus.update())
}

/**
 * 0: 3T～
 * 1: 13T～
 * 2: 25T～
 * 3: 37T～
 * 4: 49T～
 * 5: 61T～
 */
fun MujintoStatus.updatePhase(phase: Int, facilityPlan: List<MujintoFacility> = emptyList()): MujintoStatus {
    val maxSpace = mujintoFacilitySpace[linkMode][phase]
    return copy(
        facilityPlan = facilityPlan,
        pioneerPoint = 0,
        requiredPoint1 = maxSpace / 2 * 100,
        requiredPoint2 = maxSpace * 100,
        evaluationBonus = mujintoEvaluationBonuses[phase],
    )
}

class MujintoFacility(
    val type: StatusType,
    val level: Int,
    val jukuren: Boolean = false,
) {
    val space = if (type == StatusType.FRIEND) {
        if (level == 1) 2 else 3
    } else when (level) {
        5 -> if (jukuren) 2 else 3
        4, 3 -> if (jukuren) 1 else 2
        else -> 1
    }

    private fun calcIslandTrainingBonus(targetType: StatusType): Int {
        return if (type == StatusType.FRIEND) {
            if (targetType == StatusType.SKILL) 1 else 0
        } else if (type == targetType) when (level) {
            5 -> if (jukuren) 3 else 7
            4 -> if (jukuren) 2 else 5
            3 -> if (jukuren) 2 else 3
            2 -> 2
            else -> 1
        } else if (targetType == StatusType.SKILL) {
            if (jukuren) {
                if (level >= 4) 1 else 0
            } else when (level) {
                5 -> 2
                4, 3 -> 1
                else -> 0
            }
        } else if (level == 5) {
            if (upInTraining(type, targetType) && (type != StatusType.GUTS || targetType != StatusType.SPEED)) {
                1
            } else {
                0
            }
        } else {
            0
        }
    }

    /**
     * 島合宿でトレ効果を反映するかどうか
     */
    private fun applyInCamp(trainingType: StatusType): Boolean {
        return type == trainingType || type == StatusType.FRIEND
    }

    /**
     * 島トレのステータスボーナス
     */
    val islandTrainingBonus = Status(
        calcIslandTrainingBonus(StatusType.SPEED),
        calcIslandTrainingBonus(StatusType.STAMINA),
        calcIslandTrainingBonus(StatusType.POWER),
        calcIslandTrainingBonus(StatusType.GUTS),
        calcIslandTrainingBonus(StatusType.WISDOM),
        calcIslandTrainingBonus(StatusType.SKILL),
    )

    private val islandTrainingEffectValue = if (type == StatusType.FRIEND) when (level) {
        3 -> 15
        2 -> 10
        else -> 5
    } else when (level) {
        5 -> if (jukuren) 15 else 25
        4 -> if (jukuren) 10 else 15
        3 -> if (jukuren) 5 else 10
        2 -> 5
        else -> 0
    }

    /**
     * 島トレのトレ効果
     */
    fun islandTrainingEffect(targetType: StatusType, friendCount: Int): Int {
        return if (type == StatusType.FRIEND || upInTraining(type, targetType)) {
            islandTrainingEffectValue
        } else 0
    }

    private val specialityRateValue = if (type == StatusType.FRIEND || jukuren) 0 else when (level) {
        5 -> 80
        4 -> 50
        3 -> 20
        else -> 0
    }

    private val campSpecialityRateValue = specialityRateValue + if (type == StatusType.FRIEND) 0 else when (level) {
        5 -> if (jukuren) 50 else 100
        4 -> if (jukuren) 30 else 50
        3 -> if (jukuren) 20 else 60
        else -> 0
    }

    /**
     * 通常時の得意率アップ
     */
    fun specialityRate(cardType: StatusType): Int {
        return if (type == cardType) specialityRateValue else 0
    }

    /**
     * 合宿時の得意率アップ
     */
    fun campSpecialityRate(cardType: StatusType): Int {
        return if (type == cardType) campSpecialityRateValue else 0
    }

    private val campTrainingEffectValue = when (type) {
        StatusType.FRIEND -> 0

        StatusType.SPEED -> when (level) {
            5 -> if (jukuren) 25 else 50
            4 -> if (jukuren) 15 else 30
            3 -> if (jukuren) 10 else 20
            2 -> 10
            else -> 5
        }

        else -> when (level) {
            5 -> if (jukuren) 30 else 60
            4 -> if (jukuren) 25 else 40
            3 -> if (jukuren) 20 else 30
            2 -> 20
            else -> 10
        }
    }

    /**
     * 島合宿のトレーニング効果
     */
    fun campTrainingEffect(targetType: StatusType): Int {
        return if (applyInCamp(targetType)) campTrainingEffectValue else 0
    }

    /**
     * 通常時のヒント発生率
     */
    val hintUp = if (type == StatusType.FRIEND) when (level) {
        3 -> 200
        2 -> 100
        else -> 0
    } else {
        0
    }

    private val campHintUpValue = hintUp + if (!jukuren || type == StatusType.FRIEND) 0 else when (level) {
        5, 4 -> 100000
        3 -> 200
        else -> 0
    }

    /**
     * 島合宿のヒント発生率
     */
    fun campHintUp(targetType: StatusType): Int {
        return if (applyInCamp(targetType)) campHintUpValue else 0
    }

    private val campHintAllValue = jukuren && level == 5

    /**
     * 島合宿で発生している全ヒントイベントの効果が発動
     */
    fun campHintAll(targetType: StatusType): Boolean {
        return campHintAllValue && applyInCamp(targetType)
    }

    /**
     * 友情数によるトレーニング効果
     */
    val trainingEffectByFriend = if (type == StatusType.FRIEND) 5 else 0

    /**
     * サポカ出現率
     */
    val positionRateUp = if (type == StatusType.FRIEND) when (level) {
        3 -> 20
        else -> 10
    } else {
        0
    }
}

data class MujintoEvaluationBonus(
    val trainingEffect: Int,
    val hintRateUp: Int,
    val pioneerPointBonus: Int,
)

data class MujintoStatus(
    val facilities: Map<StatusType, MujintoFacility> = emptyMap(),
    val facilityPlan: List<MujintoFacility> = emptyList(),
    val pioneerPoint: Int = 0,
    val requiredPoint1: Int = 0,
    val requiredPoint2: Int = 0,
    val islandTrainingTicket: Int = 0,
    val evaluationBonus: MujintoEvaluationBonus = mujintoEvaluationBonus(1),
    val linkMode: Int = 2,
) : ScenarioStatus {

    constructor(support: List<SupportCard>) : this(
        linkMode = support.firstOrNull { it.chara == "タッカーブライン" }?.let {
            if (it.maxLevel >= 41) 2 else 1
        } ?: 0
    )

    fun toShortString() = buildString {
        append("施設：")
        trainingType.forEach {
            val facility = facilities[it]
            if (facility != null && facility.level >= 3 && it != StatusType.FRIEND) {
                append(if (facility.jukuren) "熟練" else "本能")
            }
            append(facility?.level ?: 0)
            append("/")
        }
        append(facilities[StatusType.FRIEND]?.level ?: 0)
        append(" 発展Pt：")
        append(pioneerPoint)
        append("/")
        append(requiredPoint1)
        append("/")
        append(requiredPoint2)
        append(" 島チケ：")
        append(islandTrainingTicket)
    }

    fun addPioneerPoint(point: Int, upgradeFacility: Boolean = true): MujintoStatus {
        val newFacilities = facilities.toMutableMap()
        val newPlan = facilityPlan.toMutableList()
        var newIslandTrainingTicket = islandTrainingTicket
        if (upgradeFacility) {
            if (pioneerPoint < requiredPoint1) {
                if (pioneerPoint + point >= requiredPoint1) {
                    var totalCost = 0
                    while (totalCost < requiredPoint1) {
                        val facility = newPlan.removeFirstOrNull() ?: break
                        newFacilities[facility.type] = facility
                        totalCost += facility.space * 100
                    }
                    newIslandTrainingTicket = 1
                }
            } else if (pioneerPoint < requiredPoint2) {
                if (pioneerPoint + point >= requiredPoint2) {
                    newPlan.forEach { newFacilities[it.type] = it }
                    newPlan.clear()
                    newIslandTrainingTicket = 1
                }
            }
        }
        return copy(
            facilities = newFacilities,
            facilityPlan = newPlan,
            pioneerPoint = pioneerPoint + point,
            islandTrainingTicket = newIslandTrainingTicket,
        )
    }

    fun upgradeFacilityAfterCamp(): MujintoStatus {
        val newFacilities = facilities.toMutableMap()
        val newPlan = facilityPlan.toMutableList()
        var newIslandTrainingTicket = islandTrainingTicket
        if (pioneerPoint >= requiredPoint2) {
            newPlan.forEach { newFacilities[it.type] = it }
            newPlan.clear()
            newIslandTrainingTicket = 1
        } else if (pioneerPoint >= requiredPoint1) {
            var totalCost = 0
            while (totalCost < requiredPoint1) {
                val facility = newPlan.removeFirstOrNull() ?: break
                newFacilities[facility.type] = facility
                totalCost += facility.space * 100
            }
            newIslandTrainingTicket = 1
        }
        return copy(
            facilities = newFacilities,
            facilityPlan = newPlan,
            islandTrainingTicket = newIslandTrainingTicket,
        )
    }

    val trainingEffectByFriend by lazy {
        facilities.values.sumOf {
            it.trainingEffectByFriend
        }
    }

    val islandTrainingBonus by lazy {
        facilities.values.fold(Status()) { acc, facility ->
            acc + facility.islandTrainingBonus
        }
    }

    fun islandTrainingEffect(targetType: StatusType, friendCount: Int) = facilities.values.sumOf {
        it.islandTrainingEffect(targetType, friendCount)
    }

    fun campTrainingEffect(trainingType: StatusType) = facilities.values.sumOf {
        it.campTrainingEffect(trainingType)
    }

    fun specialityRate(cardType: StatusType) = facilities.values.sumOf {
        it.specialityRate(cardType)
    }

    fun campSpecialityRate(cardType: StatusType) = facilities.values.sumOf {
        it.campSpecialityRate(cardType)
    }

    fun hintUp() = facilities.values.sumOf {
        it.hintUp
    }

    fun campHintUp(targetType: StatusType) = facilities.values.sumOf {
        it.campHintUp(targetType)
    }

    fun campHintAll(targetType: StatusType) = facilities.values.any {
        it.campHintAll(targetType)
    }

    fun positionRateUp() = facilities.values.sumOf {
        it.positionRateUp
    }

    fun getFacilityLevel(facilityType: StatusType) = facilities[facilityType]?.level ?: 0

    fun notGoalRacePioneerPtBonusEnabled() = getFacilityLevel(StatusType.FRIEND) >= 3

    fun friendFacilityCountBonusEnabled() = getFacilityLevel(StatusType.FRIEND) >= 1
}

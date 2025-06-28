package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.upInTraining
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.ScenarioMemberState
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState

fun SimulationState.updateMujintoStatus(update: MujintoStatus.() -> MujintoStatus): SimulationState {
    val mujintoStatus = this.mujintoStatus ?: return this
    return copy(scenarioStatus = mujintoStatus.update())
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

    val cost = space * 100

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
     * 島合宿でステータスボーナスとトレ効果を反映するかどうか
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

    /**
     * 島合宿のステータスボーナス
     */
    fun campTrainingBonus(trainingType: StatusType): Status {
        return if (applyInCamp(trainingType)) islandTrainingBonus else Status()
    }

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
            islandTrainingEffectValue + friendCount * trainingEffectByFriend
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

    private val campTrainingEffectValue = if (type == StatusType.FRIEND) 0 else when (level) {
        5 -> if (jukuren) 25 else 50
        4 -> if (jukuren) 15 else 30
        3 -> if (jukuren) 10 else 20
        2 -> 10
        else -> 5
    }

    /**
     * 島合宿のトレーニング効果
     */
    fun campTrainingEffect(targetType: StatusType, friendCount: Int): Int {
        return if (applyInCamp(targetType)) {
            campTrainingEffectValue + friendCount * trainingEffectByFriend
        } else 0
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

    private val trainingEffectByFriend = if (type == StatusType.FRIEND) 5 else 0

    /**
     * サポカ出現率
     */
    val positionRateUp = if (type == StatusType.FRIEND) when (level) {
        3 -> 20
        else -> 10
    } else {
        0
    }

    /**
     * 目標外レース発展Pt
     */
    val notGoalRacePioneerPtBonus = if (type == StatusType.FRIEND && level == 3) 200 else 0
}

data class MujintoEvaluationBonus(
    val trainingEffect: Int,
)

data class MujintoStatus(
    val facilities: Map<StatusType, MujintoFacility> = emptyMap(),
    val facilityPlan: List<MujintoFacility> = emptyList(),
    val pioneerPoint: Int = 0,
    val islandTrainingTicket: Int = 0,
    val evaluationBonus: List<MujintoEvaluationBonus> = emptyList(),
    val nextTurnSpecialtyBuff: Int = 0,
) : ScenarioStatus {
    fun addPioneerPoint(point: Int): MujintoStatus {
        // TODO 施設、チケット獲得
        return copy(pioneerPoint = pioneerPoint + point)
    }

    val islandTrainingBonus by lazy {
        facilities.values.fold(Status()) { acc, facility ->
            acc + facility.islandTrainingBonus
        }
    }

    fun islandTrainingEffect(targetType: StatusType, friendCount: Int) = facilities.values.sumOf {
        it.islandTrainingEffect(targetType, friendCount)
    }

    fun campTrainingBonus(trainingType: StatusType) = facilities.values.fold(Status()) { acc, facility ->
        acc + facility.campTrainingBonus(trainingType)
    }

    fun campTrainingEffect(targetType: StatusType, friendCount: Int) = facilities.values.sumOf {
        it.campTrainingEffect(targetType, friendCount)
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

    fun notGoalRacePioneerPtBonus() = facilities.values.sumOf {
        it.notGoalRacePioneerPtBonus
    }
}

// Scenario-specific state for support cards, especially for Tucker Bligh
data class MujintoMemberState(
    val exampleField: Int = 0 // Placeholder for Tucker Bligh specific mechanics
    // TODO: Define fields relevant to Tucker Bligh's unique interactions if any,
    // e.g., tracking her "得意率アップ" buff for other cards.
) : ScenarioMemberState(Scenario.MUJINTO) {
    override fun addRelation(relation: Int): ScenarioMemberState {
        // TODO: Implement if Tucker Bligh or other cards have special relation gain mechanics
        return this
    }
}

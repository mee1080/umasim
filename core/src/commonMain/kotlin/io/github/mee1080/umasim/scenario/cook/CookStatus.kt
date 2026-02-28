package io.github.mee1080.umasim.scenario.cook

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.randomSelect
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.applyIf
import io.github.mee1080.utility.mapIf
import io.github.mee1080.utility.mapValuesIf
import io.github.mee1080.utility.replaced
import kotlin.math.min
import kotlin.random.Random

data class CookStatus(
    val gardenPoint: Int = 0,
    val currentStamp: List<CookStamp> = emptyList(),
    val sleepStamp: CookStamp = CookStamp(CookMaterial.Carrot, false, 0),
    val raceStamp: CookStamp = CookStamp(CookMaterial.Carrot, false, 0),
    val materialLevel: Map<CookMaterial, Int> = CookMaterial.entries.associateWith { 1 },
    val materialCount: Map<CookMaterial, Int> = CookMaterial.entries.associateWith { 0 },
    val cookPoint: Int = 0,
    val cookGauge: Int = 0,
    val dishRank: List<Int> = listOf(0, -1, -1, -1),
    val activatedDish: CookDish? = null,
    val alwaysHarvest: Boolean = false,
) : ScenarioStatus {
    val cookPointEffect by lazy {
        when {
            cookPoint >= 12000 -> cookPointEffects[7]
            cookPoint >= 10000 -> cookPointEffects[6]
            cookPoint >= 7000 -> cookPointEffects[5]
            cookPoint >= 5000 -> cookPointEffects[4]
            cookPoint >= 2500 -> cookPointEffects[3]
            cookPoint >= 1500 -> cookPointEffects[2]
            cookPoint >= 500 -> cookPointEffects[1]
            else -> cookPointEffects[0]
        }
    }

    val requiredGardenPoint by lazy {
        val classic = dishRank[1] >= 0
        val senior = dishRank[2] >= 0
        CookMaterial.entries.associateWith {
            when (materialLevel[it]) {
                1 -> 100
                2 -> if (classic) 180 else Int.MAX_VALUE
                3 -> if (senior) 220 else Int.MAX_VALUE
                4 -> if (senior) 250 else Int.MAX_VALUE
                else -> Int.MAX_VALUE
            }
        }
    }

    fun materialLevelUp(material: CookMaterial): CookStatus {
        return copy(
            gardenPoint = gardenPoint - requiredGardenPoint[material]!!,
            materialLevel = materialLevel.mapValuesIf({ it.key == material }, { it + 1 }),
        )
    }

    fun updateTurn(
        isGoalRace: Boolean,
        isLevelUpTurn: Boolean,
        alwaysHarvest: Boolean,
    ): CookStatus {
        return copy(
            sleepStamp = CookStamp(
                CookMaterial.entries.random(),
                isLevelUpTurn || Random.nextInt(100) < 20,
                0,
            ),
            raceStamp = CookStamp(
                CookMaterial.entries.random(),
                isGoalRace || Random.nextInt(100) < 40,
                0,
            ),
            alwaysHarvest = alwaysHarvest,
            activatedDish = null,
        )
    }

    fun updateDishRank(phase: Int, rank: Int): CookStatus {
        return copy(dishRank = dishRank.replaced(phase, rank))
    }

    fun addStamp(stamp: CookStamp): CookStatus {
        var newState = copy(currentStamp = currentStamp + stamp)
        if (alwaysHarvest || newState.currentStamp.size == 4) {
            newState = newState.harvest()
        }
        return newState
    }

    val fullPowerFactor by lazy {
        when (currentStamp.count { it.fullPower }) {
            0 -> 1.0
            1 -> if (alwaysHarvest) 1.5 else 1.1
            2 -> 1.2
            3 -> 1.4
            else -> 1.6
        }
    }

    val pendingMaterials by lazy {
        calcPendingMaterials(currentStamp)
    }

    fun calcPendingMaterials(stampList: List<CookStamp>): Map<CookMaterial, Int> {
        return CookMaterial.entries.associateWith { material ->
            val level = materialLevel[material]!!
            val base = (when (level) {
                1, 2 -> 20
                3 -> 30
                else -> 40
            } * (if (alwaysHarvest) 0.5 else 1.0) * fullPowerFactor).toInt()
            val materialStamp = stampList.filter { it.material == material }
            if (materialStamp.isEmpty()) base else {
                val additional = (when (level) {
                    1 -> 20
                    2, 3 -> 30
                    else -> 40
                } * materialStamp.size * fullPowerFactor).toInt()
                val plus = (materialStamp.sumOf { it.plus } * fullPowerFactor).toInt()
                base + additional + plus
            }
        }
    }

    fun harvest(): CookStatus {
        return addMaterials(pendingMaterials).copy(
            gardenPoint = gardenPoint + ((if (alwaysHarvest) 50 else 100) * fullPowerFactor).toInt(),
            currentStamp = emptyList(),
        )
    }

    val materialLimit by lazy {
        CookMaterial.entries.associateWith { material ->
            when (materialLevel[material]) {
                1 -> 200
                2 -> 400
                3 -> 600
                4 -> 800
                else -> 999
            }
        }
    }

    fun addMaterials(materials: Map<CookMaterial, Int>): CookStatus {
        val newMaterialCount = materialCount.mapValues { (material, count) ->
            min(materialLimit[material]!!, count + (materials.getOrElse(material) { 0 }))
        }
        return copy(materialCount = newMaterialCount)
    }

    val learnedDishList by lazy {
        cookDishData.filter {
            it.rank == dishRank[it.phase]
        }
    }

    val availableDishList by lazy {
        learnedDishList.filter { dish ->
            dish.materials.all { (material, count) ->
                materialCount[material]!! >= count
            }
        }
    }

    fun modifyDish(dish: CookDish): CookDish {
        if (dish.modified) return dish
        return when (dish.phase) {
            3 -> {
                val level5MaterialCount = materialLevel.count { it.value == 5 }
                dish.copy(
                    trainingFactor = dish.trainingFactor + level5MaterialCount * 5,
                    raceBonus = dish.raceBonus + level5MaterialCount * 5,
                    modified = true,
                )
            }

            1, 2 -> {
                val level = materialLevel[statusTypeToCookMaterial[dish.mainTrainingTarget]]!!
                dish.copy(
                    trainingFactor = dish.trainingFactor + if (level == 5) 10 else 0,
                    hp = dish.hp + if (level >= 3) 5 else 0,
                    modified = true,
                )
            }

            else -> dish.copy(modified = true)
        }
    }

    val activatedDishModified by lazy {
        activatedDish?.let { modifyDish(it) }
    }
}

fun SimulationState.updateCookStatus(update: CookStatus.() -> CookStatus): SimulationState {
    val cookStatus = cookStatus ?: return this
    return copy(scenarioStatus = cookStatus.update())
}

fun SimulationState.activateDish(dish: CookDish): SimulationState {
    val cookStatus = cookStatus ?: return this
    val modifiedDish = cookStatus.modifyDish(dish)
    val newMaterialCount = cookStatus.materialCount.mapValues { (material, count) ->
        count - modifiedDish.materials.getOrElse(material) { 0 }
    }
    var newGauge = cookStatus.cookGauge + modifiedDish.gainPoint
    var success = false
    if (cookStatus.cookGauge >= 1500) {
        newGauge -= 1500
        success = true
    } else if (Random.nextInt(100) < cookStatus.cookPointEffect.successRate) {
        success = true
    }
    val newCookPoint = cookStatus.cookPoint + modifiedDish.gainPoint

    var newState = updateCookStatus {
        copy(
            materialCount = newMaterialCount,
            activatedDish = modifiedDish,
            cookPoint = newCookPoint,
            cookGauge = newGauge
        )
    }.addStatus(Status(hp = modifiedDish.hp, motivation = modifiedDish.motivation)).applyIf(modifiedDish.relation > 0) {
        addRelationAll(modifiedDish.relation)
    }.applyIf(
        (cookStatus.cookPoint < 2000 && newCookPoint >= 2000)
                || (cookStatus.cookPoint < 7000 && newCookPoint >= 7000)
                || (cookStatus.cookPoint < 12000 && newCookPoint >= 12000)
    ) {
        allTrainingLevelUp()
    }
    if (!success) return newState
    val availableEffects = cookSuccessEffects[modifiedDish.phase].filter {
        it.effect.available(this, modifiedDish)
    }
    val firstSelection = availableEffects.filter { it.firstRate > 0 }.associateWith { it.firstRate }
    val firstEffect = randomSelect(firstSelection)
    var effects: List<CookSuccessEffectRate>
    do {
        effects = availableEffects.filter {
            it.effect == firstEffect.effect || Random.nextInt(100) < it.secondRate
        }
    } while (effects.size > 3)
    effects.forEach {
        newState = it.effect.apply(newState, modifiedDish)
    }
    newState = newState.addRandomHint()
    return newState
}

private fun SimulationState.addRandomHint(): SimulationState {
    for (support in support.filter { it.card.skills.isNotEmpty() }.shuffled()) {
        for (skill in support.card.skills.shuffled()) {
            val currentLevel = status.skillHint.getOrElse(skill) { 0 }
            if (currentLevel < 5) {
                val level = support.card.hintLevel + if (support.isScenarioLink) 2 else 1
                return addStatus(Status(skillHint = mapOf(skill to level)))
            }
        }
    }
    // 適性A以上のスキルからランダム選択は未実装
    return this
}

enum class CookMaterial(val displayName: String, val statusType: StatusType) {
    Carrot("ニンジン", StatusType.SPEED),
    Garlic("にんにく", StatusType.STAMINA),
    Potato("じゃがいも", StatusType.POWER),
    HotPepper("唐辛子", StatusType.GUTS),
    Strawberry("いちご", StatusType.WISDOM);

    companion object {
        fun fromStatusType(statusType: StatusType) = statusTypeToCookMaterial[statusType]!!
    }
}

val statusTypeToCookMaterial = CookMaterial.entries.associateBy { it.statusType }

data class CookStamp(
    val material: CookMaterial,
    val fullPower: Boolean,
    val plus: Int,
) {
    override fun toString(): String {
        return buildString {
            if (fullPower) append("全力")
            append(material.displayName)
            if (plus > 0) {
                append("+$plus")
            }
        }
    }
}

data class CookDish(
    val name: String,
    val phase: Int,
    val rank: Int,
    val materials: Map<CookMaterial, Int>,
    val trainingFactor: Int,
    val mainTrainingTarget: StatusType,
    val successName: String = name,
    val raceBonus: Int = 0,
    val hp: Int = 0,
    val motivation: Int = 0,
    val relation: Int = 0,
    val gainPoint: Int = when (phase) {
        0 -> 250
        1 -> 500
        2 -> 800
        else -> 1500
    },
    val trainingTarget: Set<StatusType> = when (phase) {
        0 -> if (mainTrainingTarget == StatusType.STAMINA) {
            setOf(StatusType.SPEED, StatusType.STAMINA, StatusType.GUTS)
        } else {
            setOf(StatusType.SPEED, StatusType.POWER, StatusType.WISDOM)
        }

        3 -> trainingType.toSet()

        else -> setOf(mainTrainingTarget)
    },
    val modified: Boolean = false,
) {
    override fun toString() = buildString {
        append("CookDish(name=")
        append(name)
        append(", trainingFactor=")
        append(trainingFactor)
        append(", trainingTarget=")
        append(trainingTarget)
        if (raceBonus > 0) {
            append(", raceBonus=")
            append(raceBonus)
        }
        if (hp > 0) {
            append(", hp=")
            append(hp)
        }
        if (motivation > 0) {
            append(", motivation=")
            append(motivation)
        }
        if (relation > 0) {
            append(", relation=")
            append(relation)
        }
        append(", gainPoint=")
        append(gainPoint)
        append(") ")
    }

    fun toShortString() = buildString {
        append(name)
        append(":")
        append(trainingTarget.joinToString("/") { it.displayName })
        append(":")
        append(materials.entries.joinToString("/") { "${it.key.displayName}x${it.value}" })
    }
}

val cookDishData = listOf(
    CookDish(
        name = "GⅠプレート",
        phase = 3, rank = 0,
        materials = CookMaterial.entries.associateWith { 100 },
        trainingFactor = 125,
        mainTrainingTarget = StatusType.SPEED,
        raceBonus = 60,
        hp = 25,
        motivation = 1,
    ),
    CookDish(
        name = "GⅠプレート",
        phase = 3, rank = 1,
        materials = CookMaterial.entries.associateWith { 100 },
        trainingFactor = 165,
        mainTrainingTarget = StatusType.SPEED,
        raceBonus = 80,
        hp = 25,
        motivation = 1,
    ),
    CookDish(
        name = "GⅠプレート",
        phase = 3, rank = 2,
        materials = CookMaterial.entries.associateWith { 80 },
        trainingFactor = 165,
        mainTrainingTarget = StatusType.SPEED,
        raceBonus = 80,
        hp = 25,
        motivation = 1,
    ),
    CookDish(
        name = "ゴロゴロ具材のにんじんポトフ", successName = "秘湯：極楽ポトフの湯",
        phase = 2, rank = 0,
        materials = mapOf(CookMaterial.Carrot to 250, CookMaterial.Potato to 80),
        trainingFactor = 70,
        mainTrainingTarget = StatusType.SPEED,
        raceBonus = 25,
        hp = 10,
    ),
    CookDish(
        name = "ゴロゴロ具材のにんじんポトフ", successName = "秘湯：極楽ポトフの湯",
        phase = 2, rank = 1,
        materials = mapOf(CookMaterial.Carrot to 250, CookMaterial.Potato to 80),
        trainingFactor = 80,
        mainTrainingTarget = StatusType.SPEED,
        raceBonus = 30,
        hp = 10,
    ),
    CookDish(
        name = "ゴロゴロ具材のにんじんポトフ", successName = "秘湯：極楽ポトフの湯",
        phase = 2, rank = 2,
        materials = mapOf(CookMaterial.Carrot to 250, CookMaterial.Potato to 80),
        trainingFactor = 90,
        mainTrainingTarget = StatusType.SPEED,
        raceBonus = 35,
        hp = 10,
    ),
    CookDish(
        name = "ドカっと丸ごとにんにくラーメン", successName = "Mt.ニンニク盛ラーメン",
        phase = 2, rank = 0,
        materials = mapOf(CookMaterial.Garlic to 250, CookMaterial.HotPepper to 80),
        trainingFactor = 70,
        mainTrainingTarget = StatusType.STAMINA,
        raceBonus = 25,
        hp = 10,
    ),
    CookDish(
        name = "ドカっと丸ごとにんにくラーメン", successName = "Mt.ニンニク盛ラーメン",
        phase = 2, rank = 1,
        materials = mapOf(CookMaterial.Garlic to 250, CookMaterial.HotPepper to 80),
        trainingFactor = 80,
        mainTrainingTarget = StatusType.STAMINA,
        raceBonus = 30,
        hp = 10,
    ),
    CookDish(
        name = "ドカっと丸ごとにんにくラーメン", successName = "Mt.ニンニク盛ラーメン",
        phase = 2, rank = 2,
        materials = mapOf(CookMaterial.Garlic to 250, CookMaterial.HotPepper to 80),
        trainingFactor = 90,
        mainTrainingTarget = StatusType.STAMINA,
        raceBonus = 35,
        hp = 10,
    ),
    CookDish(
        name = "ホクホクじゃがいもガーリックピザ", successName = "2400m夢の先",
        phase = 2, rank = 0,
        materials = mapOf(CookMaterial.Potato to 250, CookMaterial.Garlic to 80),
        trainingFactor = 70,
        mainTrainingTarget = StatusType.POWER,
        raceBonus = 25,
        hp = 10,
    ),
    CookDish(
        name = "ホクホクじゃがいもガーリックピザ", successName = "2400m夢の先",
        phase = 2, rank = 1,
        materials = mapOf(CookMaterial.Potato to 250, CookMaterial.Garlic to 80),
        trainingFactor = 80,
        mainTrainingTarget = StatusType.POWER,
        raceBonus = 30,
        hp = 10,
    ),
    CookDish(
        name = "ホクホクじゃがいもガーリックピザ", successName = "2400m夢の先",
        phase = 2, rank = 2,
        materials = mapOf(CookMaterial.Potato to 250, CookMaterial.Garlic to 80),
        trainingFactor = 90,
        mainTrainingTarget = StatusType.POWER,
        raceBonus = 35,
        hp = 10,
    ),
    CookDish(
        name = "激辛麻婆じゃがにんじん", successName = "襲来！鬼ヶ島麻婆豆腐",
        phase = 2, rank = 0,
        materials = mapOf(CookMaterial.HotPepper to 250, CookMaterial.Carrot to 40, CookMaterial.Potato to 40),
        trainingFactor = 70,
        mainTrainingTarget = StatusType.GUTS,
        raceBonus = 25,
        hp = 10,
    ),
    CookDish(
        name = "激辛麻婆じゃがにんじん", successName = "襲来！鬼ヶ島麻婆豆腐",
        phase = 2, rank = 1,
        materials = mapOf(CookMaterial.HotPepper to 250, CookMaterial.Carrot to 40, CookMaterial.Potato to 40),
        trainingFactor = 80,
        mainTrainingTarget = StatusType.GUTS,
        raceBonus = 30,
        hp = 10,
    ),
    CookDish(
        name = "激辛麻婆じゃがにんじん", successName = "襲来！鬼ヶ島麻婆豆腐",
        phase = 2, rank = 2,
        materials = mapOf(CookMaterial.HotPepper to 250, CookMaterial.Carrot to 40, CookMaterial.Potato to 40),
        trainingFactor = 90,
        mainTrainingTarget = StatusType.GUTS,
        raceBonus = 35,
        hp = 10,
    ),
    CookDish(
        name = "ダブルいちごキャロットアイス", successName = "キャロット・ニ・アイスノセターノ",
        phase = 2, rank = 0,
        materials = mapOf(CookMaterial.Strawberry to 250, CookMaterial.Carrot to 80),
        trainingFactor = 80,
        mainTrainingTarget = StatusType.WISDOM,
        raceBonus = 25,
    ),
    CookDish(
        name = "ダブルいちごキャロットアイス", successName = "キャロット・ニ・アイスノセターノ",
        phase = 2, rank = 1,
        materials = mapOf(CookMaterial.Strawberry to 250, CookMaterial.Carrot to 80),
        trainingFactor = 90,
        mainTrainingTarget = StatusType.WISDOM,
        raceBonus = 30,
    ),
    CookDish(
        name = "ダブルいちごキャロットアイス", successName = "キャロット・ニ・アイスノセターノ",
        phase = 2, rank = 2,
        materials = mapOf(CookMaterial.Strawberry to 250, CookMaterial.Carrot to 80),
        trainingFactor = 100,
        mainTrainingTarget = StatusType.WISDOM,
        raceBonus = 35,
    ),
    CookDish(
        name = "にんじんじゃがポトフ",
        phase = 1, rank = 0,
        materials = mapOf(CookMaterial.Carrot to 150, CookMaterial.Potato to 80),
        trainingFactor = 40,
        mainTrainingTarget = StatusType.SPEED,
    ),
    CookDish(
        name = "にんじんじゃがポトフ",
        phase = 1, rank = 1,
        materials = mapOf(CookMaterial.Carrot to 150, CookMaterial.Potato to 80),
        trainingFactor = 50,
        mainTrainingTarget = StatusType.SPEED,
    ),
    CookDish(
        name = "にんじんじゃがポトフ",
        phase = 1, rank = 2,
        materials = mapOf(CookMaterial.Carrot to 150, CookMaterial.Potato to 80),
        trainingFactor = 60,
        mainTrainingTarget = StatusType.SPEED,
    ),
    CookDish(
        name = "にんにくラーメン",
        phase = 1, rank = 0,
        materials = mapOf(CookMaterial.Garlic to 150, CookMaterial.HotPepper to 80),
        trainingFactor = 40,
        mainTrainingTarget = StatusType.STAMINA,
    ),
    CookDish(
        name = "にんにくラーメン",
        phase = 1, rank = 1,
        materials = mapOf(CookMaterial.Garlic to 150, CookMaterial.HotPepper to 80),
        trainingFactor = 50,
        mainTrainingTarget = StatusType.STAMINA,
    ),
    CookDish(
        name = "にんにくラーメン",
        phase = 1, rank = 2,
        materials = mapOf(CookMaterial.Garlic to 150, CookMaterial.HotPepper to 80),
        trainingFactor = 60,
        mainTrainingTarget = StatusType.STAMINA,
    ),
    CookDish(
        name = "ポテトガーリックピザ",
        phase = 1, rank = 0,
        materials = mapOf(CookMaterial.Potato to 150, CookMaterial.Garlic to 80),
        trainingFactor = 40,
        mainTrainingTarget = StatusType.POWER,
    ),
    CookDish(
        name = "ポテトガーリックピザ",
        phase = 1, rank = 1,
        materials = mapOf(CookMaterial.Potato to 150, CookMaterial.Garlic to 80),
        trainingFactor = 50,
        mainTrainingTarget = StatusType.POWER,
    ),
    CookDish(
        name = "ポテトガーリックピザ",
        phase = 1, rank = 2,
        materials = mapOf(CookMaterial.Potato to 150, CookMaterial.Garlic to 80),
        trainingFactor = 60,
        mainTrainingTarget = StatusType.POWER,
    ),
    CookDish(
        name = "麻婆じゃがにんじん",
        phase = 1, rank = 0,
        materials = mapOf(CookMaterial.HotPepper to 150, CookMaterial.Carrot to 40, CookMaterial.Potato to 40),
        trainingFactor = 40,
        mainTrainingTarget = StatusType.GUTS,
    ),
    CookDish(
        name = "麻婆じゃがにんじん",
        phase = 1, rank = 1,
        materials = mapOf(CookMaterial.HotPepper to 150, CookMaterial.Carrot to 40, CookMaterial.Potato to 40),
        trainingFactor = 50,
        mainTrainingTarget = StatusType.GUTS,
    ),
    CookDish(
        name = "麻婆じゃがにんじん",
        phase = 1, rank = 2,
        materials = mapOf(CookMaterial.HotPepper to 150, CookMaterial.Carrot to 40, CookMaterial.Potato to 40),
        trainingFactor = 60,
        mainTrainingTarget = StatusType.GUTS,
    ),
    CookDish(
        name = "いちごアイスにんじん風味",
        phase = 1, rank = 0,
        materials = mapOf(CookMaterial.Strawberry to 150, CookMaterial.Carrot to 80),
        trainingFactor = 40,
        mainTrainingTarget = StatusType.WISDOM,
    ),
    CookDish(
        name = "いちごアイスにんじん風味",
        phase = 1, rank = 1,
        materials = mapOf(CookMaterial.Strawberry to 150, CookMaterial.Carrot to 80),
        trainingFactor = 50,
        mainTrainingTarget = StatusType.WISDOM,
    ),
    CookDish(
        name = "いちごアイスにんじん風味",
        phase = 1, rank = 2,
        materials = mapOf(CookMaterial.Strawberry to 150, CookMaterial.Carrot to 80),
        trainingFactor = 60,
        mainTrainingTarget = StatusType.WISDOM,
    ),
    CookDish(
        name = "サンドウィッチ",
        phase = 0, rank = 0,
        materials = mapOf(CookMaterial.Carrot to 25, CookMaterial.Potato to 50, CookMaterial.Strawberry to 50),
        trainingFactor = 25,
        relation = 2,
        mainTrainingTarget = StatusType.POWER,
    ),
    CookDish(
        name = "野菜カレー",
        phase = 0, rank = 0,
        materials = mapOf(CookMaterial.Carrot to 25, CookMaterial.Garlic to 50, CookMaterial.HotPepper to 50),
        trainingFactor = 25,
        relation = 2,
        mainTrainingTarget = StatusType.STAMINA,
    ),
)

sealed class CookSuccessEffect(
    val name: String,
    val available: (state: SimulationState, dish: CookDish) -> Boolean,
    val apply: (state: SimulationState, dish: CookDish) -> SimulationState,
) {
    data object Hp : CookSuccessEffect("体力回復", { _, _ -> true }, { state, _ ->
        state.addStatus(Status(hp = 10))
    })

    data object Motivation : CookSuccessEffect("やる気アップ", { _, _ -> true }, { state, _ ->
        state.addStatus(Status(motivation = 1))
    })

    data object Relation : CookSuccessEffect("絆アップ", { state, _ ->
        state.member.any { !it.guest && it.relation < 100 }
    }, { state, _ ->
        state.addRelationAll(3)
    })

    data object MultipleTraining : CookSuccessEffect("複数トレーニング", { state, dish ->
        if (state.isGoalRaceTurn) {
            false
        } else if (dish.phase == 3) {
            true
        } else {
            state.member.count { it.positions.contains(dish.mainTrainingTarget) } < 5 && state.support.any {
                it.positions.isNotEmpty() && !it.positions.contains(dish.mainTrainingTarget)
            }
        }
    }, { state, dish ->
        if (dish.phase == 3) {
            // GIプレート:各トレーニングに2人
            val availableSupport = state.support.filter { it.positions.isNotEmpty() }
            trainingType.fold(state) { newState, type ->
                val currentCount = state.member.count { it.positions.contains(type) }
                if (currentCount == 5) newState else {
                    val targets = availableSupport.filter { !it.positions.contains(type) }
                        .shuffled().take(min(2, 5 - currentCount))
                        .map { it.index }.toSet()
                    newState.copy(
                        member = newState.member.mapIf({ it.index in targets }) {
                            it.copy(additionalPosition = it.additionalPosition + type)
                        }
                    )
                }
            }
        } else {
            val target = state.support.filter {
                it.positions.isNotEmpty() && !it.positions.contains(dish.mainTrainingTarget)
            }.random()
            state.copy(
                member = state.member.mapIf({ it.index == target.index }) {
                    it.copy(additionalPosition = it.additionalPosition + dish.mainTrainingTarget)
                }
            )
        }
    })

    data object MaxHp : CookSuccessEffect("体力最大値アップ", { state, _ ->
        state.status.maxHp < 120
    }, { state, _ ->
        state.addStatus(Status(maxHp = 4))
    })
}

class CookSuccessEffectRate(
    val effect: CookSuccessEffect,
    val firstRate: Int,
    val secondRate: Int,
)

val cookSuccessEffects = listOf(
    listOf(
        CookSuccessEffectRate(CookSuccessEffect.Hp, 34, 0),
        CookSuccessEffectRate(CookSuccessEffect.Motivation, 33, 0),
        CookSuccessEffectRate(CookSuccessEffect.Relation, 33, 0),
        CookSuccessEffectRate(CookSuccessEffect.MaxHp, 0, 10),
    ),
    listOf(
        CookSuccessEffectRate(CookSuccessEffect.Hp, 30, 10),
        CookSuccessEffectRate(CookSuccessEffect.Motivation, 30, 10),
        CookSuccessEffectRate(CookSuccessEffect.Relation, 0, 30),
        CookSuccessEffectRate(CookSuccessEffect.MultipleTraining, 40, 10),
        CookSuccessEffectRate(CookSuccessEffect.MaxHp, 0, 10),
    ),
    listOf(
        CookSuccessEffectRate(CookSuccessEffect.Hp, 30, 15),
        CookSuccessEffectRate(CookSuccessEffect.Motivation, 20, 15),
        CookSuccessEffectRate(CookSuccessEffect.MultipleTraining, 50, 15),
        CookSuccessEffectRate(CookSuccessEffect.MaxHp, 0, 10),
    ),
    listOf(
        CookSuccessEffectRate(CookSuccessEffect.Motivation, 100, 0),
        CookSuccessEffectRate(CookSuccessEffect.MultipleTraining, 0, 100),
    ),
)

data class CookPointEffect(
    val successRate: Int,
    val trainingFactor: Int,
    val skillPtFactor: Int,
    val specialityRate: Int,
    val fanBonus: Int,
)

/**
 * |0～|0|0|0|0|0|
 * |500～|15|10|15|5|20|
 * |1500～|18|16|24|8|28|
 * |2500～|20|21|33|11|34|
 * |5000～|22|25|42|14|39|
 * |7000～|24|28|51|17|43|
 * |10000～|25|30|60|20|45|
 * |12000～|100|30|60|20|45|
 */
private val cookPointEffects = listOf(
    CookPointEffect(0, 0, 0, 0, 0),
    CookPointEffect(15, 10, 15, 5, 20),
    CookPointEffect(18, 16, 24, 8, 28),
    CookPointEffect(20, 21, 33, 11, 34),
    CookPointEffect(22, 25, 42, 14, 39),
    CookPointEffect(24, 28, 51, 17, 43),
    CookPointEffect(25, 30, 60, 20, 45),
    CookPointEffect(100, 30, 60, 20, 45),
)

data object CookMemberState : ScenarioMemberState(Scenario.COOK)

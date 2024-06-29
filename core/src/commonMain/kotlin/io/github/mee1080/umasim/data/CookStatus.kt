package io.github.mee1080.umasim.data

import io.github.mee1080.umasim.simulation2.ScenarioMemberState

data class CookStatus(
    val gardenPoint: Int = 0,
    val materialLevel: Map<CookMaterial, Int> = CookMaterial.entries.associateWith { 1 },
    val materialCount: Map<CookMaterial, Int> = CookMaterial.entries.associateWith { 0 },
    val cookPoint: Int = 0,
    val cookGauge: Int = 0,
    val dishRank: List<Int> = listOf(0, -1, -1, -1),
    val activatedDish: CookDish? = null,
) {
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

    val availableDishList by lazy {
        cookDishData.filter {
            it.rank == dishRank[it.phase]
        }
    }

    val activatedDishModified by lazy {
        when (activatedDish?.phase) {
            3 -> {
                val level5MaterialCount = materialLevel.count { it.value == 5 }
                activatedDish.copy(
                    trainingFactor = activatedDish.trainingFactor + level5MaterialCount * 5,
                    raceBonus = activatedDish.raceBonus + level5MaterialCount * 5,
                )
            }

            1, 2 -> {
                val level = materialLevel[statusTypeToCookMaterial[activatedDish.mainTrainingTarget]]!!
                activatedDish.copy(
                    trainingFactor = activatedDish.trainingFactor + if (level == 5) 10 else 0,
                    hp = activatedDish.raceBonus + if (level >= 3) 5 else 0,
                )
            }

            else -> activatedDish
        }
    }
}

enum class CookMaterial(val displayName: String, val statusType: StatusType) {
    Carrot("ニンジン", StatusType.SPEED),
    Garlic("にんにく", StatusType.STAMINA),
    Potato("じゃがいも", StatusType.POWER),
    HotPepper("唐辛子", StatusType.GUTS),
    Strawberry("いちご", StatusType.GUTS);
}

private val statusTypeToCookMaterial = CookMaterial.entries.associateBy { it.statusType }

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
    val gainGauge: Int = when (phase) {
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
            append(", motivation=")
            append(relation)
        }
        append(", gainGauge=")
        append(gainGauge)
        append(") ")
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
        raceBonus = 60,
        hp = 25,
        motivation = 1,
    ),
    CookDish(
        name = "GⅠプレート",
        phase = 3, rank = 2,
        materials = CookMaterial.entries.associateWith { 80 },
        trainingFactor = 165,
        mainTrainingTarget = StatusType.SPEED,
        raceBonus = 60,
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
        mainTrainingTarget = StatusType.POWER,
    ),
    CookDish(
        name = "野菜カレー",
        phase = 0, rank = 0,
        materials = mapOf(CookMaterial.Carrot to 25, CookMaterial.Garlic to 50, CookMaterial.HotPepper to 50),
        trainingFactor = 25,
        mainTrainingTarget = StatusType.STAMINA,
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

data object CookMemberState : ScenarioMemberState {
    override fun toString() = "Cook"
}

package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.simulation2.RamenActionParam

enum class RamenTipType(val displayName: String) {
    NOODLE("麺"),
    SOUP("スープ"),
    TOPPING("トッピング"),
    HIDDEN("隠し味")
}

data class RamenBaseEffect(
    val trainingEffect: Int = 0,
    val friendBonus: Int = 0,
    val failureRateDown: Int = 0,
    val relationGauge: Int = 0,
    val statusLimitOver: Int = 0,
    val allHintEvent: Boolean = false,
) {
    companion object {
        val Empty = RamenBaseEffect()
    }
}

val ramenBaseEffect = listOf(
    // ジュニア
    RamenBaseEffect(trainingEffect = 15, failureRateDown = 30, relationGauge = 10),
    // クラシック
    RamenBaseEffect(trainingEffect = 15, friendBonus = 30, failureRateDown = 50, statusLimitOver = 20),
    // シニア
    RamenBaseEffect(
        trainingEffect = 15, friendBonus = 45, failureRateDown = 100,
        statusLimitOver = 40, allHintEvent = true,
    ),
)

enum class RamenRegion(
    val displayName: String,
    val ramenName: String,
    val noodle: Int,
    val soup: Int,
    val topping: Int,
    val targetTypes: List<StatusType>,
    val trainingEffect: Int = 0,
    val skillPtTrainingEffect: Int = 0,
    // TODO: targetTypesのサポカのヒント獲得（ステータスアップあるか要調査）
    val hintCount: Int = 0,
    val friendBonus: Int = 0,
    // TODO: targetTypesのトレーニングで友情ボーナス
    val addMember: Int = 0,
    // TODO: friendBonusとaddMemberが全トレーニング対象になる
    val targetAll: Boolean = false,
) {
    SAPPORO(
        "札幌", "濃厚味噌ラーメン",
        2, 2, 1,
        listOf(StatusType.SPEED),
        trainingEffect = 20,
        hintCount = 1,
    ),

    HAKODATE(
        "函館", "すっきり塩ラーメン",
        1, 2, 2,
        listOf(StatusType.STAMINA),
        trainingEffect = 20,
        hintCount = 1,
    ),

    NIIGATA(
        "新潟", "岩のり背脂ラーメン",
        3, 1, 1,
        listOf(StatusType.POWER),
        trainingEffect = 20,
        hintCount = 1,
    ),

    FUKUSHIMA(
        "福島", "あっさり醤油ラーメン",
        2, 3, 0,
        listOf(StatusType.GUTS),
        trainingEffect = 20,
        hintCount = 1,
    ),

    TOKYO(
        "東京", "ガッツリ豚ラーメン",
        1, 1, 3,
        listOf(StatusType.WISDOM),
        trainingEffect = 20,
        hintCount = 1,
    ),

    NAKAYAMA(
        "中山", "玉ねぎ醤油ラーメン",
        2, 0, 3,
        listOf(StatusType.SPEED),
        friendBonus = 10,
        hintCount = 2,
        addMember = 1,
        targetAll = true,
    ),

    CHUKYO(
        "中京", "ウマ辛名古屋ラーメン",
        3, 2, 0,
        listOf(StatusType.POWER, StatusType.GUTS),
        friendBonus = 50,
        hintCount = 2,
        addMember = 1,
    ),

    KYOTO(
        "京都", "背脂ねぎラーメン",
        0, 3, 2,
        listOf(StatusType.STAMINA, StatusType.GUTS),
        friendBonus = 50,
        hintCount = 2,
        addMember = 1,
    ),

    HANSHIN(
        "阪神", "甘口醤油ラーメン",
        2, 1, 2,
        listOf(StatusType.STAMINA, StatusType.POWER),
        friendBonus = 50,
        hintCount = 2,
        addMember = 1,
    ),

    KOKURA(
        "小倉", "こってり豚骨ラーメン",
        1, 3, 1,
        listOf(StatusType.WISDOM),
        friendBonus = 50,
        hintCount = 2,
        addMember = 1,
    ),

    SAPPORO2(
        "札幌", "濃厚味噌ラーメン",
        2, 2, 1,
        listOf(StatusType.SPEED),
        friendBonus = 50,
        skillPtTrainingEffect = 50,
        addMember = 1,
    ),

    HAKODATE2(
        "函館", "すっきり塩ラーメン",
        1, 2, 2,
        listOf(StatusType.STAMINA),
        friendBonus = 60,
        skillPtTrainingEffect = 50,
        addMember = 1,
    ),

    NIIGATA2(
        "新潟", "岩のり背脂ラーメン",
        3, 1, 1,
        listOf(StatusType.POWER),
        friendBonus = 60,
        skillPtTrainingEffect = 50,
        addMember = 1,
    ),

    FUKUSHIMA2(
        "福島", "あっさり醤油ラーメン",
        2, 3, 0,
        listOf(StatusType.GUTS),
        friendBonus = 60,
        skillPtTrainingEffect = 50,
        addMember = 1,
    ),

    TOKYO2(
        "東京", "ガッツリ豚ラーメン",
        1, 1, 3,
        listOf(StatusType.WISDOM),
        friendBonus = 60,
        skillPtTrainingEffect = 50,
        addMember = 1,
    ),

    NAKAYAMA2(
        "中山", "玉ねぎ醤油ラーメン",
        2, 0, 3,
        listOf(StatusType.SPEED, StatusType.POWER, StatusType.WISDOM),
        friendBonus = 40,
        skillPtTrainingEffect = 50,
        addMember = 1,
    ),

    CHUKYO2(
        "中京", "ウマ辛名古屋ラーメン",
        3, 2, 0,
        listOf(StatusType.SPEED, StatusType.POWER, StatusType.GUTS),
        friendBonus = 40,
        skillPtTrainingEffect = 50,
        addMember = 1,
    ),

    KYOTO2(
        "京都", "背脂ねぎラーメン",
        0, 3, 2,
        listOf(StatusType.SPEED, StatusType.STAMINA, StatusType.WISDOM),
        friendBonus = 40,
        skillPtTrainingEffect = 50,
        addMember = 1,
    ),

    HANSHIN2(
        "阪神", "甘口醤油ラーメン",
        2, 1, 2,
        listOf(StatusType.SPEED, StatusType.STAMINA, StatusType.POWER),
        friendBonus = 40,
        skillPtTrainingEffect = 50,
        addMember = 1,
    ),

    KOKURA2(
        "小倉", "こってり豚骨ラーメン",
        1, 3, 1,
        listOf(StatusType.SPEED, StatusType.GUTS, StatusType.WISDOM),
        friendBonus = 40,
        skillPtTrainingEffect = 50,
        addMember = 1,
    ),

    FINALS1(
        "スペシャル", "スペシャルトレセンラーメン",
        1, 1, 1,
        trainingType.toList(),
        friendBonus = 1000,
        trainingEffect = 1000,
        skillPtTrainingEffect = 1000,
    ),

    FINALS2(
        "よくばり", "よくばりトレセンラーメン",
        1, 1, 1,
        trainingType.toList(),
        friendBonus = 1000,
        trainingEffect = 1000,
        skillPtTrainingEffect = 1000,
    ),

    FINALS3(
        "珠玉", "珠玉のトレセンラーメン",
        1, 1, 1,
        trainingType.toList(),
        friendBonus = 1000,
        trainingEffect = 1000,
        skillPtTrainingEffect = 1000,
    ),
}

val ramenRegionSelection = listOf(
    listOf(RamenRegion.SAPPORO, RamenRegion.HAKODATE, RamenRegion.NIIGATA, RamenRegion.FUKUSHIMA, RamenRegion.TOKYO),
    listOf(RamenRegion.NAKAYAMA, RamenRegion.CHUKYO, RamenRegion.KYOTO, RamenRegion.HANSHIN, RamenRegion.KOKURA),
    listOf(
        RamenRegion.SAPPORO2, RamenRegion.HAKODATE2, RamenRegion.NIIGATA2, RamenRegion.FUKUSHIMA2, RamenRegion.TOKYO2,
        RamenRegion.NAKAYAMA2, RamenRegion.CHUKYO2, RamenRegion.KYOTO2, RamenRegion.HANSHIN2, RamenRegion.KOKURA2,
    ),
    listOf(RamenRegion.FINALS1, RamenRegion.FINALS2, RamenRegion.FINALS3),
)

data class RamenBaseBonus(
    val trainingEffect: Int,
    val friendBonus: Int,
    val specialityRateUp: Int,
    val hintRateUp: Int,
)

fun ramenExcitePtBonus(excitePt: Int): RamenBaseBonus {
    // トレ効果,得意率,ヒント発生率
    return when {
        excitePt < 250 -> RamenBaseBonus(0, 0, 50, 0)
        excitePt < 500 -> RamenBaseBonus(3, 0, 55, 30)
        excitePt < 1000 -> RamenBaseBonus(5, 0, 60, 40)
        excitePt < 1500 -> RamenBaseBonus(8, 0, 63, 50)
        excitePt < 2000 -> RamenBaseBonus(10, 0, 65, 60)
        excitePt < 2500 -> RamenBaseBonus(12, 0, 68, 70)
        excitePt < 3000 -> RamenBaseBonus(14, 0, 70, 80)
        excitePt < 3500 -> RamenBaseBonus(16, 0, 73, 90)
        excitePt < 4000 -> RamenBaseBonus(18, 0, 75, 100)
        excitePt < 5000 -> RamenBaseBonus(20, 0, 78, 110)
        else -> RamenBaseBonus(20, 0, 80, 120)
    }
}

// 友情,得意率,ヒント発生率
val ramenRmjBonus = listOf(
    // ジュニア12月
    listOf(
        RamenBaseBonus(0, 3, 30, 15),
        RamenBaseBonus(0, 5, 80, 30),
    ),
    // クラシック12月
    listOf(
        RamenBaseBonus(0, 5, 60, 30),
        RamenBaseBonus(0, 10, 120, 75),
    ),
    // シニア12月
    listOf(
        RamenBaseBonus(0, 15, 150, 75),
        RamenBaseBonus(0, 25, 250, 125),
    ),
)

val ramenTargetExcitePt = listOf(1500, 3000, 3500, 9999)

val ramenGainExcitePt = listOf(30, 40, 50, 0)

val ramenRegionRankBonus = listOf(0, 3, 5, 7, 9, 10)

private val ramenGaugeBaseData = mapOf(
    listOf(8, 6, 1) to listOf(5, 4, 1),
    listOf(7, 7, 1) to listOf(5, 4, 1),
    listOf(8, 5, 2) to listOf(5, 3, 2),
    listOf(7, 6, 2) to listOf(5, 4, 1),
    listOf(9, 3, 3) to listOf(6, 2, 2),
    listOf(8, 4, 3) to listOf(5, 3, 2),
    listOf(7, 5, 3) to listOf(5, 3, 2),
    listOf(6, 6, 3) to listOf(4, 4, 2),
    listOf(6, 5, 4) to listOf(4, 3, 3),
    listOf(7, 4, 4) to listOf(5, 3, 2),
    listOf(5, 5, 5) to listOf(4, 3, 3),
)

fun ramenGaugeBase(selectedRegions: List<RamenRegion>): RamenActionParam {
    val totalNoodle = selectedRegions.sumOf { it.noodle }
    val totalSoup = selectedRegions.sumOf { it.soup }
    val totalTopping = selectedRegions.sumOf { it.topping }
    val gaugeList = ramenGaugeBaseData[listOf(totalNoodle, totalSoup, totalTopping).sortedDescending()]!!
    val gaugeMap =
        listOf(RamenTipType.NOODLE to totalNoodle, RamenTipType.SOUP to totalSoup, RamenTipType.TOPPING to totalTopping)
            .sortedByDescending { it.second * 10 + it.first.ordinal }
            .mapIndexed { index, (type, _) -> type to gaugeList[index] }
            .associate { it }
    return RamenActionParam(
        noodleGauge = gaugeMap[RamenTipType.NOODLE]!!,
        soupGauge = gaugeMap[RamenTipType.SOUP]!!,
        toppingGauge = gaugeMap[RamenTipType.TOPPING]!!,
    )
}

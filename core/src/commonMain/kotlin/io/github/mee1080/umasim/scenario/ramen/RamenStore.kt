package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType

enum class RamenTipType(val displayName: String) {
    NOODLE("麺"),
    SOUP("スープ"),
    TOPPING("トッピング"),
    HIDDEN("隠し味")
}

data class RamenBaseEffect(
    // TODO: トレ効果
    val trainingEffect: Int,
    // TODO: 友情ボーナス
    val friendBonus: Int = 0,
    // TODO: 失敗率ダウン
    val failureRateDown: Int,
    // TODO: 絆ゲージ
    val relationGauge: Int = 0,
    // TODO: 獲得上限（基礎能力・SP共通）
    val statusLimitOver: Int = 0,
    // TODO: 全サポカヒントアイコン＆全ヒントイベント発生
    val allHintEvent: Boolean = false,
)

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
    // TODO: targetTypesのトレーニングでトレ効果
    val trainingEffect: Int = 0,
    // TODO: targetTypesのトレーニングでスキルPtのトレ効果
    val skillPtTrainingEffect: Int = 0,
    // TODO: targetTypesのサポカのヒント獲得（ステータスアップあるか要調査）
    val hintCount: Int = 0,
    // TODO: targetTypesのトレーニングで友情ボーナス
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
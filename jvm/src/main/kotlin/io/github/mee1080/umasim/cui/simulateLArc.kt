package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.LArcActionSelector
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.Runner

fun simulateLarc() {
    speed3Stamina1Wisdom1Long()
//    speed3Power1Wisdom1Middle()
//    speed2Guts2Wisdom1Mile()
}

private fun speed3Stamina1Wisdom1Long() {
    doSimulation2(
        Scenario.LARC,
        Store.getChara("[うららん一等賞♪]ハルウララ", 5, 5),
        Store.getSupportByName(
            "[大望は飛んでいく]エルコンドルパサー",
            "[The frontier]ジャングルポケット",
//            "[迫る熱に押されて]キタサンブラック",
            "[ハネ退け魔を退け願い込め]スペシャルウィーク",
            "[君と見る泡沫]マンハッタンカフェ",
            "[L'aubeは迫りて]佐岳メイ",
        ).toTypedArray(),
//        targetStatus = StatusType.SPEED, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[感謝感謝！サクラ吹雪！！]サクラバクシンオー" to it }.toTypedArray())),
//        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
        factor = factor(StatusType.STAMINA, 6),
        testCount = 100000,
        selector = LArcActionSelector.speed3Stamina1Wisdom1Long,
        evaluateSetting = Runner.lArcLongEvaluateSetting,
    )
//    doSimulation2(
//        Scenario.LARC,
//        Store.getChara("[うららん一等賞♪]ハルウララ", 5, 5),
//        Store.getSupportByName(
//            "[大望は飛んでいく]エルコンドルパサー",
//            "[The frontier]ジャングルポケット",
//            "[迫る熱に押されて]キタサンブラック",
////            "[一粒の安らぎ]スーパークリーク",
//            "[君と見る泡沫]マンハッタンカフェ",
//            "[L'aubeは迫りて]佐岳メイ",
//        ).toTypedArray(),
////        targetStatus = StatusType.STAMINA, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[白い翼は舞い戻りて]ホッコータルマエ" to it }.toTypedArray())),
////        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
//        factor = factor(StatusType.STAMINA, 6),
//        testCount = 100000,
//        selector = LArcActionSelector.speed3Stamina1Wisdom1Long,
//        evaluateSetting = Runner.lArcLongEvaluateSetting,
//    )
}

private fun speed3Power1Wisdom1Middle() {
    doSimulation2(
        Scenario.LARC,
        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
        Store.getSupportByName(
            "[大望は飛んでいく]エルコンドルパサー",
            "[The frontier]ジャングルポケット",
            "[迫る熱に押されて]キタサンブラック",
//            "[やったれハロウィンナイト！]タマモクロス",
            "[燦爛]メジロラモーヌ",
            "[L'aubeは迫りて]佐岳メイ",
        ).toTypedArray(),
//        targetStatus = StatusType.POWER, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[冬溶かす熾火]メジロラモーヌ" to it }.toTypedArray())),
//        Store.getSupportByName("[ロード・オブ・ウオッカ]ウオッカ" to 4),
        factor = factor(StatusType.STAMINA, 4) + factor(StatusType.GUTS, 2),
        testCount = 100000,
        selector = LArcActionSelector.speed3Power1Wisdom1Middle,
        evaluateSetting = Runner.lArcMiddleEvaluateSetting,
    )
}

private fun speed2Guts2Wisdom1Mile() {
    doSimulation2(
        Scenario.LARC,
        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
        Store.getSupportByName(
            "[大望は飛んでいく]エルコンドルパサー",
            "[The frontier]ジャングルポケット",
            "[うらら～な休日]ハルウララ",
//            "[優しい月]ゴールドシチー",
            "[燦爛]メジロラモーヌ",
            "[L'aubeは迫りて]佐岳メイ",
        ).toTypedArray(),
        targetStatus = StatusType.GUTS, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[2人のバウンス・シャッセ]キタサンブラック" to it }.toTypedArray())),
//        Store.getSupportByName("[ロード・オブ・ウオッカ]ウオッカ" to 4),
        factor = factor(StatusType.SPEED, 2) + factor(StatusType.STAMINA, 2) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = LArcActionSelector.speed2Guts2Wisdom1Mile,
        evaluateSetting = Runner.lArcMileEvaluateSetting,
    )
//    doSimulation2(
//        Scenario.LARC,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            "[大望は飛んでいく]エルコンドルパサー",
//            "[The frontier]ジャングルポケット",
//            "[うらら～な休日]ハルウララ",
//            "[優しい月]ゴールドシチー",
////            "[燦爛]メジロラモーヌ",
//            "[L'aubeは迫りて]佐岳メイ",
//        ).toTypedArray(),
//        targetStatus = StatusType.WISDOM, rarity = 2..3, talent = 0..4,
////        Store.getSupportByName(*((0..4).map { "[我が舞、巡りて]キングヘイロー" to it }.toTypedArray())),
////        Store.getSupportByName("[ロード・オブ・ウオッカ]ウオッカ" to 4),
//        factor = factor(StatusType.SPEED, 2) + factor(StatusType.STAMINA, 2) + factor(StatusType.POWER, 2),
//        testCount = 100000,
//        selector = LArcActionSelector.speed2Guts2Wisdom1Mile,
//        evaluateSetting = Runner.lArcMileEvaluateSetting,
//    )
}
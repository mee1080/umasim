package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.UafActionSelector
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.Runner

fun simulateUaf() {
//    speed2Power1Guts1Wisdom1Mile()
    speed2Stamina1Guts1Wisdom1Long()
}

private fun speed2Power1Guts1Wisdom1Mile() {
//    doSimulation2(
//        Scenario.UAF,
//        Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5),
//        Store.getSupportByName(
////            "[血脈の胎動]ドゥラメンテ",
//            "[大望は飛んでいく]エルコンドルパサー",
//            "[冬溶かす熾火]メジロラモーヌ",
//            "[只、君臨す。]オルフェーヴル",
//            "[かっとばせー！ですわ！？]メジロマックイーン",
//            "[共に描くキラメキ]都留岐涼花",
//        ).toTypedArray(),
//        targetStatus = StatusType.SPEED, rarity = 3..3, talent = 0..3,
////        Store.getSupportByName(*((0..4).map { "[浚いの風]ヤマニンゼファー" to it }.toTypedArray())),
////        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
//        factor = factor(StatusType.SPEED, 1) + factor(StatusType.STAMINA, 1) + factor(StatusType.POWER, 4),
//        testCount = 100000,
//        selector = UafActionSelector.speed2Power1Guts1Wisdom1Mile,
//        evaluateSetting = Runner.uafMileEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.UAF,
//        Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5),
//        Store.getSupportByName(
//            "[血脈の胎動]ドゥラメンテ",
//            "[大望は飛んでいく]エルコンドルパサー",
////            "[冬溶かす熾火]メジロラモーヌ",
//            "[只、君臨す。]オルフェーヴル",
//            "[かっとばせー！ですわ！？]メジロマックイーン",
//            "[共に描くキラメキ]都留岐涼花",
//        ).toTypedArray(),
////        targetStatus = StatusType.POWER, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[白い鳥のアラベスク]ケイエスミラクル" to it }.toTypedArray())),
////        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
//        factor = factor(StatusType.SPEED, 1) + factor(StatusType.STAMINA, 1) + factor(StatusType.POWER, 4),
//        testCount = 100000,
//        selector = UafActionSelector.speed2Power1Guts1Wisdom1Mile,
//        evaluateSetting = Runner.uafMileEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.UAF,
//        Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5),
//        Store.getSupportByName(
//            "[血脈の胎動]ドゥラメンテ",
//            "[大望は飛んでいく]エルコンドルパサー",
//            "[冬溶かす熾火]メジロラモーヌ",
////            "[只、君臨す。]オルフェーヴル",
//            "[かっとばせー！ですわ！？]メジロマックイーン",
//            "[共に描くキラメキ]都留岐涼花",
//        ).toTypedArray(),
////        targetStatus = StatusType.GUTS, rarity = 2..2, talent = 0..3,
//        Store.getSupportByName(*((0..4).map { "[明日のライド・オン]ウオッカ" to it }.toTypedArray())),
////        Store.getSupportByName("[明日のライド・オン]ウオッカ" to 4),
//        factor = factor(StatusType.SPEED, 1) + factor(StatusType.STAMINA, 1) + factor(StatusType.POWER, 4),
//        testCount = 100000,
//        selector = UafActionSelector.speed2Power1Guts1Wisdom1Mile,
//        evaluateSetting = Runner.uafMileEvaluateSetting,
//    )
    doSimulation2(
        Scenario.UAF,
        Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5),
        Store.getSupportByName(
            "[血脈の胎動]ドゥラメンテ",
            "[大望は飛んでいく]エルコンドルパサー",
            "[冬溶かす熾火]メジロラモーヌ",
            "[只、君臨す。]オルフェーヴル",
//            "[かっとばせー！ですわ！？]メジロマックイーン",
            "[共に描くキラメキ]都留岐涼花",
        ).toTypedArray(),
//        targetStatus = StatusType.WISDOM, rarity = 2..2, talent = 0..3,
        Store.getSupportByName(*((0..4).map { "[ぬりぬりシェイプアップ！]ダンツフレーム" to it }.toTypedArray())),
//        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
        factor = factor(StatusType.SPEED, 1) + factor(StatusType.STAMINA, 1) + factor(StatusType.POWER, 4),
        testCount = 100000,
        selector = UafActionSelector.speed2Power1Guts1Wisdom1Mile,
        evaluateSetting = Runner.uafMileEvaluateSetting,
    )
}

private fun speed2Stamina1Guts1Wisdom1Long() {
    doSimulation2(
        Scenario.UAF,
        Store.getChara("[清らに星澄むスノーロリィタ]メジロブライト", 5, 5),
        Store.getSupportByName(
            "[血脈の胎動]ドゥラメンテ",
            "[大望は飛んでいく]エルコンドルパサー",
//            "[大地と我らのアンサンブル]サウンズオブアース",
            "[只、君臨す。]オルフェーヴル",
            "[君と見る泡沫]マンハッタンカフェ",
            "[共に描くキラメキ]都留岐涼花",
        ).toTypedArray(),
//        targetStatus = StatusType.STAMINA, rarity = 2..3, talent = 0..1,
        Store.getSupportByName(*((0..4).map { "[そして幕は上がる]ダンツフレーム" to it }.toTypedArray())),
//        Store.getSupportByName("[大地と我らのアンサンブル]サウンズオブアース" to 4),
        factor = factor(StatusType.POWER, 4) + factor(StatusType.STAMINA, 2),
        testCount = 100000,
        selector = UafActionSelector.speed2Stamina1Guts1Wisdom1Long,
        evaluateSetting = Runner.uafLongEvaluateSetting,
    )
    doSimulation2(
        Scenario.UAF,
        Store.getChara("[清らに星澄むスノーロリィタ]メジロブライト", 5, 5),
        Store.getSupportByName(
            "[血脈の胎動]ドゥラメンテ",
            "[大望は飛んでいく]エルコンドルパサー",
//            "[大地と我らのアンサンブル]サウンズオブアース",
            "[只、君臨す。]オルフェーヴル",
            "[君と見る泡沫]マンハッタンカフェ",
            "[共に描くキラメキ]都留岐涼花",
        ).toTypedArray(),
//        targetStatus = StatusType.STAMINA, rarity = 2..3, talent = 0..1,
        Store.getSupportByName(*((0..4).map { "[一粒の安らぎ]スーパークリーク" to it }.toTypedArray())),
//        Store.getSupportByName("[大地と我らのアンサンブル]サウンズオブアース" to 4),
        factor = factor(StatusType.POWER, 4) + factor(StatusType.STAMINA, 2),
        testCount = 100000,
        selector = UafActionSelector.speed2Stamina1Guts1Wisdom1Long,
        evaluateSetting = Runner.uafLongEvaluateSetting,
    )
}
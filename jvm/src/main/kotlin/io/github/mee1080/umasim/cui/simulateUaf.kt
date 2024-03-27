package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.UafActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.Runner

fun simulateUaf() {
    speed2Power1Guts1Wisdom1Mile()
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
////        targetStatus = StatusType.SPEED, rarity = 2..3, talent = 0..4,
////        Store.getSupportByName(*((0..4).map { "[感謝感謝！サクラ吹雪！！]サクラバクシンオー" to it }.toTypedArray())),
//        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
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
//            "[冬溶かす熾火]メジロラモーヌ",
            "[只、君臨す。]オルフェーヴル",
            "[かっとばせー！ですわ！？]メジロマックイーン",
            "[共に描くキラメキ]都留岐涼花",
        ).toTypedArray(),
        targetStatus = StatusType.POWER, rarity = 3..3, talent = 0..3,
//        Store.getSupportByName(*((0..4).map { "[感謝感謝！サクラ吹雪！！]サクラバクシンオー" to it }.toTypedArray())),
//        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
        factor = factor(StatusType.SPEED, 1) + factor(StatusType.STAMINA, 1) + factor(StatusType.POWER, 4),
        testCount = 100000,
        selector = UafActionSelector.speed2Power1Guts1Wisdom1Mile,
        evaluateSetting = Runner.uafMileEvaluateSetting,
    )
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
//        targetStatus = StatusType.GUTS, rarity = 3..3, talent = 4..4,
////        Store.getSupportByName(*((0..4).map { "[感謝感謝！サクラ吹雪！！]サクラバクシンオー" to it }.toTypedArray())),
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
//            "[只、君臨す。]オルフェーヴル",
////            "[かっとばせー！ですわ！？]メジロマックイーン",
//            "[共に描くキラメキ]都留岐涼花",
//        ).toTypedArray(),
//        targetStatus = StatusType.WISDOM, rarity = 3..3, talent = 4..4,
////        Store.getSupportByName(*((0..4).map { "[感謝感謝！サクラ吹雪！！]サクラバクシンオー" to it }.toTypedArray())),
////        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
//        factor = factor(StatusType.SPEED, 1) + factor(StatusType.STAMINA, 1) + factor(StatusType.POWER, 4),
//        testCount = 100000,
//        selector = UafActionSelector.speed2Power1Guts1Wisdom1Mile,
//        evaluateSetting = Runner.uafMileEvaluateSetting,
//    )
}

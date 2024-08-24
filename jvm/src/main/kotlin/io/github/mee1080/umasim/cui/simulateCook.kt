package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.CookActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.Runner

fun simulateCook() {
    speed1Power1Guts2Wisdom1Short()
}

private fun speed1Power1Guts2Wisdom1Short() {
    var defaultSupport = Store.getSupportByName(
//        "[アルストロメリアの夢]ヴィブロス",
        "[朝焼け苺の畑にて]ニシノフラワー",
        "[うらら～な休日]ハルウララ",
        "[只、君臨す。]オルフェーヴル",
        "[Take Them Down!]ナリタタイシン",
        "[謹製ッ！特大夢にんじん！]秋川理事長",
    ).toTypedArray()
    var targetStatus = StatusType.SPEED
    doSimulation2(
        Scenario.COOK,
        Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5),
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[Devilish Whispers]スティルインラブ" to it }.toTypedArray())),
//        Store.getSupportByName("[アルストロメリアの夢]ヴィブロス" to 4),
        factor = factor(StatusType.SPEED, 6),
        testCount = 100000,
        selector = CookActionSelector.speed1Power1Guts2Wisdom1Short,
        evaluateSetting = Runner.cookShortEvaluateSetting,
    )
}

private fun speed2Stamina1Guts1Wisdom1Long() {
//    doSimulation2(
//        Scenario.UAF,
//        Store.getChara("[清らに星澄むスノーロリィタ]メジロブライト", 5, 5),
//        Store.getSupportByName(
//            "[血脈の胎動]ドゥラメンテ",
//            "[大望は飛んでいく]エルコンドルパサー",
////            "[大地と我らのアンサンブル]サウンズオブアース",
//            "[只、君臨す。]オルフェーヴル",
//            "[君と見る泡沫]マンハッタンカフェ",
//            "[共に描くキラメキ]都留岐涼花",
//        ).toTypedArray(),
//        targetStatus = StatusType.STAMINA, rarity = 2..3, talent = 0..1,
////        Store.getSupportByName(*((0..4).map { "[わーく・あ・みらくる！]ヒシミラクル" to it }.toTypedArray())),
////        Store.getSupportByName("[大地と我らのアンサンブル]サウンズオブアース" to 4),
//        factor = factor(StatusType.POWER, 4) + factor(StatusType.STAMINA, 2),
//        testCount = 100000,
//        selector = UafActionSelector.speed2Stamina1Guts1Wisdom1Long,
//        evaluateSetting = Runner.uafLongEvaluateSetting,
//    )
}
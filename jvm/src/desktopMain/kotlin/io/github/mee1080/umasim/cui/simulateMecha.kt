package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.MechaActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Runner

fun simulateMecha() {
    speed2Stamina2Power1Wisdom1()
}

private fun speed2Stamina2Power1Wisdom1() {
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    var defaultSupport = Store.getSupportByName(
        "[Devilish Whispers]スティルインラブ",
        "[大望は飛んでいく]エルコンドルパサー",
        "[Cocoon]エアシャカール",
        "[そして幕は上がる]ダンツフレーム",
        "[COOL⇔CRAZY/Buddy]シンボリクリスエス",
//        "[Take Them Down!]ナリタタイシン",
    ).toTypedArray()
    var targetStatus = StatusType.SPEED
    doSimulation2(
        Scenario.MECHA,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[雲煙飛動]シンボリルドルフ" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 6),
        testCount = 100000,
        selector = MechaActionSelector.s2h2p1w1,
        evaluateSetting = Runner.mechaSetting,
        evaluateUpperRate = 0.2,
    )
    defaultSupport = Store.getSupportByName(
        "[Devilish Whispers]スティルインラブ",
        "[大望は飛んでいく]エルコンドルパサー",
        "[Cocoon]エアシャカール",
        "[そして幕は上がる]ダンツフレーム",
//        "[COOL⇔CRAZY/Buddy]シンボリクリスエス",
        "[Take Them Down!]ナリタタイシン",
    ).toTypedArray()
    targetStatus = StatusType.POWER
    doSimulation2(
        Scenario.MECHA,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[深窓の少女へ]メジロアルダン" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 6),
        testCount = 100000,
        selector = MechaActionSelector.s2h2p1w1,
        evaluateSetting = Runner.mechaSetting,
        evaluateUpperRate = 0.2,
    )
}

package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.OnsenActionSelector
import io.github.mee1080.umasim.ai.OnsenActionSelector2
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Runner

fun simulateOnsen() {
//    speed2power1guts1Wisdom1Speed()
//    speed2stamina1power1Wisdom1Stamina()
//    speed2power1guts1Wisdom1Power()
//    speed2power1guts1Wisdom1Guts()
    speed2power1guts1Wisdom1Wisdom()
}

private fun speed2power1guts1Wisdom1Speed() {
    val scenario = Scenario.ONSEN
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
//        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Unveiled Dream]ラインクラフト" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[繋がれパレード・ノーツ♪]トランセンド" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[ゆるり、ゆこま旅館]保科健子" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.SPEED
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[世界を変える眼差し]アーモンドアイ" to it }.toTypedArray())),
//        Store.getSupportByName("[世界を変える眼差し]アーモンドアイ" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = OnsenActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.onsenSetting,
        evaluateUpperRate = 0.2,
    )
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[世界を変える眼差し]アーモンドアイ" to it }.toTypedArray())),
//        Store.getSupportByName("[世界を変える眼差し]アーモンドアイ" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = OnsenActionSelector2.Option()::generateSelector,
        evaluateSetting = Runner.onsenSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed2stamina1power1Wisdom1Stamina() {
    val scenario = Scenario.ONSEN
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Unveiled Dream]ラインクラフト" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
//        "[繋がれパレード・ノーツ♪]トランセンド" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[ゆるり、ゆこま旅館]保科健子" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.STAMINA
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[Cocoon]エアシャカール" to it }.toTypedArray())),
//        Store.getSupportByName("[世界を変える眼差し]アーモンドアイ" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = OnsenActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.onsenSetting,
        evaluateUpperRate = 0.2,
    )
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[Cocoon]エアシャカール" to it }.toTypedArray())),
//        Store.getSupportByName("[世界を変える眼差し]アーモンドアイ" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = OnsenActionSelector2.Option()::generateSelector,
        evaluateSetting = Runner.onsenSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed2power1guts1Wisdom1Power() {
    val scenario = Scenario.ONSEN
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Unveiled Dream]ラインクラフト" to 4,
//        "[白き稲妻の如く]タマモクロス" to 4,
        "[繋がれパレード・ノーツ♪]トランセンド" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[ゆるり、ゆこま旅館]保科健子" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.POWER
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[All-Out Brilliance]トーセンジョーダン" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = OnsenActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.onsenSetting,
        evaluateUpperRate = 0.2,
    )
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[All-Out Brilliance]トーセンジョーダン" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = OnsenActionSelector2.Option()::generateSelector,
        evaluateSetting = Runner.onsenSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed2power1guts1Wisdom1Guts() {
    val scenario = Scenario.ONSEN
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Unveiled Dream]ラインクラフト" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
//        "[繋がれパレード・ノーツ♪]トランセンド" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[ゆるり、ゆこま旅館]保科健子" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.GUTS
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[壇上より魔法を込めて]フジキセキ" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = OnsenActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.onsenSetting,
        evaluateUpperRate = 0.2,
    )
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[壇上より魔法を込めて]フジキセキ" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = OnsenActionSelector2.Option()::generateSelector,
        evaluateSetting = Runner.onsenSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed2power1guts1Wisdom1Wisdom() {
    val scenario = Scenario.ONSEN
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Unveiled Dream]ラインクラフト" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[繋がれパレード・ノーツ♪]トランセンド" to 4,
//        "[Take Them Down!]ナリタタイシン" to 4,
        "[ゆるり、ゆこま旅館]保科健子" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.WISDOM
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[今宵、我が君のために]デュランダル" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = OnsenActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.onsenSetting,
        evaluateUpperRate = 0.2,
    )
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[今宵、我が君のために]デュランダル" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = OnsenActionSelector2.Option()::generateSelector,
        evaluateSetting = Runner.onsenSetting,
        evaluateUpperRate = 0.2,
    )
}

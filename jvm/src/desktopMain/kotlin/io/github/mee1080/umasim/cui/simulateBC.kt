package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.BCActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.bc.BCRoute
import io.github.mee1080.umasim.scenario.bc.BCScenarioEvents
import io.github.mee1080.umasim.simulation2.Runner

fun simulateBC() {
    speed2power1guts1Wisdom1Speed()
//    speed2stamina1power1Wisdom1Stamina()
//    speed2power1guts1Wisdom1Power()
//    speed2power1guts1Wisdom1Guts()
//    speed2power1guts1Wisdom1Wisdom()
//    speed2power1guts1Wisdom1Friend()
}

private fun speed2power1guts1Wisdom1Speed() {
    val scenario = Scenario.BC
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
//        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Unveiled Dream]ラインクラフト" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[繋がれパレード・ノーツ♪]トランセンド" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[激録！爆走トナカイ事件]ゴールドシップ" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.SPEED
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[世界を変える眼差し]アーモンドアイ" to it }.toTypedArray())),
//        Store.getSupportByName("[世界を変える眼差し]アーモンドアイ" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 1,
        selector = BCActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.bcSetting,
        evaluateUpperRate = 1.0,
        scenarioEvents = { BCScenarioEvents(BCRoute.Mile) },
    )
}

private fun speed2stamina1power1Wisdom1Stamina() {
    val scenario = Scenario.BC
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
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[Cocoon]エアシャカール" to it }.toTypedArray())),
//        Store.getSupportByName("[Cocoon]エアシャカール" to 4),
        factor = factor(StatusType.SPEED, 2) + factor(StatusType.GUTS, 4),
        testCount = 100000,
        selector = BCActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.bcSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed2power1guts1Wisdom1Power() {
    val scenario = Scenario.BC
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
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[星跨ぐメッセージ]ネオユニヴァース" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = BCActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.bcSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed2power1guts1Wisdom1Guts() {
    val scenario = Scenario.BC
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
        selector = BCActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.bcSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed2power1guts1Wisdom1Wisdom() {
    val scenario = Scenario.BC
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
//        Store.getSupportByName("[ミッション『心の栄養補給』]ミホノブルボン" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = BCActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.bcSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed2power1guts1Wisdom1Friend() {
    val scenario = Scenario.BC
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Unveiled Dream]ラインクラフト" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[繋がれパレード・ノーツ♪]トランセンド" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
//        "[ゆるり、ゆこま旅館]保科健子" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.WISDOM
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[ゆるり、ゆこま旅館]保科健子" to it }.toTypedArray())),
//        Store.getSupportByName("[ミッション『心の栄養補給』]ミホノブルボン" to 4),
        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
        testCount = 100000,
        selector = BCActionSelector.Option()::generateSelector,
        evaluateSetting = Runner.bcSetting,
        evaluateUpperRate = 0.2,
    )
}

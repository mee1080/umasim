package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.MujintoActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Runner

fun simulateMujinto() {
    speed1Stamina1power1guts1Wisdom1Speed()
//    speed1Stamina1power1guts1Wisdom1Stamina()
//    speed1Stamina1power1guts1Wisdom1Power()
//    speed1Stamina1power1guts1Wisdom1Guts()
    speed1Stamina1power1guts1Wisdom1Wisdom()
}

private fun speed1Stamina1power1guts1Wisdom1Speed() {
    val scenario = Scenario.MUJINTO
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
//        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Cocoon]エアシャカール" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.SPEED
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[カルストンライトオ、猫です]カルストンライトオ" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 6),
        testCount = 100000,
        selector = MujintoActionSelector.defaultOption::generateSelector,
        evaluateSetting = Runner.mujintoSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed1Stamina1power1guts1Wisdom1Stamina() {
    val scenario = Scenario.MUJINTO
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ" to 4,
//        "[Cocoon]エアシャカール" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.STAMINA
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[世界を変える眼差し]アーモンドアイ" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 6),
        testCount = 100000,
        selector = MujintoActionSelector.defaultOption::generateSelector,
        evaluateSetting = Runner.mujintoSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed1Stamina1power1guts1Wisdom1Power() {
    val scenario = Scenario.MUJINTO
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Cocoon]エアシャカール" to 4,
//        "[白き稲妻の如く]タマモクロス" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.POWER
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[世界を変える眼差し]アーモンドアイ" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 6),
        testCount = 100000,
        selector = MujintoActionSelector.defaultOption::generateSelector,
        evaluateSetting = Runner.mujintoSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed1Stamina1power1guts1Wisdom1Guts() {
    val scenario = Scenario.MUJINTO
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Cocoon]エアシャカール" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
//        "[只、君臨す。]オルフェーヴル" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.GUTS
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[気まぐれ渡り星]ステイゴールド" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 6),
        testCount = 100000,
        selector = MujintoActionSelector.defaultOption::generateSelector,
        evaluateSetting = Runner.mujintoSetting,
        evaluateUpperRate = 0.2,
    )
}

private fun speed1Stamina1power1guts1Wisdom1Wisdom() {
    val scenario = Scenario.MUJINTO
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Cocoon]エアシャカール" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
//        "[Take Them Down!]ナリタタイシン" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
    ).toTypedArray()
    val targetStatus = StatusType.WISDOM
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[今宵、我が君のために]デュランダル" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 6),
        testCount = 100000,
        selector = MujintoActionSelector.defaultOption::generateSelector,
        evaluateSetting = Runner.mujintoSetting,
        evaluateUpperRate = 0.2,
    )
}

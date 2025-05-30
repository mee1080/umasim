package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.LegendActionSelector
import io.github.mee1080.umasim.ai.generator
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.legend.LegendScenarioEvents
import io.github.mee1080.umasim.scenario.legend.getLegendBuff
import io.github.mee1080.umasim.simulation2.Runner

fun simulateLegend() {
    speed2Stamina2Wisdom1Speed()
//    speed2Stamina2Wisdom1Stamina()
    speed2Stamina2Wisdom1Wisdom()
//    speed2Power1Guts1Wisdom1Power()
//    speed3Guts1Wisdom1Guts()
//    speed2Stamina1Guts1Wisdom1Guts()
}

private fun speed2Stamina2Wisdom1Speed() {
    val scenario = Scenario.LEGEND
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
//        "[世界を変える眼差し]アーモンドアイ",
        "[Devilish Whispers]スティルインラブ",
        "[Cocoon]エアシャカール",
        "[そして幕は上がる]ダンツフレーム",
        "[Take Them Down!]ナリタタイシン",
        "[導きの光]伝説の体現者",
    ).toTypedArray()
    val targetStatus = StatusType.SPEED
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[Unveiled Dream]ラインクラフト" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 2) + factor(StatusType.POWER, 1) + factor(StatusType.STAMINA, 3),
        testCount = 100000,
        selector = LegendActionSelector.s2h2w1.generator()::generateSelector,
        evaluateSetting = Runner.legendSetting,
        evaluateUpperRate = 0.2,
        scenarioEvents = {
            LegendScenarioEvents(
                listOf(
                    "トーク術", "交渉術", "素敵なハーモニー", "極限の集中",
                    "絆が奏でるハーモニー", "怪物チャンスマイル♪", "絆が織りなす光", "集いし理想",
                    "高潔なる魂", "百折不撓", "飽くなき挑戦心",
                ).map { getLegendBuff(it) }
            )
        },
    )
}

private fun speed2Stamina2Wisdom1Stamina() {
    val scenario = Scenario.LEGEND
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ",
        "[Devilish Whispers]スティルインラブ",
        "[Cocoon]エアシャカール",
//        "[そして幕は上がる]ダンツフレーム",
        "[Take Them Down!]ナリタタイシン",
        "[導きの光]伝説の体現者",
    ).toTypedArray()
    val targetStatus = StatusType.STAMINA
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[世界を変える眼差し]アーモンドアイ" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 2) + factor(StatusType.POWER, 1) + factor(StatusType.STAMINA, 3),
        testCount = 100000,
        selector = LegendActionSelector.s2h2w1.generator()::generateSelector,
        evaluateSetting = Runner.legendSetting,
        evaluateUpperRate = 0.2,
        scenarioEvents = {
            LegendScenarioEvents(
                listOf(
                    "トーク術", "交渉術", "素敵なハーモニー", "極限の集中",
                    "絆が奏でるハーモニー", "怪物チャンスマイル♪", "絆が織りなす光", "集いし理想",
                    "高潔なる魂", "百折不撓", "飽くなき挑戦心",
                ).map { getLegendBuff(it) }
            )
        },
    )
}

private fun speed2Stamina2Wisdom1Wisdom() {
    val scenario = Scenario.LEGEND
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ",
        "[Devilish Whispers]スティルインラブ",
        "[Cocoon]エアシャカール",
        "[そして幕は上がる]ダンツフレーム",
//        "[Take Them Down!]ナリタタイシン",
        "[導きの光]伝説の体現者",
    ).toTypedArray()
    val targetStatus = StatusType.WISDOM
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[Blooming Buds]デアリングハート" to it }.toTypedArray())),
//        Store.getSupportByName("[大望は飛んでいく]エルコンドルパサー" to 4),
        factor = factor(StatusType.SPEED, 2) + factor(StatusType.POWER, 1) + factor(StatusType.STAMINA, 3),
        testCount = 100000,
        selector = LegendActionSelector.s2h2w1.generator()::generateSelector,
        evaluateSetting = Runner.legendSetting,
        evaluateUpperRate = 0.2,
        scenarioEvents = {
            LegendScenarioEvents(
                listOf(
                    "トーク術", "交渉術", "素敵なハーモニー", "極限の集中",
                    "絆が奏でるハーモニー", "怪物チャンスマイル♪", "絆が織りなす光", "集いし理想",
                    "高潔なる魂", "百折不撓", "飽くなき挑戦心",
                ).map { getLegendBuff(it) }
            )
        },
    )
}

private fun speed2Power1Guts1Wisdom1Power() {
    val scenario = Scenario.LEGEND
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ",
        "[誘うは夢心地]ドリームジャーニー",
//        "[深窓の少女へ]メジロアルダン",
        "[繋がれパレード・ノーツ♪]トランセンド",
        "[Take Them Down!]ナリタタイシン",
        "[導きの光]伝説の体現者",
    ).toTypedArray()
    val targetStatus = StatusType.POWER
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[誘うは夢心地]ドリームジャーニー" to it }.toTypedArray())),
//        Store.getSupportByName("[深窓の少女へ]メジロアルダン" to 4),
        factor = factor(StatusType.STAMINA, 3) + factor(StatusType.SPEED, 3),
        testCount = 100000,
        selector = LegendActionSelector.s2p1g1w1.generator()::generateSelector,
        evaluateSetting = Runner.legendSetting,
        evaluateUpperRate = 0.2,
        scenarioEvents = {
            LegendScenarioEvents(
                listOf(
                    "トーク術", "交渉術", "素敵なハーモニー", "極限の集中",
                    "絆が奏でるハーモニー", "怪物チャンスマイル♪", "絆が織りなす光", "集いし理想",
                    "高潔なる魂", "百折不撓", "飽くなき挑戦心",
                ).map { getLegendBuff(it) }
            )
        },
    )
}

private fun speed3Guts1Wisdom1Guts() {
    val scenario = Scenario.LEGEND
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ",
        "[Devilish Whispers]スティルインラブ",
        "[誘うは夢心地]ドリームジャーニー",
//        "[繋がれパレード・ノーツ♪]トランセンド",
        "[Take Them Down!]ナリタタイシン",
        "[導きの光]伝説の体現者",
    ).toTypedArray()
    val targetStatus = StatusType.GUTS
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
//        targetStatus, rarity = 2..3, talent = 0..4,
        Store.getSupportByName(*((0..4).map { "[My Beloved Scenery]サイレンススズカ" to it }.toTypedArray())),
//        Store.getSupportByName("[繋がれパレード・ノーツ♪]トランセンド" to 4),
        factor = factor(StatusType.STAMINA, 6),
        testCount = 100000,
        selector = LegendActionSelector.s3g1w1.generator()::generateSelector,
        evaluateSetting = Runner.legendSetting,
        evaluateUpperRate = 0.2,
        scenarioEvents = {
            LegendScenarioEvents(
                listOf(
                    "トーク術", "交渉術", "素敵なハーモニー", "極限の集中",
                    "絆が奏でるハーモニー", "怪物チャンスマイル♪", "絆が織りなす光", "集いし理想",
                    "高潔なる魂", "百折不撓", "飽くなき挑戦心",
                ).map { getLegendBuff(it) }
            )
        },
    )
}

private fun speed2Stamina1Guts1Wisdom1Guts() {
    val scenario = Scenario.LEGEND
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val defaultSupport = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ",
        "[誘うは夢心地]ドリームジャーニー",
        "[Cocoon]エアシャカール",
//        "[繋がれパレード・ノーツ♪]トランセンド",
        "[Take Them Down!]ナリタタイシン",
        "[導きの光]伝説の体現者",
    ).toTypedArray()
    val targetStatus = StatusType.GUTS
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        targetStatus, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[誘うは夢心地]ドリームジャーニー" to it }.toTypedArray())),
//        Store.getSupportByName("[My Beloved Scenery]サイレンススズカ" to 4),
        factor = factor(StatusType.POWER, 3) + factor(StatusType.SPEED, 3),
        testCount = 100000,
        selector = LegendActionSelector.s2p1g1w1.generator()::generateSelector,
        evaluateSetting = Runner.legendSetting,
        evaluateUpperRate = 0.2,
        scenarioEvents = {
            LegendScenarioEvents(
                listOf(
                    "トーク術", "交渉術", "素敵なハーモニー", "極限の集中",
                    "絆が奏でるハーモニー", "怪物チャンスマイル♪", "絆が織りなす光", "集いし理想",
                    "高潔なる魂", "百折不撓", "飽くなき挑戦心",
                ).map { getLegendBuff(it) }
            )
        },
    )
}
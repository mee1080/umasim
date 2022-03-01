/*
 * Copyright 2021 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.data.Scenario

val scenario = Scenario.URA

fun openCui(args: Array<String>) {
//    dataCheck()
//    singleSimulation()
    singleClimaxSimulation()
//    calcExpected()
//    checkNewSimulator()
//    testAoharuSimulation()
//    compareAoharuSimulation()
//    compareExpectedBasedAI()
//    deBuffSimulation(100000)
//    jigatameSimulation(100000)

//    optimizeAI(
//        Scenario.URA,
//        Store.getChara("ハルウララ", 5, 5),
//        Store.getSupportByName(
//            *(speed(4, 3)),
//            *(power(4, 2)),
//            *(friend(4, 1)),
//        )
//    )

    // 短距離スピパワ
//    optimizeAI(
//        Store.getChara("ハルウララ", 5, 5), Store.getSupportByName(
//            *(speed(4, 3)),
//            *(power(4, 2)),
//            *(friend(4, 1)),
//        ), options = generateOptions(
//            base = FactorBasedActionSelector.Option(),
//            step = 0.05,
//            speed = 0.9..1.0,
//            stamina = 0.8..1.0,
//            power = 0.8..1.0,
//            hp = 0.6..0.7,
//        ), testCount = 1000
//    )
//    doShortSimulation(
//        StatusType.SPEED, 0..4, 4, false,
//        100000, FactorBasedActionSelector.speedPower
//    )
//    (1..31 step 10).forEach { charm ->
//        println("愛嬌ターン $charm")
//        doShortSimulation(
//            StatusType.SPEED, 4..4, 4, false,
//            100000, FactorBasedActionSelector.speedPower
//        ) {
//            if (it.turn == charm + 1) {
//                it.condition.add("愛嬌○")
//            }
//        }
//    }
    // 中距離スピパワ
//    optimizeAI(
//        Store.getChara("[ぶっとび☆さまーナイト]マルゼンスキー", 5, 5), Store.getSupportByName(
//            *(speed(4, 3)),
//            *(power3(4, 3)),
//        ), options = generateOptions(
//            base = FactorBasedActionSelector.Option(),
//            step = 0.1,
//            speed = 0.6..0.8,
//            stamina = 1.0..1.2,
//            power = 1.0..1.2,
//            hp = 0.5..0.7,
//        ), testCount = 1000
//    )
//    doShortSimulation(
//        StatusType.POWER, 0..4, 4, false,
//        100000, FactorBasedActionSelector.speedPowerMiddle,
//        needFriend = false,
//        chara = Store.getChara("[ぶっとび☆さまーナイト]マルゼンスキー", 5, 5),
//    )

    // 短距離スピ賢
//    optimizeAI(
//        Store.getChara("ハルウララ", 5, 5),
//        Store.getSupportByName(
//            *(speed(4, 2)),
//            *(wisdom(4, 2)),
//            *(friend(4, 1)),
//            "[押して忍べど燃ゆるもの]ヤエノムテキ" to 4,
//        ),
//        options = generateOptions(
//            step = 0.1,
//            speed = 0.9..1.1,
//            stamina = 0.8..1.0,
//            power = 0.8..1.0,
//            guts = 0.8..0.8,
//            wisdom = 0.6..0.8,
//            hp = 0.5..0.7,
//        ),
//        testCount = 1000, turn = 60,
//    )
//    doShortSimulation(
//        StatusType.WISDOM, 0..4, 4, true,
//        100000, FactorBasedActionSelector.speedWisdomPower
//    )
//    (1..31 step 10).forEach { charm ->
//        println("愛嬌ターン $charm")
//        doShortSimulation(
//            StatusType.SPEED, 4..4, 4, true,
//            100000, FactorBasedActionSelector.speedWisdom
//        ) {
//            if (it.turn == charm + 1) {
//                it.condition.add("愛嬌○")
//            }
//        }
//    }

    // 長距離スピスタ
//    optimizeAI(
//        Store.getChara("ゴールドシップ", 5, 5), Store.getSupportByName(
//            *(speed(4, 3)),
//            *(stamina(4, 3)),
//        ), options = generateOptions(
//            step = 0.1,
//            speed = 1.0..1.2,
//            stamina = 1.0..1.2,
//            power = 0.4..0.6,
//            guts = 0.4..0.6,
//            hp = 0.6..0.8,
//        ), testCount = 1000
//    )
//    doLongSimulation(
//        StatusType.STAMINA, 0..4, 4,
//        100000, option = FactorBasedActionSelector.speedStamina
//    )

    // パワ賢
//    optimizeAI(
//        Scenario.AOHARU,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(power3(4, 3)),
//            *(wisdom2(4, 3)),
//        )
//    )
//    doSimulation2(
//        Scenario.AOHARU,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(power3(4, 3)),
//            *(wisdom2(4, 2)),
//        ).toTypedArray(),
//        StatusType.WISDOM,
//        0..4,
//        factor(StatusType.SPEED, 6),
//        100000,
//        FactorBasedActionSelector2.aoharuPowerWisdom,
//    )
//    doSimulation2(
//        Scenario.AOHARU,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(power3(4, 2)),
//            *(wisdom2(4, 3)),
//        ).toTypedArray(),
//        StatusType.POWER,
//        0..4,
//        100000,
//        FactorBasedActionSelector2.aoharuPowerWisdom,
//    )
//    optimizeAI(
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5), Store.getSupportByName(
//            *(power2(4, 3)),
//            *(wisdom(4, 3)),
//        ), options = generateOptions(
//            step = 0.1,
//            speed = 0.7..0.9,
//            stamina = 1.0..1.2,
//            power = 1.1..1.3,
//            wisdom = 0.4..0.6,
//            hp = 0.5..0.7,
//        ), turn = 60, testCount = 500
//    )
//    doPowerWisdomSimulation(
//        StatusType.POWER, 0..4, 4,
//        100000, FactorBasedActionSelector.powerWisdom
//    )

    // 短距離根性
//    optimizeAI(
//        Store.getChara("ハルウララ", 5, 5), Store.getSupportByName(
//            *(speed2(4, 2)),
//            *(guts(4, 4)),
//        ), options = generateOptions(
//            base = FactorBasedActionSelector.Option(),
//            step = 0.1,
//            speed = 1.0..1.2,
//            stamina = 0.8..0.9,
//            power = 0.8..1.0,
//            guts = 0.8..1.0,
//            hp = 0.6..0.7,
//        ), testCount = 1000
//    )
//    doShortSimulation(
//        StatusType.GUTS, 0..4, 4, false,
//        100000, FactorBasedActionSelector.speedGuts
//    )

    // スピパワ賢
//    optimizeAI(
//        Scenario.AOHARU,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(speed(4, 2)),
//            *(power3(4, 3)),
//            *(wisdom(4, 1)),
//        )
//    )
//    doSimulation2(
//        Scenario.URA,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(speed(4, 1)),
//            *(power3(4, 3)),
//            *(wisdom(4, 1)),
//        ).toTypedArray(),
//        StatusType.SPEED,
//        0..4,
//        100000,
//        FactorBasedActionSelector2.speed2Power3Wisdom1,
//    )
//    doSimulation2(
//        Scenario.AOHARU,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(speed(4, 1)),
//            *(power3(4, 3)),
//            *(wisdom(4, 1)),
//        ).toTypedArray(),
//        StatusType.SPEED,
//        0..4,
//        100000,
//        FactorBasedActionSelector2.aoharuSpeed2Power3Wisdom1,
//    )
//    doSimulation2(
//        Scenario.URA,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(speed(4, 2)),
//            *(power3(4, 2)),
//            *(wisdom(4, 1)),
//        ).toTypedArray(),
//        StatusType.POWER,
//        0..4,
//        100000,
//        FactorBasedActionSelector2.speed2Power3Wisdom1,
//    )

    // スピ2賢2ライス代理
//    optimizeAI(
//        Scenario.AOHARU,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(speed4(4, 2)),
//            *(power4(4, 1)),
//            *(wisdom(4, 2)),
//            *(friend2(4, 1)),
//        ),
//        defaultEvaluateSetting.toMutableMap().apply {
//            put(StatusType.STAMINA, 1.4 to 600)
//            put(StatusType.WISDOM, 1.0 to 600)
//        },
//        generateOptions2(
//            speed = doubleArrayOf(1.0, 1.2, 1.4),
//            power = doubleArrayOf(1.0, 1.2, 1.4),
//        )
//    )
//    val repository = SimulationResultRepository("result/umasimout.db")
//    doSimulation2(
//        Scenario.AOHARU,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(speed4(4, 2)),
//            *(power4(4, 1)),
//            *(wisdom(4, 1)),
//            *(friend2(4, 1)),
//        ).toTypedArray(),
//        StatusType.WISDOM, 0..4,
////        Store.getSupportByName(*((0..4).map { "[爆速！最速！花あらし！]サクラバクシンオー" to it }.toTypedArray())),
////        Store.getSupportByName("[袖振り合えば福となる♪]マチカネフクキタル" to 4),
//        factor(StatusType.STAMINA, 4) + factor(StatusType.POWER, 2),
//        100000,
//        FactorBasedActionSelector2.aoharuSpeed2Power1Wisdom2Friend1Optuna3,
//        { card, summaries -> stdoutOutput.invoke(card, summaries) },
////        { card, summaries -> repository.save("${card.id},${card.name},${card.talent}", summaries) }
//    )
//    optimizeAI(
//        Scenario.AOHARU,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(speed4(4, 2)),
//            *(power4(4, 1)),
//            *(wisdom(4, 1)),
//            *(friend2(4, 1)),
//            "[爆速！最速！花あらし！]サクラバクシンオー" to 4,
//        ),
//        options = generateOptions2(
//            speed = doubleArrayOf(1.2, 1.4, 1.6),
//            power = doubleArrayOf(1.2, 1.4, 1.6),
//        )
//    )

    // スピ2スタ1賢3
//    optimizeAI(
//        Scenario.AOHARU,
//        Store.getChara("[レッドストライフ]ゴールドシップ", 5, 5),
//        Store.getSupportByName(
//            *(speed4(4, 2)),
//            *(stamina(4, 1)),
//            *(wisdom(4, 3)),
//        )
//    )
//    doSimulation2(
//        Scenario.AOHARU,
//        Store.getChara("[レッドストライフ]ゴールドシップ", 5, 5),
//        Store.getSupportByName(
//            *(speed(4, 2)),
//            *(wisdom(4, 3)),
//        ).toTypedArray(),
//        StatusType.STAMINA,
//        0..4,
//        100000,
//        FactorBasedActionSelector2.aoharuSpeed2Stamina1Wisdom3,
//    )

//    doShortSimulation(StatusType.SPEED)
//    doShortSimulation(
//        StatusType.POWER, 0..4, 4, false, 100000, FactorBasedActionSelector.Option(
//            speedFactor = 0.85
//        )
//    )
//    doCharmSimulation()
//    doFailureRateSimulation()
//    optimizeAI(
//        Scenario.AOHARU,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 4, 5),
//        Store.getSupportByName(
//            "[願いまでは拭わない]ナイスネイチャ" to 4,
//            "[夕焼けはあこがれの色]スペシャルウィーク" to 4,
//            "[見習い魔女と長い夜]スイープトウショウ" to 4,
//            "[波立つキモチ]ナリタタイシン" to 4,
//            "[スノウクリスタル・デイ]マーベラスサンデー" to 4,
////            "[幽霊さんとハロウィンの魔法]ミホノブルボン" to 4,
////            "[運の行方]マチカネフクキタル" to 3,
//            "[ようこそ、トレセン学園へ！]駿川たづな" to 1,
//        ),
//        mapOf(
//            StatusType.SPEED to (1.0 to 1100),
//            StatusType.STAMINA to (1.0 to 400),
//            StatusType.POWER to (1.2 to 1100),
//            StatusType.GUTS to (1.0 to 300),
//            StatusType.WISDOM to (0.8 to 800),
//            StatusType.SKILL to (0.3 to 600),
//        ),
//        generateOptions2(
//            speed = doubleArrayOf(1.2, 1.4, 1.6),
//            power = doubleArrayOf(1.2, 1.4, 1.6),
//        )
//    )
//    doSimulation2(
//        Scenario.AOHARU,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            *(speed4(4, 2)),
//            *(stamina(4, 1)),
//            *(wisdom(4, 1)),
//            *(friend2(4, 1)),
//        ).toTypedArray(),
//        StatusType.POWER, 0..4,
////        Store.getSupportByName(*((0..4).map { "[爆速！最速！花あらし！]サクラバクシンオー" to it }.toTypedArray())),
////        Store.getSupportByName("[そこに“いる”幸せ]アグネスデジタル" to 4),
//        100000,
//        FactorBasedActionSelector2.aoharuSpeed2Stamina1Power1Wisdom1Friend1Optuna,
//        { card, summaries -> stdoutOutput.invoke(card, summaries) },
////        { card, summaries -> repository.save("${card.id},${card.name},${card.talent}", summaries) }
//    )
//    doSimulation2(
//        Scenario.AOHARU,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 4, 5),
//        Store.getSupportByName(
//            "[In my way]トーセンジョーダン" to 4,
//            "[見習い魔女と長い夜]スイープトウショウ" to 4,
//            "[夕焼けはあこがれの色]スペシャルウィーク" to 4,
//            "[スノウクリスタル・デイ]マーベラスサンデー" to 4,
//            "[幽霊さんとハロウィンの魔法]ミホノブルボン" to 4,
//        ).toTypedArray(),
////        StatusType.WISDOM, 0..4,
////        Store.getSupportByName(*((0..4).map { "[爆速！最速！花あらし！]サクラバクシンオー" to it }.toTypedArray())),
//        Store.getSupportByName("[夜に暁、空に瑞星]アドマイヤベガ" to 4),
//        factor(StatusType.SPEED, 2) + factor(
//            StatusType.POWER,
//            2
//        ) + (StatusType.STAMINA to 2) + (StatusType.STAMINA to 2),
//        100000,
//        FactorBasedActionSelector2.Option(
//            aoharuBurnFactor = { _, _ -> 23.02896997810754 },
//            aoharuFactor = { turn, _, _ ->
//                when {
//                    turn <= 24 -> 13.803821031469564
//                    turn <= 36 -> 9.47897204420649
//                    turn <= 48 -> 3.0530377402346125
//                    else -> 6.65415366956225
//                }
//            },
//            gutsFactor = 1.0004962851970132,
//            hpFactor = 1.2881367347726032,
//            powerFactor = 1.8518683878878885,
//            relationFactor = { type, rank, _ ->
//                when (type) {
//                    StatusType.SPEED -> when (rank) {
//                        0 -> 12.723879320370237
//                        1 -> 9.255694388301702
//                        else -> 13.132327846147893
//                    }
//                    StatusType.POWER -> when (rank) {
//                        0 -> 15.700659310451204
//                        else -> 11.36641199670376
//                    }
//                    else -> 6.41945391831409
//                }
//            },
//            skillPtFactor = 0.5423309544596215,
//            speedFactor = 1.886962976375078,
//            staminaFactor = 1.8822823475400616,
//            wisdomFactor = 1.458470048254414
//        ),
//        { card, summaries -> stdoutOutput.invoke(card, summaries) },
////        { card, summaries -> repository.save("${card.id},${card.name},${card.talent}", summaries) }
//    )
}
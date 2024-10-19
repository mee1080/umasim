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

import io.github.mee1080.umasim.scenario.Scenario

val scenario = Scenario.URA

fun openCui(args: Array<String>) {
    debugCook()
//    simulateCook()
//    debugUaf()
//    simulateUaf()
//    simulateLarc()
//    singleGrandLiveSimulation()
//    testProvideLesson()
//    testLessonClear()
//    testExpected()
    dataCheck()
//    singleSimulation()
//    singleClimaxSimulation()
//    calcExpected()
//    checkNewSimulator()
//    testAoharuSimulation()
//    compareAoharuSimulation()
//    compareExpectedBasedAI()
//    deBuffSimulation(100000)
//    jigatameSimulation(100000)
//    Store.raceMap.forEachIndexed { index, raceEntries ->
//        println("$index: ${raceEntries.joinToString { it.name }}")
//    }
//    Store.Climax.raceAchievement.forEach { println("${it.name}:${it.status}:${it.skill}") }
//    val rotation = RaceRotation() + listOf(
//        "皐月賞", "東京優駿（日本ダービー）", "菊花賞",
//        "大阪杯", "天皇賞（春）", "宝塚記念",
//    ).map { Store.getRace(it) } + listOf(
//        "天皇賞（秋）", "ジャパンカップ",
//    ).map { Store.getSeniorRace(it) }
//    println(rotation)
//    val current = rotation.checkAchievement(Store.Climax.raceAchievement)
//    current.forEach { name, count ->
//        println("${if (count == 0) "★" else ""} $name $count")
//    }
//    val next = (rotation + Store.getRace("もみじステークス")).checkAchievement(Store.Climax.raceAchievement)
//    next.forEach { (name, count) ->
//        if (current[name] != count) {
//            println("${if (count == 0) "★" else ""} $name $count")
//        }
//    }

//    Store.raceMap.forEachIndexed { index, raceEntries ->
//        if (rotation.getRace(index) == null) {
//            raceEntries.forEach { raceEntry ->
//                val next = (rotation + raceEntry).checkAchievement(Store.Climax.raceAchievement)
//                val diff = next.mapNotNull { (name, count) ->
//                    if (current[name] != count) name to count else null
//                }
//                println("${raceEntry.name}: ${diff.joinToString { "${if (it.second == 0) "★" else ""} ${it.first} ${it.second}" }}")
//            }
//        }
//    }


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
////        StatusType.WISDOM, 0..4,
//        Store.getSupportByName(*((0..4).map { "[あこがれの景色]ライスシャワー" to it }.toTypedArray())),
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
//        StatusType.POWER, 0..4,
//        factor(StatusType.SPEED, 6),
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
//        StatusType.SPEED, 0..4,
//        factor(StatusType.STAMINA, 6),
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
//        StatusType.POWER, 0..4,
////        Store.getSupportByName(*((0..4).map { "[うららか・ぱっしょん♪]ハルウララ" to it }.toTypedArray())),
//        factor(StatusType.STAMINA, 6),
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
//    doSimulation2(
//        Scenario.CLIMAX,
//        Store.getChara("[秋桜ダンツァトリーチェ]ゴールドシチー", 5, 5),
//        Store.getSupportByName(
//            "[飛び出せ、キラメケ]アイネスフウジン" to 4,
//            "[届け、このオモイ！]バンブーメモリー" to 4,
////            "[一等星を目指して]アドマイヤベガ" to 4,
//            "[好きなんだ、ご飯が]オグリキャップ" to 4,
//            "[感謝は指先まで込めて]ファインモーション" to 4,
//            "[願いまでは拭わない]ナイスネイチャ" to 4,
//        ).toTypedArray(),
////        StatusType.GUTS, 0..4,
//        Store.getSupportByName(*((0..4).map { "[チョベリグ心あれば桜心]サクラチヨノオー" to it }.toTypedArray())),
////        Store.getSupportByName("[キラキラカケル∞]エルコンドルパサー" to 4),
//        factor(StatusType.POWER, 4) + factor(StatusType.WISDOM, 2),
//        100000,
//        ClimaxFactorBasedActionSelector.guts4Wisdom2,
//        Runner.mileEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.CLIMAX,
//        Store.getChara("[秋桜ダンツァトリーチェ]ゴールドシチー", 5, 5),
//        Store.getSupportByName(
//            "[飛び出せ、キラメケ]アイネスフウジン" to 4,
//            "[うらら～な休日]ハルウララ" to 4,
//            "[バカと笑え]メジロパーマー" to 4,
//            "[一等星を目指して]アドマイヤベガ" to 4,
//            "[感謝は指先まで込めて]ファインモーション" to 4,
//        ).toTypedArray(),
//        StatusType.WISDOM, 0..4,
////        Store.getSupportByName(*((0..4).map { "[爆速！最速！花あらし！]サクラバクシンオー" to it }.toTypedArray())),
//        factor(StatusType.POWER, 4) + factor(StatusType.WISDOM, 2),
//        100000,
//        ClimaxFactorBasedActionSelector.guts4Wisdom2,
//        Runner.mileEvaluateSetting,
//    )

    // クライマックススピ3賢さ2代理
//    doSimulation2(
//        Scenario.CLIMAX,
//        Store.getChara("[秋桜ダンツァトリーチェ]ゴールドシチー", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[袖振り合えば福となる♪]マチカネフクキタル" to 4,
//            "[感謝は指先まで込めて]ファインモーション" to 4,
//            "[願いまでは拭わない]ナイスネイチャ" to 4,
//            "[徹底管理主義]樫本理子" to 4,
//        ).toTypedArray(),
////        StatusType.SPEED, 0..4,
//        Store.getSupportByName(*((0..4).map { "[mag!c number]エアシャカール" to it }.toTypedArray())),
////        Store.getSupportByName("[桃色のバックショット]ナリタトップロード" to 4),
//        factor(StatusType.POWER, 5) + factor(StatusType.STAMINA, 1),
//        100000,
//        ClimaxFactorBasedActionSelector.speed3Wisdom2Friend1,
//        Runner.mileEvaluateSetting,
//    )

    // クライマックススピ2根性2賢さ2
//    doSimulation2(
//        Scenario.CLIMAX,
//        Store.getChara("[秋桜ダンツァトリーチェ]ゴールドシチー", 5, 5),
//        Store.getSupportByName(
//            "[桃色のバックショット]ナリタトップロード" to 4,
//            "[一等星を目指して]アドマイヤベガ" to 4,
//            "[届け、このオモイ！]バンブーメモリー" to 4,
//            "[感謝は指先まで込めて]ファインモーション" to 4,
//            "[願いまでは拭わない]ナイスネイチャ" to 4,
//        ).toTypedArray(),
////        StatusType.SPEED, 0..4,
//        Store.getSupportByName(*((0..4).map { "[Q≠0]アグネスタキオン" to it }.toTypedArray())),
////        Store.getSupportByName("[迫る熱に押されて]キタサンブラック" to 4),
//        factor(StatusType.POWER, 4) + factor(StatusType.WISDOM, 2),
//        100000,
//        ClimaxFactorBasedActionSelector.speed2guts2Wisdom2,
//        Runner.mileEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.CLIMAX,
//        Store.getChara("[秋桜ダンツァトリーチェ]ゴールドシチー", 5, 5),
//        Store.getSupportByName(
//            "[桃色のバックショット]ナリタトップロード" to 4,
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[一等星を目指して]アドマイヤベガ" to 4,
//            "[届け、このオモイ！]バンブーメモリー" to 4,
//            "[感謝は指先まで込めて]ファインモーション" to 4,
//        ).toTypedArray(),
//        StatusType.WISDOM, 0..4,
////        Store.getSupportByName(*((0..4).map { "[キラキラカケル∞]エルコンドルパサー" to it }.toTypedArray())),
////        Store.getSupportByName("[迫る熱に押されて]キタサンブラック" to 4),
//        factor(StatusType.POWER, 4) + factor(StatusType.WISDOM, 2),
//        100000,
//        ClimaxFactorBasedActionSelector.speed2guts2Wisdom2,
//        Runner.mileEvaluateSetting,
//    )

    // スピ2賢さ2ルビーハロー
//    doSimulation2(
//        Scenario.GRAND_LIVE,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[感謝は指先まで込めて]ファインモーション" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[嗚呼華麗ナル一族]ダイイチルビー" to 4,
//            "[from the GROUND UP]ライトハロー" to 4,
//        ).toTypedArray(),
////        StatusType.SPEED, 0..4,
//        Store.getSupportByName(*((0..4).map { "[U & Me]ミホノブルボン" to it }.toTypedArray())),
////        Store.getSupportByName("[Q≠0]アグネスタキオン" to 4),
//        factor(StatusType.POWER, 4) + factor(StatusType.WISDOM, 2),
//        10000,
//        GrandLiveFactorBasedActionSelector.speed2Power1Wisdom2Friend1,
//        Runner.grandLiveMileEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.GRAND_LIVE,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[感謝は指先まで込めて]ファインモーション" to 4,
//            "[Q≠0]アグネスタキオン" to 4,
//            "[嗚呼華麗ナル一族]ダイイチルビー" to 4,
//            "[from the GROUND UP]ライトハロー" to 4,
//        ).toTypedArray(),
////        StatusType.WISDOM, 0..4,
//        Store.getSupportByName(*((0..4).map { "[駆けよ、駆けよ、駆けよ！！]オグリキャップ" to it }.toTypedArray())),
////        Store.getSupportByName("[Q≠0]アグネスタキオン" to 4),
//        factor(StatusType.POWER, 4) + factor(StatusType.WISDOM, 2),
//        10000,
//        GrandLiveFactorBasedActionSelector.speed2Power1Wisdom2Friend1,
//        Runner.grandLiveMileEvaluateSetting,
//    )

    // スピ3賢さ2ハロー
//    doSimulation2(
//        Scenario.GRAND_LIVE,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[Q≠0]アグネスタキオン" to 4,
//            "[感謝は指先まで込めて]ファインモーション" to 4,
//            "[駆けよ、駆けよ、駆けよ！！]オグリキャップ" to 4,
//            "[from the GROUND UP]ライトハロー" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.SPEED, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[マーベラス☆ショコラSHOW]マーベラスサンデー" to it }.toTypedArray())),
////        Store.getSupportByName("[Q≠0]アグネスタキオン" to 4),
//        factor = factor(StatusType.GUTS, 4) + factor(StatusType.STAMINA, 2),
//        testCount = 10000,
//        option = GrandLiveFactorBasedActionSelector.speed3Wisdom2Friend1,
//        evaluateSetting = Runner.grandLiveMileEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.GRAND_LIVE,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[Q≠0]アグネスタキオン" to 4,
//            "[おセンチ注意報♪]マルゼンスキー" to 4,
//            "[駆けよ、駆けよ、駆けよ！！]オグリキャップ" to 4,
//            "[from the GROUND UP]ライトハロー" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.WISDOM, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[燦爛]メジロラモーヌ" to it }.toTypedArray())),
////        Store.getSupportByName("[フォンデンテで笑って]ヒシアケボノ" to 4),
//        factor = factor(StatusType.GUTS, 4) + factor(StatusType.STAMINA, 2),
//        testCount = 10000,
//        option = GrandLiveFactorBasedActionSelector.speed3Wisdom2Friend1,
//        evaluateSetting = Runner.grandLiveMileEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.URA,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[Q≠0]アグネスタキオン" to 4,
//            "[感謝は指先まで込めて]ファインモーション" to 4,
//            "[駆けよ、駆けよ、駆けよ！！]オグリキャップ" to 4,
//            "[from the GROUND UP]ライトハロー" to 4,
//        ).toTypedArray(),
//        targetStatus = StatusType.SPEED, rarity = 2..3, talent = 0..4,
////        Store.getSupportByName(*((0..4).map { "[おセンチ注意報♪]マルゼンスキー" to it }.toTypedArray())),
////        Store.getSupportByName("[Q≠0]アグネスタキオン" to 4),
//        factor = factor(StatusType.STAMINA, 6),
//        testCount = 10000,
//        option = FactorBasedActionSelector2.uraMileSpeed3Wisdom2,
//        evaluateSetting = Runner.mileEvaluateSetting,
//    )

    // スピ3スタ1賢さ1ハロー
//    doSimulation2(
//        Scenario.GRAND_LIVE,
//        Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[Q≠0]アグネスタキオン" to 4,
//            "[おセンチ注意報♪]マルゼンスキー" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[from the GROUND UP]ライトハロー" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.STAMINA, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[ハネ退け魔を退け願い込め]スペシャルウィーク" to it }.toTypedArray())),
////        Store.getSupportByName("[一粒の安らぎ]スーパークリーク" to 4),
//        factor = factor(StatusType.STAMINA, 6),
//        testCount = 10000,
//        option = GrandLiveFactorBasedActionSelector.speed3Stamina1Wisdom1Friend1,
//        evaluateSetting = Runner.grandLiveLongEvaluateSetting,
//    )

    // スピ2スタ2賢さ1ハロー
//    doSimulation2(
//        Scenario.GRAND_LIVE,
//        Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[Q≠0]アグネスタキオン" to 4,
//            "[A Win Foreshadowed]シンボリクリスエス" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[from the GROUND UP]ライトハロー" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.STAMINA, rarity = 2..3, talent = 0..4,
////        Store.getSupportByName(*((0..4).map { "[一粒の安らぎ]スーパークリーク" to it }.toTypedArray())),
//        Store.getSupportByName("[一粒の安らぎ]スーパークリーク" to 4),
//        factor = factor(StatusType.STAMINA, 3) + factor(StatusType.WISDOM, 3),
//        testCount = 10000,
//        option = GrandLiveFactorBasedActionSelector.speed2Stamina2Wisdom1Friend1,
//        evaluateSetting = Runner.grandLiveLongEvaluateSetting,
//    )

    // GMスピ2パワ1根性1賢さ2
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[おセンチ注意報♪]マルゼンスキー" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[燦爛]メジロラモーヌ" to 4,
//            "[嗚呼華麗ナル一族]ダイイチルビー" to 4,
//        ).toTypedArray(),
//        targetStatus = StatusType.GUTS, rarity = 2..3, talent = 0..4,
////        Store.getSupportByName(*((0..4).map { "[燦爛]メジロラモーヌ" to it }.toTypedArray())),
////        Store.getSupportByName("[フォンデンテで笑って]ヒシアケボノ" to 4),
//        factor = factor(StatusType.GUTS, 3) + factor(StatusType.STAMINA, 3),
//        testCount = 5000,
//        option = GmActionSelector.speed2Power1Guts1Wisdom2V2,
//        evaluateSetting = Runner.gmMileEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[うらら～な休日]ハルウララ" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[燦爛]メジロラモーヌ" to 4,
//            "[嗚呼華麗ナル一族]ダイイチルビー" to 4,
//        ).toTypedArray(),
//        targetStatus = StatusType.SPEED, rarity = 2..3, talent = 0..4,
////        Store.getSupportByName(*((0..4).map { "[燦爛]メジロラモーヌ" to it }.toTypedArray())),
////        Store.getSupportByName("[フォンデンテで笑って]ヒシアケボノ" to 4),
//        factor = factor(StatusType.GUTS, 3) + factor(StatusType.STAMINA, 3),
//        testCount = 5000,
//        option = GmActionSelector.speed2Power1Guts1Wisdom2,
//        evaluateSetting = Runner.gmMileEvaluateSetting,
//    )

    // GMスピ2パワ1根性1賢さ1三女神
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[おセンチ注意報♪]マルゼンスキー" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[うらら～な休日]ハルウララ" to 4,
//            "[嗚呼華麗ナル一族]ダイイチルビー" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.GROUP, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[永劫続く栄光へ]祖にして導く者" to it }.toTypedArray())),
////        Store.getSupportByName("[フォンデンテで笑って]ヒシアケボノ" to 4),
//        factor = factor(StatusType.GUTS, 3) + factor(StatusType.STAMINA, 3),
//        testCount = 20000,
//        option = GmActionSelector.speed2Power1Guts1Wisdom1Group1,
//        evaluateSetting = Runner.gmMileEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[おセンチ注意報♪]マルゼンスキー" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[永劫続く栄光へ]祖にして導く者" to 4,
//            "[嗚呼華麗ナル一族]ダイイチルビー" to 4,
//        ).toTypedArray(),
//        targetStatus = StatusType.GUTS, rarity = 2..3, talent = 0..4,
////        Store.getSupportByName(*((0..4).map { "[永劫続く栄光へ]祖にして導く者" to it }.toTypedArray())),
////        Store.getSupportByName("[フォンデンテで笑って]ヒシアケボノ" to 4),
//        factor = factor(StatusType.GUTS, 3) + factor(StatusType.STAMINA, 3),
//        testCount = 20000,
//        option = GmActionSelector.speed2Power1Guts1Wisdom1Group1,
//        evaluateSetting = Runner.gmMileEvaluateSetting,
//    )

    // GMスピ2スタ1パワ1賢さ1三女神
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[餓狼]ナリタブライアン", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[おセンチ注意報♪]マルゼンスキー" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[永劫続く栄光へ]祖にして導く者" to 4,
//            "[パッションチャンピオーナ！]エルコンドルパサー" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.STAMINA, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[心、夜風にさらわれて]メジロマックイーン" to it }.toTypedArray())),
////        Store.getSupportByName("[一粒の安らぎ]スーパークリーク" to 4),
//        factor = factor(StatusType.STAMINA, 4) + factor(StatusType.POWER, 1) + factor(StatusType.WISDOM, 1),
//        testCount = 20000,
//        option = GmActionSelector.speed2Stamina1Power1Wisdom1Group1Long,
//        evaluateSetting = Runner.gmLongEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[餓狼]ナリタブライアン", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[おセンチ注意報♪]マルゼンスキー" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[永劫続く栄光へ]祖にして導く者" to 4,
//            "[一粒の安らぎ]スーパークリーク" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.POWER, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[よい茶の飲み置き]グラスワンダー" to it }.toTypedArray())),
////        Store.getSupportByName("[今は瞳を閉じないで]ナイスネイチャ" to 4),
//        factor = factor(StatusType.STAMINA, 4) + factor(StatusType.POWER, 1) + factor(StatusType.WISDOM, 1),
//        testCount = 20000,
//        option = GmActionSelector.speed2Stamina1Power1Wisdom1Group1Long,
//        evaluateSetting = Runner.gmLongEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[餓狼]ナリタブライアン", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[パッションチャンピオーナ！]エルコンドルパサー" to 4,
//            "[感謝は指先まで込めて]ファインモーション" to 4,
//            "[永劫続く栄光へ]祖にして導く者" to 4,
//            "[一粒の安らぎ]スーパークリーク" to 4,
//        ).toTypedArray(),
//        targetStatus = StatusType.SPEED, rarity = 2..3, talent = 0..4,
////        Store.getSupportByName(*((0..4).map { "[今は瞳を閉じないで]ナイスネイチャ" to it }.toTypedArray())),
////        Store.getSupportByName("[今は瞳を閉じないで]ナイスネイチャ" to 4),
//        factor = factor(StatusType.STAMINA, 4) + factor(StatusType.POWER, 1) + factor(StatusType.WISDOM, 1),
//        testCount = 20000,
//        option = GmActionSelector.speed2Stamina1Power1Wisdom1Group1Long,
//        evaluateSetting = Runner.gmLongEvaluateSetting,
//    )

    // スピ2根性2賢さ1三女神
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[うらら～な休日]ハルウララ" to 4,
////            "[飛び出せ、キラメケ]アイネスフウジン" to 4,
//            "[爆速！最速！花あらし！]サクラバクシンオー" to 4,
//            "[燦爛]メジロラモーヌ" to 4,
//            "[永劫続く栄光へ]祖にして導く者" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.SPEED, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[比翼のワルツ]トウカイテイオー" to it }.toTypedArray())),
////        target = Store.getSupportByName("[The frontier]ジャングルポケット" to 4),
//        factor = factor(StatusType.STAMINA, 6),
//        testCount = 50000,
//        option = GmActionSelector.speed2Guts2Wisdom1Group1Middle,
//        evaluateSetting = Runner.gmMiddleEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5),
//        Store.getSupportByName(
//            "[迫る熱に押されて]キタサンブラック" to 4,
//            "[おセンチ注意報♪]マルゼンスキー" to 4,
//            "[うらら～な休日]ハルウララ" to 4,
//            "[燦爛]メジロラモーヌ" to 4,
//            "[永劫続く栄光へ]祖にして導く者" to 4,
//        ).toTypedArray(),
//        targetStatus = StatusType.GUTS, rarity = 2..3, talent = 0..4,
////        Store.getSupportByName(*((0..4).map { "[うらら～な休日]ハルウララ" to it }.toTypedArray())),
////        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
//        factor = factor(StatusType.STAMINA, 6),
//        testCount = 50000,
//        option = GmActionSelector.speed2Guts2Wisdom1Group1Middle,
//        evaluateSetting = Runner.gmMiddleEvaluateSetting,
//    )

    // スピ1根性2賢さ2三女神
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5),
//        Store.getSupportByName(
////            "[おセンチ注意報♪]マルゼンスキー" to 4,
//            "[うらら～な休日]ハルウララ" to 4,
//            "[飛び出せ、キラメケ]アイネスフウジン" to 4,
//            "[燦爛]メジロラモーヌ" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[永劫続く栄光へ]祖にして導く者" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.SPEED, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[大望は飛んでいく]エルコンドルパサー" to it }.toTypedArray())),
////        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
//        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
//        testCount = 50000,
//        option = GmActionSelector.speed1Guts2Wisdom2Group1Short,
//        evaluateSetting = Runner.gmShortEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5),
//        Store.getSupportByName(
//            "[The frontier]ジャングルポケット" to 4,
//            "[うらら～な休日]ハルウララ" to 4,
////            "[飛び出せ、キラメケ]アイネスフウジン" to 4,
//            "[燦爛]メジロラモーヌ" to 4,
//            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[永劫続く栄光へ]祖にして導く者" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.GUTS, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[トばすぜホットサマー！]ジャングルポケット" to it }.toTypedArray())),
////        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
//        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
//        testCount = 50000,
//        option = GmActionSelector.speed1Guts2Wisdom2Group1Short,
//        evaluateSetting = Runner.gmShortEvaluateSetting,
//    )
//    doSimulation2(
//        Scenario.GM,
//        Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5),
//        Store.getSupportByName(
//            "[The frontier]ジャングルポケット" to 4,
//            "[うらら～な休日]ハルウララ" to 4,
//            "[飛び出せ、キラメケ]アイネスフウジン" to 4,
//            "[燦爛]メジロラモーヌ" to 4,
////            "[Dear Mr. C.B.]ミスターシービー" to 4,
//            "[永劫続く栄光へ]祖にして導く者" to 4,
//        ).toTypedArray(),
////        targetStatus = StatusType.WISDOM, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[君と見る泡沫]マンハッタンカフェ" to it }.toTypedArray())),
////        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
//        factor = factor(StatusType.SPEED, 4) + factor(StatusType.POWER, 2),
//        testCount = 50000,
//        option = GmActionSelector.speed1Guts2Wisdom2Group1Short,
//        evaluateSetting = Runner.gmShortEvaluateSetting,
//    )
}
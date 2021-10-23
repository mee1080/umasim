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

import io.github.mee1080.umasim.ai.FactorBasedActionSelector
import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
import io.github.mee1080.umasim.ai.SimpleActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.turnToString
import io.github.mee1080.umasim.simulation.*
import io.github.mee1080.umasim.simulation2.AoharuMemberState
import io.github.mee1080.umasim.simulation2.Evaluator
import io.github.mee1080.umasim.simulation2.SimulationEvents
import io.github.mee1080.umasim.simulation2.Summary
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import kotlin.math.roundToInt


fun singleSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック",
        "[必殺！Wキャロットパンチ！]ビコーペガサス",
        "[はやい！うまい！はやい！]サクラバクシンオー",
        "[ようこそ、トレセン学園へ！]駿川たづな",
        "[『愛してもらうんだぞ』]オグリキャップ",
        "[押して忍べど燃ゆるもの]ヤエノムテキ",
//        "[感謝は指先まで込めて]ファインモーション",
    )
    println(chara)
    println(support)
    val selector = FactorBasedActionSelector(
        FactorBasedActionSelector.Option(
            speedFactor = 0.9,
            staminaFactor = 0.8,
            wisdomFactor = 0.5,
        )
    )
    val simulator = Simulator(chara, support, Store.getTrainingList(scenario))
    println(simulator.status)
    Runner.simulate(60, simulator, selector) {
//        if (it.turn == 10) it.condition.add("練習上手○")
    }
    simulator.history.forEachIndexed { index, action -> println("${index + 1}: $action") }
    println(simulator.status)
    val summary = simulator.summary
    println(summary)
}

fun dataCheck() {
    val total = 10000
    Store.supportList.filter { it.rarity >= 2 && it.talent == 4 }.forEach { card ->
        if (card.type != StatusType.FRIEND) {
            val support = Support(0, card)
            val specialtyCount = (0 until total)
                .map { support.selectTraining() }
                .count { it == card.type }
            val specialtyRate = specialtyCount.toDouble() / total
            val specialtyStr = "${card.status.specialtyRate}/${card.unique.specialtyRate} $specialtyRate"

            val hintCount = (0 until total)
                .map { support.checkHint() }
                .count { it }
            val hintRate = hintCount.toDouble() / total
            val hintStr = "${card.status.hintFrequency}/${card.unique.hintFrequency} $hintRate"

            println("${card.name} $specialtyStr $hintStr")
        }
    }
    Store.getTrainingList(Scenario.URA).forEach { println(it) }
    Store.getTrainingList(Scenario.AOHARU).forEach { println(it) }
    Store.charaList.forEach {
        println("${it.name} ${Store.getGoalRaceList(it.charaId).joinToString(",")}")
    }
}

fun calcExpected() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック" to 4,
        "[必殺！Wキャロットパンチ！]ビコーペガサス" to 4,
        "[はやい！うまい！はやい！]サクラバクシンオー" to 4,
        "[『愛してもらうんだぞ』]オグリキャップ" to 4,
        "[押して忍べど燃ゆるもの]ヤエノムテキ" to 4,
        "[ようこそ、トレセン学園へ！]駿川たづな" to 4,
    ).mapIndexed { index, supportCard -> Support(index, supportCard).apply { friendTrainingEnabled = true } }
    println(chara)
    support.forEach { println(it) }
    val result =
        Calculator.calcExpectedTrainingStatus(chara, Store.getTraining(scenario, StatusType.SPEED), 5, 2, support)
    println(result.first)
    result.second.forEach { println("${(it.first * 10000).roundToInt() / 100.0}% : ${it.second}") }
    println(result.second.sumOf { it.first })
}

fun checkNewSimulator() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック" to 4,
        "[必殺！Wキャロットパンチ！]ビコーペガサス" to 4,
        "[はやい！うまい！はやい！]サクラバクシンオー" to 4,
        "[『愛してもらうんだぞ』]オグリキャップ" to 4,
        "[押して忍べど燃ゆるもの]ヤエノムテキ" to 4,
        "[ようこそ、トレセン学園へ！]駿川たづな" to 4,
    )

    val turn = 60
    val testCount = 10000
//    val selector = { SimpleActionSelector(StatusType.SPEED) }

    runBlocking {
        println(LocalDateTime.now())
        val selector = { FactorBasedActionSelector(FactorBasedActionSelector.speedPower) }
        launch(context) {
            val summary = mutableListOf<io.github.mee1080.umasim.simulation.Summary>()
            repeat(testCount) {
                val simulator = Simulator(chara, support, Store.getTrainingList(scenario)).apply {
                    status = status.copy(motivation = 2)
                }
                summary.add(Runner.simulate(turn, simulator, selector()))
//                simulator.history.forEach { println(it) }
//                simulator.history.forEach {
//                    if (it is Action.Training) {
//                        println(it.support.joinToString("/") { "${it.card.name}=${it.friendTraining}" })
//                    }
//                }
//                if ((it + 1) % (testCount / 100) == 0) println("$it/$testCount")
            }
            println("old,${Evaluator(summary).toSummaryString()}")
        }.join()
        println(LocalDateTime.now())
        val selector2 = { FactorBasedActionSelector2(FactorBasedActionSelector2.speedPower) }
        launch(context) {
            val summary = mutableListOf<Summary>()
            val simulator = io.github.mee1080.umasim.simulation2.Simulator(Scenario.URA, chara, support)
            repeat(testCount) {
                summary.add(simulator.simulate(turn, selector2(), SimulationEvents(
                    initialStatus = { it.copy(motivation = 2) }
                )))
//                simulator.history.forEach { println(it.first) }
//                simulator.history.forEach {
//                    val action = it.first
//                    if (action is io.github.mee1080.umasim.simulation2.Action.Training) {
//                        println(action.member.joinToString("/") { "${it.name}=${it.isFriendTraining(action.type)} ${it.supportState?.relation}" })
//                    }
//                }
//                if ((it + 1) % (testCount / 100) == 0) println("$it/$testCount")
            }
            println("new,${Evaluator(summary).toSummaryString()}")
        }.join()
        println(LocalDateTime.now())
    }
}

fun testAoharuSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        *(speed(4, 3)),
        *(wisdom(4, 3)),
    )
    // TODO 友人サポカ対応
    val simulator = io.github.mee1080.umasim.simulation2.Simulator(
        Scenario.AOHARU, chara, support, io.github.mee1080.umasim.simulation2.Simulator.Option(
            checkGoalRace = true,
        )
    )
    val result =
        simulator.simulateWithHistory(78, FactorBasedActionSelector2(FactorBasedActionSelector2.aoharuSpeedWisdom))
    result.second.forEachIndexed { index, (action, result, state) ->
        println("${turnToString(state.turn)}: ${action.toShortString()}")
        println(state.status)
        println(state.teamStatusRank.map { "${it.key}:${it.value.rank}" }.joinToString(" "))
        println(state.training.joinToString(" ") { "${it.type}:${it.level}" })
        state.member.forEach {
            val aoharuState = it.scenarioState as AoharuMemberState
            println("${it.name}: ${aoharuState.status.toShortString()}/${aoharuState.maxStatus.toShortString()} ${aoharuState.aoharuTrainingCount}")
        }
        println(result)
        println()
    }
}

fun compareAoharuSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        *(speed(4, 3)),
        *(wisdom(4, 3)),
    )

    val turn = 78
    val testCount = 1
    val selector = { SimpleActionSelector(StatusType.SPEED) }
//    val selector = { FactorBasedActionSelector2(FactorBasedActionSelector2.speedWisdom) }

    runBlocking {
        println(LocalDateTime.now())
        launch(context) {
            val summary = mutableListOf<Summary>()
            val simulator = io.github.mee1080.umasim.simulation2.Simulator(Scenario.URA, chara, support)
            repeat(testCount) {
                summary.add(simulator.simulate(turn, selector(), SimulationEvents(
                    initialStatus = { it.copy(motivation = 2) }
                )))
            }
            println("URA,${Evaluator(summary).toSummaryString()}")
            summary.last().support.forEach { println("${it.name} ${it.state.supportState} ${it.state.scenarioState}") }
        }.join()
        println(LocalDateTime.now())
        launch(context) {
            val summary = mutableListOf<Summary>()
            val simulator = io.github.mee1080.umasim.simulation2.Simulator(Scenario.AOHARU, chara, support)
            repeat(testCount) {
                summary.add(simulator.simulate(turn, selector(), SimulationEvents(
                    initialStatus = { it.copy(motivation = 2) }
                )))
            }
            println("Aoharu,${Evaluator(summary).toSummaryString()}")
            summary.last().support.forEach { println("${it.name} ${it.state.supportState} ${it.state.scenarioState}") }
        }.join()
        println(LocalDateTime.now())
    }
}
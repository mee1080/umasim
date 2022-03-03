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

import io.github.mee1080.umasim.ai.ClimaxFactorBasedActionSelector
import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.Simulator


fun singleSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック",
        "[必殺！Wキャロットパンチ！]ビコーペガサス",
        "[はやい！うまい！はやい！]サクラバクシンオー",
        "[ようこそ、トレセン学園へ！]駿川たづな",
        "[『愛してもらうんだぞ』]オグリキャップ",
        "[一粒の安らぎ]スーパークリーク",
//        "[感謝は指先まで込めて]ファインモーション",
    )
    println(chara)
    println(support)
    val selector = FactorBasedActionSelector2(
        FactorBasedActionSelector2.Option(
            speedFactor = 0.9,
            staminaFactor = 0.8,
            wisdomFactor = 0.5,
        )
    )
    val result = Simulator(Scenario.URA, chara, support).simulateWithHistory(78, selector)
    result.second.forEachIndexed { index, history ->
        println("${index + 1}:")
        println(" ${history.action.toShortString()}")
        println(" ${history.state.status}")
        println(" ${history.status}")
    }
    println(result.first)
    println(result.first.status)
}

fun dataCheck() {
    Store.getTrainingList(Scenario.URA).forEach { println(it) }
    Store.getTrainingList(Scenario.AOHARU).forEach { println(it) }
    Store.charaList.distinctBy { it.charaId }.forEach {
        println("${it.charaName} ${Store.getGoalRaceList(it.charaId).joinToString(",")}")
    }
    Store.raceMap.forEachIndexed { turn, list ->
        println("$turn:")
        list.forEach { println("  $it") }
    }
}

//fun calcExpected() {
//    val chara = Store.getChara("ハルウララ", 5, 5)
//    val support = Store.getSupportByName(
//        "[迫る熱に押されて]キタサンブラック" to 4,
//        "[必殺！Wキャロットパンチ！]ビコーペガサス" to 4,
//        "[はやい！うまい！はやい！]サクラバクシンオー" to 4,
//        "[『愛してもらうんだぞ』]オグリキャップ" to 4,
//        "[押して忍べど燃ゆるもの]ヤエノムテキ" to 4,
//        "[ようこそ、トレセン学園へ！]駿川たづな" to 4,
//    ).mapIndexed { index, supportCard -> Support(index, supportCard).apply { friendTrainingEnabled = true } }
//    println(chara)
//    support.forEach { println(it) }
//    val result =
//        Calculator.calcExpectedTrainingStatus(chara, Store.getTraining(scenario, StatusType.SPEED), 5, 2, support)
//    println(result.first)
//    result.second.forEach { println("${(it.first * 10000).roundToInt() / 100.0}% : ${it.second}") }
//    println(result.second.sumOf { it.first })
//}


fun singleClimaxSimulation() {
    val chara = Store.getChara("[秋桜ダンツァトリーチェ]ゴールドシチー", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック",
        "[袖振り合えば福となる♪]マチカネフクキタル",
        "[はやい！うまい！はやい！]サクラバクシンオー",
        "[一粒の安らぎ]スーパークリーク",
        "[その背中を越えて]サトノダイヤモンド",
        "[今ぞ盛りのさくら花]サクラチヨノオー",
    )
    println(chara)
    println(support)
    val selector = ClimaxFactorBasedActionSelector()
    val result =
        Simulator(Scenario.CLIMAX, chara, support).simulateWithHistory(78, selector)
    result.second.forEachIndexed { index, history ->
        println("${index + 1}:")
        println(" ${history.state.status}")
        println(" coin:${history.state.shopCoin} ${history.state.member.joinToString { "${it.charaName}=${it.relation}" }}")
        println(" ${history.useItem.joinToString { it.name }}")
        println(" ${history.action.toShortString()}")
        println(" ${history.status}")
    }
    println(result.first)
    println(result.first.status)
    println(result.second.flatMap { it.useItem }.groupBy { it.name }.mapValues { it.value.count() })
}
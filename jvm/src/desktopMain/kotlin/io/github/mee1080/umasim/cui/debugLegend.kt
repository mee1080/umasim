/*
 * Copyright 2024 mee1080
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

import io.github.mee1080.umasim.ai.LegendActionSelector
import io.github.mee1080.umasim.ai.generator
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.legend.LegendScenarioEvents
import io.github.mee1080.umasim.scenario.legend.getLegendBuff
import io.github.mee1080.umasim.simulation2.MultipleAction
import io.github.mee1080.umasim.simulation2.RandomEvents
import io.github.mee1080.umasim.simulation2.Runner
import io.github.mee1080.umasim.simulation2.Simulator
import kotlinx.coroutines.runBlocking

fun debugLegend() {
//    debugLegendSingleSimulation()
//    debugLegendRepeatSimulation(1000)
}

fun debugLegendSingleSimulation() {
    val chara = Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5)
    val support = Store.getSupportByName(
        "[Devilish Whispers]スティルインラブ",
        "[大望は飛んでいく]エルコンドルパサー",
        "[Cocoon]エアシャカール",
        "[大地と我らのアンサンブル]サウンズオブアース",
        "[COOL⇔CRAZY/Buddy]シンボリクリスエス",
        "[Take Them Down!]ナリタタイシン",
    )
    println(chara.name)
    println(support.joinToString(", ") { it.name })
    val selector = LegendActionSelector.Option().generateSelector()
    val factor = listOf(
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
    )
    val result = runBlocking {
        Simulator(Scenario.LEGEND, chara, support, factor)
            .simulateWithHistory(selector) { RandomEvents(it) }
    }
    result.second.forEachIndexed { index, history ->
        println()
        println("${index + 1}:")
        println("  開始時: ${history.beforeActionState.status.toShortString()}")
        println("  トレLv: ${history.beforeActionState.training.map { "${it.type}${it.level} " }}")
        println("  Legend: ${history.beforeActionState.legendStatus?.toString()}")
        history.selections.forEach { (selection, selectedAction, result) ->
            println()
            selection.forEach { action ->
                println("  ・${action.name}")
                val total = action.candidates.sumOf { it.second } / 100.0
                action.candidates.forEach {
                    println("    ${it.second / total}% ${it.first}")
                }
                action.infoToString().split("/").forEach {
                    if (it.isNotEmpty()) println("    $it")
                }
                println()
            }
            println("  -> ${selectedAction.name}")
            if (selectedAction is MultipleAction) {
                println("     結果: $result")
            }
        }
        println()
        println("  終了時: ${(history.afterTurnState.status).toShortString()}")
        println("  Legend: ${history.beforeActionState.legendStatus?.toString()}")
    }
    println(result.first)
    println(result.first.status.toShortString())
}

fun debugLegendRepeatSimulation(count: Int) {
    val chara = Store.getChara("[リアライズ・ルーン]スイープトウショウ", 5, 5)
    val support = Store.getSupportByName(
        "[世界を変える眼差し]アーモンドアイ",
        "[Devilish Whispers]スティルインラブ",
        "[Cocoon]エアシャカール",
        "[そして幕は上がる]ダンツフレーム",
        "[Take Them Down!]ナリタタイシン",
        "[導きの光]伝説の体現者",
    )
    println(chara.name)
    println(support.joinToString(", ") { it.name })
    repeat(count) {
        val selector = LegendActionSelector.s2h2w1.generator().generateSelector()
        val factor = listOf(
            StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
            StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.STAMINA to 3,
        )
        val result = runBlocking {
            Simulator(Scenario.LEGEND, chara, support, factor)
                .simulateWithHistory(selector, {
                    LegendScenarioEvents(
                        listOf(
                            "トーク術", "交渉術", "素敵なハーモニー", "極限の集中",
                            "絆が奏でるハーモニー", "怪物チャンスマイル♪", "絆が織りなす光", "集いし理想",
                            "高潔なる魂", "百折不撓", "飽くなき挑戦心",
                        ).map { getLegendBuff(it) }
                    )
                }) { RandomEvents(it) }
        }
        println(result.first.status.toShortString())
    }
}

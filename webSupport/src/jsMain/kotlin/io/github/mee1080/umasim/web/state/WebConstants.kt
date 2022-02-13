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
package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
import io.github.mee1080.umasim.data.*

object WebConstants {

    val notSelected = -1 to "未選択"

    val scenarioList = Scenario.values().map { it.ordinal to it.displayName }

    val charaList =
        listOf(Chara.empty()) + Store.charaList.filter { it.rank == 5 && it.rarity == 5 }.sortedBy { it.charaName }

    val charaMap = charaList.associateBy { it.id }

    val displayCharaList =
        charaList.map { it.id to "${it.name} (${it.speedBonus},${it.staminaBonus},${it.powerBonus},${it.gutsBonus},${it.wisdomBonus})" }

    val supportMap = Store.supportList.groupBy { it.id }

    val displaySupportList =
        listOf(Triple(notSelected.first, notSelected.second, notSelected.second)) + supportMap.entries
            .map { it.key to it.value[0] }
            .sortedBy { it.second.type.ordinal * 10000000 - it.second.rarity * 1000000 + it.first }
            .map { (_, card) -> getDisplayItem(card) }

    fun getDisplayItem(card: SupportCard) = Triple(
        card.id,
        getRarityText(card) + " " + card.name,
        card.type.displayName
    )

    fun getRarityText(card: SupportCard) = when (card.rarity) {
        1 -> "R"
        2 -> "SR"
        3 -> "SSR"
        else -> "?"
    }

    val supportTalentList = listOf(0, 1, 2, 3, 4).map { it to it.toString() }

    val displayTrainingTypeList = trainingType.map { it.ordinal to it.displayName }

    val trainingLevelList = listOf(1, 2, 3, 4, 5).map { it to it.toString() }

    val motivationList = listOf(2 to "絶好調", 1 to "好調", 0 to "普通", -1 to "不調", -2 to "絶不調")

    val trainingInfo = Scenario.values().associateWith { Store.getTrainingInfo(it) }

    val trainingList = Scenario.values().associateWith { Store.getTrainingList(it) }

    val simulationModeList = mapOf(
        Scenario.URA to listOf(
            "スピ3パワ3" to { FactorBasedActionSelector2.speedPower.generateSelector() },
            "スピ3パワ3中距離" to { FactorBasedActionSelector2.speedPowerMiddle.generateSelector() },
            "スピ3賢3" to { FactorBasedActionSelector2.speedWisdom.generateSelector() },
            "スピ3スタ3" to { FactorBasedActionSelector2.speedStamina.generateSelector() },
            "パワ3賢3" to { FactorBasedActionSelector2.aoharuPowerWisdom.generateSelector() },
            "スピ2パワ3賢1" to { FactorBasedActionSelector2.speed2Power3Wisdom1.generateSelector() },
        ), Scenario.AOHARU to listOf(
            "スピ2パワ3賢1" to { FactorBasedActionSelector2.aoharuSpeed2Power3Wisdom1.generateSelector() },
            "スピ2賢2代理ライス" to { FactorBasedActionSelector2.aoharuSpeed2Power1Wisdom2Friend1Optuna3.generateSelector() },
            "スピ2スタ1賢3" to { FactorBasedActionSelector2.aoharuSpeed2Stamina1Wisdom3.generateSelector() },
            "スピ1スタ1賢1代理デジタル" to { FactorBasedActionSelector2.aoharuSpeed2Stamina1Power1Wisdom1Friend1Optuna.generateSelector() },
        )
    )

    val displaySimulationModeList =
        simulationModeList.mapValues { it.value.mapIndexed { index, pair -> index to pair.first } }

    val specialTurnList = mapOf(
        Scenario.AOHARU to listOf(
            17 to "ジュニア9月後半加入",
            23 to "ジュニア12月後半レース＆加入",
            33 to "クラシック6月後半レース＆加入",
            43 to "クラシック12月後半レース＆加入",
            53 to "シニア6月後半レース",
            63 to "シニア12月後半レース",
        )
    )

}
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

import io.github.mee1080.umasim.ai.FactorBasedActionSelector
import io.github.mee1080.umasim.ai.SimpleActionSelector
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
            "スピパワ" to { FactorBasedActionSelector.speedPower.generateSelector() },
            "スピ賢" to { FactorBasedActionSelector.speedWisdom.generateSelector() },
            "スピ賢パワ" to { FactorBasedActionSelector.speedWisdomPower.generateSelector() },
            "スピスタ" to { FactorBasedActionSelector.speedStamina.generateSelector() },
            "パワ賢" to { FactorBasedActionSelector.powerWisdom.generateSelector() },
            "スピ根性" to { FactorBasedActionSelector.speedGuts.generateSelector() },
            "バクシン(スピード)" to { SimpleActionSelector(StatusType.SPEED) },
            "バクシン(スタミナ)" to { SimpleActionSelector(StatusType.STAMINA) },
            "バクシン(パワー)" to { SimpleActionSelector(StatusType.POWER) },
            "バクシン(根性)" to { SimpleActionSelector(StatusType.GUTS) },
            "バクシン(賢さ)" to { SimpleActionSelector(StatusType.WISDOM) },
        ), Scenario.AOHARU to listOf(
            "スピ賢" to { FactorBasedActionSelector.aoharuSpeedWisdom.generateSelector() },
            "パワ賢" to { FactorBasedActionSelector.aoharuPowerWisdom.generateSelector() },
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
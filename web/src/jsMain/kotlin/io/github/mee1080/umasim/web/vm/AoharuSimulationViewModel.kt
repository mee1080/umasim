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
package io.github.mee1080.umasim.web.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.mee1080.umasim.ai.FactorBasedActionSelector
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation.ExpectedStatus
import io.github.mee1080.umasim.simulation.Runner
import io.github.mee1080.umasim.simulation.Simulator
import io.github.mee1080.umasim.simulation.Team

class AoharuSimulationViewModel(private val root: ViewModel) {

    var history by mutableStateOf(emptyList<HistoryItem>())
        private set

    private val specialTurnList = listOf(
        17 to "ジュニア9月後半加入",
        23 to "ジュニア12月後半レース＆加入",
        33 to "クラシック6月後半レース＆加入",
        43 to "クラシック12月後半レース＆加入",
        53 to "シニア6月後半レース",
        63 to "シニア12月後半レース",
    )

    data class HistoryItem(
        val action: String,
        val charaStatus: Status,
        val teamTotalStatus: Status,
        val teamAverageStatus: ExpectedStatus,
        val teamStatusRank: Map<StatusType, AoharuTeamStatusRank>,
    ) {
        constructor(action: String, charaStatus: Status, team: Team) : this(
            action,
            charaStatus,
            team.totalStatus(charaStatus),
            team.averageStatus(charaStatus),
            team.statusRank(charaStatus),
        )

        val next = trainingType.associateWith { type ->
            val nextRank = teamStatusRank[type]!!.next
            if (nextRank == null) 0.0 else nextRank.threshold - teamAverageStatus.get(type)
        }
    }

    private val simulationModeList = listOf(
        "スピ賢" to { FactorBasedActionSelector.aoharuSpeedWisdom.generateSelector() },
        "パワ賢" to { FactorBasedActionSelector.aoharuPowerWisdom.generateSelector() },
    )

    val displaySimulationModeList = simulationModeList.mapIndexed { index, pair -> index to pair.first }

    var simulationMode by mutableStateOf(0)
        private set

    fun updateSimulationMode(mode: Int) {
        simulationMode = mode
    }

    fun doSimulation() {
        val supportList = root.supportSelectionList.mapNotNull { it.card }
        val simulator = Simulator(root.chara, supportList, root.trainingList[Scenario.AOHARU]!!)
        Runner.simulate(65, simulator, simulationModeList[simulationMode].second()) { target ->
            if (target.turn > 0) {
                target.status += Status(2, 2, 2, 2, 2)
            }
            when (target.turn) {
                18 -> target.trainingInfo.values.forEach { it.applyTeamRank(Store.Aoharu.teamStatusRank["E"]!!) }
                28 -> target.trainingInfo.values.forEach { it.applyTeamRank(Store.Aoharu.teamStatusRank["C"]!!) }
                42 -> target.trainingInfo.values.forEach { it.applyTeamRank(Store.Aoharu.teamStatusRank["A"]!!) }
                54 -> target.trainingInfo.values.forEach { it.applyTeamRank(Store.Aoharu.teamStatusRank["S"]!!) }
            }
        }
        val linkCount = supportList.count {
            it.type == StatusType.FRIEND || Store.isScenarioLink(Scenario.AOHARU, it.chara)
        }
        val newHistory = mutableListOf<HistoryItem>()
        val team = Team(supportList)
        Store.scenarioLink[Scenario.AOHARU]!!.forEach {
            team.addGuest(Store.Aoharu.getGuest(it)!!)
        }
        newHistory.add(HistoryItem("初期", simulator.history[0].status, team))
        specialTurnList.forEachIndexed { index, (turn, name) ->
            val action = simulator.history[turn]
            if (index >= 1) {
                team.addRaceBonus(50)
            }
            val item = HistoryItem(name, action.status, team)
            when (index) {
                0 -> team.addGuest(9 - team.memberCount, item.charaStatus)
                1 -> team.addGuest(13 - team.memberCount, item.charaStatus)
                2 -> team.addGuest(15 - team.memberCount, item.charaStatus)
                3 -> team.addGuest(19 - team.memberCount, item.charaStatus)
            }
            newHistory.add(item)
        }
//        for (i in 0..simulator.history.size) {
//            newHistory.add(
//                HistoryItem(
//                    if (i == 0) "初期" else simulator.history[i - 1].name,
//                    if (i == simulator.history.size) simulator.status else simulator.history[i].status,
//                )
//            )
//        }
        history = newHistory
    }
}
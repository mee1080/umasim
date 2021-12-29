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

import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation.Runner
import io.github.mee1080.umasim.simulation.Simulator
import io.github.mee1080.umasim.simulation.Team
import io.github.mee1080.umasim.web.state.AoharuSimulationState
import io.github.mee1080.umasim.web.state.HistoryItem
import io.github.mee1080.umasim.web.state.WebConstants

class AoharuSimulationViewModel(val root: ViewModel) {

    private fun updateAoharuState(update: (AoharuSimulationState) -> AoharuSimulationState) {
        root.state = root.state.copy(aoharuSimulationState = update(root.state.aoharuSimulationState))
    }

    private fun toHistoryItem(action: String, charaStatus: Status, team: Team) = HistoryItem(
        action,
        charaStatus,
        team.totalStatus(charaStatus),
        team.averageStatus(charaStatus),
        team.statusRank(charaStatus),
    )

    fun updateSimulationMode(mode: Int) {
        updateAoharuState { it.copy(simulationMode = mode) }
    }

    fun doSimulation() {
        val state = root.state
        val aoharuState = state.aoharuSimulationState
        val supportList = state.supportSelectionList.mapNotNull { it.card }
        val simulator = Simulator(state.chara, supportList, WebConstants.trainingList[Scenario.AOHARU]!!)
        Runner.simulate(
            aoharuState.simulationTurn,
            simulator,
            WebConstants.simulationModeList[Scenario.AOHARU]!![aoharuState.simulationMode].second()
        ) { target ->
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
        newHistory.add(toHistoryItem("初期", simulator.history[0].status, team))
        WebConstants.specialTurnList[Scenario.AOHARU]!!.forEachIndexed { index, (turn, name) ->
            val action = simulator.history[turn]
            if (index >= 1) {
                team.addRaceBonus(50)
            }
            val item = toHistoryItem(name, action.status, team)
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
        updateAoharuState { it.copy(simulationHistory = newHistory) }
    }
}
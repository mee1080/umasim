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

import io.github.mee1080.umasim.simulation2.ApproximateSimulationEvents
import io.github.mee1080.umasim.web.state.AoharuSimulationState
import io.github.mee1080.umasim.web.state.HistoryItem
import io.github.mee1080.umasim.web.state.WebConstants

class AoharuSimulationViewModel(val root: ViewModel) {

    private fun updateAoharuState(update: (AoharuSimulationState) -> AoharuSimulationState) {
        root.state = root.state.copy(aoharuSimulationState = update(root.state.aoharuSimulationState))
    }

//    private fun toHistoryItem(action: String, charaStatus: Status, team: Team) = HistoryItem(
//        action,
//        charaStatus,
//        team.totalStatus(charaStatus),
//        team.averageStatus(charaStatus),
//        team.statusRank(charaStatus),
//    )

    fun updateSimulationMode(mode: Int) {
        updateAoharuState { it.copy(simulationMode = mode) }
    }

    fun doSimulation() {
        val state = root.state
        val aoharuState = state.aoharuSimulationState
        val supportList = state.supportSelectionList.mapNotNull { it.card }
        val selector = WebConstants.simulationModeList[state.scenario]!![state.simulationMode].second()
        val (summary, history) = io.github.mee1080.umasim.simulation2.Simulator(
            state.scenario,
            state.chara,
            supportList,
        ).simulateWithHistory(
            state.simulationTurn,
            selector,
        ) { ApproximateSimulationEvents() }
        val newHistory = mutableListOf<HistoryItem>()
        updateAoharuState { it.copy(simulationHistory = newHistory) }
    }
}
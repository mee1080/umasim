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
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation.Calculator
import io.github.mee1080.umasim.simulation.Runner
import io.github.mee1080.umasim.simulation.Simulator
import io.github.mee1080.umasim.simulation.Support
import io.github.mee1080.umasim.util.SaveDataConverter
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.SupportSelection
import io.github.mee1080.umasim.web.state.WebConstants
import kotlinx.browser.localStorage

class ViewModel {

    companion object {
        private const val KEY_CHARA = "umasim.chara"

        private const val KEY_SUPPORT_LIST = "umasim.support_list"
    }

    var state by mutableStateOf(State())

    private fun updateState(calculate: Boolean = true, calculateBonus: Boolean = true, update: (State) -> State) {
        var newState = update(state)
        if (calculate) newState = calculate(newState)
        if (calculateBonus) newState = calculateBonus(newState)
        state = newState
    }

    private fun updateSupportSelection(
        position: Int,
        calculateBonus: Boolean = true,
        update: (SupportSelection) -> SupportSelection
    ) {
        updateState(true, calculateBonus) {
            val newList = it.supportSelectionList.toMutableList()
            newList[position] = update(newList[position])
            it.copy(supportSelectionList = newList)
        }
    }

    val aoharuSimulationViewModel = AoharuSimulationViewModel(this)

    fun updateChara(id: Int) {
        updateState { it.copy(selectedChara = id) }
        localStorage.setItem(KEY_CHARA, id.toString())
    }

    fun updateSupportFilter(value: String) {
        updateState(calculate = false, calculateBonus = false) { it.copy(supportFilter = value) }
    }

    fun applyFilter() {
        if (state.supportFilterApplied) return
        val appliedSupportFilter = state.supportFilter
        val filters = appliedSupportFilter.split("[　%s]".toRegex())
        val filteredSupportList = if (appliedSupportFilter.isEmpty()) WebConstants.displaySupportList else {
            WebConstants.displaySupportList.filter { (id, _, _) ->
                WebConstants.supportMap[id]?.first()?.matches(filters) ?: false
            }
        }
        updateState(calculate = false, calculateBonus = false) {
            it.copy(
                appliedSupportFilter = appliedSupportFilter,
                filteredSupportList = filteredSupportList,
            )
        }
    }

    fun clearFilter() {
        updateState(calculate = false, calculateBonus = false) {
            it.copy(
                supportFilter = "",
                appliedSupportFilter = "",
                filteredSupportList = WebConstants.displaySupportList,
            )
        }
    }

    fun updateSupport(position: Int, id: Int) {
        updateSupportSelection(position) { it.copy(selectedSupport = id) }
    }

    fun updateSupportTalent(position: Int, talent: Int) {
        updateSupportSelection(position) { it.copy(supportTalent = talent) }
    }

    fun updateJoin(position: Int, join: Boolean) {
        updateSupportSelection(position, false) { it.copy(join = join) }
    }

    fun updateFriend(position: Int, friend: Boolean) {
        updateSupportSelection(position, false) { it.copy(friend = friend) }
    }

    fun updateScenario(scenarioIndex: Int) {
        updateState(calculateBonus = false) { it.copy(selectedScenario = scenarioIndex) }
    }

    fun updateTeamJoinCount(delta: Int) {
        if (delta + state.teamJoinCount in 0..5) {
            updateState(calculateBonus = false) { it.copy(teamJoinCount = it.teamJoinCount + delta) }
        }
    }

    fun updateTrainingType(trainingType: Int) {
        updateState(calculateBonus = false) { it.copy(selectedTrainingType = trainingType) }
    }

    fun updateTrainingLevel(trainingLevel: Int) {
        updateState(calculateBonus = false) { it.copy(trainingLevel = trainingLevel) }
    }

    fun updateMotivation(motivation: Int) {
        updateState(calculateBonus = false) { it.copy(motivation = motivation) }
    }

//    var trainingParamTest by mutableStateOf<TrainingParamTestModel?>(null)
//
//    fun updateTrainingParamTest(enabled: Boolean) {
//        trainingParamTest = if (enabled) TrainingParamTestModel() else null
//    }

    private fun calculate(state: State): State {
        val supportList = mutableListOf<Support>()
        state.supportSelectionList.filter { it.join }.forEachIndexed { index, selection ->
            val card = selection.card
            if (card != null) {
                supportList.add(Support(index, card).apply {
                    checkHintFriend(if (selection.friend) 100 else 0)
                })
            }
        }

        val trainingType = StatusType.values()[state.selectedTrainingType]
        val trainingResult = Calculator.calcTrainingSuccessStatus(
            state.chara,
            WebConstants.trainingInfo[state.scenario]!![trainingType]!!,
            state.trainingLevel,
            state.motivation,
            supportList,
            state.scenario,
            state.teamJoinCount,
        )
        val trainingImpact = supportList.map { target ->
            target.name to trainingResult - Calculator.calcTrainingSuccessStatus(
                state.chara,
                WebConstants.trainingInfo[state.scenario]!![trainingType]!!,
                state.trainingLevel,
                state.motivation,
                supportList.filter { it.index != target.index },
                state.scenario,
                state.teamJoinCount,
            )
        }
        val expectedResult = Calculator.calcExpectedTrainingStatus(
            state.chara,
            WebConstants.trainingInfo[state.scenario]!![trainingType]!!,
            state.trainingLevel,
            state.motivation,
            state.supportSelectionList
                .mapIndexedNotNull { index, selection ->
                    selection.card?.let {
                        Support(index, it).apply { friendTrainingEnabled = selection.friend }
                    }
                },
            state.scenario,
            state.teamJoinCount,
        ).first
//        trainingParamTest?.calculate(chara, trainingType, motivation, supportList)
        localStorage.setItem(
            KEY_SUPPORT_LIST,
            SaveDataConverter.supportListToString(state.supportSelectionList.map { it.toSaveInfo() })
        )
        return state.copy(
            trainingResult = trainingResult,
            trainingImpact = trainingImpact,
            expectedResult = expectedResult,
        )
    }

    private fun calculateBonus(state: State): State {
        var race = 0
        var fan = 0
        var status = Status()
        val hintMap = mutableMapOf<String, MutableList<String>>()
        state.chara.initialStatus.skillHint.keys.forEach { skill ->
            hintMap[skill] = mutableListOf("育成キャラ")
        }
        state.supportSelectionList.mapNotNull { it.card }.forEach { card ->
            race += card.race
            fan += card.fan
            status += card.initialStatus
            val skillCount = card.skills.size
            card.skills.forEach { skill ->
                hintMap.getOrPut(skill) { mutableListOf() }.add("${card.name} 1/$skillCount")
            }
        }
        return state.copy(
            totalRaceBonus = race,
            totalFanBonus = fan,
            initialStatus = status,
            availableHint = hintMap,
        )
    }

    fun updateSimulationMode(mode: Int) {
        updateState(calculate = false, calculateBonus = false) { it.copy(simulationMode = mode) }
    }

    fun updateSimulationTurn(turn: Int) {
        updateState(calculate = false, calculateBonus = false) { it.copy(simulationTurn = turn) }
    }

    fun doUraSimulation() {
        val supportList = state.supportSelectionList.mapNotNull { it.card }
        val simulator = Simulator(state.chara, supportList, WebConstants.trainingList[state.scenario]!!)
        Runner.simulate(
            state.simulationTurn,
            simulator,
            WebConstants.simulationModeList[Scenario.URA]!![state.simulationMode].second()
        )
        updateState(calculate = false, calculateBonus = false) {
            it.copy(
                simulationResult = simulator.status,
                simulationHistory = simulator.history.map { it.name },
            )
        }
    }

    init {
        updateState(calculate = false, calculateBonus = false) { state ->
            val selectedChara = localStorage.getItem(KEY_CHARA)?.let {
                if (WebConstants.charaMap.containsKey(it.toIntOrNull() ?: -1)) it.toInt() else 0
            } ?: state.selectedChara
            val newList = state.supportSelectionList.toMutableList()
            SaveDataConverter.stringToSupportList(localStorage.getItem(KEY_SUPPORT_LIST))
                .forEachIndexed { index, supportInfo ->
                    if (newList.indices.contains(index)) {
                        newList[index] = SupportSelection.fromSaveInfo(supportInfo)
                    }
                }
            state.copy(
                selectedChara = selectedChara,
                supportSelectionList = newList,
            )
        }
    }
}
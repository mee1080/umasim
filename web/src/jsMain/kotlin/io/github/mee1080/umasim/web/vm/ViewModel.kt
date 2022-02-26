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
import io.github.mee1080.umasim.simulation2.ApproximateSimulationEvents
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.Simulator
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
            WebConstants.displaySupportList.filter { (_, card) ->
                card?.matches(filters) ?: false
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

    fun updateSupportType(position: Int, type: StatusType) {
        updateSupportSelection(position) {
            if (it.statusType == type) it else {
                val card = WebConstants.getSupportList(type).firstOrNull()
                it.copy(selectedSupport = card?.first ?: -1)
            }
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

    fun updateRelation(position: Int, relation: Int) {
        updateSupportSelection(position, false) { it.copy(relation = relation) }
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

    fun updateFanCount(fanCount: String) {
        updateState(calculateBonus = false) { it.copy(fanCount = fanCount) }
    }

    fun updateShopItemMegaphone(index: Int) {
        updateState(calculateBonus = false) { it.copy(shopItemMegaphone = index) }
    }

    fun updateShopItemWeight(index: Int) {
        updateState(calculateBonus = false) { it.copy(shopItemWeight = index) }
    }

//    var trainingParamTest by mutableStateOf<TrainingParamTestModel?>(null)
//
//    fun updateTrainingParamTest(enabled: Boolean) {
//        trainingParamTest = if (enabled) TrainingParamTestModel() else null
//    }

    private fun calculate(state: State): State {
        val joinSupportList =
            state.supportSelectionList.filter { it.join && it.card != null }.map { it.card!! to it.relation }

        val trainingType = StatusType.values()[state.selectedTrainingType]
        val supportTypeCount = state.supportSelectionList.mapNotNull { it.card?.type }.distinct().size
        val fanCount = state.fanCount.toIntOrNull() ?: 1
        val trainingResult = Calculator.calcTrainingSuccessStatus(
            state.chara,
            WebConstants.trainingList[state.scenario]!!.first { it.type == trainingType && it.level == state.trainingLevel },
            state.motivation,
            joinSupportList,
            state.teamJoinCount,
            state.scenario,
            supportTypeCount,
            fanCount,
        )

        val itemList = listOf(
            WebConstants.shopItemMegaphone.getOrNull(state.shopItemMegaphone),
            WebConstants.shopItemWeight.getOrNull(state.shopItemWeight),
        ).filterNotNull()
        val trainingItemBonus = if (state.scenario != Scenario.CLIMAX) {
            Status()
        } else {
            Calculator.calcItemBonus(trainingType, trainingResult, itemList)
        }

        val trainingImpact = joinSupportList.mapIndexed { targetIndex, target ->
            target.first.name to trainingResult - Calculator.calcTrainingSuccessStatus(
                state.chara,
                WebConstants.trainingList[state.scenario]!!.first { it.type == trainingType && it.level == state.trainingLevel },
                state.motivation,
                joinSupportList.filterIndexed { index, _ -> index != targetIndex },
                state.teamJoinCount,
                state.scenario,
                supportTypeCount,
                fanCount,
            )
        }

        val expectedResult = Calculator.calcExpectedTrainingStatus(
            state.chara,
            WebConstants.trainingList[state.scenario]!!.first { it.type == trainingType && it.level == state.trainingLevel },
            state.motivation,
            state.supportSelectionList.filter { it.card != null }.map { it.card!! to it.relation },
            state.teamJoinCount,
            state.scenario,
            supportTypeCount,
            fanCount,
        )
        val total = trainingResult.statusTotal
        val upperRate = expectedResult.second.filter { it.second.statusTotal < total }
            .sumOf { it.first } / expectedResult.second.sumOf { it.first }
//        trainingParamTest?.calculate(chara, trainingType, motivation, supportList)
        localStorage.setItem(
            KEY_SUPPORT_LIST,
            SaveDataConverter.supportListToString(state.supportSelectionList.map { it.toSaveInfo() })
        )
        return state.copy(
            trainingResult = trainingResult,
            trainingItemBonus = trainingItemBonus,
            trainingImpact = trainingImpact,
            expectedResult = expectedResult.first,
            upperRate = upperRate,
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
        val selector = WebConstants.simulationModeList[state.scenario]!![state.simulationMode].second()
        val (summary, history) = Simulator(
            state.scenario,
            state.chara,
            supportList,
        ).simulateWithHistory(
            state.simulationTurn,
            selector,
            ApproximateSimulationEvents(),
        )
        updateState(calculate = false, calculateBonus = false) {
            it.copy(
                simulationResult = summary.status,
                simulationHistory = history.map { it.first.name to it.third.status },
            )
        }
    }

    init {
        updateState { state ->
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
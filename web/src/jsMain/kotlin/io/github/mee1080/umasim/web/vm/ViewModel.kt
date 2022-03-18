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
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.ApproximateSimulationEvents
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.util.SaveDataConverter
import io.github.mee1080.umasim.web.state.*
import kotlinx.browser.localStorage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ViewModel {

    companion object {
        private const val KEY_CHARA = "umasim.chara"

        private const val KEY_SUPPORT_LIST = "umasim.support_list"

        private const val KEY_SUPPORT_SET = "umasim.support_set"
    }

    var state by mutableStateOf(State())

    val rotationViewModel = RotationViewModel(this)

    fun navigate(page: Page) {
        state = state.copy(page = page)
    }

    private fun updateState(calculate: Boolean = true, calculateBonus: Boolean = true, update: (State) -> State) {
        var newState = update(state)
        if (calculate) newState = calculate(newState)
        if (calculateBonus) newState = calculateBonus(newState)
        state = newState
    }

    private fun updateSupportSelection(
        position: Int, calculateBonus: Boolean = true, update: (SupportSelection) -> SupportSelection
    ) {
        updateState(true, calculateBonus) {
            val newList = it.supportSelectionList.toMutableList()
            newList[position] = update(newList[position])
            it.copy(supportSelectionList = newList)
        }
    }

    fun updateChara(id: Int) {
        updateState { it.copy(selectedChara = id).createRaceSetting() }
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

    fun updateSorOrder(value: WebConstants.SortOrder<*>) {
        updateState(calculate = false, calculateBonus = false) { it.copy(supportSortOrder = value) }
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

    fun updatePassion(position: Int, passion: Boolean) {
        updateSupportSelection(position, false) { it.copy(passion = passion) }
    }

    fun updateScenario(scenarioIndex: Int) {
        updateState(calculateBonus = false) { it.copy(selectedScenario = scenarioIndex).createRaceSetting() }
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
            state.supportSelectionList.filter { it.join && it.card != null }
                .map { Triple(it.card!!, it.relation, it.passion) }

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

        val itemList = listOfNotNull(
            WebConstants.shopItemMegaphone.getOrNull(state.shopItemMegaphone),
            WebConstants.shopItemWeight.getOrNull(state.shopItemWeight),
        )
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
            state.supportSelectionList.filter { it.card != null }.map { Triple(it.card!!, it.relation, it.passion) },
            state.teamJoinCount,
            state.scenario,
            supportTypeCount,
            fanCount,
        )
        val total = trainingResult.statusTotal
        val upperRate = expectedResult.second.filter { it.second.statusTotal < total }
            .sumOf { it.first } / expectedResult.second.sumOf { it.first }
//
//        val raceBonus = 100 + state.supportSelectionList.sumOf { it.card?.race ?: 0 }
//        val raceScore: (Double) -> Double = {
//            10 * raceBonus / 100 + 35 * raceBonus / 100 * 0.4 + 100 * it
//        }
//        val coinRate =
//            (trainingResult.statusTotal + trainingResult.skillPt * 0.4 - 8 * raceBonus / 100 - 25 * raceBonus / 100 * 0.4) / 100.0
//
//
//        val coinRate = WebConstants.shopItemMegaphone.getOrNull(state.shopItemMegaphone)?.let { megaPhone ->
//            // 倍率×現在トレ上昇値－MAX（現在トレ上昇値,レース上昇値）＋（ターン数－１）×（倍率×トレ期待値－MAX（トレ上昇値,レース上昇値）の期待値）－価格
//            val expectedRateTotal = expectedResult.second.sumOf { it.first }
//            BinarySearcher.run(0.0, 2.0, 0.01, 0.0) { rate ->
//                megaPhone.trainingFactor / 100.0 * trainingResult.statusTotal -
//                        max(
//                            trainingResult.statusTotal.toDouble(),
//                            raceScore(rate)
//                        ) + (megaPhone.turn - 1) * (
//                        megaPhone.trainingFactor / 100.0 * expectedResult.first.statusTotal -
//                                expectedResult.second.sumOf {
//                                    it.first / expectedRateTotal * max(
//                                        it.second.statusTotal.toDouble(),
//                                        raceScore(rate)
//                                    )
//                                }
//                        )
//            }
//        } ?: 0.0

        localStorage.setItem(
            KEY_SUPPORT_LIST, SaveDataConverter.supportListToString(state.supportSelectionList.map { it.toSaveInfo() })
        )
        return state.copy(
            trainingResult = trainingResult,
            trainingItemBonus = trainingItemBonus,
            trainingImpact = trainingImpact,
            expectedResult = expectedResult.first,
            upperRate = upperRate,
//            coinRate = coinRate,
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

    private fun State.createRaceSetting(): State {
        val list = mutableListOf<RaceSetting>()
        if (scenario == Scenario.CLIMAX) {
            val item = WebConstants.raceItem.keys.associateWith { "0" }
            list.add(RaceSetting("メイクデビュー", 1, 3, 3, 15, false))
            list.add(RaceSetting("目標外 OP/Pre-OP", 0, 5, 1, 20, true, item))
            list.add(RaceSetting("目標外 GII/GIII", 0, 8, 1, 25, true, item))
            list.add(RaceSetting("目標外 GI", 0, 10, 1, 35, true, item))
            list.add(RaceSetting("クライマックス", 3, 10, 5, 30, false, item))
        } else {
            list.add(RaceSetting("メイクデビュー", 1, 3, 3, 30, false))
            val goalRace = Store.getGoalRaceList(chara.charaId).groupBy { it.grade }.mapValues { it.value.size }
            console.log("createRaceSetting $goalRace")
            console.log("createRaceSetting ${Store.getGoalRaceList(chara.charaId)}")
            val openCount = goalRace.filterKeys { it == RaceGrade.OPEN || it == RaceGrade.PRE_OPEN }.values.sum()
            if (openCount > 0) {
                list.add(RaceSetting("目標 OP/Pre-OP", openCount, 3, 3, 30, false))
            }
            list.add(RaceSetting("目標外 OP/Pre-OP", 0, 5, 1, 30, true))
            val g2g3Count = goalRace.filterKeys { it == RaceGrade.G2 || it == RaceGrade.G3 }.values.sum()
            if (g2g3Count > 0) {
                list.add(RaceSetting("目標 GII/GIII", g2g3Count, 3, 4, 35, false))
            }
            list.add(RaceSetting("目標外 GII/GIII", 0, 8, 1, 35, true))
            val g1Count = goalRace.getOrElse(RaceGrade.G1) { 0 }
            if (g1Count > 0) {
                list.add(RaceSetting("目標 GI", g1Count, 3, 5, 45, false))
            }
            list.add(RaceSetting("目標外 GI", 0, 10, 1, 45, true))
            if (scenario == Scenario.AOHARU) {
                list.add(RaceSetting("アオハル杯 第1戦", 1, 3, 5, 10, false))
                list.add(RaceSetting("アオハル杯 第2戦", 1, 3, 5, 15, false))
                list.add(RaceSetting("アオハル杯 第3戦", 1, 4, 5, 20, false))
                list.add(RaceSetting("アオハル杯 第4戦", 1, 5, 5, 25, false))
                list.add(RaceSetting("アオハル杯 決勝", 1, 7, 5, 50, false))
            }
            list.add(RaceSetting("ファイナルズ 予選", 1, 10, 5, 40, false))
            list.add(RaceSetting("ファイナルズ 準決勝", 1, 10, 5, 60, false))
            list.add(RaceSetting("ファイナルズ 決勝", 1, 10, 5, 80, false))
        }
        return copy(raceSetting = list)
    }

    fun updateRaceCount(race: RaceSetting, count: String) {
        updateState(calculate = false, calculateBonus = false) { state ->
            state.copy(raceSetting = state.raceSetting.map {
                if (it.label == race.label) {
                    it.copy(raceCount = count)
                } else {
                    it
                }
            })
        }
    }

    fun updateRaceItemCount(race: RaceSetting, label: String, count: String) {
        updateState(calculate = false, calculateBonus = false) { state ->
            state.copy(raceSetting = state.raceSetting.map {
                if (it.label == race.label) {
                    val newItem = it.item.toMutableMap()
                    newItem[label] = count
                    it.copy(item = newItem)
                } else {
                    it
                }
            })
        }
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
                simulationHistory = history.map { it.action.name to it.state.status },
            )
        }
    }

    fun updateSupportSaveName(name: String) {
        updateState(calculate = false, calculateBonus = false) {
            it.copy(supportSaveName = name)
        }
    }

    fun updateSupportLoadName(name: String) {
        updateState(calculate = false, calculateBonus = false) {
            it.copy(supportLoadName = name)
        }
    }

    fun saveSupport() {
        val name = state.supportSaveName
        if (name.isEmpty()) return
        val support = SaveDataConverter.supportListToString(state.supportSelectionList.map { it.toSaveInfo() })
        localStorage.setItem(
            KEY_SUPPORT_SET, Json.encodeToString(loadSupportData().apply { put(name, support) })
        )
        if (!state.supportLoadList.contains(name)) {
            updateState(calculate = false, calculateBonus = false) {
                it.copy(
                    supportLoadList = it.supportLoadList.toMutableList().apply { add(name) }.sorted(),
                    supportLoadName = name,
                )
            }
        }
    }

    fun loadSupport() {
        val support = loadSupportData()[state.supportLoadName] ?: return
        val newList = state.supportSelectionList.toMutableList()
        SaveDataConverter.stringToSupportList(support)
            .forEachIndexed { index, supportInfo ->
                if (newList.indices.contains(index)) {
                    newList[index] = SupportSelection.fromSaveInfo(supportInfo)
                }
            }
        updateState { it.copy(supportSelectionList = newList, supportSaveName = state.supportLoadName) }
    }

    private fun loadSupportData(): MutableMap<String, String> {
        val data = localStorage.getItem(KEY_SUPPORT_SET) ?: return mutableMapOf()
        return Json.decodeFromString(data)
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
            val supportLoadList = loadSupportData().keys.sorted()
            state.copy(
                selectedChara = selectedChara,
                supportSelectionList = newList,
                supportLoadList = supportLoadList,
                supportLoadName = supportLoadList.firstOrNull() ?: "",
            ).createRaceSetting()
        }
    }
}
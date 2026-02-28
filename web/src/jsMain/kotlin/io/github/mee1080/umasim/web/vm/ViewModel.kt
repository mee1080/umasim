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

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.climax.MegaphoneItem
import io.github.mee1080.umasim.scenario.climax.WeightItem
import io.github.mee1080.umasim.scenario.gm.Founder
import io.github.mee1080.umasim.scenario.live.LiveCalculator
import io.github.mee1080.umasim.scenario.mecha.MechaCalculator
import io.github.mee1080.umasim.scenario.uaf.UafAthleticsLevelCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.util.SaveDataConverter
import io.github.mee1080.umasim.web.state.*
import io.github.mee1080.utility.applyIf
import io.github.mee1080.utility.applyIfNotNull
import kotlinx.browser.localStorage
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

@Stable
class ViewModel(val scope: CoroutineScope, initialPage: String?) {

    companion object {
        private const val KEY_CHARA = "umasim.chara"

        private const val KEY_SUPPORT_LIST = "umasim.support_list"

        private const val KEY_SUPPORT_SET = "umasim.support_set"
    }

    var state by mutableStateOf(State())

    val rotationViewModel = RotationViewModel(this)

    val lessonViewModel = LessonViewModel(this)

    val graphViewModel = GraphViewModel(this)

    val bcViewModel = BCViewModel(this)

    fun navigate(page: Page) {
        state = state.copy(page = page)
    }

    fun update(update: State.() -> State) {
        updateState { it.update() }
    }

    fun updateExpectedState(update: ExpectedState.() -> ExpectedState) {
        updateState { it.copy(expectedState = it.expectedState.update()) }
    }

    private fun updateState(calculate: Boolean = true, update: (State) -> State) {
        scope.launch {
            var newState = update(state)
            if (calculate) {
                newState = calculate(newState)
                newState = calculateBonus(newState)
            }
            state = newState
        }
    }

    private suspend fun suspendUpdateState(calculate: Boolean = true, update: (State) -> State) {
        withContext(Dispatchers.Main) {
            var newState = update(state)
            if (calculate) {
                newState = calculate(newState)
                newState = calculateBonus(newState)
            }
            state = newState
            delay(100L)
        }
    }

    private fun updateSupportSelection(
        position: Int, update: (SupportSelection) -> SupportSelection
    ) {
        updateState(true) {
            val newList = it.supportSelectionList.toMutableList()
            newList[position] = update(newList[position])
            it.copy(supportSelectionList = newList)
        }
    }

    fun updateChara(chara: Chara) {
        updateState { it.copy(chara = chara).createRaceSetting() }
        localStorage.setItem(KEY_CHARA, chara.id.toString())
    }

    fun updateSupportFilter(value: String) {
        updateState(calculate = false) { it.copy(supportFilter = value) }
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
        updateState(calculate = false) {
            it.copy(
                appliedSupportFilter = appliedSupportFilter,
                filteredSupportList = filteredSupportList,
            )
        }
    }

    fun clearFilter() {
        updateState(calculate = false) {
            it.copy(
                supportFilter = "",
                appliedSupportFilter = "",
                filteredSupportList = WebConstants.displaySupportList,
            )
        }
    }

    fun updateSorOrder(value: WebConstants.SortOrder<*>) {
        updateState(calculate = false) { it.copy(supportSortOrder = value) }
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
        updateSupportSelection(position) { it.copy(join = join) }
    }

    fun updateRelation(position: Int, relation: Int) {
        updateSupportSelection(position) { it.copy(relation = relation) }
    }

    fun updatePassion(position: Int, passion: Boolean) {
        updateSupportSelection(position) { it.copy(passion = passion) }
    }

    fun updateFriendCount(position: Int, friendCount: Int) {
        updateSupportSelection(position) { it.copy(friendCount = friendCount) }
    }

    fun updateScenario(scenario: Scenario) {
        updateState {
            it.copy(scenario = scenario)
                .createRaceSetting()
                .applyIf(scenario == Scenario.URA && it.teamJoinCount > 1) {
                    copy(teamJoinCount = 1)
                }
        }
    }

    fun updateTeamJoinCount(teamJoinCount: Int) {
        updateState { it.copy(teamJoinCount = teamJoinCount) }
    }

    fun updateSpecialMember(join: Boolean) {
        updateState { it.copy(teamJoinCount = if (join) 1 else 0) }
    }

    fun updateTrainingType(trainingType: StatusType) {
        updateState { it.copy(selectedTrainingType = trainingType) }
    }

    fun updateTrainingLevel(trainingLevel: Int) {
        updateState { it.copy(trainingLevel = trainingLevel) }
    }

    fun updateMotivation(motivation: Int) {
        updateState { it.copy(motivation = motivation) }
    }

    fun updateFanCount(fanCount: Int) {
        updateState { it.copy(fanCount = fanCount) }
    }

    fun updateHp(hp: Int) {
        updateState { it.copy(hp = hp) }
    }

    fun updateMaxHp(maxHp: Int) {
        updateState { it.copy(maxHp = maxHp) }
    }

    fun updateTotalRelation(totalRelation: Int) {
        updateState { it.copy(totalRelation = totalRelation) }
    }

    fun updateSpeedSkillCount(speedSkillCount: Int) {
        updateState { it.copy(speedSkillCount = speedSkillCount) }
    }

    fun updateHealSkillCount(healSkillCount: Int) {
        updateState { it.copy(healSkillCount = healSkillCount) }
    }

    fun updateAccelSkillCount(accelSkillCount: Int) {
        updateState { it.copy(accelSkillCount = accelSkillCount) }
    }

    fun updateTotalTrainingLevel(totalTrainingLevel: Int) {
        updateState { it.copy(totalTrainingLevel = totalTrainingLevel) }
    }

    fun updateShopItemMegaphone(shopItemMegaphone: MegaphoneItem) {
        updateState { it.copy(shopItemMegaphone = shopItemMegaphone) }
    }

    fun updateShopItemWeight(shopItemWeight: WeightItem) {
        updateState { it.copy(shopItemWeight = shopItemWeight) }
    }

    fun updateLiveSpeed(value: String) {
        updateState { it.copy(trainingLiveState = it.trainingLiveState.copy(speed = value)) }
    }

    fun updateLiveStamina(value: String) {
        updateState { it.copy(trainingLiveState = it.trainingLiveState.copy(stamina = value)) }
    }

    fun updateLivePower(value: String) {
        updateState { it.copy(trainingLiveState = it.trainingLiveState.copy(power = value)) }
    }

    fun updateLiveGuts(value: String) {
        updateState { it.copy(trainingLiveState = it.trainingLiveState.copy(guts = value)) }
    }

    fun updateLiveWisdom(value: String) {
        updateState { it.copy(trainingLiveState = it.trainingLiveState.copy(wisdom = value)) }
    }

    fun updateLiveSkillPt(value: String) {
        updateState { it.copy(trainingLiveState = it.trainingLiveState.copy(skillPt = value)) }
    }

    fun updateLiveFriend(value: String) {
        updateState { it.copy(trainingLiveState = it.trainingLiveState.copy(friendTrainingUpInput = value)) }
    }

    fun updateLiveSpecialityRate(value: String) {
        updateState { it.copy(trainingLiveState = it.trainingLiveState.copy(specialityRateUpInput = value)) }
    }

    fun updateGmKnowledgeType(index: Int, value: StatusType?) {
        updateState { it.copy(gmState = it.gmState.updateType(index, value)) }
    }

    fun updateGmKnowledgeBonus(index: Int, value: Int) {
        updateState { it.copy(gmState = it.gmState.updateBonus(index, value)) }
    }

    fun clearGmKnowledge() {
        updateState { it.copy(gmState = it.gmState.clearKnowledge()) }
    }

    fun updateGmWisdom(value: Founder?) {
        updateState { it.copy(gmState = it.gmState.copy(wisdom = value)) }
    }

    fun updateGmWisdomLevel(target: Founder, value: Int) {
        updateState { it.copy(gmState = it.gmState.updateWisdomLevel(target, value)) }
    }

//    var trainingParamTest by mutableStateOf<TrainingParamTestModel?>(null)
//
//    fun updateTrainingParamTest(enabled: Boolean) {
//        trainingParamTest = if (enabled) TrainingParamTestModel() else null
//    }

    private fun calculate(state: State): State {
        val allSupportList = state.supportSelectionList.filter { it.card != null }
            .mapIndexedNotNull { index, support -> support.toMemberState(state.scenario, index) }
        val joinSupportList = state.supportSelectionList.filter { it.join && it.card != null }
            .mapIndexedNotNull { index, support -> support.toMemberState(state.scenario, index) }

        val trainingType = state.selectedTrainingTypeForScenario
        val supportCount =
            state.supportSelectionList.mapNotNull { it.card?.type }.groupBy { it }.mapValues { it.value.size }
        val fanCount = state.fanCount
        val gmStatus = state.gmStatusIfEnabled
        val trainingLevel = if (state.scenario == Scenario.UAF) {
            (state.uafState.trainingGenre.ordinal + 1) * 10 + if (state.isLevelUpTurn) 5 else {
                state.uafState.selectedTrainingLevel
            }
        } else if (gmStatus?.trainingLevelUp == true || (state.scenario == Scenario.LARC && state.lArcState.overseas)) {
            6
        } else if (state.isLevelUpTurn) 5 else state.trainingLevel

        val trainingBase = WebConstants.trainingList[state.scenario]!!.first {
            it.type == trainingType && it.level == trainingLevel
        }
        val scenarioStatus = state.scenarioStatus
        val trainingCalcInfo = Calculator.CalcInfo(
            state.chara,
            trainingBase,
            state.motivation,
            joinSupportList,
            state.scenario,
            supportCount,
            fanCount,
            Status(maxHp = state.maxHp, hp = state.hp),
            state.totalRelation,
            state.speedSkillCount,
            state.healSkillCount,
            state.accelSkillCount,
            state.totalTrainingLevel,
            state.isLevelUpTurn,
            scenarioStatus,
        ).setTeamMember(state.teamJoinCount)
        val scenarioBonus = state.scenario.calculator.getScenarioCalcBonus(trainingCalcInfo)
        val trainingResult = Calculator.calcTrainingSuccessStatusSeparated(trainingCalcInfo, scenarioBonus)
        val trainingPerformanceValue = if (state.scenario == Scenario.GRAND_LIVE) {
            LiveCalculator.calcPerformanceValue(trainingCalcInfo)
        } else 0

        val itemList = listOfNotNull(
            if (state.shopItemMegaphone == WebConstants.dummyMegaphoneItem) null else state.shopItemMegaphone,
            if (state.shopItemWeight == WebConstants.dummyWeightItem) null else state.shopItemWeight,
        )
        val trainingItemBonus = when {
            state.scenario.hasSecondTrainingStatus -> trainingResult.second
            state.scenario == Scenario.CLIMAX -> Calculator.calcItemBonus(
                trainingType,
                trainingResult.first.first,
                itemList
            )

            else -> Status()
        }
        val mechaLearningGain = if (state.scenario != Scenario.MECHA) null else {
            kotlin.runCatching {
                MechaCalculator.calcLearningGain(
                    trainingCalcInfo.mechaStatus!!,
                    trainingType,
                    state.isLevelUpTurn,
                    joinSupportList.any { it.isFriendTraining(trainingType) },
                    state.mechaState.gear,
                    joinSupportList.count() + state.teamJoinCount,
                )
            }.getOrElse { Status() }
        }

        val trainingImpact = joinSupportList.mapIndexed { targetIndex, target ->
            val notJoinResult = Calculator.calcTrainingSuccessStatusSeparated(
                Calculator.CalcInfo(
                    state.chara,
                    trainingBase,
                    state.motivation,
                    joinSupportList.filterIndexed { index, _ -> index != targetIndex },
                    state.scenario,
                    supportCount,
                    fanCount,
                    Status(maxHp = state.maxHp, hp = state.hp),
                    state.totalRelation,
                    state.speedSkillCount,
                    state.healSkillCount,
                    state.accelSkillCount,
                    state.totalTrainingLevel,
                    state.isLevelUpTurn,
                    scenarioStatus,
                ).setTeamMember(state.teamJoinCount)
            )
            target.name to trainingResult.first.first + trainingResult.second - notJoinResult.first.first - notJoinResult.second
        }

        val supportList = state.supportSelectionList.mapIndexedNotNull { index, support ->
            support.toMemberState(state.scenario, index)
        }
        val expectedResult = Calculator.calcExpectedTrainingStatus(
            Calculator.CalcInfo(
                state.chara,
                trainingBase,
                state.motivation,
                supportList,
                state.scenario,
                supportCount,
                fanCount,
                Status(maxHp = state.maxHp, hp = state.hp),
                state.totalRelation,
                state.speedSkillCount,
                state.healSkillCount,
                state.accelSkillCount,
                state.totalTrainingLevel,
                state.isLevelUpTurn,
                scenarioStatus,
            ),
            state.teamJoinCount,
            // TODO ScenarioCalculator使用
            specialityRateUp = { 0 },
            positionRateUp = 0,
        )
        val total = trainingResult.first.first.statusTotal
        val upperRate = expectedResult.second.filter { it.second.statusTotal < total }
            .sumOf { it.first } / expectedResult.second.sumOf { it.first }

        val friendProbability = 1.0 - supportList.fold(1.0) { acc, memberState ->
            val type = memberState.card.type
            acc * if (type.outingType || !memberState.friendTrainingEnabled) 1.0 else {
                1.0 - calcRate(
                    type,
                    *Calculator.calcCardPositionSelection(
                        trainingCalcInfo,
                        memberState,
                        // TODO ScenarioCalculatorで計算
                        state.specialityRateUp,
                        0,
                    )
                )
            }
        }
        val supportSelectionList = state.supportSelectionList.toMutableList()
        supportList.forEach { memberState ->
            val specialtyRate = calcRate(
                memberState.card.type,
                *Calculator.calcCardPositionSelection(
                    trainingCalcInfo.copy(member = allSupportList),
                    memberState,
                    // TODO ScenarioCalculatorで計算
                    state.specialityRateUp,
                    0,
                )
            )
            supportSelectionList[memberState.index] = supportSelectionList[memberState.index].copy(
                specialtyRate = specialtyRate,
            )
        }

        localStorage.setItem(
            KEY_SUPPORT_LIST, SaveDataConverter.supportListToString(state.supportSelectionList.map { it.toSaveInfo() })
        )
        return state.copy(
            supportSelectionList = supportSelectionList,
            trainingResult = trainingResult.first.first,
            trainingItemBonus = trainingItemBonus,
            trainingPerformanceValue = trainingPerformanceValue,
            rawTrainingResult = trainingResult.first.second,
            trainingImpact = trainingImpact,
            expectedResult = expectedResult.first,
            upperRate = upperRate,
//            coinRate = coinRate,
            friendProbability = friendProbability,
            mechaState = state.mechaState.applyIfNotNull(mechaLearningGain) {
                copy(learningLevelGain = it)
            },
            mujintoState = state.mujintoState.applyIf(state.scenario == Scenario.MUJINTO) {
                calcAndUpdateTrainingResult(allSupportList, trainingCalcInfo)
            },
        )
    }

    fun calculateExpected() {
        val state = state
        scope.launch(Dispatchers.Default) {
            val oldStatus = state.expectedState.status
            suspendUpdateState { it.copy(expectedState = it.expectedState.copy(status = null)) }
            val supportList =
                state.supportSelectionList.filter { it.card != null }.mapIndexedNotNull { index, support ->
                    support.toMemberState(state.scenario, index)
                } + createTeamMemberState(state.expectedState.teamJoinCount.toIntOrNull() ?: 0, state.scenario)
            val training = state.expectedState.getTraining(state.scenario)
            val calcInfo = ExpectedCalculator.ExpectedCalcInfo(
                state.chara,
                training,
                state.motivation,
                supportList,
                state.scenario,
                state.fanCount,
                Status(maxHp = state.maxHp, hp = state.hp),
                state.totalRelation,
                state.speedSkillCount,
                state.healSkillCount,
                state.accelSkillCount,
                state.totalTrainingLevel,
                state.isLevelUpTurn,
                state.scenarioStatus,
            )
            val typeRate = state.expectedState.targetTypes.associateWith { 0.0 }.toMutableMap()
            val expected = ExpectedCalculator(
                calcInfo,
                state.expectedState.targetTypes,
                state.expectedState.createEvaluator(),
            ).calc(typeRate)
            val typeRateList = state.expectedState.targetTypes.map { it to typeRate.getOrElse(it) { 0.0 } }
            suspendUpdateState {
                var newHistory = it.expectedState.statusHistory
                oldStatus?.let { status ->
                    newHistory = listOf(status to it.expectedState.typeRateList) + newHistory
                    if (newHistory.size > 5) {
                        newHistory = newHistory.subList(0, 4)
                    }
                }
                it.copy(
                    expectedState = it.expectedState.copy(
                        status = expected,
                        typeRateList = typeRateList,
                        statusHistory = newHistory,
                    )
                )
            }
        }
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
            status += card.initialStatus(state.supportSelectionList.mapNotNull { it.card?.type })
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
        updateState(calculate = false) { state ->
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
        updateState(calculate = false) { state ->
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
        updateState(calculate = false) { it.copy(simulationMode = mode) }
    }

    fun doUraSimulation() {
        scope.launch(Dispatchers.Default) {
            val supportList = state.supportSelectionList.mapNotNull { it.card }
            val selector = WebConstants.simulationModeList[state.scenario]!![state.simulationMode].second()
            val (summary, history) = Simulator(
                state.scenario,
                state.chara,
                supportList,
            ).simulateWithHistory(
                selector,
            ) { ApproximateSimulationEvents() }
            updateState(calculate = false) {
                it.copy(
                    simulationResult = summary.status,
                    simulationHistory = history.map { it.action.name to it.beforeActionState.status },
                )
            }
        }
    }

    fun updateSupportSaveName(name: String) {
        updateState(calculate = false) {
            it.copy(supportSaveName = name)
        }
    }

    fun updateSupportLoadName(name: String) {
        updateState(calculate = false) {
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
            updateState(calculate = false) {
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
        SaveDataConverter.stringToSupportList(support).forEachIndexed { index, supportInfo ->
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
            val pathList = initialPage?.split("/")
            val startPage = if (pathList.isNullOrEmpty()) Page.Top else {
                Page.entries.firstOrNull { it.path == pathList.getOrNull(0) } ?: Page.Top
            }
            val graphTarget = if (startPage == Page.Graph) pathList?.getOrNull(1) else null
            val selectedChara = localStorage.getItem(KEY_CHARA)?.let { id ->
                WebConstants.charaList.firstOrNull { it.id == id.toIntOrNull() }
            } ?: state.chara
            val newList = state.supportSelectionList.toMutableList()
            SaveDataConverter.stringToSupportList(localStorage.getItem(KEY_SUPPORT_LIST))
                .forEachIndexed { index, supportInfo ->
                    if (newList.indices.contains(index)) {
                        newList[index] = SupportSelection.fromSaveInfo(supportInfo)
                    }
                }
            val supportLoadList = loadSupportData().keys.sorted()
            state.copy(
                page = startPage,
                chara = selectedChara,
                supportSelectionList = newList,
                supportLoadList = supportLoadList,
                supportLoadName = supportLoadList.firstOrNull() ?: "",
            ).createRaceSetting().applyIfNotNull(graphTarget) {
                copy(graphState = graphState.copy(targetPath = it))
            }
        }
    }

    fun updateLArc(update: LArcState.() -> LArcState) {
        update { copy(lArcState = lArcState.update()) }
    }

    fun setAllAptitude(value: Int) {
        updateLArc {
            copy(
                overseasTurfAptitude = value,
                longchampAptitude = value,
                lifeRhythm = value,
                nutritionManagement = value,
                frenchSkill = value,
                overseasExpedition = value,
                strongHeart = value,
                mentalStrength = value,
                hopeOfLArc = value,
            )
        }
    }

    fun updateUaf(update: UafState.() -> UafState) {
        update { copy(uafState = uafState.update()) }
    }

    fun updateCook(update: CookState.() -> CookState) {
        update { copy(cookState = cookState.update()) }
    }

    fun calcUafAthleticsLevel() {
        updateUaf { copy(athleticsLevelUpRate = emptyList(), athleticsLevelUpCalculating = true) }
        scope.launch(Dispatchers.Default) {
            val state = state
            val joinSupportList = state.supportSelectionList.filter { it.card != null }
                .mapIndexedNotNull { index, support -> support.toMemberState(state.scenario, index) }

            val trainingType = state.selectedTrainingTypeForScenario
            val supportCount =
                state.supportSelectionList.mapNotNull { it.card?.type }.groupBy { it }.mapValues { it.value.size }
            val fanCount = state.fanCount
            val uafStatus = state.uafStatusIfEnabled
            val trainingLevel = (state.uafState.trainingGenre.ordinal + 1) * 10 + if (state.isLevelUpTurn) 5 else {
                state.uafState.selectedTrainingLevel
            }

            val trainingBase = WebConstants.trainingList[state.scenario]!!.first {
                it.type == trainingType && it.level == trainingLevel
            }
            val trainingCalcInfo = Calculator.CalcInfo(
                state.chara,
                trainingBase,
                state.motivation,
                joinSupportList,
                state.scenario,
                supportCount,
                fanCount,
                Status(maxHp = state.maxHp, hp = state.hp),
                state.totalRelation,
                state.speedSkillCount,
                state.healSkillCount,
                state.accelSkillCount,
                state.totalTrainingLevel,
                state.isLevelUpTurn,
                uafStatus,
            )
            val result = UafAthleticsLevelCalculator.calc(trainingCalcInfo, state.uafState.athleticsLevelUpBonus)
            var expected = 0.0
            val rateList = mutableListOf<Pair<Int, Double>>()
            result.forEachIndexed { index, rate ->
                if (rate > 0.0) {
                    rateList.add(index to rate)
                    expected += index * rate
                }
            }
            withContext(Dispatchers.Main) {
                updateUaf {
                    copy(
                        athleticsLevelUpRate = rateList,
                        expectedAthleticsLevelUp = expected,
                        athleticsLevelUpCalculating = false,
                    )
                }
            }
        }
    }

    fun updateMecha(update: MechaState.() -> MechaState) {
        update { copy(mechaState = mechaState.update()) }
    }

    fun updateMujinto(update: MujintoState.() -> MujintoState) {
        update { copy(mujintoState = mujintoState.update()) }
    }

    fun updateBC(update: BCState.() -> BCState) {
        update { copy(bcState = bcState.update()) }
    }
}

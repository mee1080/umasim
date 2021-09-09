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
import io.github.mee1080.umasim.ai.SimpleActionSelector
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation.*
import io.github.mee1080.umasim.util.SaveDataConverter
import kotlinx.browser.localStorage

class ViewModel(store: Store = Store) {

    companion object {
        private val notSelected = -1 to "未選択"

        private const val KEY_CHARA = "umasim.chara"

        private const val KEY_SUPPORT_LIST = "umasim.support_list"
    }

    val aoharuSimulationViewModel = AoharuSimulationViewModel(this)

    private val charaList =
        listOf(Chara.empty()) + store.charaList.filter { it.rank == 5 && it.rarity == 5 }.sortedBy { it.charaName }

    private val charaMap = charaList.associateBy { it.id }

    val displayCharaList =
        charaList.map { it.id to "${it.name} (${it.speedBonus},${it.staminaBonus},${it.powerBonus},${it.gutsBonus},${it.wisdomBonus})" }

    var selectedChara by mutableStateOf(displayCharaList[0].first)
        private set

    val chara get() = charaMap[selectedChara]!!

    fun updateChara(id: Int) {
        selectedChara = id
        calculate()
        calculateBonus()
        localStorage.setItem(KEY_CHARA, id.toString())
    }

    private val supportMap = store.supportList.groupBy { it.id }

    val displaySupportList =
        listOf(Triple(notSelected.first, notSelected.second, notSelected.second)) + supportMap.entries
            .map { it.key to it.value[0] }
            .sortedBy { it.second.type.ordinal * 10000000 - it.second.rarity * 1000000 + it.first }
            .map { (_, card) -> getDisplayItem(card) }

    private fun getDisplayItem(card: SupportCard) = Triple(
        card.id,
        getRarityText(card) + " " + card.name,
        card.type.displayName
    )

    var supportFilter by mutableStateOf("")
        private set

    var appliedSupportFilter by mutableStateOf("")
        private set

    fun updateSupportFilter(value: String) {
        supportFilter = value
    }

    val supportFilterApplied get() = supportFilter == appliedSupportFilter

    var filteredSupportList by mutableStateOf(displaySupportList)

    fun applyFilter() {
        if (supportFilterApplied) return
        appliedSupportFilter = supportFilter
        val filters = supportFilter.split("[　%s]".toRegex())
        filteredSupportList = if (supportFilter.isEmpty()) displaySupportList else {
            displaySupportList.filter { (id, _, _) ->
                supportMap[id]?.first()?.matches(filters) ?: false
            }
        }
    }

    fun clearFilter() {
        supportFilter = ""
        applyFilter()
    }

    private fun getRarityText(card: SupportCard) = when (card.rarity) {
        1 -> "R"
        2 -> "SR"
        3 -> "SSR"
        else -> "?"
    }

    val supportTalentList = listOf(0, 1, 2, 3, 4).map { it to it.toString() }

    val supportSelectionList = Array(6) { SupportSelection() }

    inner class SupportSelection {

        internal fun toSaveInfo() = SaveDataConverter.SupportInfo(selectedSupport, supportTalent, join, friend)

        internal fun fromSaveInfo(info: SaveDataConverter.SupportInfo) {
            selectedSupport = info.id
            supportTalent = info.talent
            join = info.join
            friend = info.friend
        }

        val supportList: List<Triple<Int, String, String>>
            get() {
                return if (appliedSupportFilter.isEmpty()) filteredSupportList else {
                    val selectedCard = card
                    if (selectedCard != null && filteredSupportList.firstOrNull { it.first == selectedSupport } == null) {
                        listOf(getDisplayItem(selectedCard), *filteredSupportList.toTypedArray())
                    } else {
                        filteredSupportList
                    }
                }
            }

        var selectedSupport by mutableStateOf(notSelected.first)
            private set

        var supportTalent by mutableStateOf(4)
            private set

        var join by mutableStateOf(true)
            private set

        var friend by mutableStateOf(true)
            private set

        val card get() = supportMap[selectedSupport]?.firstOrNull { it.talent == supportTalent }

        val name get() = card?.name ?: "未選択"

        val friendTraining get() = friend && selectedTrainingType == card?.type?.ordinal

        fun updateSupport(id: Int) {
            selectedSupport = id
            calculate()
            calculateBonus()
        }

        fun updateSupportTalent(talent: Int) {
            supportTalent = talent
            calculate()
            calculateBonus()
        }

        fun updateJoin(join: Boolean) {
            this.join = join
            calculate()
        }

        fun updateFriend(friend: Boolean) {
            this.friend = friend
            calculate()
        }

        val isSelected get() = card != null

        val initialRelation get() = card?.initialRelation ?: 0

        val relationUpCount
            get() = if (card?.type == StatusType.FRIEND) {
                (60 - initialRelation - 1) / 4 + 1
            } else {
                (81 - initialRelation - 1) / 7 + 1
            }

        val specialtyRate
            get() = card?.let { card ->
                calcRate(card.type, *Calculator.calcCardPositionSelection(card))
            } ?: 0.0

        val hintRate
            get() = card?.let { card ->
                if (card.type == StatusType.FRIEND) 0.0 else card.hintFrequency
            } ?: 0.0
    }

    val scenarioList = Scenario.values().map { it.ordinal to it.displayName }

    var selectedScenario by mutableStateOf(Scenario.AOHARU.ordinal)
        private set

    val scenario get() = Scenario.values().getOrElse(selectedScenario) { Scenario.AOHARU }

    fun updateScenario(scenarioIndex: Int) {
        selectedScenario = scenarioIndex
        calculate()
    }

    var teamJoinCount by mutableStateOf(0)
        private set

    fun updateTeamJoinCount(delta: Int) {
        if (delta + teamJoinCount in 0..5) {
            teamJoinCount += delta
            calculate()
        }
    }

    val displayTrainingTypeList = trainingType.map { it.ordinal to it.displayName }

    var selectedTrainingType by mutableStateOf(StatusType.SPEED.ordinal)
        private set

    fun updateTrainingType(trainingType: Int) {
        selectedTrainingType = trainingType
        calculate()
    }

    val trainingLevelList = listOf(1, 2, 3, 4, 5).map { it to it.toString() }

    var trainingLevel by mutableStateOf(1)
        private set

    fun updateTrainingLevel(trainingLevel: Int) {
        this.trainingLevel = trainingLevel
        calculate()
    }

    val motivationList = listOf(2 to "絶好調", 1 to "好調", 0 to "普通", -1 to "不調", -2 to "絶不調")

    var motivation by mutableStateOf(2)
        private set

    fun updateMotivation(motivation: Int) {
        this.motivation = motivation
        calculate()
    }

    private val trainingInfo = Scenario.values().associateWith { store.getTrainingInfo(it) }

    var trainingResult by mutableStateOf(Status())
        private set

    var trainingImpact by mutableStateOf(emptyList<Pair<String, Status>>())
        private set

    var expectedResult by mutableStateOf(ExpectedStatus())

    var trainingParamTest by mutableStateOf<TrainingParamTestModel?>(null)

    fun updateTrainingParamTest(enabled: Boolean) {
        trainingParamTest = if (enabled) TrainingParamTestModel() else null
    }

    private fun calculate() {
        val supportList = mutableListOf<Support>()
        supportSelectionList.filter { it.join }.forEachIndexed { index, selection ->
            val card = selection.card
            if (card != null) {
                supportList.add(Support(index, card).apply {
                    checkHintFriend(if (selection.friend) 100 else 0)
                })
            }
        }

        val trainingType = StatusType.values()[selectedTrainingType]
        trainingResult = Calculator.calcTrainingSuccessStatus(
            chara,
            trainingInfo[scenario]!![trainingType]!!,
            trainingLevel,
            motivation,
            supportList,
            scenario,
            teamJoinCount,
        )
        trainingImpact = supportList.map { target ->
            target.name to trainingResult - Calculator.calcTrainingSuccessStatus(
                chara,
                trainingInfo[scenario]!![trainingType]!!,
                trainingLevel,
                motivation,
                supportList.filter { it.index != target.index },
                scenario,
                teamJoinCount,
            )
        }
        expectedResult = Calculator.calcExpectedTrainingStatus(
            chara,
            trainingInfo[scenario]!![trainingType]!!,
            trainingLevel,
            motivation,
            supportSelectionList
                .mapIndexedNotNull { index, selection ->
                    selection.card?.let {
                        Support(index, it).apply { friendTrainingEnabled = selection.friend }
                    }
                },
            scenario,
            teamJoinCount,
        ).first
        trainingParamTest?.calculate(chara, trainingType, motivation, supportList)
        localStorage.setItem(
            KEY_SUPPORT_LIST,
            SaveDataConverter.supportListToString(supportSelectionList.map { it.toSaveInfo() })
        )
    }

    var totalRaceBonus by mutableStateOf(0)
        private set

    var totalFanBonus by mutableStateOf(0)
        private set

    var initialStatus by mutableStateOf(Status())
        private set

    var availableHint by mutableStateOf(mapOf<String, List<String>>())

    private fun calculateBonus() {
        var race = 0
        var fan = 0
        var status = Status()
        val hintMap = mutableMapOf<String, MutableList<String>>()
        chara.initialStatus.skillHint.keys.forEach { skill ->
            hintMap[skill] = mutableListOf("育成キャラ")
        }
        supportSelectionList.mapNotNull { it.card }.forEach { card ->
            race += card.race
            fan += card.fan
            status += card.initialStatus
            val skillCount = card.skills.size
            card.skills.forEach { skill ->
                hintMap.getOrPut(skill) { mutableListOf() }.add("${card.name} 1/$skillCount")
            }
        }
        totalRaceBonus = race
        totalFanBonus = fan
        initialStatus = status
        availableHint = hintMap
    }

    val trainingList = Scenario.values().associateWith { store.getTrainingList(it) }

    private val simulationModeList = listOf(
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
    )

    val displaySimulationModeList = simulationModeList.mapIndexed { index, pair -> index to pair.first }

    var simulationMode by mutableStateOf(0)
        private set

    fun updateSimulationMode(mode: Int) {
        simulationMode = mode
    }

    var simulationTurn by mutableStateOf(55)
        private set

    fun updateSimulationTurn(turn: Int) {
        simulationTurn = turn
    }

    var simulationResult by mutableStateOf(Status())
        private set

    var simulationHistory by mutableStateOf(emptyList<String>())
        private set

    fun doSimulation() {
        val supportList = supportSelectionList.mapNotNull { it.card }
        val simulator = Simulator(chara, supportList, trainingList[scenario]!!)
        Runner.simulate(simulationTurn, simulator, simulationModeList[simulationMode].second())
        simulationResult = simulator.status
        simulationHistory = simulator.history.map { it.name }
    }

    init {
        localStorage.getItem(KEY_CHARA)?.let {
            val id = it.toIntOrNull() ?: return@let
            if (charaMap.containsKey(id)) {
                selectedChara = id
            }
        }
        SaveDataConverter.stringToSupportList(localStorage.getItem(KEY_SUPPORT_LIST))
            .forEachIndexed { index, supportInfo ->
                supportSelectionList.getOrNull(index)?.fromSaveInfo(supportInfo)
            }
        calculate()
        calculateBonus()
    }
}
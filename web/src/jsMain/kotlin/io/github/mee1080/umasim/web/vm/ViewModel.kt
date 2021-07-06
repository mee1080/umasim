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
import io.github.mee1080.umasim.ai.ActionSelectorImpl
import io.github.mee1080.umasim.ai.SimpleActionSelector
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation.*
import kotlinx.browser.localStorage

class ViewModel(store: Store = Store) {

    companion object {
        private val notSelected = -1 to "未選択"

        private const val KEY_SUPPORT_LIST = "umasim.support_list"
    }

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
    }

    private val supportMap = store.supportList.groupBy { it.id }

    val displaySupportList =
        listOf(Triple(notSelected.first, notSelected.second, notSelected.second)) + supportMap.entries
            .map { it.key to it.value[0] }
            .sortedBy { it.second.type.ordinal * 10000000 - it.second.rarity * 1000000 + it.first }
            .map { (id, card) ->
                Triple(
                    id,
                    getRarityText(card) + " " + card.name,
                    card.type.displayName
                )
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

        internal fun toSaveString() = "1:$selectedSupport:$supportTalent:${if (join) 1 else 0}:${if (friend) 1 else 0}"

        internal fun loadFromString(saved: String) {
            val split = saved.split(":")
            if (split[0] == "1") {
                try {
                    selectedSupport = split[1].toInt()
                    supportTalent = split[2].toInt()
                    join = split[3] == "1"
                    friend = split[4] == "1"
                } catch (_: Exception) {
                    // ignore
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

    private val trainingInfo = store.trainingList
        .groupBy { it.type }
        .mapValues { entry -> TrainingInfo(entry.key, entry.value.sortedBy { it.level }) }

    var trainingResult by mutableStateOf(Status())
        private set

    private fun calculate() {
        val supportList = mutableListOf<Support>()
        supportSelectionList.filter { it.join }.forEachIndexed { index, selection ->
            val card = supportMap[selection.selectedSupport]
                ?.firstOrNull { it.talent == selection.supportTalent }
            if (card != null) {
                supportList.add(Support(index, card).apply {
                    checkHintFriend(if (selection.friend) 100 else 0)
                })
            }
        }
        val trainingType = StatusType.values()[selectedTrainingType]
        trainingInfo[trainingType]
        trainingResult = Calculator.calcTrainingSuccessStatus(
            chara,
            trainingInfo[trainingType]!!,
            trainingLevel,
            motivation,
            supportList
        )
        localStorage.setItem(KEY_SUPPORT_LIST, "1," + supportSelectionList.joinToString(",") { it.toSaveString() })
    }

    var totalRaceBonus by mutableStateOf(0)
        private set

    var totalFanBonus by mutableStateOf(0)
        private set

    private fun calculateBonus() {
        var race = 0
        var fan = 0
        supportSelectionList.forEach { selection ->
            val card = supportMap[selection.selectedSupport]
                ?.firstOrNull { it.talent == selection.supportTalent }
            if (card != null) {
                race += card.race
                fan += card.fan
            }
        }
        totalRaceBonus = race
        totalFanBonus = fan
    }

    private val trainingList = store.trainingList

    private val simulationModeList = listOf(
        "スピパワ" to { ActionSelectorImpl.speedPower() },
        "スピスタ" to { ActionSelectorImpl.speedStamina() },
        "パワ賢" to { ActionSelectorImpl.powerWisdom() },
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
        val supportList = supportSelectionList.mapNotNull { selection ->
            supportMap[selection.selectedSupport]?.firstOrNull { it.talent == selection.supportTalent }
        }
        val simulator = Simulator(chara, supportList, trainingList)
        Runner.simulate(simulationTurn, simulator, simulationModeList[simulationMode].second())
        simulationResult = simulator.status
        simulationHistory = simulator.history.map { it.name }
    }

    init {
        val savedSupport = localStorage.getItem(KEY_SUPPORT_LIST)
        if (savedSupport != null) {
            val split = savedSupport.split(",")
            if (split[0] == "1") {
                (1 until split.size).forEach {
                    supportSelectionList.getOrNull(it - 1)?.loadFromString(split[it])
                }
            }
        }
        calculate()
        calculateBonus()
    }
}
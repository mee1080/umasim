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
package io.github.mee1080.umasim.gui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.mee1080.umasim.ai.FactorBasedActionSelector
import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.gui.vm.FactorBasedActionSelectorSettingViewModel
import io.github.mee1080.umasim.simulation.Evaluator
import io.github.mee1080.umasim.simulation.Runner
import io.github.mee1080.umasim.simulation.Simulator
import io.github.mee1080.umasim.simulation.Summary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(private val scope: CoroutineScope) {

    enum class ViewState {
        DEFAULT, SIMULATION_SETTING,
    }

    var viewState by mutableStateOf(ViewState.DEFAULT)

    val charaList = Store.charaList.sortedWith { o1, o2 ->
        when {
            o1.charaName != o2.charaName -> o1.charaName.compareTo(o2.charaName)
            else -> o1.name.compareTo(o2.name)
        }
    }

    var selectedChara by mutableStateOf<Chara?>(null)

    var charaSelecting by mutableStateOf(false)
        private set

    fun toggleCharaSelect() {
        cancelSupportSelect()
        charaSelecting = !charaSelecting
    }

    fun cancelCharaSelect() {
        charaSelecting = false
    }

    fun selectChara(chara: Chara?) {
        selectedChara = chara
        charaSelecting = false
    }

    val supportList = Store.supportList.sortedWith { o1, o2 ->
        when {
            o1.rarity != o2.rarity -> o2.rarity - o1.rarity
            o1.chara != o2.chara -> o1.chara.compareTo(o2.chara)
            else -> o1.name.compareTo(o2.name)
        }
    }

    val selectedSupportList = mutableStateListOf(
        Store.getSupportByName("[迫る熱に押されて]キタサンブラック", 4),
        Store.getSupportByName("[必殺！Wキャロットパンチ！]ビコーペガサス", 4),
        Store.getSupportByName("[『愛してもらうんだぞ』]オグリキャップ", 4),
        Store.getSupportByName("[感謝は指先まで込めて]ファインモーション", 4),
        Store.getSupportByName("[ようこそ、トレセン学園へ！]駿川たづな", 4),
        null,
    )

    var selectingSupportIndex by mutableStateOf(-1)
        private set

    val supportSelecting get() = selectingSupportIndex >= 0

    fun toggleSupportSelect(index: Int) {
        cancelCharaSelect()
        selectingSupportIndex = if (selectingSupportIndex == index) -1 else index
    }

    fun cancelSupportSelect() {
        selectingSupportIndex = -1
    }

    val selectingSupport get() = selectedSupportList.getOrNull(selectingSupportIndex)

    fun selectSupport(card: SupportCard?) {
        selectedSupportList[selectingSupportIndex] = card
        selectingSupportIndex = -1
    }

    var simulationRunning by mutableStateOf(false)
        private set

    val canSimulate
        get() = !charaSelecting && selectedChara != null
                && !supportSelecting && !selectedSupportList.contains(null)

    val simulationSetting by mutableStateOf(FactorBasedActionSelectorSettingViewModel())

    fun startSimulate() {
        if (!canSimulate || simulationRunning) return
        val chara = selectedChara!!
        val support = selectedSupportList.filterNotNull()
        val option = simulationSetting.option
        simulationRunning = true
        scope.launch(Dispatchers.Default) {
            val selector = FactorBasedActionSelector(option)
            val summary = mutableListOf<Summary>()
            repeat(1000) {
                val simulator = Simulator(chara, support, Store.trainingList)
                summary.add(Runner.simulate(60, simulator, selector))
            }
            println(Evaluator(summary).toSummaryString())
            simulationRunning = false
        }
    }
}
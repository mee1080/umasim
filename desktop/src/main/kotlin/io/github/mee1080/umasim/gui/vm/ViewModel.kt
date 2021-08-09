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
package io.github.mee1080.umasim.gui.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.mee1080.umasim.ai.FactorBasedActionSelector
import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.simulation.Runner
import io.github.mee1080.umasim.simulation.Simulator
import io.github.mee1080.umasim.simulation.Summary
import kotlinx.coroutines.*
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

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

    var selectedChara by mutableStateOf(charaList.getOrNull(0))
        private set

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
        save()
    }

    val supportList = Store.supportList.sortedWith { o1, o2 ->
        when {
            o1.rarity != o2.rarity -> o2.rarity - o1.rarity
            o1.chara != o2.chara -> o1.chara.compareTo(o2.chara)
            else -> o1.name.compareTo(o2.name)
        }
    }

    val selectedSupportList = mutableStateListOf<SupportCard?>(
        Store.getSupportByName("[迫る熱に押されて]キタサンブラック", 4),
        Store.getSupportByName("[必殺！Wキャロットパンチ！]ビコーペガサス", 4),
        Store.getSupportByName("[『愛してもらうんだぞ』]オグリキャップ", 4),
        Store.getSupportByName("[感謝は指先まで込めて]ファインモーション", 4),
        Store.getSupportByName("[ようこそ、トレセン学園へ！]駿川たづな", 4),
        Store.getSupportByName("[一粒の安らぎ]スーパークリーク", 4),
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
        save()
    }

    val canSimulate
        get() = !charaSelecting && selectedChara != null
                && !supportSelecting && !selectedSupportList.contains(null)

    var simulationTurn by mutableStateOf(60)
        private set

    fun updateSimulationTurn(value: Int) {
        if (value != simulationTurn) simulationTurn = value
    }

    var simulationCount by mutableStateOf(1000)
        private set

    fun updateSimulationCount(value: Int) {
        if (value != simulationCount) simulationCount = value
    }

    var simulationThread by mutableStateOf(4)
        private set

    fun updateSimulationThread(value: Int) {
        if (value != simulationThread) simulationThread = value
    }

    val simulationSetting by mutableStateOf(FactorBasedActionSelectorSettingViewModel())

    private var simulationJob by mutableStateOf<Job?>(null)

    val simulationRunning get() = simulationJob?.isActive == true

    var simulationRunningCount by mutableStateOf(0)

    var simulationFinishedCount by mutableStateOf(0)

    var simulationResultFile by mutableStateOf<String?>(null)

    val simulationResult get() = simulationResultFile?.let { File(it).readText(Charsets.UTF_8) } ?: ""

    var simulationResultVisible by mutableStateOf(true)

    fun startSimulation() {
        if (!canSimulate || simulationRunning) return
        save()
        val chara = selectedChara!!
        val support = selectedSupportList.filterNotNull()
        val option = simulationSetting.option
        val simulationCount = simulationCount
        val simulationTurn = simulationTurn
        val simulationThread = simulationThread
        simulationRunningCount = simulationCount
        simulationFinishedCount = 0
        simulationJob = scope.launch(Dispatchers.Default) {
            val simulationContext = Executors.newFixedThreadPool(simulationThread).asCoroutineDispatcher()
            simulationContext.use { context ->
                try {
                    val finishedCount = AtomicInteger(0)
                    val totalSummary = (0 until simulationThread).map { thread ->
                        async(context) {
                            val summary = mutableListOf<Summary>()
                            val count =
                                simulationCount / simulationThread + (if (simulationCount % simulationThread > thread) 1 else 0)
                            repeat(count) {
                                if (!isActive) throw CancellationException()
                                val simulator = Simulator(chara, support, Store.trainingList)
                                val selector = FactorBasedActionSelector(option)
                                summary.add(Runner.simulate(simulationTurn, simulator, selector))
                                if (it % 100 == 99) {
                                    simulationFinishedCount = finishedCount.addAndGet(100)
                                }
                            }
                            simulationFinishedCount = finishedCount.addAndGet(count % 100)
                            summary
                        }
                    }.fold(mutableListOf<Summary>()) { acc, result ->
                        acc.addAll(result.await())
                        acc
                    }
                    if (!isActive) throw CancellationException()
                    val file =
                        ResultWriter().output(chara, support, option, simulationCount, simulationTurn, totalSummary)
                    simulationResultFile = file.absolutePath
                    simulationResultVisible = true
                } catch (e: CancellationException) {
                    // nothing to do
                } finally {
                    simulationJob = null
                }
            }
        }
    }

    fun cancelSimulation() {
        simulationJob?.cancel()
    }

    private fun save() {
        SaveData.save(this)
    }

    init {
        SaveData.load(this)
    }
}
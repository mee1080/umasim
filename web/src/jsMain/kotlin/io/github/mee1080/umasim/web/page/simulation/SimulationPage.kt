/*
 * Copyright 2022 mee1080
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
package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.*
import io.github.mee1080.umasim.ai.CookActionSelector
import io.github.mee1080.umasim.ai.LegendActionSelector
import io.github.mee1080.umasim.ai.MechaActionSelector
import io.github.mee1080.umasim.ai.UafActionSelector
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.ScenarioEvents
import io.github.mee1080.umasim.scenario.legend.LegendScenarioEvents
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.web.components.atoms.*
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.page.share.StatusTable
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.displayName
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun SimulationPage(state: State) {
    var setting by remember { mutableStateOf(SimulationSetting()) }
    var running by remember { mutableStateOf(false) }
    var aiSelectorOption by remember {
        mutableStateOf(
            when (state.scenario) {
                Scenario.UAF -> UafActionSelector.Option()
                Scenario.COOK -> CookActionSelector.Option()
                Scenario.MECHA -> MechaActionSelector.s2h2p1w1Generator
                Scenario.LEGEND -> LegendActionSelector.Option()
                else -> null
            }
        )
    }
    val aiSelector by derivedStateOf { aiSelectorOption?.generateSelector() }
    var optionEdit by remember { mutableStateOf(false) }
    (aiSelectorOption as? SerializableActionSelectorGenerator)?.let { option ->
        OptionEditDialog(optionEdit, option) {
            optionEdit = false
            if (it != null) aiSelectorOption = it
        }
    }
    Div({
        style {
            height(100.percent)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
        }
    }) {
        Div({
            style {
                display(DisplayStyle.Flex)
                justifyContent(JustifyContent.SpaceBetween)
                alignItems(AlignItems.Center)
                padding(4.px)
            }
        }) {
            Div({
                style { typeScale(MdSysTypeScale.headlineMedium) }
            }) {
                Text("手動シミュレーション")
            }
            Div {
                if (aiSelectorOption != null) {
                    MdFilledButton("AI設定") {
                        onClick { optionEdit = true }
                        style { marginRight(8.px) }
                    }
                }
                MdFilledButton("リセット") {
                    onClick { running = false }
                }
            }
        }
        if (running) {
            RunningSimulation(state, setting, aiSelector)
        } else {
            Card({
                style { margin(16.px) }
            }) {
                Div { Text("シナリオ：") }
                Div({ style { marginLeft(32.px) } }) {
                    Div { Text(state.scenario.displayName) }
                }
                Div { Text("育成ウマ娘：") }
                Div({ style { marginLeft(32.px) } }) {
                    Div { Text(state.chara.name) }
                }
                Div({ style { marginTop(32.px) } }) { Text("サポートカード：") }
                Div({ style { marginLeft(32.px) } }) {
                    state.supportSelectionList.map { it.card }.forEach {
                        Div { Text(it.displayName()) }
                    }
                }
                Div({ style { marginTop(32.px) } }) { Text("青因子選択（全て☆３）：") }
                val statusSelection = trainingType.toList()
                repeat(6) { index ->
                    Div {
                        MdRadioGroup(
                            statusSelection,
                            setting.factorList[index].first,
                            itemToLabel = { it.displayName },
                            onSelect = { setting = setting.setFactor(index, it, 3) },
                        )
                    }
                }
                if (state.scenario == Scenario.LEGEND) {
                    Div({ style { marginTop(32.px) } }) { Text("心得指定：") }
                    Div({
                        style {
                            display(DisplayStyle.Flex)
                            columnGap(8.px)
                        }
                    }) {
                        legendBuffTemplates.forEach {(name,list) ->
                            MdFilledButton(name) {
                                onClick { setting = setting.copy(legendBuffList = list) }
                            }
                        }
                    }
                    repeat(11) { row ->
                        LegendBuffSelect(setting, row) { setting = it }
                    }
                }
            }
            DivFlexCenter({ style { justifyContent(JustifyContent.Center) } }) {
                MdFilledButton("シミュレーション開始") {
                    onClick { running = true }
                }
            }
        }
    }
}

@Composable
private fun LegendBuffSelect(setting: SimulationSetting, index: Int, onUpdate: (SimulationSetting) -> Unit) {
    val selection = when {
        index >= 4 -> legendBuffList3
        index >= 2 -> legendBuffList2
        else -> legendBuffList1
    }
    Div {
        MdOutlinedSelect(
            selection = selection,
            selectedItem = setting.legendBuffList[index],
            onSelect = { onUpdate(setting.setLegendBuff(index, it)) },
            itemToValue = { it.second },
        )
    }
}

private val emptyAiResult: Triple<Action?, List<Double>, Double> = Triple(null, emptyList(), 0.0)

@Composable
fun RunningSimulation(state: State, setting: SimulationSetting, aiSelector: ActionSelector?) {
    val scope = rememberCoroutineScope()
    val selector = remember { ManualActionSelector() }
    var aiResult by remember { mutableStateOf(emptyAiResult) }
    var simulationState by remember { mutableStateOf<SimulationState?>(null) }
    var selection by remember { mutableStateOf<List<Action>>(emptyList()) }
    var result by remember { mutableStateOf<Summary?>(null) }
    DisposableEffect(Unit) {
        val job = scope.launch {
            launch {
                selector.selectionChannel.receiveAsFlow().collect { entry ->
                    simulationState = entry.first
                    selection = entry.second
                    result = null
                    aiSelector?.let { aiSelectorNonNull ->
                        aiResult = emptyAiResult
                        aiResult = aiSelectorNonNull.selectWithScore(entry.first, entry.second)
                    }
                }
            }
            launch {
                val scenarioEventProducer: ((SimulationState) -> ScenarioEvents)? = when (state.scenario) {
                    Scenario.LEGEND -> {
                        { LegendScenarioEvents(setting.legendBuffListOutput) }
                    }

                    else -> null
                }
                result = Simulator(
                    scenario = state.scenario,
                    chara = state.chara,
                    supportCardList = state.supportSelectionList.mapNotNull { it.card },
                    factorList = setting.factorList,
                ).simulate(selector, scenarioEventProducer)
                selection = emptyList()
            }
        }
        onDispose {
            job.cancel()
        }
    }
    LaunchedEffect(aiSelector) {
        val simulationStateNonNull = simulationState ?: return@LaunchedEffect
        aiSelector?.let { aiSelectorNonNull ->
            aiResult = emptyAiResult
            aiResult = aiSelectorNonNull.selectWithScore(simulationStateNonNull, selection)
        }
    }
    simulationState?.let { simulationStateNonNull ->
        SimulationStateBlock(simulationStateNonNull)
        MdDivider(1.px)
        if (selection.isNotEmpty()) {
            SelectionBlock(simulationStateNonNull, selection, aiResult.first, aiResult.second) {
                scope.launch {
                    selector.resultChannel.send(it)
                }
            }
        }
    }
    result?.let {
        Div { Text("育成終了") }
        StatusTable(it.status, summary = true)
    }
}

class ManualActionSelector : ActionSelector {

    val selectionChannel = Channel<Pair<SimulationState, List<Action>>>()

    val resultChannel = Channel<Action>()

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        selectionChannel.send(state to selection)
        return resultChannel.receive()
    }
}
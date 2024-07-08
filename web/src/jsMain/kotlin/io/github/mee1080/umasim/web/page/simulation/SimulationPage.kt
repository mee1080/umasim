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
import io.github.mee1080.umasim.ai.UafActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
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
    val factorList = remember {
        mutableStateListOf(
            StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
            StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
        )
    }
    var running by remember { mutableStateOf(false) }
    var aiSelectorOption by remember {
        mutableStateOf(
            when (state.scenario) {
                Scenario.UAF -> UafActionSelector.Option()
                Scenario.COOK -> CookActionSelector.Option()
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
            RunningSimulation(state, factorList, aiSelector)
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
                            factorList[index].first,
                            itemToLabel = { it.displayName },
                            onSelect = { factorList[index] = it to factorList[index].second },
                        )
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

private val emptyAiResult: Pair<Action?, List<Double>> = null to emptyList()

@Composable
fun RunningSimulation(state: State, factorList: List<Pair<StatusType, Int>>, aiSelector: ActionSelector?) {
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
                result = Simulator(
                    scenario = state.scenario,
                    chara = state.chara,
                    supportCardList = state.supportSelectionList.mapNotNull { it.card },
                    factorList = factorList,
                ).simulate(selector)
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
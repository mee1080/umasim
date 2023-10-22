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
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.web.components.atoms.MdDivider
import io.github.mee1080.umasim.web.components.atoms.MdFilledButton
import io.github.mee1080.umasim.web.components.atoms.MdSysTypeScale
import io.github.mee1080.umasim.web.components.atoms.typeScale
import io.github.mee1080.umasim.web.page.share.StatusTable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun SimulationPage() {
    val selector = remember { ManualActionSelector() }
    var simulationState by remember { mutableStateOf<SimulationState?>(null) }
    var selection by remember { mutableStateOf<List<Action>>(emptyList()) }
    var result by remember { mutableStateOf<Summary?>(null) }
    val scope = rememberCoroutineScope()
    var reset by remember { mutableStateOf(0) }
    DisposableEffect(reset) {
        val job = scope.launch {
            launch {
                selector.selectionChannel.receiveAsFlow().collect {
                    simulationState = it.first
                    selection = it.second
                    result = null
                }
            }
            launch {
                result = Simulator(
                    scenario = Scenario.LARC,
                    chara = Store.getChara("[うららん一等賞♪]ハルウララ", 5, 5),
                    supportCardList = Store.getSupportByName(
                        "[大望は飛んでいく]エルコンドルパサー",
                        "[The frontier]ジャングルポケット",
                        "[迫る熱に押されて]キタサンブラック",
                        "[一粒の安らぎ]スーパークリーク",
                        "[君と見る泡沫]マンハッタンカフェ",
                        "[L'aubeは迫りて]佐岳メイ",
                    ),
                    factorList = listOf(
                        StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.STAMINA to 3,
                        StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.GUTS to 3,
                    ),
                ).simulate(selector)
                selection = emptyList()
            }
        }
        onDispose {
            job.cancel()
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
            MdFilledButton("リセット") {
                onClick { reset++ }
            }
        }
        simulationState?.let { SimulationStateBlock(it) }
        MdDivider(1.px)
        if (selection.isNotEmpty()) {
            SelectionBlock(selection) {
                scope.launch {
                    selector.resultChannel.send(it)
                }
            }
        }
        result?.let {
            Div { Text("育成終了") }
            StatusTable(it.status, summary = true)
        }
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
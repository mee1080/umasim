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
package io.github.mee1080.umasim.gui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.rememberDialogState
import io.github.mee1080.umasim.gui.vm.ViewModel
import java.lang.Integer.min
import kotlin.math.max

@Composable
fun DefaultPanel(model: ViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text("育成ウマ娘", Modifier.padding(8.dp, 8.dp))
                CharaView(
                    model.selectedChara,
                    Modifier.background(Color(224, 224, 224)),
                    selected = model.charaSelecting,
                ) {
                    model.toggleCharaSelect()
                }
                Text("サポートカード", Modifier.padding(8.dp, 8.dp))
                model.selectedSupportList.forEachIndexed { index, card ->
                    SupportCardView(
                        card,
                        Modifier.background(Color(224, 224, 224)),
                        selected = model.selectingSupportIndex == index,
                        tooltip = true,
                    ) {
                        model.toggleSupportSelect(index)
                    }
                }
            }
            if (model.charaSelecting) {
                CharaSelector(
                    model.charaList,
                    modifier = Modifier.weight(1f),
                    initialChara = model.selectedChara,
                    onCanceled = { model.cancelCharaSelect() },
                ) {
                    model.selectChara(it)
                }
            }
            if (model.supportSelecting) {
                SupportCardSelector(
                    model.supportList,
                    modifier = Modifier.weight(1f),
                    initialCard = model.selectingSupport,
                    onCanceled = { model.cancelSupportSelect() }
                ) {
                    model.selectSupport(it)
                }
            }
        }
        Text("シミュレーション設定", Modifier.padding(8.dp, 8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(224, 224, 224))
                .padding(16.dp)
        ) {
            TextField(
                model.simulationCount.toString(),
                onValueChange = {
                    model.updateSimulationCount(max(0, it.toIntOrNull() ?: 0))
                },
                modifier = Modifier.padding(8.dp, 0.dp).width(160.dp),
                singleLine = true,
                label = { Text("シミュレーション回数") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            )
            TextField(
                model.simulationTurn.toString(),
                onValueChange = {
                    model.updateSimulationTurn(max(0, min(78, it.toIntOrNull() ?: 0)))
                },
                modifier = Modifier.padding(8.dp, 0.dp).width(120.dp),
                singleLine = true,
                label = { Text("ターン数") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            )
            TextField(
                model.simulationThread.toString(),
                onValueChange = {
                    model.updateSimulationThread(max(1, min(32, it.toIntOrNull() ?: 0)))
                },
                modifier = Modifier.padding(8.dp, 0.dp).width(120.dp),
                singleLine = true,
                label = { Text("スレッド数") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            )
            Button(
                { model.viewState = ViewModel.ViewState.SIMULATION_SETTING },
                modifier = Modifier.align(Alignment.CenterVertically).padding(8.dp)
            ) {
                Text("AI設定")
            }
        }
        if (model.canSimulate) {
            if (model.simulationRunning) {
                Row {
                    Text(
                        "シミュレーション実行中... (${model.simulationFinishedCount}/${model.simulationRunningCount})",
                        modifier = Modifier.weight(1f).padding(8.dp),
                    )
                    Button(
                        { model.cancelSimulation() },
                        modifier = Modifier.weight(1f).padding(8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                    ) {
                        Text("キャンセル")
                    }
                }
            } else {
                Button(
                    { model.startSimulation() },
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp).fillMaxWidth(),
                ) {
                    Text("シミュレーション実行")
                }
            }
        }
        if (model.simulationResultFile != null) {
            Dialog(
                onCloseRequest = { model.simulationResultVisible = false },
                state = rememberDialogState(size = WindowSize(1024.dp, 768.dp)),
                visible = model.simulationResultVisible,
                title = "シミュレーション結果",
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text("シミュレーション結果", modifier = Modifier.padding(8.dp))
                    Text("${model.simulationResultFile} に保存済み")
                    OutlinedTextField(
                        model.simulationResult,
                        {},
                        readOnly = true,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    )
                    Button(
                        { model.simulationResultVisible = false },
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp).fillMaxWidth(),
                    ) { Text("閉じる") }
                }
            }
        }
    }
}
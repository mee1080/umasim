package io.github.mee1080.umasim.gui.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.gui.ViewModel
import io.github.mee1080.umasim.gui.component.CharaSelector
import io.github.mee1080.umasim.gui.component.CharaView
import io.github.mee1080.umasim.gui.component.SupportCardSelector
import io.github.mee1080.umasim.gui.component.SupportCardView
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
            Button(
                { model.startSimulate() },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp).fillMaxWidth(),
                enabled = !model.simulationRunning,
            ) {
                Text("シミュレーション実行")
            }
        }
    }
}
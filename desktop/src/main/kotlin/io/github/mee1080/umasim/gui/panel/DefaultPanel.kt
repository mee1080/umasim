package io.github.mee1080.umasim.gui.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.gui.ViewModel
import io.github.mee1080.umasim.gui.component.CharaSelector
import io.github.mee1080.umasim.gui.component.CharaView
import io.github.mee1080.umasim.gui.component.SupportCardSelector
import io.github.mee1080.umasim.gui.component.SupportCardView

@Composable
fun DefaultPanel(model: ViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
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
        Button(
            { model.viewState = ViewModel.ViewState.SIMULATION_SETTING },
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).fillMaxWidth(),
        ) {
            Text("シミュレーション設定")
        }
        if (model.canSimulate) {
            Button(
                { model.startSimulate() },
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).fillMaxWidth(),
                enabled = !model.simulationRunning,
            ) {
                Text("シミュレーション実行")
            }
        }
    }
}
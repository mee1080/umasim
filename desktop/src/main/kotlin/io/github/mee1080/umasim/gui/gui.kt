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

import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.gui.component.CharaView
import io.github.mee1080.umasim.gui.component.SupportCardSelector
import io.github.mee1080.umasim.gui.component.SupportCardView

fun openGui(args: Array<String>) = Window(
    title = "ウマ娘シミュレータ",
    size = IntSize(1024, 768),
) {
    val scope = rememberCoroutineScope()
    val model by remember { mutableStateOf(ViewModel(scope)) }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text("育成ウマ娘", Modifier.padding(8.dp, 8.dp))
                    CharaView(
                        model.selectedChara,
                        Modifier.background(Color(224, 224, 224))
                    ) {
                        model.toggleCharaSelect()
                    }
                    Text("サポートカード", Modifier.padding(8.dp, 8.dp))
                    model.selectedSupportList.forEachIndexed { index, card ->
                        SupportCardView(
                            card,
                            Modifier.background(Color(224, 224, 224))
                        ) {
                            model.toggleSupportSelect(index)
                        }
                    }
                }
                if (model.supportSelecting) {
                    SupportCardSelector(
                        model.supportList,
                        modifier = Modifier.weight(1f),
                        initialCard = model.selectingSupport,
                    ) {
                        model.selectSupport(it)
                    }
                }
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
}
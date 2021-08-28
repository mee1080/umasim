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

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.gui.vm.ViewModel

@Composable
fun PresetSelector(
    model: ViewModel,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .border(2.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                .padding(4.dp)
        ) {
            Text("プリセット登録")
            Row {
                TextField(
                    name,
                    { name = it },
                    label = { Text("プリセット名") },
                    modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                )
                Button({ model.addPreset(name) }, Modifier.align(Alignment.CenterVertically)) {
                    Text("登録")
                }
            }
            if (model.addPresetFailed) {
                Text("登録失敗しました", color = Color.Red)
            }
        }
        BaseCard(onClick = { model.resetPreset() }, selected = true) {
            Text("（現在の設定）")
        }
        Box(modifier = Modifier.weight(1f)) {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                model.supportPresets.sortedBy { it.first }.forEach {
                    Row {
                        BaseCard(
                            Modifier.weight(1f).align(Alignment.CenterVertically),
                            onClick = { model.applyPreset(it.second) }
                        ) {
                            Text(it.first)
                        }
                        Button(
                            { model.updatePreset(it.first) },
                            Modifier.padding(4.dp).align(Alignment.CenterVertically)
                        ) {
                            Text("更新")
                        }
                        Button(
                            { model.deletePreset(it.first) },
                            Modifier.padding(4.dp).align(Alignment.CenterVertically)
                        ) {
                            Text("削除")
                        }
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(scrollState)
            )
        }
        Button({ model.finishSelectPreset() }, modifier = Modifier.fillMaxWidth()) { Text("閉じる") }
    }
}
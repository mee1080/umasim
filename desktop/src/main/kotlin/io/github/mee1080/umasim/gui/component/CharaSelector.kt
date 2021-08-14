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
import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Store

@Composable
fun CharaSelector(
    charaList: List<Chara>,
    modifier: Modifier = Modifier,
    initialChara: Chara? = null,
    onCanceled: () -> Unit = {},
    onSelect: (Chara?) -> Unit = { },
) {
    var text by remember { mutableStateOf("") }
    var rarity by remember { mutableStateOf(initialChara?.rarity ?: 5) }
    var rank by remember { mutableStateOf(initialChara?.rank ?: 5) }
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
            TextField(
                text,
                { text = it },
                label = { Text("検索") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            )
        }
        Row(
            modifier = Modifier.padding(8.dp, 0.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (1..5).forEach {
                LabeledRadioButton(it == rarity, { rarity = it }) { Text("☆${it}") }
            }
        }
        Row(
            modifier = Modifier.padding(8.dp, 0.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (1..5).forEach {
                LabeledRadioButton(it == rank, { rank = it }) { Text("覚醒${it}") }
            }
        }
        if (initialChara != null) {
            val current = Store.getCharaOrNull(initialChara.id, rarity, rank)
            if (current != null) {
                CharaView(current, selected = true) { onSelect(current) }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                charaList
                    .filter { it.rarity == rarity && it.rank == rank }
                    .filter { text.isEmpty() || it.name.contains(text) }
                    .forEach {
                        CharaView(it, selected = it.id == initialChara?.id) { onSelect(it) }
                    }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(scrollState)
            )
        }
        Button({ onSelect(null) }, modifier = Modifier.fillMaxWidth()) { Text("選択解除") }
        Button({ onCanceled() }, modifier = Modifier.fillMaxWidth()) { Text("キャンセル") }
    }
}
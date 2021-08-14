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
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.SupportCard

@Composable
fun SupportCardSelector(
    supportList: List<SupportCard>,
    modifier: Modifier = Modifier,
    initialCard: SupportCard? = null,
    onCanceled: () -> Unit = {},
    onSelect: (SupportCard?) -> Unit = { },
) {
    var rememberedInitialCard by remember { mutableStateOf(initialCard?.id) }
    var type by remember { mutableStateOf(initialCard?.type ?: StatusType.NONE) }
    var text by remember { mutableStateOf("") }
    var talent by remember { mutableStateOf(initialCard?.talent ?: 0) }
    if (initialCard != null && rememberedInitialCard != initialCard.id) {
        @Suppress("UNUSED_VALUE")
        rememberedInitialCard = initialCard.id
        type = initialCard.type
        talent = initialCard.talent
    }
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
            val radioModifier = Modifier.weight(1f)
            LabeledRadioButton(type == StatusType.NONE, { type = StatusType.NONE }) { Text("全て") }
            Row {
                LabeledRadioButton(type == StatusType.SPEED, { type = StatusType.SPEED }, radioModifier) {
                    Text("スピ")
                }
                LabeledRadioButton(type == StatusType.STAMINA, { type = StatusType.STAMINA }, radioModifier) {
                    Text("スタ")
                }
                LabeledRadioButton(type == StatusType.POWER, { type = StatusType.POWER }, radioModifier) {
                    Text("パワ")
                }
            }
            Row {
                LabeledRadioButton(type == StatusType.GUTS, { type = StatusType.GUTS }, radioModifier) {
                    Text("根性")
                }
                LabeledRadioButton(type == StatusType.WISDOM, { type = StatusType.WISDOM }, radioModifier) {
                    Text("賢さ")
                }
                LabeledRadioButton(type == StatusType.FRIEND, { type = StatusType.FRIEND }, radioModifier) {
                    Text("友人")
                }
            }
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
            (0..4).forEach {
                LabeledRadioButton(it == talent, { talent = it }) { Text("${it}凸") }
            }
        }
        rememberedInitialCard?.let {
            val current = Store.getSupportOrNull(it, talent = talent)
            if (current != null) {
                SupportCardView(current, selected = true) { onSelect(current) }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                supportList
                    .filter { it.talent == talent }
                    .filter { type == StatusType.NONE || type == it.type }
                    .filter { text.isEmpty() || it.name.contains(text) }
                    .forEach {
                        SupportCardView(it, selected = it.id == initialCard?.id) { onSelect(it) }
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
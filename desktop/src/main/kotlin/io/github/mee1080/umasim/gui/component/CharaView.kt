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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.data.Chara

@Composable
fun CharaView(
    chara: Chara?,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val color = remember {
        chara?.let {
            Color(it.imageColor.toInt(16)).copy(alpha = 0.5f)
        } ?: Color(128, 128, 128, 128)
    }
    BaseCard(modifier, onClick, color, selected) {
        if (chara == null) {
            Text("未選択")
        } else {
            Row {
                Text(
                    "☆${chara.rarity}",
                    modifier = Modifier
                        .padding(0.dp, 4.dp, 4.dp, 4.dp)
                        .background(Color(252, 220, 139), RoundedCornerShape(8.dp))
                        .padding(4.dp)
                )
                Text(
                    chara.name,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    "覚醒${chara.rank}",
                    modifier = Modifier
                        .padding(8.dp, 4.dp, 4.dp, 0.dp)
                        .background(Color(224, 224, 192), RoundedCornerShape(8.dp))
                        .padding(4.dp)
                )
            }
        }
    }
}
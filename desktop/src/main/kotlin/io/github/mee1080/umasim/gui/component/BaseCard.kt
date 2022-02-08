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

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp

inline fun <T : Any> Modifier.ifNotNull(
    value: T?,
    builder: Modifier.(T) -> Modifier
) = value?.let { builder(it) } ?: this

inline fun Modifier.ifTrue(
    value: Boolean,
    builder: Modifier.() -> Modifier
) = if (value) builder() else this

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    color: Color? = null,
    selected: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    var hover by remember { mutableStateOf(false) }
    val hasBorder = true
    Card(
        modifier = Modifier
            .ifNotNull(onClick) { clickable(onClick = it) }
            .ifTrue(hasBorder) { border(1.dp, Color.Black) }
            .height(64.dp)
            .padding(8.dp),
//            .clickable {
//                onClick()
//            }
//            .pointerMoveFilter(
//                onEnter = { hover = true; false },
//                onExit = { hover = false; false },
//            ),
        backgroundColor = when {
            hover -> Color(255, 255, 128)
            selected -> Color(255, 255, 192)
            else -> MaterialTheme.colors.surface
        },
        elevation = if (hover) 6.dp else 4.dp,
    ) {
        Box(
            modifier = Modifier.padding(8.dp, 0.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (color != null) {
                Divider(
                    color = color,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 4.dp),
                    thickness = 4.dp
                )
            }
            content()
        }
    }
}
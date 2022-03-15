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
package io.github.mee1080.umasim.web.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.NumberInput
import org.jetbrains.compose.web.dom.Text

@Composable
fun LabeledNumberInput(
    label: String,
    value: Number,
    onInput: (Number) -> Unit
) {
    Div {
        Text(label)
        NumberInput(value) {
            style { width(4.em) }
            onInput { e -> e.value?.let { onInput(it) } }
        }
    }
}
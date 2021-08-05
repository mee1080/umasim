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
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Option
import org.jetbrains.compose.web.dom.Select
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLSelectElement

@Composable
fun LabeledSelect(
    label: String,
    selection: List<Pair<Int, String>>,
    selectedValue: Int,
    onSelect: (Int) -> Unit,
) {
    Div({ style { padding(8.px) } }) {
        Text(label)
        Select({
            prop(
                { e: HTMLSelectElement, v -> e.selectedIndex = v },
                selection.indexOfFirst { it.first == selectedValue })
            onChange { onSelect(it.value!!.toInt()) }
        }) {
            selection.forEach { (index, name) ->
                Option(index.toString(), { if (index == selectedValue) selected() }) { Text(name) }
            }
        }
    }
}
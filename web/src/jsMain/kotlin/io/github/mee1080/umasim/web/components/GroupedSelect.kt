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
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSelectElement

@Composable
fun GroupedSelect(
    label: String,
    selection: List<Triple<Int, String, String>>,
    selectedValue: Int,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    onSelect: (Int) -> Unit,
    afterContent: ContentBuilder<HTMLDivElement>? = null
) {
    Div({
        attrs?.invoke(this)
        style {
            padding(8.px)
        }
    }) {
        Text(label)
        Select({
            prop(
                { e: HTMLSelectElement, v -> e.selectedIndex = v },
                selection.indexOfFirst { it.first == selectedValue })
            onChange { onSelect(it.value!!.toInt()) }
        }) {
            val list = mutableListOf<Pair<String, List<Pair<Int, String>>>>()
            var current = "" to mutableListOf<Pair<Int, String>>()
            selection.forEach {
                if (it.third != current.first) {
                    current = it.third to mutableListOf()
                    list.add(current)
                }
                current.second.add(it.first to it.second)
            }
            list.forEach { (label, data) ->
                OptGroup(label) {
                    data.forEach { (index, name) ->
                        Option(index.toString(), { if (index == selectedValue) selected() }) { Text(name) }
                    }
                }
            }
        }
        afterContent?.invoke(this)
    }
}
/*
 * Copyright 2023 mee1080
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
@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.atoms

import androidx.compose.runtime.*
import io.github.mee1080.umasim.web.components.lib.generateId
import io.github.mee1080.umasim.web.components.lib.getElementById
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

@Composable
fun <T> AutoComplete(
    value: T,
    selection: List<T>,
    valid: MutableState<Boolean> = remember { mutableStateOf(true) },
    itemToString: (T) -> String = { it.toString() },
    itemToContent: @Composable ElementScope<MdMenuItemElement>.(T) -> Unit = { Text(itemToString(it)) },
    itemMatches: (T, String) -> Boolean = { item, input -> itemToString(item).contains(input) },
    maxDisplayCandidates: Int = 10,
    onChange: ((T) -> Unit)? = null,
) {
    val anchorId = remember { generateId() }
    val menuId = remember { generateId() }
    var inputText by remember { mutableStateOf(itemToString(value)) }
    var menuOpen by remember { mutableStateOf(false) }
    val displaySelection = remember(inputText, selection, itemToString, itemMatches, maxDisplayCandidates) {
        selection
            .map { it to itemToString(it) }
            .filter { itemMatches(it.first, inputText) }
            .take(maxDisplayCandidates)
    }
    LaunchedEffect(value) {
        inputText = itemToString(value)
    }
    LaunchedEffect(inputText, selection) {
        val item = displaySelection.firstOrNull { it.second == inputText }
        valid.value = item != null
        if (item != null && onChange != null) {
            onChange(item.first)
        }
    }
    Span({ style { position(Position.Relative) } }) {
        MdOutlinedTextField(
            inputText,
            trailingIcon = if (menuOpen) "arrow_drop_up" else "arrow_drop_down"
        ) {
            id(anchorId)
            if (!valid.value) error()
            onInput { input ->
                inputText = input
                menuOpen = true
            }
            onClick {
                menuOpen = true
            }
            onKeyDown {
                when (it.key) {
                    "Escape" -> {
                        menuOpen = false
                    }

                    "ArrowDown" -> {
                        if (!menuOpen) {
                            menuOpen = true
                        } else {
                            (getElementById<HTMLElement>(menuId).firstChild as? HTMLElement)?.focus()
                        }
                    }
                }
            }
        }
        MdMenu(anchorId, {
            id(menuId)
            if (menuOpen) open()
            defaultFocus(MdMenuFocusState.NONE)
            onClosed { menuOpen = false }
        }) {
            displaySelection.forEach { item ->
                MdMenuItem({
                    onSelected {
                        inputText = item.second
                    }
                }) {
                    itemToContent(item.first)
                }
            }
        }
    }
}
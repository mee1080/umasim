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

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.lib.stylePermanently
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementScope
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        stylePermanently(CardStyle)
    }
}

@Composable
fun Card(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: @Composable ElementScope<HTMLElement>.() -> Unit,
) {
    Div({
        classes(CardStyle.card)
        attrs?.invoke(this)
    }, content)
}

private object CardStyle : StyleSheet() {
    val card by style {
        display(DisplayStyle.InlineBlock)
        padding(16.px)
        borderRadius(12.px)
        color(MdSysColor.onSurface.value)
        backgroundColor(MdSysColor.surface.value)
        border(1.px, LineStyle.Solid, MdSysColor.outlineVariant.value)
    }
}
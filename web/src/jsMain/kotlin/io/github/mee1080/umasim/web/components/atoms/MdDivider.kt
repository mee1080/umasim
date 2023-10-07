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
import io.github.mee1080.umasim.web.components.lib.require
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        require("@material/web/divider/divider.js")
    }
}

@Composable
fun MdDivider(
    verticalMargin: CSSNumeric,
    attrs: AttrBuilderContext<MdDividerElement>? = null,
) {
    MdDivider {
        style { margin(verticalMargin, 0.px) }
        attrs?.invoke(this)
    }
}

@Composable
fun MdDivider(
    attrs: AttrBuilderContext<MdDividerElement>? = null,
) {
    TagElement<MdDividerElement>("md-divider", {
        attrs?.invoke(this)
    }, null)
}

abstract external class MdDividerElement : HTMLElement

fun AttrsScope<MdDividerElement>.inset() = attr("inset", "")
fun AttrsScope<MdDividerElement>.insetStart() = attr("inset-start", "")
fun AttrsScope<MdDividerElement>.insetEnd() = attr("inset-end", "")

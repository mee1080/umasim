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
import io.github.mee1080.umasim.web.components.lib.slot
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLSpanElement

private val initializer = object {
    init {
        require("@material/web/icon/icon.js")
    }
}

@Composable
fun MdIcon(
    name: String,
    attrs: AttrBuilderContext<MdIconElement>? = null,
) {
    TagElement<MdIconElement>("md-icon", {
        attrs?.invoke(this)
    }) {
        Text(name)
    }
}

@Composable
fun SlotIcon(name: String, icon: String?) {
    icon?.let { MdIcon(it) { slot(name) } }
}

abstract external class MdIconElement : HTMLSpanElement
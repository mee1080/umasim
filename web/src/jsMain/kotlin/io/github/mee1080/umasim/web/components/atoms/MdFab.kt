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
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLButtonElement

private val initializer = object {
    init {
        require("@material/web/fab/fab.js")
        require("@material/web/fab/branded-fab.js")
    }
}

enum class FabSize(val value: String) {
    Medium("medium"), Small("small"), Large("large")
}

enum class FabVariant(val value: String) {
    Surface("surface"), Primary("primary"), Secondary("secondary"), Tertiary("tertiary")
}

@Composable
fun MdFab(
    icon: String,
    attrs: AttrBuilderContext<MdFabElement>? = null,
) {
    TagElement<MdFabElement>("md-fab", {
        attrs?.invoke(this)
    }) {
        MdIcon(icon) { slot("icon") }
    }
}

@Composable
fun MdExtendedFab(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdFabElement>? = null,
) {
    TagElement<MdFabElement>("md-fab", {
        label(label)
        attrs?.invoke(this)
    }) {
        if (icon != null) MdIcon(icon) { slot("icon") }
    }
}

@Composable
fun MdBrandedFab(
    icon: String,
    attrs: AttrBuilderContext<MdFabElement>? = null,
) {
    TagElement<MdFabElement>("md-branded-fab", {
        attrs?.invoke(this)
    }) {
        MdIcon(icon) { slot("icon") }
    }
}

@Composable
fun MdBrandedExtendedFab(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdFabElement>? = null,
) {
    TagElement<MdFabElement>("md-branded-fab", {
        label(label)
        attrs?.invoke(this)
    }) {
        if (icon != null) MdIcon(icon) { slot("icon") }
    }
}

abstract external class MdFabElement : HTMLButtonElement

fun AttrsScope<MdFabElement>.variant(value: FabVariant) = attr("variant", value.value)
fun AttrsScope<MdFabElement>.size(value: FabSize) = attr("size", value.value)
fun AttrsScope<MdFabElement>.label(value: String) = attr("label", value)
fun AttrsScope<MdFabElement>.lowered() = attr("lowered", "")

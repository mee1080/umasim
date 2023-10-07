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
import io.github.mee1080.umasim.web.components.lib.addEventListener
import io.github.mee1080.umasim.web.components.lib.require
import io.github.mee1080.umasim.web.components.lib.slot
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        require("@material/web/chips/chip-set.js")
        require("@material/web/chips/assist-chip.js")
        require("@material/web/chips/filter-chip.js")
        require("@material/web/chips/input-chip.js")
        require("@material/web/chips/suggestion-chip.js")
    }
}

@Composable
fun MdChipSet(
    attrs: AttrBuilderContext<MdChipSetElement>? = null,
    content: @Composable ElementScope<MdChipSetElement>.() -> Unit,
) {
    TagElement<MdChipSetElement>("md-chip-set", {
        attrs?.invoke(this)
    }, content)
}

@Composable
private fun <T : MdChipElement> MdChip(
    type: String,
    label: String,
    icon: String?,
    attrs: AttrBuilderContext<T>?,
) {
    TagElement<T>("md-$type-chip", {
        label(label)
        attrs?.invoke(this)
    }) {
        if (icon != null) {
            MdIcon(icon) { slot("icon") }
        }
    }
}

@Composable
fun ElementScope<MdChipSetElement>.MdAssistChip(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdAssistChipElement>? = null,
) {
    MdChip("assist", label, icon, attrs)
}

@Composable
fun ElementScope<MdChipSetElement>.MdSuggestionChip(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdSuggestionChipElement>? = null,
) {
    MdChip("suggestion", label, icon, attrs)
}

@Composable
fun ElementScope<MdChipSetElement>.MdFilterChip(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdFilterChipElement>? = null,
) {
    MdChip("filter", label, icon) {
        attrs?.invoke(this)
    }
}

@Composable
fun ElementScope<MdChipSetElement>.MdInputChip(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdInputChipElement>? = null,
) {
    MdChip("input", label, icon) {
        attrs?.invoke(this)
    }
}

sealed external class MdChipSetElement : HTMLElement

sealed external class MdChipElement : HTMLButtonElement
abstract external class MdAssistChipElement : MdChipElement
abstract external class MdSuggestionChipElement : MdChipElement
abstract external class MdFilterChipElement : MdChipElement
abstract external class MdInputChipElement : MdChipElement

/** Whether or not the chip is disabled. */
fun AttrsScope<MdChipElement>.disabled() = attr("disabled", "")

/** When true, allow disabled chips to be focused with arrow keys. */
fun AttrsScope<MdChipElement>.alwaysFocusable() = attr("always-focusable", "")

/** The label of the chip. */
fun AttrsScope<MdChipElement>.label(value: String) = attr("label", value)

fun AttrsScope<MdAssistChipElement>.elevated() = attr("elevated", "")
fun AttrsScope<MdAssistChipElement>.href(value: String) = attr("href", value)
fun AttrsScope<MdAssistChipElement>.target(value: ATarget) = attr("target", value.targetStr)

fun AttrsScope<MdFilterChipElement>.elevated() = attr("elevated", "")
fun AttrsScope<MdFilterChipElement>.removable() = attr("removable", "")
fun AttrsScope<MdFilterChipElement>.selected() = attr("selected", "")
fun AttrsScope<MdFilterChipElement>.ariaLabelRemove(value: String) = attr("", value)

fun AttrsScope<MdFilterChipElement>.onSelected(listener: (value: Boolean) -> Unit) =
    addEventListener("click", { selected }, listener)

fun AttrsScope<MdFilterChipElement>.onRemove(listener: () -> Unit) =
    addEventListener("remove", listener)

fun AttrsScope<MdInputChipElement>.avatar() = attr("avatar", "")
fun AttrsScope<MdInputChipElement>.href(value: String) = attr("href", value)
fun AttrsScope<MdInputChipElement>.target(value: ATarget) = attr("target", value.targetStr)
fun AttrsScope<MdInputChipElement>.removeOnly() = attr("remove-only", "")
fun AttrsScope<MdInputChipElement>.selected() = attr("selected", "")
fun AttrsScope<MdInputChipElement>.ariaLabelRemove(value: String) = attr("", value)

fun AttrsScope<MdInputChipElement>.onRemove(listener: () -> Unit) =
    addEventListener("remove", listener)

fun AttrsScope<MdSuggestionChipElement>.elevated() = attr("elevated", "")
fun AttrsScope<MdSuggestionChipElement>.href(value: String) = attr("href", value)
fun AttrsScope<MdSuggestionChipElement>.target(value: ATarget) = attr("target", value.targetStr)

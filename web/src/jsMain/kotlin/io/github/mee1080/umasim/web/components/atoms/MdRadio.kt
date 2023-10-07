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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import io.github.mee1080.umasim.web.components.lib.addEventListener
import io.github.mee1080.umasim.web.components.lib.generateId
import io.github.mee1080.umasim.web.components.lib.require
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.HTMLSpanElement

private val initializer = object {
    init {
        require("@material/web/radio/radio.js")
    }
}

@Composable
fun <T : Any> MdRadioGroup(
    selection: List<T>,
    selectedItem: T?,
    attrs: AttrBuilderContext<HTMLSpanElement>? = null,
    radioAttrs: AttrBuilderContext<MdRadioElement>? = null,
    labelAttrs: AttrsScope<HTMLLabelElement>.() -> Unit = {},
    onSelect: (T) -> Unit = {},
    itemToKey: (T) -> Any = { it },
    itemToLabel: (T) -> String = { it.toString() }
) {
    val name = remember { generateId() }
    Span({
        style {
            display(DisplayStyle.Flex)
            columnGap(12.px)
            padding(12.px)
        }
        attrs?.invoke(this)
    }) {
        selection.forEach { item ->
            key(itemToKey(item)) {
                MdRadio(
                    itemToLabel(item),
                    item == selectedItem,
                    labelAttrs,
                ) {
                    name(name)
                    onSelect { onSelect(item) }
                    radioAttrs?.invoke(this)
                }
            }
        }
    }
}

@Composable
fun MdRadio(
    label: String,
    checked: Boolean = false,
    labelAttrs: AttrsScope<HTMLLabelElement>.() -> Unit = {},
    attrs: AttrBuilderContext<MdRadioElement>? = null,
) {
    Label(attrs = {
        style {
            display(DisplayStyle.LegacyInlineFlex)
            columnGap(4.px)
            alignItems(AlignItems.Center)
        }
        labelAttrs()
    }) {
        MdRadio(checked, attrs)
        Text(label)
    }
}

@Composable
fun MdRadio(
    checked: Boolean = false,
    attrs: AttrBuilderContext<MdRadioElement>? = null,
) {
    TagElement<MdRadioElement>("md-radio", {
        attrs?.invoke(this)
        if (checked) checked()
    }, null)
}

abstract external class MdRadioElement : HTMLElement

/** Whether or not the radio is disabled. */
fun AttrsScope<MdRadioElement>.disabled() = attr("disabled", "")

/** The element value to use in form submission when checked. */
fun AttrsScope<MdRadioElement>.value(value: String) = attr("value", value)

fun AttrsScope<MdRadioElement>.checked() = attr("checked", "")
fun AttrsScope<MdRadioElement>.name(value: String) = attr("name", value)
fun AttrsScope<MdRadioElement>.form(value: String) = attr("form", value)

fun AttrsScope<MdRadioElement>.onSelect(listener: () -> Unit) {
    addEventListener<Boolean>("change", { checked }) { if (it) listener() }
}
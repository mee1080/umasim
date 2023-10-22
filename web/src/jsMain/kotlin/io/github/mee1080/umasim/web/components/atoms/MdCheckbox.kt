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
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLLabelElement

private val initializer = object {
    init {
        require("@material/web/checkbox/checkbox.js")
    }
}

@Composable
fun MdCheckbox(
    label: String,
    checked: Boolean = false,
    labelAttrs: AttrsScope<HTMLLabelElement>.() -> Unit = {},
    attrs: AttrBuilderContext<MdCheckboxElement>? = null,
) {
    Label(attrs = {
        style {
            display(DisplayStyle.LegacyInlineFlex)
            columnGap(4.px)
            alignItems(AlignItems.Center)
            cursor("pointer")
        }
        labelAttrs()
    }) {
        MdCheckbox(checked, attrs)
        Text(label)
    }
}

@Composable
fun MdCheckbox(
    checked: Boolean = false,
    attrs: AttrBuilderContext<MdCheckboxElement>? = null,
) {
    TagElement<MdCheckboxElement>("md-checkbox", {
        attrs?.invoke(this)
        if (checked) checked()
    }, null)
}

abstract class MdCheckboxElement : HTMLElement()

/** Whether or not the checkbox is selected. */
fun AttrsScope<MdCheckboxElement>.checked() = attr("checked", "")

/** Whether or not the checkbox is disabled. */
fun AttrsScope<MdCheckboxElement>.disabled() = attr("disabled", "")

/** Whether or not the checkbox is indeterminate. */
fun AttrsScope<MdCheckboxElement>.indeterminate() = attr("indeterminate", "")

/** When true, require the checkbox to be selected when participating in form submission. */
fun AttrsScope<MdCheckboxElement>.required() = attr("required", "")

/** The value of the checkbox that is submitted with a form when selected. */
fun AttrsScope<MdCheckboxElement>.value(value: String) = attr("value", value)

fun AttrsScope<MdCheckboxElement>.name(value: String) = attr("name", value)
fun AttrsScope<MdCheckboxElement>.form(value: String) = attr("form", value)

fun AttrsScope<MdCheckboxElement>.onChange(listener: (Boolean) -> Unit) {
    addEventListener("change", { checked }, listener)
}

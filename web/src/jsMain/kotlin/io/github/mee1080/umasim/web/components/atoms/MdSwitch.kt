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
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLLabelElement

private val initializer = object {
    init {
        require("@material/web/switch/switch.js")
    }
}

@Composable
fun MdSwitch(
    label: String,
    checked: Boolean = false,
    labelAttrs: AttrsScope<HTMLLabelElement>.() -> Unit = {},
    attrs: AttrBuilderContext<MdSwitchElement>? = null,
) {
    Label(attrs = {
        style {
            display(DisplayStyle.LegacyInlineFlex)
            alignItems(AlignItems.Center)
        }
        labelAttrs()
    }) {
        Text(label)
        MdSwitch(checked, attrs)
    }
}

@Composable
fun MdSwitch(
    selected: Boolean = false,
    attrs: AttrBuilderContext<MdSwitchElement>? = null,
) {
    TagElement<MdSwitchElement>("md-switch", {
        attrs?.invoke(this)
        if (selected) selected()
    }, null)
}

abstract external class MdSwitchElement : HTMLElement

/** Disables the switch and makes it non-interactive. */
fun AttrsScope<MdSwitchElement>.disabled() = attr("disabled", "")

/** Puts the switch in the selected state and sets the form submission value to the value property. */
fun AttrsScope<MdSwitchElement>.selected() = attr("selected", "")

/** Shows both the selected and deselected icons. */
fun AttrsScope<MdSwitchElement>.icons() = attr("icons", "")

/** Shows only the selected icon, and not the deselected icon. If true, overrides the behavior of the icons property. */
fun AttrsScope<MdSwitchElement>.showOnlySelectedIcon() = attr("show-only-selected-icon", "")

/**
 * When true, require the switch to be selected when participating in form submission.
 * https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/checkbox#validation
 */
fun AttrsScope<MdSwitchElement>.required() = attr("required", "")

/** The value associated with this switch on form submission. null is submitted when selected is false. */
fun AttrsScope<MdSwitchElement>.value(value: String) = attr("value", value)

fun AttrsScope<MdSwitchElement>.onInput(listener: (Boolean) -> Unit) =
    addEventListener("input", { selected }, listener)

fun AttrsScope<MdSwitchElement>.onChange(listener: (Boolean) -> Unit) =
    addEventListener("change", { selected }, listener)

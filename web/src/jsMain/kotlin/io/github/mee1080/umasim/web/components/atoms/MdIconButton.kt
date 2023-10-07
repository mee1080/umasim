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
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLButtonElement

private val initializer = object {
    init {
        require("@material/web/iconbutton/icon-button.js")
        require("@material/web/iconbutton/filled-icon-button.js")
        require("@material/web/iconbutton/filled-tonal-icon-button.js")
        require("@material/web/iconbutton/outlined-icon-button.js")
    }
}

@Composable
fun MdIconButton(
    icon: String,
    attrs: AttrBuilderContext<MdIconButtonElement>? = null,
) = MdIconButton("", icon, attrs)

@Composable
fun MdFilledIconButton(
    icon: String,
    attrs: AttrBuilderContext<MdIconButtonElement>? = null,
) = MdIconButton("filled-", icon, attrs)

@Composable
fun MdFilledTonalIconButton(
    icon: String,
    attrs: AttrBuilderContext<MdIconButtonElement>? = null,
) = MdIconButton("filled-tonal-", icon, attrs)

@Composable
fun MdOutlinedIconButton(
    icon: String,
    attrs: AttrBuilderContext<MdIconButtonElement>? = null,
) = MdIconButton("outlined-", icon, attrs)

@Composable
private fun MdIconButton(
    type: String,
    icon: String,
    attrs: AttrBuilderContext<MdIconButtonElement>? = null,
) {
    TagElement<MdIconButtonElement>("md-${type}icon-button", {
        attrs?.invoke(this)
    }) {
        MdIcon(icon)
    }
}

abstract external class MdIconButtonElement : HTMLButtonElement

fun AttrsScope<MdIconButtonElement>.disabled() = attr("disabled", "")
fun AttrsScope<MdIconButtonElement>.flipIconInRtl() = attr("flip-icon-in-rtl", "")
fun AttrsScope<MdIconButtonElement>.href(value: String) = attr("href", value)
fun AttrsScope<MdIconButtonElement>.target(value: ATarget) = attr("target", value.targetStr)
fun AttrsScope<MdIconButtonElement>.ariaLabelSelected(value: String) = attr("aria-label-selected", value)
fun AttrsScope<MdIconButtonElement>.toggle() = attr("toggle", "")
fun AttrsScope<MdIconButtonElement>.selected() = attr("selected", "")
fun AttrsScope<MdIconButtonElement>.type(value: ButtonType) = attr("type", value.str)
fun AttrsScope<MdIconButtonElement>.value(value: String) = attr("value", value)
fun AttrsScope<MdIconButtonElement>.name(value: String) = attr("name", value)
fun AttrsScope<MdIconButtonElement>.form(value: String) = attr("form", value)

fun AttrsScope<MdIconButtonElement>.onInput(listener: (Boolean) -> Unit) =
    addEventListener("input", { selected }, listener)

fun AttrsScope<MdIconButtonElement>.onChange(listener: (Boolean) -> Unit) =
    addEventListener("change", { selected }, listener)

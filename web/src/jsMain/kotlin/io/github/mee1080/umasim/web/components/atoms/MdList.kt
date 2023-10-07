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
import io.github.mee1080.umasim.web.components.lib.Slot
import io.github.mee1080.umasim.web.components.lib.require
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        require("@material/web/list/list.js")
        require("@material/web/list/list-item.js")
    }
}

enum class MdListItemType(val attrValue: String) {
    Text("text"), Button("button"), Link("link"),
}

@Composable
fun MdList(
    attrs: AttrBuilderContext<MdListElement>? = null,
    content: @Composable ElementScope<MdListElement>.() -> Unit,
) {
    TagElement<MdListElement>("md-list", {
        attrs?.invoke(this)
    }, content)
}

@Composable
fun ElementScope<MdListElement>.MdListItem(
    headline: String,
    supportingText: String? = null,
    iconStart: String? = null,
    iconEnd: String? = null,
    attrs: AttrBuilderContext<MdListItemElement>? = null,
) {
    MdListItem(
        attrs,
        supportingText = supportingText?.let { { Text(supportingText) } },
        iconStart = iconStart,
        iconEnd = iconEnd,
    ) { Text(headline) }
}

@Composable
fun ElementScope<MdListElement>.MdListItem(
    attrs: AttrBuilderContext<MdListItemElement>? = null,
    overline: ContentBuilder<HTMLElement>? = null,
    headline: ContentBuilder<HTMLElement>? = null,
    supportingText: ContentBuilder<HTMLElement>? = null,
    trailingSupportingText: ContentBuilder<HTMLElement>? = null,
    iconStart: String? = null,
    iconEnd: String? = null,
    content: ContentBuilder<MdListItemElement>? = null,
) {
    TagElement<MdListItemElement>("md-list-item", {
        attrs?.invoke(this)
    }) {
        content?.invoke(this)
        Slot("overline", overline)
        Slot("headline", headline)
        Slot("supporting-text", supportingText)
        Slot("trailing-supporting-text", trailingSupportingText)
        SlotIcon("start", iconStart)
        SlotIcon("end", iconEnd)
    }
}

abstract external class MdListElement : HTMLElement
abstract external class MdListItemElement : HTMLElement

fun AttrsScope<MdListItemElement>.disabled() = attr("disabled", "")
fun AttrsScope<MdListItemElement>.type(value: MdListItemType) = attr("type", value.attrValue)
fun AttrsScope<MdListItemElement>.href(value: String) = attr("href", value)
fun AttrsScope<MdListItemElement>.target(value: ATarget) = attr("target", value.targetStr)

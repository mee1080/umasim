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
import io.github.mee1080.umasim.web.components.lib.addEventListener
import io.github.mee1080.umasim.web.components.lib.require
import io.github.mee1080.umasim.web.components.lib.slot
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        require("@material/web/tabs/tabs.js")
        require("@material/web/tabs/primary-tab.js")
        require("@material/web/tabs/secondary-tab.js")
    }
}

@Composable
fun <T> MdPrimaryTabs(
    selection: List<T>,
    selectedItem: T? = null,
    itemToLabel: (T) -> String = { it.toString() },
    itemToIcon: (T) -> String? = { null },
    onSelect: (T) -> Unit = {},
    tabAttrs: AttrBuilderContext<MdPrimaryTabElement>? = null,
    attrs: AttrBuilderContext<MdTabsElement>? = null,
) {
    MdPrimaryTabs({
        onChange {
            selection.getOrNull(it.toInt())?.let(onSelect)
        }
        attrs?.invoke(this)
    }) {
        selection.forEach {
            key(it) {
                MdTab(it == selectedItem, tabAttrs, itemToIcon(it)) { Text(itemToLabel(it)) }
            }
        }
    }
}

@Composable
fun <T> MdSecondaryTabs(
    selection: List<T>,
    selectedItem: T? = null,
    itemToLabel: (T) -> String = { it.toString() },
    itemToIcon: (T) -> String? = { null },
    onSelect: (T) -> Unit = {},
    tabAttrs: AttrBuilderContext<MdSecondaryTabElement>? = null,
    attrs: AttrBuilderContext<MdTabsElement>? = null,
) {
    MdSecondaryTabs({
        onChange {
            selection.getOrNull(it.toInt())?.let(onSelect)
        }
        attrs?.invoke(this)
    }) {
        selection.forEach {
            key(it) {
                MdTab(it == selectedItem, tabAttrs, itemToIcon(it)) { Text(itemToLabel(it)) }
            }
        }
    }
}

@Composable
fun MdPrimaryTabs(
    attrs: AttrBuilderContext<MdTabsElement>? = null,
    content: @Composable ElementScope<MdPrimaryTabsElement>.() -> Unit
) {
    MdTabs(attrs, content)
}

@Composable
fun MdSecondaryTabs(
    attrs: AttrBuilderContext<MdTabsElement>? = null,
    content: @Composable ElementScope<MdSecondaryTabsElement>.() -> Unit
) {
    MdTabs(attrs, content)
}

@Composable
private fun <T : MdTabsElement> MdTabs(
    attrs: AttrBuilderContext<MdTabsElement>? = null,
    content: @Composable ElementScope<T>.() -> Unit
) {
    TagElement<T>("md-tabs", {
        attrs?.invoke(this)
    }, content)
}

@Composable
fun ElementScope<MdPrimaryTabsElement>.MdTab(
    active: Boolean,
    attrs: AttrBuilderContext<MdPrimaryTabElement>? = null,
    icon: String? = null,
    content: @Composable ElementScope<MdPrimaryTabElement>.() -> Unit,
) {
    TagElement<MdPrimaryTabElement>("md-primary-tab", {
        if (active) active()
        attrs?.invoke(this)
    }) {
        icon?.let { MdIcon(it) { slot("icon") } }
        content()
    }
}

@Composable
fun ElementScope<MdSecondaryTabsElement>.MdTab(
    active: Boolean,
    attrs: AttrBuilderContext<MdSecondaryTabElement>? = null,
    icon: String? = null,
    content: @Composable ElementScope<MdSecondaryTabElement>.() -> Unit,
) {
    TagElement<MdSecondaryTabElement>("md-secondary-tab", {
        if (active) active()
        attrs?.invoke(this)
    }) {
        icon?.let { MdIcon(it) { slot("icon") } }
        content()
    }
}

abstract external class MdTabsElement : HTMLElement
abstract external class MdPrimaryTabsElement : MdTabsElement
abstract external class MdSecondaryTabsElement : MdTabsElement
abstract external class MdTabElement : HTMLElement
abstract external class MdPrimaryTabElement : MdTabElement
abstract external class MdSecondaryTabElement : MdTabElement

/** Whether or not to automatically select a tab when it is focused. */
fun AttrsScope<MdTabsElement>.autoActivate() = attr("auto-activate", "")

fun AttrsScope<MdTabsElement>.onChange(listener: (Number) -> Unit) =
    addEventListener("change", {
        console.log(this)
        activeTabIndex
    }, listener)

/** Whether or not the tab is selected. */
fun AttrsScope<MdTabElement>.active() = attr("active", "")

/** In SSR, set this to true when an icon is present. */
fun AttrsScope<MdTabElement>.hasIcon() = attr("has-icon", "")

/** In SSR, set this to true when there is no label and only an icon. */
fun AttrsScope<MdTabElement>.iconOnly() = attr("icon-only", "")

/** Whether or not the icon renders inline with label or stacked vertically. */
fun AttrsScope<MdPrimaryTabElement>.inlineIcon() = attr("inline-icon", "")

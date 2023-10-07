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
package io.github.mee1080.umasim.web.components.parts

import androidx.compose.runtime.*
import io.github.mee1080.umasim.web.components.atoms.MdElevation
import io.github.mee1080.umasim.web.components.atoms.MdIcon
import io.github.mee1080.umasim.web.components.atoms.MdSysColor
import io.github.mee1080.umasim.web.components.atoms.mdElevationLevel
import io.github.mee1080.umasim.web.components.lib.stylePermanently
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

@Composable
fun HideBlock(
    header: String,
    initialOpen: Boolean = false,
    wrapperAttr: AttrBuilderContext<HTMLElement>? = null,
    headerAttr: AttrBuilderContext<HTMLElement>? = null,
    headerClosed: String = header,
    headerAttrClosed: AttrBuilderContext<HTMLElement>? = headerAttr,
    contentAttr: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>,
) {
    var open by remember { mutableStateOf(initialOpen) }
    HideBlock(
        open = open,
        onToggle = { open = !open },
        header = { Text(header) },
        wrapperAttr = wrapperAttr,
        headerAttr = headerAttr,
        headerClosed = { Text(headerClosed) },
        headerAttrClosed = headerAttrClosed,
        contentAttr = contentAttr,
        content = content,
    )
}

@Composable
fun HideBlock(
    open: Boolean,
    onToggle: () -> Unit,
    header: ContentBuilder<HTMLElement>,
    wrapperAttr: AttrBuilderContext<HTMLElement>? = null,
    headerAttr: AttrBuilderContext<HTMLElement>? = null,
    headerClosed: ContentBuilder<HTMLElement> = header,
    headerAttrClosed: AttrBuilderContext<HTMLElement>? = headerAttr,
    contentAttr: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>,
) {
    Div({
        classes(HideBlockStyle.wrapper)
        wrapperAttr?.invoke(this)
    }) {
        MdElevation()
        Div({
            if (open) {
                classes(HideBlockStyle.header, HideBlockStyle.open)
                headerAttr?.invoke(this)
            } else {
                classes(HideBlockStyle.header)
                headerAttrClosed?.invoke(this)
            }
            onClick {
                onToggle()
            }
        }) {
            MdIcon(if (open) "arrow_drop_up" else "arrow_drop_down")
            if (open) header() else headerClosed()
        }
        Div({
            if (open) {
                classes(HideBlockStyle.content, HideBlockStyle.open)
            } else {
                classes(HideBlockStyle.content)
            }
            contentAttr?.invoke(this)
        }, content)
    }
}

@Composable
fun NestedHideBlock(
    header: String,
    initialOpen: Boolean = false,
    wrapperAttr: AttrBuilderContext<HTMLElement>? = null,
    headerAttr: AttrBuilderContext<HTMLElement>? = null,
    headerClosed: String = header,
    headerAttrClosed: AttrBuilderContext<HTMLElement>? = headerAttr,
    contentAttr: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>,
) {
    HideBlock(
        header = header,
        initialOpen = initialOpen,
        wrapperAttr = {
            classes(HideBlockStyle.nested)
            wrapperAttr?.invoke(this)
        },
        headerAttr = headerAttr,
        headerClosed = headerClosed,
        headerAttrClosed = headerAttrClosed,
        contentAttr = contentAttr,
        content = content,
    )
}

@OptIn(ExperimentalComposeWebApi::class)
private object HideBlockStyle : StyleSheet() {
    val wrapper by style {
        position(Position.Relative)
        borderRadius(16.px)
        margin(8.px)
        mdElevationLevel(1)
    }

    val nested by style {
        mdElevationLevel(1)
    }

    val open by style {}

    val header by style {
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        padding(8.px)
        gap(8.px)
        flexWrap(FlexWrap.Nowrap)
        overflowX("hidden")
        width(100.percent)
        borderRadius(16.px)
        cursor("pointer")

        color(MdSysColor.onSecondaryContainer.value)
        backgroundColor(MdSysColor.secondaryContainer.value)
    }

    init {
        ".$header.$open" style {
            borderRadius(16.px, 16.px, 0.px, 0.px)
        }

        ".$nested .$header" style {
            color(MdSysColor.onTertiaryContainer.value)
            backgroundColor(MdSysColor.tertiaryContainer.value)
        }
    }

    val content by style {
        width(100.percent)
        height(0.px)
        padding(0.px, 8.px, 0.px, 8.px)
        overflow("hidden")
        borderRadius(0.px, 0.px, 16.px, 16.px)

        color(MdSysColor.onSurface.value)
        backgroundColor(MdSysColor.surfaceContainerLow.value)
    }

    init {
        ".$content.$open" style {
            height(auto)
            padding(8.px)
            overflow("unset")
        }

        ".$nested .$content" style {
            backgroundColor(MdSysColor.surfaceContainer.value)
        }
    }
}

private val initializer = object {
    init {
        stylePermanently(HideBlockStyle)
    }
}

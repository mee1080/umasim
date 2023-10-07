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
import io.github.mee1080.umasim.web.components.lib.*
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        require("@material/web/menu/menu.js")
        require("@material/web/menu/menu-item.js")
        require("@material/web/menu/sub-menu.js")
    }
}

enum class MdMenuCorner(val attrValue: String) {
    BottomStart("end-start"),
    BottomEnd("end-end"),
    TopStart("start-start"),
    TopEnd("start-end"),
}

enum class MdMenuPosition(val attrValue: String) {
    Absolute("absolute"),
    Fixed("fixed"),
}

enum class MdMenuFocusState(val attrValue: String) {
    NONE("none"),
    LIST_ROOT("list-root"),
    FIRST_ITEM("first-item"),
    LAST_ITEM("last-item"),
}

enum class MdMenuItemType(val attrValue: String) {
    MenuItem("menuitem"),
    Option("option"),
    Button("button"),
    Link("link"),
}

@Composable
fun MdMenu(
    anchor: String,
    attrs: AttrBuilderContext<MdMenuElement>? = null,
    content: @Composable ElementScope<MdMenuElement>.() -> Unit,
) {
    MdMenu({
        anchor(anchor)
        attrs?.invoke(this)
    }, content)
}

@Composable
private fun MdMenu(
    attrs: AttrBuilderContext<MdMenuElement>? = null,
    content: @Composable ElementScope<MdMenuElement>.() -> Unit,
) {
    TagElement<MdMenuElement>("md-menu", {
        attrs?.invoke(this)
    }, content)
}

@Composable
fun ElementScope<MdMenuElement>.MdMenuItem(
    headline: String,
    supportingText: String? = null,
    iconStart: String? = null,
    iconEnd: String? = null,
    attrs: AttrBuilderContext<MdMenuItemElement>? = null,
) {
    MdMenuItem(
        attrs,
        supportingText = supportingText?.let { { Text(supportingText) } },
        iconStart = iconStart,
        iconEnd = iconEnd,
    ) { Text(headline) }
}

@Composable
fun ElementScope<MdMenuElement>.MdMenuItem(
    attrs: AttrBuilderContext<MdMenuItemElement>? = null,
    overline: ContentBuilder<HTMLElement>? = null,
    headline: ContentBuilder<HTMLElement>? = null,
    supportingText: ContentBuilder<HTMLElement>? = null,
    trailingSupportingText: ContentBuilder<HTMLElement>? = null,
    iconStart: String? = null,
    iconEnd: String? = null,
    content: ContentBuilder<MdMenuItemElement>? = null,
) {
    TagElement<MdMenuItemElement>("md-menu-item", {
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

@Composable
fun ElementScope<MdMenuElement>.MdSubMenu(
    headline: String,
    attrs: AttrBuilderContext<MdSubMenuElement>? = null,
    itemAttrs: AttrBuilderContext<MdMenuItemElement>? = null,
    supportingText: String? = null,
    iconStart: String? = null,
    iconEnd: String? = null,
    subMenuAttrs: AttrBuilderContext<MdMenuElement>? = null,
    subMenuContent: @Composable ElementScope<MdMenuElement>.() -> Unit,
) {
    MdSubMenu(
        attrs, itemAttrs,
        headline = { Text(headline) },
        supportingText = supportingText?.let { { Text(supportingText) } },
        iconStart = iconStart,
        iconEnd = iconEnd,
        subMenuAttrs = subMenuAttrs,
        subMenuContent = subMenuContent,
    )
}

@Composable
fun ElementScope<MdMenuElement>.MdSubMenu(
    attrs: AttrBuilderContext<MdSubMenuElement>? = null,
    itemAttrs: AttrBuilderContext<MdMenuItemElement>? = null,
    overline: ContentBuilder<HTMLElement>? = null,
    headline: ContentBuilder<HTMLElement>? = null,
    supportingText: ContentBuilder<HTMLElement>? = null,
    trailingSupportingText: ContentBuilder<HTMLElement>? = null,
    iconStart: String? = null,
    iconEnd: String? = null,
    content: ContentBuilder<MdMenuItemElement>? = null,
    subMenuAttrs: AttrBuilderContext<MdMenuElement>? = null,
    subMenuContent: @Composable ElementScope<MdMenuElement>.() -> Unit,
) {
    TagElement<MdSubMenuElement>("md-sub-menu", attrs) {
        MdMenuItem({
            slot("item")
            itemAttrs?.invoke(this)
        }, overline, headline, supportingText, trailingSupportingText, iconStart, iconEnd, content)
        MdMenu({
            subMenuAttrs?.invoke(this)
            slot("menu")
        }, subMenuContent)
    }
}

abstract external class MdMenuElement : HTMLElement
abstract external class MdMenuItemElement : HTMLElement
abstract external class MdSubMenuElement : HTMLElement

/** The ID of the element in the same root node in which the menu should align to. Overrides setting anchorElement = elementReference. */
fun AttrsScope<MdMenuElement>.anchor(value: String) = attr("anchor", value)

/**
 * Whether the positioning algorithm should calculate relative to the parent of the anchor element (absolute) or relative to the window (fixed).
 * Examples for position = 'fixed':
 * - If there is no position:relative in the given parent tree and the surface is position:absolute
 * - If the surface is position:fixed
 * - If the surface is in the "top layer"
 * - The anchor and the surface do not share a common position:relative ancestor
 * When using positioning = fixed, in most cases, the menu should position itself above most other position:absolute or position:fixed elements when placed inside of them. e.g. using a menu inside of an md-dialog.
 * NOTE: Fixed menus will not scroll with the page and will be fixed to the window instead.
 */
fun AttrsScope<MdMenuElement>.positioning(value: MdMenuPosition) = attr("positioning", value.attrValue)

/** Skips the opening and closing animations. */
fun AttrsScope<MdMenuElement>.quick() = attr("quick", "")

/**
 * Displays overflow content like a submenu.
 * NOTE: This may cause adverse effects if you set md-menu {max-height:...} and have items overflowing items in the "y" direction.
 */
fun AttrsScope<MdMenuElement>.hasOverflow() = attr("has-overflow", "")

/** Opens the menu and makes it visible. Alternative to the .show() and .close() methods */
fun AttrsScope<MdMenuElement>.open() = attr("open", "")

/**
 * Offsets the menu's inline alignment from the anchor by the given number in pixels. This value is direction aware and will follow the LTR / RTL direction.
 * e.g. LTR: positive -> right, negative -> left RTL: positive -> left, negative -> right
 */
fun AttrsScope<MdMenuElement>.xOffset(value: Number) = attr("x-offset", value.toString())

/**
 * Offsets the menu's block alignment from the anchor by the given number in pixels.
 * e.g. positive -> down, negative -> up
 */
fun AttrsScope<MdMenuElement>.yOffset(value: Number) = attr("y-offset", value.toString())

/** The max time between the keystrokes of the typeahead menu behavior before it clears the typeahead buffer. */
fun AttrsScope<MdMenuElement>.typeaheadDelay(value: Number) = attr("typeahead-delay", value.toString())

/**
 * The corner of the anchor which to align the menu in the standard logical property style of-e.g. 'end-start'.
 * NOTE: This value may not be respected by the menu positioning algorithm if the menu would render outisde the viewport.
 */
fun AttrsScope<MdMenuElement>.anchorCorner(value: MdMenuCorner) = attr("anchor-corner", value.attrValue)

/**
 * The corner of the menu which to align the anchor in the standard logical property style of-e.g. 'start-start'.
 * NOTE: This value may not be respected by the menu positioning algorithm if the menu would render outisde the viewport.
 */
fun AttrsScope<MdMenuElement>.menuCorner(value: MdMenuCorner) = attr("menu-corner", value.attrValue)

/**
 * Keeps the user clicks outside the menu.
 * NOTE: clicking outside may still cause focusout to close the menu so see stayOpenOnFocusout.
 */
fun AttrsScope<MdMenuElement>.stayOpenOnOutsideClick() = attr("stay-open-on-outside-click", "")

/**
 * Keeps the menu open when focus leaves the menu's composed subtree.
 * NOTE: Focusout behavior will stop propagation of the focusout event. Set this property to true to opt-out of menu's focusout handling altogether.
 */
fun AttrsScope<MdMenuElement>.stayOpenOnFocusout() = attr("stay-open-on-focusout", "")

/** After closing, does not restore focus to the last focused element before the menu was opened. */
fun AttrsScope<MdMenuElement>.skipRestoreFocus() = attr("skip-restore-focus", "")

/**
 * The element that should be focused by default once opened.
 * NOTE: When setting default focus to 'LIST_ROOT', remember to change tabindex to 0 and change md-menu's display to something other than display: contents when necessary.
 */
fun AttrsScope<MdMenuElement>.defaultFocus(value: MdMenuFocusState) = attr("default-focus", value.attrValue)

fun AttrsScope<MdMenuElement>.onOpening(listener: () -> Unit) = addEventListener("opening", listener)
fun AttrsScope<MdMenuElement>.onOpened(listener: () -> Unit) = addEventListener("opened", listener)
fun AttrsScope<MdMenuElement>.onClosing(listener: () -> Unit) = addEventListener("closing", listener)
fun AttrsScope<MdMenuElement>.onClosed(listener: () -> Unit) = addEventListener("closed", listener)

fun AttrsScope<MdMenuItemElement>.disabled() = attr("disabled", "")
fun AttrsScope<MdMenuItemElement>.type(value: MdMenuItemType) = attr("type", value.attrValue)
fun AttrsScope<MdMenuItemElement>.href(value: String) = attr("href", value)
fun AttrsScope<MdMenuItemElement>.target(value: ATarget) = attr("target", value.targetStr)
fun AttrsScope<MdMenuItemElement>.keepOpen() = attr("keep-open", "")
fun AttrsScope<MdMenuItemElement>.onSelected(listener: () -> Unit) = addEventListener("close-menu", listener)

fun AttrsScope<MdSubMenuElement>.anchorCorner(value: MdMenuCorner) = attr("anchor-corner", value.attrValue)
fun AttrsScope<MdSubMenuElement>.menuCorner(value: MdMenuCorner) = attr("menu-corner", value.attrValue)
fun AttrsScope<MdSubMenuElement>.hoverOpenDelay(value: Number) = attr("hover-open-delay", value.toString())
fun AttrsScope<MdSubMenuElement>.hoverCloseDelay(value: Number) = attr("hover-close-delay", value.toString())

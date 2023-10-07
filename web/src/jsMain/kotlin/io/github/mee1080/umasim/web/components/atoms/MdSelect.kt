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
import io.github.mee1080.umasim.web.components.lib.*
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        require("@material/web/select/filled-select.js")
        require("@material/web/select/outlined-select.js")
        require("@material/web/select/select-option.js")
    }
}

@Composable
fun <T : Any> MdFilledSelect(
    selection: List<T>,
    selectedItem: T?,
    attrs: AttrBuilderContext<MdSelectElement>? = null,
    onSelect: (T) -> Unit = {},
    itemToValue: (T) -> String = { it.toString() },
    itemToDisplayText: (T) -> String = itemToValue,
) {
    val valueToItem = selection.associateBy { itemToValue(it) }
    MdSelect("filled", {
        selectedItem?.let {
            value(itemToValue(it))
        }
        attrs?.invoke(this)
        onChange {
            val item = valueToItem[it] ?: return@onChange
            onSelect(item)
        }
    }) {
        selection.forEach {
            MdSelectOption(itemToValue(it), itemToDisplayText(it))
        }
    }
}

@Composable
fun <T : Any> MdOutlinedSelect(
    selection: List<T>,
    selectedItem: T?,
    attrs: AttrBuilderContext<MdSelectElement>? = null,
    onSelect: (T) -> Unit = {},
    itemToValue: (T) -> String = { it.toString() },
    itemToDisplayText: (T) -> String = itemToValue,
) {
    val valueToItem = selection.associateBy { itemToValue(it) }
    MdSelect("outlined", {
        selectedItem?.let {
            value(itemToValue(it))
        }
        attrs?.invoke(this)
        onChange {
            val item = valueToItem[it] ?: return@onChange
            onSelect(item)
        }
    }) {
        selection.forEach {
            MdSelectOption(itemToValue(it), itemToDisplayText(it))
        }
    }
}

@Composable
private fun MdSelect(
    type: String,
    attrs: AttrBuilderContext<MdSelectElement>? = null,
    content: @Composable ElementScope<MdSelectElement>.() -> Unit,
) {
    TagElement<MdSelectElement>("md-$type-select", {
        attrs?.invoke(this)
    }, content)
}

@Composable
fun ElementScope<MdSelectElement>.MdSelectOption(
    value: String,
    displayText: String,
    supportingText: String? = null,
    iconStart: String? = null,
    iconEnd: String? = null,
    selected: Boolean = false,
    key: Any = value,
    attrs: AttrBuilderContext<MdSelectOptionElement>? = null,
) {
    MdSelectOption(
        value, displayText, attrs,
        supportingText = supportingText?.let { { Text(supportingText) } },
        iconStart = iconStart,
        iconEnd = iconEnd,
        selected = selected,
        key = key,
    )
}

@Composable
fun ElementScope<MdSelectElement>.MdSelectOption(
    value: String,
    displayText: String,
    attrs: AttrBuilderContext<MdSelectOptionElement>? = null,
    overline: ContentBuilder<HTMLElement>? = null,
    supportingText: ContentBuilder<HTMLElement>? = null,
    trailingSupportingText: ContentBuilder<HTMLElement>? = null,
    iconStart: String? = null,
    iconEnd: String? = null,
    selected: Boolean = false,
    key: Any = value,
) {
    key(key) {
        TagElement<MdSelectOptionElement>("md-select-option", {
            value(value)
            if (selected) selected()
            attrs?.invoke(this)
        }) {
            Slot("overline", overline)
            Slot("headline") { Text(displayText) }
            Slot("supporting-text", supportingText)
            Slot("trailing-supporting-text", trailingSupportingText)
            SlotIcon("start", iconStart)
            SlotIcon("end", iconEnd)
        }
    }
}

abstract external class MdSelectElement : HTMLElement
abstract external class MdSelectOptionElement : HTMLElement

/** Opens the menu synchronously with no animation. */
fun AttrsScope<MdSelectElement>.quick() = attr("quick", "")

/** Whether or not the select is required. */
fun AttrsScope<MdSelectElement>.required() = attr("required", "")

/** Disables the select. */
fun AttrsScope<MdSelectElement>.disabled() = attr("disabled", "")

/**
 * The error message that replaces supporting text when error is true. If errorText is an empty string, then the supporting text will continue to show.
 * This error message overrides the error message displayed by reportValidity().
 */
fun AttrsScope<MdSelectElement>.errorText(value: String) = attr("error-text", value)

/** The floating label for the field. */
fun AttrsScope<MdSelectElement>.label(value: String) = attr("label", value)

/** Conveys additional information below the select, such as how it should be used. */
fun AttrsScope<MdSelectElement>.supportingText(value: String) = attr("supporting-text", value)

/**
 * Gets or sets whether or not the select is in a visually invalid state.
 * This error state overrides the error state controlled by reportValidity().
 */
fun AttrsScope<MdSelectElement>.error() = attr("error", "")

/**
 * Whether or not the underlying md-menu should be position: fixed to display in a top-level manner, or position: absolute.
 * position:fixed is useful for cases where select is inside of another element with stacking context and hidden overflows such as md-dialog.
 */
fun AttrsScope<MdSelectElement>.menuPositioning(value: MdMenuPosition) = attr("menu-positioning", value.toString())

/** The max time between the keystrokes of the typeahead menu behavior before it clears the typeahead buffer. */
fun AttrsScope<MdSelectElement>.typeaheadDelay(value: Number) = attr("typeahead-delay", value.toString())

/** Whether or not the text field has a leading icon. Used for SSR. */
fun AttrsScope<MdSelectElement>.hasLeadingIcon() = attr("has-leading-icon", "")

/** Text to display in the field. Only set for SSR. */
fun AttrsScope<MdSelectElement>.displayText(value: String) = attr("display-text", value)

fun AttrsScope<MdSelectElement>.value(value: String) = prop("value", value)
fun AttrsScope<MdSelectElement>.selectedIndex(value: Number) = attr("selected-index", value.toString())
fun AttrsScope<MdSelectElement>.name(value: String) = attr("name", value)
fun AttrsScope<MdSelectElement>.form(value: String) = attr("form", value)

fun AttrsScope<MdSelectElement>.onChange(listener: (String) -> Unit) = addEventListener("change", { value }, listener)
fun AttrsScope<MdSelectElement>.onClosed(listener: () -> Unit) = addEventListener("closed", listener)

/** Disables the item and makes it non-selectable and non-interactive. */
fun AttrsScope<MdSelectOptionElement>.disabled() = attr("disabled", "")

/** Sets the item in the selected visual state when a submenu is opened. */
fun AttrsScope<MdSelectOptionElement>.selected() = attr("selected", "")

/** Form value of the option. */
fun AttrsScope<MdSelectOptionElement>.value(value: String) = attr("value", value)
fun AttrsScope<MdSelectOptionElement>.displayText(value: String) = attr("display-text", value)

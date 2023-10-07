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
import androidx.compose.web.events.SyntheticEvent
import io.github.mee1080.umasim.web.components.lib.getTargetValue
import io.github.mee1080.umasim.web.components.lib.prop
import io.github.mee1080.umasim.web.components.lib.require
import io.github.mee1080.umasim.web.components.lib.slot
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.EventTarget

private val initializer = object {
    init {
        require("@material/web/textfield/filled-text-field.js")
        require("@material/web/textfield/outlined-text-field.js")
    }
}
//
//enum class MdTextFieldType(val attrValue: String) {
//    Email("email"),
//    Number("number"),
//    Password("password"),
//    Search("search"),
//    Tel("tel"),
//    Text("text"),
//    Url("url"),
//    TextArea("textarea"),
//
////    Color("color"),
////    Date("date"),
////    DateTimeLocal("datetime-local"),
////    File("file"),
////    Month("month"),
////    Time("time"),
////    Week("week"),
//}

@Composable
fun MdFilledNumberTextField(
    value: Number?,
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    attrs: (MdTextFieldAttrsScope<Number?>.() -> Unit)? = null,
) = MdTextFieldImpl("filled", InputTypeNumber, value?.toString() ?: "", leadingIcon, trailingIcon, attrs)

@Composable
fun MdFilledIntTextField(
    value: Int?,
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    attrs: (MdTextFieldAttrsScope<Int?>.() -> Unit)? = null,
) = MdTextFieldImpl("filled", InputTypeInt, value?.toString() ?: "", leadingIcon, trailingIcon, attrs)

@Composable
fun MdFilledTextField(
    value: String,
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    attrs: (MdTextFieldAttrsScope<String>.() -> Unit)? = null,
) = MdTextFieldImpl("filled", MdInputType.Text, value, leadingIcon, trailingIcon, attrs)

@Composable
fun MdFilledTextField(
    type: MdInputType<String>,
    value: String,
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    attrs: (MdTextFieldAttrsScope<String>.() -> Unit)? = null,
) = MdTextFieldImpl("filled", type, value, leadingIcon, trailingIcon, attrs)

@Composable
fun MdOutlinedNumberTextField(
    value: Number?,
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    attrs: (MdTextFieldAttrsScope<Number?>.() -> Unit)? = null,
) = MdTextFieldImpl("outlined", InputTypeNumber, value?.toString() ?: "", leadingIcon, trailingIcon, attrs)

@Composable
fun MdOutlinedIntTextField(
    value: Int?,
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    attrs: (MdTextFieldAttrsScope<Int?>.() -> Unit)? = null,
) = MdTextFieldImpl("outlined", InputTypeInt, value?.toString() ?: "", leadingIcon, trailingIcon, attrs)

@Composable
fun MdOutlinedTextField(
    value: String,
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    attrs: (MdTextFieldAttrsScope<String>.() -> Unit)? = null,
) = MdTextFieldImpl("outlined", MdInputType.Text, value, leadingIcon, trailingIcon, attrs)

@Composable
fun MdOutlinedTextField(
    type: MdInputType<String>,
    value: String,
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    attrs: (MdTextFieldAttrsScope<String>.() -> Unit)? = null,
) = MdTextFieldImpl("outlined", type, value, leadingIcon, trailingIcon, attrs)

@Composable
private fun <T> MdTextFieldImpl(
    style: String,
    type: MdInputType<T>,
    value: String,
    leadingIcon: String? = null,
    trailingIcon: String? = null,
    attrs: (MdTextFieldAttrsScope<T>.() -> Unit)?,
) {
    TagElement<MdTextFieldElement>("md-$style-text-field", {
        val scope = MdTextFieldAttrsScope(type, this)
        attrs?.invoke(scope)
        value(value)
    }) {
        if (leadingIcon != null) {
            MdIcon(leadingIcon) { slot("leading-icon") }
        }
        if (trailingIcon != null) {
            MdIcon(trailingIcon) { slot("trailing-icon") }
        }
    }
}

sealed interface MdInputType<ValueType> {

    object Email : MdInputTypeString("email")
    object Password : MdInputTypeString("password")
    object Search : MdInputTypeString("search")
    object Tel : MdInputTypeString("tel")
    object Text : MdInputTypeString("text")
    object Url : MdInputTypeString("url")
    object TextArea : MdInputTypeString("textarea")

    object Color : MdInputTypeString("color")
    object Date : MdInputTypeString("date")
    object DateTimeLocal : MdInputTypeString("datetime-local")
    object File : MdInputTypeString("file")
    object Month : MdInputTypeString("month")
    object Time : MdInputTypeString("time")
    object Week : MdInputTypeString("week")

    val typeStr: String
    fun inputValue(event: SyntheticEvent<EventTarget>): ValueType
}

private data object InputTypeNumber : MdInputType<Number?> {
    override val typeStr = "number"
    override fun inputValue(event: SyntheticEvent<EventTarget>): Number? {
        return event.getTargetValue<String> { value }.toDoubleOrNull()
    }
}

private data object InputTypeInt : MdInputType<Int?> {
    override val typeStr = "number"
    override fun inputValue(event: SyntheticEvent<EventTarget>): Int? {
        return event.getTargetValue<String> { value }.toIntOrNull()
    }
}

open class MdInputTypeString(override val typeStr: String) : MdInputType<String> {
    override fun inputValue(event: SyntheticEvent<EventTarget>): String {
        return event.getTargetValue { value }
    }
}

//private class InputTypeWrapped<ValueType>(private val base: InputType<ValueType>) : MdInputType<ValueType> {
//    override val type get() = MdTextFieldType.valueOf(base::class.simpleName!!)
//    override fun inputValue(event: SyntheticEvent<EventTarget>) = base.inputValue(event.nativeEvent)
//}
//
//private fun InputType<String>.hack() = InputTypeWrapped(this)

abstract external class MdTextFieldElement : HTMLElement

class MdTextFieldAttrsScope<ValueType>(
    private val inputType: MdInputType<ValueType>,
    attrsScope: AttrsScope<MdTextFieldElement>,
) : AttrsScope<MdTextFieldElement> by attrsScope {

    init {
        type(inputType)
    }

    fun onInput(
        listener: (ValueType) -> Unit,
    ) {
        addEventListener("input") {
            listener(inputType.inputValue(it))
        }
    }
}

fun AttrsScope<MdTextFieldElement>.disabled() = attr("disabled", "")

/** Gets or sets whether or not the text field is in a visually invalid state. */
fun AttrsScope<MdTextFieldElement>.error() = attr("error", "")

/** The error message that replaces supporting text when error is true. If errorText is an empty string, then the supporting text will continue to show. */
fun AttrsScope<MdTextFieldElement>.errorText(value: String) = attr("error-text", value)

fun AttrsScope<MdTextFieldElement>.label(value: String) = attr("label", value)

fun AttrsScope<MdTextFieldElement>.required() = attr("required", "")

/** The current value of the text field. It is always a string. */
fun AttrsScope<MdTextFieldElement>.value(value: String) = prop("value", value)

/** An optional prefix to display before the input value. */
fun AttrsScope<MdTextFieldElement>.prefixText(value: String) = attr("prefix-text", value)

/** An optional suffix to display after the input value. */
fun AttrsScope<MdTextFieldElement>.suffixText(value: String) = attr("suffix-text", value)

/** Whether or not the text field has a leading icon. Used for SSR. */
fun AttrsScope<MdTextFieldElement>.hasLeadingIcon() = attr("has-leading-icon", "")

/** Whether or not the text field has a trailing icon. Used for SSR. */
fun AttrsScope<MdTextFieldElement>.hasTrailingIcon() = attr("has-trailing-icon", "")

/** Conveys additional information below the text field, such as how it should be used. */
fun AttrsScope<MdTextFieldElement>.supportingText(value: String) = attr("supporting-text", value)

/** Override the input text CSS direction. Useful for RTL languages that use LTR notation for fractions. */
fun AttrsScope<MdTextFieldElement>.textDirection(value: String) = attr("text-direction", value)

/** The number of rows to display for a type="textarea" text field. Defaults to 2. */
fun AttrsScope<MdTextFieldElement>.rows(value: Number) = attr("rows", value.toString())

//fun AttrsScope<MdTextFieldElement>.inputMode(value: InputMode) = attr("inputmode", value.str)

/** Defines the greatest value in the range of permitted values. */
fun AttrsScope<MdTextFieldElement>.max(value: String) = attr("max", value)

/** The maximum number of characters a user can enter into the text field. Set to -1 for none. */
fun AttrsScope<MdTextFieldElement>.maxLength(value: Number) = attr("maxlength", value.toString())

/** Defines the most negative value in the range of permitted values. */
fun AttrsScope<MdTextFieldElement>.min(value: String) = attr("min", value)

/** The minimum number of characters a user can enter into the text field. Set to -1 for none. */
fun AttrsScope<MdTextFieldElement>.minLength(value: Number) = attr("minlength", value.toString())

/** A regular expression that the text field's value must match to pass constraint validation. */
fun AttrsScope<MdTextFieldElement>.pattern(value: String) = attr("pattern", value)

fun AttrsScope<MdTextFieldElement>.placeholder(value: String) = attr("placeholder", value)

/** Indicates whether or not a user should be able to edit the text field's value. */
fun AttrsScope<MdTextFieldElement>.readOnly() = attr("readonly", "")

/** Returns or sets the element's step attribute, which works with min and max to limit the increments at which a numeric or date-time value can be set. */
fun AttrsScope<MdTextFieldElement>.step(value: String) = attr("step", value)

/** The <input> type to use, defaults to "text". The type greatly changes how the text field behaves. */
private fun AttrsScope<MdTextFieldElement>.type(value: MdInputType<*>) = attr("type", value.typeStr)

/** Describes what, if any, type of autocomplete functionality the input should provide. */
fun AttrsScope<MdTextFieldElement>.autocomplete(value: String) = attr("autocomplete", value)

fun AttrsScope<MdTextFieldElement>.name(value: String) = attr("name", value)

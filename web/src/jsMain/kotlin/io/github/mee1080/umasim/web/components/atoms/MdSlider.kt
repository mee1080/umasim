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
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLDivElement

private val initializer = object {
    init {
        require("@material/web/slider/slider.js")
    }
}

@Composable
fun MdSlider(
    value: Number,
    min: Number,
    max: Number,
    attrs: AttrBuilderContext<MdSingleSliderElement>? = null,
) {
    MdSlider<MdSingleSliderElement> {
        value(value)
        min(min)
        max(max)
        attrs?.invoke(this)
    }
}

@Composable
fun MdSlider(
    valueStart: Number,
    valueEnd: Number,
    min: Number,
    max: Number,
    attrs: AttrBuilderContext<MdRangeSliderElement>? = null,
) {
    MdSlider<MdRangeSliderElement> {
        valueStart(valueStart)
        valueEnd(valueEnd)
        min(min)
        max(max)
        range()
        attrs?.invoke(this)
    }
}

@Composable
private fun <T : MdSliderElement> MdSlider(
    attrs: AttrBuilderContext<T>? = null,
) {
    TagElement<T>("md-slider", {
        attrs?.invoke(this)
    }, null)
}

abstract external class MdSliderElement : HTMLDivElement
abstract external class MdSingleSliderElement : MdSliderElement
abstract external class MdRangeSliderElement : MdSliderElement

/** Whether or not the slider is disabled. */
fun AttrsScope<MdSliderElement>.disabled() = attr("disabled", "")

/** The slider minimum value */
fun AttrsScope<MdSliderElement>.min(value: Number) = attr("min", value.toString())

/** The slider maximum value */
fun AttrsScope<MdSliderElement>.max(value: Number) = attr("max", value.toString())

/** The step between values. */
fun AttrsScope<MdSliderElement>.step(value: Number) = attr("step", value.toString())

/** Whether or not to show tick marks. */
fun AttrsScope<MdSliderElement>.ticks() = attr("ticks", "")

/** Whether or not to show a value label when activated. */
fun AttrsScope<MdSliderElement>.labeled() = attr("labeled", "")

fun AttrsScope<MdSliderElement>.form(value: String) = attr("", value)

/** The slider value displayed when range is false. */
fun AttrsScope<MdSingleSliderElement>.value(value: Number) = attr("value", value.toString())

/** An optional label for the slider's value displayed when range is false; if not set, the label is the value itself. */
fun AttrsScope<MdSingleSliderElement>.valueLabel(value: String) = attr("value-label", value)

fun AttrsScope<MdSingleSliderElement>.name(value: String) = attr("", value)

fun AttrsScope<MdSingleSliderElement>.onInput(listener: (Number) -> Unit) =
    addEventListener("input", { value }, listener)

fun AttrsScope<MdSingleSliderElement>.onChange(listener: (Number) -> Unit) =
    addEventListener("change", { value }, listener)

/** The slider start value displayed when range is true. */
fun AttrsScope<MdRangeSliderElement>.valueStart(value: Number) = attr("value-start", value.toString())

/** The slider end value displayed when range is true. */
fun AttrsScope<MdRangeSliderElement>.valueEnd(value: Number) = attr("value-end", value.toString())

/** An optional label for the slider's start value displayed when range is true; if not set, the label is the valueStart itself. */
fun AttrsScope<MdRangeSliderElement>.valueLabelStart(value: String) = attr("value-label-start", value)

/** An optional label for the slider's end value displayed when range is true; if not set, the label is the valueEnd itself. */
fun AttrsScope<MdRangeSliderElement>.valueLabelEnd(value: String) = attr("value-label-end", value)

/** Aria label for the slider's start handle displayed when range is true. */
fun AttrsScope<MdRangeSliderElement>.ariaLabelStart(value: String) = attr("aria-label-start", value)

/** Aria value text for the slider's start value displayed when range is true. */
fun AttrsScope<MdRangeSliderElement>.ariaValueTextStart(value: String) = attr("aria-valuetext-start", value)

/** Aria label for the slider's end handle displayed when range is true. */
fun AttrsScope<MdRangeSliderElement>.ariaLabelEnd(value: String) = attr("aria-label-end", value)

/** Aria value text for the slider's end value displayed when range is true. */
fun AttrsScope<MdRangeSliderElement>.ariaValueTextEnd(value: String) = attr("aria-valuetext-end", value)

/** Whether or not to show a value range. When false, the slider displays a slideable handle for the value property; when true, it displays slideable handles for the valueStart and valueEnd properties. */
fun AttrsScope<MdRangeSliderElement>.range() = attr("range", "")

fun AttrsScope<MdRangeSliderElement>.nameStart(value: String) = attr("", value)
fun AttrsScope<MdRangeSliderElement>.nameEnd(value: String) = attr("", value)

fun AttrsScope<MdRangeSliderElement>.onInput(listener: (start: Number, end: Number) -> Unit) {
    addEventListener("input", { Pair(valueStart, valueEnd) }, listener)
}

fun AttrsScope<MdRangeSliderElement>.onChange(listener: (start: Number, end: Number) -> Unit) {
    addEventListener("change", { Pair(valueStart, valueEnd) }, listener)
}
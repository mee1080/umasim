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
import io.github.mee1080.umasim.web.components.lib.require
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        require("@material/web/progress/linear-progress.js")
        require("@material/web/progress/circular-progress.js")
    }
}

@Composable
fun MdLinearProgress(
    value: Number,
    max: Number,
    buffer: Number? = null,
    attrs: AttrBuilderContext<MdLinearProgressElement>? = null,
) {
    MdProgress<MdLinearProgressElement>("linear") {
        value(value)
        max(max)
        buffer?.let { buffer(it) }
        attrs?.invoke(this)
    }
}

@Composable
fun MdLinearProgress(
    indeterminate: Boolean = true,
    attrs: AttrBuilderContext<MdLinearProgressElement>? = null,
) {
    MdProgress<MdLinearProgressElement>("linear") {
        if (indeterminate) indeterminate()
        attrs?.invoke(this)
    }
}

@Composable
fun MdCircularProgress(
    value: Number,
    max: Number,
    attrs: AttrBuilderContext<MdCircularProgressElement>? = null,
) {
    MdProgress<MdCircularProgressElement>("circular") {
        value(value)
        max(max)
        attrs?.invoke(this)
    }
}

@Composable
fun MdCircularProgress(
    indeterminate: Boolean = true,
    attrs: AttrBuilderContext<MdCircularProgressElement>? = null,
) {
    MdProgress<MdCircularProgressElement>("circular") {
        if (indeterminate) indeterminate()
        attrs?.invoke(this)
    }
}

@Composable
private fun <T : MdProgressElement> MdProgress(
    type: String,
    attrs: AttrBuilderContext<T>? = null,
) {
    TagElement<T>("md-$type-progress", {
        attrs?.invoke(this)
    }, null)
}

abstract external class MdProgressElement : HTMLElement
abstract external class MdLinearProgressElement : MdProgressElement
abstract external class MdCircularProgressElement : MdProgressElement

/** Progress to display, a fraction between 0 and max. */
fun AttrsScope<MdProgressElement>.value(value: Number) = attr("value", value.toString())

/** Maximum progress to display, defaults to 1. */
fun AttrsScope<MdProgressElement>.max(value: Number) = attr("max", value.toString())

/** Whether or not to display indeterminate progress, which gives no indication to how long an activity will take. */
fun AttrsScope<MdProgressElement>.indeterminate() = attr("indeterminate", "")

/** Whether or not to render indeterminate mode using 4 colors instead of one. */
fun AttrsScope<MdProgressElement>.fourColor() = attr("four-color", "")

/** Buffer amount to display, a fraction between 0 and 1. */
fun AttrsScope<MdLinearProgressElement>.buffer(value: Number) = attr("buffer", value.toString())

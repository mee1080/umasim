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
package io.github.mee1080.umasim.web.components.lib

import androidx.compose.web.events.SyntheticEvent
import org.jetbrains.compose.web.attributes.AttrsScope
import org.w3c.dom.events.EventTarget

internal inline fun AttrsScope<*>.addEventListener(
    eventName: String,
    crossinline listener: () -> Unit,
) {
    addEventListener(eventName) { _ -> listener() }
}

internal inline fun <T> AttrsScope<*>.addEventListener(
    eventName: String,
    crossinline targetValue: dynamic.() -> Any?,
    crossinline listener: (T) -> Unit,
) {
    addEventListener(eventName) {
        listener(it.getTargetValue(targetValue))
    }
}

internal inline fun <T, U> AttrsScope<*>.addEventListener(
    eventName: String,
    crossinline targetValue: dynamic.() -> Pair<Any?, Any?>,
    crossinline listener: (T, U) -> Unit,
) {
    addEventListener<Pair<T, U>>(eventName, targetValue) {
        listener(it.first, it.second)
    }
}

inline fun <T> SyntheticEvent<out EventTarget>.getTargetValue(value: dynamic.() -> Any?): T {
    return value.invoke(target.asDynamic()).unsafeCast<T>()
}

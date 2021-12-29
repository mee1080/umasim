/*
 * Copyright 2021 mee1080
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
package io.github.mee1080.umasim.web

import androidx.compose.web.events.SyntheticEvent
import org.jetbrains.compose.web.attributes.EventsListenerBuilder
import org.w3c.dom.Element
import org.w3c.dom.events.EventTarget
import org.w3c.dom.get

private val touchInfo = mutableMapOf<Int, Pair<Int, Int>>()

private fun distance(x1: Int, y1: Int, x2: Int, y2: Int) = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)

fun EventsListenerBuilder.onClickOrTouch(listener: (SyntheticEvent<EventTarget>) -> Unit) {
    onClick {
        listener(it)
    }
    onTouchStart {
        if ((it.target as? Element)?.hasAttribute("disabled") == true) return@onTouchStart
        val touches = it.changedTouches
        for (i in 0 until touches.length) {
            val touch = touches[i] ?: continue
            touchInfo[touch.identifier] = touch.pageX to touch.pageY
        }
    }
    onTouchEnd {
        it.preventDefault()
        if ((it.target as? Element)?.hasAttribute("disabled") == true) return@onTouchEnd
        val touches = it.changedTouches
        var clicked = false
        for (i in 0 until touches.length) {
            val touch = touches[i] ?: continue
            val start = touchInfo.remove(touch.identifier) ?: continue
            clicked = clicked || distance(start.first, start.second, touch.pageX, touch.pageY) < 100
        }
        if (clicked) {
            listener(it)
        }
    }
}
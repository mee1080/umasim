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

import org.jetbrains.compose.web.css.*

fun StyleScope.gridTemplateColumns(repeat: GridRepeat) {
    gridTemplateColumns(repeat.toString())
}

fun StyleScope.gridTemplateColumns(vararg values: CSSUnitValue) {
    gridTemplateColumns(values.joinToString(" "))
}

fun StyleScope.minMax(min: CSSUnitValue, max: CSSUnitValue) = MinMax(min, max)

data class MinMax<T : CSSUnit>(
    val min: CSSSizeValue<T>,
    val max: CSSUnitValue,
) : CSSSizeValue<T> by min {
    override fun toString() = "minmax($min, $max)"
}

class GridRepeat(
    private val count: String,
    private val values: Array<out CSSUnitValue>,
) {
    override fun toString() = "repeat($count, ${values.joinToString(" ")}"
}

interface GridRepeatStyle {
    companion object {
        inline val AutoFit get() = GridRepeatStyle("auto-fit")
        inline val AutoFill get() = GridRepeatStyle("auto-fill")
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun GridRepeatStyle(value: String) = value.unsafeCast<GridRepeatStyle>()

fun StyleScope.repeatGrid(count: Int, vararg values: CSSUnitValue) = GridRepeat(count.toString(), values)

fun StyleScope.repeatGrid(count: GridRepeatStyle, vararg values: CSSUnitValue) = GridRepeat(count.toString(), values)

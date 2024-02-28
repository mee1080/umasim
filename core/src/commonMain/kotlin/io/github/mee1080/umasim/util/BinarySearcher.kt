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
package io.github.mee1080.umasim.util

object BinarySearcher {
    inline fun run(
        startMin: Double,
        startMax: Double,
        limit: Double,
        target: Double,
        calc: (Double) -> Double
    ): Double {
        if (startMin >= startMax) throw IllegalArgumentException()
        val minResult = calc(startMin)
        val maxResult = calc(startMax)
        val direction = if (maxResult >= minResult) 1.0 else -1.0
        if (minResult * direction > target * direction) return startMin
        if (maxResult * direction < target * direction) return startMax
        var min = startMin
        var max = startMax
        var diff = max - min
        while (diff > limit) {
            val value = min + diff / 2.0
            val result = calc(value)
            if (result == target) return value
            if (result * direction > target * direction) {
                max = value
            } else {
                min = value
            }
            diff = max - min
        }
        return min + diff / 2.0
    }
}
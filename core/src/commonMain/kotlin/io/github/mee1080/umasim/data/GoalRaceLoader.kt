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
package io.github.mee1080.umasim.data

object GoalRaceLoader {

    @Suppress("UNUSED_CHANGED_VALUE")
    fun load(text: String): List<GoalRace> {
        val list = mutableListOf<GoalRace>()
        text.split("\n").forEach {
            val data = it.trim().split("\t")
            if (data.size >= 2) {
                var i = 0
                list.add(
                    GoalRace(
                        data[i++].toInt(),
                        data[i++].split(",").map { value -> value.toInt() },
                    )
                )
            }
        }
        return list
    }
}
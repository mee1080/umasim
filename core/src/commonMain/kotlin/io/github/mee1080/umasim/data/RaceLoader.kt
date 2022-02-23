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

object RaceLoader {

    fun load(text: String): List<List<RaceEntry>> {
        val list = mutableListOf<RaceEntry>()
        text.split("\n").forEach {
            val data = it.trim().split("\t")
            if (data.size >= 11) {
                list.addAll(toEntry(data))
            }
        }
        val grouped = list.groupBy { it.turn }
        return List(73) { grouped[it]?.sortedByDescending { entry -> entry.getFan } ?: emptyList() }
    }

    private fun toEntry(data: List<String>): List<RaceEntry> {
        val turnOffset = data[4].toInt() * 2 + data[5].toInt() - 2
        return (1..3).filter { data[it] == "1" }.map {
            RaceEntry(
                it * 24 - 24 + turnOffset,
                data[0],
                data[6].toInt(),
                data[7].toInt(),
                toRaceGrade(data[8].toInt()),
                toRaceDistance(data[9].toInt()),
                toRaceGround(data[10]),
            )
        }
    }
}
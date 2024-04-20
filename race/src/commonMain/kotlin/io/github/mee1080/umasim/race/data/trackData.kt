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
/*
 * This file was ported from uma-clock-emu by Romulus Urakagi Tsai(@urakagi)
 * https://github.com/urakagi/uma-clock-emu
 */
package io.github.mee1080.umasim.race.data

import io.github.mee1080.umasim.race.calc2.Track
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val trackData by lazy {
    Json.decodeFromString<Map<Int, RaceTrack>>(rawCourseData)
}

val recentEventTrackList by lazy {
    listOf(
        Triple("京都", "芝3200", CourseCondition.GOOD),
        Triple("京都", "芝3200", CourseCondition.YAYAOMO),
        Triple("京都", "芝3200", CourseCondition.OMO),
        Triple("京都", "芝3200", CourseCondition.BAD),

        Triple("東京", "芝2400", CourseCondition.OMO),
    ).mapNotNull { target ->
        val course = trackData.entries.firstOrNull { it.value.name == target.first } ?: return@mapNotNull null
        val track = course.value.courses.entries.firstOrNull { it.value.name.startsWith(target.second) }
            ?: return@mapNotNull null
        Track(course.key, track.key, target.third)
    }
}

@Serializable
data class RaceTrack(
    val name: String,
    val courses: Map<Int, TrackDetail>,
)

@Serializable
data class TrackDetail(
    val raceTrackId: Int,
    val name: String,
    val distance: Int,
    val distanceType: Int,
    val surface: Int,
    val turn: Int,
    val courseSetStatus: List<Int>,
    val laneMax: Int,
    val finishTimeMin: Double,
    val finishTimeMax: Double,
    val corners: List<Corner>,
    val straights: List<Straight>,
    val slopes: List<Slope>,
) {
    fun getSlope(position: Double): Double {
        for (slope in slopes) {
            if (position >= slope.start && position <= slope.end) {
                return 0.0001 * slope.slope
            }
        }
        return 0.0
    }

    val isBasisDistance = if (distance % 400 == 0) 1 else 0

    val distanceCategory = when (distanceType) {
        1 -> Distance.SHORT
        2 -> Distance.MILE
        3 -> Distance.MIDDLE
        4 -> Distance.LONG
        else -> throw RuntimeException("unknown distance type: $distanceType")
    }
}

@Serializable
data class Corner(
    val start: Double,
    val length: Double,
) {
    val end = start + length
}

@Serializable
data class Straight(
    val start: Double,
    val end: Double,
)

@Serializable
data class Slope(
    val start: Double,
    val length: Double,
    val slope: Double,
) {
    val end = start + length
}
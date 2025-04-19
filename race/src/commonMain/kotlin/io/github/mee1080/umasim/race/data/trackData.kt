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

private class RecentEventTrackEntry(
    val courseName: String,
    val distance: String,
    val condition: CourseCondition,
    val gateCount: Int,
)

val recentEventTrackList by lazy {
    listOf(
        RecentEventTrackEntry("京都", "芝3200m(外)", CourseCondition.GOOD, 9),

        RecentEventTrackEntry("大井", "ダート2000m", CourseCondition.GOOD, 12),
        RecentEventTrackEntry("大井", "ダート2000m", CourseCondition.YAYAOMO, 12),
        RecentEventTrackEntry("大井", "ダート2000m", CourseCondition.OMO, 12),
        RecentEventTrackEntry("大井", "ダート2000m", CourseCondition.BAD, 12),
    ).mapNotNull { target ->
        val course = trackData.entries.firstOrNull { it.value.name == target.courseName } ?: return@mapNotNull null
        val track = course.value.courses.entries.firstOrNull { it.value.name.startsWith(target.distance) }
            ?: return@mapNotNull null
        Track(course.key, track.key, target.condition, target.gateCount)
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

    // FIXME レース途中で変化するコースあり
    val maxLaneDistance = courseWidth * laneMax / 10000.0

    // FIXME 新潟1000以外に直線のみのコースが実装されたら要確認
    val moveLanePoint = corners.firstOrNull()?.start ?: 30.0
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
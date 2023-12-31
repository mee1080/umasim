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

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val trackData by lazy {
    Json.decodeFromString<Map<Int, RaceTrack>>(rawCourseData)
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
    val finishTimeMin: Float,
    val finishTimeMax: Float,
    val corners: List<Corner>,
    val straights: List<Straight>,
    val slopes: List<Slope>,
) {
    fun toPosition(distanceLeft: Int) = distance - distanceLeft

    fun getSlope(position: Float): Float {
        for (slope in slopes) {
            if (position >= slope.start && position <= slope.end) {
                return 0.0001f * slope.slope
            }
        }
        return 0f
    }
}

@Serializable
data class Corner(
    val start: Float,
    val length: Float,
) {
    val end = start + length
}

@Serializable
data class Straight(
    val start: Float,
    val end: Float,
)

@Serializable
data class Slope(
    val start: Float,
    val length: Float,
    val slope: Float,
) {
    val end = start + length
}
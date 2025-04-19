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
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val trackData by lazy {
    Json.decodeFromString<Map<Int, RaceTrack>>(rawCourseData)
}

private lateinit var recentEventTrackData: List<Pair<String, Track>>

val recentEventTrackList by lazy {
    recentEventTrackData.map { it.second }
}

suspend fun loadRecentEventTrackList() {
    val list = mutableListOf<Pair<String, Track>>()
    val text = HttpClient()
        .get("https://raw.githubusercontent.com/mee1080/umasim/refs/heads/main/data/event_track.txt")
        .bodyAsText()
    text.split("\n").forEach { line ->
        val data = line.trim().split(",")
        if (data.size >= 4) {
            try {
                val month = data[0].toInt()
                val type = if (data[1] == "L") "リーグオブヒーローズ" else "チャンピオンズミーティング"
                val label = "${month}月$type"
                val courseName = data[2]
                val distance = data[3]
                val conditions = if (data.size < 5) CourseCondition.entries else {
                    listOf(CourseCondition.valueOf(data[4]))
                }
                val gateCount = if (data[1] == "L") 12 else 9
                val course = trackData.entries.first { it.value.name == courseName }
                val track = course.value.courses.entries.first { it.value.name.startsWith(distance) }
                conditions.forEach {
                    list.add(label to Track(course.key, track.key, it, gateCount))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    recentEventTrackData = list
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
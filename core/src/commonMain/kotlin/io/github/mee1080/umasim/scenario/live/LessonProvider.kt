/*
 * Copyright 2022 mee1080
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
package io.github.mee1080.umasim.scenario.live

import io.github.mee1080.umasim.data.*

object LessonProvider {

    fun provide(
        period: LivePeriod,
        lessonCount: Int,
        learnedSongs: List<SongLesson>
    ): List<Lesson> {
        val song = isSong(period, lessonCount)
        val result = mutableListOf<Lesson>()
        if (song) {
            val songs = liveSongLesson[period]!! - learnedSongs.toSet()
            if (songs.size >= 3) {
                return songs.shuffled().subList(0, 3)
            }
            result.addAll(songs)
        }
        val categoryRate = liveTechniqueCategoryRate[period.lessonPeriod]!!
        val selections = liveTechniqueLesson[period.lessonPeriod]!!
        val categories = mutableSetOf<TechniqueLessonCategory>()
        for (position in result.size until 3) {
            val categorySelection = categoryRate[position].filter {
                !categories.contains(it.first)
            }
            val category = randomSelect(categorySelection)
            if (category.single) {
                categories += category
            }
            val lesson = randomSelect(selections[category]!!.list)
            result += lesson
        }
        return result
    }

    fun isSong(
        period: LivePeriod,
        lessonCount: Int,
    ): Boolean {
        return when (period) {
            LivePeriod.Junior -> {
                val count = lessonCount % 16
                count == 2 || count == 5 || count == 9 || count == 14
            }

            LivePeriod.Senior2 -> {
                val count = lessonCount % 15
                count == 2 || count == 5 || count == 8 || count == 13
            }

            LivePeriod.Finals -> false

            else -> {
                val count = lessonCount % 17
                count == 2 || count == 5 || count == 8 || count == 13
            }
        }
    }
}
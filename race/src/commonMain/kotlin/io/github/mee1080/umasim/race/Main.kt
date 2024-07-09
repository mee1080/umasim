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
package io.github.mee1080.umasim.race

import io.github.mee1080.umasim.race.data.trackData

fun main() {
//    skillData.forEach {
//        println("${it.implemented} : ${it.rarity} ${it.displayType} ${it.name}")
//    }
//    skillData2.forEach {
//        println(it)
//    }
//    testCalc()
//    testCalc2()
    trackData.forEach { (_, course) ->
        course.courses.forEach { (_, track) ->
            val half = track.distance / 2.0
            if (track.slopes.any { it.slope > 0.0 && it.start <= half && it.end >= half }) {
                println("${course.name} ${track.name}")
                track.slopes.forEach {
                    println("　${if (it.slope > 0.0) "上り" else "下り"} ${it.start} ～ ${it.end} ${if (it.slope > 0.0 && it.start <= half && it.end >= half) "★" else ""}")
                }
                println()
            }
        }
    }
}
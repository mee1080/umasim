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

import io.github.mee1080.umasim.race.data.Style
import io.github.mee1080.umasim.race.data.calcBaseWisdomSkillBuff
import io.github.mee1080.umasim.race.data.getWisdomSkillBuff
import io.github.mee1080.utility.roundToString

fun main() {
//    skillData.forEach {
//        println("${it.implemented} : ${it.rarity} ${it.displayType} ${it.name}")
//    }
//    skillData2.forEach {
//        println(it)
//    }
//    testCalc()
//    testCalc2()
//    trackData.forEach { (_, course) ->
//        course.courses.forEach { (_, track) ->
//            val start = track.distance * 5.0 / 12.0
//            val end = track.distance * 2.0 / 3.0
//            val targets = track.straights.filter { (it.start <= end && it.end >= start) }
//            if (targets.count() >= 2) {
//                println("${course.name} ${track.name}")
//                println("  中盤後半 $start ～ $end")
//                val lengthList = mutableListOf<Double>()
//                targets.forEach {
//                    val length = min(it.end, end) - max(it.start, start)
//                    lengthList += length
//                    println("　  ${it.start} ～ ${it.end} $length")
//                }
//                val diff = lengthList.max() / lengthList.min()
//                println("  diff $diff")
//                println()
//            }
//        }
//    }
    val styles = listOf(Style.NIGE, Style.SEN, Style.SASI, Style.OI)
    for (wisdom in 1201..2501 step 20) {
        val base = calcBaseWisdomSkillBuff(wisdom)
        println("$wisdom\t${wisdom - 1200}\t$base")
    }
    for (phase in 0..3) {
        println("Phase $phase")
        for (wisdom in 1200..2500 step 10) {
            val buffs = styles.map { getWisdomSkillBuff(wisdom, it)[phase]!! + 1.0 }
            println("$wisdom\t${buffs.joinToString("\t") { it.roundToString(3) }}")
        }
    }
}
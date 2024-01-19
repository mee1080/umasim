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

import io.github.mee1080.umasim.race.calc.RaceCalculator
import io.github.mee1080.umasim.race.calc.RaceSetting
import io.github.mee1080.umasim.race.calc.Track
import io.github.mee1080.umasim.race.data.getSkill
import io.github.mee1080.umasim.race.data2.skillData2

fun main() {
//    skillData.forEach {
//        println("${it.implemented} : ${it.rarity} ${it.displayType} ${it.name}")
//    }
    skillData2.forEach {
        println(it)
    }
}

private fun testRace() {
    val setting = RaceSetting(
        track = Track(10001, 10101),
        skillActivateAdjustment = 2,
        randomPosition = 3,
        hasSkills = listOf(
            getSkill("右回り◎"),
//            getSkill("弧線のプロフェッサー"),
            getSkill("好転一息"),
            getSkill("レッツ・アナボリック！"),
            getSkill("追込ためらい"),
        )
    )
    println(setting)
    println(setting.trackDetail)
    setting.hasSkills.forEach { println(it.name) }
    val calculator = RaceCalculator(setting)
    repeat(10) {
        val (result, state) = calculator.simulate()
        println("$result")
        state.simulation.invokedSkills.forEach { println(it.skill) }
    }
    val (result, state) = calculator.simulate()
    println(result)
    state.simulation.frames.forEach {
        println(it)
    }
}
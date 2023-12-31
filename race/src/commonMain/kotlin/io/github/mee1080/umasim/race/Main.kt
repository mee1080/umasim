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

fun main() {
    val setting = RaceSetting(
        track = Track(10001, 10101),
//        skillActivateAdjustment = 2,
    )
    println(setting)
    println(setting.trackDetail)
    val calculator = RaceCalculator(setting)
    repeat(100) {
        val (result, frames) = calculator.simulate()
        println("$result")
    }
//    val (result, frames) = calculator.simulate()
//    frames.forEach {
//        println(it)
//    }
}
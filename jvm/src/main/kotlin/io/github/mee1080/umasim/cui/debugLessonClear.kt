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
package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.data.LessonPeriod
import io.github.mee1080.umasim.data.Performance
import io.github.mee1080.umasim.data.liveTechniqueCategoryRate
import io.github.mee1080.umasim.data.liveTechniqueLesson
import io.github.mee1080.umasim.simulation2.LessonClearCalculator

fun testLessonClear() {
    val calculator = LessonClearCalculator(
        liveTechniqueCategoryRate[LessonPeriod.Classic]!!,
        liveTechniqueLesson[LessonPeriod.Classic]!!,
    ) { _, restPerformance ->
        restPerformance.countOver(16) * 1000 + restPerformance.totalValue
    }
    val performance = Performance(78, 28, 17, 13, 11)
    val step = 4

    val time = System.currentTimeMillis()
    val result = calculator.calc(performance, step, 0.0001)
    println("calc: ${System.currentTimeMillis() - time} ms")
    println(result.contentToString())
}
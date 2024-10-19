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
package io.github.mee1080.umasim.web.vm

import io.github.mee1080.umasim.scenario.live.*
import io.github.mee1080.umasim.web.state.LessonState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonViewModel(private val root: ViewModel) {

    fun update(action: LessonState.() -> LessonState) {
        root.update { copy(lessonState = lessonState.action()) }
    }

    suspend fun suspendUpdate(action: LessonState.() -> LessonState) {
        withContext(Dispatchers.Main) {
            update(action)
            delay(100L)
        }
    }

    fun calculate() {
        root.scope.launch(Dispatchers.Default) {
            val state = root.state.lessonState
            val parameters = state.convertParameters()
            if (parameters == null) {
                suspendUpdate { copy(message = "入力が不正です", result = emptyList()) }
            } else {
                suspendUpdate { copy(message = "計算中...", result = emptyList()) }
                val period = state.period
                val evaluate: (TechniqueLesson, Performance) -> Int = when (period) {
                    LessonPeriod.Junior -> {
                        { _, restPerformance ->
                            restPerformance.countOver(10) * 1000 + restPerformance.totalValue
                        }
                    }

                    LessonPeriod.Classic -> {
                        { _, restPerformance ->
                            restPerformance.countOver(16) * 1000 + restPerformance.totalValue
                        }
                    }

                    LessonPeriod.Senior -> {
                        { _, restPerformance ->
                            restPerformance.countOver(24) * 1000 + restPerformance.totalValue
                        }
                    }
                }
                val result = LessonClearCalculator(
                    liveTechniqueCategoryRate[period]!!,
                    liveTechniqueLesson[period]!!,
                    evaluate,
                ).calc(parameters.first, state.stepCount, parameters.second)
                suspendUpdate { copy(message = "", result = result.toList()) }
            }
        }
    }
}
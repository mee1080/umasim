package io.github.mee1080.umasim.web.vm

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.LessonClearCalculator
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
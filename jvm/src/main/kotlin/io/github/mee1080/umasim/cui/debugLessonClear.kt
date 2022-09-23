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

    var time = System.currentTimeMillis()
    println(calculator.calc(performance, step, 0.0001))
    println("calc: ${System.currentTimeMillis() - time} ms")

//    time = System.currentTimeMillis()
//    println(calculator.calc(performance, step))
//    println("calc: ${System.currentTimeMillis() - time} ms")
//
//    time = System.currentTimeMillis()
//    println(calculator.calc(performance, step))
//    println("calc: ${System.currentTimeMillis() - time} ms")
}
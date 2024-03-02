package io.github.mee1080.umasim.cui


import io.github.mee1080.umasim.simulation2.ColorFactor
import io.github.mee1080.umasim.simulation2.UafAthleticsLevelCalculator
import kotlin.system.measureTimeMillis

fun main() {
    val factors = listOf(
        ColorFactor(2, false, true, 0.306, 0.154),
        ColorFactor(4, false, true, 0.231, 0.171),
        ColorFactor(0, false, true, 0.328, 0.149),
        ColorFactor(0, false, true, 0.268, 0.162),
        ColorFactor(3, false, true, 0.286, 0.159),
        ColorFactor(5, true, false, 0.167, 0.167),
//        ColorFactor(5, false, false, 0.18, 0.18),
//        ColorFactor(5, false, false, 0.18, 0.18),
    )
    val time = measureTimeMillis {
        val list = UafAthleticsLevelCalculator.calc(factors, false)
        var expected = 0.0
        list.forEachIndexed { index, rate ->
            if (rate > 0.0) {
                println("$index: $rate")
                expected += index * rate
            }
        }
        println(expected)
        println(list.sum())
    }
    println("time: ${time / 1000.0} s")
}

package io.github.mee1080.umasim.cui


import io.github.mee1080.umasim.simulation2.ColorFactor
import io.github.mee1080.umasim.simulation2.UafAthleticsLevelCalculator
import kotlin.system.measureTimeMillis

fun main() {
    val factors = listOf(
        ColorFactor(0, true, true, 0.3, 0.15),
        ColorFactor(1, true, true, 0.26, 0.16),
        ColorFactor(2, true, true, 0.18, 0.18),
        ColorFactor(3, true, true, 0.22, 0.17),
        ColorFactor(4, true, true, 0.26, 0.16),
        ColorFactor(5, false, false, 0.18, 0.18),
//        ColorFactor(5, false, false, 0.18, 0.18),
        ColorFactor(5, false, false, 0.18, 0.18),
    )
    val time = measureTimeMillis {
        val list = UafAthleticsLevelCalculator.calc(factors, true)
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

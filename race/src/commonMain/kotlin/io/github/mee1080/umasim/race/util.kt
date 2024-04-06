package io.github.mee1080.umasim.race

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

//inline fun <T> Collection<T>.sumOf(selector: (T) -> Float): Float {
//    var sum = 0f
//    for (element in this) {
//        sum += selector(element)
//    }
//    return sum
//}

fun Double.roundString(position: Int = 0): String {
    return if (isNaN()) "-" else if (position == 0) roundToInt().toString() else {
        val minus = if (this < 0) "-" else ""
        val factor = 10.0.pow(position).roundToInt()
        val intValue = (abs(this) * factor).roundToInt()
        return "$minus${intValue / factor}.${intValue % factor}"
    }
}

fun Double.roundPercentString(position: Int = 0) = (this * 100).roundString(position)

fun <T> List<T>.averageOf(
    selector: (T) -> Double,
) = map(selector).average()

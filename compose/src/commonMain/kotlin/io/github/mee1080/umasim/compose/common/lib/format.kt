package io.github.mee1080.umasim.compose.common.lib

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.toTimeString(): String {
    val minute = (this / 60.0).toInt()
    val second = (this - minute * 60.0).toInt()
    val milliSecond = ((this - minute * 60.0 - second) * 1000.0).toInt()
    return "$minute:${second.zeroPad(2)}.${milliSecond.zeroPad(3)}"
}

fun Int.zeroPad(length: Int): String {
    return toString().padStart(length, '0')
}

fun Double.roundString(position: Int = 0): String {
    return if (position == 0) roundToInt().toString() else {
        val minus = if (this < 0) "-" else ""
        val factor = 10.0.pow(position).roundToInt()
        val intValue = (abs(this) * factor).roundToInt()
        return "$minus${intValue / factor}.${intValue % factor}"
    }
}

fun Double.toPercentString(position: Int = 0): String {
    return (this * 100.0).roundString(position)
}
package io.github.mee1080.utility

import kotlin.math.*


fun Long.millisecondToTimeString(): String {
    if (this == Long.MAX_VALUE || this == Long.MIN_VALUE) return "-"
    val value = absoluteValue
    return buildString {
        if (this@millisecondToTimeString < 0L) append('-')
        val hour = value / 3600000L
        if (hour > 0L) {
            append(hour)
            append(':')
        }
        val minute = (value % 3600000L) / 60000L
        append(if (hour == 0L) minute else minute.zeroPad(2))
        append(':')
        val second = (value % 60000L) / 1000L
        append(second.zeroPad(2))
        append('.')
        val milliSecond = value % 1000L
        append(milliSecond.zeroPad(3))
    }
}

fun Float.secondToTimeString(): String {
    if (isNaN() || isInfinite()) return "-"
    return (this * 1000f).roundToLong().millisecondToTimeString()
}

fun Double.secondToTimeString(): String {
    if (isNaN() || isInfinite()) return "-"
    return (this * 1000.0).roundToLong().millisecondToTimeString()
}

fun Int.zeroPad(length: Int): String {
    return toString().padStart(length, '0')
}

fun Long.zeroPad(length: Int): String {
    return toString().padStart(length, '0')
}

fun Float.roundToString(position: Int = 0, unit: String = ""): String {
    return if (isNaN() || isInfinite()) {
        "-"
    } else if (position == 0) {
        "${roundToInt()}$unit"
    } else {
        val minus = if (this < 0) "-" else ""
        val factor = 10f.pow(position).roundToInt()
        val intValue = (abs(this) * factor).roundToInt()
        return "$minus${intValue / factor}.${(intValue % factor).zeroPad(position)}$unit"
    }
}

fun Double.roundToString(position: Int = 0, unit: String = ""): String {
    return if (isNaN() || isInfinite()) {
        "-"
    } else if (position == 0) {
        "${roundToInt()}$unit"
    } else {
        val minus = if (this < 0) "-" else ""
        val factor = 10.0.pow(position).roundToInt()
        val intValue = (abs(this) * factor).roundToInt()
        "$minus${intValue / factor}.${(intValue % factor).zeroPad(position)}$unit"
    }
}

fun Float.toPercentString(position: Int = 0, unit: String = "%"): String {
    return (this * 100f).roundToString(position, unit)
}

fun Double.toPercentString(position: Int = 0, unit: String = "%"): String {
    return (this * 100.0).roundToString(position, unit)
}

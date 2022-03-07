package io.github.mee1080.umasim.util

object BinarySearcher {
    inline fun run(
        startMin: Double,
        startMax: Double,
        limit: Double,
        target: Double,
        calc: (Double) -> Double
    ): Double {
        var min = startMin
        var max = startMax
        var diff = max - min
        while (diff > limit) {
            val value = min + diff / 2.0
            val result = calc(value)
            println("$min,$max - $diff : $value -> $result / $target")
            if (result == target) return value
            if (result > target) {
                max = value
            } else {
                min = value
            }
            diff = max - min
        }
        return min + diff / 2.0
    }
}
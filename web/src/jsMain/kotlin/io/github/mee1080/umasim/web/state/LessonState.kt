package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.scenario.live.LessonPeriod
import io.github.mee1080.umasim.scenario.live.Performance

data class LessonState(
    val periodIndex: Int = 0,
    val dance: String = "10",
    val passion: String = "10",
    val vocal: String = "10",
    val visual: String = "10",
    val mental: String = "10",
    val stepCount: Int = 2,
    val threshold: String = "0.001",

    val message: String? = null,
    val result: List<Double> = emptyList(),

    val periodList: List<Pair<Int, String>> = LessonPeriod.entries.map { it.ordinal to it.displayName }
) {
    val period get() = LessonPeriod.entries.getOrElse(periodIndex) { LessonPeriod.Junior }

    fun convertParameters() = kotlin.runCatching {
        Pair(
            Performance(
                dance.toInt(),
                passion.toInt(),
                vocal.toInt(),
                visual.toInt(),
                mental.toInt(),
            ),
            threshold.toDouble(),
        )
    }.getOrNull()
}
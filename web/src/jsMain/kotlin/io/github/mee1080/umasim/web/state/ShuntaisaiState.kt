package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.shuntaisai.*

data class ShuntaisaiState(
    val turn: Int = 1,
    val levels: Map<StatusType, Int> = trainingType.associateWith { 1 },
    val practice: Set<StatusType> = emptySet(),
    val teachings: Map<StatusType, ShuntaisaiTeaching> = emptyMap(),
    val transformedFrom: Map<StatusType, ShuntaisaiTeaching> = emptyMap(),
    val presentationResult: ShuntaisaiPresentationResult = ShuntaisaiPresentationResult.SUCCESS,
    val azumaSsr: Boolean = true,
) {
    val period get() = (turn - 1) / 24

    val maxLevel get() = if (azumaSsr) shuntaisaiAzumaMaxLevel else shuntaisaiDefaultMaxLevel

    fun toShuntaisaiStatus(supportTypeCount: Int): ShuntaisaiStatus {
        val presentationBonus = if (period == 0) ShuntaisaiPresentationBonus.Initial else {
            shuntaisaiPresentationBonus[minOf(period, 3) - 1][presentationResult] ?: ShuntaisaiPresentationBonus.Initial
        }
        return ShuntaisaiStatus(
            turn = turn,
            maxLevel = maxLevel,
            gauges = trainingType.associateWith { if (practice.contains(it)) shuntaisaiMaxGauge else 0 },
            levels = levels.mapValues { it.value.coerceAtMost(maxLevel) },
            teachings = teachings.mapValues { (type, teaching) ->
                ShuntaisaiTeachingSlot(teaching, transformedFrom[type]?.takeIf { it.period < teaching.period })
            },
            presentationBonus = presentationBonus,
            supportTypeCount = supportTypeCount,
        )
    }
}

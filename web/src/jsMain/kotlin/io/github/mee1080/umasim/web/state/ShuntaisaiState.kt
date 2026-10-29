package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.shuntaisai.*

data class ShuntaisaiState(
    val turn: Int = 1,
    val levels: Map<StatusType, Int> = trainingType.associateWith { 1 },
    val teachings: Map<StatusType, ShuntaisaiTeaching> = emptyMap(),
    val transformedFrom: Map<StatusType, ShuntaisaiTeaching> = emptyMap(),
    val presentationResult: ShuntaisaiPresentationResult = ShuntaisaiPresentationResult.GREAT_SUCCESS,
) {
    val period get() = (turn - 1) / 24

    val isFinals get() = turn >= shuntaisaiFinalsStartTurn

    val maxLevel get() = shuntaisaiAzumaMaxLevel

    fun toShuntaisaiStatus(supportTypeCount: Int, practice: StatusType? = null): ShuntaisaiStatus {
        val presentationBonus = if (period == 0) ShuntaisaiPresentationBonus.Initial else {
            shuntaisaiPresentationBonus[minOf(period, 3) - 1][presentationResult] ?: ShuntaisaiPresentationBonus.Initial
        }
        return ShuntaisaiStatus(
            turn = turn,
            maxLevel = maxLevel,
            gauges = trainingType.associateWith { if (it == practice) shuntaisaiMaxGauge else 0 },
            levels = levels.mapValues { it.value.coerceAtMost(maxLevel) },
            teachings = teachings.mapValues { (type, teaching) ->
                ShuntaisaiTeachingSlot(teaching, transformedFrom[type]?.takeIf { it.period < teaching.period })
            },
            presentationBonus = presentationBonus,
            supportTypeCount = supportTypeCount,
        )
    }
}

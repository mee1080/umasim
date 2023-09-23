package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Status

class SimulationHistoryItem(
    val action: Action,
    val actionResult: Status,
    val beforeActionState: SimulationState,
    val afterTurnState: SimulationState,
    val selections: List<Pair<List<Action>, SelectedAction>>,
) {
    @Deprecated("use action", ReplaceWith("action"))
    val first
        get() = action

    @Deprecated("use actionResult", ReplaceWith("actionResult"))
    val second
        get() = actionResult

    @Deprecated("use beforeActionState", ReplaceWith("beforeActionState"))
    val third
        get() = beforeActionState

    val useItem
        get() = selections.flatMap {
            (it.second.scenarioAction as? SelectedClimaxAction)?.useItem ?: emptyList()
        }
}
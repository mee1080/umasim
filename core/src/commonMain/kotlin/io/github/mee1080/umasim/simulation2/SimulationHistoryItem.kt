package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Status

class SimulationHistoryItem(
    val action: Action,
    val status: Status,
    val state: SimulationState,
    val selections: List<Pair<List<Action>, SelectedAction>>,
) {
    @Deprecated("use action", ReplaceWith("action"))
    val first
        get() = action

    @Deprecated("use status", ReplaceWith("status"))
    val second
        get() = status

    @Deprecated("use state", ReplaceWith("state"))
    val third
        get() = state

    val useItem
        get() = selections.flatMap {
            (it.second.scenarioAction as? SelectedClimaxAction)?.useItem ?: emptyList()
        }
}
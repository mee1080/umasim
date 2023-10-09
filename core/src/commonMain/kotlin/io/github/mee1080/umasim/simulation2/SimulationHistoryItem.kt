package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Status

class SimulationHistoryItem(
    val action: Action,
    val actionResult: ActionResult,
    val beforeActionState: SimulationState,
    val afterTurnState: SimulationState,
    val selections: List<Pair<List<Action>, SelectedAction>>,
) {
    val useItem
        get() = selections.flatMap {
            (it.second.scenarioAction as? SelectedClimaxAction)?.useItem ?: emptyList()
        }
}
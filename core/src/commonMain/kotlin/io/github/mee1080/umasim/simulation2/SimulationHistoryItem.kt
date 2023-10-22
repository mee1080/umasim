package io.github.mee1080.umasim.simulation2

class SimulationHistoryItem(
    val beforeActionState: SimulationState,
    val afterTurnState: SimulationState,
    val selections: List<Triple<List<Action>, Action, ActionResult>>,
) {
    val action get() = selections.last().second

    val actionResult get() = selections.last().third

    val useItem
        get() = selections.flatMap {
            (it.third as? ClimaxBuyUseItemResult)?.useItem ?: emptyList()
        }
}
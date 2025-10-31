package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.simulation2.Action
import io.github.mee1080.umasim.simulation2.ActionResult
import io.github.mee1080.umasim.simulation2.ScenarioActionParam

data class OnsenActionParam(
    val excavationPoint: Int
) : ScenarioActionParam

sealed class OnsenActionResult : ActionResult
data class OnsenBathingResult(val success: Boolean) : OnsenActionResult()
data class OnsenSelectGensen(val gensen: Gensen) : OnsenActionResult()


class OnsenBathing : Action() {
    override fun predict(state: io.github.mee1080.umasim.simulation2.SimulationState, goal: Boolean): Array<ActionResult> {
        return arrayOf(OnsenBathingResult(true))
    }
}

package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Status

// TODO GrandLive
class GrandLiveScenarioEvents : CommonScenarioEvents() {

    override fun onTurnEnd(state: SimulationState): SimulationState {
        val newState = super.onTurnEnd(state)
        return when (newState.turn) {
            45 -> newState
                .updateStatus { it + Status(wisdom = 20, skillPt = 20) }

            else -> newState
        }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        val newState = super.afterSimulation(state)
        // 理事長絆最高
        return newState.updateStatus { it + Status(15, 15, 15, 15, 15, 50) }
    }
}

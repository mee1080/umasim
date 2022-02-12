package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Status

class UraScenarioEvents : ScenarioEvents {

    override fun onTurnEnd(state: SimulationState): SimulationState {
        return when (state.turn) {
            45 -> state
                .updateStatus { it + Status(wisdom = 20, skillPt = 20) }
            else -> state
        }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        // 理事長絆最高
        return state.updateStatus { it + Status(15, 15, 15, 15, 15, 50) }
    }
}

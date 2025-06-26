package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.simulation2.ActionSelector
import io.github.mee1080.umasim.simulation2.SimulationState

class MujintoScenarioEvents : BaseScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        val mujintoStatus = MujintoStatus()
        return super.beforeSimulation(state).copy(scenarioStatus = mujintoStatus)
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {

            // 建設計画
            3 -> base.selectPlan(selector)

            // 評価会
            12, 24, 36, 48, 60 -> base.mujintoEvaluation().selectPlan(selector)

            else -> base
        }
    }

    private fun SimulationState.mujintoEvaluation(): SimulationState {
        // TODO
        return this
    }

    private suspend fun SimulationState.selectPlan(selector: ActionSelector): SimulationState {
        // TODO
        return this
    }
}

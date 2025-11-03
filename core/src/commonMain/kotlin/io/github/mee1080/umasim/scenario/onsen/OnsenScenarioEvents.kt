package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.min

class OnsenScenarioEvents : BaseScenarioEvents() {
    override fun beforeSimulation(state: SimulationState): SimulationState {
        val onsenStatus = OnsenStatus(state.support.map { it.card }, state.factor)
        return super.beforeSimulation(state).copy(scenarioStatus = onsenStatus)
    }

    override fun beforeAction(state: SimulationState): SimulationState {
        val base = super.beforeAction(state)
        val onsenStatus = state.onsenStatus ?: return base
        return when (base.turn) {
            37, 61 -> base.addOnsenTicket(onsenTicketOnDig[onsenStatus.hoshinaRarity])
            else -> base
        }
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {
            3 -> base
                .copy(scenarioStatus = OnsenStatus(state.support.map { it.card }, state.factor))
                .selectGensen(selector)

            24 -> base
                .addAllStatus(10, 150)
                .allTrainingLevelUp()
                .selectGensen(selector)

            48 -> base
                .addAllStatus(30, 200, mapOf("機先の勝負" to 1))
                .allTrainingLevelUp()
                .selectGensen(selector)

            65 -> base.selectGensen(selector)

            72 -> base
                .addAllStatus(40, 300, mapOf("時中の妙" to 3, "本気で休んで、もう一度" to 3))
                .addOnsenTicket(min(1, onsenTicketOnDig[base.onsenStatus?.hoshinaRarity ?: 0]))

            73 -> base
                .addStatus(Status(skillHint = mapOf("全身全霊" to 1)))

            else -> base
        }
    }

    private fun SimulationState.addOnsenTicket(count: Int): SimulationState {
        return updateOnsenStatus {
            copy(onsenTicket = min(3, onsenTicket + count))
        }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        return state.addAllStatus(status = 50, skillPt = 250, skillHint = mapOf("保養が導く奇跡" to 2))
    }
}

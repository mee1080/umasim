package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.addGuest
import io.github.mee1080.umasim.simulation2.*

class OnsenScenarioEvents : BaseScenarioEvents() {
    override fun beforeSimulation(state: SimulationState): SimulationState {
        val onsenStatus = OnsenStatus(state.support.map { it.card })
        return super.beforeSimulation(state).copy(scenarioStatus = onsenStatus)
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        var base = super.afterAction(state, selector)
        base = checkExcavationComplete(base)
        return when (base.turn) {
            3 -> base.selectGensen(selector).addGuest(6, Scenario.ONSEN)
            24 -> base.yuamiKai(1, selector)
            31 -> base.addBathingTickets()
            48 -> base.yuamiKai(2, selector)
            55 -> base.addBathingTickets()
            61 -> base.selectGensen(selector)
            72 -> base.yuamiKai(3, selector)
            73 -> base.addStatus(Status(skillHint = mapOf("全身全霊" to 1)))
            else -> base
        }
    }

    private fun checkExcavationComplete(state: SimulationState): SimulationState {
        val onsenStatus = state.onsenStatus ?: return state
        val gensen = onsenStatus.selectedGensen ?: return state
        if (gensen.strata.all { (type, required) -> (onsenStatus.excavationProgress[type] ?: 0) >= required }) {
            val newProgress = onsenStatus.excavationProgress.toMutableMap()
            gensen.strata.keys.forEach { newProgress[it] = 0 }
            val ticketBonus = when (onsenStatus.hoshinaRank) {
                2 -> 2
                1 -> 1
                else -> 0
            }
            return state.updateOnsenStatus {
                copy(
                    excavatedGensen = excavatedGensen + gensen.name,
                    excavationProgress = newProgress,
                    onsenTicket = onsenTicket + ticketBonus
                )
            }
        }
        return state
    }

    private suspend fun SimulationState.selectGensen(selector: ActionSelector): SimulationState {
        val onsenStatus = onsenStatus ?: return this
        val candidates = gensenData.values
            .filter { it.name !in onsenStatus.excavatedGensen }
            .map { OnsenSelectGensen(it.name) }
        val selected = selector.select(this, candidates) as OnsenSelectGensen
        return updateOnsenStatus { copy(selectedGensen = gensenData[selected.gensen]) }
    }

    private suspend fun SimulationState.yuamiKai(count: Int, selector: ActionSelector): SimulationState {
        val (status, skillPt, skillHint) = when (count) {
            1 -> Triple(10, 150, emptyMap())
            2 -> Triple(30, 200, mapOf("機先の勝負" to 1))
            3 -> Triple(40, 300, mapOf("時中の妙" to 3, "本気で休んで、もう一度" to 3))
            else -> Triple(0, 0, emptyMap())
        }
        return addAllStatus(status, skillPt, skillHint)
            .allTrainingLevelUp()
            .run { if (count < 3) selectGensen(selector) else this }
    }

    private fun SimulationState.addBathingTickets(): SimulationState {
        val onsenStatus = onsenStatus ?: return this
        val amount = when (onsenStatus.hoshinaRank) {
            2 -> 2
            1 -> 1
            else -> 0
        }
        return updateOnsenStatus { copy(onsenTicket = onsenTicket + amount) }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        return state.addAllStatus(status = 50, skillPt = 250, skillHint = mapOf("保養が導く奇跡" to 2))
    }
}

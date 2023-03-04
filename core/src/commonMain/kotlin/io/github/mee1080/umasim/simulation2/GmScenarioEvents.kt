package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*

class GmScenarioEvents : CommonScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        return super.beforeSimulation(state).copy(
            goalRace = state.goalRace.dropLast(3) +
                    RaceEntry(78, "グランドマスターズ", 0, 30000, RaceGrade.FINALS, 1200, RaceGround.UNKNOWN, "")
        )
    }

    override fun beforeAction(state: SimulationState): SimulationState {
        return super.beforeAction(state).updateTrainingLevel()
    }

    private fun SimulationState.updateTrainingLevel(): SimulationState {
        val gmStatus = gmStatus ?: return this
        val specialRacePoint = when {
            turn <= 24 -> 0
            turn <= 48 -> 20
            turn <= 72 -> 40
            else -> 60
        }
        val newTraining = training.map {
            val value = it.count * 5 + gmStatus.fragmentCount[it.type]!! * 3 + specialRacePoint
            val level = when {
                value < 30 -> 1
                value < 60 -> 2
                value < 90 -> 3
                value < 120 -> 4
                else -> 5
            }
            if (level != it.level) it.copy(level = level) else it
        }
        return copy(training = newTraining)
    }

    override fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {
            // 導入
            2 -> base.copy(gmStatus = GmStatus())

            24 -> base.applyRace(state.raceStatus(5, 12, 60), 750)

            48 -> base.applyRace(state.raceStatus(5, 20, 75), 10000)

            72 -> base.applyRace(state.raceStatus(5, 25, 90), 20000)

            else -> base
        }
    }

    private fun SimulationState.applyRace(raceStatus: Status, fanCount: Int): SimulationState {
        return updateStatus {
            it + applyScenarioRaceBonus(raceStatus) + Status(fanCount = raceFanCount(fanCount))
        }
    }

    override fun onTurnEnd(state: SimulationState): SimulationState {
        val base = super.onTurnEnd(state)
        val gmStatus = base.gmStatus ?: return base
        return state.copy(gmStatus = gmStatus.turnChange())
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        return super.afterSimulation(state).updateStatus {
            it + Status(20, 20, 20, 20, 20, 60)
        }
    }
}

package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.RaceEntry
import io.github.mee1080.umasim.data.RaceGrade
import io.github.mee1080.umasim.data.Status

object ClimaxCalculator : ScenarioCalculator {

    override fun applyScenarioRaceBonus(state: SimulationState, base: Status): Status {
        var status = base
        status = state.enableItem.raceBonus?.let {
            status.copy(
                speed = (status.speed * (1 + it.raceFactor / 100.0)).toInt(),
                stamina = (status.stamina * (1 + it.raceFactor / 100.0)).toInt(),
                power = (status.power * (1 + it.raceFactor / 100.0)).toInt(),
                guts = (status.guts * (1 + it.raceFactor / 100.0)).toInt(),
                wisdom = (status.wisdom * (1 + it.raceFactor / 100.0)).toInt(),
                skillPt = (status.skillPt * (1 + it.raceFactor / 100.0)).toInt(),
            )
        } ?: status
        status = state.enableItem.fanBonus?.let {
            status.copy(
                fanCount = (status.fanCount * (1 + it.fanFactor / 100.0)).toInt(),
            )
        } ?: status
        return status
    }

    override fun calcBaseRaceStatus(state: SimulationState, race: RaceEntry, goal: Boolean): Status {
        return if (goal) when (race.grade) {
            RaceGrade.DEBUT -> state.raceStatus(3, 3, 15)
            RaceGrade.FINALS -> state.raceStatus(5, 10, 30)
            else -> throw IllegalArgumentException()
        } else {
            // HP:通常-15との差分
            Status(hp = -5) + when (race.grade) {
                RaceGrade.PRE_OPEN -> state.raceStatus(1, 5, 20)
                RaceGrade.OPEN -> state.raceStatus(1, 5, 20)
                RaceGrade.G3 -> state.raceStatus(1, 8, 25)
                RaceGrade.G2 -> state.raceStatus(1, 8, 25)
                RaceGrade.G1 -> state.raceStatus(1, 10, 35)
                else -> throw IllegalArgumentException()
            }
        }
    }
}

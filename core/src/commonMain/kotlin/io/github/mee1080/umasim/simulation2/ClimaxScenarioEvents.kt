package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.RaceEntry
import io.github.mee1080.umasim.data.RaceGrade
import io.github.mee1080.umasim.data.RaceGround
import io.github.mee1080.umasim.data.Status

class ClimaxScenarioEvents : ScenarioEvents {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        return state.copy(
            goalRace = listOf(
                state.goalRace.first(),
                RaceEntry(74, "クライマックス第1戦", 0, 7000, RaceGrade.FINALS, 1200, RaceGround.UNKNOWN, ""),
                RaceEntry(76, "クライマックス第2戦", 0, 10000, RaceGrade.FINALS, 1200, RaceGround.UNKNOWN, ""),
                RaceEntry(78, "クライマックス第3戦", 0, 30000, RaceGrade.FINALS, 1200, RaceGround.UNKNOWN, ""),
            )
        )
    }

    override fun beforeAction(state: SimulationState): SimulationState? {
        return when (state.turn) {
            // クラシック継承
            31 -> state
                .updateFactor()
            // クラシック夏合宿
            40 -> state
                .updateStatus { it + Status(guts = 10) }
            // シニア継承
            55 -> state
                .updateFactor()
            else -> null
        }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        // 余ったコインを秘伝書換算でステータスに加算
        val coinToStatus = state.shopCoin / 10
        return state.copy(
            status = state.status + Status(
                speed = coinToStatus,
                stamina = coinToStatus,
                power = coinToStatus,
                guts = coinToStatus,
                wisdom = coinToStatus,
            )
        )
    }
}

package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.SimulationState

object BCCalculator : ScenarioCalculator {
    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean
    ): Status {
        // TODO: BCシナリオ固有のステータス上昇計算を実装する
        return super.calcScenarioStatus(info, base, raw, friendTraining)
    }

    override fun updateScenarioTurn(state: SimulationState): SimulationState {
        // TODO: BCシナリオ固有のターン更新処理を実装する
        return super.updateScenarioTurn(state)
    }

    // TODO: 必要に応じて他のメソッドをオーバーライドする
}

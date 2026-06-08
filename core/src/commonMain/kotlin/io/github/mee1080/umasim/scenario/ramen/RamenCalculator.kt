package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.Calculator.CalcInfo

object RamenCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean
    ): Status {
        // TODO: ラーメンシナリオ固有のステータス上昇計算を実装する
        return Status()
    }

    override fun getScenarioCalcBonus(baseInfo: CalcInfo) = null // TODO: 必要に応じてボーナスを実装
}

package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.RaceEntry
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.Action
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.ScenarioActionParam
import io.github.mee1080.umasim.simulation2.SimulationState

// TODO: 無人島シナリオ固有の計算ロジックを実装する必要があれば、Defaultではなく独自のCalculatorを実装する
object MujintoCalculator : ScenarioCalculator {
    // TODO: 以下の各メソッドについて、無人島シナリオ固有の計算ロジックが必要な場合は実装する

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        // TODO: 無人島シナリオ固有のステータス計算があれば実装
        return super.calcScenarioStatus(info, base, raw, friendTraining)
    }

    override fun calcBaseRaceStatus(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean,
    ): Status? {
        // TODO: 無人島シナリオ固有のレースステータス計算があれば実装
        return super.calcBaseRaceStatus(state, race, goal)
    }

    override fun applyScenarioRaceBonus(
        state: SimulationState,
        base: Status,
    ): Status {
        // TODO: 無人島シナリオ固有のレースボーナス適用があれば実装
        return super.applyScenarioRaceBonus(state, base)
    }

    override fun raceScenarioActionParam(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean,
    ): ScenarioActionParam? {
        // TODO: 無人島シナリオ固有のレースアクションパラメータがあれば実装
        return super.raceScenarioActionParam(state, race, goal)
    }

    override fun predictSleep(
        state: SimulationState,
    ): Array<Action>? {
        // TODO: 無人島シナリオ固有の睡眠予測があれば実装
        return super.predictSleep(state)
    }

    override fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> {
        // TODO: 無人島シナリオ固有のアクションパラメータ予測があれば実装
        return super.predictScenarioActionParams(state, baseActions)
    }

    override fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean,
    ): Array<Action> {
        // TODO: 無人島シナリオ固有のアクション予測があれば実装
        return super.predictScenarioAction(state, goal)
    }

    override fun normalRaceBlocked(
        state: SimulationState,
    ): Boolean {
        // TODO: 無人島シナリオ固有の通常レースブロック条件があれば実装
        return super.normalRaceBlocked(state)
    }

    override fun updateScenarioTurn(
        state: SimulationState,
    ): SimulationState {
        // TODO: 無人島シナリオ固有のターン更新処理があれば実装
        return super.updateScenarioTurn(state)
    }

    override fun updateOnAddStatus(
        state: SimulationState,
        status: Status,
    ): SimulationState {
        // TODO: 無人島シナリオ固有のステータス加算時処理があれば実装
        return super.updateOnAddStatus(state, status)
    }
}

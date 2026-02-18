package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.scenario.CommonScenarioEvents
import io.github.mee1080.umasim.simulation2.ActionSelector
import io.github.mee1080.umasim.simulation2.SimulationState

class BCScenarioEvents : CommonScenarioEvents() {
    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.beforeAction(state, selector)
        // TODO: BCシナリオ固有の特定ターンでのイベント処理を実装する
        return base
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        // TODO: BCシナリオ固有のアクション後イベント処理を実装する
        return base
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        // TODO: BCシナリオ固有の育成完了時イベント処理を実装する
        return super.afterSimulation(state)
    }
}

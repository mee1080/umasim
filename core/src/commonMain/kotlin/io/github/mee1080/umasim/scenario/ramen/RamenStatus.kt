package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState

fun SimulationState.updateRamenStatus(update: RamenStatus.() -> RamenStatus): SimulationState {
    val ramenStatus = this.ramenStatus ?: return this
    return copy(scenarioStatus = ramenStatus.update())
}

/**
 * Ramenシナリオ固有の状態を保持するクラス。
 */
data class RamenStatus(
    // TODO: ラーメンシナリオ固有の状態を定義する
    val ramenLevel: Int = 1,
) : ScenarioStatus

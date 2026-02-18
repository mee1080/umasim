package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.simulation2.ScenarioStatus

/**
 * BCシナリオ固有の状態を保持するクラス。
 */
data class BCStatus(
    // TODO: BCシナリオ固有のプロパティを追加する
    val dummy: Int = 0
) : ScenarioStatus

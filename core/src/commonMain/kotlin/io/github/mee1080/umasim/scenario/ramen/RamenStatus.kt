package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.simulation2.SimulationState

fun SimulationState.updateRamenStatus(update: RamenStatus.() -> RamenStatus): SimulationState {
    val ramenStatus = this.ramenStatus ?: return this
    return copy(scenarioStatus = ramenStatus.update())
}

/**
 * Ramenシナリオ固有の状態を保持するクラス。
 */
data class RamenStatus(
    val selectedRegions: List<RamenStore.Region> = emptyList(),
    val gauges: Map<RamenStore.TipType, Int> = mapOf(
        RamenStore.TipType.NOODLE to 0,
        RamenStore.TipType.SOUP to 0,
        RamenStore.TipType.TOPPING to 0
    ),
    val tips: Map<RamenStore.TipType, Int> = mapOf(
        RamenStore.TipType.NOODLE to 0,
        RamenStore.TipType.SOUP to 0,
        RamenStore.TipType.TOPPING to 0,
        RamenStore.TipType.HIDDEN to 0
    ),
    val excitementPt: Int = 0,
    val activeTastingRegion: RamenStore.Region? = null,
) : io.github.mee1080.umasim.simulation2.ScenarioStatus {
    fun addGauges(noodle: Int = 0, soup: Int = 0, topping: Int = 0): RamenStatus {
        val newGauges = gauges.toMutableMap()
        val newTips = tips.toMutableMap()

        fun add(type: RamenStore.TipType, amount: Int) {
            if (amount <= 0) return
            val currentGauge = newGauges[type] ?: 0
            val totalGauge = currentGauge + amount
            val gainedTips = totalGauge / 7
            val remainingGauge = totalGauge % 7

            newGauges[type] = remainingGauge
            newTips[type] = minOf(10, (newTips[type] ?: 0) + gainedTips)
        }

        add(RamenStore.TipType.NOODLE, noodle)
        add(RamenStore.TipType.SOUP, soup)
        add(RamenStore.TipType.TOPPING, topping)

        return copy(gauges = newGauges, tips = newTips)
    }

    fun addHiddenTaste(amount: Int): RamenStatus {
        val newTips = tips.toMutableMap()
        newTips[RamenStore.TipType.HIDDEN] = minOf(4, (newTips[RamenStore.TipType.HIDDEN] ?: 0) + amount)
        return copy(tips = newTips)
    }
}

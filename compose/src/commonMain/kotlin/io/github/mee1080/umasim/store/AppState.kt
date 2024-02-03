package io.github.mee1080.umasim.store

import androidx.compose.runtime.Stable
import io.github.mee1080.umasim.race.calc2.RaceSetting
import io.github.mee1080.umasim.store.framework.State

const val NOT_SELECTED = "(未選択)"

@Stable
data class AppState(
    val setting: RaceSetting = RaceSetting(),
    val charaName: String = NOT_SELECTED,
    val skillIdSet: Set<String> = emptySet(),
    val simulationCount: Int = 100,
    val simulationProgress: Int = 0,
    val simulationSummary: SimulationSummary? = null,
) : State

data class SimulationSummary(
    val allSummary: SimulationSummaryEntry,
    val spurtSummary: SimulationSummaryEntry,
    val notSpurtSummary: SimulationSummaryEntry,
    val spurtRate: Double,
)

data class SimulationSummaryEntry(
    val count: Int = 0,
    val averageTime: Double = 0.0,
    val bestTime: Double = 0.0,
    val worstTime: Double = 0.0,
    val averageSp: Double = 0.0,
    val bestSp: Double = 0.0,
    val worstSp: Double = 0.0,
)

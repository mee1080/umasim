package io.github.mee1080.umasim.store

import androidx.compose.runtime.Stable
import io.github.mee1080.umasim.race.calc2.RaceSetting
import io.github.mee1080.umasim.race.data2.SkillData
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

@Stable
data class SimulationSummary(
    val allSummary: SimulationSummaryEntry,
    val spurtSummary: SimulationSummaryEntry,
    val notSpurtSummary: SimulationSummaryEntry,
    val spurtRate: Double,
    val skillSummaries: List<Pair<SkillData, SimulationSkillSummary>>,
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

data class SimulationSkillInfo(
    val phase1StartFrame: Int,
    val phase2StartFrame: Int,
    val startFrame1: Int = -1,
    val endFrame1: Int = -1,
    val startFrame2: Int = -1,
    val endFrame2: Int = -1,
) {
    val phase1ConnectionFrame by lazy { calcConnectionFrame(phase1StartFrame) }
    val phase2ConnectionFrame by lazy { calcConnectionFrame(phase2StartFrame) }
    val phase2DelayFrame by lazy { calcDelayFrame(phase2StartFrame) }

    val triggeredPhase by lazy {
        when {
            startFrame1 < phase1StartFrame -> 0
            startFrame1 < phase2StartFrame -> 1
            else -> 2
        }
    }

    private fun calcConnectionFrame(phaseStartFrame: Int): Int? {
        return when (phaseStartFrame) {
            in (startFrame1 + 1)..endFrame1 -> phaseStartFrame - startFrame1
            in (startFrame2 + 1)..endFrame2 -> phaseStartFrame - startFrame2
            else -> null
        }
    }

    private fun calcDelayFrame(phaseStartFrame: Int): Int? {
        return when {
            phaseStartFrame <= startFrame1 -> startFrame1 - phaseStartFrame
            phaseStartFrame <= startFrame2 -> startFrame2 - phaseStartFrame
            else -> null
        }
    }
}

data class SimulationSkillSummary(
    val count: Int,
    val triggerRate: Double,
    val averageStartFrame1: Double,
    val doubleTriggerRate: Double,
    val averageStartFrame2: Double,
    val phase0TriggeredRate:Double,
    val phase1ConnectionRate: Double,
    val averagePhase1ConnectionFrame: Double,
    val phase1TriggeredRate:Double,
    val phase2ConnectionRate: Double,
    val averagePhase2ConnectionFrame: Double,
    val phase2TriggeredRate:Double,
    val averagePhase2DelayFrame: Double,
)
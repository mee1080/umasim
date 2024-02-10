package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.averageOf
import io.github.mee1080.umasim.race.calc2.RaceCalculator
import io.github.mee1080.umasim.race.calc2.RaceSimulationResult
import io.github.mee1080.umasim.race.calc2.RaceState
import io.github.mee1080.umasim.race.data2.SkillData
import io.github.mee1080.umasim.store.*
import io.github.mee1080.umasim.store.framework.AsyncOperation
import io.github.mee1080.umasim.store.framework.OnRunning

private val simulationTag = OnRunning.Tag()

private val simulationPolicy = OnRunning.Ignore(simulationTag)

private val simulationCancelPolicy = OnRunning.CancelAndRun(simulationTag)

fun runSimulation() = AsyncOperation<AppState>({ state ->
    if (state.simulationCount <= 0) return@AsyncOperation
    emit { it.copy(simulationSummary = null) }
    val calculator = RaceCalculator(state.setting)
    val results = mutableListOf<RaceSimulationResult>()
    val skillSummaries = state.setting.hasSkills.associateWith {
        mutableListOf<SimulationSkillInfo>()
    }
    repeat(state.simulationCount) { count ->
        emit { it.copy(simulationProgress = count + 1) }
        val result = calculator.simulate()
        results += result.first
        createSkillMap(result.second).forEach {
            skillSummaries[it.key]?.add(it.value)
        }
    }
    val spurtResults = results.filter { it.maxSpurt }
    val notSpurtResult = results.filter { !it.maxSpurt }
    val summary = SimulationSummary(
        allSummary = toSummary(results),
        spurtSummary = toSummary(spurtResults),
        notSpurtSummary = toSummary(notSpurtResult),
        spurtRate = spurtResults.size.toDouble() / results.size,
        skillSummaries = skillSummaries.map { it.key to toSummary(it.value) },
    )
    emit {
        it.copy(
            simulationProgress = 0,
            simulationSummary = summary,
        )
    }
}, simulationPolicy)

private fun toSummary(result: List<RaceSimulationResult>): SimulationSummaryEntry {
    return if (result.isEmpty()) SimulationSummaryEntry() else SimulationSummaryEntry(
        count = result.size,
        averageTime = result.averageOf { it.raceTime },
        bestTime = result.minOf { it.raceTime },
        worstTime = result.maxOf { it.raceTime },
        averageSp = result.averageOf { it.spDiff },
        bestSp = result.maxOf { it.spDiff },
        worstSp = result.minOf { it.spDiff },
    )
}

private fun createSkillMap(state: RaceState): Map<SkillData, SimulationSkillInfo> {
    val (phase1Start, phase2Start) = getPhaseChangeFrames(state)
    val skillMap = state.setting.hasSkills.associateWith {
        SimulationSkillInfo(phase1Start, phase2Start)
    }.toMutableMap()
    state.simulation.frames.forEachIndexed { index, frame ->
        frame.skills.forEach {
            val current = skillMap[it.skill] ?: return@forEach
            if (current.startFrame1 < 0) {
                skillMap[it.skill] = current.copy(startFrame1 = index)
            } else {
                skillMap[it.skill] = current.copy(startFrame2 = index)
            }
        }
        frame.endedSkills.forEach {
            val current = skillMap[it.data.skill] ?: return@forEach
            if (current.endFrame1 < 0) {
                skillMap[it.data.skill] = current.copy(endFrame1 = index)
            } else {
                skillMap[it.data.skill] = current.copy(endFrame2 = index)
            }
        }
    }
    return skillMap
}

private fun getPhaseChangeFrames(state: RaceState): Pair<Int, Int> {
    var phase1 = -1
    var phase2 = -1
    var nextPhaseStart = state.setting.phase1Start
    state.simulation.frames.forEachIndexed { index, frame ->
        if (phase1 < 0) {
            if (frame.startPosition >= nextPhaseStart) {
                phase1 = index - 1
                nextPhaseStart = state.setting.phase2Start
            }
        } else if (phase2 < 0) {
            if (frame.startPosition >= nextPhaseStart) {
                phase2 = index - 1
                return@forEachIndexed
            }
        }
    }
    return phase1 to phase2
}

private fun toSummary(list: List<SimulationSkillInfo>): SimulationSkillSummary {
    val triggeredList = list.filter { it.startFrame1 >= 0 }
    val secondTriggeredList = triggeredList.filter { it.startFrame2 >= 0 }
    val phase1ConnectedList = triggeredList.mapNotNull { it.phase1ConnectionFrame }
    val phase2ConnectedList = triggeredList.mapNotNull { it.phase2ConnectionFrame }
    return SimulationSkillSummary(
        count = triggeredList.size,
        triggerRate = if (list.isEmpty()) 0.0 else triggeredList.size.toDouble() / list.size,
        averageStartFrame1 = triggeredList.averageOf { it.startFrame1.toDouble() },
        doubleTriggerRate = if (list.isEmpty()) 0.0 else secondTriggeredList.size.toDouble() / list.size,
        averageStartFrame2 = secondTriggeredList.averageOf { it.startFrame2.toDouble() },
        phase0TriggeredRate = if (triggeredList.isEmpty()) 0.0 else {
            triggeredList.count { it.triggeredPhase == 0 }.toDouble() / triggeredList.size
        },
        phase1ConnectionRate = if (triggeredList.isEmpty()) 0.0 else phase1ConnectedList.size.toDouble() / triggeredList.size,
        averagePhase1ConnectionFrame = phase1ConnectedList.average(),
        phase1TriggeredRate = if (triggeredList.isEmpty()) 0.0 else {
            triggeredList.count { it.triggeredPhase == 1 }.toDouble() / triggeredList.size
        },
        phase2ConnectionRate = if (triggeredList.isEmpty()) 0.0 else phase2ConnectedList.size.toDouble() / triggeredList.size,
        averagePhase2ConnectionFrame = phase2ConnectedList.average(),
        phase2TriggeredRate = if (triggeredList.isEmpty()) 0.0 else {
            triggeredList.count { it.triggeredPhase == 2 }.toDouble() / triggeredList.size
        },
        averagePhase2DelayFrame = triggeredList.mapNotNull { it.phase2DelayFrame }.average(),
    )
}

fun cancelSimulation() = AsyncOperation<AppState>({ state ->
    emit { it.copy(simulationProgress = 0, simulationSummary = null) }
}, simulationCancelPolicy)

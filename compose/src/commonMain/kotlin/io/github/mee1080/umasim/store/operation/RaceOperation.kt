package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.averageOf
import io.github.mee1080.umasim.race.calc2.RaceCalculator
import io.github.mee1080.umasim.race.calc2.RaceSimulationResult
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.SimulationSummary
import io.github.mee1080.umasim.store.SimulationSummaryEntry
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
    repeat(state.simulationCount) { count ->
        emit { it.copy(simulationProgress = count + 1) }
        results += calculator.simulate().first
    }
    val spurtResults = results.filter { it.maxSpurt }
    val notSpurtResult = results.filter { !it.maxSpurt }
    val summary = SimulationSummary(
        allSummary = toSummary(results),
        spurtSummary = toSummary(spurtResults),
        notSpurtSummary = toSummary(notSpurtResult),
        spurtRate = spurtResults.size.toDouble() / results.size,
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

fun cancelSimulation() = AsyncOperation<AppState>({ state ->
    emit { it.copy(simulationProgress = 0, simulationSummary = null) }
}, simulationCancelPolicy)

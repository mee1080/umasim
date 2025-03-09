package io.github.mee1080.umasim.scenario

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.RaceEntry
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.simulation2.Action
import io.github.mee1080.umasim.simulation2.Calculator.CalcInfo
import io.github.mee1080.umasim.simulation2.ScenarioActionParam
import io.github.mee1080.umasim.simulation2.SimulationState

interface ScenarioCalculator {

    object Default : ScenarioCalculator

    fun calcScenarioStatus(
        info: CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status = Status()

    fun calcBaseRaceStatus(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean,
    ): Status? = null

    fun applyScenarioRaceBonus(
        state: SimulationState,
        base: Status,
    ): Status = base

    fun raceScenarioActionParam(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean,
    ): ScenarioActionParam? = null

    fun predictSleep(
        state: SimulationState,
    ): Array<Action>? = null

    fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> = baseActions

    fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean,
    ): Array<Action> = emptyArray()

    fun normalRaceBlocked(
        state: SimulationState,
    ): Boolean = false

    fun updateScenarioTurn(
        state: SimulationState,
    ): SimulationState = state

    fun updateOnAddStatus(
        state: SimulationState,
        status: Status,
    ): SimulationState = state
}

/*
 * Copyright 2021 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*
import kotlin.math.min

class Simulator(
    scenario: Scenario,
    chara: Chara,
    supportCardList: List<SupportCard>,
    factorList: List<Pair<StatusType, Int>> = emptyList(),
    val option: Option = Option()
) {

    class Option(
        val turn: Int = Int.MAX_VALUE,
    )

    private val initialRelationAll = supportCardList.sumOf { it.initialRelationAll }

    private val initialState = SimulationState(
        scenario = scenario,
        chara = chara,
        factor = factorList,
        goalRace = Store.getGoalRaceList(chara.charaId),
        member = supportCardList.toMemberState(scenario).map {
            it.copy(
                supportState = it.supportState?.copy(
                    relation = it.supportState.relation + initialRelationAll
                )
            )
        },
        training = Store.getTrainingList(scenario)
            .groupBy { it.type }
            .map { entry ->
                TrainingState(
                    type = entry.key,
                    base = entry.value.sortedBy { it.level },
                    level = 1,
                    count = 0,
                    levelOverride = null,
                )
            },
        status = Status(
            skillPt = 120,
            hp = 100,
            motivation = 0,
            maxHp = 100,
            skillHint = emptyMap(),
            fanCount = 1,
        ) + chara.initialStatus + supportCardList
            .map { target -> target.initialStatus(supportCardList.map { it.type }) }
            .reduce { acc, status -> acc + status },
        supportCount = supportCardList.groupBy { it.type }.mapValues { it.value.size },
        totalRaceBonus = supportCardList.sumOf { it.race },
        totalFanBonus = supportCardList.sumOf { it.fan },
    )

    suspend fun simulate(
        selector: ActionSelector,
        eventsProducer: (SimulationState) -> SimulationEvents = { SimulationEvents() },
    ) = simulateWithHistory(selector, eventsProducer).first

    suspend fun simulateWithHistory(
        selector: ActionSelector,
        eventsProducer: (SimulationState) -> SimulationEvents = { SimulationEvents() },
    ): Pair<Summary, List<SimulationHistoryItem>> {
        var state = initialState
        val turn = min(option.turn, state.scenario.turn)
        val history = mutableListOf<SimulationHistoryItem>()
        val scenarioEvents = when (state.scenario) {
            Scenario.URA -> UraScenarioEvents()
            Scenario.AOHARU -> AoharuScenarioEvents()
            Scenario.CLIMAX -> ClimaxScenarioEvents()
            Scenario.GRAND_LIVE -> GrandLiveScenarioEvents()
            Scenario.GM -> GmScenarioEvents()
            Scenario.LARC -> LArcScenarioEvents()
            Scenario.UAF -> UafScenarioEvents()
        }
        val events = eventsProducer(state)
        state = scenarioEvents.beforeSimulation(state)
        state = state.copy(status = scenarioEvents.initialStatus(state.status))
        state = events.beforeSimulation(state)
        state = state.copy(status = events.initialStatus(state.status))
        selector.init(state)
        repeat(min(turn, if (state.scenario == Scenario.LARC) 67 else 78)) {
            state = state.onTurnChange()
            state = scenarioEvents.beforeAction(state)
            state = events.beforeAction(state)
            state = state.shuffleMember()
            val beforeActionState = state
            val selections = mutableListOf<Triple<List<Action>, Action, ActionResult>>()
            do {
                val selection = state.predict(state.turn)
                val action = selector.select(state, selection)
                val result = action.randomSelectResult()
                selections += Triple(selection, action, result)
                state = state.applyAction(action, result)
            } while (!action.turnChange)
            state = scenarioEvents.afterAction(state, selector)
            state = scenarioEvents.onTurnEnd(state)
            history.add(SimulationHistoryItem(beforeActionState, state, selections))
        }
        state = scenarioEvents.afterSimulation(state)
        return Summary(state.status, history, state.member) to history
    }
}

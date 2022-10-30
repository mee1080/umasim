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

class Simulator(
    scenario: Scenario,
    chara: Chara,
    supportCardList: List<SupportCard>,
    factorList: List<Pair<StatusType, Int>> = emptyList(),
    val option: Option = Option()
) {

    class Option(
        val levelUpTurns: IntArray? = null,
        val raceBonusStatus: Int = 8,
        val raceBonusSkillPt: Int = 180,
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
        levelUpTurns = option.levelUpTurns?.asList() ?: listOf(37, 38, 39, 40, 61, 62, 63, 64),
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
        supportTypeCount = supportCardList.map { it.type }.distinct().count(),
        totalRaceBonus = supportCardList.sumOf { it.race },
        totalFanBonus = supportCardList.sumOf { it.fan },
    )

    private val raceBonus = initialState.totalRaceBonus.let {
        Status(
            speed = option.raceBonusStatus * it / 100,
            stamina = option.raceBonusStatus * it / 100,
            power = option.raceBonusStatus * it / 100,
            guts = option.raceBonusStatus * it / 100,
            wisdom = option.raceBonusStatus * it / 100,
            skillPt = option.raceBonusSkillPt * it / 100
        )
    }

    fun simulate(
        turn: Int,
        selector: ActionSelector,
        events: SimulationEvents = SimulationEvents()
    ) = simulateWithHistory(turn, selector, events).first

    fun simulateWithHistory(
        turn: Int,
        selector: ActionSelector,
        events: SimulationEvents = SimulationEvents()
    ): Pair<Summary, List<SimulationHistoryItem>> {
        var state = initialState
        val history = mutableListOf<SimulationHistoryItem>()
        val scenarioEvents = when (state.scenario) {
            Scenario.URA -> UraScenarioEvents()
            Scenario.AOHARU -> AoharuScenarioEvents()
            Scenario.CLIMAX -> ClimaxScenarioEvents()
            Scenario.GRAND_LIVE -> GrandLiveScenarioEvents()
        }
        state = scenarioEvents.beforeSimulation(state)
        state = state.copy(status = scenarioEvents.initialStatus(state.status))
        state = events.beforeSimulation(state)
        state = state.copy(status = events.initialStatus(state.status))
        selector.init(state)
        repeat(turn) {
            state = state.onTurnChange()
            state = scenarioEvents.beforeAction(state)
            state = events.beforeAction(state)
            state = state.shuffleMember()
            var action: Action?
            val useItem = mutableListOf<ShopItem>()
            do {
                val selection = state.predict(state.turn)
                val selectedAction = selector.selectWithItem(state, selection)
                selectedAction.buyItem?.let {
                    state = state.buyItem(it)
                }
                selectedAction.useItem?.let {
                    useItem.addAll(it)
                    state = state.applyItem(it)
                }
                selectedAction.lesson?.let {
                    state = state.purchaseLesson(it)
                }
                action = selectedAction.action
            } while (action == null)
            val result = randomSelect(action.resultCandidate)
            history.add(SimulationHistoryItem(action, result, state, useItem))
            state = state.applyAction(action, result)
            state = scenarioEvents.afterAction(state, selector)
            state = scenarioEvents.onTurnEnd(state)
        }
        state = scenarioEvents.afterSimulation(state)
        state = state.updateStatus { it + raceBonus }
        return Summary(state.status, history, state.member) to history
    }
}

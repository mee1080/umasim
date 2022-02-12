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
    val option: Option = Option()
) {

    class Option(
        val levelUpTurns: IntArray? = null,
        val raceBonusStatus: Int = 8,
        val raceBonusSkillPt: Int = 180,
    )

    private val initialState = SimulationState(
        scenario = scenario,
        chara = chara,
        goalRace = Store.getGoalRaceList(chara.charaId),
        member = supportCardList.mapIndexed { index, card ->
            MemberState(
                index = index,
                card = card,
                position = StatusType.NONE,
                supportState = SupportState(
                    relation = card.initialRelation,
                    hintIcon = false,
                ),
                scenarioState = when (scenario) {
                    Scenario.URA -> UraMemberState
                    Scenario.AOHARU -> Store.Aoharu.getTeamMember(card.id)?.let { member ->
                        AoharuMemberState(
                            member = member,
                            status = member.initialStatus,
                            maxStatus = member.maxStatus,
                            aoharuTrainingCount = 0,
                            aoharuIcon = false,
                        )
                    } ?: AoharuNotMemberState
                }
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
        turn = 0,
        status = Status(
            skillPt = 120,
            hp = 100,
            motivation = 0,
            maxHp = 100,
            skillHint = emptyMap(),
        ) + chara.initialStatus + supportCardList
            .map { it.initialStatus }
            .reduce { acc, status -> acc + status },
        condition = emptyList(),
        supportTypeCount = supportCardList.map { it.type }.distinct().count(),
        totalRaceBonus = supportCardList.sumOf { it.race },
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
    ): Pair<Summary, List<Triple<Action, Status, SimulationState>>> {
        var state = initialState
        val history = mutableListOf<Triple<Action, Status, SimulationState>>()
        val scenarioEvents = when (state.scenario) {
            Scenario.URA -> ScenarioEvents()
            Scenario.AOHARU -> AoharuScenarioEvents.asScenarioEvents()
        }
        state = scenarioEvents.beforeSimulation(state)
        state = state.copy(status = scenarioEvents.initialStatus(state.status))
        state = events.beforeSimulation(state)
        state = state.copy(status = events.initialStatus(state.status))
        repeat(turn) {
            state = state.onTurnChange()
            state = scenarioEvents.beforeAction(state) ?: events.beforeAction(state)
            val action = selector.select(state, state.predict(state.turn))
            val result = randomSelect(action.resultCandidate)
            history.add(Triple(action, result, state))
            state = state.applyAction(action, result)
            state = scenarioEvents.afterAction(state) ?: events.afterAction(state)
            state = scenarioEvents.onTurnEnd(state)
        }
        state = state.updateStatus { it + raceBonus }
        return Summary(state.status, history, state.member) to history
    }
}

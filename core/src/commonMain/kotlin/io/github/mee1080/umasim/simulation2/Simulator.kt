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
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class Simulator(
    scenario: Scenario,
    chara: Chara,
    supportCardList: List<SupportCard>,
    val option: Option = Option()
) {

    class Option(
        val levelUpTurns: IntArray = intArrayOf(28, 29, 30, 31, 46, 47, 48, 49),
        val raceBonusStatus: Int = 60,
        val raceBonusSkillPt: Int = 700,
    )

    var state = SimulationState(
        scenario = scenario,
        chara = chara,
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
                    Scenario.AOHARU -> Store.Aoharu.getTeamMember(card.id)!!.let { member ->
                        AoharuMemberState(
                            member = member,
                            status = member.initialStatus,
                            maxStatus = member.maxStatus,
                            aoharuTrainingCount = 0,
                            aoharuIcon = false,
                        )
                    }
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
        levelUpTurns = option.levelUpTurns.asList(),
        turn = 0,
        status = Status(
            skillPt = 120,
            hp = 100,
            motivation = 0,
            maxHp = 100,
            skillHint = emptyMap(),
        ) + chara.initialStatus + supportCardList.map { it.initialStatus }
            .reduce { acc, status -> acc + status } + supportCardList.sumOf { it.race }.let {
            Status(
                speed = option.raceBonusStatus * it / 100,
                stamina = option.raceBonusStatus * it / 100,
                power = option.raceBonusStatus * it / 100,
                guts = option.raceBonusStatus * it / 100,
                wisdom = option.raceBonusStatus * it / 100,
                skillPt = option.raceBonusSkillPt * it / 100
            )
        },
        condition = emptyList(),
    )

    val history = mutableListOf<Triple<Action, Status, SimulationState>>()

    val summary get() = Summary(state.status, history, state.member)

    var selection = listOf<Action>()

    fun getTrainingSelection(type: StatusType) =
        selection.first { it is Action.Training && it.type == type } as Action.Training

    init {
        nextTurn()
    }

    private fun nextTurn() {
        state = state.onTurnChange()
        val supportPosition = state.member.groupBy { it.position }

        selection = mutableListOf(
            *(state.training.map {
                calcTrainingResult(it, supportPosition[it.type] ?: emptyList())
            }).toTypedArray(),
            *(if (state.isLevelUpTurn) arrayOf(
                Action.Sleep(
                    state.status,
                    Status(hp = 40, motivation = 1) to 1
                )
            ) else arrayOf(
                Action.Sleep(
                    state.status,
                    Status(hp = 70) to 25,
                    Status(hp = 50) to 62,
                    Status(hp = 30) to 10,
                    Status(hp = 30, motivation = -1) to 3,
                ),
                Action.Outing(
                    state.status,
                    null,
                    Status(motivation = 2) to 10,
                    Status(hp = 10, motivation = 1) to 4,
                    Status(hp = 20, motivation = 1) to 2,
                    Status(hp = 30, motivation = 1) to 1,
                    Status(hp = 10, motivation = 1) to 8,
                )
            ))
        )
    }

    fun doTraining(type: StatusType) {
        doAction(getTrainingSelection(type))
    }

    fun doSleep() {
        doAction(selection.first { it is Action.Sleep })
    }

    fun doOuting() {
        doAction(selection.first { it is Action.Outing })
    }

    fun doAction(action: Action) {
        val result = randomSelect(*action.resultCandidate)
        history.add(Triple(action, result, state))
        state = state.applyAction(action, result)
        nextTurn()
    }

    private fun calcTrainingResult(
        training: TrainingState,
        support: List<MemberState>
    ): Action {
        val failureRate = calcTrainingFailureRate(training.current, support)
        val successStatus =
            Calculator.calcTrainingSuccessStatus(
                state.chara,
                training.current,
                state.status.motivation,
                support
            ) + Status(supportRelation = calcTrainingRelation(state.condition.contains("愛嬌○"), support))
        val successCandidate = calcTrainingHint(support)
            .map { successStatus + it to 100 - failureRate }.toTypedArray()
        val failureRateValue = failureRate * successCandidate.size
        val failureCandidate = when {
            failureRate == 0 -> {
                emptyArray()
            }
            training.type == StatusType.WISDOM -> {
                arrayOf(Status(hp = successStatus.hp) to failureRateValue)
            }
            failureRate >= 30 -> {
                val target = trainingType.copyOf().apply { shuffle() }.slice(0..1).map { it to -10 }.toTypedArray()
                arrayOf(
                    Status(hp = 10, motivation = -2).add(
                        training.type to -10,
                        *target
                    ) to failureRateValue
                )
            }
            else -> {
                arrayOf(Status(motivation = -1).add(training.type to -5) to failureRateValue)
            }
        }
        return Action.Training(
            state.status,
            training.type,
            failureRate,
            training.currentLevel,
            state.isLevelUpTurn,
            support,
            *successCandidate,
            *failureCandidate,
        )
    }

    private fun calcTrainingHint(support: List<MemberState>): List<Status> {
        val hintSupportList = support.filter { it.hint }
        if (hintSupportList.isEmpty()) return listOf(Status())
        val hintSupport = hintSupportList.random()
        val hintList = hintSupport.card.skills.filter { state.status.skillHint[it] != 5 }
            .map { Status(skillHint = mapOf(it to 1 + hintSupport.card.hintLevel)) }.toTypedArray()
        return listOf(*hintList, hintSupport.card.hintStatus)
    }

    private fun calcTrainingFailureRate(training: TrainingBase, support: List<MemberState>): Int {
        val base = (state.status.hp - state.status.maxHp) * (state.status.hp * 10 - training.failureRate) / 400.0
        val supported = base * support.map { it.card.failureRate }.fold(1.0) { acc, d -> acc * d }
        val supportedInRange = max(0, min(99, ceil(supported).toInt()))
        val conditioned = supportedInRange + arrayOf(
            "練習ベタ" to 2,
            "練習上手○" to -2,
            "小さなほころび" to 5,
            "大輪の輝き" to -2,
        ).sumOf { if (state.condition.contains(it.first)) it.second else 0 }
        return max(0, min(100, conditioned))
    }

    private fun calcTrainingRelation(charm: Boolean, support: List<MemberState>): Map<Int, Int> {
        return support.associate {
            it.index to if (it.card.type == StatusType.FRIEND) {
                if (charm) 6 else 4
            } else {
                (if (charm) 9 else 7) + (if (it.hint) {
                    if (charm) 7 else 5
                } else 0)
            }
        }
    }
}

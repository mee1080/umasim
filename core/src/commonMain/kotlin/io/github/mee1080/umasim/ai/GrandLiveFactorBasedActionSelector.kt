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
package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

@Suppress("unused")
class GrandLiveFactorBasedActionSelector(val option: Option = Option()) : ActionSelector {

    companion object {
        private const val DEBUG = false
        private const val DEBUG_LESSON = false

        val speed2Power1Wisdom2Friend1 = Option().copy(
            speedFactor = 1.4,
            staminaFactor = 0.2,
            powerFactor = 0.4,
            gutsFactor = 0.2,
            wisdomFactor = 0.5,
            hpFactor = 0.7,
            motivationFactor = 25.0,
            relationFactor = { type, rank, _ ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        1 -> 9.0
                        2 -> 10.9
                        else -> 14.7
                    }

                    else -> when (rank) {
                        1 -> 11.6
                        else -> 12.5
                    }
                }
            }
        )
    }

    @Serializable
    data class Option(
        val speedFactor: Double = 1.0,
        val staminaFactor: Double = 1.0,
        val powerFactor: Double = 1.0,
        val gutsFactor: Double = 0.4,
        val wisdomFactor: Double = 0.6,
        val skillPtFactor: Double = 0.4,
        val hpFactor: Double = 0.5,
        val motivationFactor: Double = 15.0,
        val relationFactor: (type: StatusType, rank: Int, count: Int) -> Double = { _: StatusType, rank: Int, count: Int ->
            when (count) {
                1 -> 3.0
                2 -> if (rank == 0) 5.5 else 7.0
                else -> when (rank) {
                    0 -> 7.0
                    1 -> 9.0
                    else -> 12.0
                }
            }
        },
    ) : ActionSelectorGenerator {
        override fun generateSelector() = GrandLiveFactorBasedActionSelector(this)
    }

    private var lastItemCheckedTurn = -1

    private var whistleCount = 5

    private var amuletCount = 5

    override fun init(state: SimulationState) {
        lastItemCheckedTurn = -1
        whistleCount = 5
        amuletCount = 5
    }

    override fun select(state: SimulationState, selection: List<Action>): Action {
        return selection
            .filterNot { it is Race }
            .maxByOrNull { calcScore(state, it) } ?: selection.first()
    }

    override fun selectWithItem(state: SimulationState, selection: List<Action>): SelectedAction {
        val liveStatus = state.liveStatus
        if (liveStatus != null) {
            if (DEBUG_LESSON) println("${state.turn}: ${state.status.performance} ${liveStatus.lessonSelection.joinToString { it.displayName }}")
            val period = LivePeriod.turnToPeriod(state.turn)
            val lesson = liveStatus.lessonSelection.mapNotNull {
                val rest = (state.status.performance!! - it.cost)
                if (rest.valid) it to rest else null
            }.minByOrNull {
                if (it.first is SongLesson) {
                    when (it.first.liveBonus) {
                        LiveBonus.FriendTraining10 -> 40000
                        LiveBonus.FriendTraining5 -> 30000
                        else -> {
                            val learnBonus = it.first.learnBonus
                            if (learnBonus is TrainingBonus) {
                                when (learnBonus.type) {
                                    StatusType.SPEED -> 18000 * learnBonus.value
                                    StatusType.SKILL -> 10000 * learnBonus.value
                                    StatusType.POWER -> 500
                                    else -> 0
                                }
                            } else 0
                        }
                    } + it.second.countOver(period.lessonPeriod.baseCost) * 1000 + it.second.totalValue
                } else {
                    it.second.countOver(period.lessonPeriod.baseCost) * 1000 + it.second.totalValue
                }
            }?.first
            if (lesson != null) {
                if (DEBUG_LESSON) println("purchase ${lesson.displayName}")
                return SelectedAction(lesson = lesson)
            }
        }
        return SelectedAction(action = select(state, selection))
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (DEBUG) println("${state.turn}: $action")
        val total = action.resultCandidate.sumOf { it.second }.toDouble()
        val score = action.resultCandidate.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            (calcScore(calcExpectedHintStatus(action) + it.first)) * it.second / total
        } + calcRelationScore(state, action)
        if (DEBUG) println("total $score")
        return score
    }

    private fun calcExpectedHintStatus(action: Action): ExpectedStatus {
        if (action !is Training) return ExpectedStatus()
        val target = action.member.filter { it.hint }.map { it.card.hintStatus }
        if (target.isEmpty()) return ExpectedStatus()
        val rate = 1.0 / target.size
        return target.fold(ExpectedStatus()) { acc, status -> acc.add(rate, status) }
    }

    private fun calcScore(status: StatusValues): Double {
        val score = status.speed.toDouble() * option.speedFactor +
                status.stamina.toDouble() * option.staminaFactor +
                status.power.toDouble() * option.powerFactor +
                status.guts.toDouble() * option.gutsFactor +
                status.wisdom.toDouble() * option.wisdomFactor +
                status.skillPt.toDouble() * option.skillPtFactor +
                status.hp.toDouble() * option.hpFactor +
                status.motivation.toDouble() * option.motivationFactor
        if (DEBUG) println("  $score $status")
        return score
    }

    private fun calcRelationScore(state: SimulationState, action: Action): Double {
        if (action !is Training) return 0.0
        val supportRank = mutableMapOf<StatusType, MutableList<Pair<MemberState, Int>>>()
        state.support.forEach {
            supportRank.getOrPut(it.card.type) { mutableListOf() }.add(it to it.relation)
        }
        supportRank.values.forEach { list -> list.sortByDescending { it.second } }
        val score = action.support.sumOf { support ->
            if (support.relation >= support.card.requiredRelation) return@sumOf 0.0
            val list = supportRank[support.card.type]!!
            val rank = list.indexOfFirst { it.first == support }
            option.relationFactor(support.card.type, rank, list.size)
        }
        if (DEBUG) println("  relation $score")
        return score
    }

    override fun toString(): String {
        return "ClimaxFactorBasedActionSelector $option"
    }
}
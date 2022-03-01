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

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

@Suppress("unused")
class ClimaxFactorBasedActionSelector(val option: Option = Option()) : ActionSelector {

    companion object {
        private const val DEBUG = false

    }

    @Serializable
    data class Option(
        val speedFactor: Double = 1.0,
        val staminaFactor: Double = 1.0,
        val powerFactor: Double = 1.0,
        val gutsFactor: Double = 0.0,
        val wisdomFactor: Double = 0.0,
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
        val expectedStatusFactor: Double = 0.0,
    ) {
        fun generateSelector() = ClimaxFactorBasedActionSelector(this)
    }

    override fun select(state: SimulationState, selection: List<Action>): Action {
        return selection.maxByOrNull { calcScore(state, it) } ?: selection.first()
    }

    override fun selectWithItem(state: SimulationState, selection: List<Action>): SelectedAction {
        if (state.possessionItem.isNotEmpty()) {
            return SelectedAction(null, state.possessionItem)
        }
        return SelectedAction(select(state, selection), null)
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (DEBUG) println("${state.turn}: $action")
        val total = action.resultCandidate.sumOf { it.second }.toDouble()
        val expected = calcExpectedScore(state, action)
        val score = action.resultCandidate.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            (calcScore(state, it.first) - expected) * it.second / total
        } + calcRelationScore(state, action)
        if (DEBUG) println("total $score")
        return score
    }

    private fun calcScore(state: SimulationState, status: Status): Double {
        val score = status.speed * option.speedFactor +
                status.stamina * option.staminaFactor +
                status.power * option.powerFactor +
                status.guts * option.gutsFactor +
                status.wisdom * option.wisdomFactor +
                status.skillPt * option.skillPtFactor +
                status.hp * option.hpFactor +
                status.motivation * option.motivationFactor
        if (DEBUG) println("  $score $status")
        return score
    }

    private fun calcExpectedScore(state: SimulationState, action: Action): Double {
        if (option.expectedStatusFactor <= 0.0) return 0.0
        return option.expectedStatusFactor * when (action) {
            is Training -> {
                val expectedStatus = Calculator.calcExpectedTrainingStatus(
                    state.chara,
                    state.getTraining(action.type).current,
                    state.status.motivation,
                    state.member,
                    state.scenario,
                    state.supportTypeCount,
                    state.status.fanCount,
                ).first
                expectedStatus.speed * option.speedFactor +
                        expectedStatus.stamina * option.staminaFactor +
                        expectedStatus.power * option.powerFactor +
                        expectedStatus.guts * option.gutsFactor +
                        expectedStatus.wisdom * option.wisdomFactor +
                        expectedStatus.skillPt * option.skillPtFactor +
                        expectedStatus.hp * option.hpFactor +
                        expectedStatus.motivation * option.motivationFactor
            }
            else -> 0.0
        }
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
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

import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

class FactorBasedActionSelector2(val option: Option = Option()) : ActionSelector {

    companion object {
        private const val DEBUG = false

        val speedPower = Option(
            speedFactor = 0.8,
            staminaFactor = 0.9,
            powerFactor = 0.9,
            hpFactor = 0.6,
        )

        val speedPowerMiddle = Option(
            speedFactor = 0.8,
            staminaFactor = 1.0,
            powerFactor = 1.2,
            hpFactor = 0.7,
        )

        val speedWisdom = Option(
            speedFactor = 0.6,
            staminaFactor = 1.2,
            powerFactor = 0.9,
            wisdomFactor = 0.7,
            hpFactor = 0.6,
        )

        val speedWisdomPower = Option(
            speedFactor = 0.9,
            staminaFactor = 0.9,
            powerFactor = 0.9,
            gutsFactor = 0.0,
            wisdomFactor = 0.8,
            hpFactor = 0.5,
        )

        val speedStamina = Option(
            speedFactor = 1.2,
            staminaFactor = 1.1,
            powerFactor = 0.5,
            gutsFactor = 0.6,
            hpFactor = 0.7,
        )

        val powerWisdom = Option(
            speedFactor = 0.9,
            staminaFactor = 1.1,
            powerFactor = 1.2,
            gutsFactor = 0.0,
            wisdomFactor = 0.6,
            skillPtFactor = 0.4,
            hpFactor = 0.7,
            motivationFactor = 15.0,
        )

        val speedGuts = Option(
            speedFactor = 1.2,
            staminaFactor = 0.8,
            powerFactor = 0.8,
            gutsFactor = 0.9,
            hpFactor = 0.7,
        )

        val speed2Power3Wisdom1 = Option(
            speedFactor = 1.2,
            staminaFactor = 1.0,
            powerFactor = 1.0,
            gutsFactor = 0.8,
            wisdomFactor = 1.0,
            skillPtFactor = 0.2,
            hpFactor = 0.8,
            motivationFactor = 25.0,
        )

        val aoharuSpeedWisdom = speedWisdom.let { it.copy(hpFactor = it.hpFactor * 0.9) }

        val aoharuPowerWisdom = Option(
            speedFactor = 1.2,
            staminaFactor = 1.2,
            powerFactor = 1.2,
            gutsFactor = 0.4,
            wisdomFactor = 1.0,
            skillPtFactor = 0.2,
            hpFactor = 0.8,
            motivationFactor = 25.0,
        )

        val aoharuSpeed2Power3Wisdom1 = Option(
            speedFactor = 1.0,
            staminaFactor = 0.6,
            powerFactor = 1.0,
            gutsFactor = 0.4,
            wisdomFactor = 0.8,
            skillPtFactor = 0.4,
            hpFactor = 0.8,
            motivationFactor = 25.0,
        )

        val aoharuSpeed2Stamina1Wisdom3 = Option(
            speedFactor = 1.2,
            staminaFactor = 1.2,
            powerFactor = 1.0,
            gutsFactor = 0.2,
            wisdomFactor = 0.8,
            skillPtFactor = 0.4,
            hpFactor = 0.8,
            motivationFactor = 25.0,
        )

        val aoharuSpeed2Power1Wisdom2Friend1 = Option(
            speedFactor = 1.2,
            staminaFactor = 1.2,
            powerFactor = 1.4,
            gutsFactor = 0.4,
            wisdomFactor = 0.6,
            skillPtFactor = 0.2,
            hpFactor = 0.8,
            motivationFactor = 25.0,
        )

        val aoharuSpeed2Power1Wisdom2Friend1Optuna = Option(
            speedFactor = 1.385,
            staminaFactor = 1.909,
            powerFactor = 1.606,
            gutsFactor = 0.395,
            wisdomFactor = 0.569,
            skillPtFactor = 0.494,
            hpFactor = 0.981,
            motivationFactor = 25.0,
        )

        val aoharuSpeed2Power1Wisdom2Friend1Optuna2 = Option(
            speedFactor = 1.305,
            staminaFactor = 1.660,
            powerFactor = 1.534,
            gutsFactor = 0.614,
            wisdomFactor = 0.715,
            skillPtFactor = 0.152,
            hpFactor = 0.938,
            motivationFactor = 25.0,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> if (rank == 0) 12.748 else 7.986
                    StatusType.POWER -> 11.139
                    StatusType.WISDOM -> if (rank == 0) 3.614 else 6.560
                    else -> 0.0
                }
            },
            aoharuFactor = { turn ->
                when {
                    turn <= 24 -> 9.068
                    turn <= 36 -> 12.353
                    turn <= 48 -> 0.001
                    else -> 4.544
                }
            },
        )
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
        val aoharuFactor: (Int) -> Double = {
            when {
                it <= 24 -> 15.0
                it <= 36 -> 10.0
                it <= 48 -> 5.0
                else -> 0.0
            }
        },
        val expectedStatusFactor: Double = 0.0,
    ) {
        fun generateSelector() = FactorBasedActionSelector2(this)
    }

    override fun select(state: SimulationState, selection: List<Action>): Action {
        return selection.maxByOrNull { calcScore(state, it) } ?: selection.first()
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (DEBUG) println("${state.turn}: $action")
        val total = action.resultCandidate.sumOf { it.second }.toDouble()
        val expected = calcExpectedScore(state, action)
        val score = action.resultCandidate.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            (calcScore(state, it.first) - expected) * it.second / total
        } + calcRelationScore(state, action) + calcAoharuScore(state, action)
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
            supportRank.getOrPut(it.card.type) { mutableListOf() }
                .add(it to (state.status.supportRelation[it.index] ?: 0))
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

    private fun calcAoharuScore(state: SimulationState, action: Action): Double {
        if (state.scenario != Scenario.AOHARU || action !is Training) return 0.0
        return action.member.mapNotNull { it.scenarioState as? AoharuMemberState }
            .sumOf {
                when {
                    it.aoharuBurn -> 2.0
                    it.aoharuIcon -> 1.0
                    else -> 0.0
                }
            } * option.aoharuFactor(state.turn)
    }

    override fun toString(): String {
        return "FactorBasedActionSelector2 $option"
    }
}
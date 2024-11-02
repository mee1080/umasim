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

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.*

@Suppress("unused")
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
            wisdomFactor = 0.8,
            skillPtFactor = 0.2,
            hpFactor = 0.7,
            motivationFactor = 25.0,
        )

        val uraMileSpeed3Wisdom2 = Option(
            speedFactor = 1.80,
            staminaFactor = 0.96,
            powerFactor = 1.09,
            gutsFactor = 0.20,
            wisdomFactor = 0.98,
            hpFactor = 1.13,
            skillPtFactor = 0.50,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> if (rank == 0) 17.0 else if (rank == 1) 16.7 else 19.0
                    StatusType.WISDOM -> if (rank == 0) 14.4 else 14.8
                    else -> 0.0
                }
            },
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
            aoharuFactor = { turn, _, _ ->
                when {
                    turn <= 24 -> 9.068
                    turn <= 36 -> 12.353
                    turn <= 48 -> 0.001
                    else -> 4.544
                }
            },
        )

        val aoharuSpeed2Power1Wisdom2Friend1Optuna3 = Option(
            speedFactor = 1.1008224827809614,
            staminaFactor = 1.2401307836111561,
            powerFactor = 1.460344961127203,
            gutsFactor = 0.4938925605556113,
            wisdomFactor = 0.3727392759658323,
            skillPtFactor = 0.8422039524664462,
            hpFactor = 0.9860660753314441,
            motivationFactor = 25.0,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> if (rank == 0) 16.222886230045777 else 1.586706618142665
                    StatusType.POWER -> 4.609934735798087
                    StatusType.WISDOM -> if (rank == 0) 14.910845068242013 else 7.334876620138475
                    else -> 0.0
                }
            },
            aoharuFactor = { turn, level, _ ->
                when {
                    level < 4 -> {
                        when {
                            turn <= 24 -> 10.901683848286718
                            turn <= 36 -> 12.317501510709448
                            turn <= 48 -> 13.68851304287568
                            else -> 9.434084503148606
                        }
                    }

                    level == 4 -> 7.895556162884356
                    else -> 0.0
                }
            },
        )

        val aoharuSpeed2Stamina1Power1Wisdom1Friend1Optuna = Option(
            gutsFactor = 0.8721424770847755,
            hpFactor = 1.190741489079833,
            powerFactor = 1.9397883814901953,
            skillPtFactor = 0.7175793855213755,
            speedFactor = 1.8405613002176744,
            staminaFactor = 1.919950102050362,
            wisdomFactor = 1.2035104585726557,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> if (rank == 0) 8.50318815446151 else 8.387078026660346
                    StatusType.POWER -> 8.44405062038027
                    StatusType.WISDOM -> if (rank == 0) 4.480003059916107 else 4.730957810641986
                    else -> 0.0
                }
            },
            aoharuFactor = { turn, _, _ ->
                when {
                    turn <= 24 -> 15.375249098470697
                    turn <= 36 -> 17.531913102005795
                    turn <= 48 -> 0.021897123503436644
                    else -> 1.8353582784678524
                }
            },
        )
    }

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
        val aoharuFactor: (turn: Int, level: Int, type: StatusType) -> Double = { turn, level, _ ->
            when {
                level < 4 -> {
                    when {
                        turn <= 24 -> 15.0
                        turn <= 36 -> 10.0
                        turn <= 48 -> 5.0
                        else -> 0.0
                    }
                }

                level == 4 -> 10.0
                else -> when {
                    turn <= 24 -> 3.0
                    turn <= 36 -> 2.0
                    turn <= 48 -> 1.0
                    else -> 0.0
                }
            }
        },
        val aoharuBurnFactor: (turn: Int, type: StatusType) -> Double = { _, _ -> 10.0 },
        val expectedStatusFactor: Double = 0.0,
    ) : ActionSelectorGenerator {
        override fun generateSelector() = FactorBasedActionSelector2(this)

        fun aoharuSettingToString(): String {
            return (1..78).joinToString("\n") { "$it ${aoharuFactor(it, 0, StatusType.SPEED)}" }
        }
    }

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        return selection.maxByOrNull { calcScore(state, it) } ?: selection.first()
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
//        if (action is Training) {
//            val nakayama = action.support.any { it.name == "[一天地六に身を任せ]ナカヤマフェスタ" }
//            println("${action.type} ${state.status.hp} ${action.failureRate} ${if (nakayama) "○" else "×"}")
//        }
        if (DEBUG) println("${state.turn}: $action")
        val total = action.candidates.sumOf { it.second }.toDouble()
        val expected = calcExpectedScore(state, action)
        val score = action.candidates.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            (calcScore(calcExpectedHintStatus(action) + it.first.status) - expected) * it.second / total
        } + calcRelationScore(state, action) + calcAoharuScore(state, action)
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

    private fun calcScore(status: ExpectedStatus): Double {
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
                    Calculator.CalcInfo(
                        state.chara,
                        state.getTraining(action.type).current,
                        state.status.motivation,
                        state.member,
                        state.scenario,
                        state.supportCount,
                        state.status.fanCount,
                        state.status,
                        state.totalRelation,
                        // TODO
                        0,
                        0,
                        0,
                        state.totalTrainingLevel,
                        state.isLevelUpTurn,
                        state.scenarioStatus,
                    ),
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

    private fun calcAoharuScore(state: SimulationState, action: Action): Double {
        if (state.scenario != Scenario.AOHARU || action !is Training) return 0.0
        return action.member.mapNotNull { it.scenarioState as? AoharuMemberState }
            .sumOf {
                when {
                    it.aoharuIcon -> option.aoharuFactor(state.turn, it.aoharuTrainingCount, action.type)
                    else -> 0.0
                }
            }
    }

    override fun toString(): String {
        return "FactorBasedActionSelector2 $option"
    }
}
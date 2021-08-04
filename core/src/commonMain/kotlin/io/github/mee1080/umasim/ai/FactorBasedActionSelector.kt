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
import io.github.mee1080.umasim.simulation.Action
import io.github.mee1080.umasim.simulation.ActionSelector
import io.github.mee1080.umasim.simulation.SimulationState

class FactorBasedActionSelector(private val option: Option = Option()) : ActionSelector {

    companion object {
        private const val DEBUG = false

        val speedPower = Option(
            speedFactor = 0.8,
            staminaFactor = 0.9,
            powerFactor = 0.9,
            hpFactor = 0.6,
        )

        val speedWisdom = Option(
            speedFactor = 0.6,
            staminaFactor = 1.2,
            powerFactor = 0.9,
            wisdomFactor = 0.7,
            hpFactor = 0.6,
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
    ) {
        fun generateSelector() = FactorBasedActionSelector(this)
    }

    override fun select(state: SimulationState): Action {
        return state.selection.maxByOrNull { calcScore(state, it) } ?: state.selection.first()
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (DEBUG) println("${state.turn}: $action")
        val total = action.resultCandidate.sumOf { it.second }.toDouble()
        val score = action.resultCandidate.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            calcScore(state, it.first) * it.second / total
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

    private fun calcRelationScore(state: SimulationState, action: Action): Double {
        if (action !is Action.Training) return 0.0
        val supportRank = mutableMapOf<StatusType, MutableList<Pair<Action.SupportInfo, Int>>>()
        state.supportInfo.forEach {
            supportRank.getOrPut(it.card.type) { mutableListOf() }
                .add(it to (state.status.supportRelation[it.index] ?: 0))
        }
        supportRank.values.forEach { list -> list.sortByDescending { it.second } }
        val score = action.support.sumOf { support ->
            if (support.friendTrainingEnabled) return@sumOf 0.0
            val list = supportRank[support.card.type]!!
            val rank = list.indexOfFirst { it.first == support }
            when (list.size) {
                1 -> 3.0
                2 -> if (rank == 0) 5.5 else 7.0
                else -> when (rank) {
                    0 -> 7.0
                    1 -> 9.0
                    else -> 12.0
                }
            }
        }
        if (DEBUG) println("  relation $score")
        return score
    }
}
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
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
class UafActionSelector(private val options: List<Option>) : ActionSelector {

    companion object {
        private const val DEBUG = false

        val speed3Stamina1Wisdom1Long = {
            UafActionSelector(speed3Stamina1Wisdom1LongOptions)
        }

        val speed3Stamina1Wisdom1LongOptions: List<Option>

        init {
            val domestic = Option(
                speedFactor = 1.5,
                staminaFactor = 1.7,
                powerFactor = 1.0,
                gutsFactor = 1.1,
                wisdomFactor = 0.9,
                skillPtFactor = 0.4,
                hpFactor = 1.1,
                motivationFactor = 15.0,
                relationFactor = 5.0,
                hpKeepFactor = 0.4,
                riskFactor = 1.6,
            )
            val overseas = domestic.copy(
                hpFactor = 1.7,
                hpKeepFactor = 2.2,
                riskFactor = 1.4,
            )
            val domestic2 = domestic.copy(
                hpKeepFactor = 2.0
            )
            val overseas2 = overseas.copy(
                hpKeepFactor = 0.2
            )
            speed3Stamina1Wisdom1LongOptions = listOf(domestic, overseas, domestic2, overseas2)
        }

        val speed3Power1Wisdom1Middle = {
            UafActionSelector(speed3Power1Wisdom1MiddleOptions)
        }

        val speed3Power1Wisdom1MiddleOptions: List<Option>

        init {
            val domestic = Option(
                speedFactor = 1.2,
                staminaFactor = 1.4,
                powerFactor = 1.2,
                gutsFactor = 1.4,
                wisdomFactor = 1.0,
                skillPtFactor = 0.4,
                hpFactor = 1.0,
                motivationFactor = 17.0,
                relationFactor = 4.5,
                hpKeepFactor = 0.2,
                riskFactor = 2.4,
            )
            val overseas = domestic.copy(
                hpFactor = 1.7,
                hpKeepFactor = 2.6,
                riskFactor = 1.4,
            )
            val domestic2 = domestic.copy(
                hpKeepFactor = 1.4
            )
            val overseas2 = overseas.copy(
                hpKeepFactor = 1.4
            )
            speed3Power1Wisdom1MiddleOptions = listOf(domestic, overseas, domestic2, overseas2)
        }

        val speed2Guts2Wisdom1Mile = {
            UafActionSelector(speed2Guts2Wisdom1MileOptions)
        }

        val speed2Guts2Wisdom1MileOptions: List<Option>

        init {
            val domestic = Option(
                speedFactor = 1.3,
                staminaFactor = 1.0,
                powerFactor = 1.2,
                gutsFactor = 1.2,
                wisdomFactor = 0.85,
                skillPtFactor = 0.4,
                hpFactor = 0.5,
                motivationFactor = 19.0,
                relationFactor = 4.0,
                hpKeepFactor = 1.2,
                riskFactor = 4.2,
            )
            val overseas = domestic.copy(
                hpFactor = 1.6,
                hpKeepFactor = 1.8,
                riskFactor = 9.2,
            )
            val domestic2 = domestic.copy(
                hpKeepFactor = 2.4
            )
            val overseas2 = overseas.copy(
                hpKeepFactor = 0.2
            )
            speed2Guts2Wisdom1MileOptions = listOf(domestic, overseas, domestic2, overseas2)
        }
    }

    data class Option(
        val speedFactor: Double = 1.0,
        val staminaFactor: Double = 1.0,
        val powerFactor: Double = 1.0,
        val gutsFactor: Double = 1.0,
        val wisdomFactor: Double = 1.0,
        val skillPtFactor: Double = 1.0,
        val hpFactor: Double = 1.6,
        val motivationFactor: Double = 15.0,
        val relationFactor: Double = 10.0,
        val hpKeepFactor: Double = 1.0,
        val riskFactor: Double = 2.0,
    ) : ActionSelectorGenerator {
        override fun generateSelector() = UafActionSelector(listOf(this))
    }

    private var option: Option = options[0]

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        option = options[0]
        // TODO 相談
        return selection.maxByOrNull { calcScore(state, it) } ?: selection.first()
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (!action.turnChange) return Double.MIN_VALUE
        if (DEBUG) println("${state.turn}: $action")
        val total = action.candidates.sumOf { it.second }.toDouble()
        val needHp = state.turn in listOf(35, 36, 58, 59)
        val score = action.candidates.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            val result = it.first as? StatusActionResult ?: return@sumOf 0.0
            val statusScore = calcScore(calcExpectedHintStatus(action) + result.status, needHp, state.status)
            val relationScore = if (result.success) calcRelationScore(state, action) else 0.0
            (statusScore + relationScore) * it.second / total
        }
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

    private fun calcScore(status: ExpectedStatus, needHp: Boolean, currentStatus: Status): Double {
        val score = status.speed * option.speedFactor +
                status.stamina * option.staminaFactor +
                status.power * option.powerFactor +
                status.guts * option.gutsFactor +
                status.wisdom * option.wisdomFactor +
                status.skillPt * option.skillPtFactor +
                status.hp * (if (needHp) option.hpFactor * 10 else option.hpFactor) +
                status.motivation * option.motivationFactor +
                min(0.0, max(-20.0, currentStatus.hp + status.hp - 70.0)) * option.hpKeepFactor
        if (DEBUG) println("  $score $status")
        return score * (if (score < 0) option.riskFactor else 1.0)
    }

    private fun calcRelationScore(state: SimulationState, action: Action): Double {
        if (action !is Training) return 0.0
        val score = option.relationFactor * action.support.count { it.relation < it.card.requiredRelation }
        if (DEBUG) println("  relation $score")
        return score
    }

    override fun toString(): String {
        return "UafActionSelector($option)"
    }
}
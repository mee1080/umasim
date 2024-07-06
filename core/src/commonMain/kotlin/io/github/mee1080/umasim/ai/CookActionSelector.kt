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
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
class CookActionSelector(private val options: List<Option>) : ActionSelector {

    companion object {
        private const val DEBUG = false

        val speed2Power1Guts1Wisdom1Mile = {
            CookActionSelector(speed2Power1Guts1Wisdom1MileOptions)
        }

        val speed2Power1Guts1Wisdom1MileOptions: List<Option>

        init {
            val option1 = Option(
                speedFactor = 1.0,
                staminaFactor = 1.0,
                powerFactor = 1.0,
                gutsFactor = 1.2,
                wisdomFactor = 1.4,
                skillPtFactor = 1.0,
                hpFactor = 0.6,
                motivationFactor = 17.0,
                relationFactor = 6.0,
                hpKeepFactor = 0.3,
                riskFactor = 8.2,
            )
            val option2 = option1.copy(
                hpFactor = 1.5,
                hpKeepFactor = 1.1,
                riskFactor = 7.4,
            )
            val option3 = option1.copy(
                hpFactor = 0.5,
                hpKeepFactor = 1.2,
                riskFactor = 3.2,
            )
            speed2Power1Guts1Wisdom1MileOptions = listOf(option1, option2, option3)
        }

        val speed2Stamina1Guts1Wisdom1Long = {
            CookActionSelector(speed2Stamina1Guts1Wisdom1LongOptions)
        }

        val speed2Stamina1Guts1Wisdom1LongOptions: List<Option>

        init {
            val option1 = Option(
                speedFactor = 1.0,
                staminaFactor = 1.2,
                powerFactor = 1.3,
                gutsFactor = 0.9,
                wisdomFactor = 1.1,
                skillPtFactor = 1.2,
                hpFactor = 1.1,
                motivationFactor = 19.0,
                relationFactor = 8.0,
                hpKeepFactor = 0.7,
                riskFactor = 3.4,
            )
            val option2 = option1.copy(
                hpFactor = 0.8,
                hpKeepFactor = 0.7,
                riskFactor = 8.8,
            )
            val option3 = option1.copy(
                hpFactor = 2.0,
                hpKeepFactor = 0.1,
                riskFactor = 1.0,
            )
            speed2Stamina1Guts1Wisdom1LongOptions = listOf(option1, option2, option3)
        }
    }

    @Serializable
    data class Option(
        val speedFactor: Double = 1.0,
        val staminaFactor: Double = 1.0,
        val powerFactor: Double = 1.0,
        val gutsFactor: Double = 0.8,
        val wisdomFactor: Double = 1.0,
        val skillPtFactor: Double = 1.0,
        val hpFactor: Double = 1.0,
        val motivationFactor: Double = 10.0,
        val relationFactor: Double = 8.0,
        val hpKeepFactor: Double = 0.3,
        val riskFactor: Double = 2.0,
    ) : ActionSelectorGenerator {
        override fun generateSelector() = CookActionSelector(listOf(this))
    }

    private var option: Option = options[0]

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        return selectWithScore(state, selection).first
    }

    override suspend fun selectWithScore(state: SimulationState, selection: List<Action>): Pair<Action, List<Double>> {
        option = when {
            state.turn <= 24 -> options[0]
            state.turn <= 48 -> options.getOrElse(1) { options[0] }
            else -> options.getOrElse(2) { options[0] }
        }
        val expectedScore = calcExpectedScores(state)
//        val expectedScore = mapOf(StatusType.SKILL to 0.0)
        val selectionWithScore = selection.map { it to calcScore(state, it, expectedScore) }
        val actionScores = selectionWithScore.map { it.second }
        // 半年の最終ターンでは相談を選ぶ
        var selected = selectionWithScore.maxByOrNull { it.second }?.first ?: selection.first()
        if (selected is Sleep) {
            selected = selection.firstOrNull { it is Outing && it.support != null } ?: selected
        }
        return selected to actionScores
    }

    private var expectedStatusCache: Pair<Int, Double>? = null

    private fun calcExpectedScores(state: SimulationState): Double {
        val expectedStatusCacheNonNull = expectedStatusCache
        if (expectedStatusCacheNonNull != null) {
            if ((state.turn - 1) / 12 == expectedStatusCacheNonNull.first) {
                return expectedStatusCacheNonNull.second
            }
        }
        val max = trainingType.maxOf {
            val expectedStatus = Calculator.calcExpectedTrainingStatus(
                state.baseCalcInfo,
                noCache = true,
            ).first
            calcScore(expectedStatus, state.status)
        }
        expectedStatusCache = ((state.turn - 1) / 12) to max
        return max
    }

    private fun calcScore(state: SimulationState, action: Action, expectedScore: Double): Double {
        if (!action.turnChange) return Double.MIN_VALUE
        if (DEBUG) println("${state.turn}: $action")
        val total = action.candidates.sumOf { it.second }.toDouble()
        val score = action.candidates.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            val result = it.first as? StatusActionResult ?: return@sumOf 0.0
            val statusScore = calcScore(calcExpectedHintStatus(action) + result.status, state.status)
            val expectedStatusScore = when (action) {
                is Training -> expectedScore
                is Race -> expectedScore
                else -> 0.0
            }
            val relationScore = if (result.success) calcRelationScore(state, action) else 0.0
            (statusScore - expectedStatusScore + relationScore) * it.second / total
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

    private fun calcScore(status: ExpectedStatus, currentStatus: Status): Double {
        val score = status.speed * option.speedFactor +
                status.stamina * option.staminaFactor +
                status.power * option.powerFactor +
                status.guts * option.gutsFactor +
                status.wisdom * option.wisdomFactor +
                status.skillPt * option.skillPtFactor +
                status.hp * option.hpFactor +
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
        return "CookActionSelector($option)"
    }
}
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
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
class UafActionSelector(private val options: List<Option>) : ActionSelector {

    companion object {
        private const val DEBUG = true

        val speed2Power1Guts1Wisdom1Long = {
            UafActionSelector(speed2Power1Guts1Wisdom1LongOptions)
        }

        val speed2Power1Guts1Wisdom1LongOptions: List<Option>

        init {
            val option = Option(
                speedFactor = 1.0,
                staminaFactor = 1.0,
                powerFactor = 1.0,
                gutsFactor = 0.8,
                wisdomFactor = 1.0,
                skillPtFactor = 1.0,
                hpFactor = 0.5,
                motivationFactor = 15.0,
                relationFactor = 8.0,
                hpKeepFactor = 0.4,
                riskFactor = 1.4,
                athleticBaseFactor = 3.0,
                athleticRequiredFactor = 3.0,
                athleticBonusFactor = 20.0,
            )
            speed2Power1Guts1Wisdom1LongOptions = listOf(option)
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
        val hpFactor: Double = 1.2,
        val motivationFactor: Double = 15.0,
        val relationFactor: Double = 8.0,
        val hpKeepFactor: Double = 0.3,
        val riskFactor: Double = 2.0,
        val athleticBaseFactor: Double = 2.5,
        val athleticRequiredFactor: Double = 2.0,
        val athleticBonusFactor: Double = 50.0,
    ) : ActionSelectorGenerator {
        override fun generateSelector() = UafActionSelector(listOf(this))
    }

    private var option: Option = options[0]

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        return selectWithScore(state, selection).first
    }

    override suspend fun selectWithScore(state: SimulationState, selection: List<Action>): Pair<Action, List<Double>> {
        option = options[0]
        // TODO 相談
        val expectedScore = calcExpectedScores(state)
        val selectionWithScore = selection.map { it to calcScore(state, it, expectedScore) }
        var selected = selectionWithScore.maxByOrNull { it.second }?.first ?: selection.first()
        if (selected is Sleep) {
            selected = selection.firstOrNull { it is Outing && it.support != null } ?: selected
        }
        return selected to selectionWithScore.map { it.second }
    }

    private fun calcExpectedScores(state: SimulationState) = buildMap {
        val uafStatus = state.uafStatus ?: return@buildMap
        val scores = mutableListOf<Double>()
        trainingType.forEach {
            val expectedStatus = Calculator.calcExpectedTrainingStatus(
                state.baseCalcInfo.copy(training = uafStatus.getTraining(it, state.isLevelUpTurn))
            ).first
            val score = calcScore(expectedStatus, state.status)
            scores += score
            put(it, score)
        }
        put(StatusType.SKILL, scores.max())
    }

    private fun calcScore(state: SimulationState, action: Action, expectedScore: Map<StatusType, Double>): Double {
        if (!action.turnChange) return Double.MIN_VALUE
        if (DEBUG) println("${state.turn}: $action")
        val total = action.candidates.sumOf { it.second }.toDouble()
        val score = action.candidates.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            val result = it.first as? StatusActionResult ?: return@sumOf 0.0
            val statusScore = calcScore(calcExpectedHintStatus(action) + result.status, state.status)
            val expectedStatusScore = when (action) {
                is Training -> expectedScore[action.type]!!
                is Race -> expectedScore[StatusType.SKILL]!!
                else -> 0.0
            }
            val relationScore = if (result.success) calcRelationScore(state, action) else 0.0
            val athleticScore = if (result.success) calcAthleticScore(state, action, result) else 0.0
            (statusScore - expectedStatusScore + relationScore + athleticScore) * it.second / total
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

    private fun calcAthleticScore(state: SimulationState, action: Action, result: StatusActionResult): Double {
        val uafStatus = state.uafStatus ?: return 0.0
        val scenarioActionParam = result.scenarioActionParam as? UafScenarioActionParam ?: return 0.0
        val score = if (action is Training) {
            val needWinCount = when {
                state.turn > 72 -> 0
                state.turn > 60 -> 50
                state.turn > 48 -> 40
                state.turn > 36 -> 30
                state.turn > 24 -> 20
                else -> 10
            }
            scenarioActionParam.athleticsLevelUp.map { (type, value) ->
                val athletic = uafStatus.trainingAthletics[type]!!
                val level = uafStatus.athleticsLevel[athletic]!!
                value * option.athleticBaseFactor + max(0, needWinCount - level) * option.athleticRequiredFactor
            }.sum()
        } else if (!uafStatus.levelUpBonus && scenarioActionParam.notTraining) {
            option.athleticBonusFactor
        } else 0.0
        if (DEBUG) println("  athletic $score")
        return score
    }

    override fun toString(): String {
        return "UafActionSelector($option)"
    }
}
/*
 * Copyright 2024 mee1080
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
import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
abstract class BaseActionSelector<Option : BaseActionSelector.BaseOption, Context : BaseActionSelector.BaseContext<Option>> :
    ActionSelector {

    companion object {
        private const val DEBUG = false
    }

    abstract fun getContext(state: SimulationState): Context

    interface BaseOption : SerializableActionSelectorGenerator {
        val speedFactor: Double
        val staminaFactor: Double
        val powerFactor: Double
        val gutsFactor: Double
        val wisdomFactor: Double
        val skillPtFactor: Double
        val hpFactor: Double
        val motivationFactor: Double
        val relationFactor: Double
        val outingRelationFactor: Double
        val hpKeepFactor: Double
        val riskFactor: Double
    }

    open class BaseContext<Option : BaseOption>(
        val option: Option,
        val state: SimulationState,
    ) {
        operator fun component1() = option
        operator fun component2() = state

        private val _selectionWithScore = mutableListOf<Pair<Action, Double>>()
        val selectionWithScore: List<Pair<Action, Double>> get() = _selectionWithScore

        var maxAction: Pair<Action, Double>? = null
            private set

        var maxTurnChangeAction: Pair<Action, Double>? = null
            private set

        fun add(action: Action, score: Double) {
            _selectionWithScore += action to score
            if (score > (maxAction?.second ?: Double.MIN_VALUE)) {
                maxAction = action to score
            }
            if (action.turnChange && score > (maxTurnChangeAction?.second ?: Double.MIN_VALUE)) {
                maxTurnChangeAction = action to score
            }
        }
    }

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        return selectWithScore(state, selection).first
    }

    override suspend fun selectWithScore(state: SimulationState, selection: List<Action>): Pair<Action, List<Double>> {
        val context = getContext(state)
        val expectedScore = calcExpectedScores(context)
        selection.forEach {
            context.add(it, calcScore(context, it, expectedScore))
        }
        val actionScores = context.selectionWithScore.map { it.second }
        var selected = context.selectionWithScore.maxByOrNull { it.second }?.first ?: selection.first()
        if (selected is Sleep) {
            selected = selection.firstOrNull { it is Outing && it.support != null } ?: selected
        }
        return selected to actionScores
    }

    private var expectedStatusCache: Pair<Int, Double>? = null

    private fun calcExpectedScores(context: Context): Double {
        val (option, state) = context
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
            calcScore(option, expectedStatus, state.status)
        }
        expectedStatusCache = ((state.turn - 1) / 12) to max
        return max
    }

    private fun calcScore(context: Context, action: Action, expectedScore: Double): Double {
        val scenarioActionScore = calcScenarioActionScore(context, action, expectedScore)
        if (scenarioActionScore != null) return scenarioActionScore
        if (!action.turnChange) return Double.MIN_VALUE
        val (option, state) = context
        val hpKeepFactor = if (state.turn == 36) 20.0 * option.hpKeepFactor else option.hpKeepFactor
        if (DEBUG) println("${state.turn}: $action")
        val total = action.candidates.sumOf { it.second }.toDouble()
        val score = action.candidates.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            val result = it.first as? StatusActionResult ?: return@sumOf 0.0
            var score = calcScore(option, calcExpectedHintStatus(action) + result.status, state.status, hpKeepFactor)
            score -= when (action) {
                is Training -> expectedScore
                is Race -> expectedScore
                else -> 0.0
            }
            if (result.success) {
                score += calcRelationScore(option, action)
            }
            score += actionParamScore(option, result.scenarioActionParam)
            score * it.second / total
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

    private fun calcScore(
        option: Option,
        status: ExpectedStatus,
        currentStatus: Status,
        hpKeepFactor: Double = option.hpKeepFactor,
    ): Double {
        val score = status.speed * option.speedFactor +
                status.stamina * option.staminaFactor +
                status.power * option.powerFactor +
                status.guts * option.gutsFactor +
                status.wisdom * option.wisdomFactor +
                status.skillPt * option.skillPtFactor +
                status.hp * option.hpFactor +
                status.motivation * option.motivationFactor +
                min(0.0, max(-20.0, currentStatus.hp + status.hp - 70.0)) * hpKeepFactor
        if (DEBUG) println("  $score $status")
        return score * (if (score < 0) option.riskFactor else 1.0)
    }

    private fun calcRelationScore(option: Option, action: Action): Double {
        if (action !is Training) return 0.0
        val score = action.support.sumOf {
            when {
                it.relation >= it.card.requiredRelation -> 0.0
                it.outingType -> option.outingRelationFactor
                else -> option.relationFactor
            }
        }
        if (DEBUG) println("  relation $score")
        return score
    }

    protected open fun actionParamScore(option: Option, scenarioActionParam: ScenarioActionParam?): Double {
        return 0.0
    }

    protected open fun calcScenarioActionScore(context: Context, action: Action, expectedScore: Double): Double? {
        return null
    }
}
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
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.min

@Suppress("unused")
abstract class BaseActionSelector2<Option : BaseActionSelector2.BaseOption, Context : BaseActionSelector2.BaseContext<Option>>
    : ActionSelector {

    companion object {
        private const val DEBUG = false
    }

    abstract fun getContext(state: SimulationState): Context

    interface BaseOption : SerializableActionSelectorGenerator {
        val speed: Int
        val stamina: Int
        val power: Int
        val guts: Int
        val wisdom: Int

        val training: Int
        val hp: Int
        val hpKeep: Int
        val motivation: Int
        val risk: Int

        val relation: Int
        val outingRelation: Int

        val friend: Int
        val friendCount: Int
        val supportCount: Int
        val guestCount: Int
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

    override suspend fun selectWithScore(
        state: SimulationState,
        selection: List<Action>,
    ): Triple<Action, List<Double>, Double> {
        val context = getContext(state)
        selection.forEach {
            context.add(it, calcScore(context, it))
        }
        val actionScores = context.selectionWithScore.map { it.second }
        var selected = context.selectionWithScore.maxByOrNull { it.second } ?: (selection.first() to 0.0)
        if (selected.first is Sleep) {
            val supportOuting = selection.firstOrNull { it is Outing && it.support != null }
            if (supportOuting != null) {
                selected = selected.copy(first = supportOuting)
            }
        }
        return Triple(selected.first, actionScores, selected.second)
    }

    private suspend fun calcScore(context: Context, action: Action): Double {
        val scenarioActionScore = calcScenarioActionScore(context, action)
        if (scenarioActionScore != null) return scenarioActionScore
        return calcBaseScore(context, action)
    }

    fun calcBaseScore(context: Context, action: Action): Double {
        if (action is FriendAction) return calcFriendActionScore(context, action)
        if (!action.turnChange) return Double.MIN_VALUE
        if (DEBUG) println("${context.state.turn}: ${action.toShortString()}")
        val score = if (action is Training) {
            calcTrainingBaseScore(context, action)
        } else {
            calcCandidatesScore(context, action)
        }
        if (DEBUG) println("total $score")
        return score
    }

    protected open fun calcTrainingBaseScore(context: Context, action: Training): Double {
        val (option, _) = context
        var score = option.training.toDouble()
        if (action.friendTraining) {
            score += option.friend
        }
        val support = action.support
        score += support.count { it.isFriendTraining(action.type) } * option.friendCount
        score += support.count() * option.supportCount
        score += (action.member.count() - support.count()) * option.guestCount
        score -= action.failureRate * option.risk
        if (DEBUG) println("  base $score")
        score += calcCandidatesScore(context, action)
        score += calcTrainingAdditionalScore(context, action)
        score *= when (action.type) {
            StatusType.SPEED -> option.speed
            StatusType.STAMINA -> option.stamina
            StatusType.POWER -> option.power
            StatusType.GUTS -> option.guts
            StatusType.WISDOM -> option.wisdom
            else -> 100
        } / 100.0
        return score
    }

    protected open fun calcTrainingAdditionalScore(context: Context, action: Training): Double {
        return 0.0
    }

    protected fun calcCandidatesScore(
        context: Context,
        action: Action,
    ): Double {
        val totalRate = action.candidates.sumOf { it.second }.toDouble()
        val (option, state) = context
        return action.candidates.sumOf { (result, rate) ->
            var subScore = 0.0
            subScore += min(state.status.maxHp - state.status.hp, result.status.hp) * option.hp
            subScore += min(2 - state.status.motivation, result.status.motivation) * option.motivation
            subScore += min(0, state.status.hp + result.status.hp - 50) * option.hpKeep
            result.scenarioActionParam?.let {
                subScore += calcScenarioActionParamScore(context, action, it)
            }
            if (result.success) {
                subScore += calcRelationScore(option, action)
            }
            if (DEBUG) println("  $rate $subScore $result")
            subScore * rate / totalRate
        }
    }

    private fun calcExpectedHintStatus(action: Action): ExpectedStatus {
        if (action !is Training) return ExpectedStatus()
        val target = action.member.filter { it.hint }.map { it.card.hintStatus }
        if (target.isEmpty()) return ExpectedStatus()
        val rate = 1.0 / target.size
        return target.fold(ExpectedStatus()) { acc, status -> acc.add(rate, status) }
    }

    private fun calcRelationScore(option: Option, action: Action): Double {
        if (action !is Training) return 0.0
        val score = action.support.sumOf {
            val relationUp = min(7 + (if (it.hint) 5 else 0), it.card.requiredRelation - it.relation)
            if (relationUp <= 0) 0 else {
                if (it.outingType) option.outingRelation * relationUp else option.relation * relationUp
            }
        }.toDouble()
        if (DEBUG) println("  relation $score")
        return score
    }

    protected open fun calcScenarioActionParamScore(
        context: Context,
        action: Action,
        scenarioActionParam: ScenarioActionParam,
    ): Double {
        return 0.0
    }

    protected open suspend fun calcScenarioActionScore(context: Context, action: Action): Double? {
        return null
    }

    protected fun calcFriendActionScore(context: Context, action: FriendAction): Double {
        val state = context.state
        val result = action.result
        if (result.status.motivation > 0) {
            if (state.status.motivation < 2) {
                return 3000.0
            }
        }
        if (result.otherRelation > 0) {
            if (state.support.any { it.relation < it.card.requiredRelation }) {
                return 2000.0
            }
        }
        if (result.status.hp > 0) {
            if (state.status.hp > 0) {
                return 1000.0
            }
        }
        return result.status.totalPlusSkillPt.toDouble()
    }
}

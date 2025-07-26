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
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.min

@Suppress("unused")
abstract class BaseActionSelector3<Option : BaseActionSelector3.BaseOption, Context : BaseActionSelector3.BaseContext<Option>> :
    ActionSelector {

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
        val skillPt: Int
        val hp: Int
        val motivation: Int
        val relation: Int
        val outingRelation: Int
        val hpKeep: Int
        val risk: Int
        val maxSleep: Int
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
        val selected = selectFromScore(context)
        return Triple(selected.first, actionScores, selected.second)
    }

    protected open fun selectFromScore(context: Context): Pair<Action, Double> {
        val (_, state) = context
        var selected = context.selectionWithScore.maxBy { it.second }

        // レースで体力50以下になる場合はお休み
        (selected.first as? Race)?.let { race ->
            if (state.status.hp + race.result.status.hp <= 50) {
                context.selectionWithScore.firstOrNull { it.first is Sleep }?.let {
                    selected = it
                }
            }
        }

        // お休みで友人お出かけが可能な場合は友人お出かけ
        if (selected.first is Sleep) {
            val supportOuting = context.selectionWithScore.firstOrNull {
                (it.first as? Outing)?.support != null
            }
            if (supportOuting != null) {
                selected = supportOuting
            }
        }
        return selected
    }

    private suspend fun calcScore(context: Context, action: Action): Double {
        val scenarioActionScore = calcScenarioActionScore(context, action)
        if (scenarioActionScore != null) return scenarioActionScore
        return calcBaseScore(context, action)
    }

    fun calcBaseScore(context: Context, action: Action): Double {
        if (!action.turnChange) return Double.MIN_VALUE
        val (option, state) = context
        val hpKeepFactor = if (state.turn == 36) 20 * option.hpKeep else option.hpKeep
        if (DEBUG) println("${state.turn}: ${action.toShortString()}")
        val total = action.candidates.sumOf { it.second }.toDouble()
        val score = action.candidates.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            val result = it.first as? StatusActionResult ?: return@sumOf 0.0
            var score = calcScore(option, calcExpectedHintStatus(action) + result.status, state.status, hpKeepFactor)
            if (result.success) {
                score += calcRelationScore(option, action)
            }
            score += calcScenarioScore(context, action, result.scenarioActionParam)
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
        hpKeepFactor: Int,
    ): Double {
        val score = status.speed * option.speed +
                status.stamina * option.stamina +
                status.power * option.power +
                status.guts * option.guts +
                status.wisdom * option.wisdom +
                status.skillPt * option.skillPt +
                status.hp * option.hp +
                status.motivation * option.motivation +
                min(0.0, currentStatus.hp + status.hp - 50.0) * hpKeepFactor +
                if (currentStatus.hp + status.hp <= currentStatus.maxHp) option.maxSleep else 0
        if (DEBUG) println("  $score $status")
        return score * (if (score < 0) option.risk / 100.0 else 1.0)
    }

    private fun calcRelationScore(option: Option, action: Action): Double {
        if (action !is Training) return 0.0
        val score = action.support.sumOf {
            when {
                it.relation >= it.card.requiredRelation -> 0
                it.outingType -> option.outingRelation
                else -> option.relation
            }
        }.toDouble()
        if (DEBUG) println("  relation $score")
        return score
    }

    /**
     * 通常アクションのシナリオ固有スコア計算
     */
    protected open fun calcScenarioScore(
        context: Context,
        action: Action,
        scenarioActionParam: ScenarioActionParam?,
    ): Double {
        return 0.0
    }

    /**
     * シナリオ固有アクションのスコア計算
     */
    protected open suspend fun calcScenarioActionScore(context: Context, action: Action): Double? {
        return null
    }
}

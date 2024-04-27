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

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
class UafActionSelector(private val options: List<Option>) : ActionSelector {

    companion object {
        private const val DEBUG = false

        val speed2Power1Guts1Wisdom1Mile = {
            UafActionSelector(speed2Power1Guts1Wisdom1MileOptions)
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
                athleticBaseFactor = 2.8,
                athleticRequiredFactor = 1.4,
                athleticBonusFactor = 40.0,
                consultMinScore = 36.0,
                consultAthleticRequiredFactor = 2.0,
                consultHeatUpStatusFactor = 0.2,
            )
            val option2 = option1.copy(
                hpFactor = 1.5,
                hpKeepFactor = 1.1,
                riskFactor = 7.4,
                athleticBaseFactor = 3.2,
                athleticRequiredFactor = 4.0,
                athleticBonusFactor = 20.0,
            )
            val option3 = option1.copy(
                hpFactor = 0.5,
                hpKeepFactor = 1.2,
                riskFactor = 3.2,
                athleticBaseFactor = 3.6,
                athleticRequiredFactor = 3.6,
                athleticBonusFactor = 40.0,
            )
            speed2Power1Guts1Wisdom1MileOptions = listOf(option1, option2, option3)
        }

        val speed2Stamina1Guts1Wisdom1Long = {
            UafActionSelector(speed2Stamina1Guts1Wisdom1LongOptions)
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
                athleticBaseFactor = 4.4,
                athleticRequiredFactor = 1.2,
                athleticBonusFactor = 45.0,
                consultMinScore = 38.0,
                consultAthleticRequiredFactor = 1.8,
                consultHeatUpStatusFactor = 0.2,
                keepRedHeatUpFactor = 1.1,
            )
            val option2 = option1.copy(
                hpFactor = 0.8,
                hpKeepFactor = 0.7,
                riskFactor = 8.8,
                athleticBaseFactor = 2.6,
                athleticRequiredFactor = 2.6,
                athleticBonusFactor = 20.0,
            )
            val option3 = option1.copy(
                hpFactor = 2.0,
                hpKeepFactor = 0.1,
                riskFactor = 1.0,
                athleticBaseFactor = 3.8,
                athleticRequiredFactor = 1.8,
                athleticBonusFactor = 25.0,
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
        val athleticBaseFactor: Double = 2.5,
        val athleticRequiredFactor: Double = 2.0,
        val athleticBonusFactor: Double = 40.0,
        val consultMinScore: Double = 25.0,
        val consultAthleticRequiredFactor: Double = 0.5,
        val consultHeatUpStatusFactor: Double = 0.5,
        val keepRedHeatUpFactor: Double = 1.0,
    ) : ActionSelectorGenerator {
        override fun generateSelector() = UafActionSelector(listOf(this))
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
        val noConsultScore = selection.map { it to calcScore(state, it, expectedScore) }
        val consultScore = calcConsultScore(state, selection, expectedScore, noConsultScore)
        val selectionWithScore = noConsultScore.map {
            if (it.first is UafConsult) it.first to consultScore[it.first]!! else it
        }
        val actionScores = selectionWithScore.map { it.second }
        val consultMax = selectionWithScore.filter { it.first is UafConsult }.maxByOrNull { it.second }
        // 半年の最終ターンでは相談を選ぶ
        val consultMinScore = if (state.turn % 12 == 0) 0.0 else option.consultMinScore
        if (consultMax != null && consultMax.second >= consultMinScore) {
            return consultMax.first to actionScores
        }
        var selected = selectionWithScore.maxByOrNull { it.second }?.first ?: selection.first()
        if (selected is Sleep) {
            selected = selection.firstOrNull { it is Outing && it.support != null } ?: selected
        }
        return selected to actionScores
    }

    private var expectedStatusCache: Pair<Int, Double>? = null

    private fun calcExpectedScores(state: SimulationState): Double {
        val uafStatus = state.uafStatus ?: return 0.0
        val expectedStatusCacheNonNull = expectedStatusCache
        if (expectedStatusCacheNonNull != null) {
            if ((state.turn - 1) / 12 == expectedStatusCacheNonNull.first) {
                return expectedStatusCacheNonNull.second
            }
        }
        val max = trainingType.maxOf {
            val expectedStatus = Calculator.calcExpectedTrainingStatus(
                state.baseCalcInfo.copy(training = uafStatus.getTraining(it, state.isLevelUpTurn)),
                noCache = true,
            ).first
            calcScore(expectedStatus, state.status)
        }
        expectedStatusCache = ((state.turn - 1) / 12) to max
        return max
    }

    private fun calcScore(state: SimulationState, action: Action, expectedScore: Double): Double {
        if (action is UafConsult) return 0.0
        if (!action.turnChange) return Double.MIN_VALUE
        if (DEBUG) println("${state.turn}: $action")
        val total = action.candidates.sumOf { it.second }.toDouble()
        val heatUpRed = state.uafStatus!!.heatUp[UafGenre.Red]!! > 0
        val score = action.candidates.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            val result = it.first as? StatusActionResult ?: return@sumOf 0.0
            val statusScore = calcScore(calcExpectedHintStatus(action) + result.status, state.status)
            val expectedStatusScore = when (action) {
                is Training -> expectedScore * (if (heatUpRed) option.keepRedHeatUpFactor else 1.0)
                is Race -> expectedScore
                else -> 0.0
            }
            val relationScore = if (result.success) calcRelationScore(state, action) else 0.0
            val athleticScore = if (result.success) calcAthleticScore(state, action, result) else 0.0
            (statusScore - expectedStatusScore + relationScore + athleticScore) * it.second / total
        }
        if (DEBUG) println("total $score")
        return score
    }

    private fun calcConsultScore(
        state: SimulationState,
        selection: List<Action>,
        expectedScore: Double,
        selectionWithScore: List<Pair<Action, Double>>,
    ): Map<UafConsult, Double> {
        val uafStatus = state.uafStatus ?: return emptyMap()
        val consultList = selection.filterIsInstance<UafConsult>()
        if (consultList.isEmpty()) return emptyMap()

        val consultResults = buildMap {
            UafGenre.entries.forEach { from ->
                UafGenre.entries.forEach { to ->
                    put(from to to, calcConsultResult(state, from, to))
                }
            }
        }
        val heatUpRed = uafStatus.heatUp[UafGenre.Red]!! > 0
        val trainingScores = if (heatUpRed) {
            selectionWithScore.filter { it.first is Training }
                .associateBy { (it.first as Training).type }
        } else emptyMap()
        val maxTrainingScore = if (heatUpRed) {
            trainingScores.maxOf { it.value.second }
        } else 0.0

        return consultList.associateWith { action ->
            val consultResult = consultResults[action.result.from to action.result.to]!!
            val noConsultResult = consultResults[action.result.from to action.result.from]!!
            val reverseResult = consultResults[action.result.to to action.result.from]!!
            // 基本競技Lvスコア：相談による競技Lv上昇量（色が逆のパターンと比較して小さい方）×係数
            val baseScore = min(consultResult.first, reverseResult.first) * option.athleticBaseFactor
            // 必要競技Lvスコア：相談によって得られる次回大会までの不足分（相談しないパターンとの差）×係数
            val requiredScore = (consultResult.second - noConsultResult.second) * option.consultAthleticRequiredFactor
            // 赤ヒートアップステータススコア
            val statusScore = if (heatUpRed) {
                val newState = state.applySelectedUafAction(action.result)
                trainingType.maxOf { type ->
                    val newTraining = newState.uafStatus!!.getTraining(type, newState.isLevelUpTurn)
                    val currentTraining = trainingScores[type]!!.first as Training
                    val newAction = newState.predictUafScenarioActionParams(
                        listOf(newState.calcTrainingResult(newTraining, currentTraining.member))
                    ).first()
                    val newScore = calcScore(newState, newAction, expectedScore)
                    newScore - maxTrainingScore
                } * option.consultHeatUpStatusFactor
            } else 0.0
            baseScore + requiredScore + statusScore
        }
    }

    private fun calcConsultResult(state: SimulationState, from: UafGenre, to: UafGenre): Pair<Int, Int> {
        val uafStatus = state.uafStatus ?: return 0 to 0
        val needWinCount = uafNeedWinCount(state.turn)
        val levelUp = uafStatus.athleticsLevelUp.filter {
            uafStatus.trainingAthletics[it.key]!!.genre == from
        }
        // 相談による競技Lv上昇量合計
        val total = levelUp.entries.sumOf { (_, value) -> value }
        // 上昇量のうち次回大会までの不足分
        val required = levelUp.entries.sumOf { (type, value) ->
            val athletic = UafAthletic.get(to, type)
            val level = uafStatus.athleticsLevel[athletic]!!
            min(value, max(0, needWinCount - level))
        }
        return total to required
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
            val needWinCount = uafNeedWinCount(state.turn)
            scenarioActionParam.athleticsLevelUp.map { (type, value) ->
                val athletic = uafStatus.trainingAthletics[type]!!
                val level = uafStatus.athleticsLevel[athletic]!!
                value * option.athleticBaseFactor + min(
                    value,
                    max(0, needWinCount - level)
                ) * option.athleticRequiredFactor
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
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
import io.github.mee1080.umasim.data.LArcAptitude
import io.github.mee1080.umasim.data.LArcMemberState
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.min

@Suppress("unused")
class LArcActionSelector(
    private val domesticOption1: Option = Option(),
    private val overseasOption1: Option = domesticOption1,
    private val domesticOption2: Option = domesticOption1,
    private val overseasOption2: Option = overseasOption1,
) : ActionSelector {

    companion object {
        private const val DEBUG = false

        val speed3Stamina1Wisdom1Long = Option(
            speedFactor = 0.9,
            staminaFactor = 1.0,
            powerFactor = 0.6,
            gutsFactor = 0.7,
            wisdomFactor = 0.45,
            skillPtFactor = 0.45,
            hpFactor = 1.0,
            motivationFactor = 15.0,
            relationFactor = 7.5,
            starGaugeFactor = 4.0,
            aptitudePtFactor = 0.0,
            ssMatchScore = 70.0,
        )
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

        val starGaugeFactor: Double = 10.0,
        val aptitudePtFactor: Double = 10.0,
        val ssMatchScore: Double = 100.0,
    ) : ActionSelectorGenerator {
        override fun generateSelector() = LArcActionSelector(this)
    }

    private var option: Option = domesticOption1

    override fun select(state: SimulationState, selection: List<Action>): Action {
        throw NotImplementedError()
    }

    override fun selectWithItem(state: SimulationState, selection: List<Action>): SelectedAction {
        option = when {
            state.turn <= 36 -> domesticOption1
            state.turn <= 43 -> overseasOption1
            state.turn <= 60 -> domesticOption2
            else -> overseasOption2
        }
        val aptitude = selectAptitude(state, selection)
        if (aptitude != null) {
            return SelectedAction(scenarioAction = SelectedLArcAction(aptitude))
        }
        return SelectedAction(
            action = selection.maxByOrNull { calcScore(state, it) } ?: selection.first(),
        )
    }

    private fun selectAptitude(state: SimulationState, selection: List<Action>): LArcAptitude? {
        val status = state.lArcStatus ?: return null
        val turn = state.turn
        val aptitudePt = status.aptitudePt
        val aptitude = when {

            // 上4つは取れる時に取る
            status.overseasTurfAptitude == 1 -> LArcAptitude.OverseasTurfAptitude
            status.longchampAptitude == 1 -> LArcAptitude.LongchampAptitude
            status.lifeRhythm == 1 -> LArcAptitude.LifeRhythm
            status.nutritionManagement == 1 -> LArcAptitude.NutritionManagement

            // TODO クラシック遠征中、状況に応じてトレ効果+50%を取る

            // スキルPt+10を取る
            status.overseasExpedition in 1..2 -> LArcAptitude.OverseasExpedition

            // クラシック凱旋門賞前、余裕があれば友情+20%を取る
            turn == 42 && status.mentalStrength == 1 && aptitudePt >= 700 -> LArcAptitude.MentalStrength
            turn == 42 && status.mentalStrength == 2 && aptitudePt >= 500 -> LArcAptitude.MentalStrength

            // クラシック凱旋門賞、デバフ解除
            turn == 43 -> {
                when {
                    status.mentalStrength == 1 -> LArcAptitude.MentalStrength
                    status.strongHeart == 1 -> LArcAptitude.StrongHeart
                    status.frenchSkill == 1 -> LArcAptitude.FrenchSkill
                    else -> null
                }
            }

            // 帰国後、友情+20%を取る
            turn >= 44 && status.mentalStrength < 3 -> LArcAptitude.MentalStrength

            // TODO シニア遠征中、状況に応じてトレ効果+50%を取る

            // シニア遠征中、体力軽減を取る
            turn in 61..64 && status.strongHeart == 2 -> LArcAptitude.StrongHeart

            // シニア凱旋門賞、デバフ解除、凱旋門賞の夢を取る
            turn == 67 -> {
                when {
                    status.hopeOfLArc == 1 -> LArcAptitude.HopeOfLArc
                    status.mentalStrength == 1 -> LArcAptitude.MentalStrength
                    status.strongHeart == 1 -> LArcAptitude.StrongHeart
                    status.frenchSkill == 1 -> LArcAptitude.FrenchSkill
                    status.consecutiveVictories == 1 -> LArcAptitude.ConsecutiveVictories
                    else -> null
                }
            }

            else -> null
        } ?: return null
        val cost = aptitude.getCost(status)
        return if (aptitudePt >= cost) aptitude else null
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (DEBUG) println("${state.turn}: $action")
        if (action is SSMatch) return if (action.member.size == 5) option.ssMatchScore else 0.0
        val total = action.resultCandidate.sumOf { it.second }.toDouble()
        val score = action.resultCandidate.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            (calcScore(calcExpectedHintStatus(action) + it.first)) * it.second / total
        } + calcRelationScore(state, action) + calcLarcScore(state, action)
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

    private fun calcRelationScore(state: SimulationState, action: Action): Double {
        if (action !is Training) return 0.0
        val score = option.relationFactor * action.support.count { it.relation < it.card.requiredRelation }
        if (DEBUG) println("  relation $score")
        return score
    }

    private fun calcLarcScore(state: SimulationState, action: Action): Double {
        if (action !is Training || state.lArcStatus == null) return 0.0
        val param = action.scenarioActionParam as LArcActionParam
        return if (state.isLevelUpTurn) {
            // 海外
            option.aptitudePtFactor * param.aptitudePt + (if (param.mayEventChance) option.aptitudePtFactor * 20.0 else 0.0)
        } else {
            // 国内
            action.member.sumOf {
                val scenarioState = it.scenarioState as LArcMemberState
                option.starGaugeFactor * min(3 - scenarioState.starLevel, 1 + action.friendCount)
            } + (if (param.mayEventChance) option.starGaugeFactor * 2.0 else 0.0)
        }
    }

    override fun toString(): String {
        return "LArcActionSelector $option"
    }
}
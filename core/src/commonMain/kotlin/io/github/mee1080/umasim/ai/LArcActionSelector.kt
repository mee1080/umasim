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
import io.github.mee1080.umasim.scenario.larc.LArcAptitude
import io.github.mee1080.umasim.scenario.larc.LArcMemberState
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
class LArcActionSelector(
    private val domesticOption1: Option = Option(),
    private val overseasOption1: Option = domesticOption1,
    private val domesticOption2: Option = domesticOption1,
    private val overseasOption2: Option = overseasOption1,
) : ActionSelector {

    constructor(options: List<Option>) : this(options[0], options[1], options[2], options[3])

    companion object {
        private const val DEBUG = false

        val speed3Stamina1Wisdom1Long = {
            LArcActionSelector(speed3Stamina1Wisdom1LongOptions)
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
                starGaugeFactor = 3.6,
                aptitudePtFactor = 0.2,
                ssMatchScore = 105.0,
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
            LArcActionSelector(speed3Power1Wisdom1MiddleOptions)
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
                starGaugeFactor = 3.9,
                aptitudePtFactor = 0.2,
                ssMatchScore = 105.0,
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
            LArcActionSelector(speed2Guts2Wisdom1MileOptions)
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
                starGaugeFactor = 4.2,
                aptitudePtFactor = 0.2,
                ssMatchScore = 110.0,
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

        val starGaugeFactor: Double = 10.0,
        val aptitudePtFactor: Double = 10.0,
        val ssMatchScore: Double = 100.0,
    ) : ActionSelectorGenerator {
        override fun generateSelector() = LArcActionSelector(this)
    }

    private var option: Option = domesticOption1

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        option = when {
            state.turn <= 36 -> domesticOption1
            state.turn <= 43 -> overseasOption1
            state.turn <= 60 -> domesticOption2
            else -> overseasOption2
        }
        val aptitude = selectAptitude(state, selection)
        if (aptitude != null) {
            return LArcGetAptitude(aptitude)
        }
        return selection.maxByOrNull { calcScore(state, it) } ?: selection.first()
    }

    private fun selectAptitude(state: SimulationState, selection: List<Action>): LArcGetAptitudeResult? {
        val status = state.lArcStatus ?: return null
        val turn = state.turn
        val aptitudePt = status.aptitudePt
        val aptitude = when {

            // 上4つは取れる時に取る
            status.overseasTurfAptitude == 1 -> LArcAptitude.OverseasTurfAptitude
            status.longchampAptitude == 1 -> LArcAptitude.LongchampAptitude
            status.nutritionManagement == 1 -> LArcAptitude.NutritionManagement

            // TODO クラシック遠征中、状況に応じてトレ効果+50%を取る

            // FIXME 固定でスピ+50%を取る
            turn in 37..40 && status.nutritionManagement == 2 -> LArcAptitude.NutritionManagement

            // スキルPt+10を取る
            status.overseasExpedition in 1..2 -> LArcAptitude.OverseasExpedition

            // クラシック凱旋門賞前、余裕があれば友情+20%を取る
            turn == 42 && status.mentalStrength == 1 && aptitudePt >= 700 -> LArcAptitude.MentalStrength
            turn == 42 && status.mentalStrength == 2 && aptitudePt >= 500 -> LArcAptitude.MentalStrength

            // クラシック凱旋門賞、残り220Pt（+80Ptで友情+20%）までデバフ解除
            turn == 43 -> {
                when {
                    status.mentalStrength == 1 -> LArcAptitude.MentalStrength
                    status.lifeRhythm == 1 && aptitudePt >= 320 -> LArcAptitude.LifeRhythm
                    status.strongHeart == 1 && aptitudePt >= 420 -> LArcAptitude.StrongHeart
                    status.frenchSkill == 1 && aptitudePt >= 320 -> LArcAptitude.FrenchSkill
                    else -> null
                }
            }

            // 帰国後、友情+20%を取る
            turn >= 44 && status.mentalStrength < 3 -> LArcAptitude.MentalStrength

            // TODO シニア遠征中、状況に応じてトレ効果+50%を取る

            // シニア遠征中、体力軽減を取る
            turn in 61..63 && status.strongHeart == 2 -> LArcAptitude.StrongHeart

            // FIXME 固定でスピ→賢さ→パワ→スタ→根性の順で取る
            turn in 61..63 && status.nutritionManagement == 2 -> LArcAptitude.NutritionManagement
            turn in 61..63 && status.frenchSkill == 2 -> LArcAptitude.FrenchSkill
            turn in 61..63 && status.lifeRhythm == 2 -> LArcAptitude.LifeRhythm
            turn in 61..63 && status.longchampAptitude == 2 -> LArcAptitude.LongchampAptitude
            turn in 61..63 && status.overseasTurfAptitude == 2 -> LArcAptitude.OverseasTurfAptitude

            // デバフ未解除の場合
            turn in 61..63 && status.lifeRhythm == 1 -> LArcAptitude.LifeRhythm
            turn in 61..63 && status.frenchSkill == 1 -> LArcAptitude.FrenchSkill
            turn in 61..63 && status.strongHeart == 1 -> LArcAptitude.StrongHeart

            // シニア凱旋門賞、デバフ解除、凱旋門賞の夢を取る
            turn == 67 -> {
                when {
                    status.hopeOfLArc == 1 -> LArcAptitude.HopeOfLArc
                    status.lifeRhythm == 1 -> LArcAptitude.LifeRhythm
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
        return if (aptitudePt >= cost) LArcGetAptitudeResult(aptitude, aptitude.getLevel(status) + 1) else null
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (!action.turnChange) return Double.MIN_VALUE
        if (DEBUG) println("${state.turn}: $action")
        if (action is SSMatch) return if (action.member.size == 5) option.ssMatchScore else 0.0
        val total = action.candidates.sumOf { it.second }.toDouble()
        val needHp = state.turn in listOf(35, 36, 58, 59)
        val score = action.candidates.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            val result = it.first as? StatusActionResult ?: return@sumOf 0.0
            val statusScore = calcScore(calcExpectedHintStatus(action) + result.status, needHp, state.status)
            val relationScore = if (result.success) calcRelationScore(state, action) else 0.0
            val lArcScore = if (result.success) calcLarcScore(
                state, action,
                result.scenarioActionParam as? LArcActionParam
            ) else 0.0
            (statusScore + relationScore + lArcScore) * it.second / total
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

    private fun calcLarcScore(state: SimulationState, action: Action, param: LArcActionParam?): Double {
        if (param == null || action !is Training || state.lArcStatus == null) return 0.0
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
        return "LArcActionSelector $domesticOption1 -> $overseasOption1 -> $domesticOption2 -> $overseasOption2"
    }
}
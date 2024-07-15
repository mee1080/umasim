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
import kotlinx.serialization.encodeToString
import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
class CookActionSelector(private val options: List<Option>) : ActionSelector {

    companion object {
        private const val DEBUG = false

        val speed1Power1Guts2Wisdom1Short = {
            CookActionSelector(speed1Power1Guts2Wisdom1ShortOptions)
        }

        val speed1Power1Guts2Wisdom1ShortOptions: List<Option>

        init {
            val option1 = Option(
                speedFactor = 1.0,
                staminaFactor = 1.0,
                powerFactor = 1.1,
                gutsFactor = 0.6,
                wisdomFactor = 1.0,
                skillPtFactor = 0.9,
                hpFactor = 0.9,
                motivationFactor = 7.0,
                relationFactor = 17.5,
                outingRelationFactor = 35.0,
                hpKeepFactor = 0.3,
                riskFactor = 4.0,
                stampFactor = 4.0,
                fullPowerFactor = 20.0,
                cookThreshold = 55.0,
                cookPtLimit = 2500,
                cookPtRequired = 2000,
            )
            val option2 = option1.copy(
                hpFactor = 0.9,
                hpKeepFactor = 0.0,
                riskFactor = 2.0,
                stampFactor = 3.5,
                fullPowerFactor = 20.0,
                cookThreshold = 20.0,
                cookPtLimit = 8000,
                cookPtRequired = 6500,
            )
            val option3 = option1.copy(
                hpFactor = 0.4,
                hpKeepFactor = 0.2,
                riskFactor = 4.0,
                stampFactor = 4.0,
                fullPowerFactor = 30.0,
                cookThreshold = 45.0,
                cookPtLimit = Int.MAX_VALUE,
                cookPtRequired = 9000,
            )
            speed1Power1Guts2Wisdom1ShortOptions = listOf(option1, option2, option3)
        }

        /**
         * トレーニングターン
         * GIプレート必要量×(残ターン数÷2)-収穫量×(残ターン数-2)
         * Lv2:180/130/80、Lv3:152/116/80、Lv4:120/100/80
         * レースターン
         * GIプレート必要量×残ターン数-収穫量×(残ターン数-2)
         * Lv2:340/210/80、Lv3:312/196/80、Lv4:280/180/80
         */
        val finalsNeedMaterial = listOf(
            listOf(180, 340, 130, 210, 80, 80),
            listOf(152, 312, 116, 196, 80, 80),
            listOf(120, 280, 100, 180, 80, 80),
        )

        private const val cookScore = 1000.0
        private const val materialLevelUpScore = 100000.0
        private const val ignoreScore = -1000.0
    }

    @Serializable
    data class Option(
        val speedFactor: Double = 1.0,
        val staminaFactor: Double = 1.0,
        val powerFactor: Double = 1.0,
        val gutsFactor: Double = 0.7,
        val wisdomFactor: Double = 1.2,
        val skillPtFactor: Double = 0.6,
        val hpFactor: Double = 0.4,
        val motivationFactor: Double = 10.0,
        val relationFactor: Double = 10.0,
        val outingRelationFactor: Double = 30.0,
        val hpKeepFactor: Double = 0.2,
        val riskFactor: Double = 2.0,

        val stampFactor: Double = 2.0,
        val fullPowerFactor: Double = 30.0,
        val cookThreshold: Double = 50.0,
        val cookPtLimit: Int = Int.MAX_VALUE,
        val cookPtRequired: Int = 2500,
        val materialPriority: Map<CookMaterial, Double> = mapOf(
            CookMaterial.Carrot to 2.0,
            CookMaterial.Garlic to 1.0,
            CookMaterial.Potato to 1.8,
            CookMaterial.HotPepper to 1.4,
            CookMaterial.Strawberry to 1.2,
        ),
    ) : SerializableActionSelectorGenerator {
        override fun generateSelector() = CookActionSelector(listOf(this))
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    data class Context(
        val option: Option,
        val state: SimulationState,
    ) {
        private val _selectionWithScore = mutableListOf<Pair<Action, Double>>()
        val selectionWithScore: List<Pair<Action, Double>> get() = _selectionWithScore

        var maxAction: Pair<Action, Double>? = null
            private set

        var maxTurnChangeAction: Pair<Action, Double>? = null
            private set

        fun add(action: Action, score: Double) {
            _selectionWithScore += action to score
            if (action !is CookMaterialLevelUp && score > (maxAction?.second ?: Double.MIN_VALUE)) {
                maxAction = action to score
            }
            if (action.turnChange && score > (maxTurnChangeAction?.second ?: Double.MIN_VALUE)) {
                maxTurnChangeAction = action to score
            }
        }

        val materialLevels by lazy { state.cookStatus!!.materialLevel.values.sortedDescending() }

        private val maxActionParam: CookActionParam?
            get() {
                val action = maxTurnChangeAction ?: return null
                val result = action.first.candidates.first().first as StatusActionResult
                return result.scenarioActionParam as CookActionParam
            }

        val pendingMaterials by lazy {
            val param = maxActionParam ?: return@lazy state.cookStatus!!.pendingMaterials
            val stampList = state.cookStatus!!.currentStamp + param.stamp
            state.cookStatus.calcPendingMaterials(stampList)
        }

        val totalMaterials by lazy {
            CookMaterial.entries.associateWith {
                state.cookStatus!!.materialCount[it]!! + pendingMaterials[it]!!
            }
        }

        val phase by lazy { (state.turn - 1) / 24 }
    }

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        return selectWithScore(state, selection).first
    }

    override suspend fun selectWithScore(state: SimulationState, selection: List<Action>): Pair<Action, List<Double>> {
        val option = when {
            state.turn <= 24 -> options[0]
            state.turn <= 48 -> options.getOrElse(1) { options[0] }
            else -> options.getOrElse(2) { options[0] }
        }
        val context = Context(option, state)
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
        if (action is CookMaterialLevelUp) return calcMaterialLevelUpScore(context, action)
        if (action is CookActivateDish) return calcActivateDishScore(context, action)
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
            (result.scenarioActionParam as? CookActionParam)?.let { param ->
                score += actionParamScore(option, param)
            }
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

    private fun actionParamScore(option: Option, param: CookActionParam): Double {
        return option.stampFactor * param.stamp.plus + if (param.stamp.fullPower) {
            option.fullPowerFactor
        } else 0.0
    }

    private fun calcActivateDishScore(context: Context, action: CookActivateDish): Double {
        val (option, state) = context
        val cookStatus = state.cookStatus ?: return ignoreScore
        val dish = action.result.dish
        if (DEBUG) println("cook ${dish.toShortString()}")
        return if (dish.phase != context.phase) {
            // 時期のあわない料理は無視
            ignoreScore
        } else if (state.turn >= 73) {
            // ファイナルズ期間
            // 野菜Lv42444、必要量80、毎回友情踏める前提でGIプレート回数を計算
            // GIプレート以外は作らない
            if (dish.phase == 3) {
                val finalsTurn = state.turn - 73
                if (cookStatus.materialCount.all {
                        val rank = when (cookStatus.materialLevel[it.key]) {
                            4, 5 -> 2
                            3 -> 1
                            else -> 0
                        }
                        it.value >= finalsNeedMaterial[rank][finalsTurn]
                    }) cookScore else ignoreScore
            } else ignoreScore
        } else if (cookStatus.cookPoint >= option.cookPtLimit) {
            // お料理Pt上限
            ignoreScore
        } else {
            val (maxAction, score) = context.maxTurnChangeAction ?: (null to 0.0)
            val threshold = if (cookStatus.cookPoint < option.cookPtRequired) 0.0 else option.cookThreshold
            if (DEBUG) println("  $score $threshold ${maxAction?.toShortString()}")
            if (maxAction is Training && score >= threshold && dish.trainingTarget.contains(maxAction.type)) {
                cookScore
            } else ignoreScore
        }
    }

    private fun calcMaterialLevelUpScore(context: Context, action: CookMaterialLevelUp): Double {
        val (option, state) = context
        val cookStatus = state.cookStatus ?: return ignoreScore
        val maxAction = context.maxAction?.first
        val material = action.result.target
        val level = action.result.level
        val dish = (maxAction as? CookActivateDish)?.result?.dish
        val lastTurn = state.turn % 4 == 0
        if (DEBUG) println("MaterialLevelUp ${action.result} ${context.materialLevels} ${maxAction?.toShortString()}")
        return when {
            context.materialLevels[3] == 1 -> {
                // Lv21222まで
                if (lastTurn && material != CookMaterial.Garlic) {
                    // 4ターン目ににんにく以外で最も獲得量を増やせる野菜、同数の場合最も保管数が少ない野菜
                    context.pendingMaterials[material]!! * materialLevelUpScore + (1000 - cookStatus.materialCount[material]!!)
                } else ignoreScore
            }

            context.materialLevels[3] == 2 -> {
                // Lv21222->Lv31333
                if (material == CookMaterial.Garlic) {
                    // にんにくは上げない
                    ignoreScore
                } else if (state.status.maxHp - state.status.hp >= 15 && material.statusType == dish?.mainTrainingTarget) {
                    // 体力が減っており料理を作る予定の場合はその野菜
                    10.0 * materialLevelUpScore
                } else if (lastTurn) {
                    // 4ターン目に優先度の高い野菜
                    option.materialPriority[material]!! * materialLevelUpScore
                } else ignoreScore
            }

            context.materialLevels[4] == 1 -> {
                // Lv31333->Lv32333
                // にんにくをLv2
                if (level == 2) materialLevelUpScore else ignoreScore
            }

            context.materialLevels[1] == 3 -> {
                // Lv32333->Lv42433
                if (level == 4 && lastTurn && option.materialPriority[material]!! > 1.5) {
                    // 4ターン目にニンジンとじゃがいもで最も獲得量を増やせる野菜、同数の場合最も保管数が少ない野菜
                    context.pendingMaterials[material]!! * materialLevelUpScore + (1000 - cookStatus.materialCount[material]!!)
                } else ignoreScore
            }

            context.materialLevels[1] == 4 -> {
                // Lv42433->Lv52444orLv42444
                if (material == CookMaterial.Garlic || material == CookMaterial.Potato) {
                    // にんにくとじゃがいもは上げない
                    ignoreScore
                } else if (material == CookMaterial.Carrot && dish?.mainTrainingTarget == StatusType.SPEED) {
                    // スピード料理を作る場合ニンジンを上げる
                    materialLevelUpScore
                } else if (level == 4 && lastTurn) {
                    // 4ターン目に唐辛子といちごで最も獲得量を増やせる野菜、同数の場合最も保管数が少ない野菜
                    context.pendingMaterials[material]!! * materialLevelUpScore + (1000 - cookStatus.materialCount[material]!!)
                } else ignoreScore
            }

            else -> {
                // LvLv52444orLv42444以降
                if (material == CookMaterial.Garlic) {
                    // にんにくは上げない
                    ignoreScore
                } else if (state.turn >= 73) {
                    // ファイナルズ期間中はすぐに上げる
                    materialLevelUpScore
                } else if (material.statusType == dish?.mainTrainingTarget) {
                    // 作る料理に応じた野菜を上げる
                    materialLevelUpScore
                } else ignoreScore
            }
        }
    }

    override fun toString(): String {
        return "CookActionSelector(${options.joinToString()})"
    }
}
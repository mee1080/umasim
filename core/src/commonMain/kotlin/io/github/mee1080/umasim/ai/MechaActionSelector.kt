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

import io.github.mee1080.umasim.scenario.mecha.MechaChipType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

class MechaActionSelector(private val options: List<Option>) :
    BaseActionSelector<MechaActionSelector.Option, MechaActionSelector.Context>() {

    companion object {
        private const val DEBUG = false

        val s2h2p1w1Generator = object : ActionSelectorGenerator {
            override fun generateSelector(): ActionSelector {
                return MechaActionSelector(s2h2p1w1Options)
            }
        }

        val s2h2p1w1 = {
            MechaActionSelector(s2h2p1w1Options)
        }

        val s2h2p1w1Options: List<Option>

        init {
            val option1 = Option(
                speedFactor = 100,
                staminaFactor = 120,
                powerFactor = 100,
                gutsFactor = 120,
                wisdomFactor = 60,
                skillPtFactor = 80,
                hpFactor = 100,
                motivationFactor = 4000,
                relationFactor = 1400,
                outingRelationFactor = 1400,
                hpKeepFactor = 200,
                riskFactor = 500,
                learningLevelFactor = 50,
                overdriveGaugeFactor = 2000,
            )
            val option2 = option1.copy(
                speedFactor = 160,
                staminaFactor = 140,
                powerFactor = 180,
                gutsFactor = 180,
                wisdomFactor = 120,
                skillPtFactor = 180,
                hpFactor = 100,
//                motivationFactor = 700,
//                relationFactor = 1750,
                outingRelationFactor = 3500,
                hpKeepFactor = 400,
                riskFactor = 100,
                learningLevelFactor = 40,
                overdriveGaugeFactor = 2500,
            )
            val option3 = option1.copy(
                speedFactor = 60,
                staminaFactor = 80,
                powerFactor = 60,
                gutsFactor = 80,
                wisdomFactor = 60,
                skillPtFactor = 60,
                hpFactor = 100,
//                motivationFactor = 700,
//                relationFactor = 1750,
                outingRelationFactor = 3500,
                hpKeepFactor = 100,
                riskFactor = 100,
                learningLevelFactor = 60,
                overdriveGaugeFactor = 2000,
            )
            val option4 = option1.copy(
                speedFactor = 60,
                staminaFactor = 100,
                powerFactor = 80,
                gutsFactor = 100,
                wisdomFactor = 60,
                skillPtFactor = 80,
                hpFactor = 100,
//                motivationFactor = 700,
//                relationFactor = 1750,
                outingRelationFactor = 3500,
                hpKeepFactor = 300,
                riskFactor = 300,
                learningLevelFactor = 70,
                overdriveGaugeFactor = 5000,
            )
            val option5 = option1.copy(
                speedFactor = 60,
                staminaFactor = 100,
                powerFactor = 100,
                gutsFactor = 80,
                wisdomFactor = 60,
                skillPtFactor = 80,
                hpFactor = 100,
//                motivationFactor = 700,
//                relationFactor = 1750,
                outingRelationFactor = 3500,
                hpKeepFactor = 200,
                riskFactor = 200,
                learningLevelFactor = 80,
                overdriveGaugeFactor = 3000,
            )
            s2h2p1w1Options = listOf(option1, option2, option3, option4, option5)
        }

        val tuningTemplate = mapOf(
            // 初回
            2 to listOf(
                // 緑3(スピ1パワ2)→青2(スタ1根性1)→赤1(賢さ1)→青3(根性2)
                buildList {
                    add(MechaChipType.LEG to 0)
                    add(MechaChipType.LEG to 1)
                    add(MechaChipType.LEG to 1)
                    add(MechaChipType.BODY to 0)
                    add(MechaChipType.BODY to 1)
                    add(MechaChipType.HEAD to 0)
                    add(MechaChipType.BODY to 1)
                }
            ),

            // クラシック前半
            24 to listOf(
                // 赤9(賢さ4ヒント5)→青3(スタ1根性2)→緑1(パワ1)
                buildList {
                    repeat(4) { add(MechaChipType.HEAD to 0) }
                    repeat(5) { add(MechaChipType.HEAD to 1) }
                    add(MechaChipType.BODY to 0)
                    add(MechaChipType.BODY to 1)
                    add(MechaChipType.BODY to 1)
                    add(MechaChipType.LEG to 1)
                }
            ),

            // クラシック後半
            36 to listOf(
                // 赤15→青3(スタ1根性2)→緑1(パワ1)
                buildList {
                    repeat(5) { add(MechaChipType.HEAD to 0) }
                    repeat(5) { add(MechaChipType.HEAD to 1) }
                    repeat(5) { add(MechaChipType.HEAD to 2) }
                    add(MechaChipType.BODY to 0)
                    add(MechaChipType.BODY to 1)
                    add(MechaChipType.BODY to 1)
                    add(MechaChipType.LEG to 1)
                }
            ),

            // シニア前半
            48 to listOf(
                buildList {
                    // 赤15→青9(スタ4根性5)→緑1(パワ1)
                    repeat(5) { add(MechaChipType.HEAD to 0) }
                    repeat(5) { add(MechaChipType.HEAD to 1) }
                    repeat(5) { add(MechaChipType.HEAD to 2) }
                    repeat(4) { add(MechaChipType.BODY to 0) }
                    repeat(5) { add(MechaChipType.BODY to 1) }
                    add(MechaChipType.LEG to 1)
                }
            ),

            // シニア後半
            60 to listOf(
                buildList {
                    // 緑15→赤12(賢さ2ヒント5得意率5)→青4(友情4)
                    repeat(5) { add(MechaChipType.LEG to 0) }
                    repeat(5) { add(MechaChipType.LEG to 1) }
                    repeat(5) { add(MechaChipType.LEG to 2) }
                    repeat(2) { add(MechaChipType.HEAD to 0) }
                    repeat(5) { add(MechaChipType.HEAD to 1) }
                    repeat(5) { add(MechaChipType.HEAD to 2) }
                    repeat(4) { add(MechaChipType.BODY to 2) }
                }
            ),

            // ファイナルズ
            72 to listOf(
                buildList {
                    // ファイナルズ：緑15→青15→赤7(得意率5ヒント2)
                    repeat(5) { add(MechaChipType.LEG to 0) }
                    repeat(5) { add(MechaChipType.LEG to 1) }
                    repeat(5) { add(MechaChipType.LEG to 2) }
                    repeat(5) { add(MechaChipType.BODY to 0) }
                    repeat(5) { add(MechaChipType.BODY to 1) }
                    repeat(5) { add(MechaChipType.BODY to 2) }
                    repeat(5) { add(MechaChipType.HEAD to 2) }
                    repeat(2) { add(MechaChipType.HEAD to 1) }
                }
            ),
        )
    }

    @Serializable
    data class Option(
        override val speedFactor: Int = 100,
        override val staminaFactor: Int = 100,
        override val powerFactor: Int = 100,
        override val gutsFactor: Int = 100,
        override val wisdomFactor: Int = 90,
        override val skillPtFactor: Int = 60,
        override val hpFactor: Int = 40,
        override val motivationFactor: Int = 3000,
        override val relationFactor: Int = 1000,
        override val outingRelationFactor: Int = 1000,
        override val hpKeepFactor: Int = 200,
        override val riskFactor: Int = 200,
        val learningLevelFactor: Int = 60,
        val overdriveGaugeFactor: Int = 5000,
    ) : BaseOption {
        override fun generateSelector() = MechaActionSelector(listOf(this))
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    class Context(option: Option, state: SimulationState) : BaseContext<Option>(option, state) {
    }

    override fun toString(): String {
        return "MechaActionSelector(${options.joinToString()})"
    }

    override fun getContext(state: SimulationState): Context {
        val option = options.getOrElse(
            when {
                state.turn >= 60 -> 4
                state.turn >= 48 -> 3
                state.turn >= 36 -> 2
                state.turn >= 24 -> 1
                else -> 0
            }
        ) { options[0] }
        return Context(option, state)
    }

    override fun calcExpectedScores(context: Context): Double {
        // 期待値は計算が複雑になるため使用しない
        return 0.0
    }

    override suspend fun calcScenarioActionScore(context: Context, action: Action, expectedScore: Double): Double? {
        return when (action) {
            MechaOverdrive -> calcOverdriveScore(context)
            is MechaTuning -> calcTuningScore(context, action)
            else -> null
        }
    }

    override fun actionParamScore(option: Option, scenarioActionParam: ScenarioActionParam?): Double {
        val param = scenarioActionParam as? MechaActionParam ?: return 0.0
        if (DEBUG) {
            println(
                "$param : ${
                    param.learningLevel.statusTotal * option.learningLevelFactor + param.overdriveGage * option.overdriveGaugeFactor
                }"
            )
        }
        return (param.learningLevel.statusTotal * option.learningLevelFactor +
                param.overdriveGage * option.overdriveGaugeFactor).toDouble()
    }

    private fun calcOverdriveScore(context: Context): Double {
        val mechaStatus = context.state.mechaStatus ?: return 0.0
        val turn = context.state.turn
        // トレーニング以外を行いたいターンは発動しない
        if (context.maxAction?.first !is Training) return 0.0
        // 計算の高速化のため、単純なルールで実装
        val activate = when {
            // 2ターン目までは初期ODゲージ上昇×2の場合のみ発動
            turn <= 2 -> mechaStatus.overdriveGauge == 6

            // ジュニア前半/シニア後半以降は最速発動
            turn <= 12 || turn >= 61 -> true

            // 次回チューニング直後にODゲージMAXになるよう調整
            // （発動して残りターン＜必要ゲージになるなら発動しない）
            (12 - (turn - 1) % 12) < 9 - mechaStatus.overdriveGauge -> false

            else -> true
        }
        return if (activate) 10000000.0 else 0.0
    }

    private fun calcTuningScore(context: Context, action: MechaTuning): Double {
        val mechaStatus = context.state.mechaStatus ?: return 0.0
        val tuningTargets =
            tuningTemplate[context.state.turn]?.getOrNull(0)?.take(mechaStatus.maxMechaEnergy) ?: return 0.0
        val result = action.result
        val currentPt = mechaStatus.chipLevels[result.type]!![result.index]
        val targetPt = tuningTargets.count { it.first == result.type && it.second == result.index }
        if (DEBUG) println("Tuning $result $currentPt / $targetPt")
        return if (currentPt < targetPt) 10000000.0 else 0.0
    }
}
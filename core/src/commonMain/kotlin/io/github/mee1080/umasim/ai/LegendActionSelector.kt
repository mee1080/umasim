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

import io.github.mee1080.umasim.scenario.legend.LegendMember
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

class LegendActionSelector(private val options: List<Option>) :
    BaseActionSelector<LegendActionSelector.Option, LegendActionSelector.Context>() {

    companion object {
        private const val DEBUG = false

        val s2h2p1w1Generator = object : ActionSelectorGenerator {
            override fun generateSelector(): ActionSelector {
                return LegendActionSelector(s2h2p1w1Options)
            }
        }

        val s2h2p1w1 = {
            LegendActionSelector(s2h2p1w1Options)
        }

        val s2h2p1w1Options: List<Option>

        init {
            val option1 = Option(
                speedFactor = 60,
                staminaFactor = 60,
                powerFactor = 80,
                gutsFactor = 60,
                wisdomFactor = 80,
                skillPtFactor = 100,
                hpFactor = 120,
                motivationFactor = 6000,
                relationFactor = 2500,
                outingRelationFactor = 2000,
                hpKeepFactor = 100,
                riskFactor = 100,
            )
            val option2 = option1.copy(
                speedFactor = 80,
                staminaFactor = 100,
                powerFactor = 120,
                gutsFactor = 100,
                wisdomFactor = 60,
                skillPtFactor = 100,
                hpFactor = 120,
                hpKeepFactor = 100,
                riskFactor = 100,
            )
            val option3 = option1.copy(
                speedFactor = 120,
                staminaFactor = 160,
                powerFactor = 120,
                gutsFactor = 140,
                wisdomFactor = 100,
                skillPtFactor = 100,
                hpFactor = 150,
                hpKeepFactor = 200,
                riskFactor = 300,
            )
            val option4 = option1.copy(
                speedFactor = 60,
                staminaFactor = 80,
                powerFactor = 80,
                gutsFactor = 100,
                wisdomFactor = 60,
                skillPtFactor = 80,
                hpFactor = 150,
                hpKeepFactor = 100,
                riskFactor = 300,
            )
            val option5 = option1.copy(
                speedFactor = 80,
                staminaFactor = 100,
                powerFactor = 120,
                gutsFactor = 120,
                wisdomFactor = 80,
                skillPtFactor = 80,
                hpFactor = 180,
                hpKeepFactor = 200,
                riskFactor = 200,
            )
            s2h2p1w1Options = listOf(option1, option2, option3, option4, option5)
        }
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
    ) : BaseOption {
        override fun generateSelector() = LegendActionSelector(listOf(this))
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    class Context(option: Option, state: SimulationState) : BaseContext<Option>(option, state) {
    }

    override fun toString(): String {
        return "LegendActionSelector(${options.joinToString()})"
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
            is LegendSelectBuff -> {
                val buff = action.result.buff
                buff.rank * 100.0 + when (buff.member) {
                    LegendMember.Blue -> 0.0
                    LegendMember.Green -> 50.0
                    LegendMember.Red -> 0.0
                }
            }

            else -> null
        }
    }

    override fun actionParamScore(option: Option, scenarioActionParam: ScenarioActionParam?): Double {
        val param = scenarioActionParam as? LegendActionParam ?: return 0.0
        return 0.0
    }
}

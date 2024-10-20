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

import io.github.mee1080.umasim.simulation2.Action
import io.github.mee1080.umasim.simulation2.MechaActionParam
import io.github.mee1080.umasim.simulation2.ScenarioActionParam
import io.github.mee1080.umasim.simulation2.SimulationState
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

class MechaActionSelector(private val options: List<Option>) :
    BaseActionSelector<MechaActionSelector.Option, MechaActionSelector.Context>() {

    companion object {
        private const val DEBUG = false

        val speed1Power1Guts2Wisdom1Short = {
            MechaActionSelector(speed1Power1Guts2Wisdom1ShortOptions)
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
            )
            val option2 = option1.copy(
                hpFactor = 0.9,
                hpKeepFactor = 0.0,
                riskFactor = 2.0,
            )
            val option3 = option1.copy(
                hpFactor = 0.4,
                hpKeepFactor = 0.2,
                riskFactor = 4.0,
            )
            speed1Power1Guts2Wisdom1ShortOptions = listOf(option1, option2, option3)
        }

    }

    @Serializable
    data class Option(
        override val speedFactor: Double = 1.0,
        override val staminaFactor: Double = 1.0,
        override val powerFactor: Double = 1.0,
        override val gutsFactor: Double = 0.7,
        override val wisdomFactor: Double = 1.2,
        override val skillPtFactor: Double = 0.6,
        override val hpFactor: Double = 0.4,
        override val motivationFactor: Double = 10.0,
        override val relationFactor: Double = 10.0,
        override val outingRelationFactor: Double = 30.0,
        override val hpKeepFactor: Double = 0.2,
        override val riskFactor: Double = 2.0,
    ) : BaseOption {
        override fun generateSelector() = MechaActionSelector(listOf(this))
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    class Context(option: Option, state: SimulationState) : BaseContext<Option>(option, state) {
        val phase by lazy { (state.turn - 1) / 24 }
    }

    override fun toString(): String {
        return "MechaActionSelector(${options.joinToString()})"
    }

    override fun getContext(state: SimulationState): Context {
        // TODO
        return Context(options.first(), state)
    }

    override fun calcScenarioActionScore(context: Context, action: Action, expectedScore: Double): Double? {
        // TODO
        return null
    }

    override fun actionParamScore(option: Option, scenarioActionParam: ScenarioActionParam?): Double {
        val param = scenarioActionParam as? MechaActionParam ?: return 0.0
        // TODO
        return 0.0
    }
}
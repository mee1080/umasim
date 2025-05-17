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
import io.github.mee1080.umasim.scenario.legend.LegendMemberState
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

fun List<LegendActionSelector.Option>.generator() = object : ActionSelectorGenerator {
    override fun generateSelector(): ActionSelector {
        return LegendActionSelector(this@generator)
    }
}

class LegendActionSelector(
    private val options: List<Option>,
) : BaseActionSelector2<LegendActionSelector.Option, LegendActionSelector.Context>() {

    companion object {
        private const val DEBUG = false

        val s2h2w1 = listOf(
            Option(
                training = 350,
                hpKeep = 10,
                motivation = 1000,
                risk = 10,
                relation = 10,
                friend = 100,
                friendCount = 300,
                supportCount = 400,
                guestCount = 250,
                forcedSupportCount = -250,
                supportBestFriendGauge = 50,
                forcedGuestCount = 100
            ),
            Option(
                training = 0,
                hpKeep = 10,
                motivation = 1000,
                risk = 10,
                relation = 10,
                friend = 100,
                friendCount = 300,
                supportCount = 400,
                guestCount = 250,
                forcedSupportCount = -250,
                supportBestFriendGauge = 50,
                forcedGuestCount = 100
            ),
        )

        val s3g1w1 = listOf(
            Option(
                speed = 90,
                stamina = 90,
                power = 100,
                guts = 110,
                wisdom = 70,
                training = 350,
                hpKeep = 10,
                motivation = 1000,
                risk = 10,
                relation = 10,
                friend = 100,
                friendCount = 300,
                supportCount = 400,
                guestCount = 250,
                forcedSupportCount = -250,
                supportBestFriendGauge = 50,
                forcedGuestCount = 100
            ),
            Option(
                speed = 90,
                stamina = 90,
                power = 100,
                guts = 110,
                wisdom = 70,
                training = 0,
                hpKeep = 10,
                motivation = 1000,
                risk = 10,
                relation = 10,
                friend = 100,
                friendCount = 300,
                supportCount = 400,
                guestCount = 250,
                forcedSupportCount = -250,
                supportBestFriendGauge = 50,
                forcedGuestCount = 100
            ),
        )

        val s2p1g1w1 = listOf(
            Option(
                speed = 90,
                stamina = 100,
                power = 100,
                guts = 90,
                wisdom = 70,
                training = 350,
                hpKeep = 10,
                motivation = 1000,
                risk = 10,
                relation = 10,
                friend = 100,
                friendCount = 300,
                supportCount = 400,
                guestCount = 250,
                forcedSupportCount = -250,
                supportBestFriendGauge = 50,
                forcedGuestCount = 100
            ),
            Option(
                speed = 90,
                stamina = 100,
                power = 100,
                guts = 90,
                wisdom = 70,
                training = 0,
                hpKeep = 10,
                motivation = 1000,
                risk = 10,
                relation = 10,
                friend = 100,
                friendCount = 300,
                supportCount = 400,
                guestCount = 250,
                forcedSupportCount = -250,
                supportBestFriendGauge = 50,
                forcedGuestCount = 100
            ),
        )

        val s2h1g1w1 = listOf(
            Option(
                speed = 90,
                stamina = 100,
                power = 100,
                guts = 90,
                wisdom = 70,
                training = 350,
                hpKeep = 10,
                motivation = 1000,
                risk = 10,
                relation = 10,
                friend = 100,
                friendCount = 300,
                supportCount = 400,
                guestCount = 250,
                forcedSupportCount = -250,
                supportBestFriendGauge = 50,
                forcedGuestCount = 100
            ),
            Option(
                speed = 90,
                stamina = 100,
                power = 100,
                guts = 90,
                wisdom = 70,
                training = 0,
                hpKeep = 10,
                motivation = 1000,
                risk = 10,
                relation = 10,
                friend = 100,
                friendCount = 300,
                supportCount = 400,
                guestCount = 250,
                forcedSupportCount = -250,
                supportBestFriendGauge = 50,
                forcedGuestCount = 100
            ),
        )
    }

    @Serializable
    data class Option(
        override val speed: Int = 100,
        override val stamina: Int = 110,
        override val power: Int = 110,
        override val guts: Int = 100,
        override val wisdom: Int = 70,

        override val training: Int = 300,
        override val hp: Int = 20,
        override val hpKeep: Int = 20,
        override val motivation: Int = 500,
        override val risk: Int = 50,

        override val relation: Int = 50,
        override val outingRelation: Int = 50,

        override val friend: Int = 500,
        override val friendCount: Int = 400,
        override val supportCount: Int = 300,
        override val guestCount: Int = 200,

        val forcedSupportCount: Int = -100,
        val supportBestFriendGauge: Int = 100,
        val forcedGuestCount: Int = 200,
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
                state.turn >= 36 -> 1
                else -> 0
            }
        ) { options[0] }
        return Context(option, state)
    }

    override fun calcTrainingAdditionalScore(context: Context, action: Training): Double {
        val (option, state) = context
        val mastery = state.legendStatus?.mastery ?: return 0.0
        var score = 0.0
        when (mastery) {
            LegendMember.Blue -> {
                // TODO
            }

            LegendMember.Green -> {
                // TODO
            }

            LegendMember.Red -> {
                val support = action.support.filter { !it.outingType }
                val guest = action.member.filter { it.guest }
                score += support.count {
                    it.card.type == action.type && (it.scenarioState as LegendMemberState).forceSpeciality
                } * option.forcedSupportCount
                score += support.count {
                    (it.scenarioState as LegendMemberState).bestFriendGauge < 20
                } * option.supportBestFriendGauge
                if (support.any { it.hint && (it.scenarioState as LegendMemberState).bestFriendGauge < 13 - state.charmBonus }) {
                    score += option.supportBestFriendGauge
                }
                score += guest.count {
                    it.card.type == action.type && (it.scenarioState as LegendMemberState).forceSpeciality
                } * option.forcedGuestCount
            }
        }
        if (DEBUG) println("  mastery $mastery $score")
        return score
    }

    override suspend fun calcScenarioActionScore(context: Context, action: Action): Double? {
        return when (action) {
            is LegendSelectBuff -> {
                val buff = action.result.buff
                buff.rank * 100.0 + when (buff.member) {
                    LegendMember.Blue -> 0.0
                    LegendMember.Green -> 0.0
                    LegendMember.Red -> 0.0
                }
            }

            is LegendDeleteBuff -> {
                val buff = action.result.buff
                -buff.rank * 100.0
            }

            else -> null
        }
    }

    override fun calcScenarioActionParamScore(
        context: Context,
        action: Action,
        scenarioActionParam: ScenarioActionParam,
    ): Double {
        val param = scenarioActionParam as? LegendActionParam ?: return 0.0
        return 0.0
    }
}

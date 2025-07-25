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

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

fun List<MujintoActionSelector.Option>.generator() = object : ActionSelectorGenerator {
    override fun generateSelector(): ActionSelector {
        return MujintoActionSelector(this@generator)
    }
}

class MujintoActionSelector(
    private val options: List<Option>,
) : BaseActionSelector2<MujintoActionSelector.Option, MujintoActionSelector.Context>() {

    companion object {
        private const val DEBUG = true

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
        override fun generateSelector() = MujintoActionSelector(listOf(this))
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    class Context(option: Option, state: SimulationState) : BaseContext<Option>(option, state) {
    }

    override fun toString(): String {
        return "MujintoActionSelector(${options.joinToString()})"
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

    override suspend fun selectWithScore(
        state: SimulationState,
        selection: List<Action>
    ): Triple<Action, List<Double>, Double> {
        val result = super.selectWithScore(state, selection)

        val mujintoStatus = state.mujintoStatus ?: return result
        if (mujintoStatus.islandTrainingTicket == 0) return result
        val islandTraining = selection.firstOrNull { it is MujintoTraining } ?: return result

        // クラシック合宿直前で島トレ券がある場合は島トレ
        if (state.turn >= 34 && state.turn <= 36) {
            val restTurn = 37 - state.turn - state.goalRace.count { it.turn <= 36 && it.turn >= state.turn }
            if (restTurn <= mujintoStatus.islandTrainingTicket) {
                return Triple(islandTraining, result.second, 1000000.0)
            }
        }

        // ファイナルズ直前で島トレ券が残る場合は島トレ
        if (state.turn >= 64) {
            val restTurn = 73 - state.turn - state.goalRace.count { it.turn >= state.turn }
            if (restTurn <= mujintoStatus.islandTrainingTicket) {
                return Triple(islandTraining, result.second, 1000000.0)
            }
        }

        // 選択中の行動で島トレ券を獲得する場合は島トレ
        // TODO レースで回避
        if (state.turn <= 60) {
            if (mujintoStatus.pioneerPoint >= mujintoStatus.requiredPoint2) return result
            val maxPioneerPoint = result.first.candidates.maxOf {
                (it.first.scenarioActionParam as? MujintoActionParam)?.pioneerPoint ?: 0
            }
            if (mujintoStatus.pioneerPoint >= mujintoStatus.requiredPoint1) {
                if (mujintoStatus.pioneerPoint + maxPioneerPoint >= mujintoStatus.requiredPoint2) {
                    return Triple(islandTraining, result.second, 1000000.0)
                }
            } else {
                if (mujintoStatus.pioneerPoint + maxPioneerPoint >= mujintoStatus.requiredPoint1) {
                    return Triple(islandTraining, result.second, 1000000.0)
                }
            }
        }
        return result
    }

    override suspend fun calcScenarioActionScore(context: Context, action: Action): Double? {
        val (_, state) = context
        return when (action) {
            is MujintoTraining -> {
                if (DEBUG) println("${state.turn}: ${action.toShortString()} ${action.result.status.toShortString()}")
                // TODO 絆たまる前
                val friendFacilityCount = action.result.member
                    .groupBy { it.position }
                    .count { position -> position.value.any { it.isFriendTraining(position.key) } }
                if (DEBUG) println("  friendFacilityCount: $friendFacilityCount")
                if (friendFacilityCount >= 3) {
                    // 3施設以上は最優先で実行
                    100000.0
                } else 0.0
            }

            is MujintoAddPlan -> {
                if (DEBUG) println("${state.turn}: ${action.toShortString()}")
                val facility = action.result.facility
                when (state.turn) {
                    2 -> {
                        // ジュニア前半：海/スピスタ
                        when (facility.type) {
                            StatusType.FRIEND -> 2.0
                            StatusType.SPEED, StatusType.STAMINA -> 1.0
                            else -> 0.0
                        }
                    }

                    12 -> {
                        // ジュニア後半：パワ賢さ/根性スピスタ
                        when (facility.type) {
                            StatusType.POWER, StatusType.WISDOM -> 2.0
                            StatusType.SPEED, StatusType.STAMINA, StatusType.GUTS -> 1.0
                            else -> 0.0
                        }
                    }

                    24 -> {
                        // クラシック前半：スピパワ/スタ根性
                        when (facility.type) {
                            StatusType.SPEED if (!facility.jukuren) -> 2.0
                            StatusType.POWER -> 2.0
                            StatusType.STAMINA if (!facility.jukuren) -> 1.0
                            StatusType.GUTS -> 1.0
                            else -> 0.0
                        }
                    }

                    36 -> {
                        // クラシック後半：スピ/スタパワ
                        when (facility.type) {
                            StatusType.SPEED -> 2.0
                            StatusType.STAMINA -> 1.0
                            StatusType.POWER if (!facility.jukuren) -> 1.0
                            else -> 0.0
                        }
                    }

                    48 -> {
                        // クラシック後半：スピ/スタ根性
                        when (facility.type) {
                            StatusType.SPEED -> 2.0
                            StatusType.STAMINA -> 1.0
                            StatusType.GUTS if (!facility.jukuren) -> 1.0
                            else -> 0.0
                        }
                    }

                    else -> 0.0
                }
            }

            else -> null
        }
    }

    override fun calcScenarioActionParamScore(
        context: Context,
        action: Action,
        scenarioActionParam: ScenarioActionParam,
    ): Double {
        val param = scenarioActionParam as? MujintoActionParam ?: return 0.0
        return 0.0
    }
}

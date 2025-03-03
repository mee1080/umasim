/*
 * Copyright 2025 mee1080
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
package io.github.mee1080.umasim.scenario.legend

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.addGuest
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.mapIf

fun SimulationState.updateLegendStatus(update: LegendStatus.() -> LegendStatus): SimulationState {
    val legendStatus = legendStatus ?: return this
    return copy(scenarioStatus = legendStatus.update())
}

fun LegendStatus.addBuff(buff: LegendBuff, enabled: Boolean? = null): LegendStatus {
    return copy(
        buffGauge = LegendMember.entries.associateWith { 0 },
        buffList = (buffList + LegendBuffState(buff, enabled ?: (buff.condition == null))).takeLast(10),
    )
}

fun SimulationState.setLegendMastery(mastery: LegendMember): SimulationState {
    var state = this
    if (mastery == LegendMember.Red) {
        state = state
            .addGuest(StatusType.SPEED, Scenario.LEGEND)
            .addGuest(StatusType.STAMINA, Scenario.LEGEND)
            .addGuest(StatusType.POWER, Scenario.LEGEND)
            .addGuest(StatusType.GUTS, Scenario.LEGEND)
            .addGuest(StatusType.WISDOM, Scenario.LEGEND)
        state = state.copy(
            member = state.member.mapIf({ !it.outingType }) { member ->
                member.copy(
                    scenarioState = (member.scenarioState as LegendMemberState).copy(
                        bestFriendLevel = 1,
                        bestFriendGauge = 20,
                    )
                )
            }
        )
    }
    return state.updateLegendStatus {
        setMastery(mastery)
    }
}

fun LegendStatus.setMastery(mastery: LegendMember): LegendStatus {
    return copy(
        mastery = mastery,
        specialStateTurn = when (mastery) {
            LegendMember.Blue -> 3
            LegendMember.Green -> 1
            LegendMember.Red -> 0
        }
    )
}

data class LegendStatus(
    val buffGauge: Map<LegendMember, Int> = LegendMember.entries.associateWith { 0 },
    val buffList: List<LegendBuffState> = emptyList(),
    val mastery: LegendMember? = null,
    val specialStateTurn: Int = 0,
) : ScenarioStatus {
    val baseBuffEffect by lazy { getBuffEffect(0, 0) }

    fun getBuffEffect(memberCount: Int, friendCount: Int): LegendBuffEffect {
        val buff = buffList.filter { it.enabled }.fold(LegendBuffEffect()) { acc, it ->
            acc + it.buff.getEffect(memberCount, friendCount, if (specialStateTurn > 0) mastery else null)
        }
        return if (specialStateTurn > 0) buff + mastery!!.specialStateEffect else buff
    }
}

enum class LegendMember(
    val color: String,
    val displayName: String,
    val specialStateName: String,
    val specialStateEffect: LegendBuffEffect = LegendBuffEffect(),
) {
    Blue("青", "セントライト", "超絶好調"),
    Green(
        "緑", "スピードシンボリ", "挑戦ゾーン",
        LegendBuffEffect(trainingBonus = 30, hpCost = 50),
    ),
    Red("赤", "ハイセイコー", ""),
}

data class LegendBuffState(
    val buff: LegendBuff,
    val enabled: Boolean,
    val coolTime: Int = 0,
)

data class LegendBuff(
    val name: String,
    val description: String,
    val member: LegendMember,
    val rank: Int,
    val effect: LegendBuffEffect,
    val effectByMemberCount: LegendBuffEffect? = null,
    val effectBySpecialState: LegendBuffEffect? = null,
    val condition: LegendBuffCondition? = null,
    val coolTime: Int = 0,
    val instant: Boolean = false,
) {
    fun getEffect(memberCount: Int, friendCount: Int, specialState: LegendMember?): LegendBuffEffect {
        return effect + effectByMemberCount?.let {
            it * if (it.friendBonus > 0) friendCount else memberCount
        } + effectBySpecialState?.let {
            if (member == specialState) it else null
        }
    }
}

data class LegendBuffEffect(
    val friendBonus: Int = 0,
    val motivationBonus: Int = 0,
    val trainingBonus: Int = 0,
    // TODO
    val hintCount: Int = 0,
    val hintFrequency: Int = 0,
    val specialtyRate: Int = 0,
    val hpCost: Int = 0,
    // TODO
    val relationBonus: Int = 0,
    val motivationUp: Int = 0,
    val positionRate: Int = 0,
    // TODO
    val addMember: Int = 0,
    // TODO
    val forceHint: Int = 0,
    val relationUp: Int = 0,
) {
    operator fun plus(other: LegendBuffEffect?) = if (other == null) this else LegendBuffEffect(
        friendBonus = friendBonus + other.friendBonus,
        motivationBonus = motivationBonus + other.motivationBonus,
        trainingBonus = trainingBonus + other.trainingBonus,
        hintCount = hintCount + other.hintCount,
        hintFrequency = hintFrequency + other.hintFrequency,
        specialtyRate = specialtyRate + other.specialtyRate,
        hpCost = hpCost + other.hpCost,
        relationBonus = relationBonus + other.relationBonus,
        motivationUp = motivationUp + other.motivationUp,
        positionRate = positionRate + other.positionRate,
        addMember = addMember + other.addMember,
        forceHint = forceHint + other.forceHint,
        relationUp = relationUp + other.relationUp,
    )

    operator fun times(value: Int) = LegendBuffEffect(
        friendBonus = friendBonus * value,
        motivationBonus = motivationBonus * value,
        trainingBonus = trainingBonus * value,
        hintCount = hintCount * value,
        hintFrequency = hintFrequency * value,
        specialtyRate = specialtyRate * value,
        hpCost = hpCost * value,
        relationBonus = relationBonus * value,
        motivationUp = motivationUp * value,
        positionRate = positionRate * value,
        addMember = addMember * value,
        forceHint = forceHint * value,
        relationUp = relationUp * value,
    )
}

sealed interface LegendBuffCondition {
    val shortName: String
    fun activateBeforeAction(status: Status): Boolean = false
    fun deactivateBeforeAction(status: Status): Boolean = false
    fun activateAfterAction(action: Action, result: ActionResult): Boolean = false
    fun deactivateAfterAction(action: Action, result: ActionResult): Boolean = false

    data object AfterRest : LegendBuffCondition {
        override val shortName = "休憩後"

        override fun activateAfterAction(action: Action, result: ActionResult): Boolean {
            return action is Sleep
        }

        override fun deactivateAfterAction(action: Action, result: ActionResult): Boolean {
            return action is Training
        }
    }

    data object AfterTraining : LegendBuffCondition {
        override val shortName = "トレ後"

        override fun activateAfterAction(action: Action, result: ActionResult): Boolean {
            return action is Training && result.success
        }

        override fun deactivateAfterAction(action: Action, result: ActionResult): Boolean {
            return action is Training
        }
    }

    data object AfterFriendTraining : LegendBuffCondition {
        override val shortName = "友情後"

        override fun activateAfterAction(action: Action, result: ActionResult): Boolean {
            return action is Training && action.friendTraining && result.success
        }

        override fun deactivateAfterAction(action: Action, result: ActionResult): Boolean {
            return action is Training
        }
    }

    data class AfterSupportCount(val count: Int) : LegendBuffCondition {
        override val shortName = "${count}人トレ後"

        override fun activateAfterAction(action: Action, result: ActionResult): Boolean {
            return action is Training && action.member.size >= count
        }

        override fun deactivateAfterAction(action: Action, result: ActionResult): Boolean {
            return action is Training
        }
    }

    data object Motivation : LegendBuffCondition {
        override val shortName = "絶好調時"

        override fun activateBeforeAction(status: Status): Boolean {
            return status.motivation >= 2
        }

        override fun deactivateBeforeAction(status: Status): Boolean {
            return status.motivation < 2
        }
    }
}

data class LegendMemberState(
    val guest: Boolean,
    val bestFriendLevel: Int = 0,
    val bestFriendGauge: Int = 0,
) : ScenarioMemberState(Scenario.LEGEND) {
    val trainingBonus by lazy {
        if (guest) bestFriendGuestTrainingBonus[bestFriendLevel] else bestFriendSupportTrainingBonus[bestFriendLevel]
    }

    val friendBonus by lazy {
        if (guest) bestFriendGuestFriendBonus[bestFriendLevel] else 0
    }
}

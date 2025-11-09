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
package io.github.mee1080.umasim.scenario

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.ActionSelector
import io.github.mee1080.umasim.simulation2.MemberState
import io.github.mee1080.umasim.simulation2.SimulationState
import io.github.mee1080.umasim.simulation2.addStatus
import kotlin.math.min

interface ScenarioEvents {
    fun beforeSimulation(state: SimulationState): SimulationState = state
    fun initialStatus(status: Status): Status = status
    suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState = state
    fun beforePredict(state: SimulationState): SimulationState = state
    suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState = state
    fun onTurnEnd(state: SimulationState): SimulationState = state
    fun afterSimulation(state: SimulationState): SimulationState = state
}

open class BaseScenarioEvents : ScenarioEvents {

    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        return when (state.turn) {
            // クラシック継承
            31 -> state
                .updateFactor()

            // シニア継承
            55 -> state
                .updateFactor()

            else -> state
        }
    }

    override fun beforeSimulation(state: SimulationState): SimulationState {
        return state.updateFactor()
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        // 記者絆4
        return state.addStatus(Status(3, 3, 3, 3, 3, 10))
    }
}

open class CommonScenarioEvents : BaseScenarioEvents() {

    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.beforeAction(state, selector)
        return when (base.turn) {
            // ジュニア新年
            25 -> base
                .updateNewYear(20, 20)

            // クラシック夏合宿
            40 -> base
                .addStatus(Status(guts = 10))

            // クラシック新年
            49 -> base
                .updateNewYear(30, 35)

            // 福引2等
            50 -> base
                .addStatus(Status(5, 5, 5, 5, 5, hp = 20, motivation = 1))

            // ファン感謝祭
            55 -> base
                .addStatus(Status(motivation = 1))

            else -> base
        }
    }
}

internal fun SimulationState.updateNewYear(plusHp: Int, plusSkillPt: Int): SimulationState {
    return if (status.hp + plusHp > status.maxHp) {
        addStatus(Status(skillPt = plusSkillPt))
    } else {
        addStatus(Status(hp = plusHp))
    }
}

internal fun SimulationState.updateFactor() = addStatus(
    Status().add(*factor.map {
        it.first to when (it.second) {
            3 -> 21
            2 -> 12
            1 -> 5
            else -> 0
        }
    }.toTypedArray())
)

internal fun SimulationState.addGuest(totalCount: Int, scenario: Scenario): SimulationState {
    val supportNames = member.filter { !it.outingType }.map { it.charaName }.toSet()
    var memberIndex = member.size
    val guestMembers = Store.guestSupportCardList
        .filter { !it.type.outingType && !supportNames.contains(it.chara) }
        .shuffled()
        .take(totalCount - supportNames.size)
        .map { MemberState(memberIndex++, it, StatusType.NONE, null, scenario.memberState(it, true)) }
    return copy(member = member + guestMembers)
}

internal fun SimulationState.addGuest(type: StatusType, scenario: Scenario): SimulationState {
    val supportNames = member.filter { !it.outingType }.map { it.charaName }.toSet()
    val guestSupport = Store.guestSupportCardList
        .filter { it.type == type && !supportNames.contains(it.chara) }
        .random()
    val guestMember =
        MemberState(member.size, guestSupport, StatusType.NONE, null, scenario.memberState(guestSupport, true))
    return copy(member = member + guestMember)
}

fun SimulationState.allTrainingLevelUp(): SimulationState {
    val newTraining = training.map {
        it.copy(level = min(5, it.level + 1))
    }
    return copy(training = newTraining)
}

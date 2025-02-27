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
import io.github.mee1080.umasim.simulation2.updateStatus

interface ScenarioEvents {
    fun beforeSimulation(state: SimulationState): SimulationState = state
    fun initialStatus(status: Status): Status = status
    fun beforeAction(state: SimulationState): SimulationState = state
    fun beforePredict(state: SimulationState): SimulationState = state
    suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState = state
    fun onTurnEnd(state: SimulationState): SimulationState = state
    fun afterSimulation(state: SimulationState): SimulationState = state
}

open class CommonScenarioEvents : ScenarioEvents {

    override fun beforeAction(state: SimulationState): SimulationState {
        return when (state.turn) {
            // ジュニア新年
            25 -> state
                .updateStatus { it.updateNewYear(20, 20) }
            // クラシック継承
            31 -> state
                .updateFactor()
            // クラシック夏合宿
            40 -> state
                .updateStatus { it + Status(guts = 10) }
            // クラシック新年
            49 -> state
                .updateStatus { it.updateNewYear(30, 35) }
            // 福引2等
            50 -> state
                .updateStatus { it + Status(5, 5, 5, 5, 5, hp = 20, motivation = 1) }
            // シニア継承、ファン感謝祭
            55 -> state
                .updateFactor()
                .updateStatus { it + Status(motivation = 1) }

            else -> state
        }
    }

    override fun beforeSimulation(state: SimulationState): SimulationState {
        return state.updateFactor()
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        // 記者絆4
        return state.updateStatus { it + Status(3, 3, 3, 3, 3, 10) }
    }
}

internal fun Status.updateNewYear(plusHp: Int, plusSkillPt: Int): Status {
    return if (hp + plusHp > maxHp) {
        copy(skillPt = skillPt + plusSkillPt)
    } else {
        copy(hp = hp + plusHp)
    }
}

internal fun SimulationState.updateFactor() = updateStatus { status ->
    status.add(*factor.map {
        it.first to when (it.second) {
            3 -> 21
            2 -> 12
            1 -> 5
            else -> 0
        }
    }.toTypedArray())
}

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

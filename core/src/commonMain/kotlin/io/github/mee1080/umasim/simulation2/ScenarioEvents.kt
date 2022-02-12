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
package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Status

interface ScenarioEvents {
    fun beforeSimulation(state: SimulationState): SimulationState = state
    fun initialStatus(status: Status): Status = status
    fun beforeAction(state: SimulationState): SimulationState? = null
    fun afterAction(state: SimulationState): SimulationState? = null
    fun onTurnEnd(state: SimulationState): SimulationState = state
    fun afterSimulation(state: SimulationState): SimulationState = state
}

class CommonScenarioEvents {

    fun beforeAction(state: SimulationState): SimulationState {
        return when (state.turn) {
            // ジュニア新年
            25 -> state
                .updateStatus { it.updateNewYear(20, 20) }
            // クラシック夏合宿
            40 -> state
                .updateStatus { it + Status(guts = 10) }
            // クラシック新年
            49 -> state
                .updateStatus { it.updateNewYear(30, 35) }
            // 福引2等
            50 -> state
                .updateStatus { it + Status(5, 5, 5, 5, 5, hp = 20, motivation = 1) }
            // ファン感謝祭
            55 -> state
                .updateStatus { it + Status(motivation = 1) }
            else -> state
        }
    }

    private fun Status.updateNewYear(plusHp: Int, plusSkillPt: Int): Status {
        return if (hp + plusHp > maxHp) {
            copy(skillPt = skillPt + plusSkillPt)
        } else {
            copy(hp = hp + plusHp)
        }
    }

    fun afterSimulation(state: SimulationState): SimulationState {
        // 記者絆4
        return state.updateStatus { it + Status(3, 3, 3, 3, 3, 10) }
    }
}
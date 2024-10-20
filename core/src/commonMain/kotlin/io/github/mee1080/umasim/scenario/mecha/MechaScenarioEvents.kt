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
package io.github.mee1080.umasim.scenario.mecha

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.CommonScenarioEvents
import io.github.mee1080.umasim.simulation2.SimulationState
import io.github.mee1080.umasim.simulation2.updateStatus

class MechaScenarioEvents : CommonScenarioEvents() {

    override fun onTurnEnd(state: SimulationState): SimulationState {
        val newState = super.onTurnEnd(state)
        return when (newState.turn) {
            45 -> newState
                .updateStatus { it + Status(wisdom = 20, skillPt = 20) }
            else -> newState
        }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        val newState = super.afterSimulation(state)
        return newState.updateStatus { it + Status(15, 15, 15, 15, 15, 50) }
    }
}

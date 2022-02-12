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

open class SimulationEvents(
    val initialStatus: (status: Status) -> Status = { it }
) {
    open fun beforeSimulation(state: SimulationState): SimulationState = state
    open fun beforeAction(state: SimulationState): SimulationState = state
    open fun afterAction(state: SimulationState): SimulationState = state
}

class ApproximateSimulationEvents(
    initialStatus: (status: Status) -> Status = { it }
) : SimulationEvents(initialStatus) {
    override fun beforeAction(state: SimulationState): SimulationState {
        val turn = state.turn
        return when {
            turn <= 24 -> {
                if (turn % 3 == 0) {
                    state.updateStatus { it + Status(4, 4, 4, 4, 4) }
                } else state
            }
            turn <= 48 -> {
                if (turn % 6 == 0) {
                    state.updateStatus { it + Status(4, 4, 4, 4, 4) }
                } else state
            }
            turn <= 72 -> {
                if (turn % 12 == 0) {
                    state.updateStatus { it + Status(4, 4, 4, 4, 4) }
                } else state
            }
            else -> state
        }
    }
}
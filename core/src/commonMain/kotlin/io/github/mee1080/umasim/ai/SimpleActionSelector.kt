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
package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation.Action
import io.github.mee1080.umasim.simulation.ActionSelector
import io.github.mee1080.umasim.simulation.SimulationState

class SimpleActionSelector(private val targetStatus: StatusType) : ActionSelector,
    io.github.mee1080.umasim.simulation2.ActionSelector {
    override fun select(state: SimulationState): Action {
        val training = state.selectTraining(targetStatus)
        return when {
            state.status.motivation <= 1 -> state.selectOuting()
            training.failureRate >= 10 -> state.selectSleep()
            else -> training
        }
    }

    override fun select(
        state: io.github.mee1080.umasim.simulation2.SimulationState,
        selection: List<io.github.mee1080.umasim.simulation2.Action>
    ): io.github.mee1080.umasim.simulation2.Action {
        val training = selectTraining(selection, targetStatus)
        return when {
            state.status.motivation <= 1 -> selectOuting(selection)
            training.failureRate >= 10 -> selectSleep(selection)
            else -> training
        }
    }
}
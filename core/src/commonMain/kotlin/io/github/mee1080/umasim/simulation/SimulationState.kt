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
package io.github.mee1080.umasim.simulation

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType

interface SimulationState {

    val supportInfo: List<Action.SupportInfo>

    val turn: Int

    val status: Status

    val condition: List<String>

    val selection: List<Action>

    fun selectOuting() = selection.first { it is Action.Outing }

    fun selectSleep() = selection.first { it is Action.Sleep }

    fun selectTraining(type: StatusType) =
        selection.first { it is Action.Training && it.type == type } as Action.Training

}

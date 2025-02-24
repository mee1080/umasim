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

import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState

fun SimulationState.updateLegendStatus(update: LegendStatus.() -> LegendStatus): SimulationState {
    val legendStatus = legendStatus ?: return this
    return copy(scenarioStatus = legendStatus.update())
}

data class LegendStatus(
    val dummy: Int = 0,
) : ScenarioStatus

enum class LegendMember(val displayName: String) {
    Blue("セントライト"),
    Green("スピードシンボリ"),
    Red("ハイセイコー"),
}

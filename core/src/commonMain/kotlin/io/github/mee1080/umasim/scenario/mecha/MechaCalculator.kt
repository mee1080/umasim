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

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.Action
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.MechaActionParam
import io.github.mee1080.umasim.simulation2.SimulationState
import kotlin.math.min

object MechaCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean
    ): Status {
        // TODO
        return Status()
    }

    fun applyScenarioAction(
        state: SimulationState,
        action: Action,
        scenarioAction: MechaActionParam?,
    ): SimulationState {
        if (scenarioAction == null) return state
        val maxLearningLevel = when {
            // TODO
            state.turn >= 60 -> 600
            else -> 200
        }
        return state.updateMechaStatus {
            copy(
                learningLevels = learningLevels.mapValues { (type, value) ->
                    min(maxLearningLevel, value + scenarioAction.learningLevel.get(type))
                },
                overdriveGauge = min(6, overdriveGauge + (if (scenarioAction.gear) 1 else 0)),
            )
        }
    }

    override fun updateScenarioTurn(state: SimulationState): SimulationState {
        return state.updateMechaStatus {
            copy(overdrive = false)
        }
    }
}

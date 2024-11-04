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
import io.github.mee1080.umasim.data.StatusType
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
        val mechaStatus = info.mechaStatus ?: return Status()
        val learningFactor = mechaStatus.learningTrainingFactors[info.training.type] ?: 0
        val gearFactor = mechaStatus.gearFactor(info.training.type)
        val odMemberCountFactor = if (mechaStatus.overdrive) mechaStatus.odMemberCountBonus * info.member.size else 0
        val singleInfo = StatusSingleInfo(
            mechaStatus = mechaStatus,
            friendTraining = friendTraining,
            learningFactor = learningFactor,
            gearFactor = gearFactor,
            odMemberCountFactor = odMemberCountFactor,
        )
        return Status(
            speed = calcStatusSingle(singleInfo, StatusType.SPEED, base.speed),
            stamina = calcStatusSingle(singleInfo, StatusType.STAMINA, base.stamina),
            power = calcStatusSingle(singleInfo, StatusType.POWER, base.power),
            guts = calcStatusSingle(singleInfo, StatusType.GUTS, base.guts),
            wisdom = calcStatusSingle(singleInfo, StatusType.WISDOM, base.wisdom),
            skillPt = calcStatusSingle(singleInfo, StatusType.SKILL, base.skillPt),
            hp = if (mechaStatus.overdrive) -(base.hp * mechaStatus.odHpCostDown / 100.0).toInt() else 0,
        )
    }

    private data class StatusSingleInfo(
        val mechaStatus: MechaStatus,
        val friendTraining: Boolean,
        val learningFactor: Int,
        val gearFactor: Int,
        val odMemberCountFactor: Int,
    )

    private fun calcStatusSingle(
        info: StatusSingleInfo,
        target: StatusType,
        baseValue: Int,
    ): Int {
        if (baseValue == 0) return 0
        val friendFactor = if (info.friendTraining) info.mechaStatus.friendBonus else 0
        val skillPtFactor = if (target == StatusType.SKILL) info.mechaStatus.skillPt else 0
        val odBaseBonus = if (info.mechaStatus.overdrive) 25 else 0
        val odStatusBonus = if (info.mechaStatus.overdrive) info.mechaStatus.odStatusBonus(target) else 0
        if (Calculator.DEBUG) println("Mecha: $target $baseValue ${info.learningFactor} ${info.gearFactor} $friendFactor $skillPtFactor $odBaseBonus $odStatusBonus ${info.odMemberCountFactor}")
        val total = baseValue *
                (10000 + info.learningFactor) / 10000.0 *
                (10000 + info.gearFactor) / 10000.0 *
                (100 + friendFactor) / 100.0 *
                (100 + skillPtFactor) / 100.0 *
                (100 + odBaseBonus) / 100.0 *
                (100 + odStatusBonus) / 100.0 *
                (100 + info.odMemberCountFactor) / 100.0
        return min(100, total.toInt() - baseValue)
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

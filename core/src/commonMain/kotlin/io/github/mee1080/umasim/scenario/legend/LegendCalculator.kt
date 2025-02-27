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

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.LegendActionParam
import io.github.mee1080.umasim.simulation2.SimulationState

object LegendCalculator : ScenarioCalculator {

    fun applyScenarioAction(state: SimulationState, scenarioAction: LegendActionParam): SimulationState {
        return state.updateLegendStatus {
            // TODO
            copy()
        }
    }

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        val legendState = info.legendStatus ?: return Status()
        val trainingType = info.training.type
        val friendMember = info.member.filter {
            it.card.type == trainingType && (it.relation >= 80 || (it.scenarioState as LegendMemberState).bestFriendGauge >= 20)
        }
        val bestFriend = legendState.mastery == LegendMember.Red
        val effect = legendState.getBuffEffect(info.member.size, friendMember.size)
        val friendTrainingReChecked = friendTraining || (bestFriend && info.member.any {
            val memberState = it.scenarioState as LegendMemberState
            memberState.guest && it.card.type == info.training.type && memberState.bestFriendGauge >= 20
        })
        val baseFriendBonus = if (friendTrainingReChecked) effect.friendBonus else 0
        val bestFriendFriendBonus = if (bestFriend) {
            friendMember.sumOf { (it.scenarioState as LegendMemberState).friendBonus }
        } else 0
        val friendFactor = (baseFriendBonus + bestFriendFriendBonus + 100) / 100.0
        val motivationBonus = effect.motivationBonus
        val baseTrainingBonus = effect.trainingBonus
        val bestFriendTrainingBonus = if (bestFriend) info.bestFriendTrainingBonus() else 0
        if (Calculator.DEBUG) println("Legend: friend=$baseFriendBonus/$bestFriendFriendBonus/$friendFactor motivation=$motivationBonus training=$baseTrainingBonus/$bestFriendTrainingBonus")
        val bonus = Calculator.ScenarioCalcBonus(
            friendFactor = friendFactor,
            motivationBonus = motivationBonus,
            trainingBonus = baseTrainingBonus + bestFriendTrainingBonus,
        )
        return Status(
            speed = Calculator.calcTrainingStatus(info, StatusType.SPEED, friendTrainingReChecked, bonus = bonus)
                .toInt() - base.speed,
            stamina = Calculator.calcTrainingStatus(info, StatusType.STAMINA, friendTrainingReChecked, bonus = bonus)
                .toInt() - base.stamina,
            power = Calculator.calcTrainingStatus(info, StatusType.POWER, friendTrainingReChecked, bonus = bonus)
                .toInt() - base.power,
            guts = Calculator.calcTrainingStatus(info, StatusType.GUTS, friendTrainingReChecked, bonus = bonus)
                .toInt() - base.guts,
            wisdom = Calculator.calcTrainingStatus(info, StatusType.WISDOM, friendTrainingReChecked, bonus = bonus)
                .toInt() - base.wisdom,
            skillPt = Calculator.calcTrainingStatus(info, StatusType.SKILL, friendTrainingReChecked, bonus = bonus)
                .toInt() - base.skillPt,
        )
    }

    private fun Calculator.CalcInfo.bestFriendTrainingBonus(): Int {
        return member.sumOf { (it.scenarioState as LegendMemberState).trainingBonus }
    }

    private fun Calculator.CalcInfo.bestFriendFriendBonus(trainingTpe: StatusType): Int {
        return member.filter { it.card.type == trainingTpe }
            .sumOf { (it.scenarioState as LegendMemberState).friendBonus }
    }
}

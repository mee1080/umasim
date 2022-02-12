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

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation.ExpectedStatus

data class SimulationState(
    val scenario: Scenario,
    val chara: Chara,
    val goalRace: List<GoalRaceEntry>,
    val member: List<MemberState>,
    val training: List<TrainingState>,
    val levelUpTurns: Collection<Int>,
    val turn: Int,
    val status: Status,
    val condition: List<String>,
    val supportTypeCount: Int,
) {
    val support get() = member.filter { !it.guest }

    val charm get() = condition.contains("愛嬌○")

    val conditionFailureRate
        get() = arrayOf(
            "練習ベタ" to 2,
            "練習上手○" to -2,
            "小さなほころび" to 5,
            "大輪の輝き" to -2,
        ).sumOf { if (condition.contains(it.first)) it.second else 0 }

    val isLevelUpTurn get() = levelUpTurns.contains(turn)

    val teamMember get() = member.filter { it.scenarioState is AoharuMemberState }

    val teamAverageStatus
        get() = member
            .mapNotNull { it.scenarioState as? AoharuMemberState }
            .fold(status to 1) { acc, aoharuMemberState -> acc.first + aoharuMemberState.status to acc.second + 1 }
            .let {
                ExpectedStatus(
                    it.first.speed.toDouble() / it.second,
                    it.first.stamina.toDouble() / it.second,
                    it.first.power.toDouble() / it.second,
                    it.first.guts.toDouble() / it.second,
                    it.first.wisdom.toDouble() / it.second,
                )
            }

    val teamStatusRank: Map<StatusType, AoharuTeamStatusRank>
        get() {
            val average = teamAverageStatus
            return trainingType.associateWith { type ->
                Store.Aoharu.teamStatusRank.values.first {
                    average.get(type) >= it.threshold
                }
            }
        }

    fun getTraining(type: StatusType) = training.first { it.type == type }
}

data class MemberState(
    val index: Int,
    val card: SupportCard,
    val position: StatusType,
    val supportState: SupportState?,
    val scenarioState: ScenarioMemberState,
) {
    val name get() = card.name
    val charaName get() = card.chara
    val guest get() = supportState == null
    val relation get() = supportState?.relation ?: 0
    val friendTrainingEnabled get() = relation >= 80
    fun isFriendTraining(type: StatusType) = friendTrainingEnabled && type == card.type
    fun getFriendBonus(type: StatusType) = if (isFriendTraining(type)) card.friendFactor else 1.0
    val wisdomFriendRecovery get() = if (isFriendTraining(StatusType.WISDOM)) card.wisdomFriendRecovery else 0
    val hint = supportState?.hintIcon == true
    fun getTrainingRelation(charm: Boolean) = getTrainingRelation(if (charm) 2 else 0)
    private fun getTrainingRelation(charmValue: Int) = card.trainingRelation + charmValue + if (hint) {
        5 + charmValue
    } else 0
}

data class SupportState(
    val relation: Int,
    val hintIcon: Boolean,
)

sealed interface ScenarioMemberState {
    val hintBlocked get() = false
}

object UraMemberState : ScenarioMemberState {
    override fun toString() = "URA"
}

data class AoharuMemberState(
    val member: TeamMemberData,
    val status: Status,
    val maxStatus: Status,
    val aoharuTrainingCount: Int,
    val aoharuIcon: Boolean,
) : ScenarioMemberState {
    val aoharuBurn get() = aoharuIcon && aoharuTrainingCount == 4
    override val hintBlocked get() = aoharuIcon
}

object AoharuNotMemberState : ScenarioMemberState

data class TrainingState(
    val type: StatusType,
    val base: List<TrainingBase>,
    val level: Int,
    val count: Int,
    val levelOverride: Int?
) {
    val currentLevel get() = levelOverride ?: level
    val current get() = base[currentLevel - 1]
}
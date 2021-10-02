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
import kotlin.math.min
import kotlin.random.Random

fun SimulationState.onTurnChange(): SimulationState {
    val turn = turn + 1
    // 各トレーニングの配置数が5以下になるよう調整
    var newMember: List<MemberState>
    do {
        newMember = member.map { it.onTurnChange(turn) }
    } while (newMember.groupBy { it.position }.any { it.value.size > 5 })
    val levelOverride = if (levelUpTurns.contains(turn)) 5 else null
    return copy(
        turn = turn,
        status = status.adjustRange(),
        member = newMember,
        training = training.map { it.copy(levelOverride = levelOverride) }
    )
}

fun SimulationState.updateStatus(update: (status: Status) -> Status) = copy(status = update(status))

private fun MemberState.onTurnChange(turn: Int): MemberState {
    // 各トレーニングに配置
    val position = randomSelect(*Calculator.calcCardPositionSelection(card))
    val scenarioState = when (scenarioState) {
        is AoharuMemberState -> scenarioState.copy(
            // アオハルアイコン表示
            aoharuIcon = turn >= 3 && Random.nextDouble() < 0.4
        )
        else -> scenarioState
    }
    // ヒントアイコン表示
    val supportState = supportState?.copy(
        hintIcon = !scenarioState.hintBlocked && card.checkHint()
    )
    return copy(
        position = position,
        supportState = supportState,
        scenarioState = scenarioState,
    )
}

fun SimulationState.applyAction(action: Action, result: Status): SimulationState {
    // ステータス更新
    val newStatus = status + result
    return if (action is Training) {
        val newTraining = training.map {
            if (action.type == it.type) {
                it.applyAction(action, scenario == Scenario.URA)
            } else it
        }
        val memberIndices = action.member.map { it.index }
        val newMember = member.map {
            if (memberIndices.contains(it.index)) {
                it.applyAction(action, charm, chara)
            } else it
//            val relation = result.supportRelation[index]
//            val supportState = member.supportState
//            if (relation != null && supportState != null) {
//                member.copy(supportState = supportState.copy(relation = supportState.relation + relation))
//            } else member
        }
        copy(
            member = newMember,
            training = newTraining,
            status = newStatus,
        )
    } else {
        copy(status = newStatus)
    }
}

private fun MemberState.applyAction(action: Training, charm: Boolean, chara: Chara): MemberState {
    // 絆上昇量を反映
    val supportState = supportState?.copy(
        relation = min(100, supportState.relation + getTrainingRelation(charm))
    )
    // アオハル特訓上昇量を反映
    val scenarioState = when (scenarioState) {
        is AoharuMemberState -> scenarioState.applyAction(action, chara)
        else -> scenarioState
    }
    return copy(
        supportState = supportState,
        scenarioState = scenarioState,
    )
}

private fun AoharuMemberState.applyAction(action: Training, chara: Chara): AoharuMemberState {
    if (!aoharuIcon) return this
    var status = status
    var maxStatus = maxStatus
    if (aoharuBurn) {
        // 爆発時はメンバーのタイプに応じて最大値と現在値が上昇
        val burnStatus = Store.Aoharu.getBurnTeam(member.type).getStatus(chara)
        maxStatus += burnStatus
        status += burnStatus
    } else {
        // アオハル特訓時はトレーニングタイプに応じて現在値が上昇
        status += Store.Aoharu.getTrainingTeam(action.type, action.level).getRandomStatus(chara)
    }
    // 最大値を反映
    status = status.adjustRange(maxStatus)
    return copy(
        status = status,
        maxStatus = maxStatus,
        aoharuTrainingCount = aoharuTrainingCount + 1,
    )
}

private fun TrainingState.applyAction(action: Training, autoLevelUp: Boolean): TrainingState {
    val count = count + 1
    return if (autoLevelUp && level < 5 && count >= 4) {
        copy(level = level + 1, count = 0)
    } else {
        copy(count = count)
    }
}
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
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.utility.applyIf

open class SimulationEvents(
    val initialStatus: (status: Status) -> Status = { it }
) {
    open fun beforeSimulation(state: SimulationState): SimulationState = state
    open fun beforeAction(state: SimulationState): SimulationState = state
    open fun afterAction(state: SimulationState): SimulationState = state
}

class ApproximateSimulationEvents(
    initialStatus: (status: Status) -> Status = { it },
    private val beforeActionEvents: (status: SimulationState) -> SimulationState = { it },
) : SimulationEvents(initialStatus) {
    override fun beforeAction(state: SimulationState): SimulationState {
        val newState = beforeActionEvents(state)
        val turn = newState.turn
        return when {
            turn <= 24 -> {
                if (turn % 3 == 0) {
                    newState.addStatus(Status(4, 4, 4, 4, 4))
                } else newState
            }

            turn <= 48 -> {
                if (turn % 6 == 0) {
                    newState.addStatus(Status(4, 4, 4, 4, 4))
                } else newState
            }

            turn <= 72 -> {
                if (turn % 12 == 0) {
                    newState.addStatus(Status(4, 4, 4, 4, 4))
                } else newState
            }

            else -> newState
        }
    }
}

/**
 * ランダムイベント近似
 *
 * 調査結果
 * 　グラライイベ発生数（55育成平均）
 * 　　シナリオ/ウマ娘（固定含む）：15.9
 * 　　　固定：登場、チュートリアル、夏合宿前×2、正月×2、福引、バレ、クリスマス、感謝祭：10
 * 　　　シナリオ/ウマ娘（ランダム）：5.9
 * 　　連続（主にSSR3SR2友人編成）：12.6 → 大豊食祭実装時に確率上昇
 * 　　非連続：3.2
 * 　　編成外：1.7
 * 　合計33.5
 * 　非固定合計23.5
 *
 * 近似条件
 * 　イベント候補
 * 　　連続全サポカ分、非連続3、編成外2、ウマ娘ランダム5（シナリオはグラライ固有なので除外）
 *
 * 　発生ターン
 * 　　夏合宿、ファイナルズ以外の2nターン（合計32回）に、ランダムな順番で発生
 * 　　FIXME イベント確率アップは考慮しない
 *
 * 　上昇量
 * 　　連続1/非連続：得意ステ10、ランダムステ5(各10%)orSP10(10%)or体力10(20%)orやる気1(20%)、絆7
 * 　　連続2：得意ステ15、ランダムステ5(各10%)orSP10(10%)or体力10(20%)orやる気1(20%)、絆7
 * 　　連続3：得意ステ20、ランダムステ10(各1/7)orSP20(2/7)、絆7
 * 　　編成外：ランダムステ10、ランダムステ5(各10%)orSP10(10%)or体力10(20%)orやる気1(20%)
 * 　　ウマ娘ランダム：各ステ+15or体力10（勝負服と通常ランダム平均したらこれぐらい？）
 * 　　FIXME スキルヒントは考慮しない
 */
class RandomEvents(
    state: SimulationState,
    initialStatus: (status: Status) -> Status = { it },
) : SimulationEvents(initialStatus) {

    private val eventQueue = mutableListOf<EventEntry>()

    private val continuousEventCount = IntArray(6) { 0 }

    init {
        val supportTargets = state.member
            .mapIndexed { index, member -> index to member }
            .filter { !it.second.guest && !it.second.outingType }
        val commonEventTargets = supportTargets.map { it.first }.shuffled().take(3)
        supportTargets.forEach { (index, member) ->
            if (!member.guest && !member.outingType) {
                val type = member.card.type
                eventQueue += EventEntry.Continuous(index, type)
                eventQueue += EventEntry.Continuous(index, type)
                if (member.card.rarity >= 3) {
                    eventQueue += EventEntry.Continuous(index, type)
                }
                if (commonEventTargets.contains(index)) {
                    eventQueue += EventEntry.Common(index, type)
                }
            }
        }
        eventQueue += EventEntry.Outside
        eventQueue += EventEntry.Outside
        trainingType.forEach {
            eventQueue += EventEntry.Chara(it)
        }
        eventQueue += EventEntry.Chara(null)
        eventQueue.shuffle()
    }

    override fun beforeAction(state: SimulationState): SimulationState {
        if (eventQueue.isEmpty()) return state
        val turn = state.turn
        return if (turn % 2 == 2 && turn !in 37..40 && turn !in 60..63 && turn < 72) {
            val event = eventQueue.removeFirst()
            state.addStatus(
                event.calcStatus(state.supportEventEffect, continuousEventCount)
            ).applyIf(event.relationTarget >= 0) {
                addRelation(7, state.member[event.relationTarget])
            }
        } else state
    }

    private sealed class EventEntry(val supportEvent: Boolean, val relationTarget: Int = -1) {

        companion object {
            protected val randomStatus = listOf(
                Status(speed = 5), Status(stamina = 5), Status(power = 5), Status(guts = 5), Status(wisdom = 5),
                Status(skillPt = 10),
                Status(hp = 10), Status(hp = 10), Status(motivation = 1), Status(motivation = 1),
            )
            protected val finalRandomStatus = listOf(
                Status(speed = 10), Status(stamina = 10), Status(power = 10), Status(guts = 10), Status(wisdom = 10),
                Status(skillPt = 20), Status(skillPt = 20),
            )
        }

        class Continuous(target: Int, val type: StatusType) : EventEntry(true, target) {
            override fun baseStatus(continuousEventCount: IntArray): Status {
                continuousEventCount[relationTarget] += 1
                return when (continuousEventCount[relationTarget]) {
                    1 -> randomStatus.random().add(type to 10)
                    2 -> randomStatus.random().add(type to 15)
                    3 -> finalRandomStatus.random().add(type to 20)
                    else -> Status()
                }
            }
        }

        class Common(target: Int, val type: StatusType) : EventEntry(true, target) {
            override fun baseStatus(continuousEventCount: IntArray): Status {
                return randomStatus.random().add(type to 10)
            }
        }

        data object Outside : EventEntry(true) {
            override fun baseStatus(continuousEventCount: IntArray): Status {
                return randomStatus.random().add(trainingType.random() to 10)
            }
        }

        class Chara(val type: StatusType?) : EventEntry(false) {
            override fun baseStatus(continuousEventCount: IntArray): Status {
                return if (type == null) Status(hp = 10) else Status().add(type to 15)
            }
        }

        abstract fun baseStatus(continuousEventCount: IntArray): Status

        fun calcStatus(supportEventEffect: Int?, continuousEventCount: IntArray): Status {
            val status = baseStatus(continuousEventCount)
            return if (!supportEvent || supportEventEffect == null) status else {
                status.multiplyToInt(supportEventEffect)
            }
        }
    }
}
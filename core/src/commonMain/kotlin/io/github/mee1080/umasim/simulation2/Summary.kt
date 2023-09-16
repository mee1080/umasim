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

class Summary(
    val status: Status,
    history: List<SimulationHistoryItem>,
    supportList: List<MemberState>,
) {

    val trainingCount = trainingType.associateWith { 0 }.toMutableMap()

    var sleepCount = 0
        private set

    var outingCount = 0
        private set

    var raceCount = 0
        private set

    var ssMatchCount = 0
        private set

    val trainingSupportCount = trainingType.associateWith { IntArray(6) { 0 } }.toMutableMap()

    val trainingSupportCountToString get() = toString(trainingSupportCount)

    val trainingFriendCount = trainingType.associateWith { IntArray(6) { 0 } }.toMutableMap()

    val trainingFriendCountToString get() = toString(trainingFriendCount)

    var trainingHintCount = 0

    data class SupportSummary(
        val state: MemberState,
        var trainingCount: Int = 0,
        var friendCount: Int = 0,
        var hintCount: Int = 0,
    ) {
        val relation get() = state.relation
        val name get() = state.name
        fun toShortString() =
            "SupportSummary(${state.toShortString()}, trainingCount=$trainingCount, friendCount=$friendCount, hintCount=$hintCount)"
    }

    val support = supportList.map { SupportSummary(it) }.toTypedArray()

    init {
        history.map { it.action }.forEach { action ->
            when (action) {
                is Training -> {
                    trainingCount[action.type] = trainingCount[action.type]!! + 1
                    trainingSupportCount[action.type]!![action.member.size]++
                    trainingFriendCount[action.type]!![action.member.count { it.isFriendTraining(action.type) }]++
                    trainingHintCount += if (action.member.any { it.hint }) 1 else 0
                    action.member.forEach {
                        val summary = support[it.index]
                        summary.trainingCount++
                        if (it.isFriendTraining(action.type)) summary.friendCount++
                        if (it.hint) summary.hintCount++
                    }
                }

                is Outing -> {
                    outingCount++
                }

                is Sleep -> {
                    sleepCount++
                }

                is Race -> {
                    raceCount++
                }

                is SSMatch -> {
                    ssMatchCount++
                }
            }
        }
    }


    fun toString(map: Map<StatusType, IntArray>) = trainingType.joinToString("/") { map[it]!!.joinToString(",") }

    override fun toString() = buildString {
        append("Summary(trainingCount=")
        append(trainingType.joinToString("/") { trainingCount[it]!!.toString() })
        append(", sleepCount=$sleepCount, outingCount=$outingCount, trainingSupportCount=")
        append(trainingSupportCountToString)
        append(", trainingFriendCount")
        append(trainingFriendCountToString)
        append(", trainingHintCount=$trainingHintCount, support=")
        append(support.joinToString("/") { it.toShortString() })
    }
}
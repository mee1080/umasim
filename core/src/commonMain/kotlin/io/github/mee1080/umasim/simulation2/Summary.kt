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
    actionList: List<Triple<Action, Status, SimulationState>>,
    supportList: List<MemberState>,
) {

    val trainingCount = trainingType.associateWith { 0 }.toMutableMap()

    var sleepCount = 0
        private set

    var outingCount = 0
        private set

    val trainingSupportCount = trainingType.associateWith { IntArray(6) { 0 } }.toMutableMap()

    val trainingSupportCountToString get() = toString(trainingSupportCount)

    val trainingFriendCount = trainingType.associateWith { IntArray(6) { 0 } }.toMutableMap()

    val trainingFriendCountToString get() = toString(trainingFriendCount)

    var trainingHintCount = 0

    data class SupportSummary(
        var name: String,
        var relation: Int,
        var trainingCount: Int = 0,
        var friendCount: Int = 0,
        var hintCount: Int = 0,
    )

    val support = supportList.map { SupportSummary(it.name, status.getSupportRelation(it.index)) }.toTypedArray()

    init {
        actionList.map { it.first }.forEach { action ->
            when (action) {
                is Training -> {
                    trainingCount[action.type] = trainingCount[action.type]!! + 1
                    trainingSupportCount[action.type]!![action.member.size]++
                    trainingFriendCount[action.type]!![action.member.count { it.isFriendTraining(action.type) }]++
                    trainingHintCount += if (action.member.count { it.hint } > 0) 1 else 0
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
            }
        }
    }


    fun toString(map: Map<StatusType, IntArray>) = buildString {
        if (map.isEmpty()) {
            append("{}")
        } else {
            append("{")
            map.forEach { (key, value) -> append("$key=${value.contentToString()}, ") }
            deleteRange(length - 2, length)
            append("}")
        }
    }

    override fun toString(): String {
        return "Summary(trainingCount=$trainingCount, sleepCount=$sleepCount, outingCount=$outingCount, trainingSupportCountToString='$trainingSupportCountToString', trainingFriendCountToString='$trainingFriendCountToString', trainingHintCount=$trainingHintCount, support=${support.contentToString()})"
    }
}
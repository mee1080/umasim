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

sealed class Action(
    val name: String,
    val status: Status,
    vararg resultCandidate: Pair<Status, Int>
) {
    var result: Status? = null

    val resultCandidate = resultCandidate
        .map { (status + it.first).adjustRange() - status to it.second }
        .toTypedArray()

    override fun toString(): String {
        return "$name $status $result ${infoToString()}"
    }

    protected open fun infoToString() = ""

    class Training(
        currentStatus: Status,
        val type: StatusType,
        val failureRate: Int,
        val level: Int,
        val levelUpTurn: Boolean,
        support: List<Support>,
        vararg resultCandidate: Pair<Status, Int>
    ) : Action("トレーニング(${type.displayName})", currentStatus, *resultCandidate) {
        val support = support.map { SupportInfo(it, type) }
        override fun infoToString() = "level=$level${if (levelUpTurn) "(+)" else ""} $support"
    }

    class Sleep(
        currentStatus: Status,
        vararg resultCandidate: Pair<Status, Int>
    ) : Action("お休み", currentStatus, *resultCandidate)

    class Outing(
        currentStatus: Status,
        support: Support?,
        vararg resultCandidate: Pair<Status, Int>
    ) : Action("お出かけ" + (support?.let { "(${it.card.name})" } ?: ""), currentStatus, *resultCandidate) {
        val support = support?.let { SupportInfo(support) }
        override fun infoToString() = support.toString()
    }

    class SupportInfo(
        support: Support,
        type: StatusType = StatusType.NONE
    ) {
        val index = support.index
        val card = support.card
        val hint = support.hint
        val friendTrainingEnabled = support.friendTrainingEnabled
        val friendTraining = support.isFriendTraining(type)

        override fun toString() = "${card.name} hint=$hint friendEnabled=$friendTrainingEnabled friend=$friendTraining"

        override fun hashCode() = index.hashCode()

        override fun equals(other: Any?): Boolean {
            return other is SupportInfo && index == other.index
        }
    }
}

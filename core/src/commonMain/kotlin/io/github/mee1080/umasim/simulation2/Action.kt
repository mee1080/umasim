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

sealed class Action(
    val name: String,
    val status: Status,
    vararg resultCandidate: Pair<Status, Int>
) {

    val resultCandidate = resultCandidate
        .map { (status + it.first).adjustRange() - status to it.second }
        .toTypedArray()

    override fun toString(): String {
        return "$name $status ${infoToString()}"
    }

    protected open fun infoToString() = ""

    class Training(
        currentStatus: Status,
        val type: StatusType,
        val failureRate: Int,
        val level: Int,
        val levelUpTurn: Boolean,
        val member: List<MemberState>,
        vararg resultCandidate: Pair<Status, Int>,
    ) : Action(
        "トレーニング(${type.displayName}Lv$level${if (levelUpTurn) "(+)" else ""})",
        currentStatus,
        *resultCandidate
    ) {
        override fun infoToString() = member.joinToString("/") { it.name }
    }

    class Sleep(
        currentStatus: Status,
        vararg resultCandidate: Pair<Status, Int>
    ) : Action("お休み", currentStatus, *resultCandidate)

    class Outing(
        currentStatus: Status,
        val support: MemberState?,
        vararg resultCandidate: Pair<Status, Int>,
    ) : Action("お出かけ" + (support?.let { "(${it.card.name})" } ?: ""), currentStatus, *resultCandidate) {
        override fun infoToString() = support.toString()
    }
}

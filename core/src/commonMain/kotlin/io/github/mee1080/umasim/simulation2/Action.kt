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

import io.github.mee1080.umasim.data.RaceGrade
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType

sealed interface Action {
    val name: String
    val resultCandidate: List<Pair<Status, Int>>
    fun infoToString() = ""
    fun toShortString() = "$name ${infoToString()}"
    fun updateCandidate(resultCandidate: List<Pair<Status, Int>>): Action
}

data class Outing(
    val support: MemberState?,
    override val resultCandidate: List<Pair<Status, Int>>,
) : Action {
    override val name = "お出かけ"
    override fun infoToString() = support?.let { "(${it.card.name})" } ?: ""
    override fun updateCandidate(resultCandidate: List<Pair<Status, Int>>) = copy(
        resultCandidate = resultCandidate
    )
}

data class Sleep(
    override val resultCandidate: List<Pair<Status, Int>>,
) : Action {
    override val name = "お休み"
    override fun toString() = "Sleep"
    override fun updateCandidate(resultCandidate: List<Pair<Status, Int>>) = copy(
        resultCandidate = resultCandidate
    )
}

data class Training(
    val type: StatusType,
    val failureRate: Int,
    val level: Int,
    val member: List<MemberState>,
    override val resultCandidate: List<Pair<Status, Int>>,
    val baseStatus: Status,
) : Action {
    val support get() = member.filter { !it.guest }
    override val name = "トレーニング(${type.displayName}Lv$level)"
    override fun infoToString() = member.joinToString("/") {
        buildString {
            append(it.name)
            if (it.isFriendTraining(type)) append("(友情)")
            if (it.hint) append("(ヒント)")
            (it.scenarioState as? AoharuMemberState)?.let { aoharu ->
                if (aoharu.aoharuBurn) append("(アオハル魂爆発)") else if (aoharu.aoharuIcon) append("(アオハル特訓)")
            }
        }
    }

    override fun updateCandidate(resultCandidate: List<Pair<Status, Int>>) = copy(
        resultCandidate = resultCandidate
    )
}

data class Race(
    val goal: Boolean,
    val raceName: String,
    val grade: RaceGrade,
    override val resultCandidate: List<Pair<Status, Int>>,
) : Action {
    override val name = raceName + if (goal) "(目標)" else ""
    override fun updateCandidate(resultCandidate: List<Pair<Status, Int>>) = copy(
        resultCandidate = resultCandidate
    )
}
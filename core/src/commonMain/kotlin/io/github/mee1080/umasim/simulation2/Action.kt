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

sealed interface Action {
    val name: String
    val resultCandidate: List<Pair<Status, Int>>
    val scenarioActionParam: ScenarioActionParam?
    fun infoToString() = ""
    fun toShortString() = "$name ${infoToString()}"
    fun updateCandidate(resultCandidate: List<Pair<Status, Int>>): Action
}

data class Outing(
    val support: MemberState?,
    override val resultCandidate: List<Pair<Status, Int>>,
    override val scenarioActionParam: ScenarioActionParam? = null,
) : Action {
    override val name = "お出かけ"
    override fun infoToString() = support?.let { "(${it.card.name})" } ?: ""
    override fun updateCandidate(resultCandidate: List<Pair<Status, Int>>) = copy(
        resultCandidate = resultCandidate
    )
}

data class Sleep(
    override val resultCandidate: List<Pair<Status, Int>>,
    override val scenarioActionParam: ScenarioActionParam? = null,
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
    val friendTraining: Boolean,
    override val scenarioActionParam: ScenarioActionParam? = null,
) : Action {
    val support get() = member.filter { !it.guest }
    override val name = "トレーニング(${type.displayName}Lv$level)"
    override fun infoToString() = member.joinToString("/") {
        buildString {
            if (it.guest) append("(ゲスト)${it.charaName}") else append("${it.name}(${it.relation})")
            if (it.isFriendTraining(type)) append("(友情)")
            if (it.hint) append("(ヒント)")
            when (val scenario = it.scenarioState) {
                is AoharuMemberState -> {
                    if (scenario.aoharuBurn) {
                        append("(アオハル魂爆発)")
                    } else if (scenario.aoharuIcon) {
                        append("(アオハル特訓)")
                    } else {
                        append("(${scenario.aoharuTrainingCount})")
                    }
                }

                is LArcMemberState -> {
                    append("(${scenario.starGauge},Lv${scenario.starLevel})")
                }
            }
        }
    }

    override fun updateCandidate(resultCandidate: List<Pair<Status, Int>>) = copy(
        resultCandidate = resultCandidate
    )

    val friendCount by lazy {
        if (!friendTraining) 0 else member.count { it.isFriendTraining(type) }
    }
}

data class Race(
    val goal: Boolean,
    val raceName: String,
    val grade: RaceGrade,
    override val resultCandidate: List<Pair<Status, Int>>,
    override val scenarioActionParam: ScenarioActionParam? = null,
) : Action {
    override val name = raceName + if (goal) "(目標)" else ""
    override fun updateCandidate(resultCandidate: List<Pair<Status, Int>>) = copy(
        resultCandidate = resultCandidate
    )
}

sealed interface ScenarioActionParam {
    fun toShortString(): String
}

data class GmActionParam(
    val knowledgeFounder: Founder,
    val knowledgeType: StatusType,
    val knowledgeCount: Int,
    val knowledgeEventRate: Double = 0.0,
) : ScenarioActionParam {
    override fun toShortString() =
        "$knowledgeFounder/${knowledgeType}x$knowledgeCount${if (knowledgeEventRate > 0.0) "+1?" else ""}"
}

data class SSMatch(
    val isSSSMatch: Boolean,
    val member: Set<MemberState>,
    override val resultCandidate: List<Pair<Status, Int>>,
    override val scenarioActionParam: LArcActionParam,
) : Action {
    override val name = if (isSSSMatch) "SSSマッチ(${member.size}人)" else "SSマッチ(${member.size}人)"

    override fun infoToString() = member.joinToString("/") {
        buildString {
            if (it.guest) append("(ゲスト)${it.charaName}") else append(it.name)
            val lArc = it.scenarioState as LArcMemberState
            append('(')
            append(lArc.starType)
            append(',')
            append(lArc.nextStarEffect[0])
            append(',')
            append(lArc.starLevel)
            append(')')
        }
    }

    override fun updateCandidate(resultCandidate: List<Pair<Status, Int>>) = copy(
        resultCandidate = resultCandidate
    )
}

data class LArcActionParam(
    val supporterPt: Int = 0,
    val aptitudePt: Int = 0,
    val starGaugeMember: Set<MemberState> = emptySet(),
    val condition: Set<String> = emptySet(),
) : ScenarioActionParam {
    override fun toShortString() = "supporterPt=$supporterPt, aptitudePt=$aptitudePt"

    operator fun plus(other: LArcActionParam) = LArcActionParam(
        supporterPt = supporterPt + other.supporterPt,
        aptitudePt = aptitudePt + other.aptitudePt,
        starGaugeMember = starGaugeMember + other.starGaugeMember,
    )
}
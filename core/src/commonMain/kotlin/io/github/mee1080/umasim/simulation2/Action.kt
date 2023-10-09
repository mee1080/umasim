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
    val candidates: List<Pair<ActionResult, Int>>
    fun infoToString() = ""
    fun toShortString() = "$name ${infoToString()}"

    fun addScenarioActionParam(scenarioActionParam: ScenarioActionParam): List<Pair<ActionResult, Int>> {
        return candidates.map { it.first.addScenarioActionParam(scenarioActionParam) to it.second }
    }
}

sealed interface ActionResult {
    val status: Status
    val scenarioActionParam: ScenarioActionParam?
    val success: Boolean
    fun addScenarioActionParam(scenarioActionParam: ScenarioActionParam): ActionResult
}

class StatusActionResult private constructor(
    override val status: Status,
    override val scenarioActionParam: ScenarioActionParam?,
    override val success: Boolean = true,
) : ActionResult {
    constructor(
        current: Status,
        status: Status,
        scenarioActionParam: ScenarioActionParam? = null,
        success: Boolean = true,
    ) : this((current + status).adjustRange() - current, scenarioActionParam, success)

    override fun addScenarioActionParam(scenarioActionParam: ScenarioActionParam) = StatusActionResult(
        status, if (success) scenarioActionParam else null, success
    )

    override fun toString() =
        "StatusActionResult(status=${status.toShortString()},scenario=${scenarioActionParam?.toShortString()},success=$success)"
}

data class Outing(
    val support: MemberState?,
    override val candidates: List<Pair<ActionResult, Int>>,
) : Action {
    override val name = "お出かけ"
    override fun infoToString() = support?.let { "(${it.card.name})" } ?: ""
}

data class Sleep(
    override val candidates: List<Pair<ActionResult, Int>>,
) : Action {
    override val name = "お休み"
    override fun toString() = "Sleep"
}

data class Training(
    val type: StatusType,
    val failureRate: Int,
    val level: Int,
    val member: List<MemberState>,
    override val candidates: List<Pair<ActionResult, Int>>,
    val baseStatus: Status,
    val friendTraining: Boolean,
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

    val friendCount by lazy {
        if (!friendTraining) 0 else member.count { it.isFriendTraining(type) }
    }
}

data class Race(
    val goal: Boolean,
    val raceName: String,
    val grade: RaceGrade,
    override val candidates: List<Pair<ActionResult, Int>>,
) : Action {
    override val name = raceName + if (goal) "(目標)" else ""
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
    override val candidates: List<Pair<ActionResult, Int>>,
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
}

data class LArcActionParam(
    val supporterPt: Int = 0,
    val aptitudePt: Int = 0,
    val starGaugeMember: Set<MemberState> = emptySet(),
    val condition: Set<String> = emptySet(),
    val mayEventChance: Boolean = false,
) : ScenarioActionParam {
    override fun toShortString() = buildString {
        append("LArc(")
        if (supporterPt > 0) append("sup=$supporterPt,")
        if (aptitudePt > 0) append("apt=$aptitudePt,")
        append(")")
    }

    operator fun plus(other: LArcActionParam) = LArcActionParam(
        supporterPt = supporterPt + other.supporterPt,
        aptitudePt = aptitudePt + other.aptitudePt,
        starGaugeMember = starGaugeMember + other.starGaugeMember,
    )
}
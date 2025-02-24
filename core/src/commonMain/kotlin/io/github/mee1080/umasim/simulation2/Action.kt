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
import io.github.mee1080.umasim.data.randomSelect
import io.github.mee1080.umasim.scenario.climax.ShopItem
import io.github.mee1080.umasim.scenario.cook.CookDish
import io.github.mee1080.umasim.scenario.cook.CookMaterial
import io.github.mee1080.umasim.scenario.cook.CookStamp
import io.github.mee1080.umasim.scenario.gm.Founder
import io.github.mee1080.umasim.scenario.larc.LArcAptitude
import io.github.mee1080.umasim.scenario.larc.LArcMemberState
import io.github.mee1080.umasim.scenario.live.Lesson
import io.github.mee1080.umasim.scenario.mecha.MechaChipType
import io.github.mee1080.umasim.scenario.uaf.UafGenre

sealed interface Action {
    val name: String
    val candidates: List<Pair<ActionResult, Int>>
    val turnChange: Boolean get() = true
    fun infoToString() = ""
    fun toShortString() = "$name ${infoToString()}"
    fun randomSelectResult(): ActionResult
}

data object NoAction : Action {
    override val name = "行動終了"
    override val candidates: List<Pair<ActionResult, Int>> = emptyList()
    override fun randomSelectResult() = StatusActionResult(Status(), null)
}

sealed interface SingleAction : Action {
    val result: ActionResult
    override val candidates get() = listOf(result to 1)
    override fun randomSelectResult() = result
}

sealed interface MultipleAction : Action {
    val totalRate get() = candidates.sumOf { it.second }
    override fun randomSelectResult() = randomSelect(candidates)
}

sealed interface ActionResult {
    val status: Status get() = Status()
}

data class StatusActionResult(
    override val status: Status,
    val scenarioActionParam: ScenarioActionParam?,
    val success: Boolean = true,
) : ActionResult {
    constructor(
        current: Status,
        status: Status,
        scenarioActionParam: ScenarioActionParam? = null,
        success: Boolean = true,
    ) : this((current + status).adjustRange() - current, scenarioActionParam, success)

    override fun toString() =
        "StatusActionResult(status=${status.toShortString()},scenario=${scenarioActionParam?.toShortString()},success=$success)"
}

data class Outing(
    val support: MemberState?,
    override val candidates: List<Pair<ActionResult, Int>>,
) : MultipleAction {
    override val name = "お出かけ " + infoToString()
    override fun infoToString() =
        support?.let { "(${it.card.name} ${(it.supportState?.outingStep ?: 0) - 1}回目)" } ?: "(育成ウマ娘)"
}

data class Sleep(
    override val candidates: List<Pair<ActionResult, Int>>,
) : MultipleAction {
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
) : MultipleAction {
    val support get() = member.filter { !it.guest }
    override val name = "トレーニング(${type.displayName}Lv$level)"

    fun memberToStrings() = member.map {
        buildString {
            append(it.name)
            if (!it.guest) append("(${it.relation})")
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

    override fun infoToString() = memberToStrings().joinToString("/")

    val friendCount by lazy {
        if (!friendTraining) 0 else member.count { it.isFriendTraining(type) }
    }
}

data class Race(
    val goal: Boolean,
    val raceName: String,
    val grade: RaceGrade,
    override val result: ActionResult,
) : SingleAction {
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
    override val result: ActionResult,
) : SingleAction {
    override val name = if (isSSSMatch) "SSSマッチ(${member.size}人)" else "SSマッチ(${member.size}人)"

    fun memberToStrings() = member.map {
        buildString {
            append(it.name)
            val lArc = it.scenarioState as LArcMemberState
            append('(')
            append(lArc.nextStarEffect[0].displayName)
            append(' ')
            append(lArc.starType.displayName)
            append("Lv")
            append(lArc.starLevel)
            append(')')
        }
    }

    override fun infoToString() = memberToStrings().joinToString("/")
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

class ClimaxBuyUseItem(
    override val result: ClimaxBuyUseItemResult,
) : SingleAction {
    override val name get() = result.toString()
    override val turnChange get() = false
}

class ClimaxBuyUseItemResult(
    val buyItem: List<ShopItem>? = null,
    val useItem: List<ShopItem>? = null,
) : ActionResult {
    override fun toString() = buildString {
        if (!buyItem.isNullOrEmpty()) {
            append("アイテム購入：")
            append(buyItem.joinToString { it.name })
        }
        if (!useItem.isNullOrEmpty()) {
            append(" アイテム使用：")
            append(useItem.joinToString { it.name })
        }
    }
}

class LiveGetLesson(
    override val result: LiveGetLessonResult,
) : SingleAction {
    override val name get() = "レッスン獲得：$result"
    override val turnChange get() = false
}

class LiveGetLessonResult(
    val lesson: Lesson,
) : ActionResult {
    override fun toString() = lesson.displayName
}

data class GmActivateWisdom(
    override val result: GmActivateWisdomResult,
) : SingleAction {
    override val name get() = "叡智獲得：$result"
    override val turnChange get() = false
}

class GmActivateWisdomResult(
    val founder: Founder
) : ActionResult {
    override fun toString() = founder.charaName
}

class LArcGetAptitude(
    override val result: LArcGetAptitudeResult,
) : SingleAction {
    override val name get() = "海外適性獲得：$result"
    override val turnChange get() = false
}

class LArcGetAptitudeResult(
    val aptitude: LArcAptitude,
    val level: Int,
) : ActionResult {
    override fun toString() = "${aptitude.displayName}Lv$level(${aptitude.getCost(level)}Pt)"
}

class UafConsult(
    override val result: UafConsultResult,
) : SingleAction {
    override val name get() = "相談：$result"
    override val turnChange get() = false

    companion object {
        val instance = mapOf(
            UafGenre.Blue to listOf(UafGenre.Red, UafGenre.Yellow),
            UafGenre.Red to listOf(UafGenre.Blue, UafGenre.Yellow),
            UafGenre.Yellow to listOf(UafGenre.Blue, UafGenre.Red),
        ).mapValues { entry ->
            entry.value.map { UafConsult(UafConsultResult(entry.key, it)) }
        }
    }
}

class UafConsultResult(
    val from: UafGenre,
    val to: UafGenre,
) : ActionResult {
    override fun toString() = "${from.longDisplayName}->${to.longDisplayName}"
}

class UafScenarioActionParam(
    val athleticsLevelUp: Map<StatusType, Int> = emptyMap(),
    val notTraining: Boolean = false,
) : ScenarioActionParam {
    override fun toShortString() = buildString {
        append("Uaf(")
        if (athleticsLevelUp.isNotEmpty()) append(athleticsLevelUp.toString())
        if (notTraining) append("次ターン競技Lv上昇量アップ")
        append(")")
    }
}

data class CookActionParam(
    val stamp: CookStamp,
) : ScenarioActionParam {
    override fun toShortString() = buildString {
        append("Cook(")
        if (stamp.fullPower) {
            append("全力:")
        }
        append(stamp.material.displayName)
        if (stamp.plus > 0) {
            append("+")
            append(stamp.plus)
        }
        append(")")
    }
}

class CookMaterialLevelUp(
    override val result: CookMaterialLevelUpResult,
) : SingleAction {
    override val name get() = "野菜LvUp：$result"
    override val turnChange get() = false

    constructor(target: CookMaterial, level: Int) : this(CookMaterialLevelUpResult(target, level))
}

class CookMaterialLevelUpResult(
    val target: CookMaterial,
    val level: Int,
) : ActionResult {
    override fun toString() = "${target.displayName} Lv$level"
}

class CookActivateDish(
    override val result: CookActivateDishResult,
) : SingleAction {
    override val name get() = "料理：$result"
    override val turnChange get() = false

    constructor(dish: CookDish) : this(CookActivateDishResult(dish))
}

class CookActivateDishResult(
    val dish: CookDish,
) : ActionResult {
    override fun toString() = dish.toShortString()
}

data class MechaActionParam(
    val learningLevel: Status,
    val overdriveGage: Int,
) : ScenarioActionParam {
    override fun toShortString() = buildString {
        append("Mecha learningLevel=$learningLevel, overdriveGage=$overdriveGage")
    }
}

data object MechaOverdriveResult : ActionResult {
    override fun toString() = "オーバードライブ"
}

data object MechaOverdrive : SingleAction {
    override val result get() = MechaOverdriveResult
    override val name get() = "オーバードライブ"
    override val turnChange get() = false
}

class MechaTuningResult(
    val type: MechaChipType,
    val index: Int,
) : ActionResult {
    override fun toString() = type.chipNames[index]
}

class MechaTuning(
    override val result: MechaTuningResult,
) : SingleAction {
    override val name get() = "チューニング：$result +1"
    override val turnChange get() = false

    constructor(type: MechaChipType, index: Int) : this(MechaTuningResult(type, index))
}

data class LegendActionParam(
    val dummy: Int,
) : ScenarioActionParam {
    override fun toShortString() = TODO()
}

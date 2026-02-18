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
import io.github.mee1080.umasim.scenario.legend.LegendBuff
import io.github.mee1080.umasim.scenario.legend.LegendMember
import io.github.mee1080.umasim.scenario.live.Lesson
import io.github.mee1080.umasim.scenario.mecha.MechaChipType
import io.github.mee1080.umasim.scenario.mujinto.MujintoFacility
import io.github.mee1080.umasim.scenario.mujinto.facilityName
import io.github.mee1080.umasim.scenario.onsen.Gensen
import io.github.mee1080.umasim.scenario.onsen.StratumType
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
    val success: Boolean get() = true
    val scenarioActionParam: ScenarioActionParam? get() = null
}

data class StatusActionResult(
    override val status: Status,
    override val scenarioActionParam: ScenarioActionParam? = null,
    override val success: Boolean = true,
) : ActionResult {
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
    override val name = buildString {
        if (grade != RaceGrade.DEBUT && grade != RaceGrade.FINALS) {
            append('[')
            append(grade.displayName)
            append("] ")
        }
        append(raceName)
        if (goal) {
            append(" (目標)")
        }
    }
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
    val legendMember: LegendMember,
    val gauge: Int,
) : ScenarioActionParam {
    override fun toShortString() = "${legendMember.color}+$gauge"
}

sealed interface LegendActionResult : ActionResult

data class LegendSelectBuffResult(
    val buff: LegendBuff,
) : LegendActionResult {
    override fun toString() = buff.name
}

data class LegendSelectBuff(
    override val result: LegendSelectBuffResult,
) : SingleAction {
    override val name get() = "心得選択：${result}"
    override val turnChange get() = false

    constructor(buff: LegendBuff) : this(LegendSelectBuffResult(buff))
}

data class LegendDeleteBuffResult(
    val buff: LegendBuff,
) : LegendActionResult {
    override fun toString() = buff.name
}

data class LegendDeleteBuff(
    override val result: LegendDeleteBuffResult,
) : SingleAction {
    override val name get() = "心得削除：${result}"
    override val turnChange get() = false

    constructor(buff: LegendBuff) : this(LegendDeleteBuffResult(buff))
}

data class FriendActionResult(
    val support: MemberState,
    override val status: Status,
    val relation: Int,
    val otherRelation: Int = 0,
    override val scenarioActionParam: ScenarioActionParam? = null,
    val outingStep: Int = 0,
) : ActionResult {
    override fun toString() =
        "FriendActionResult(support=${support.card.name},status=${status.toShortString()},relation=$relation,scenario=${scenarioActionParam?.toShortString()},step=$outingStep)"
}

data class FriendAction(
    override val name: String,
    override val result: FriendActionResult,
) : SingleAction {
    override val turnChange get() = false
}

data class MujintoActionParam(
    val pioneerPoint: Int,
    val upgradeFacility: Boolean = true,
) : ScenarioActionParam {
    override fun toShortString() = buildString {
        append("Mujinto pioneerPoint=$pioneerPoint")
    }
}

sealed interface MujintoActionResult : ActionResult

data class MujintoTrainingResult(
    override val status: Status,
    val member: List<MemberState>,
    val friendTraining: Boolean,
    val pioneerPoint: Int = 0,
) : MujintoActionResult {
    override fun toString() = "島トレ ${status.toShortString()}"
}

data class MujintoTraining(
    val member: List<MemberState>,
    val friendTraining: Boolean,
    override val result: MujintoTrainingResult,
) : SingleAction {
    override val name get() = "島トレ"
    override val turnChange get() = true
}

data class MujintoAddPlanResult(
    val facility: MujintoFacility,
) : MujintoActionResult {
    override fun toString() = buildString {
        append(facility.type.facilityName)
        append(" ")
        if (facility.level >= 3 && facility.type != StatusType.FRIEND) {
            append(if (facility.jukuren) "熟練" else "本能")
        }
        append("Lv")
        append(facility.level)
    }
}

data class MujintoAddPlan(
    override val result: MujintoAddPlanResult,
) : SingleAction {
    override val name get() = "施設計画：${result}"
    override val turnChange get() = false
}

sealed interface OnsenActionResult : ActionResult

data class OnsenActionParam(
    val digPoint: Int = 0,
    val onsenTicket: Int = 0,
    val digBonus: Status = Status(),
) : ScenarioActionParam {
    override fun toShortString() = buildList {
        if (digPoint > 0) {
            add("掘削Pt:$digPoint")
        }
        if (onsenTicket > 0) {
            add("入浴券:$onsenTicket")
        }
    }.joinToString(", ")
}

data class OnsenPR(
    val memberCount: Int,
    override val result: ActionResult,
) : SingleAction {
    override val name = "PR活動 $memberCount 人"
}

data object OnsenBathingResult : OnsenActionResult

data object OnsenBathing : SingleAction {
    override val name = "入浴"
    override val turnChange = false
    override val result = OnsenBathingResult
}

data class OnsenSelectGensenResult(
    val gensen: Gensen,
) : OnsenActionResult

data class OnsenSelectGensen(
    val gensen: Gensen,
) : SingleAction {
    override val name = buildString {
        append("源泉選択 ")
        append(gensen.name)
        append("(")
        append(
            gensen.strata.joinToString(", ") { (type, size) ->
                "${type.displayName} $size"
            }
        )
        if (gensen.immediateEffectHp > 0) {
            append(" / 即時効果：体力+")
            append(gensen.immediateEffectHp)
        }
        append(" / 継続効果：")
        append(gensen.continuousEffect)
        append(")")
    }
    override val turnChange = false
    override val result = OnsenSelectGensenResult(gensen)
}

data class OnsenSelectEquipmentResult(
    val equipment: StratumType,
) : OnsenActionResult

data class OnsenSelectEquipment(
    val equipment: StratumType,
) : SingleAction {
    override val name = "装備選択 ${equipment.displayName}"
    override val turnChange = false
    override val result = OnsenSelectEquipmentResult(equipment)
}

sealed interface BCActionResult : ActionResult

data class BCActionParam(
    val dummy: Int = 0
) : ScenarioActionParam {
    override fun toShortString() = "BC(dummy=$dummy)"
}

data class BCAction(
    override val name: String,
    override val result: ActionResult,
) : SingleAction

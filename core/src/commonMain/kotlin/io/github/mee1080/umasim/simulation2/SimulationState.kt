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
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.aoharu.AoharuTeamStatusRank
import io.github.mee1080.umasim.scenario.climax.*
import io.github.mee1080.umasim.scenario.cook.CookStatus
import io.github.mee1080.umasim.scenario.gm.GmStatus
import io.github.mee1080.umasim.scenario.larc.LArcStatus
import io.github.mee1080.umasim.scenario.live.LiveStatus
import io.github.mee1080.umasim.scenario.uaf.UafStatus
import kotlin.math.min

interface ScenarioStatus

data class SimulationState(
    val scenario: Scenario,
    val chara: Chara,
    val factor: List<Pair<StatusType, Int>>,
    val goalRace: List<RaceEntry>,
    val member: List<MemberState>,
    val training: List<TrainingState>,
    val turn: Int = 0,
    val status: Status,
    val condition: List<String> = emptyList(),
    val supportCount: Map<StatusType, Int>,
    val totalRaceBonus: Int,
    val totalFanBonus: Int,
    val refreshTurn: Int = 0,
    val shopCoin: Int = 0,
    val possessionItem: List<ShopItem> = emptyList(),
    val enableItem: EnableItem = EnableItem(),
    val scenarioStatus: ScenarioStatus? = null,
) {
    val liveStatus get() = scenarioStatus as? LiveStatus

    val gmStatus get() = scenarioStatus as? GmStatus

    val lArcStatus get() = scenarioStatus as? LArcStatus

    val uafStatus get() = scenarioStatus as? UafStatus

    val cookStatus get() = scenarioStatus as? CookStatus

    val supportTypeCount = supportCount.size

    val nextGoalRace by lazy { goalRace.firstOrNull { it.turn >= turn } }

    val isGoalRaceTurn get() = nextGoalRace?.turn == turn

    val itemAvailable get() = scenario == Scenario.CLIMAX

    val liveAvailable get() = liveStatus != null

    val wisdomAvailable get() = gmStatus != null

    val lArcAvailable get() = lArcStatus != null

    val support get() = member.filter { !it.guest }

    val charm get() = condition.contains("愛嬌○")

    val conditionFailureRate
        get() = arrayOf(
            "練習ベタ" to 2,
            "練習上手○" to -2,
            "小さなほころび" to 5,
            "大輪の輝き" to -2,
        ).sumOf { if (condition.contains(it.first)) it.second else 0 }

    val levelUpTurns get() = scenario.levelUpTurns

    val isLevelUpTurn get() = levelUpTurns.contains(turn)

    val teamMember get() = member.filter { it.scenarioState is AoharuMemberState }

    val teamAverageStatus
        get() = member.mapNotNull { it.scenarioState as? AoharuMemberState }
            .fold(status to 1) { acc, aoharuMemberState -> acc.first + aoharuMemberState.status to acc.second + 1 }
            .let {
                ExpectedStatus(
                    it.first.speed.toDouble() / it.second,
                    it.first.stamina.toDouble() / it.second,
                    it.first.power.toDouble() / it.second,
                    it.first.guts.toDouble() / it.second,
                    it.first.wisdom.toDouble() / it.second,
                )
            }

    val teamStatusRank: Map<StatusType, AoharuTeamStatusRank>
        get() {
            val average = teamAverageStatus
            return trainingType.associateWith { type ->
                Store.Aoharu.teamStatusRank.values.first {
                    average.get(type) >= it.threshold
                }
            }
        }

    fun getTraining(type: StatusType) = training.first { it.type == type }

    val totalRelation by lazy { member.sumOf { it.relation } }

    val totalTrainingLevel by lazy {
        uafStatus?.totalTrainingLevel ?: training.sumOf { it.currentLevel }
    }

    val hintFrequencyUp = gmStatus?.wisdomHintFrequency

    val forceHint = uafStatus?.forceHint ?: false

    val supportEventEffect = gmStatus?.wisdomSupportEventEffect

    val baseCalcInfo = Calculator.CalcInfo(
        chara = chara,
        // 必要に応じて利用側で変更する
        training = training[0].current,
        motivation = status.motivation,
        // 必要に応じて利用側で変更する
        member = support,
        scenario = scenario,
        supportCount = supportCount,
        fanCount = status.fanCount,
        currentStatus = status,
        totalRelation = totalRelation,
        // TODO スキルPt160ごとに速度スキル1つ取る想定。ヒント取れるかは知らん。
        speedSkillCount = min(5, status.skillPt / 160),
        // TODO スキルPt160ごとに回復スキル1つ取る想定。ヒント取れるかは知らん。速度と両方編成するとおかしくなる
        healSkillCount = min(3, status.skillPt / 160),
        // TODO スキルPt160ごとに加速スキル1つ取る想定。ヒント取れるかは知らん。速度と回復と両方編成するとおかしくなる
        accelSkillCount = min(3, status.skillPt / 160),
        totalTrainingLevel = totalTrainingLevel,
        isLevelUpTurn = isLevelUpTurn,
        liveStatus = liveStatus,
        gmStatus = gmStatus,
        lArcStatus = lArcStatus,
        uafStatus = uafStatus,
        cookStatus = cookStatus,
    )

    val specialityRateUp get() = liveStatus?.specialityRateUp ?: cookStatus?.cookPointEffect?.specialityRate ?: 0
}

data class MemberState(
    val index: Int,
    val card: SupportCard,
    val position: StatusType,
    val supportState: SupportState?,
    val scenarioState: ScenarioMemberState,
    val isScenarioLink: Boolean = supportState != null && scenarioState.scenarioLink.contains(card.chara),
    val additionalPosition: Set<StatusType> = emptySet(),
) {
    fun toShortString() =
        "MemberState($index, ${card.name}, scenarioState=${scenarioState.toShortString()}, position=$position, supportState=$supportState)"

    val name get() = if (guest) "(ゲスト)${charaName}" else card.name
    val charaName get() = card.chara
    val guest get() = supportState == null
    val relation get() = supportState?.relation ?: 0
    val friendTrainingEnabled get() = relation >= 80
    val friendCount get() = supportState?.friendCount ?: 0
    val outingType get() = card.type.outingType

    fun isFriendTraining(type: StatusType) = if (card.type == StatusType.GROUP) {
        supportState?.passion == true
    } else {
        friendTrainingEnabled && type == card.type
    }

    val hint = supportState?.hintIcon == true

    fun getTrainingRelation(charmValue: Int, trainingBonus: Int, hint: Boolean) =
        card.trainingRelation + charmValue + trainingBonus + if (hint) {
            5 + charmValue
        } else 0

    val positions by lazy { (additionalPosition + position).filter { it != StatusType.NONE } }
}

data class SupportState(
    val relation: Int,
    val hintIcon: Boolean,
    val passionTurn: Int,
    val friendCount: Int,
    val outingStep: Int = 0,
) {
    val passion get() = passionTurn > 0
    val outingEnabled = outingStep >= 2
}

open class ScenarioMemberState(val scenario: Scenario) {
    open val hintBlocked get() = false
    val scenarioLink get() = scenario.scenarioLink
    override fun toString() = scenario.name
    open fun toShortString() = toString()
}

object GrandLiveMemberState : ScenarioMemberState(Scenario.GRAND_LIVE)

data class AoharuMemberState(
    val member: TeamMemberData,
    val status: Status,
    val maxStatus: Status,
    val aoharuTrainingCount: Int,
    val aoharuIcon: Boolean,
) : ScenarioMemberState(Scenario.AOHARU) {
    val aoharuBurn get() = aoharuIcon && aoharuTrainingCount == 4
    override val hintBlocked get() = aoharuIcon
}

data object AoharuNotMemberState : ScenarioMemberState(Scenario.AOHARU)

data class TrainingState(
    val type: StatusType,
    val base: List<TrainingBase>,
    val level: Int,
    val count: Int,
    val levelOverride: Int?,
) {
    val currentLevel get() = levelOverride ?: level
    val current get() = base[currentLevel - 1]
}

data class EnableItem(
    val megaphone: MegaphoneItem? = null,
    val megaphoneTurn: Int = 0,
    val weight: WeightItem? = null,
    val raceBonus: RaceBonusItem? = null,
    val fanBonus: FanBonusItem? = null,
    val unique: UniqueItem? = null,
) {
    val list by lazy {
        listOfNotNull(megaphone, weight, raceBonus, fanBonus, unique)
    }
}
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
package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.rotation.RaceRotationCalculator
import io.github.mee1080.umasim.simulation.Calculator
import io.github.mee1080.umasim.simulation2.toMemberState
import io.github.mee1080.umasim.util.SaveDataConverter

enum class Page(val displayName: String, val icon: String) {
    Top("育成シミュレータ", "trending_up"),
    Rotation("ローテーションシミュレータ", "event_note"),
}

data class State(
    val page: Page = Page.Top,
    val rotationState: RotationState? = null,

    val scenario: Scenario = Scenario.CLIMAX,
    val chara: Chara = WebConstants.charaList[0],
    val supportSaveName: String = "",
    val supportLoadList: List<String> = emptyList(),
    val supportLoadName: String = "",
    val supportFilter: String = "",
    val appliedSupportFilter: String = "",
    val supportSortOrder: WebConstants.SortOrder<*> = WebConstants.supportSortOrder.first(),
    val filteredSupportList: List<Pair<Int, SupportCard?>> = WebConstants.displaySupportList,
    val supportSelectionList: List<SupportSelection> = Array(6) { SupportSelection() }.asList(),
    val teamJoinCount: Int = 0,
    val selectedTrainingType: Int = StatusType.SPEED.ordinal,
    val trainingLevel: Int = 1,
    val motivation: Int = 2,
    val fanCount: String = "1",
    val hp: Int = 50,
    val maxHp: Int = 100,
    val totalRelation: Int = 0,
    val shopItemMegaphone: Int = -1,
    val shopItemWeight: Int = -1,
    val trainingResult: Status = Status(),
    val trainingItemBonus: Status = Status(),
    val trainingImpact: List<Pair<String, Status>> = emptyList(),
    val expectedResult: ExpectedStatus = ExpectedStatus(),
    val upperRate: Double = 0.0,
//    val coinRate: Double = 0.0,
    val raceSetting: List<RaceSetting> = emptyList(),
    val totalRaceBonus: Int = 0,
    val totalFanBonus: Int = 0,
    val initialStatus: Status = Status(),
    val availableHint: Map<String, List<String>> = mapOf(),
    val simulationMode: Int = 0,
    val simulationTurn: Int = 78,
    val simulationResult: Status = Status(),
    val simulationHistory: List<Pair<String, Status>> = emptyList(),
    val aoharuSimulationState: AoharuSimulationState = AoharuSimulationState(),
) {

    val supportFilterApplied get() = supportFilter == appliedSupportFilter

    fun getSupportSelection(position: Int): List<Pair<Int, SupportCard?>> {
        val selection = supportSelectionList.getOrNull(position) ?: return emptyList()
        return if (appliedSupportFilter.isEmpty()) filteredSupportList else {
            val selectedCard = selection.card
            if (selectedCard != null && filteredSupportList.firstOrNull { it.first == selection.selectedSupport } == null) {
                listOf(selectedCard.id to selectedCard) + filteredSupportList
            } else {
                filteredSupportList
            }
        }.filter { selection.statusType == StatusType.NONE || it.second?.type == selection.statusType }
            .sortedWith(supportSortOrder)
    }

    fun isFriendTraining(position: Int): Boolean {
        val selection = supportSelectionList.getOrNull(position) ?: return false
        return selection.friend && selectedTrainingType == selection.card?.type?.ordinal
    }

    fun calcRaceStatus(race: RaceSetting): Pair<Int, Int> {
        val baseStatus = calcRaceStatus(race.statusValue)
        val baseSkillPt = calcRaceStatus(race.skillPt)
        var itemCount = 0
        var totalStatus = 0
        var totalSkillPt = 0
        race.item.forEach { (label, strCount) ->
            val count = strCount.toIntOrNull() ?: 0
            itemCount += count
            val multiplier = WebConstants.raceItem[label]!!
            totalStatus += count * (baseStatus * multiplier).toInt() * race.statusCount
            totalSkillPt += count * (baseSkillPt * multiplier).toInt()
        }
        val raceCount = race.raceCount.toIntOrNull() ?: 0
        totalStatus += (raceCount - itemCount) * baseStatus * race.statusCount
        totalSkillPt += (raceCount - itemCount) * baseSkillPt
        return totalStatus to totalSkillPt
    }

    fun calcRaceStatus(value: Int) = (value * (1 + totalRaceBonus / 100.0)).toInt()
}

data class SupportSelection(
    val selectedSupport: Int = WebConstants.notSelected.first,
    val supportTalent: Int = 4,
    val join: Boolean = true,
    val relation: Int = 0,
    val passion: Boolean = false,
    val friendCount: Int = 0,
) {
    companion object {
        fun fromSaveInfo(info: SaveDataConverter.SupportInfo) = SupportSelection(
            selectedSupport = info.id,
            supportTalent = info.talent,
            join = info.join,
            relation = info.relation,
        )
    }

    fun toSaveInfo() = SaveDataConverter.SupportInfo(selectedSupport, supportTalent, join, relation)

    val card get() = WebConstants.supportMap[selectedSupport]?.firstOrNull { it.talent == supportTalent }

    val statusType get() = card?.type ?: StatusType.NONE

    val name get() = card?.name ?: "未選択"

    val isSelected get() = card != null

    val initialRelation get() = card?.initialRelation ?: 0

    val relationSelection = card?.targetRelation ?: listOf(0, 80)

    val friend get() = relation >= 80

    val relationUpCount
        get() = if (card?.type?.outingType == true) {
            (60 - initialRelation - 1) / 4 + 1
        } else {
            (80 - initialRelation - 1) / 7 + 1
        }

    val specialtyRate
        get() = card?.let { card ->
            calcRate(card.type, *Calculator.calcCardPositionSelection(card))
        } ?: 0.0

    val hintRate
        get() = card?.let { card ->
            if (card.type.outingType) 0.0 else card.hintFrequency
        } ?: 0.0

    fun toMemberState(scenario: Scenario, index: Int) = card?.toMemberState(
        scenario, index, relation, passion, friendCount,
    )
}

data class AoharuSimulationState(
    val simulationMode: Int = 0,
    val simulationTurn: Int = 65,
    val simulationHistory: List<HistoryItem> = emptyList(),
)

data class HistoryItem(
    val action: String,
    val charaStatus: Status,
    val teamTotalStatus: Status,
    val teamAverageStatus: ExpectedStatus,
    val teamStatusRank: Map<StatusType, AoharuTeamStatusRank>,
) {
    val next = trainingType.associateWith { type ->
        val nextRank = teamStatusRank[type]!!.next
        if (nextRank == null) 0.0 else nextRank.threshold - teamAverageStatus.get(type)
    }
}

data class RaceSetting(
    val label: String,
    val raceCount: String,
    val statusValue: Int,
    val statusCount: Int,
    val skillPt: Int,
    val editable: Boolean,
    val item: Map<String, String> = emptyMap(),
) {
    constructor(
        label: String,
        raceCount: Int,
        statusValue: Int,
        statusCount: Int,
        skillPt: Int,
        editable: Boolean,
        item: Map<String, String> = emptyMap(),
    ) : this(
        label, raceCount.toString(), statusValue, statusCount, skillPt, editable, item,
    )
}

data class RotationState(
    val calcState: RaceRotationCalculator.State,
    val raceSelection: List<List<RaceEntry>>,
    val achievementList: List<RaceAchievement>,
    val groundSetting: Map<RaceGround, RaceRotationCalculator.Rank>,
    val distanceSetting: Map<RaceDistance, RaceRotationCalculator.Rank>,
    val option: RaceRotationCalculator.Option,
    val charaSelection: List<Pair<Int, String>>,
    val recommendFilter: RecommendFilter = NoFilter,
    val rotationSaveName: String = "",
    val rotationLoadList: List<String> = emptyList(),
    val rotationLoadName: String = "",
) {
    val selectedChara = calcState.charaId
    val rotation = calcState.rotation
    val selectedRace = rotation.list
    val raceCount = selectedRace.count { it != null }
    val raceType = rotation.raceType
    val recommendation = calcState.recommendation.filter { recommendFilter(it) }
}

sealed interface RecommendFilter {
    operator fun invoke(entry: Triple<RaceEntry, Int, List<Pair<String, Int?>>>): Boolean
}

object NoFilter : RecommendFilter {
    override fun invoke(entry: Triple<RaceEntry, Int, List<Pair<String, Int?>>>) = true
    override fun toString() = "全て"
}

class TurnJustFilter(val turn: Int) : RecommendFilter {
    override fun invoke(entry: Triple<RaceEntry, Int, List<Pair<String, Int?>>>) = entry.first.turn == turn
    override fun toString() = turnToString(turn)
}

class TurnAfterFilter(val turn: Int) : RecommendFilter {
    override fun invoke(entry: Triple<RaceEntry, Int, List<Pair<String, Int?>>>) = entry.first.turn >= turn
    override fun toString() = turnToString(turn) + " 以降"
}

class AchievementFilter(val name: String) : RecommendFilter {
    override fun invoke(entry: Triple<RaceEntry, Int, List<Pair<String, Int?>>>) = entry.third.any { it.first == name }
    override fun toString() = name
}
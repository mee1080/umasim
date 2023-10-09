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
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.toMemberState
import io.github.mee1080.umasim.util.SaveDataConverter
import io.github.mee1080.umasim.util.replace

enum class Page(val displayName: String, val icon: String) {
    Top("トレーニング", "trending_up"),
    Rotation("ローテーション", "event_note"),
    Lesson("レッスン", "queue_music"),
}

data class State(
    val page: Page = Page.Top,
    val rotationState: RotationState? = null,
    val lessonState: LessonState = LessonState(),

    val divideMode: Boolean = false,
    val scenario: Scenario = Scenario.LARC,
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
    val selectedTrainingType: StatusType = StatusType.SPEED,
    val trainingLevel: Int = 1,
    val motivation: Int = 2,
    val fanCount: Int = 0,
    val hp: Int = 50,
    val maxHp: Int = 100,
    val totalRelation: Int = 0,
    val speedSkillCount: Int = 0,
    val healSkillCount: Int = 0,
    val totalTrainingLevel: Int = 5,
    val shopItemMegaphone: MegaphoneItem = WebConstants.dummyMegaphoneItem,
    val shopItemWeight: WeightItem = WebConstants.dummyWeightItem,
    val trainingResult: Status = Status(),
    val trainingItemBonus: Status = Status(),
    val trainingPerformanceValue: Int = 0,
    val trainingImpact: List<Pair<String, Status>> = emptyList(),
    val expectedResult: ExpectedStatus = ExpectedStatus(),
    val upperRate: Double = 0.0,
//    val coinRate: Double = 0.0,
    val friendProbability: Double = 0.0,
    val raceSetting: List<RaceSetting> = emptyList(),
    val totalRaceBonus: Int = 0,
    val totalFanBonus: Int = 0,
    val initialStatus: Status = Status(),
    val availableHint: Map<String, List<String>> = mapOf(),
    val simulationMode: Int = 0,
    val simulationResult: Status = Status(),
    val simulationHistory: List<Pair<String, Status>> = emptyList(),
    val aoharuSimulationState: AoharuSimulationState = AoharuSimulationState(),
    val trainingLiveState: TrainingLiveState = TrainingLiveState(),
    val expectedState: ExpectedState = ExpectedState(),
    val gmState: GmState = GmState(),
    val lArcState: LArcState = LArcState(),
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
        return selection.friend && selection.join && selectedTrainingType == selection.card?.type
    }

    val friendTraining: Boolean by lazy { (0..6).any { isFriendTraining(it) } }

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

    val trainingLiveStateIfEnabled get() = if (scenario == Scenario.GRAND_LIVE) trainingLiveState else null

    val gmStatusIfEnabled get() = if (scenario == Scenario.GM) gmState.toGmStatus() else null

    val lArcStatusIfEnabled get() = if (scenario == Scenario.LARC) lArcState.toLArcStatus() else null
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
            calcRate(card.type, *Calculator.calcCardPositionSelection(card, 0))
        } ?: 0.0

    val hintRate
        get() = card?.let { card ->
            if (card.type.outingType) 0.0 else card.hintFrequency
        } ?: 0.0

    fun toMemberState(scenario: Scenario, index: Int) = card?.toMemberState(
        scenario, index, relation, if (passion) 1 else 0, friendCount,
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

data class TrainingLiveState(
    val speed: String = "0",
    val stamina: String = "0",
    val power: String = "0",
    val guts: String = "0",
    val wisdom: String = "0",
    val skillPt: String = "0",
    val friendTrainingUpInput: String = "0",
    val specialityRateUpInput: String = "0",
) : TrainingLiveStatus {
    override val friendTrainingUp: Int get() = friendTrainingUpInput.toIntOrNull() ?: 0
    override val specialityRateUp: Int get() = specialityRateUpInput.toIntOrNull() ?: 0
    override fun trainingUp(type: StatusType) = when (type) {
        StatusType.SPEED -> speed
        StatusType.STAMINA -> stamina
        StatusType.POWER -> power
        StatusType.GUTS -> guts
        StatusType.WISDOM -> wisdom
        StatusType.SKILL -> skillPt
        else -> "0"
    }.toIntOrNull() ?: 0
}

data class ExpectedState(
    val status: ExpectedStatus? = null,
    val typeRateList: List<Pair<StatusType, Double>> = emptyList(),
    val statusHistory: List<Pair<ExpectedStatus, List<Pair<StatusType, Double>>>> = emptyList(),

    val teamJoinCount: String = "0",
    val levelSpeed: Int = 3,
    val levelStamina: Int = 3,
    val levelPower: Int = 3,
    val levelGuts: Int = 3,
    val levelWisdom: Int = 3,
    val evaluateSpeed: String = "100",
    val evaluateStamina: String = "100",
    val evaluatePower: String = "100",
    val evaluateGuts: String = "100",
    val evaluateWisdom: String = "100",
    val evaluateSkillPt: String = "100",
    val evaluateHp: String = "70",
    val evaluatePerformance: String = "50",
    val evaluateRelation: String = "1000",
) {
    val targetTypes by lazy {
        listOfNotNull(
            if (levelSpeed > 0) StatusType.SPEED else null,
            if (levelStamina > 0) StatusType.STAMINA else null,
            if (levelPower > 0) StatusType.POWER else null,
            if (levelGuts > 0) StatusType.GUTS else null,
            if (levelWisdom > 0) StatusType.WISDOM else null,
        )
    }

    fun getTraining(scenario: Scenario): Map<StatusType, TrainingBase> {
        return WebConstants.trainingInfo[scenario]!!.mapValues { entry ->
            entry.value.base.firstOrNull {
                it.level == when (it.type) {
                    StatusType.SPEED -> levelSpeed
                    StatusType.STAMINA -> levelStamina
                    StatusType.POWER -> levelPower
                    StatusType.GUTS -> levelGuts
                    StatusType.WISDOM -> levelWisdom
                    else -> -1
                }
            } ?: entry.value.base.first()
        }
    }

    fun createEvaluator(): (Status, Int) -> Int {
        val evaluateSpeed = evaluateSpeed.toIntOrNull() ?: 0
        val evaluateStamina = evaluateStamina.toIntOrNull() ?: 0
        val evaluatePower = evaluatePower.toIntOrNull() ?: 0
        val evaluateGuts = evaluateGuts.toIntOrNull() ?: 0
        val evaluateWisdom = evaluateWisdom.toIntOrNull() ?: 0
        val evaluateSkillPt = evaluateSkillPt.toIntOrNull() ?: 0
        val evaluateHp = evaluateHp.toIntOrNull() ?: 0
        val evaluatePerformance = evaluatePerformance.toIntOrNull() ?: 0
        val evaluateRelation = evaluateRelation.toIntOrNull() ?: 0
        return { status, needRelationCount ->
            status.speed * evaluateSpeed +
                    status.stamina * evaluateStamina +
                    status.power * evaluatePower +
                    status.guts * evaluateGuts +
                    status.wisdom * evaluateWisdom +
                    status.skillPt * evaluateSkillPt +
                    status.hp * evaluateHp +
                    (status.performance?.totalValue ?: 0) * evaluatePerformance +
                    needRelationCount * evaluateRelation
        }
    }

    fun evaluate(status: ExpectedStatus): Double {
        val evaluateSpeed = evaluateSpeed.toIntOrNull() ?: 0
        val evaluateStamina = evaluateStamina.toIntOrNull() ?: 0
        val evaluatePower = evaluatePower.toIntOrNull() ?: 0
        val evaluateGuts = evaluateGuts.toIntOrNull() ?: 0
        val evaluateWisdom = evaluateWisdom.toIntOrNull() ?: 0
        val evaluateSkillPt = evaluateSkillPt.toIntOrNull() ?: 0
        val evaluateHp = evaluateHp.toIntOrNull() ?: 0
        val evaluatePerformance = evaluatePerformance.toIntOrNull() ?: 0
        return status.speed * evaluateSpeed +
                status.stamina * evaluateStamina +
                status.power * evaluatePower +
                status.guts * evaluateGuts +
                status.wisdom * evaluateWisdom +
                status.skillPt * evaluateSkillPt +
                status.hp * evaluateHp +
                (status.performance?.totalValue ?: 0.0) * evaluatePerformance
    }
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

data class LessonState(
    val periodIndex: Int = 0,
    val dance: String = "10",
    val passion: String = "10",
    val vocal: String = "10",
    val visual: String = "10",
    val mental: String = "10",
    val stepCount: Int = 2,
    val threshold: String = "0.001",

    val message: String? = null,
    val result: List<Double> = emptyList(),

    val periodList: List<Pair<Int, String>> = LessonPeriod.entries.map { it.ordinal to it.displayName }
) {
    val period get() = LessonPeriod.entries.getOrElse(periodIndex) { LessonPeriod.Junior }

    fun convertParameters() = kotlin.runCatching {
        Pair(
            Performance(
                dance.toInt(),
                passion.toInt(),
                vocal.toInt(),
                visual.toInt(),
                mental.toInt(),
            ),
            threshold.toDouble(),
        )
    }.getOrNull()
}

data class GmState(
    val knowledgeTable: List<Knowledge?> = List(14) { null },
    val wisdom: Founder? = null,
    val wisdomLevel: Map<Founder, Int> = Founder.entries.associateWith { 0 }
) {
    fun toGmStatus() = GmStatus(
        knowledgeTable1 = knowledgeTable.subList(0, 8).filterNotNull(),
        knowledgeTable2 = knowledgeTable.subList(8, 12).filterNotNull(),
        knowledgeTable3 = knowledgeTable.subList(12, 14).filterNotNull(),
        activeWisdom = wisdom,
        wisdomLevel = wisdomLevel,
    )

    fun updateType(index: Int, value: StatusType?): GmState {
        val knowledge = if (value == null) null else {
            val bonus = knowledgeTable[index]?.bonus ?: if (index >= 8) 2 else 1
            Knowledge(Founder.Red, value, bonus)
        }
        return copy(knowledgeTable = knowledgeTable.replace(index) { knowledge })
    }

    fun updateBonus(index: Int, value: Int): GmState {
        val type = knowledgeTable[index]?.type ?: return this
        val knowledge = Knowledge(Founder.Red, type, value)
        return copy(knowledgeTable = knowledgeTable.replace(index) { knowledge })
    }

    fun clearKnowledge(): GmState {
        return copy(knowledgeTable = List(14) { null })
    }

    fun updateWisdomLevel(target: Founder, value: Int): GmState {
        val newWisdomLevel = wisdomLevel.mapValues { if (it.key == target) value else it.value }
        return copy(wisdomLevel = newWisdomLevel)
    }
}

data class LArcState(
    val expectations: Int = 0,
    val overseas: Boolean = false,
    val overseasTurfAptitude: Int = 0,
    val longchampAptitude: Int = 0,
    val lifeRhythm: Int = 0,
    val nutritionManagement: Int = 0,
    val frenchSkill: Int = 0,
    val overseasExpedition: Int = 0,
    val strongHeart: Int = 0,
    val mentalStrength: Int = 0,
    val hopeOfLArc: Int = 0,
) {
    fun toLArcStatus() = LArcStatus(
        supporterPt = expectations * 1700,
        overseasTurfAptitude = overseasTurfAptitude,
        longchampAptitude = longchampAptitude,
        lifeRhythm = lifeRhythm,
        nutritionManagement = nutritionManagement,
        frenchSkill = frenchSkill,
        overseasExpedition = overseasExpedition,
        strongHeart = strongHeart,
        mentalStrength = mentalStrength,
        hopeOfLArc = hopeOfLArc,
    )
}
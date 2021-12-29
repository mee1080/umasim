package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation.Calculator
import io.github.mee1080.umasim.simulation.ExpectedStatus
import io.github.mee1080.umasim.util.SaveDataConverter

data class State(
    val selectedScenario: Int = Scenario.AOHARU.ordinal,
    val selectedChara: Int = WebConstants.displayCharaList[0].first,
    val supportFilter: String = "",
    val appliedSupportFilter: String = "",
    val filteredSupportList: List<Triple<Int, String, String>> = WebConstants.displaySupportList,
    val supportSelectionList: List<SupportSelection> = Array(6) { SupportSelection() }.asList(),
    val teamJoinCount: Int = 0,
    val selectedTrainingType: Int = StatusType.SPEED.ordinal,
    val trainingLevel: Int = 1,
    val motivation: Int = 2,
    val trainingResult: Status = Status(),
    val trainingImpact: List<Pair<String, Status>> = emptyList(),
    val expectedResult: ExpectedStatus = ExpectedStatus(),
    val totalRaceBonus: Int = 0,
    val totalFanBonus: Int = 0,
    val initialStatus: Status = Status(),
    val availableHint: Map<String, List<String>> = mapOf(),
    val simulationMode: Int = 0,
    val simulationTurn: Int = 55,
    val simulationResult: Status = Status(),
    val simulationHistory: List<String> = emptyList(),
    val aoharuSimulationState: AoharuSimulationState = AoharuSimulationState(),
) {

    val scenario get() = Scenario.values().getOrElse(selectedScenario) { Scenario.AOHARU }

    val chara get() = WebConstants.charaMap[selectedChara]!!

    val supportFilterApplied get() = supportFilter == appliedSupportFilter

    fun getSupportSelection(position: Int): List<Triple<Int, String, String>> {
        return if (appliedSupportFilter.isEmpty()) filteredSupportList else {
            val selection = supportSelectionList.getOrNull(position) ?: return emptyList()
            val selectedCard = selection.card
            if (selectedCard != null && filteredSupportList.firstOrNull { it.first == selection.selectedSupport } == null) {
                listOf(WebConstants.getDisplayItem(selectedCard), *filteredSupportList.toTypedArray())
            } else {
                filteredSupportList
            }
        }
    }

    fun isFriendTraining(position: Int): Boolean {
        val selection = supportSelectionList.getOrNull(position) ?: return false
        return selection.friend && selectedTrainingType == selection.card?.type?.ordinal
    }
}

data class SupportSelection(
    val selectedSupport: Int = WebConstants.notSelected.first,
    val supportTalent: Int = 4,
    val join: Boolean = true,
    val friend: Boolean = true,
) {
    companion object {
        fun fromSaveInfo(info: SaveDataConverter.SupportInfo) = SupportSelection(
            selectedSupport = info.id,
            supportTalent = info.talent,
            join = info.join,
            friend = info.friend,
        )
    }

    fun toSaveInfo() = SaveDataConverter.SupportInfo(selectedSupport, supportTalent, join, friend)

    val card get() = WebConstants.supportMap[selectedSupport]?.firstOrNull { it.talent == supportTalent }

    val name get() = card?.name ?: "未選択"

    val isSelected get() = card != null

    val initialRelation get() = card?.initialRelation ?: 0

    val relationUpCount
        get() = if (card?.type == StatusType.FRIEND) {
            (60 - initialRelation - 1) / 4 + 1
        } else {
            (81 - initialRelation - 1) / 7 + 1
        }

    val specialtyRate
        get() = card?.let { card ->
            calcRate(card.type, *Calculator.calcCardPositionSelection(card))
        } ?: 0.0

    val hintRate
        get() = card?.let { card ->
            if (card.type == StatusType.FRIEND) 0.0 else card.hintFrequency
        } ?: 0.0
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
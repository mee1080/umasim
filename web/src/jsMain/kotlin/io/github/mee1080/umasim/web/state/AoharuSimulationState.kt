package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.aoharu.AoharuTeamStatusRank

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

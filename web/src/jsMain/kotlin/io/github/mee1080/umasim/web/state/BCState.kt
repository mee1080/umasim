package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.bc.BCStatus
import io.github.mee1080.umasim.scenario.bc.BCTeamMember
import io.github.mee1080.umasim.scenario.bc.BCTeamParameter

data class BCState(
    val teamMembers: List<BCMemberState> = listOf(
        BCMemberState("メンバー1"),
        BCMemberState("メンバー2"),
        BCMemberState("メンバー3")
    ),
    val physicalLevel: Int = 1,
    val techniqueLevel: Int = 1,
    val mentalLevel: Int = 1,
    val dreamsTrainingActive: Boolean = false,
) {
    fun toBCStatus(selectedTrainingType: StatusType) = BCStatus(
        teamMember = teamMembers.map { it.toBCTeamMember(selectedTrainingType) },
        teamParameter = mapOf(
            BCTeamParameter.Physical to physicalLevel,
            BCTeamParameter.Technique to techniqueLevel,
            BCTeamParameter.Mental to mentalLevel,
        ),
        dreamsTrainingActive = dreamsTrainingActive
    )
}

data class BCMemberState(
    val charaName: String,
    val memberRank: Int = 0,
    val dreamGaugeMax: Boolean = false,
    val placed: Boolean = false,
) {
    fun toBCTeamMember(currentPosition: StatusType) = BCTeamMember(
        charaName = charaName,
        dreamGauge = if (dreamGaugeMax) 3 else 0,
        memberRank = memberRank,
        position = if (placed) currentPosition else StatusType.NONE
    )
}

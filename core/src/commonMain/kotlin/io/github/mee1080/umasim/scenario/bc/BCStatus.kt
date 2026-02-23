package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState

fun SimulationState.updateBCStatus(update: BCStatus.() -> BCStatus): SimulationState {
    val bcStatus = this.bcStatus ?: return this
    return copy(scenarioStatus = bcStatus.update())
}

/**
 * BCシナリオ固有の状態を保持するクラス。
 */
data class BCStatus(
    val teamMember: List<BCTeamMember> = emptyList(),
    val casinoRarity: Int = 0,
    val dreamsPoint: Int = 0,
    val teamParameter: Map<BCTeamParameter, Int> = BCTeamParameter.entries.associateWith { 1 },
    val dreamsTrainingCount: Int = 0,
    val dreamsTrainingActive: Boolean = false,
) : ScenarioStatus {

    val teamRank by lazy { teamMember.minOfOrNull { it.memberRank } ?: 0 }
    val teamRankEffect get() = teamRankEffects.getOrElse(teamRank) { teamRankEffects.last() }

    val friendBonus
        get() = teamRankEffect.friendBonus + if (dreamsTrainingActive) {
            physicalFriendBonus[teamParameter[BCTeamParameter.Physical]!!]
        } else 0

    fun teamMemberIn(position: StatusType) =
        if (dreamsTrainingActive) teamMember else teamMember.filter { it.position == position }

    fun trainingEffect(position: StatusType) = teamMemberIn(position).sumOf { it.trainingEffect }

    val subParameterRate get() = if (dreamsTrainingActive) 10 + physicalSubParameter[teamParameter[BCTeamParameter.Physical]!!] else 100

    val hpCostDown get() = if (dreamsTrainingActive) physicalHpCostDown[teamParameter[BCTeamParameter.Physical]!!] else 0

    val skillPtEffect get() = if (dreamsTrainingActive) techniqueSkillPtEffect[teamParameter[BCTeamParameter.Technique]!!] else 0

    val minHintCount get() = if (dreamsTrainingActive) techniqueMinHintCount[teamParameter[BCTeamParameter.Technique]!!] else 0

    val hintAll get() = dreamsTrainingActive && techniqueHintAll[teamParameter[BCTeamParameter.Technique]!!] > 0

    val trainingRelationUp get() = if (dreamsTrainingActive) memtalRelationUp[teamParameter[BCTeamParameter.Mental]!!] else 0

    val failureRateDown get() = if (dreamsTrainingActive) memtalFailureRateDown[teamParameter[BCTeamParameter.Mental]!!] else 0

    val mainLimitUp get() = if (dreamsTrainingActive) mentalMainLimitUp[teamParameter[BCTeamParameter.Mental]!!] else 0

    val skillPtLimitUp get() = if (dreamsTrainingActive) mentalSkillPtLimitUp[teamParameter[BCTeamParameter.Mental]!!] else 0
}

data class BCTeamMember(
    val charaName: String,
    val dreamGauge: Int = 0,
    val memberRank: Int = 0,
    val position: StatusType = StatusType.NONE,
) {
    val memberRankString get() = rankToString[memberRank]

    val memberRankEffect get() = memberRankEffects.getOrElse(memberRank) { memberRankEffects.last() }

    val trainingEffect get() = memberRankEffect.trainingEffect + if (dreamGauge == 3) memberRankEffect.maxTrainingEffect else 0
}

data class BCMemberRankEffect(
    val trainingEffect: Int,
    val maxTrainingEffect: Int,
)

data class BCTeamRankEffect(
    val friendBonus: Int,
    val specialityRateUp: Int,
    val hintFrequencyUp: Int,
)

enum class BCTeamParameter(val displayName: String) {
    Physical("フィジカル"),
    Technique("テクニック"),
    Mental("メンタル"),
}

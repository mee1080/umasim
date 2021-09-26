package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*

data class SimulationState(
    val scenario: Scenario,
    val chara: Chara,
    val member: List<MemberState>,
    val training: List<TrainingState>,
    val levelUpTurns: Collection<Int>,
    val turn: Int,
    val status: Status,
    val condition: List<String>,
) {
    val charm get() = condition.contains("愛嬌○")
    val isLevelUpTurn get() = levelUpTurns.contains(turn)
}

data class MemberState(
    val index: Int,
    val card: SupportCard,
    val position: StatusType,
    val supportState: SupportState?,
    val scenarioState: ScenarioMemberState,
) {
    val name get() = card.name
    val guest get() = supportState == null
    val friendTrainingEnabled get() = (supportState?.relation ?: 0) > 80
    fun isFriendTraining(type: StatusType) = friendTrainingEnabled && type == card.type
    fun getFriendBonus(type: StatusType) = if (isFriendTraining(type)) card.friendFactor else 1.0
    val wisdomFriendRecovery get() = if (isFriendTraining(StatusType.WISDOM)) card.wisdomFriendRecovery else 0
    val hint = supportState?.hintIcon == true
    fun getTrainingRelation(charm: Boolean) = getTrainingRelation(if (charm) 2 else 0)
    private fun getTrainingRelation(charmValue: Int) = card.trainingRelation + charmValue + if (hint) {
        5 + charmValue
    } else 0
}

data class SupportState(
    val relation: Int,
    val hintIcon: Boolean,
)

sealed interface ScenarioMemberState {
    val hintBlocked get() = false
}

object UraMemberState : ScenarioMemberState {
    override fun toString() = "URA"
}

data class AoharuMemberState(
    val member: TeamMemberData,
    val status: Status,
    val maxStatus: Status,
    val aoharuTrainingCount: Int,
    val aoharuIcon: Boolean,
) : ScenarioMemberState {
    val aoharuBurn get() = aoharuIcon && aoharuTrainingCount == 4
    override val hintBlocked get() = aoharuIcon
}

data class TrainingState(
    val type: StatusType,
    val base: List<TrainingBase>,
    val level: Int,
    val count: Int,
    val levelOverride: Int?
) {
    val currentLevel get() = levelOverride ?: level
    val current get() = base[currentLevel - 1]
}
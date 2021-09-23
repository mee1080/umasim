package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*

data class SimulationState(
    val scenario: Scenario,
    val chara: Chara,
    val member: List<MemberState>,
    val training: List<TrainingState>,
    val turn: Int,
    val status: Status,
    val condition: List<String>,
) {

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
    }

    data class SupportState(
        val relation: Int,
        val hintIcon: Boolean,
    )

    sealed interface ScenarioMemberState

    object UraMemberState : ScenarioMemberState {
        override fun toString() = "URA"
    }

    data class AoharuMemberState(
        val member: TeamMemberData,
        val status: Status,
        val aoharuTrainingCount: Int,
        val aoharuIcon: Boolean,
    ) : ScenarioMemberState {
        val aoharuBurn get() = aoharuIcon && aoharuTrainingCount == 4
    }

    data class TrainingState(
        val type: StatusType,
        val base: List<TrainingBase>,
        val level: Int,
        val count: Int,
        val levelOverride: Int?
    ) {
        val current = base[(levelOverride ?: level) - 1]
    }
}
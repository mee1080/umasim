package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.live.TrainingLiveStatus

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
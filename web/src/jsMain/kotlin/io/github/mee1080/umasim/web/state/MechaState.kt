package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.mecha.MechaChipType
import io.github.mee1080.umasim.scenario.mecha.MechaLinkEffect
import io.github.mee1080.umasim.scenario.mecha.MechaStatus
import io.github.mee1080.umasim.scenario.mecha.updateTurn

data class MechaState(
    val learningSpeed: Int = 1,
    val learningStamina: Int = 1,
    val learningPower: Int = 1,
    val learningGuts: Int = 1,
    val learningWisdom: Int = 1,

    val chipLevelHead1: Int = 0,
    val chipLevelHead2: Int = 0,
    val chipLevelHead3: Int = 0,
    val chipLevelBody1: Int = 0,
    val chipLevelBody2: Int = 0,
    val chipLevelBody3: Int = 0,
    val chipLevelLeg1: Int = 0,
    val chipLevelLeg2: Int = 0,
    val chipLevelLeg3: Int = 0,

    val gear: Boolean = false,
    val phase: MechaPhase = MechaPhase.Junior1,
    val overdrive: Boolean = false,

    val learningLevelGain: Status = Status(),
) {
    fun toMechaStatus(charaNames: List<String>) = MechaStatus(
        linkEffects = MechaLinkEffect(charaNames),
        learningLevels = mapOf(
            StatusType.SPEED to learningSpeed,
            StatusType.STAMINA to learningStamina,
            StatusType.POWER to learningPower,
            StatusType.GUTS to learningGuts,
            StatusType.WISDOM to learningWisdom
        ),
        chipLevels = mapOf(
            MechaChipType.HEAD to listOf(chipLevelHead1, chipLevelHead2, chipLevelHead3),
            MechaChipType.BODY to listOf(chipLevelBody1, chipLevelBody2, chipLevelBody3),
            MechaChipType.LEG to listOf(chipLevelLeg1, chipLevelLeg2, chipLevelLeg3),
        ),
        gearExists = trainingType.associateWith { gear },
    ).updateTurn(phase.turn).copy(overdrive = overdrive)
}

enum class MechaPhase(val label: String, val turn: Int) {
    Junior1("ジュニア前半", 1),
    Junior2("ジュニア後半", 13),
    Classic1("クラシック前半", 25),
    Classic2("クラシック後半", 37),
    Senior2("シニア前半", 49),
    Senior1("シニア後半", 61),
    Finals("ファイナルズ", 73),
}

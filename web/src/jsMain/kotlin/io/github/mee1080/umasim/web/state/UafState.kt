package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.uaf.UafAthletic
import io.github.mee1080.umasim.scenario.uaf.UafGenre
import io.github.mee1080.umasim.scenario.uaf.UafStatus

data class UafState(
    val trainingGenre: UafGenre = UafGenre.Blue,
    val selectedTrainingType: StatusType = StatusType.SPEED,
    val speedAthleticLevel: Int = 1,
    val staminaAthleticLevel: Int = 1,
    val powerAthleticLevel: Int = 1,
    val gutsAthleticLevel: Int = 1,
    val wisdomAthleticLevel: Int = 1,
    val speedAthleticLevelUp: Int = 3,
    val staminaAthleticLevelUp: Int = 3,
    val powerAthleticLevelUp: Int = 3,
    val gutsAthleticLevelUp: Int = 3,
    val wisdomAthleticLevelUp: Int = 3,
    val linkSpeed: Boolean = false,
    val linkStamina: Boolean = false,
    val linkPower: Boolean = false,
    val linkGuts: Boolean = false,
    val linkWisdom: Boolean = false,
    val blueFestivalBonus: Int = 0,
    val redFestivalBonus: Int = 0,
    val yellowFestivalBonus: Int = 0,
    val heatUpBlue: Boolean = false,
    val heatUpRed: Boolean = false,
    val heatUpYellow: Boolean = false,
    val athleticsLevelUpBonus: Boolean = false,
    val athleticsLevelUpRate: List<Pair<Int, Double>> = emptyList(),
    val expectedAthleticsLevelUp: Double = 0.0,
    val athleticsLevelUpCalculating: Boolean = false,
) {
    val trainingName get() = UafAthletic.byStatusType[selectedTrainingType]!!.first { it.genre == trainingGenre }.longDisplayName

    val selectedTrainingLevel: Int
        get() {
            val athleticLevel = when (selectedTrainingType) {
                StatusType.SPEED -> speedAthleticLevel
                StatusType.STAMINA -> staminaAthleticLevel
                StatusType.POWER -> powerAthleticLevel
                StatusType.GUTS -> gutsAthleticLevel
                StatusType.WISDOM -> wisdomAthleticLevel
                else -> 0
            }
            return when {
                athleticLevel >= 50 -> 5
                athleticLevel >= 40 -> 4
                athleticLevel >= 30 -> 3
                athleticLevel >= 20 -> 2
                else -> 1
            }
        }

    fun toUafStatus(): UafStatus {
        val athletics = trainingType.associateWith { type ->
            if (checkJoin(type)) {
                UafAthletic.byStatusType[type]!!.first { it.genre == trainingGenre }
            } else {
                UafAthletic.byStatusType[type]!!.first { it.genre != trainingGenre }
            }
        }
        val athleticsLevel = mapOf(
            athletics[StatusType.SPEED]!! to speedAthleticLevel,
            athletics[StatusType.STAMINA]!! to staminaAthleticLevel,
            athletics[StatusType.POWER]!! to powerAthleticLevel,
            athletics[StatusType.GUTS]!! to gutsAthleticLevel,
            athletics[StatusType.WISDOM]!! to wisdomAthleticLevel,
        )
        val heatUp = mapOf(
            UafGenre.Blue to if (heatUpBlue) 2 else 0,
            UafGenre.Red to if (heatUpRed) 2 else 0,
            UafGenre.Yellow to if (heatUpYellow) 2 else 0,
        )
        val festivalBonus = blueFestivalBonus + redFestivalBonus + yellowFestivalBonus
        val athleticLevelUp = mapOf(
            StatusType.SPEED to speedAthleticLevelUp,
            StatusType.STAMINA to staminaAthleticLevelUp,
            StatusType.POWER to powerAthleticLevelUp,
            StatusType.GUTS to gutsAthleticLevelUp,
            StatusType.WISDOM to wisdomAthleticLevelUp,
        )
        return UafStatus(
            athleticsLevel = athleticsLevel,
            trainingAthletics = athletics,
            heatUp = heatUp,
            festivalBonus = festivalBonus,
            athleticsLevelUp = athleticLevelUp,
        )
    }

    private fun checkJoin(type: StatusType): Boolean {
        return type == selectedTrainingType || when (type) {
            StatusType.SPEED -> linkSpeed
            StatusType.STAMINA -> linkStamina
            StatusType.POWER -> linkPower
            StatusType.GUTS -> linkGuts
            StatusType.WISDOM -> linkWisdom
            else -> false
        }
    }
}
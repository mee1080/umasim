package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase

enum class StrataType {
    SAND,
    EARTH,
    ROCK,
}

val equipmentStrata = mapOf(
    EquipmentType.HOLE_DIGGER to StrataType.SAND,
    EquipmentType.EARTH_DRILL to StrataType.EARTH,
    EquipmentType.METAL_CROWN to StrataType.ROCK,
)

val equipmentTrainingType = mapOf(
    EquipmentType.HOLE_DIGGER to listOf(StatusType.SPEED, StatusType.WISDOM, StatusType.STAMINA),
    EquipmentType.EARTH_DRILL to listOf(StatusType.GUTS, StatusType.SPEED, StatusType.POWER),
    EquipmentType.METAL_CROWN to listOf(StatusType.POWER, StatusType.WISDOM, StatusType.STAMINA),
)

data class InnRankBonus(
    val trainingEffectUp: Int,
    val failureRateDown: Int,
    val statusBonus: Status,
)

val innRankBonuses = listOf(
    InnRankBonus(0, 0, Status()), // Rank 1
    InnRankBonus(5, 2, Status(5, 5, 5, 5, 5)), // Rank 2
    InnRankBonus(10, 4, Status(10, 10, 10, 10, 10)), // Rank 3
    InnRankBonus(15, 6, Status(15, 15, 15, 15, 15)), // Rank 4
    InnRankBonus(20, 8, Status(20, 20, 20, 20, 20)), // Rank 5
    InnRankBonus(25, 10, Status(25, 25, 25, 25, 25)), // Rank 6
)

data class YuyamiKaiReward(
    val status: Status,
    val skillPt: Int,
)

val yuyamiKaiRewards = mapOf(
    // Junior
    1 to (0..6).map {
        YuyamiKaiReward(Status(all = 10 + it * 2), 20 + it * 5)
    },
    // Classic
    2 to (0..6).map {
        YuyamiKaiReward(Status(all = 15 + it * 3), 30 + it * 8)
    },
    // Senior
    3 to (0..6).map {
        YuyamiKaiReward(Status(all = 20 + it * 4), 40 + it * 10)
    }
)

const val ONSEN_HP_RECOVERY = 45
const val ONSEN_BOND_RECOVERY = 10
const val ONSEN_MOTIVATION_UP = 1
const val ONSEN_EFFECT_DURATION = 2

const val SUPER_RECOVERY_HP = 20
const val SUPER_RECOVERY_SKILL_PT = 100
const val SUPER_RECOVERY_TEMP_MAX_HP = 150

val onsenMaxStatus = Status(1900, 1800, 1700, 1700, 1400)

val onsenTrainingData: List<TrainingBase> = listOf(
    TrainingBase(StatusType.SPEED, 1, 520, Status(12, 0, 1, 0, 0, 6, -22)),
    TrainingBase(StatusType.SPEED, 2, 524, Status(13, 0, 1, 0, 0, 6, -23)),
    TrainingBase(StatusType.SPEED, 3, 528, Status(14, 0, 1, 0, 0, 6, -24)),
    TrainingBase(StatusType.SPEED, 4, 532, Status(15, 0, 2, 0, 0, 6, -26)),
    TrainingBase(StatusType.SPEED, 5, 536, Status(16, 0, 3, 0, 0, 6, -17)),
    TrainingBase(StatusType.STAMINA, 1, 507, Status(0, 9, 0, 5, 0, 6, -22)),
    TrainingBase(StatusType.STAMINA, 2, 511, Status(0, 10, 0, 5, 0, 6, -23)),
    TrainingBase(StatusType.STAMINA, 3, 515, Status(0, 11, 0, 5, 0, 6, -24)),
    TrainingBase(StatusType.STAMINA, 4, 519, Status(0, 12, 0, 6, 0, 6, -26)),
    TrainingBase(StatusType.STAMINA, 5, 523, Status(0, 14, 0, 5, 0, 6, -17)),
    TrainingBase(StatusType.POWER, 1, 516, Status(0, 3, 11, 0, 0, 6, -22)),
    TrainingBase(StatusType.POWER, 2, 520, Status(0, 3, 12, 0, 0, 6, -23)),
    TrainingBase(StatusType.POWER, 3, 524, Status(0, 3, 13, 0, 0, 6, -24)),
    TrainingBase(StatusType.POWER, 4, 528, Status(0, 4, 14, 0, 0, 6, -26)),
    TrainingBase(StatusType.POWER, 5, 532, Status(0, 5, 15, 0, 0, 6, -17)),
    TrainingBase(StatusType.GUTS, 1, 532, Status(2, 0, 2, 10, 0, 6, -22)),
    TrainingBase(StatusType.GUTS, 2, 536, Status(2, 0, 2, 11, 0, 6, -23)),
    TrainingBase(StatusType.GUTS, 3, 540, Status(2, 0, 2, 12, 0, 6, -24)),
    TrainingBase(StatusType.GUTS, 4, 544, Status(3, 0, 2, 13, 0, 6, -26)),
    TrainingBase(StatusType.GUTS, 5, 548, Status(5, 0, 4, 14, 0, 6, -17)),
    TrainingBase(StatusType.WISDOM, 1, 320, Status(5, 0, 0, 0, 8, 6, 5)),
    TrainingBase(StatusType.WISDOM, 2, 321, Status(5, 0, 0, 0, 9, 6, 5)),
    TrainingBase(StatusType.WISDOM, 3, 322, Status(5, 0, 0, 0, 10, 6, 5)),
    TrainingBase(StatusType.WISDOM, 4, 323, Status(6, 0, 0, 0, 11, 6, 5)),
    TrainingBase(StatusType.WISDOM, 5, 324, Status(6, 0, 0, 0, 12, 6, 5)),
)

package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase
import io.github.mee1080.umasim.data.trainingType

val mujintoTrainingData: List<TrainingBase> = listOf(
    TrainingBase(StatusType.SPEED, 1, 520, Status(12, 0, 1, 0, 0, 6, -20)),
    TrainingBase(StatusType.SPEED, 2, 524, Status(13, 0, 1, 0, 0, 6, -21)),
    TrainingBase(StatusType.SPEED, 3, 528, Status(14, 0, 1, 0, 0, 6, -22)),
    TrainingBase(StatusType.SPEED, 4, 532, Status(15, 0, 2, 0, 0, 6, -24)),
    TrainingBase(StatusType.SPEED, 5, 536, Status(16, 0, 3, 0, 0, 6, -15)),
    TrainingBase(StatusType.STAMINA, 1, 507, Status(0, 9, 0, 5, 0, 6, -20)),
    TrainingBase(StatusType.STAMINA, 2, 511, Status(0, 10, 0, 5, 0, 6, -21)),
    TrainingBase(StatusType.STAMINA, 3, 515, Status(0, 11, 0, 5, 0, 6, -22)),
    TrainingBase(StatusType.STAMINA, 4, 519, Status(0, 12, 0, 6, 0, 6, -24)),
    TrainingBase(StatusType.STAMINA, 5, 523, Status(0, 14, 0, 5, 0, 6, -15)),
    TrainingBase(StatusType.POWER, 1, 516, Status(0, 3, 11, 0, 0, 6, -20)),
    TrainingBase(StatusType.POWER, 2, 520, Status(0, 3, 12, 0, 0, 6, -21)),
    TrainingBase(StatusType.POWER, 3, 524, Status(0, 3, 13, 0, 0, 6, -22)),
    TrainingBase(StatusType.POWER, 4, 528, Status(0, 4, 14, 0, 0, 6, -24)),
    TrainingBase(StatusType.POWER, 5, 532, Status(0, 5, 15, 0, 0, 6, -15)),
    TrainingBase(StatusType.GUTS, 1, 532, Status(2, 0, 2, 10, 0, 6, -20)),
    TrainingBase(StatusType.GUTS, 2, 536, Status(2, 0, 2, 11, 0, 6, -21)),
    TrainingBase(StatusType.GUTS, 3, 540, Status(2, 0, 2, 12, 0, 6, -22)),
    TrainingBase(StatusType.GUTS, 4, 544, Status(3, 0, 2, 13, 0, 6, -24)),
    TrainingBase(StatusType.GUTS, 5, 548, Status(5, 0, 4, 14, 0, 6, -15)),
    TrainingBase(StatusType.WISDOM, 1, 320, Status(5, 0, 0, 0, 8, 6, 5)),
    TrainingBase(StatusType.WISDOM, 2, 321, Status(5, 0, 0, 0, 9, 6, 5)),
    TrainingBase(StatusType.WISDOM, 3, 322, Status(5, 0, 0, 0, 10, 6, 5)),
    TrainingBase(StatusType.WISDOM, 4, 323, Status(6, 0, 0, 0, 11, 6, 5)),
    TrainingBase(StatusType.WISDOM, 5, 324, Status(6, 0, 0, 0, 12, 6, 5)),
)

val mujintoIslandTrainingBase = TrainingBase(StatusType.FRIEND, 0, 0, Status(8, 6, 4, 4, 10, 7, 0))

val mujintoCampTrainingDataNoFacility = listOf(
    TrainingBase(StatusType.SPEED, 5, 536, Status(14, 0, 1, 0, 0, 8, -15)),
    TrainingBase(StatusType.STAMINA, 5, 523, Status(0, 14, 0, 5, 0, 16, -15)),
    TrainingBase(StatusType.POWER, 5, 532, Status(0, 5, 14, 0, 0, 16, -15)),
    TrainingBase(StatusType.GUTS, 5, 548, Status(5, 0, 4, 14, 0, 16, -15)),
    TrainingBase(StatusType.WISDOM, 5, 324, Status(7, 0, 0, 0, 12, 16, 5)),
)

val mujintoCampTrainingData = listOf(
    TrainingBase(StatusType.SPEED, 5, 536, Status(14, 1, 1, 1, 1, 8, -15)),
    TrainingBase(StatusType.STAMINA, 5, 523, Status(2, 14, 2, 5, 2, 16, -15)),
    TrainingBase(StatusType.POWER, 5, 532, Status(2, 5, 14, 2, 2, 16, -15)),
    TrainingBase(StatusType.GUTS, 5, 548, Status(5, 2, 4, 14, 2, 16, -15)),
    TrainingBase(StatusType.WISDOM, 5, 324, Status(7, 2, 2, 2, 12, 16, 5)),
)

val mujintoFacilities = (trainingType + StatusType.FRIEND).associateWith { type ->
    when (type) {
        StatusType.FRIEND -> (1..3)
        else -> (1..5)
    }.associateWith { level ->
        when {
            type != StatusType.FRIEND && level >= 3 -> listOf(false, true)
            else -> listOf(false)
        }.associateWith { jukuren ->
            MujintoFacility(type, level, jukuren)
        }
    }
}

fun mujintoFacility(type: StatusType, level: Int, jukuren: Boolean = false): MujintoFacility {
    return mujintoFacilities[type]!![level]!![jukuren]!!
}

fun mujintoIslandTrainingRate(support: StatusType, position: StatusType, target: StatusType): Double {
    return if (position == StatusType.FRIEND) {
        mujintoIslandTrainingRateHouse[support]!![target]!!
    } else {
        mujintoIslandTrainingRateStatus[position]!![target]!!
    }
}

/**
 * 島トレのスピ～賢さの倍率
 * 配置→ステータス→倍率
 */
private val mujintoIslandTrainingRateStatus = mapOf(
    StatusType.SPEED to mapOf(
        StatusType.SPEED to 0.6,
        StatusType.STAMINA to 0.0,
        StatusType.POWER to 0.4,
        StatusType.GUTS to 0.0,
        StatusType.WISDOM to 0.0,
        StatusType.SKILL to 0.5,
    ),
    StatusType.STAMINA to mapOf(
        StatusType.SPEED to 0.0,
        StatusType.STAMINA to 0.6,
        StatusType.POWER to 0.0,
        StatusType.GUTS to 0.4,
        StatusType.WISDOM to 0.0,
        StatusType.SKILL to 0.5,
    ),
    StatusType.POWER to mapOf(
        StatusType.SPEED to 0.0,
        StatusType.STAMINA to 0.3,
        StatusType.POWER to 0.6,
        StatusType.GUTS to 0.0,
        StatusType.WISDOM to 0.0,
        StatusType.SKILL to 0.7,
    ),
    StatusType.GUTS to mapOf(
        StatusType.SPEED to 0.2,
        StatusType.STAMINA to 0.0,
        StatusType.POWER to 0.2,
        StatusType.GUTS to 0.6,
        StatusType.WISDOM to 0.0,
        StatusType.SKILL to 0.5,
    ),
    StatusType.WISDOM to mapOf(
        StatusType.SPEED to 0.4,
        StatusType.STAMINA to 0.0,
        StatusType.POWER to 0.0,
        StatusType.GUTS to 0.0,
        StatusType.WISDOM to 0.6,
        StatusType.SKILL to 0.5,
    ),
)

/**
 * 島トレの海の家の倍率
 * サポカタイプ→ステータス→倍率
 */
private val mujintoIslandTrainingRateHouse = mapOf(
    StatusType.SPEED to mapOf(
        StatusType.SPEED to 0.2,
        StatusType.STAMINA to 0.15,
        StatusType.POWER to 0.25,
        StatusType.GUTS to 0.15,
        StatusType.WISDOM to 0.15,
        StatusType.SKILL to 0.7,
    ),
    StatusType.STAMINA to mapOf(
        StatusType.SPEED to 0.2,
        StatusType.STAMINA to 0.15,
        StatusType.POWER to 0.1,
        StatusType.GUTS to 0.15,
        StatusType.WISDOM to 0.15,
        StatusType.SKILL to 0.7,
    ),
    StatusType.POWER to mapOf(
        StatusType.SPEED to 0.2,
        StatusType.STAMINA to 0.15,
        StatusType.POWER to 0.1,
        StatusType.GUTS to 0.15,
        StatusType.WISDOM to 0.15,
        StatusType.SKILL to 0.7,
    ),
    StatusType.GUTS to mapOf(
        StatusType.SPEED to 0.2,
        StatusType.STAMINA to 0.15,
        StatusType.POWER to 0.1,
        StatusType.GUTS to 0.15,
        StatusType.WISDOM to 0.15,
        StatusType.SKILL to 0.7,
    ),
    StatusType.WISDOM to mapOf(
        StatusType.SPEED to 0.2,
        StatusType.STAMINA to 0.15,
        StatusType.POWER to 0.15,
        StatusType.GUTS to 0.15,
        StatusType.WISDOM to 0.15,
        StatusType.SKILL to 0.7,
    ),
    StatusType.FRIEND to mapOf(
        StatusType.SPEED to 0.2,
        StatusType.STAMINA to 0.15,
        StatusType.POWER to 0.15,
        StatusType.GUTS to 0.15,
        StatusType.WISDOM to 0.15,
        StatusType.SKILL to 0.7,
    ),
    StatusType.GROUP to mapOf(
        StatusType.SPEED to 0.2,
        StatusType.STAMINA to 0.15,
        StatusType.POWER to 0.15,
        StatusType.GUTS to 0.15,
        StatusType.WISDOM to 0.15,
        StatusType.SKILL to 0.7,
    ),
)
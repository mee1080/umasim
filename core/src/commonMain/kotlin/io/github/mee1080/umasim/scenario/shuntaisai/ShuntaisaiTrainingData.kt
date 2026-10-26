package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase

val shuntaisaiTrainingData: List<TrainingBase> = listOf(
    TrainingBase(StatusType.SPEED, 1, 520, Status(12, 0, 2, 0, 0, 7, -20)),
    TrainingBase(StatusType.SPEED, 2, 524, Status(13, 0, 2, 0, 0, 7, -21)),
    TrainingBase(StatusType.SPEED, 3, 528, Status(14, 0, 2, 0, 0, 7, -22)),
    TrainingBase(StatusType.SPEED, 4, 532, Status(15, 0, 3, 0, 0, 7, -24)),
    TrainingBase(StatusType.SPEED, 5, 536, Status(16, 0, 3, 0, 0, 9, -25)),
    TrainingBase(StatusType.STAMINA, 1, 507, Status(0, 10, 0, 3, 0, 7, -20)),
    TrainingBase(StatusType.STAMINA, 2, 511, Status(0, 11, 0, 4, 0, 7, -21)),
    TrainingBase(StatusType.STAMINA, 3, 515, Status(0, 12, 0, 4, 0, 7, -22)),
    TrainingBase(StatusType.STAMINA, 4, 519, Status(0, 13, 0, 5, 0, 7, -24)),
    TrainingBase(StatusType.STAMINA, 5, 523, Status(0, 14, 0, 6, 0, 10, -25)),
    TrainingBase(StatusType.POWER, 1, 516, Status(0, 6, 9, 0, 0, 7, -21)),
    TrainingBase(StatusType.POWER, 2, 520, Status(0, 6, 10, 0, 0, 7, -22)),
    TrainingBase(StatusType.POWER, 3, 524, Status(0, 6, 11, 0, 0, 7, -23)),
    TrainingBase(StatusType.POWER, 4, 528, Status(0, 6, 12, 0, 0, 7, -25)),
    TrainingBase(StatusType.POWER, 5, 532, Status(0, 6, 14, 0, 0, 10, -25)),
    TrainingBase(StatusType.GUTS, 1, 532, Status(2, 0, 2, 12, 0, 7, -21)),
    TrainingBase(StatusType.GUTS, 2, 536, Status(2, 0, 2, 13, 0, 7, -22)),
    TrainingBase(StatusType.GUTS, 3, 540, Status(2, 0, 2, 14, 0, 7, -23)),
    TrainingBase(StatusType.GUTS, 4, 544, Status(3, 0, 2, 15, 0, 7, -25)),
    TrainingBase(StatusType.GUTS, 5, 548, Status(3, 0, 2, 15, 0, 10, -25)),
    TrainingBase(StatusType.WISDOM, 1, 320, Status(2, 0, 0, 0, 8, 6, 5)),
    TrainingBase(StatusType.WISDOM, 2, 321, Status(2, 0, 0, 0, 9, 6, 5)),
    TrainingBase(StatusType.WISDOM, 3, 322, Status(2, 0, 0, 0, 10, 6, 5)),
    TrainingBase(StatusType.WISDOM, 4, 323, Status(3, 0, 0, 0, 11, 6, 5)),
    TrainingBase(StatusType.WISDOM, 5, 324, Status(3, 0, 0, 0, 12, 8, 5)),
)

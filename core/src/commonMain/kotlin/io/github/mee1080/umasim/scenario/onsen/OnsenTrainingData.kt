package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase

val onsenTrainingData: List<TrainingBase> = listOf(
    TrainingBase(StatusType.SPEED, 1, 520, Status(12, 0, 2, 0, 0, 7, -20)),
    TrainingBase(StatusType.SPEED, 2, 524, Status(13, 0, 2, 0, 0, 7, -21)),
    TrainingBase(StatusType.SPEED, 3, 528, Status(14, 0, 2, 0, 0, 7, -22)),
    TrainingBase(StatusType.SPEED, 4, 532, Status(15, 0, 3, 0, 0, 7, -24)),
    TrainingBase(StatusType.SPEED, 5, 536, Status(16, 0, 3, 0, 0, 6, -40)),
    TrainingBase(StatusType.STAMINA, 1, 507, Status(0, 11, 0, 3, 0, 7, -22)),
    TrainingBase(StatusType.STAMINA, 2, 511, Status(0, 12, 0, 3, 0, 7, -23)),
    TrainingBase(StatusType.STAMINA, 3, 515, Status(0, 13, 0, 3, 0, 7, -24)),
    TrainingBase(StatusType.STAMINA, 4, 519, Status(0, 14, 0, 4, 0, 7, -26)),
    TrainingBase(StatusType.STAMINA, 5, 523, Status(0, 23, 0, 8, 0, 6, -42)),
    TrainingBase(StatusType.POWER, 1, 516, Status(0, 3, 11, 0, 0, 7, -22)),
    TrainingBase(StatusType.POWER, 2, 520, Status(0, 3, 12, 0, 0, 7, -23)),
    TrainingBase(StatusType.POWER, 3, 524, Status(0, 3, 13, 0, 0, 7, -24)),
    TrainingBase(StatusType.POWER, 4, 528, Status(0, 4, 14, 0, 0, 7, -26)),
    TrainingBase(StatusType.POWER, 5, 532, Status(0, 5, 15, 0, 0, 6, -42)),
    TrainingBase(StatusType.GUTS, 1, 532, Status(2, 0, 2, 12, 0, 7, -22)),
    TrainingBase(StatusType.GUTS, 2, 536, Status(2, 0, 2, 13, 0, 7, -23)),
    TrainingBase(StatusType.GUTS, 3, 540, Status(2, 0, 2, 14, 0, 7, -24)),
    TrainingBase(StatusType.GUTS, 4, 544, Status(3, 0, 2, 15, 0, 7, -26)),
    TrainingBase(StatusType.GUTS, 5, 548, Status(4, 0, 3, 24, 0, 6, -42)),
    TrainingBase(StatusType.WISDOM, 1, 320, Status(2, 0, 0, 0, 11, 7, 5)),
    TrainingBase(StatusType.WISDOM, 2, 321, Status(2, 0, 0, 0, 12, 7, 5)),
    TrainingBase(StatusType.WISDOM, 3, 322, Status(2, 0, 0, 0, 13, 7, 5)),
    TrainingBase(StatusType.WISDOM, 4, 323, Status(3, 0, 0, 0, 14, 7, 5)),
    TrainingBase(StatusType.WISDOM, 5, 324, Status(6, 0, 0, 0, 15, 6, 5)),
    TrainingBase(StatusType.SPEED, 6, 536, Status(23, 0, 6, 0, 0, 7, -40)),
    TrainingBase(StatusType.STAMINA, 6, 523, Status(0, 23, 0, 8, 0, 7, -42)),
    TrainingBase(StatusType.POWER, 6, 532, Status(0, 8, 23, 0, 0, 7, -42)),
    TrainingBase(StatusType.GUTS, 6, 548, Status(4, 0, 3, 24, 0, 7, -42)),
    TrainingBase(StatusType.WISDOM, 6, 324, Status(3, 0, 0, 0, 15, 7, 5)),
)

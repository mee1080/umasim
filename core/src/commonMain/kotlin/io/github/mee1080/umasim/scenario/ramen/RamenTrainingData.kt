package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase

// TODO: ラーメンシナリオ固有のトレーニング値を設定する（暫定的にURAベース）
val ramenTrainingData: List<TrainingBase> = listOf(
    TrainingBase(StatusType.SPEED, 1, 520, Status(11, 0, 6, 0, 0, 4, -21)),
    TrainingBase(StatusType.SPEED, 2, 524, Status(12, 0, 6, 0, 0, 4, -22)),
    TrainingBase(StatusType.SPEED, 3, 528, Status(13, 0, 6, 0, 0, 4, -23)),
    TrainingBase(StatusType.SPEED, 4, 532, Status(14, 0, 7, 0, 0, 4, -25)),
    TrainingBase(StatusType.SPEED, 5, 536, Status(15, 0, 8, 0, 0, 4, -27)),
    TrainingBase(StatusType.POWER, 1, 516, Status(0, 6, 9, 0, 0, 4, -20)),
    TrainingBase(StatusType.POWER, 2, 520, Status(0, 6, 10, 0, 0, 4, -21)),
    TrainingBase(StatusType.POWER, 3, 524, Status(0, 6, 11, 0, 0, 4, -22)),
    TrainingBase(StatusType.POWER, 4, 528, Status(0, 7, 12, 0, 0, 4, -24)),
    TrainingBase(StatusType.POWER, 5, 532, Status(0, 8, 13, 0, 0, 4, -26)),
    TrainingBase(StatusType.GUTS, 1, 532, Status(5, 0, 5, 8, 0, 4, -22)),
    TrainingBase(StatusType.GUTS, 2, 536, Status(5, 0, 5, 9, 0, 4, -23)),
    TrainingBase(StatusType.GUTS, 3, 540, Status(5, 0, 5, 10, 0, 4, -24)),
    TrainingBase(StatusType.GUTS, 4, 544, Status(5, 0, 5, 12, 0, 4, -26)),
    TrainingBase(StatusType.GUTS, 5, 548, Status(6, 0, 5, 13, 0, 4, -28)),
    TrainingBase(StatusType.STAMINA, 1, 507, Status(0, 10, 0, 6, 0, 4, -19)),
    TrainingBase(StatusType.STAMINA, 2, 511, Status(0, 11, 0, 6, 0, 4, -20)),
    TrainingBase(StatusType.STAMINA, 3, 515, Status(0, 12, 0, 6, 0, 4, -21)),
    TrainingBase(StatusType.STAMINA, 4, 519, Status(0, 13, 0, 7, 0, 4, -23)),
    TrainingBase(StatusType.STAMINA, 5, 523, Status(0, 14, 0, 8, 0, 4, -25)),
    TrainingBase(StatusType.WISDOM, 1, 320, Status(2, 0, 0, 0, 10, 5, 5)),
    TrainingBase(StatusType.WISDOM, 2, 321, Status(2, 0, 0, 0, 11, 5, 5)),
    TrainingBase(StatusType.WISDOM, 3, 322, Status(2, 0, 0, 0, 12, 5, 5)),
    TrainingBase(StatusType.WISDOM, 4, 323, Status(3, 0, 0, 0, 13, 5, 5)),
    TrainingBase(StatusType.WISDOM, 5, 324, Status(4, 0, 0, 0, 14, 5, 5)),
)

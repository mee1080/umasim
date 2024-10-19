package io.github.mee1080.umasim.scenario.cook

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.TrainingBase
import io.github.mee1080.umasim.data.toSupportType

internal val cookTrainingData = listOf(
    TrainingBase(toSupportType("S"), 1, 520, Status(11, 0, 2, 0, 0, 5, -19)),
    TrainingBase(toSupportType("S"), 2, 524, Status(12, 0, 2, 0, 0, 5, -20)),
    TrainingBase(toSupportType("S"), 3, 528, Status(13, 0, 2, 0, 0, 5, -21)),
    TrainingBase(toSupportType("S"), 4, 532, Status(14, 0, 3, 0, 0, 5, -23)),
    TrainingBase(toSupportType("S"), 5, 536, Status(15, 0, 4, 0, 0, 5, -25)),
    TrainingBase(toSupportType("H"), 1, 516, Status(0, 8, 0, 5, 0, 5, -20)),
    TrainingBase(toSupportType("H"), 2, 520, Status(0, 9, 0, 5, 0, 5, -21)),
    TrainingBase(toSupportType("H"), 3, 524, Status(0, 10, 0, 6, 0, 5, -22)),
    TrainingBase(toSupportType("H"), 4, 528, Status(0, 11, 0, 7, 0, 5, -24)),
    TrainingBase(toSupportType("H"), 5, 532, Status(0, 12, 0, 8, 0, 5, -26)),
    TrainingBase(toSupportType("P"), 1, 532, Status(0, 4, 9, 0, 0, 5, -20)),
    TrainingBase(toSupportType("P"), 2, 536, Status(0, 4, 10, 0, 0, 5, -21)),
    TrainingBase(toSupportType("P"), 3, 540, Status(0, 4, 11, 0, 0, 5, -22)),
    TrainingBase(toSupportType("P"), 4, 544, Status(0, 5, 12, 0, 0, 5, -24)),
    TrainingBase(toSupportType("P"), 5, 548, Status(0, 6, 13, 0, 0, 5, -26)),
    TrainingBase(toSupportType("G"), 1, 507, Status(2, 0, 2, 10, 0, 5, -20)),
    TrainingBase(toSupportType("G"), 2, 511, Status(2, 0, 2, 11, 0, 5, -21)),
    TrainingBase(toSupportType("G"), 3, 515, Status(2, 0, 2, 12, 0, 5, -22)),
    TrainingBase(toSupportType("G"), 4, 519, Status(3, 0, 3, 13, 0, 5, -24)),
    TrainingBase(toSupportType("G"), 5, 523, Status(3, 0, 3, 14, 0, 5, -26)),
    TrainingBase(toSupportType("W"), 1, 320, Status(2, 0, 0, 0, 8, 5, 5)),
    TrainingBase(toSupportType("W"), 2, 321, Status(2, 0, 0, 0, 9, 5, 5)),
    TrainingBase(toSupportType("W"), 3, 322, Status(2, 0, 0, 0, 10, 5, 5)),
    TrainingBase(toSupportType("W"), 4, 323, Status(3, 0, 0, 0, 11, 5, 5)),
    TrainingBase(toSupportType("W"), 5, 324, Status(4, 0, 0, 0, 12, 5, 5)),
)

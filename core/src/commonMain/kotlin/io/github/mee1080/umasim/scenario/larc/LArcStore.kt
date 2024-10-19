package io.github.mee1080.umasim.scenario.larc

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.TrainingBase
import io.github.mee1080.umasim.data.toSupportType

internal val lArcTrainingData = listOf(
    TrainingBase(toSupportType("S"), 1, 520, Status(10, 0, 3, 0, 0, 6, -21)),
    TrainingBase(toSupportType("S"), 2, 524, Status(11, 0, 3, 0, 0, 6, -22)),
    TrainingBase(toSupportType("S"), 3, 528, Status(12, 0, 3, 0, 0, 6, -23)),
    TrainingBase(toSupportType("S"), 4, 532, Status(13, 0, 4, 0, 0, 6, -25)),
    TrainingBase(toSupportType("S"), 5, 536, Status(14, 0, 5, 0, 0, 6, -27)),
    TrainingBase(toSupportType("S"), 6, 536, Status(16, 0, 5, 0, 0, 6, -21)),
    TrainingBase(toSupportType("H"), 1, 507, Status(0, 9, 0, 4, 0, 6, -19)),
    TrainingBase(toSupportType("H"), 2, 511, Status(0, 10, 0, 4, 0, 6, -20)),
    TrainingBase(toSupportType("H"), 3, 515, Status(0, 11, 0, 4, 0, 6, -21)),
    TrainingBase(toSupportType("H"), 4, 519, Status(0, 12, 0, 5, 0, 6, -23)),
    TrainingBase(toSupportType("H"), 5, 523, Status(0, 13, 0, 6, 0, 6, -25)),
    TrainingBase(toSupportType("H"), 6, 523, Status(0, 15, 0, 7, 0, 6, -19)),
    TrainingBase(toSupportType("P"), 1, 516, Status(0, 5, 11, 0, 0, 6, -20)),
    TrainingBase(toSupportType("P"), 2, 520, Status(0, 5, 12, 0, 0, 6, -21)),
    TrainingBase(toSupportType("P"), 3, 524, Status(0, 5, 13, 0, 0, 6, -22)),
    TrainingBase(toSupportType("P"), 4, 528, Status(0, 6, 14, 0, 0, 6, -24)),
    TrainingBase(toSupportType("P"), 5, 532, Status(0, 7, 15, 0, 0, 6, -26)),
    TrainingBase(toSupportType("P"), 6, 532, Status(0, 9, 17, 0, 0, 6, -20)),
    TrainingBase(toSupportType("G"), 1, 532, Status(3, 0, 2, 10, 0, 6, -21)),
    TrainingBase(toSupportType("G"), 2, 536, Status(3, 0, 2, 11, 0, 6, -22)),
    TrainingBase(toSupportType("G"), 3, 540, Status(3, 0, 2, 12, 0, 6, -23)),
    TrainingBase(toSupportType("G"), 4, 544, Status(4, 0, 2, 13, 0, 6, -25)),
    TrainingBase(toSupportType("G"), 5, 548, Status(4, 0, 3, 14, 0, 6, -27)),
    TrainingBase(toSupportType("G"), 6, 548, Status(6, 0, 5, 16, 0, 6, -21)),
    TrainingBase(toSupportType("W"), 1, 320, Status(2, 0, 0, 0, 9, 6, 5)),
    TrainingBase(toSupportType("W"), 2, 321, Status(2, 0, 0, 0, 10, 6, 5)),
    TrainingBase(toSupportType("W"), 3, 322, Status(2, 0, 0, 0, 11, 6, 5)),
    TrainingBase(toSupportType("W"), 4, 323, Status(3, 0, 0, 0, 12, 6, 5)),
    TrainingBase(toSupportType("W"), 5, 324, Status(4, 0, 0, 0, 13, 6, 5)),
    TrainingBase(toSupportType("W"), 6, 324, Status(5, 0, 0, 0, 14, 6, 6)),
)
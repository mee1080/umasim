package io.github.mee1080.umasim.scenario.gm

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.TrainingBase
import io.github.mee1080.umasim.data.toSupportType

val gmWisdomLevelEffect = mapOf(
    Founder.Red to arrayOf(
        WisdomLevelEffect(),
        WisdomLevelEffect(hpCost = 10, trainingFactor = 5),
        WisdomLevelEffect(hpCost = 15, trainingFactor = 8),
        WisdomLevelEffect(hpCost = 18, trainingFactor = 11),
        WisdomLevelEffect(hpCost = 20, trainingFactor = 13),
        WisdomLevelEffect(hpCost = 23, trainingFactor = 15),
    ),
    Founder.Blue to arrayOf(
        WisdomLevelEffect(),
        WisdomLevelEffect(hintFrequency = 20, trainingEventFrequency = 20, trainingFactor = 5),
        WisdomLevelEffect(hintFrequency = 25, trainingEventFrequency = 25, trainingFactor = 8),
        WisdomLevelEffect(hintFrequency = 30, trainingEventFrequency = 30, trainingFactor = 11),
        WisdomLevelEffect(hintFrequency = 33, trainingEventFrequency = 33, trainingFactor = 13),
        WisdomLevelEffect(hintFrequency = 35, trainingEventFrequency = 35, trainingFactor = 15),
    ),
    Founder.Yellow to arrayOf(
        WisdomLevelEffect(),
        WisdomLevelEffect(supportEventEffect = 10, supportEventFrequency = 20, trainingFactor = 5),
        WisdomLevelEffect(supportEventEffect = 15, supportEventFrequency = 40, trainingFactor = 8),
        WisdomLevelEffect(supportEventEffect = 20, supportEventFrequency = 60, trainingFactor = 11),
        WisdomLevelEffect(supportEventEffect = 23, supportEventFrequency = 80, trainingFactor = 13),
        WisdomLevelEffect(supportEventEffect = 25, supportEventFrequency = 90, trainingFactor = 15),
    ),
)

internal val gmTrainingData = listOf(
    TrainingBase(toSupportType("S"), 1, 520, Status(10, 0, 3, 0, 0, 5, -19)),
    TrainingBase(toSupportType("S"), 2, 524, Status(11, 0, 3, 0, 0, 5, -20)),
    TrainingBase(toSupportType("S"), 3, 528, Status(12, 0, 3, 0, 0, 5, -21)),
    TrainingBase(toSupportType("S"), 4, 532, Status(13, 0, 4, 0, 0, 5, -23)),
    TrainingBase(toSupportType("S"), 5, 536, Status(14, 0, 5, 0, 0, 5, -25)),
    TrainingBase(toSupportType("S"), 6, 536, Status(15, 0, 6, 0, 0, 5, -25)),
    TrainingBase(toSupportType("P"), 1, 516, Status(0, 4, 9, 0, 0, 5, -20)),
    TrainingBase(toSupportType("P"), 2, 520, Status(0, 4, 10, 0, 0, 5, -21)),
    TrainingBase(toSupportType("P"), 3, 524, Status(0, 4, 11, 0, 0, 5, -22)),
    TrainingBase(toSupportType("P"), 4, 528, Status(0, 5, 12, 0, 0, 5, -24)),
    TrainingBase(toSupportType("P"), 5, 532, Status(0, 6, 13, 0, 0, 5, -26)),
    TrainingBase(toSupportType("P"), 6, 532, Status(0, 7, 14, 0, 0, 5, -26)),
    TrainingBase(toSupportType("G"), 1, 532, Status(2, 0, 3, 9, 0, 5, -20)),
    TrainingBase(toSupportType("G"), 2, 536, Status(2, 0, 3, 10, 0, 5, -21)),
    TrainingBase(toSupportType("G"), 3, 540, Status(2, 0, 3, 11, 0, 5, -22)),
    TrainingBase(toSupportType("G"), 4, 544, Status(3, 0, 3, 12, 0, 5, -24)),
    TrainingBase(toSupportType("G"), 5, 548, Status(3, 0, 4, 13, 0, 5, -26)),
    TrainingBase(toSupportType("G"), 6, 548, Status(4, 0, 5, 14, 0, 5, -26)),
    TrainingBase(toSupportType("H"), 1, 507, Status(0, 8, 0, 6, 0, 5, -20)),
    TrainingBase(toSupportType("H"), 2, 511, Status(0, 9, 0, 6, 0, 5, -21)),
    TrainingBase(toSupportType("H"), 3, 515, Status(0, 10, 0, 6, 0, 5, -22)),
    TrainingBase(toSupportType("H"), 4, 519, Status(0, 11, 0, 7, 0, 5, -24)),
    TrainingBase(toSupportType("H"), 5, 523, Status(0, 12, 0, 8, 0, 5, -26)),
    TrainingBase(toSupportType("H"), 6, 523, Status(0, 13, 0, 9, 0, 5, -26)),
    TrainingBase(toSupportType("W"), 1, 320, Status(2, 0, 0, 0, 8, 5, 5)),
    TrainingBase(toSupportType("W"), 2, 321, Status(2, 0, 0, 0, 9, 5, 5)),
    TrainingBase(toSupportType("W"), 3, 322, Status(2, 0, 0, 0, 10, 5, 5)),
    TrainingBase(toSupportType("W"), 4, 323, Status(3, 0, 0, 0, 11, 5, 5)),
    TrainingBase(toSupportType("W"), 5, 324, Status(4, 0, 0, 0, 12, 5, 5)),
    TrainingBase(toSupportType("W"), 6, 324, Status(5, 0, 0, 0, 13, 5, 5)),
)

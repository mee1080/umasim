package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase

// TODO: Replace with actual Mujinto scenario training data once available.
// These are placeholder values.
// The structure assumes 5 levels for each training type.
// HP costs and specific stat gains need to be verified from mujinto_memo.md or game data.

val mujintoTrainingData: List<TrainingBase> = listOf(
    // Speed Training (Placeholder Values)
    TrainingBase(StatusType.SPEED, 1, 500, Status(speed = 10, power = 2, skillPt = 5, hp = -20)),
    TrainingBase(StatusType.SPEED, 2, 505, Status(speed = 11, power = 2, skillPt = 5, hp = -20)),
    TrainingBase(StatusType.SPEED, 3, 510, Status(speed = 12, power = 3, skillPt = 6, hp = -21)),
    TrainingBase(StatusType.SPEED, 4, 515, Status(speed = 13, power = 3, skillPt = 6, hp = -21)),
    TrainingBase(StatusType.SPEED, 5, 520, Status(speed = 14, power = 4, skillPt = 7, hp = -22)),

    // Stamina Training (Placeholder Values)
    TrainingBase(StatusType.STAMINA, 1, 500, Status(stamina = 9, guts = 3, skillPt = 5, hp = -20)),
    TrainingBase(StatusType.STAMINA, 2, 505, Status(stamina = 10, guts = 3, skillPt = 5, hp = -20)),
    TrainingBase(StatusType.STAMINA, 3, 510, Status(stamina = 11, guts = 4, skillPt = 6, hp = -21)),
    TrainingBase(StatusType.STAMINA, 4, 515, Status(stamina = 12, guts = 4, skillPt = 6, hp = -21)),
    TrainingBase(StatusType.STAMINA, 5, 520, Status(stamina = 13, guts = 5, skillPt = 7, hp = -22)),

    // Power Training (Placeholder Values)
    TrainingBase(StatusType.POWER, 1, 500, Status(speed = 2, power = 9, stamina = 2, skillPt = 5, hp = -20)),
    TrainingBase(StatusType.POWER, 2, 505, Status(speed = 2, power = 10, stamina = 2, skillPt = 5, hp = -20)),
    TrainingBase(StatusType.POWER, 3, 510, Status(speed = 3, power = 11, stamina = 3, skillPt = 6, hp = -21)),
    TrainingBase(StatusType.POWER, 4, 515, Status(speed = 3, power = 12, stamina = 3, skillPt = 6, hp = -21)),
    TrainingBase(StatusType.POWER, 5, 520, Status(speed = 4, power = 13, stamina = 4, skillPt = 7, hp = -22)),

    // Guts Training (Placeholder Values)
    TrainingBase(StatusType.GUTS, 1, 500, Status(stamina = 3, power = 3, guts = 7, skillPt = 5, hp = -20)),
    TrainingBase(StatusType.GUTS, 2, 505, Status(stamina = 3, power = 3, guts = 8, skillPt = 5, hp = -20)),
    TrainingBase(StatusType.GUTS, 3, 510, Status(stamina = 4, power = 4, guts = 9, skillPt = 6, hp = -21)),
    TrainingBase(StatusType.GUTS, 4, 515, Status(stamina = 4, power = 4, guts = 10, skillPt = 6, hp = -21)),
    TrainingBase(StatusType.GUTS, 5, 520, Status(stamina = 5, power = 5, guts = 11, skillPt = 7, hp = -22)),

    // Wisdom Training (Placeholder Values)
    // Wisdom training usually recovers HP or has a lower HP cost.
    TrainingBase(StatusType.WISDOM, 1, 300, Status(speed = 2, wisdom = 9, skillPt = 8, hp = 5)),
    TrainingBase(StatusType.WISDOM, 2, 305, Status(speed = 2, wisdom = 10, skillPt = 8, hp = 5)),
    TrainingBase(StatusType.WISDOM, 3, 310, Status(speed = 3, wisdom = 11, skillPt = 9, hp = 6)),
    TrainingBase(StatusType.WISDOM, 4, 315, Status(speed = 3, wisdom = 12, skillPt = 9, hp = 6)),
    TrainingBase(StatusType.WISDOM, 5, 320, Status(speed = 4, wisdom = 13, skillPt = 10, hp = 7))
)

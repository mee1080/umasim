/*
 * Copyright 2024 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.mee1080.umasim.scenario.mecha

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase

internal val mechaTrainingData = listOf(
    TrainingBase(StatusType.SPEED, 1, 520, Status(11, 0, 2, 0, 0, 5, -19)),
    TrainingBase(StatusType.SPEED, 2, 524, Status(12, 0, 2, 0, 0, 5, -20)),
    TrainingBase(StatusType.SPEED, 3, 528, Status(13, 0, 2, 0, 0, 5, -21)),
    TrainingBase(StatusType.SPEED, 4, 532, Status(14, 0, 3, 0, 0, 5, -23)),
    TrainingBase(StatusType.SPEED, 5, 536, Status(15, 0, 4, 0, 0, 5, -25)),
    TrainingBase(StatusType.STAMINA, 1, 507, Status(0, 10, 0, 4, 0, 5, -20)),
    TrainingBase(StatusType.STAMINA, 2, 511, Status(0, 11, 0, 4, 0, 5, -21)),
    TrainingBase(StatusType.STAMINA, 3, 515, Status(0, 12, 0, 4, 0, 5, -22)),
    TrainingBase(StatusType.STAMINA, 4, 519, Status(0, 13, 0, 5, 0, 5, -24)),
    TrainingBase(StatusType.STAMINA, 5, 523, Status(0, 14, 0, 6, 0, 5, -26)),
    TrainingBase(StatusType.POWER, 1, 516, Status(0, 4, 10, 0, 0, 5, -20)),
    TrainingBase(StatusType.POWER, 2, 520, Status(0, 4, 11, 0, 0, 5, -21)),
    TrainingBase(StatusType.POWER, 3, 524, Status(0, 4, 12, 0, 0, 5, -22)),
    TrainingBase(StatusType.POWER, 4, 528, Status(0, 5, 13, 0, 0, 5, -24)),
    TrainingBase(StatusType.POWER, 5, 532, Status(0, 6, 14, 0, 0, 5, -26)),
    TrainingBase(StatusType.GUTS, 1, 532, Status(2, 0, 2, 9, 0, 5, -20)),
    TrainingBase(StatusType.GUTS, 2, 536, Status(2, 0, 2, 10, 0, 5, -21)),
    TrainingBase(StatusType.GUTS, 3, 540, Status(2, 0, 2, 11, 0, 5, -22)),
    TrainingBase(StatusType.GUTS, 4, 544, Status(3, 0, 2, 12, 0, 5, -24)),
    TrainingBase(StatusType.GUTS, 5, 548, Status(4, 0, 3, 13, 0, 5, -26)),
    TrainingBase(StatusType.WISDOM, 1, 320, Status(2, 0, 0, 0, 8, 5, 5)),
    TrainingBase(StatusType.WISDOM, 2, 321, Status(2, 0, 0, 0, 9, 5, 5)),
    TrainingBase(StatusType.WISDOM, 3, 322, Status(2, 0, 0, 0, 10, 5, 5)),
    TrainingBase(StatusType.WISDOM, 4, 323, Status(3, 0, 0, 0, 11, 5, 5)),
    TrainingBase(StatusType.WISDOM, 5, 324, Status(4, 0, 0, 0, 12, 5, 5)),
)

val mechaChipLearningBonus = listOf(0, 10, 18, 26, 33, 40)

val mechaChipHintFrequency = listOf(0, 15, 30, 45, 60, 75)

val mechaChipSpecialityRate = listOf(0, 15, 30, 45, 60, 75)

val mechaChipFriendBonus = listOf(0, 2, 4, 6, 8, 10)

val mechaChipSkillPt = listOf(0, 12, 24, 36, 48, 60)

val mechaOdStatusBonusDivider = listOf(0, 0, 300, 200, 200, 200)

val mechaOdLearningLevelBonus = listOf(0, 0, 15, 20, 25, 25)

val mechaOdHpCostDown = listOf(0, 0, 0, 0, 50, 50)

val mechaOdMemberCountBonus = listOf(0, 1, 1, 1, 3, 3)

val mechaOdRelationBonus = listOf(0, 3, 3, 3, 3, 3)

val mechaOdHpGain = listOf(0, 0, 0, 0, 15, 15)

val mechaOdMotivationGain = listOf(0, 0, 0, 0, 1, 1)

val mechaLearningGain = listOf(
    // 通常
    listOf(
        // 通常
        listOf(
            listOf(7, 11, 14, 18, 21, 25),
            listOf(2, 3, 4, 5, 6, 7),
            listOf(1, 1, 2, 2, 3, 3),
        ),
        // メカギア
        listOf(
            listOf(9, 13, 17, 21, 26, 30),
            listOf(2, 4, 5, 6, 7, 8),
            listOf(1, 1, 2, 3, 3, 4),
        ),
        // 友情
        listOf(
            listOf(0, 17, 21, 25, 29, 33),
            listOf(0, 4, 6, 7, 8, 10),
            listOf(0, 2, 2, 3, 4, 4),
        ),
    ),
    // 合宿
    listOf(
        // 通常
        listOf(
            listOf(14, 18, 21, 25, 28, 32),
            listOf(4, 5, 6, 7, 8, 9),
            listOf(2, 2, 3, 3, 4, 4),
        ),
        // メカギア
        listOf(
            listOf(17, 21, 26, 30, 34, 38),
            listOf(5, 6, 7, 8, 10, 11),
            listOf(2, 3, 3, 4, 4, 5),
        ),
        // 友情
        listOf(
            listOf(0, 25, 29, 33, 38, 42),
            listOf(0, 7, 8, 10, 10, 12),
            listOf(0, 3, 4, 4, 5, 5),
        ),
    ),
)

val mechaLearningSubStatus = mapOf(
    StatusType.SPEED to (StatusType.POWER to StatusType.STAMINA),
    StatusType.STAMINA to (StatusType.WISDOM to StatusType.GUTS),
    StatusType.POWER to (StatusType.STAMINA to StatusType.WISDOM),
    StatusType.GUTS to (StatusType.SPEED to StatusType.POWER),
    StatusType.WISDOM to (StatusType.GUTS to StatusType.SPEED),
)

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

import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState

val SimulationState.mechaStatus get() = scenarioStatus as? MechaStatus

fun SimulationState.updateMechaStatus(update: MechaStatus.() -> MechaStatus): SimulationState {
    return copy(scenarioStatus = mechaStatus?.update())
}

data class MechaStatus(
    val dummy: Int = 0,
) : ScenarioStatus

enum class MechaChipType {
    Dummy,
}

data class MechaChip(
    val chipId: Int,
    val scheduleId: Int,
    val type: MechaChipType,
    val boardId: Int,
)

data class MechaChipEffect(
    val chipId: Int,
    val point: Int,
    val groupId: Int,
    val effectCategory: Int,
    val effectType: Int,
    val effectValue1: Int,
    val effectValue2: Int,
    val effectValue3: Int,
    val effectValue4: Int,
)

data class MechaLearningBonus(
    val pointMin: Int,
    val pointMax: Int,
    val baseValue: Int,
    val bonusValue: Int,
)

data class MechaLinkEffect(
    val charaId: Int,
    val effectType: Int,
    val effectValue1: Int,
    val effectValue2: Int,
    val effectValue3: Int,
    val effectValue4: Int,
)

data class MechaProgressRate(
    val progressMin: Int,
    val progressMax: Int,
    val resultType: Int,
)

data class MechaSchedule(
    val turnNum: Int,
    val targetPt: Int,
    val stockNum: Int,
    val dressId: Int,
)

data class MechaTrainingBoost(
    val boostSetId: Int,
    val levelMin: Int,
    val levelMax: Int,
    val addValue: Int,
)

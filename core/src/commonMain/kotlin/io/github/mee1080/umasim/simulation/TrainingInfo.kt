/*
 * Copyright 2021 mee1080
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
package io.github.mee1080.umasim.simulation

import io.github.mee1080.umasim.data.AoharuTeamStatusRank
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase
import kotlin.math.min

class TrainingInfo(
    val type: StatusType,
    val base: List<TrainingBase>
) {

    constructor(base: TrainingBase) : this(base.type, listOf(base))

    var level = 1
        private set

    val autoLevelUp = base[0].scenario == Scenario.URA

    var count = 0
        set(value) {
            field = if (autoLevelUp && level < 5 && value >= 4) {
                level++
                0
            } else {
                value
            }
        }

    val current get() = base.getOrElse(level - 1) { base[0] }

    val failureRate get() = current.failureRate

    fun getBaseStatus(level: Int? = null) = (level?.let { base.getOrNull(it - 1) } ?: current).status

    fun applyTeamRank(rank: AoharuTeamStatusRank) {
        level = min(level, rank.trainingLevel)
    }
}

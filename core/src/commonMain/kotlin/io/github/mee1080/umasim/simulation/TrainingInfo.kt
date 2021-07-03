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

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase

class TrainingInfo(
    val type: StatusType,
    val base: List<TrainingBase>
) {
    var level = 1
        private set

    var count = 0
        set(value) {
            field = if (level < 5 && value >= 4) {
                level++
                0
            } else {
                value
            }
        }

    val current get() = base[level - 1]

    val failureRate get() = current.failureRate

    fun getBaseStatus(level: Int? = null) = (level?.let { base[it - 1] } ?: current).status
}

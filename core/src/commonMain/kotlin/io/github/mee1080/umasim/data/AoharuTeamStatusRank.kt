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
package io.github.mee1080.umasim.data

data class AoharuTeamStatusRank(
    val ordinal: Int,
    val rank: String,
    val threshold: Int,
    val minStatus: Int,
    val maxStatus: Int,
    val maxBonus: Int,
    val trainingLevel: Int,
) {
    fun getRandomStatus() = (minStatus..maxStatus).random()

    fun getAverageStatus() = (minStatus..maxStatus).average()

    val next get() = Store.Aoharu.teamStatusRank.values.firstOrNull { it.ordinal == ordinal - 1 }
}
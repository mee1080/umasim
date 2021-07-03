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

data class Chara(
    val id: Int,
    val name: String,
    val charaId: Int,
    val charaName: String,
    val rarity: Int,
    val rank: Int,
    val speedBonus: Int,
    val staminaBonus: Int,
    val powerBonus: Int,
    val gutsBonus: Int,
    val wisdomBonus: Int,
    val initialStatus: Status,
    val imageColor: String,
) {
    fun getBonus(type: StatusType) = 100 + when (type) {
        StatusType.SPEED -> speedBonus
        StatusType.STAMINA -> staminaBonus
        StatusType.POWER -> powerBonus
        StatusType.GUTS -> gutsBonus
        StatusType.WISDOM -> wisdomBonus
        else -> 0
    }

    companion object {
        fun empty() = Chara(
            0,
            "補正なし",
            0,
            "補正なし",
            1,
            1,
            0,
            0,
            0,
            0,
            0,
            Status(),
            "FFFFFF",
        )
    }
}
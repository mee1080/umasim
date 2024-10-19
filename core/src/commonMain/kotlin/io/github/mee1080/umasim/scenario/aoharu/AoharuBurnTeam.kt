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
package io.github.mee1080.umasim.scenario.aoharu

import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType

data class AoharuBurnTeam(
    val type: StatusType,
    val status: Status,
) {
    fun getStatus(chara: Chara) = Status(
        applyBonus(status.speed, chara.speedBonus),
        applyBonus(status.stamina, chara.staminaBonus),
        applyBonus(status.power, chara.powerBonus),
        applyBonus(status.guts, chara.gutsBonus),
        applyBonus(status.wisdom, chara.wisdomBonus),
    )

    private fun applyBonus(value: Int, bonus: Int) = value * (100 + bonus) / 1000 * 10
}
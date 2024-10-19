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

import io.github.mee1080.umasim.scenario.live.Performance
import io.github.mee1080.utility.diffIntMap
import io.github.mee1080.utility.sumMapOf
import kotlin.math.max
import kotlin.math.min

interface StatusValues {
    val speed: Number
    val stamina: Number
    val power: Number
    val guts: Number
    val wisdom: Number
    val skillPt: Number
    val hp: Number
    val motivation: Number
    val maxHp: Number
    val performanceValue: Number
}

data class Status(
    override val speed: Int = 0,
    override val stamina: Int = 0,
    override val power: Int = 0,
    override val guts: Int = 0,
    override val wisdom: Int = 0,
    override val skillPt: Int = 0,
    override val hp: Int = 0,
    override val motivation: Int = 0,
    override val maxHp: Int = 0,
    val skillHint: Map<String, Int> = emptyMap(),
    val fanCount: Int = 0,
    val performance: Performance? = null,
) : StatusValues {

    override val performanceValue = performance?.totalValue ?: 0

    val statusTotal by lazy { speed + stamina + power + guts + wisdom }

    val totalPlusSkillPt by lazy { statusTotal + skillPt }

    val totalPlusSkillPtPerformanceX2 by lazy { totalPlusSkillPt * 2 + (performance?.totalValue ?: 0) }

    operator fun plus(other: Status) = Status(
        speed + other.speed,
        stamina + other.stamina,
        power + other.power,
        guts + other.guts,
        wisdom + other.wisdom,
        skillPt + other.skillPt,
        hp + other.hp,
        motivation + other.motivation,
        maxHp + other.maxHp,
        sumMapOf(skillHint, other.skillHint, max = 5),
        fanCount + other.fanCount,
        performance?.plus(other.performance) ?: other.performance,
    )

    operator fun minus(other: Status) = Status(
        speed - other.speed,
        stamina - other.stamina,
        power - other.power,
        guts - other.guts,
        wisdom - other.wisdom,
        skillPt - other.skillPt,
        hp - other.hp,
        motivation - other.motivation,
        maxHp - other.maxHp,
        diffIntMap(skillHint, other.skillHint),
        fanCount - other.fanCount,
        performance?.minus(other.performance),
    )

    private fun diffSkillHint(first: Map<String, Int>, second: Map<String, Int>): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        first.entries.forEach {
            val value = second[it.key]
            if (value != it.value) {
                result[it.key] = it.value - (value ?: 0)
            }
        }
        return result
    }

    fun adjustRange() = copy(
//        speed = max(1, min(1200, speed)),
//        stamina = max(1, min(1200, stamina)),
//        power = max(1, min(1200, power)),
//        guts = max(1, min(1200, guts)),
//        wisdom = max(1, min(1200, wisdom)),
        skillPt = max(0, skillPt),
        hp = max(0, min(maxHp, hp)),
        motivation = max(-2, min(2, motivation)),
    )

    fun adjustRange(max: Status) = copy(
        speed = max(1, min(max.speed, speed)),
        stamina = max(1, min(max.stamina, stamina)),
        power = max(1, min(max.power, power)),
        guts = max(1, min(max.guts, guts)),
        wisdom = max(1, min(max.wisdom, wisdom)),
    )

    fun get(statusType: StatusType): Int {
        return when (statusType) {
            StatusType.SPEED -> speed
            StatusType.STAMINA -> stamina
            StatusType.POWER -> power
            StatusType.GUTS -> guts
            StatusType.WISDOM -> wisdom
            StatusType.SKILL -> skillPt
            else -> 0
        }
    }

    fun add(vararg target: Pair<StatusType, Int>): Status {
        val map = mutableMapOf<StatusType, Int>()
        target.forEach {
            map[it.first] = (map[it.first] ?: 0) + it.second
        }
        return copy(
            speed = speed + (map[StatusType.SPEED] ?: 0),
            stamina = stamina + (map[StatusType.STAMINA] ?: 0),
            power = power + (map[StatusType.POWER] ?: 0),
            guts = guts + (map[StatusType.GUTS] ?: 0),
            wisdom = wisdom + (map[StatusType.WISDOM] ?: 0),
            skillPt = skillPt + (map[StatusType.SKILL] ?: 0),
        )
    }

    fun toShortString() =
        "Status($speed/$stamina/$power/$guts/$wisdom/$skillPt, HP=$hp/$maxHp/${motivationToString(motivation)}, fan=$fanCount)"

    fun countOver(value: Int) = (if (speed >= value) 1 else 0) + (if (stamina >= value) 1 else 0) +
            (if (power >= value) 1 else 0) + (if (guts >= value) 1 else 0) + (if (wisdom >= value) 1 else 0)

    fun multiplyToInt(value: Int): Status {
        val factor = (100 + value)
        return copy(
            speed = speed * factor / 100,
            stamina = stamina * factor / 100,
            power = power * factor / 100,
            guts = guts * factor / 100,
            wisdom = wisdom * factor / 100,
            skillPt = skillPt * factor / 100,
        )
    }
}

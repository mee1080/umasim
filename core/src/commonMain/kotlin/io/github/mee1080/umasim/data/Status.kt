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

import kotlin.math.max
import kotlin.math.min

data class Status(
    val speed: Int = 0,
    val stamina: Int = 0,
    val power: Int = 0,
    val guts: Int = 0,
    val wisdom: Int = 0,
    val skillPt: Int = 0,
    val hp: Int = 0,
    val motivation: Int = 0,
    val maxHp: Int = 0,
    val skillHint: Map<String, Int> = emptyMap(),
    val supportRelation: Map<Int, Int> = emptyMap(),
) {

    val statusTotal get() = speed + stamina + power + guts + wisdom

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
        mergeIntMap(skillHint, other.skillHint, 0, 5),
        mergeIntMap(supportRelation, other.supportRelation, 0, 100),
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
        diffIntMap(supportRelation, other.supportRelation),
    )

    private fun <K> mergeIntMap(first: Map<K, Int>, second: Map<K, Int>, defaultValue: Int, max: Int): Map<K, Int> {
        return mergeMap(first, second, defaultValue) { a, b -> min(max, a + b) }
    }

    private fun <K, V> mergeMap(first: Map<K, V>, second: Map<K, V>, defaultValue: V, merge: (V, V) -> V): Map<K, V> {
        if (second.isEmpty()) return first
        val result = mutableMapOf<K, V>()
        result.putAll(first)
        second.entries.forEach {
            result[it.key] = merge.invoke(result[it.key] ?: defaultValue, it.value)
        }
        return result
    }

    private fun <K> diffIntMap(first: Map<K, Int>, second: Map<K, Int>): Map<K, Int> {
        return diffMap(first, second, 0) { a, b -> a - b }
    }

    private fun <K, V> diffMap(first: Map<K, V>, second: Map<K, V>, defaultValue: V, diff: (V, V) -> V): Map<K, V> {
        val result = mutableMapOf<K, V>()
        first.entries.forEach {
            val value = second[it.key]
            if (value != it.value) {
                result[it.key] = diff(it.value, value ?: defaultValue)
            }
        }
        return result
    }

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

    fun getSupportRelation(index: Int) = supportRelation[index] ?: 0

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

    fun toShortString() = "$speed,$stamina,$power,$guts,$wisdom"
}

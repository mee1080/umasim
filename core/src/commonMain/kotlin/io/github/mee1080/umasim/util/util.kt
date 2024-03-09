/*
 * Copyright 2022 mee1080
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
package io.github.mee1080.umasim.util

import kotlin.math.min

fun <T : R, R> T.applyIf(
    condition: Boolean,
    action: T.() -> R,
) = if (condition) action() else this

fun <T : R, R> T.applyIf(
    condition: (T) -> Boolean,
    action: T.() -> R,
) = if (condition(this)) action() else this

fun <T : R, R, D> T.applyIfNotNull(
    data: D?,
    action: T.(D) -> R,
) = data?.let { action(it) } ?: this

fun <T> List<T>.mapIf(
    condition: (T) -> Boolean,
    action: T.() -> T
) = map { if (condition(it)) it.action() else it }

fun <T> List<T>.replace(
    targetIndex: Int,
    action: T.() -> T
) = mapIndexed { index, item -> if (index == targetIndex) item.action() else item }

fun <T> List<T>.replace(
    targetItem: T,
    newItem: T
) = map { if (it == targetItem) newItem else it } as List<T>

fun <K, V> Map<K, V>.mapValuesIf(
    condition: (Map.Entry<K, V>) -> Boolean,
    action: (V) -> V,
) = mapValues { if (condition(it)) action(it.value) else it.value }

fun <K, V> Map<K, V>.replace(
    key: K,
    value: V,
) = toMutableMap().apply { put(key, value) } as Map<K, V>

fun <K, V> Map<K, V>.replace(
    key: K,
    action: (V) -> V,
) = toMutableMap().apply { get(key)?.let { put(key, action(it)) } } as Map<K, V>

fun <K> sumMapOf(
    first: Map<K, Int>,
    second: Map<K, Int>,
    defaultValue: Int = 0,
    max: Int = Int.MAX_VALUE,
): Map<K, Int> {
    return mergeMap(first, second, defaultValue) { a, b -> min(max, a + b) }
}

inline fun <K, V> mergeMap(first: Map<K, V>, second: Map<K, V>, defaultValue: V, merge: (V, V) -> V): Map<K, V> {
    if (second.isEmpty()) return first
    val result = mutableMapOf<K, V>()
    result.putAll(first)
    second.entries.forEach {
        result[it.key] = merge.invoke(result[it.key] ?: defaultValue, it.value)
    }
    return result
}

fun <K> diffIntMap(first: Map<K, Int>, second: Map<K, Int>): Map<K, Int> {
    return diffMap(first, second, 0) { a, b -> a - b }
}

inline fun <K, V> diffMap(first: Map<K, V>, second: Map<K, V>, defaultValue: V, diff: (V, V) -> V): Map<K, V> {
    val result = mutableMapOf<K, V>()
    first.entries.forEach {
        val value = second[it.key]
        if (value != it.value) {
            result[it.key] = diff(it.value, value ?: defaultValue)
        }
    }
    return result
}

package io.github.mee1080.utility

import kotlin.math.min

inline fun <T> List<T>.mapIf(
    condition: (T) -> Boolean,
    mapper: (T) -> T,
) = map { if (condition(it)) mapper(it) else it }

inline fun <T> List<T>.mapIfIndexed(
    condition: (index: Int, T) -> Boolean,
    mapper: (index: Int, T) -> T,
) = mapIndexed { index, element -> if (condition(index, element)) mapper(index, element) else element }

fun <T> List<T>.replaced(
    index: Int,
    value: T,
) = toMutableList().apply { set(index, value) } as List<T>

fun <T> List<T>.replaced(
    index: Int,
    mapper: (T) -> T,
) = toMutableList().apply { set(index, mapper(get(index))) } as List<T>

fun <T> List<T>.replaceItem(
    from: T,
    to: T,
) = mapIf({ it == from }) { to }

fun <K, V> Map<K, V>.mapValuesIf(
    condition: (Map.Entry<K, V>) -> Boolean,
    action: (V) -> V,
) = mapValues { if (condition(it)) action(it.value) else it.value }

fun <K, V> Map<K, V>.replaced(
    key: K,
    value: V,
) = toMutableMap().apply { put(key, value) } as Map<K, V>

fun <K, V> Map<K, V>.replaced(
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

fun <T> List<T>.averageOf(
    selector: (T) -> Double,
) = map(selector).average()

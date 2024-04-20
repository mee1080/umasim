@file:Suppress("unused")

package io.github.mee1080.utility

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T : R, R> T.applyIf(
    condition: Boolean,
    block: T.() -> R,
): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (condition) block() else this
}

@OptIn(ExperimentalContracts::class)
inline fun <T : R, R> T.applyIf(
    condition: (T) -> Boolean,
    block: T.() -> R,
): R {
    contract {
        callsInPlace(condition, InvocationKind.EXACTLY_ONCE)
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return applyIf(condition(this), block)
}

@OptIn(ExperimentalContracts::class)
inline fun <T : R, R, D : Any> T.applyIfNotNull(
    obj: D?,
    block: T.(D) -> R,
): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return obj?.let { block(it) } ?: this
}

fun Int.plusMinus(value: Int) = (this - value)..(this + value)

fun Long.plusMinus(value: Long) = (this - value)..(this + value)

fun CharRange.randomString(size: Int) = buildString(size) {
    repeat(size) {
        append(this@randomString.random())
    }
}

fun Collection<Char>.randomString(size: Int) = buildString(size) {
    repeat(size) {
        append(this@randomString.random())
    }
}

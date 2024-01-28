package io.github.mee1080.umasim.compose.common.lib

import androidx.compose.runtime.Immutable

@Immutable
class ImmutableList<out T>(
    val data: List<T>,
) : List<T> by data {
    override fun toString() = data.toString()
}

fun <T> List<T>.toImmutable() = ImmutableList(this)

fun <T> Array<T>.toImmutable() = ImmutableList(this.asList())

fun <T> immutableListOf(vararg data: T) = data.toImmutable()
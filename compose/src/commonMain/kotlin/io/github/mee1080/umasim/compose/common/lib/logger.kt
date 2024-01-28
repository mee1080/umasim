package io.github.mee1080.umasim.compose.common.lib

private const val ACTIVE = false

fun debugLog(message: String) {
    if (ACTIVE) println(message)
}

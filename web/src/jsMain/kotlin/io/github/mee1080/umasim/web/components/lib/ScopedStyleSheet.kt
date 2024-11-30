@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.lib

import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.utils.serializeRules

// ScopedStyleSheet.kt

private var index = 0

abstract class ScopedStyleSheet : StyleSheet("scoped-${index++}-")

fun <T : StyleSheet> T.install(): T {
    appendCss(serializeRules().joinToString(" "))
    return this
}

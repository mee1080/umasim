package io.github.mee1080.umasim.web.components.material

import kotlinx.browser.document
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.StylePropertyValue
import org.jetbrains.compose.web.css.StyleScope

external fun require(module: String)

private var idGenerator: Int = 0

fun generateId(format: (Int) -> String = { "generated-id-$it" }) = format(idGenerator++)

fun <T> getElementById(id: String) = document.getElementById(id).unsafeCast<T>()

class CssVar<T : StylePropertyValue>(
    private val scope: StyleScope,
    private val name: String,
    private val fallbackValue: (() -> T)? = null,
) {

    val value get() = fallbackValue?.let { "var($name, ${it()})" } ?: "var($name)"

    fun value(defaultValue: T): String {
        return "var($name, $defaultValue)"
    }

    operator fun invoke(value: T) {
        scope.variable(name, value)
    }

    operator fun invoke(other: CssVar<T>) {
        scope.variable(name, other.value)
    }

    operator fun invoke(other: CssVar<T>, defaultValue: T) {
        scope.variable(name, other.value(defaultValue))
    }
}

fun AttrsScope<*>.slot(value: String) = attr("slot", value)
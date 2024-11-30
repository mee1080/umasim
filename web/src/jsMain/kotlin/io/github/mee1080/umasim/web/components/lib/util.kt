/*
 * Copyright 2023 mee1080
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
@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.lib

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.stringPresentation
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.MediaQueryList
import org.w3c.dom.MediaQueryListEvent
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

const val ROOT_ELEMENT_ID = "root"

external fun require(module: String): dynamic

private var idGenerator: Int = 0

fun generateId(format: (Int) -> String = { "generated-id-$it" }) = format(idGenerator++)

fun <T> getElementById(id: String) = document.getElementById(id).unsafeCast<T>()

fun AttrsScope<*>.slot(value: String) = attr("slot", value)

class TypedStyleScope<T : HTMLElement>(styleScope: StyleScope) : StyleScope by styleScope

fun <T : HTMLElement> AttrsScope<T>.styleTyped(applyVariables: TypedStyleScope<T>.() -> Unit) {
    style { TypedStyleScope<T>(this).applyVariables() }
}

inline fun <T : HTMLElement> StyleScope.variablesFor(builder: TypedStyleScope<T>.() -> Unit) {
    TypedStyleScope<T>(this).builder()
}

fun stylePermanently(styleSheet: StyleSheet) {
    val style = document.createElement("style") as HTMLStyleElement
    style.innerText = styleSheet.cssRules.joinToString("\n") {
        it.stringPresentation(indent = "", delimiter = "")
    }
    getElementById<HTMLElement>(ROOT_ELEMENT_ID).prepend(style)
}

@Composable
fun Slot(name: String, content: ContentBuilder<HTMLElement>?) {
    content?.let { Div({ slot(name) }, it) }
}

private val propCache = mutableMapOf<String, (HTMLElement, Any?) -> Unit>()

private fun setProp(name: String) = propCache.getOrPut(name) {
    { element: HTMLElement, value: Any? -> element.asDynamic()[name] = value }
}

fun AttrsScope<*>.prop(prop: String, value: Any?) = prop(setProp(prop), value)

fun appendCss(css: String) {
    val element = document.createElement("style") as HTMLStyleElement
    element.textContent = css
    document.head?.appendChild(element)
}

fun MediaQueryList.addEventListener(event: String, listener: (MediaQueryListEvent) -> Unit) {
    asDynamic().addEventListener(event, listener)
}

fun watchPrefersColorScheme(listener: (dark: Boolean) -> Unit) {
    val target = window.matchMedia("(prefers-color-scheme: dark)")
    target.addEventListener("change") {
        listener(it.matches)
    }
    listener(target.matches)
}

@Composable
fun onWindowResize(onResize: () -> Unit) {
    DisposableEffect(Unit) {
        val listener = object : EventListener {
            override fun handleEvent(event: Event) {
                onResize()
            }
        }
        window.addEventListener("resize", listener)
        onDispose {
            window.removeEventListener("resize", listener)
        }
    }
}

@Composable
fun onDocumentHidden(onHidden: () -> Unit) {
    DisposableEffect(Unit) {
        val listener = object : EventListener {
            override fun handleEvent(event: Event) {
                if (document.asDynamic().visibilityState === "hidden") {
                    onHidden()
                }
            }
        }
        document.addEventListener("visibilitychange", listener)
        onDispose {
            onHidden()
            document.removeEventListener("visibilitychange", listener)
        }
    }
}

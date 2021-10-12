package io.github.mee1080.umasim.web

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable


fun sample() {
    renderComposable(rootElementId = "root") {
        var innerSize by mutableStateOf(window.innerWidth to window.innerHeight)
        val s = object : StyleSheet() {
            val innerWidth by variable<CSSSizeValue<CSSUnit.px>>()
            val innerHeight by variable<CSSSizeValue<CSSUnit.px>>()
            private val Number.iw get() = toFloat() / 100 * innerWidth.value()
            private val Number.ih get() = toFloat() / 100 * innerHeight.value()
            val test by style {
                width(100.iw)
                height(100.ih)
                backgroundColor(Color.green)
                border(10.px, LineStyle.Solid, Color.blue)
            }
        }
        Style(s)
        LaunchedEffect("listen") {
            window.addEventListener("resize", {
                innerSize = window.innerWidth to window.innerHeight
            })
        }
        Div({
            style {
                s.innerWidth(innerSize.first.px)
                s.innerHeight(innerSize.second.px)
            }
        }) {
            Div({ classes(s.test) })
        }
    }
}
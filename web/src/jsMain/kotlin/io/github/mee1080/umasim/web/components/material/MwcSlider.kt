@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.material

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

@Composable
fun MwcSlider(
    value: Number,
    min: Number,
    max: Number,
    attrs: MwcSliderAttrsScope.() -> Unit = {},
) {
    TagElement<MwcSliderElement>("mwc-slider", {
        val scope = MwcSliderAttrsScope(this)
        attrs.invoke(scope)
        value(value)
        min(min)
        max(max)
    }, null)
}

abstract external class MwcSliderElement : HTMLElement

class MwcSliderAttrsScope(
    attrsScope: AttrsScope<MwcSliderElement>
) : AttrsScope<MwcSliderElement> by attrsScope {

    fun onInput(
        listener: (Number) -> Unit
    ) {
        addEventListener("input") {
            listener(it.nativeEvent.asDynamic().detail.value as Number)
        }
    }

    fun onChange(
        listener: (Number) -> Unit
    ) {
        addEventListener("change") {
            listener(it.nativeEvent.asDynamic().detail.value as Number)
        }
    }
}

fun AttrsScope<MwcSliderElement>.value(value: Number) = attr("value", value.toString())
fun AttrsScope<MwcSliderElement>.min(value: Number) = attr("min", value.toString())
fun AttrsScope<MwcSliderElement>.max(value: Number) = attr("max", value.toString())
fun AttrsScope<MwcSliderElement>.disabled() = attr("disabled", "")
fun AttrsScope<MwcSliderElement>.step(value: Number) = attr("step", value.toString())
fun AttrsScope<MwcSliderElement>.discrete() = attr("discrete", "")
fun AttrsScope<MwcSliderElement>.withTickMarks() = attr("withTickMarks", "")
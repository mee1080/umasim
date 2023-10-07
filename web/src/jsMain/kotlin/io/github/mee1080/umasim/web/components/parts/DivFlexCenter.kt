package io.github.mee1080.umasim.web.components.parts

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.atoms.MdSlider
import io.github.mee1080.umasim.web.components.atoms.onInput
import io.github.mee1080.umasim.web.components.atoms.step
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement


@Composable
fun DivFlexCenter(
    content: ContentBuilder<HTMLDivElement>
) {
    Div({
        style {
            display(DisplayStyle.Flex)
            flexWrap(FlexWrap.Wrap)
            alignItems(AlignItems.Center)
        }
    }, content)
}

@Composable
fun SliderEntry(label: String, value: Int, min: Int, max: Int, step: Int? = null, onInput: (Number) -> Unit) {
    DivFlexCenter {
        Span({ style { flexShrink(0) } }) { Text(label) }
        MdSlider(value, min, max) {
            step?.let { step(step) }
            onInput(onInput)
            style { width(300.px) }
        }
        Span({ style { flexShrink(0) } }) { Text(value.toString()) }
    }
}

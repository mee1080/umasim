package io.github.mee1080.umasim.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.mee1080.umasim.web.components.material.MwcButton
import io.github.mee1080.umasim.web.components.material.icon
import io.github.mee1080.umasim.web.onClickOrTouch
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.dom.Div

@Composable
fun HideBlock(
    open: Boolean,
    onChange: (Boolean) -> Unit,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Div({
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
        }
        onClickOrTouch { onChange(!open) }
    }) {
        MwcButton({
            onClickOrTouch { onChange(!open) }
            icon(if (open) "expand_less" else "expand_circle_down")
        })
        header()
    }
    if (open) content()
}

@Composable
fun HideBlock(header: @Composable () -> Unit, visible: Boolean = false, content: @Composable () -> Unit) {
    val open = remember { mutableStateOf(visible) }
    HideBlock(open.value, {
        open.value = it
    }, header, content)
}
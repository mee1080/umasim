package io.github.mee1080.umasim.compose.common.atoms

import androidx.compose.foundation.clickable
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextDecoration
import io.github.mee1080.umasim.compose.common.lib.jumpToUrl

@Composable
fun TextLink(
    url: String,
    content: String = url,
) {
    Text(
        text = content,
        style = LocalTextStyle.current.copy(textDecoration = TextDecoration.Underline),
        color = Color.Blue,
        modifier = Modifier
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable {
                jumpToUrl(url)
            },
    )
}
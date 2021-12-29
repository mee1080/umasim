package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Text

@Composable
fun LicenseInfo() {
    Hr { style { marginTop(16.px) } }
    A(
        href = "https://github.com/mee1080/umasim/blob/main/Library/web.md",
        attrs = {
            target(ATarget.Blank)
            attr("rel", "noreferrer noopener")
        }
    ) { Text("使用ライブラリ") }
}
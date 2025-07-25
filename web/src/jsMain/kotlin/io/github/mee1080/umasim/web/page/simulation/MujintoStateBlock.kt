package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.mujinto.MujintoStatus
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun MujintoStateBlock(mujintoStatus: MujintoStatus) {
    Div {
        Text(mujintoStatus.toShortString())
    }
}

package io.github.mee1080.umasim.compose.common

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.mee1080.umasim.compose.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("ウマ娘レースエミュレータ移植版") {
        App()
    }
}

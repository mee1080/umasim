package io.github.mee1080.umasim

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.mee1080.umasim.compose.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ウマ娘レースエミュレータ移植版",
        state = rememberWindowState(width = 900.dp, height = 900.dp)
    ) {
        App("LINESeedJP_A_TTF_Rg.ttf")
    }
}
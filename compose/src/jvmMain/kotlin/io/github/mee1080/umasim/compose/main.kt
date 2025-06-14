package io.github.mee1080.umasim.compose

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.mee1080.umasim.compose.common.lib.defaultThreadCount
import io.github.mee1080.umasim.mcp.runStdioMcpServer
import io.github.mee1080.utility.localMode

fun main(args: Array<String>) {
    if (args.getOrNull(0) == "mcp") {
        val simulationThreadCount = args.getOrNull(1)?.toIntOrNull() ?: defaultThreadCount
        runStdioMcpServer(simulationThreadCount)
    }
    localMode = args.contains("local")
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ウマ娘レースエミュレータ移植版",
            state = rememberWindowState(width = 900.dp, height = 900.dp)
        ) {
            App()
        }
    }
}
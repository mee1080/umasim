package io.github.mee1080.umasim.compose.common.lib

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.awt.Desktop
import java.net.URI

actual fun jumpToUrl(url: String) {
    if (Desktop.isDesktopSupported()) {
        val desktop = Desktop.getDesktop()
        if (desktop?.isSupported(Desktop.Action.BROWSE) == true) {
            desktop.browse(URI(url))
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
actual val mainDispatcher = Dispatchers.Default.limitedParallelism(1)

actual val asyncDispatcher = Dispatchers.Default

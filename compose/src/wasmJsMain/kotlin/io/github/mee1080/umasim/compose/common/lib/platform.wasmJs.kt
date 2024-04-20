package io.github.mee1080.umasim.compose.common.lib

import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

actual fun jumpToUrl(url: String) {
    window.open(url, "_blank", "noopener,noreferrer")
}

@OptIn(ExperimentalCoroutinesApi::class)
actual val mainDispatcher = Dispatchers.Default.limitedParallelism(1)

actual val asyncDispatcher = Dispatchers.Default

actual val defaultFontResource = "LINESeedJP_OTF_Rg.woff2"

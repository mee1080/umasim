package io.github.mee1080.umasim.compose.common.lib

import io.github.mee1080.umasim.compose.generated.resources.LINESeedJP_OTF_Rg
import io.github.mee1080.umasim.compose.generated.resources.Res
import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

actual fun jumpToUrl(url: String) {
    window.open(url, "_blank", "noopener,noreferrer")
}

@OptIn(ExperimentalCoroutinesApi::class)
actual val mainDispatcher = Dispatchers.Default.limitedParallelism(1)

actual val asyncDispatcher = Dispatchers.Default

actual val defaultFontResource = Res.font.LINESeedJP_OTF_Rg

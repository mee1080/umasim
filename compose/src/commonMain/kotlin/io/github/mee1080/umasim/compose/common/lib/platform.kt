package io.github.mee1080.umasim.compose.common.lib

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.FontResource

expect fun jumpToUrl(url: String)

expect val mainDispatcher: CoroutineDispatcher

expect val asyncDispatcher: CoroutineDispatcher

expect val defaultFontResource: FontResource

expect val defaultThreadCount: Int

expect val progressReportInterval: Int

expect val progressReportDelay: Long

expect fun CoroutineScope.launchCheckUpdate(onUpdate: () -> Unit)

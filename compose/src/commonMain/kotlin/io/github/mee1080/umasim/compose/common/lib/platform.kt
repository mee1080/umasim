package io.github.mee1080.umasim.compose.common.lib

import kotlinx.coroutines.CoroutineDispatcher

expect fun jumpToUrl(url: String)

expect val mainDispatcher: CoroutineDispatcher

expect val asyncDispatcher: CoroutineDispatcher

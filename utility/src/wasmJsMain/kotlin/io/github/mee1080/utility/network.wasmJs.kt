package io.github.mee1080.utility

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.Response

@OptIn(ExperimentalWasmJsInterop::class)
actual suspend fun fetchFromUrl(url: String): String {
    val response = window.fetch(url).await<Response>()
    return response.text().await<JsString>().toString()
}
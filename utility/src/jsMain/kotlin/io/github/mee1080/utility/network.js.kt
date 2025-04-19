package io.github.mee1080.utility

import kotlinx.browser.window
import kotlinx.coroutines.await

actual suspend fun fetchFromUrl(url: String): String {
    val response = window.fetch(url).await()
    return response.text().await()
}
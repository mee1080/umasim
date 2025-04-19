package io.github.mee1080.utility

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

actual suspend fun fetchFromUrl(url: String): String {
    return HttpClient().get(url).bodyAsText()
}
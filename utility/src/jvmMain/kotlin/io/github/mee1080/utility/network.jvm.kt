package io.github.mee1080.utility

import java.net.HttpURLConnection
import java.net.URL

actual suspend fun fetchFromUrl(url: String): String {
//    return HttpClient().get(url).bodyAsText()
    val url = URL(url)
    val connection = url.openConnection() as HttpURLConnection

    try {
        connection.requestMethod = "GET"
        return connection.inputStream.bufferedReader().use {
            it.readText()
        }
    } finally {
        connection.disconnect()
    }
}
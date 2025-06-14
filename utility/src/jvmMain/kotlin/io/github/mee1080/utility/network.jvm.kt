package io.github.mee1080.utility

import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

var localMode = false

actual suspend fun fetchFromUrl(url: String): String {
    if (localMode && url.startsWith("https://raw.githubusercontent.com/mee1080/umasim/refs/heads/main/")) {
        return Files.readString(
            Path.of(
                url.replace(
                    "https://raw.githubusercontent.com/mee1080/umasim/refs/heads/main/",
                    "../"
                )
            )
        )
    }
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
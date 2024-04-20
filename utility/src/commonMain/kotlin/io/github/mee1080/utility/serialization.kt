package io.github.mee1080.utility

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json by lazy {
    Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
}

inline fun <reified T> encodeToString(value: T) = json.encodeToString(value)

inline fun <reified T> decodeFromString(value: String) = json.decodeFromString<T>(value)

inline fun <reified T> decodeFromStringOrNull(value: String) = runCatching {
    json.decodeFromString<T>(value)
}.getOrNull()

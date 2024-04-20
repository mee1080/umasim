package io.github.mee1080.utility

import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

private const val fileName = "settings.conf"

private val properties by lazy {
    Properties().also { properties ->
        try {
            FileInputStream(fileName).use { properties.load(it) }
        } catch (_: IOException) {
            // ignore
        }
    }
}

actual val persistentSettings: Settings by lazy {
    PropertiesSettings(properties) { properties ->
        try {
            FileOutputStream(fileName).use { properties.store(it, null) }
        } catch (_: IOException) {
            // ignore
        }
    }
}

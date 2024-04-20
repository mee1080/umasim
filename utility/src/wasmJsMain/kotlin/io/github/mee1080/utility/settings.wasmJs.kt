package io.github.mee1080.utility

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

actual val persistentSettings: Settings by lazy { StorageSettings() }

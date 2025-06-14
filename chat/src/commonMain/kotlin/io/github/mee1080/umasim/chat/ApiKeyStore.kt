package io.github.mee1080.umasim.chat

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set // For operator function
// If you are using the no-arg factory, direct import like com.russhwolf.settings.Settings might not be needed for instantiation
// but ensure you have the correct imports for the Settings interface and any extension functions like .set or .getStringOrNull

object ApiKeyStore {
    private const val API_KEY_NAME = "gemini_api_key"
    private val settings: Settings by lazy { Settings() } // Instantiate using the no-arg constructor

    fun saveApiKey(apiKey: String) {
        settings[API_KEY_NAME] = apiKey // Using operator extension
    }

    fun getApiKey(): String? {
        return settings.getStringOrNull(API_KEY_NAME)
    }

    fun clearApiKey() {
        settings.remove(API_KEY_NAME)
    }
}

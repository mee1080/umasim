package io.github.mee1080.umasim.chat

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set // For operator function

object ModelNameStore {
    private const val MODEL_NAME_KEY = "selected_model_name"
    private val settings: Settings by lazy { Settings() } // Instantiate using the no-arg constructor

    fun saveModelName(modelName: String) {
        settings[MODEL_NAME_KEY] = modelName // Using operator extension
    }

    fun getModelName(): String? {
        return settings.getStringOrNull(MODEL_NAME_KEY)
    }

    fun clearModelName() {
        settings.remove(MODEL_NAME_KEY)
    }
}

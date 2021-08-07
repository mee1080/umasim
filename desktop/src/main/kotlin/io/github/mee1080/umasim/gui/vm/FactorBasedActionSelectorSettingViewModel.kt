package io.github.mee1080.umasim.gui.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.mee1080.umasim.ai.FactorBasedActionSelector

class FactorBasedActionSelectorSettingViewModel {

    var option by mutableStateOf(FactorBasedActionSelector.Option())
        private set

    fun updateOption(value: FactorBasedActionSelector.Option) {
        option = value
    }

    val presets = listOf(
        "スピ3パワ2友人1" to FactorBasedActionSelector.speedPower,
        "スピ3賢さ2友人1" to FactorBasedActionSelector.speedWisdom,
        "スピ3スタ3" to FactorBasedActionSelector.speedStamina,
        "スピ2根性4" to FactorBasedActionSelector.speedGuts,
        "パワ3賢さ3" to FactorBasedActionSelector.powerWisdom,
    )

    var preset by mutableStateOf<FactorBasedActionSelector.Option?>(null)
        private set

    fun updatePreset(value: FactorBasedActionSelector.Option?) {
        preset = value
    }

    fun applyPreset() {
        preset?.let { updateOption(it) }
    }
}
/*
 * Copyright 2021 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
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
        "スピ2賢さ2パワ1友人1" to FactorBasedActionSelector.speedWisdomPower,
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
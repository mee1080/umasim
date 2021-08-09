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
package io.github.mee1080.umasim.gui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.gui.vm.FactorBasedActionSelectorSettingViewModel
import kotlin.math.roundToInt

@Composable
fun FactorBasedActionSelectorSetting(
    model: FactorBasedActionSelectorSettingViewModel,
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
) {
    Column(modifier = modifier) {
        Text("プリセット", modifier = Modifier.padding(8.dp))
        Row {
            Spinner(
                "選択",
                model.presets,
                modifier = Modifier.padding(8.dp).align(Alignment.CenterVertically),
                onSelect = { model.updatePreset(it.second) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                candidateToText = { it.first },
            )
            Button(
                onClick = { model.applyPreset() },
                modifier = Modifier.padding(8.dp).align(Alignment.CenterVertically),
                enabled = model.preset != null,
            ) {
                Text("反映")
            }
        }
        Text("設定", modifier = Modifier.padding(8.dp))
        SettingSlider("スピード", model.option.speedFactor) {
            model.updateOption(model.option.copy(speedFactor = it))
        }
        SettingSlider("スタミナ", model.option.staminaFactor) {
            model.updateOption(model.option.copy(staminaFactor = it))
        }
        SettingSlider("パワー", model.option.powerFactor) {
            model.updateOption(model.option.copy(powerFactor = it))
        }
        SettingSlider("根性", model.option.gutsFactor) {
            model.updateOption(model.option.copy(gutsFactor = it))
        }
        SettingSlider("賢さ", model.option.wisdomFactor) {
            model.updateOption(model.option.copy(wisdomFactor = it))
        }
        SettingSlider("スキルPt", model.option.skillPtFactor) {
            model.updateOption(model.option.copy(skillPtFactor = it))
        }
        SettingSlider("体力", model.option.hpFactor) {
            model.updateOption(model.option.copy(hpFactor = it))
        }
        SettingSlider("やる気", model.option.motivationFactor, 20.0) {
            model.updateOption(model.option.copy(motivationFactor = it))
        }
        Button(
            onClick = onClose,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("完了") }
    }
}

@Composable
private fun SettingSlider(label: String, value: Double, max: Double = 2.0, onValueChange: (Double) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            label,
            Modifier
                .padding(8.dp, 0.dp)
                .width(80.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            ((value * 100).roundToInt() / 100.0).toString(),
            Modifier
                .width(60.dp)
                .align(Alignment.CenterVertically)
        )
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange((it.toDouble() * 100).roundToInt() / 100.0) },
            modifier = Modifier.fillMaxWidth().padding(8.dp, 0.dp).align(Alignment.CenterVertically),
            valueRange = 0f..max.toFloat(),
            steps = (max * 100).toInt() / 5 - 1,
            colors = SliderDefaults.colors(
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
            )
        )
    }
}
package io.github.mee1080.umasim.gui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
        Text("プリセット")
        Row {
            Spinner(
                "選択",
                model.presets,
                { model.updatePreset(it.second) },
                { it.first },
            )
            Button(
                onClick = { model.applyPreset() },
                enabled = model.preset != null,
            ) {
                Text("反映")
            }
        }
        Text("設定")
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
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, Modifier.width(80.dp))
        Text(((value * 100).roundToInt() / 100.0).toString(), Modifier.width(60.dp))
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toDouble()) },
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..max.toFloat(),
            steps = (max * 100).toInt() / 5 - 1,
            colors = SliderDefaults.colors(
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
            )
        )
    }
}
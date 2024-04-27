package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.utility.roundToString
import kotlin.math.roundToInt

@Composable
fun LastSimulationDetailOutput(state: AppState) {
    val settingState = derivedStateOf { state.lastSimulationSettingWithPassive }
    val setting = settingState.value ?: return
    Column {
        Text("補正後ステータス：${setting.modifiedSpeed}/${setting.modifiedStamina}/${setting.modifiedPower}/${setting.modifiedGuts}/${setting.modifiedWisdom}")
        Text(
            "初期耐力：${setting.spMax.roundToString(2)}/金回復≒${
                setting.equalStamina(550).roundToInt()
            }スタミナ/白回復≒${
                setting.equalStamina(150).roundToInt()
            }スタミナ/終盤耐力消耗係数：${
                setting.spurtSpCoef.roundToString(3)
            }"
        )
        Text(
            "スキル発動率：${setting.skillActivateRate.roundToString(1)}%/掛かり率：${
                setting.temptationRate.roundToString(1)
            }%"
        )
        Text("スタート　目標速度：${setting.v0.roundToString(2)} 加速度：${setting.a0.roundToString(2)}")
        Text("序盤　目標速度：${setting.v1.roundToString(2)} 加速度：${setting.a1.roundToString(2)}")
        Text("中盤　目標速度：${setting.v2.roundToString(2)} 加速度：${setting.a2.roundToString(2)}")
        Text("終盤　目標速度：${setting.v3.roundToString(2)} 加速度：${setting.a3.roundToString(2)}")
        Text("最高スパート速度：${setting.maxSpurtSpeed.roundToString(2)}")
    }
}
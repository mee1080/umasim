package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import io.github.mee1080.umasim.race.data.Style
import io.github.mee1080.umasim.race.data.secondPerFrame
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
        Text("最高スパート　速度：${setting.maxSpurtSpeed.roundToString(2)}")
        if (setting.runningStyle == Style.NIGE) {
            Text("位置取り争い　速度：${setting.leadCompetitionSpeed.roundToString(2)}")
        }
        Text(
            "位置取り調整　速度：${setting.positionCompetitionSpeed.roundToString(2)} 耐力：${
                setting.positionCompetitionStamina.roundToString(2)
            }"
        )
        if (setting.runningStyle != Style.OI) {
            Text(
                "リード確保　速度：${setting.secureLeadSpeed.roundToString(2)} 耐力：${
                    setting.secureLeadStamina.roundToString(2)
                }"
            )
        }
        Text(
            "脚色十分　加速度基本値：${setting.conservePowerAccelerationBase.roundToString(2)} 継続時間：${
                (setting.conservePowerFrame * secondPerFrame).roundToString(2)
            }"
        )
        Text("スタミナ勝負　速度：${setting.staminaLimitBreakSpeed.roundToString(2)}")
        Text(
            "追い比べ　速度：${setting.competeFightSpeed.roundToString(2)} 加速度：${
                setting.competeFightAcceleration.roundToString(2)
            }"
        )
    }
}
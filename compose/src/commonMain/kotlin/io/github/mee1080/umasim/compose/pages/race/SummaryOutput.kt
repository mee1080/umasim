package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.lib.roundString
import io.github.mee1080.umasim.compose.common.lib.toPercentString
import io.github.mee1080.umasim.compose.common.lib.toTimeString
import io.github.mee1080.umasim.compose.common.parts.LinedTable
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.SimulationSummary
import io.github.mee1080.umasim.store.SimulationSummaryEntry
import kotlin.math.roundToInt

@Composable
fun SummaryOutput(state: AppState) {
    val summary = state.simulationSummary ?: return
    val setting = state.setting
    Column {
        Text("結果", style = MaterialTheme.typography.headlineSmall)
        Text("最大スパート率：${summary.spurtRate.toPercentString(2)}%")
        SummaryTable(summary)
        Text("補正後ステータス：${setting.modifiedSpeed}/${setting.modifiedStamina}/${setting.modifiedPower}/${setting.modifiedGuts}/${setting.modifiedWisdom}")
        Text(
            "初期耐力：${setting.spMax}/金回復≒${
                setting.equalStamina(550).roundToInt()
            }スタミナ/白回復≒${
                setting.equalStamina(150).roundToInt()
            }スタミナ/終盤耐力消耗係数：${
                setting.spurtSpCoef.roundString(3)
            }"
        )
        Text(
            "スキル発動率：${setting.skillActivateRate.roundString(1)}%/掛かり率：${
                setting.temptationRate.roundString(1)
            }%"
        )
        Text("スタート　目標速度：${setting.v0.roundString(2)} 加速度：${setting.a0.roundString(2)}")
        Text("序盤　目標速度：${setting.v1.roundString(2)} 加速度：${setting.a1.roundString(2)}")
        Text("中盤　目標速度：${setting.v2.roundString(2)} 加速度：${setting.a2.roundString(2)}")
        Text("終盤　目標速度：${setting.v3.roundString(2)} 加速度：${setting.a3.roundString(2)}")
        Text("最高スパート速度：${setting.maxSpurtSpeed.roundString(2)}")
    }
}

@Composable
private fun SummaryTable(summary: SimulationSummary) {
    val tableData = derivedStateOf {
        buildList {
            add(listOf("", "平均タイム", "最速タイム", "最遅タイム", "平均余剰耐力", "最大余剰耐力", "最小余剰耐力"))
            add(toTableData("全体", summary.allSummary))
            add(toTableData("最大スパート", summary.spurtSummary))
            add(toTableData("非最大スパート", summary.notSpurtSummary))
        }
    }
    LinedTable(
        rowCount = 4, columnCount = 7,
        modifier = Modifier.fillMaxWidth(),
    ) { row, column ->
        val modifier = (if (column == 0) Modifier.width(192.dp) else Modifier.weight(1f))
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp)
        Column(
            horizontalAlignment = when {
                row == 0 -> Alignment.CenterHorizontally
                column == 0 -> Alignment.Start
                else -> Alignment.End
            },
            verticalArrangement = Arrangement.Center,
            modifier = modifier,
        ) {
            Text(tableData.value[row][column])
        }
    }
}

private fun toTableData(label: String, entry: SimulationSummaryEntry): List<String> {
    return if (entry.count == 0) {
        listOf(label, "-", "-", "-", "-", "-", "-")
    } else {
        listOf(
            label,
            entry.averageTime.toTimeString(), entry.bestTime.toTimeString(), entry.worstTime.toTimeString(),
            entry.averageSp.roundString(1), entry.bestSp.roundString(1), entry.worstSp.roundString(1),
        )
    }
}
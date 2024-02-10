package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.lib.roundString
import io.github.mee1080.umasim.compose.common.lib.toPercentString
import io.github.mee1080.umasim.compose.common.lib.toTimeString
import io.github.mee1080.umasim.compose.common.parts.LinedTable
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.SimulationSkillSummary
import io.github.mee1080.umasim.store.SimulationSummary
import io.github.mee1080.umasim.store.SimulationSummaryEntry
import kotlin.math.roundToInt

@Composable
fun SummaryOutput(state: AppState) {
    val summary = state.simulationSummary ?: return
    val setting = state.setting
    Column {
        Text("結果", style = MaterialTheme.typography.headlineSmall)
        Text("最大スパート率：${summary.spurtRate.toPercentString(2)}")
        SummaryTable(summary)
        Text("補正後ステータス：${setting.modifiedSpeed}/${setting.modifiedStamina.roundToInt()}/${setting.modifiedPower.roundToInt()}/${setting.modifiedGuts.roundToInt()}/${setting.modifiedWisdom.roundToInt()}")
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
        SkillTable(summary)
    }
}

@Composable
private fun SummaryTable(summary: SimulationSummary) {
    val tableData by remember {
        derivedStateOf {
            buildList {
                add(
                    listOf(
                        "",
                        "平均タイム",
                        "最速タイム",
                        "最遅タイム",
                        "平均余剰耐力",
                        "最大余剰耐力",
                        "最小余剰耐力"
                    )
                )
                add(toTableData("全体", summary.allSummary))
                add(toTableData("最大スパート", summary.spurtSummary))
                add(toTableData("非最大スパート", summary.notSpurtSummary))
            }
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
            Text(tableData[row][column])
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

@Composable
private fun SkillTable(summary: SimulationSummary) {
    val summaries = summary.skillSummaries
    if (summaries.isEmpty()) return
    val tableData by remember {
        derivedStateOf {
            buildList {
                add(
                    listOf(
                        "発動数",
                        "発動率",
                        "平均発動位置1",
                        "2回発動率",
                        "平均発動位置2",
                        "序盤発動率",
                        "中盤接続率",
                        "平均中盤接続時間",
                        "中盤発動率",
                        "終盤接続率",
                        "平均終盤接続時間",
                        "終盤発動率",
                        "平均終盤遅延",
                    )
                )
                summaries.forEach { add(toTableData(it.second)) }
            }
        }
    }
    Text("スキル情報", modifier = Modifier.padding(top = 8.dp))
    Row {
        Column {
            Text("", Modifier.padding(4.dp))
            summaries.forEach {
                Text(it.first.name, Modifier.padding(4.dp))
            }
        }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState),
            ) {
                for (col in 0..<13) {
                    Column(
                        Modifier
                            .padding(horizontal = 2.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        tableData.forEachIndexed { row, item ->
                            Text(
                                item[col], Modifier.padding(4.dp).align(
                                    if (row == 0) Alignment.CenterHorizontally else Alignment.End
                                )
                            )
                        }
                    }
                }
            }
            HorizontalScrollbar(rememberScrollbarAdapter(scrollState), Modifier.fillMaxWidth())
        }
    }
}

private fun toTableData(entry: SimulationSkillSummary): List<String> {
    return if (entry.count == 0) {
        listOf("0") + List(12) { "-" }
    } else {
        listOf(
            entry.count.toString(),
            entry.triggerRate.toPercentString(1),
            (entry.averageStartFrame1 / 15.0).roundString(2, "s"),
            entry.doubleTriggerRate.toPercentString(1),
            (entry.averageStartFrame2 / 15.0).roundString(2, "s"),
            entry.phase0TriggeredRate.toPercentString(1),
            entry.phase1ConnectionRate.toPercentString(1),
            (entry.averagePhase1ConnectionFrame / 15.0).roundString(2, "s"),
            entry.phase1TriggeredRate.toPercentString(1),
            entry.phase2ConnectionRate.toPercentString(1),
            (entry.averagePhase2ConnectionFrame / 15.0).roundString(2, "s"),
            entry.phase2TriggeredRate.toPercentString(1),
            (entry.averagePhase2DelayFrame / 15.0).roundString(2, "s"),
        )
    }
}
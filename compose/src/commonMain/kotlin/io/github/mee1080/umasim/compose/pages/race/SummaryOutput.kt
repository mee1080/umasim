package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.LabeledCheckbox
import io.github.mee1080.umasim.compose.common.parts.LinedTable
import io.github.mee1080.umasim.compose.common.parts.NumberInput
import io.github.mee1080.umasim.compose.common.parts.Table
import io.github.mee1080.umasim.race.calc2.RaceSetting
import io.github.mee1080.umasim.store.*
import io.github.mee1080.utility.roundToString
import io.github.mee1080.utility.secondToTimeString
import io.github.mee1080.utility.toPercentString
import kotlin.math.roundToInt

@Composable
fun SummaryOutput(state: AppState) {
    val summary = state.simulationSummary ?: return
    Column {
        Text("結果", style = MaterialTheme.typography.headlineSmall)
        Text("最大スパート率：${summary.spurtRate.toPercentString(2)}")
        SummaryTable(summary)
        SkillTable(summary)
    }
}

private val tableHeader = listOf(
    "",
    "平均タイム",
    "最速タイム",
    "最遅タイム",
    "平均余剰耐力",
    "最大余剰耐力",
    "最小余剰耐力",
    "位置取り調整回数",
    "持久力温存発生率",
    "持久力温存平均距離",
    "追い比べ完走率",
    "追い比べ平均時間",
)

@Composable
private fun SummaryTable(summary: SimulationSummary) {
    Column {
        val scrollState = rememberScrollState()
        val tableData = buildList {
            add(tableHeader)
            add(toTableData("全体", summary.allSummary))
            add(toTableData("最大スパート", summary.spurtSummary))
            add(toTableData("非最大スパート", summary.notSpurtSummary))
        }
        LinedTable(
            rowCount = 4, columnCount = tableHeader.size,
            modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState),
            cellBackground = MaterialTheme.colorScheme.surface,
            cellPadding = 4.dp,
        ) { row, column ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = when {
                    row == 0 -> Alignment.CenterHorizontally
                    column == 0 -> Alignment.Start
                    else -> Alignment.End
                },
                verticalArrangement = Arrangement.Center,
            ) {
                Text(tableData[row][column])
            }
        }
        HorizontalScrollbar(rememberScrollbarAdapter(scrollState), Modifier.fillMaxWidth())
    }
}

private fun toTableData(label: String, entry: SimulationSummaryEntry): List<String> {
    return if (entry.count == 0) {
        listOf(label, "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
    } else {
        listOf(
            label,
            entry.averageTime.secondToTimeString(),
            entry.bestTime.secondToTimeString(),
            entry.worstTime.secondToTimeString(),
            entry.averageSp.roundToString(1),
            entry.bestSp.roundToString(1),
            entry.worstSp.roundToString(1),
            entry.positionCompetitionCount.roundToString(2),
            entry.staminaKeepRate.toPercentString(1),
            entry.staminaKeepDistance.roundToString(1),
            entry.competeFightFinishRate.toPercentString(1),
            entry.competeFightTime.roundToString(1),
        )
    }
}

@Composable
private fun SkillTable(summary: SimulationSummary) {
    val summaries = summary.skillSummaries
    if (summaries.isEmpty()) return
    val tableData = buildList {
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
                "速度上昇無効割合",
            )
        )
        summaries.forEach { add(toTableData(summary.setting, it.second)) }
    }
    var calcSp by remember { mutableStateOf(true) }
    var kire by remember { mutableStateOf(false) }
    val rareHintLvList = remember {
        SnapshotStateList(summaries.size) { 2 }
    }
    val normalHintLvList = remember {
        SnapshotStateList(summaries.size) { 4 }
    }
    val skillPtList by derivedStateOf {
        val kireFactor = if (kire) 0.1 else 0.0
        summaries.mapIndexed { index, (_, skill) ->
            val rareFactor = skillLvToFactor[rareHintLvList[index]] - kireFactor
            val normalFactor = skillLvToFactor[normalHintLvList[index]] - kireFactor
            (skill.rareSkillPt * rareFactor).toInt() + skill.normalSkillPt.sumOf { (it * normalFactor).toInt() }
        }
    }
    Row(
        modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text("スキル情報")
        LabeledCheckbox(calcSp, { calcSp = it }) {
            Text("SP計算")
        }
    }
    Row {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("", Modifier.padding(4.dp))
            summaries.forEach {
                Text(it.first, Modifier.padding(4.dp))
            }
        }
        Table(
            tableData.size, 14, scrollable = true,
            modifier = Modifier.weight(1f),
        ) { row, col ->
            Text(
                tableData[row][col], Modifier.padding(4.dp).align(
                    when {
                        row == 0 -> Alignment.Center
                        col == 2 || col == 4 -> Alignment.CenterStart
                        else -> Alignment.CenterEnd
                    }
                )
            )
        }
        if (calcSp) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("SP / ヒントLv / 下位", Modifier.padding(4.dp))
                summaries.forEachIndexed { index, (_, skill) ->
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = skillPtList[index].toString(),
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.End,
                        )
                        if (skill.rareSkillPt > 0) {
                            Text("/")
                            NumberInput(
                                value = rareHintLvList[index], onValueChange = { rareHintLvList[index] = it },
                                min = 0, max = 5,
                            )
                        }
                        if (skill.normalSkillPt.isNotEmpty()) {
                            Text("/")
                            NumberInput(
                                value = normalHintLvList[index], onValueChange = { normalHintLvList[index] = it },
                                min = 0, max = 5,
                            )
                        }
                    }
                }
            }
        }
    }
    if (calcSp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("合計SP: ${skillPtList.sum()}")
            LabeledCheckbox(kire, { kire = it }) { Text("切れ者") }
        }
    }
}

private fun toTableData(setting: RaceSetting, entry: SimulationSkillSummary): List<String> {
    return if (entry.count == 0) {
        listOf("0") + List(13) { "-" }
    } else {
        listOf(
            entry.count.toString(),
            entry.triggerRate.toPercentString(1),
            (entry.averageStartFrame1 / 15.0).roundToString(2, "s") + "/" +
                    entry.averageStartPosition1.roundToString(2, "m") +
                    toPositionString(setting, entry.averageStartPosition1),
            entry.doubleTriggerRate.toPercentString(1),
            (entry.averageStartFrame2 / 15.0).roundToString(2, "s") + "/" +
                    entry.averageStartPosition2.roundToString(2, "m") +
                    toPositionString(setting, entry.averageStartPosition2),
            entry.phase0TriggeredRate.toPercentString(1),
            entry.phase1ConnectionRate.toPercentString(1),
            (entry.averagePhase1ConnectionFrame / 15.0).roundToString(2, "s"),
            entry.phase1TriggeredRate.toPercentString(1),
            entry.phase2ConnectionRate.toPercentString(1),
            (entry.averagePhase2ConnectionFrame / 15.0).roundToString(2, "s"),
            entry.phase2TriggeredRate.toPercentString(1),
            (entry.averagePhase2DelayFrame / 15.0).roundToString(2, "s"),
            entry.invalidRate.toPercentString(1),
        )
    }
}

private fun toPositionString(setting: RaceSetting, position: Double): String {
    return when {
        position.isNaN() || position.isInfinite() -> ""
        position < setting.phase0Half -> ""
        position < setting.phase1Start -> "(中盤入り前${(setting.phase1Start - position).roundToInt()}m)"
        position < setting.phase1Half -> "(中盤開始${(position - setting.phase1Start).roundToInt()}m)"
        position < setting.phase2Start -> "(終盤入り前${(setting.phase2Start - position).roundToInt()}m)"
        position < setting.phase2Half -> "(終盤開始${(position - setting.phase2Start).roundToInt()}m)"
        else -> "(ゴール前${(setting.courseLength - position).roundToInt()}m)"
    }
}

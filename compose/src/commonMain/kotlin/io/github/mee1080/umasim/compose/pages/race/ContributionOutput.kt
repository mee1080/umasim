package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.parts.Table
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.utility.roundToString
import io.github.mee1080.utility.secondToTimeString

val efficiencyColors = listOf(
    Color.LightGray,
    Color.Blue,
    Color.Green,
    Color(255, 128, 0),
    Color.Yellow,
    Color.Red,
)

@Composable
fun ContributionOutput(state: AppState) {
    val results by derivedStateOf { state.contributionResults }
    if (results.isEmpty()) return
    val names = listOf("") + results.map { it.name }
    val tableData = listOf(
        listOf("平均", "上振れ20%", "下振れ20%", "SP効率(秒/100SP、青:Lv1～赤:Lv5)", "備考")
    ) + results.map { entry ->
        if (entry.averageDiff.isNaN()) {
            listOf(
                entry.averageTime.secondToTimeString(),
                entry.upperTime.secondToTimeString(),
                entry.lowerTime.secondToTimeString(),
                "",
                "",
            )
        } else {
            val skill = state.setting.hasSkills.firstOrNull { it.name == entry.name }
            listOf(
//                "${entry.averageTime.secondToTimeString()} (${entry.averageDiff.roundToString(3, displayPlus = true)})",
//                "${entry.upperTime.secondToTimeString()} (${entry.upperDiff.roundToString(3, displayPlus = true)})",
//                "${entry.lowerTime.secondToTimeString()} (${entry.lowerDiff.roundToString(3, displayPlus = true)})",
                entry.averageDiff.roundToString(3, displayPlus = true),
                entry.upperDiff.roundToString(3, displayPlus = true),
                entry.lowerDiff.roundToString(3, displayPlus = true),
                entry.efficiency[0].roundToString(4),
                skill?.notice?.joinToString(", ") ?: "",
            )
        }
    }
    Column {
        Text(state.simulationMode.label, style = MaterialTheme.typography.headlineSmall)
        Row {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                names.forEach { Text(it, Modifier.padding(4.dp)) }
            }
            Table(results.size + 1, 5, scrollable = true) { row, col ->
                if (row != 0 && col == 3) {
                    Box(Modifier.padding(4.dp).align(Alignment.CenterStart)) {
                        val efficiency = results[row - 1].efficiency
                        if (!efficiency[0].isNaN()) {
                            Row(Modifier.height(16.dp).align(Alignment.CenterStart)) {
                                for (i in 0..5) {
                                    val width = efficiency[i] - efficiency.getOrElse(i - 1) { 0.0 }
                                    Box(
                                        Modifier.fillMaxHeight().width((width * 2000).dp)
                                            .background(efficiencyColors[i])
                                    )
                                }
                            }
                        }
                        Text(
                            tableData[row][col],
                            Modifier.align(Alignment.CenterStart),
                        )
                    }
                } else {
                    Text(
                        tableData[row][col],
                        Modifier.padding(4.dp)
                            .align(if (row == 0 || col == 4) Alignment.CenterStart else Alignment.CenterEnd),
                    )
                }
            }
        }
        Text("※信頼できるデータ出すにはシミュレーション回数10000回ぐらいは必要です。特に斜行セットとか。")
    }
}
package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.parts.Table
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.utility.roundToString
import io.github.mee1080.utility.secondToTimeString

@Composable
fun ContributionOutput(state: AppState) {
    val results by derivedStateOf { state.contributionResults }
    if (results.isEmpty()) return
    val names = listOf("") + results.map { it.name }
    val tableData = listOf(
        listOf("平均", "上振れ20%", "下振れ20%", "備考")
    ) + results.map { entry ->
        if (entry.averageDiff.isNaN()) {
            listOf(
                entry.averageTime.secondToTimeString(),
                entry.upperTime.secondToTimeString(),
                entry.lowerTime.secondToTimeString(),
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
            Table(results.size + 1, 4, scrollable = true) { row, col ->
                Text(
                    tableData[row][col],
                    Modifier.padding(4.dp)
                        .align(if (row == 0 || col == 3) Alignment.CenterStart else Alignment.CenterEnd),
                )
            }
        }
        Text("※シミュレーション回数1000回で0.05s程度の誤差が出ます")
    }
}
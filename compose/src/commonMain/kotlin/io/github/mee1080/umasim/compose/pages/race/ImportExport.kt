package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.MyButton
import io.github.mee1080.umasim.race.calc2.UmaStatus
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.ImportExportConverter
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.importChara

@Composable
fun ImportExport(virtual: Boolean, state: AppState, dispatch: OperationDispatcher<AppState>) {
    val chara by derivedStateOf { state.chara(virtual) }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ImportDialog(virtual, dispatch)
        ExportDialog(chara)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ImportDialog(virtual: Boolean, dispatch: OperationDispatcher<AppState>) {
    var open by remember { mutableStateOf(false) }
    MyButton({ open = true }) {
        Text("インポート")
    }
    if (open) {
        var input by remember { mutableStateOf("") }
        var result by remember { mutableStateOf<UmaStatus?>(null) }
        AlertDialog(
            onDismissRequest = { open = false },
            text = {
                Column {
                    Text("※枠内にCtrl+Vなどでペーストし、インポートを押してください(Androidたぶん非対応)")
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        modifier = Modifier.heightIn(max = 200.dp),
                    )
                    MyButton(
                        onClick = { result = ImportExportConverter.importChara(input) },
                        modifier = Modifier.padding(vertical = 8.dp),
                    ) {
                        Text("インポート")
                    }
                    result?.let {
                        Text("キャラ：${it.charaName}")
                        Text("ステータス：${it.speed}/${it.stamina}/${it.power}/${it.guts}/${it.wisdom}")
                        Text("適性：バ場${it.surfaceFit}/距離${it.distanceFit}/脚質${it.styleFit}")
                        if (it.hasSkills.isNotEmpty()) {
                            Text("スキル：")
                            FlowRow(Modifier.padding(start = 16.dp)) {
                                it.hasSkills.forEach { skill ->
                                    Text("${skill.name}, ")
                                }
                            }
                        }
                        Text("※インポートされるのは、ステータス、適性、スキルのみです")
                    }
                }
            },
            confirmButton = {
                MyButton(
                    onClick = {
                        result?.let {
                            dispatch(importChara(virtual, it))
                        }
                        open = false
                    },
                    enabled = result != null,
                ) {
                    Text("反映")
                }
            },
            dismissButton = {
                MyButton({ open = false }) {
                    Text("キャンセル")
                }
            }
        )
    }
}

@Composable
private fun ExportDialog(chara: UmaStatus) {
    var open by remember { mutableStateOf(false) }
    MyButton({ open = true }) {
        Text("エクスポート")
    }
    if (open) {
        var value by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            value = ImportExportConverter.exportChara(chara)
        }
        AlertDialog(
            onDismissRequest = { open = false },
            text = {
                Column {
                    OutlinedTextField(
                        value = value,
                        onValueChange = {},
                        readOnly = true,
                    )
                    Text("※枠内をCtrl+Cなどでコピーしてください(Android非対応)")
                    Text("※エクスポートされるのは、ステータス、適性、スキルのみです")
                }
            },
            confirmButton = {
                MyButton({ open = false }) {
                    Text("閉じる")
                }
            },
        )
    }
}

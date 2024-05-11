package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.MyButton
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.SimulationMode
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.runSimulation
import io.github.mee1080.utility.toPercentString

@Composable
fun ActionInput(state: AppState, dispatch: OperationDispatcher<AppState>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MyButton(
                onClick = { dispatch(runSimulation()) },
                enabled = state.simulationCount > 0 && state.simulationProgress == 0,
            ) {
                Text("エミュレート開始")
            }
            if (state.simulationMode == SimulationMode.NORMAL) {
                MyButton(
                    onClick = { dispatch(runSimulation(1)) },
                    enabled = state.simulationProgress == 0,
                ) {
                    Text("1回のみ")
                }
            }
        }
        if (state.simulationCount > 0 && state.simulationProgress > 0) {
            Column(Modifier.fillMaxWidth()) {
                val progress = state.simulationProgress.toFloat() / state.simulationCount
                Text(progress.toPercentString())
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.MyButton
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.runSimulation

@Composable
fun RacePage(state: AppState, dispatch: OperationDispatcher<AppState>) {
    Column(
        Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header()
        CharaInput(state, dispatch)
        CourseInput(state, dispatch)
        SkillInput(state, dispatch)
        SettingInput(state, dispatch)
        HorizontalDivider()
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MyButton(
                onClick = { dispatch(runSimulation()) },
                enabled = state.simulationCount > 0 && state.simulationProgress == 0,
            ) {
                Text("エミュレート開始")
            }
            MyButton(
                onClick = { dispatch(runSimulation(1)) },
                enabled = state.simulationProgress == 0,
            ) {
                Text("1回のみ")
            }
            if (state.simulationCount > 0 && state.simulationProgress > 0) {
                LinearProgressIndicator(
                    progress = { state.simulationProgress.toFloat() / state.simulationCount },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        SummaryOutput(state)
        GraphOutput(state)
        Footer()
    }
}
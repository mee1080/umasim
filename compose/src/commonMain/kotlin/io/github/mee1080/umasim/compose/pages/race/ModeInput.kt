package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.LabeledCheckbox
import io.github.mee1080.umasim.compose.common.atoms.SelectBox
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.SimulationMode
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.setContributionTarget
import io.github.mee1080.umasim.store.operation.setSimulationMode

@Composable
fun ModeInput(state: AppState, dispatch: OperationDispatcher<AppState>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SelectBox(
            SimulationMode.entries, state.simulationMode,
            onSelect = { dispatch(setSimulationMode(it)) },
            label = { Text("モード") },
            itemToString = { it.label },
        )
        if (state.simulationMode == SimulationMode.CONTRIBUTION || state.simulationMode == SimulationMode.CONTRIBUTION2) {
            ContributionSetting(state, dispatch)
        }
    }
}

@Composable
private fun ContributionSetting(state: AppState, dispatch: OperationDispatcher<AppState>) {
    Column {
        state.hasSkills(false).forEach { skill ->
            val id = skill.id
            LabeledCheckbox(
                selected = state.contributionTargets.contains(id),
                onCheckedChange = { dispatch(setContributionTarget(id, it)) },
            ) {
                Text(skill.name)
            }
        }
    }
}
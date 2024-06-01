package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher

@Composable
fun RacePage(state: AppState, dispatch: OperationDispatcher<AppState>) {
    Column(
        Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header()
        ImportExport(false, state, dispatch)
        CharaInput(false, state, dispatch)
        CourseInput(state, dispatch)
        SkillInput(false, state, dispatch)
        SettingInput(state, dispatch)
        HorizontalDivider()
        ModeInput(state, dispatch)
        ActionInput(state, dispatch)
        SummaryOutput(state)
        GraphOutput(state)
        LastSimulationDetailOutput(state)
        ContributionOutput(state)
        ApproximateSetting(state, dispatch)
        Footer()
    }
}
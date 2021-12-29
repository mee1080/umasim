package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.LabeledRadioGroup
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

@Composable
fun ScenarioSelect(model: ViewModel, state: State) {
    H2 { Text("育成シナリオ") }
    LabeledRadioGroup("scenario", "シナリオ：", WebConstants.scenarioList, state.selectedScenario, model::updateScenario)
}
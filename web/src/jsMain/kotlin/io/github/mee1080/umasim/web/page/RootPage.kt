package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.vm.ViewModel

@Composable
fun RootPage(model: ViewModel, state: State) {
    ScenarioSelect(model, state)
    CharaSelect(model, state)
    SupportSelect(model, state)
    TrainingInfo(model, state)
    SupportInfo(model, state)
    when (state.scenario) {
        Scenario.URA -> UraSimulation(model, state)
        Scenario.AOHARU -> AoharuSimulation(model.aoharuSimulationViewModel, state.aoharuSimulationState)
    }
    LicenseInfo()
}
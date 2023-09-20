/*
 * Copyright 2021 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.mee1080.umasim.web.page.top

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.web.components.HideBlock
import io.github.mee1080.umasim.web.page.top.result.*
import io.github.mee1080.umasim.web.page.top.setting.CharaSelect
import io.github.mee1080.umasim.web.page.top.setting.ScenarioSelect
import io.github.mee1080.umasim.web.page.top.setting.SupportSelect
import io.github.mee1080.umasim.web.page.top.setting.TrainingSetting
import io.github.mee1080.umasim.web.page.top.simulation.UraSimulation
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

@Composable
fun RootPage(model: ViewModel, state: State) {
    ScenarioSelect(model, state)
    CharaSelect(model, state)
    SupportSelect(model, state)
    HideBlock({ H2 { Text("トレーニング設定") } }, true) { TrainingSetting(model, state) }
    HideBlock({ H2 { Text("トレーニング上昇量") } }, true) { TrainingInfo(model, state) }
    HideBlock({ H2 { Text("トレーニング期待値") } }, true) { ExpectedStatusDisplay(model, state) }
    HideBlock({ H2 { Text("レースボーナス計算") } }) { RaceBonus(model, state) }
    HideBlock({ H2 { Text("編成情報") } }) { SupportInfo(state) }
    HideBlock({ H2 { Text("サポートカード情報") } }) { SupportCardInfo(state) }
    if (state.scenario == Scenario.URA) {
        HideBlock({ H2 { Text("シミュレーション") } }) {
            UraSimulation(model, state)
//        when (state.scenario) {
//            Scenario.URA -> UraSimulation(model, state)
//            Scenario.AOHARU -> AoharuSimulation(model.aoharuSimulationViewModel, state.aoharuSimulationState)
//        }
        }
    }
}
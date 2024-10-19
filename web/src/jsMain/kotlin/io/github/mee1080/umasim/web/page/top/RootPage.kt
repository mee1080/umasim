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
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.web.components.atoms.*
import io.github.mee1080.umasim.web.page.top.result.*
import io.github.mee1080.umasim.web.page.top.setting.CharaSelect
import io.github.mee1080.umasim.web.page.top.setting.ScenarioSelect
import io.github.mee1080.umasim.web.page.top.setting.SupportSelect
import io.github.mee1080.umasim.web.page.top.setting.TrainingSetting
import io.github.mee1080.umasim.web.page.top.simulation.UraSimulation
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun RootPage(model: ViewModel, state: State) {
    Div({
        style {
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.SpaceBetween)
            alignItems(AlignItems.Center)
        }
    }) {
        Div({
            style { typeScale(MdSysTypeScale.headlineMedium) }
        }) {
            Text("トレーニング計算機")
        }
        MdCheckbox("上下分割", state.divideMode) {
            onChange { model.update { copy(divideMode = it) } }
        }
    }
    if (state.divideMode) {
        Div({
            style {
                display(DisplayStyle.Flex)
                height(0.px)
                flexGrow(1)
                flexDirection(FlexDirection.Column)
                justifyContent(JustifyContent.Stretch)
            }
        }) {
            repeat(2) {
                MdDivider(1.px)
                Div({
                    style {
                        height(0.px)
                        flexGrow(1)
                        overflowY("scroll")
                    }
                }) {
                    RootPageContent(model, state)
                }
            }
        }
    } else {
        RootPageContent(model, state)
    }
}

@Composable
private fun RootPageContent(model: ViewModel, state: State) {
    ScenarioSelect(model, state)
    CharaSelect(model, state)
    SupportSelect(model, state)
    TrainingSetting(model, state)
    TrainingInfo(model, state)
    if (state.scenario != Scenario.UAF) {
        ExpectedStatusDisplay(model, state)
    }
    RaceBonus(model, state)
    SupportInfo(model, state)
    SupportCardInfo(state)
    if (state.scenario == Scenario.URA) {
        UraSimulation(model, state)
    }
}
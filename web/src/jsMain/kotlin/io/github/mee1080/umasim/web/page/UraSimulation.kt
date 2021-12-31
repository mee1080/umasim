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
package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.web.components.LabeledSelect
import io.github.mee1080.umasim.web.onClickOrTouch
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.colspan
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.*

@Composable
fun UraSimulation(model: ViewModel, state: State) {
    Div {
        LabeledSelect(
            "モード",
            WebConstants.displaySimulationModeList[Scenario.URA]!!,
            state.simulationMode,
            model::updateSimulationMode
        )
        Div({ style { padding(8.px) } }) {
            Text("ターン数")
            NumberInput {
                value(state.simulationTurn.toString())
                onInput { model.updateSimulationTurn(it.value?.toInt() ?: 0) }
            }
        }
        Div({ style { padding(8.px) } }) {
            Button({ onClickOrTouch { model.doUraSimulation() } }) { Text("シミュレーション実行（β版）") }
        }
    }
    Div {
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
                Th { Text("スキルPt") }
            }
            Tr {
                Td { Text(state.simulationResult.speed.toString()) }
                Td { Text(state.simulationResult.stamina.toString()) }
                Td { Text(state.simulationResult.power.toString()) }
                Td { Text(state.simulationResult.guts.toString()) }
                Td { Text(state.simulationResult.wisdom.toString()) }
                Td { Text(state.simulationResult.skillPt.toString()) }
            }
            Tr {
                Th { Text("ヒント") }
                Td({
                    style { textAlign("left") }
                    colspan(5)
                }) {
                    Text(state.simulationResult.skillHint.map { "${it.key}:${it.value}" }.joinToString("\n"))
                }
            }
            Tr {
                Th { Text("行動履歴") }
                Td({
                    style { textAlign("left") }
                    colspan(5)
                }) {
                    state.simulationHistory.forEachIndexed { index, item ->
                        Div { Text("${index + 1}: $item") }
                    }
                }
            }
        }
    }
}
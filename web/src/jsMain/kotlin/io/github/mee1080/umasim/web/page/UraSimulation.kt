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
import io.github.mee1080.umasim.data.motivationToString
import io.github.mee1080.umasim.web.components.LabeledSelect
import io.github.mee1080.umasim.web.onClickOrTouch
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.unsetWidth
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.*

@Composable
fun UraSimulation(model: ViewModel, state: State) {
    Div {
        LabeledSelect(
            "モード",
            WebConstants.displaySimulationModeList[state.scenario]!!,
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
        H3 { Text("ステータス") }
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
        }
        H3 { Text("ヒント") }
        Div {
            Text(state.simulationResult.skillHint.map { "${it.key}:${it.value}" }.joinToString("\n"))
        }
        H3 { Text("履歴") }
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th { Text("ターン") }
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
                Th { Text("スキルPt") }
                Th { Text("体力") }
                Th { Text("やる気") }
                Th { Text("ファン数") }
                Th({ unsetWidth() }) { Text("行動") }
            }
            state.simulationHistory.forEachIndexed { index, (action, status) ->
                Tr {
                    Td { Text("${index + 1}") }
                    Td { Text(status.speed.toString()) }
                    Td { Text(status.stamina.toString()) }
                    Td { Text(status.power.toString()) }
                    Td { Text(status.guts.toString()) }
                    Td { Text(status.wisdom.toString()) }
                    Td { Text(status.skillPt.toString()) }
                    Td { Text(status.hp.toString()) }
                    Td { Text(motivationToString(status.motivation)) }
                    Td { Text(status.fanCount.toString()) }
                    Td({
                        unsetWidth()
                        style {
                            textAlign("left")
                        }
                    }) { Text(action) }
                }
            }
        }
    }
}
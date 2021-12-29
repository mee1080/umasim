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
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.web.components.LabeledSelect
import io.github.mee1080.umasim.web.components.StatusHeaders
import io.github.mee1080.umasim.web.onClickOrTouch
import io.github.mee1080.umasim.web.round
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.unsetWidth
import io.github.mee1080.umasim.web.vm.AoharuSimulationViewModel
import org.jetbrains.compose.web.attributes.rowspan
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.*

@Composable
fun AoharuSimulation(model: AoharuSimulationViewModel) {
    val state = model.root.state.aoharuSimulationState
    H2 { Text("アオハルシミュレーション") }
    Div {
        LabeledSelect(
            "モード",
            WebConstants.displaySimulationModeList[Scenario.AOHARU]!!,
            state.simulationMode,
            model::updateSimulationMode
        )
        Div({ style { padding(8.px) } }) {
            Button({ onClickOrTouch { model.doSimulation() } }) { Text("シミュレーション実行（β版）") }
        }
        if (state.simulationHistory.isNotEmpty()) {
            Table({ classes(AppStyle.table) }) {
                Tr {
                    Th({ unsetWidth() }) { Text("ターン") }
                    Th({ unsetWidth() }) { Text("項目") }
                    StatusHeaders()
                }
                state.simulationHistory.forEach {
                    Tr {
                        Td({
                            unsetWidth()
                            rowspan(4)
                            style { textAlign("left") }
                        }) { Text(it.action) }
                        Td({
                            unsetWidth()
                            style { textAlign("left") }
                        }) { Text("育成ウマ娘") }
                        Td { Text(it.charaStatus.speed.toString()) }
                        Td { Text(it.charaStatus.stamina.toString()) }
                        Td { Text(it.charaStatus.power.toString()) }
                        Td { Text(it.charaStatus.guts.toString()) }
                        Td { Text(it.charaStatus.wisdom.toString()) }
                    }
                    Tr {
                        Td({
                            unsetWidth()
                            style { textAlign("left") }
                        }) { Text("チーム平均") }
                        Td { Text(it.teamAverageStatus.speed.round().toString()) }
                        Td { Text(it.teamAverageStatus.stamina.round().toString()) }
                        Td { Text(it.teamAverageStatus.power.round().toString()) }
                        Td { Text(it.teamAverageStatus.guts.round().toString()) }
                        Td { Text(it.teamAverageStatus.wisdom.round().toString()) }
                    }
                    Tr {
                        Td({
                            unsetWidth()
                            style { textAlign("left") }
                        }) { Text("チームランク") }
                        Td({ style { textAlign("center") } }) { Text(it.teamStatusRank[StatusType.SPEED]!!.rank) }
                        Td({ style { textAlign("center") } }) { Text(it.teamStatusRank[StatusType.STAMINA]!!.rank) }
                        Td({ style { textAlign("center") } }) { Text(it.teamStatusRank[StatusType.POWER]!!.rank) }
                        Td({ style { textAlign("center") } }) { Text(it.teamStatusRank[StatusType.GUTS]!!.rank) }
                        Td({ style { textAlign("center") } }) { Text(it.teamStatusRank[StatusType.WISDOM]!!.rank) }
                    }
                    Tr {
                        Td({
                            unsetWidth()
                            style { textAlign("left") }
                        }) { Text("次のランクまで") }
                        trainingType.forEach { type ->
                            Td { Text(it.next[type]!!.round().toString()) }
                        }
                    }
                }
            }
            Div { Text("※アオハル特訓、アオハル魂爆発を、一切行わなかった場合のシミュレーションです") }
        }
    }
}
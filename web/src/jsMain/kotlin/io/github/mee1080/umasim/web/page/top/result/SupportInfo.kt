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
package io.github.mee1080.umasim.web.page.top.result

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.unsetWidth
import org.jetbrains.compose.web.dom.*
import kotlin.math.roundToInt

@Composable
fun SupportInfo(state: State) {
    HideBlock("編成情報") {
        H3 { Text("レースボーナス合計：${state.totalRaceBonus}") }
        H3 { Text("ファンボーナス合計：${state.totalFanBonus}") }
        H3 { Text("初期ステータスアップ") }
        Div {
            Table({ classes(AppStyle.table) }) {
                Tr {
                    Th { Text("スピード") }
                    Th { Text("スタミナ") }
                    Th { Text("パワー") }
                    Th { Text("根性") }
                    Th { Text("賢さ") }
                }
                Tr {
                    Td { Text(state.initialStatus.speed.toString()) }
                    Td { Text(state.initialStatus.stamina.toString()) }
                    Td { Text(state.initialStatus.power.toString()) }
                    Td { Text(state.initialStatus.guts.toString()) }
                    Td { Text(state.initialStatus.wisdom.toString()) }
                }
            }
        }
        H3 { Text("得意率・絆・ヒント率") }
        Div {
            Table({ classes(AppStyle.table) }) {
                Tr {
                    Th({
                        style {
                            property("border", "none")
                        }
                        unsetWidth()
                    }) { }
                    Th { Text("得意練習配置率") }
                    Th { Text("初期絆") }
                    Th { Text("必要絆上げ回数") }
                    Th { Text("ヒント発生率") }
                }
                state.supportSelectionList.filter { it.isSelected }.forEach {
                    Tr {
                        Td({
                            unsetWidth()
                        }) { Text(it.name) }
                        Td { Text("${(it.specialtyRate * 1000).roundToInt() / 10.0}%") }
                        Td { Text(it.initialRelation.toString()) }
                        Td { Text(it.relationUpCount.toString()) }
                        Td { Text("${(it.hintRate * 1000).roundToInt() / 10.0}%") }
                    }
                }
            }
            Div { Text("※得意練習配置率とヒント発生率は推定値、必要絆上げ回数はイベントとヒント除く") }
        }
        H3 { Text("獲得可能スキルヒント（イベント除く）") }
        Div {
            state.availableHint.forEach {
                Div { Text("${it.key} ： ${it.value.joinToString(", ")}") }
            }
        }
    }
}
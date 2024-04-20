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
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.web.components.atoms.MdCheckbox
import io.github.mee1080.umasim.web.components.atoms.MdFilledButton
import io.github.mee1080.umasim.web.components.atoms.disabled
import io.github.mee1080.umasim.web.components.atoms.onChange
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.unsetWidth
import io.github.mee1080.umasim.web.vm.ViewModel
import io.github.mee1080.utility.roundToString
import io.github.mee1080.utility.toPercentString
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import kotlin.math.roundToInt

@Composable
fun SupportInfo(model: ViewModel, state: State) {
    HideBlock("編成情報", initialOpen = true) {
        if (state.scenario == Scenario.UAF) {
            H3 { Text("競技レベルアップ期待値") }
            DivFlexCenter({ style { marginBottom(8.px) } }) {
                MdFilledButton("計算") {
                    if (state.uafState.athleticsLevelUpCalculating) disabled()
                    onClick { model.calcUafAthleticsLevel() }
                }
                MdCheckbox("お休み/お出かけ/レース後", state.uafState.athleticsLevelUpBonus) {
                    style { marginLeft(8.px) }
                    onChange { model.updateUaf { copy(athleticsLevelUpBonus = it) } }
                }
            }
            if (state.uafState.athleticsLevelUpRate.isNotEmpty()) {
                Div {
                    Text("期待値：${state.uafState.expectedAthleticsLevelUp.roundToString(3)}")
                }
                Table({ classes(AppStyle.table) }) {
                    Tr {
                        Th { Text("上昇値") }
                        Th { Text("確率") }
                        Th({
                            style {
                                width(200.px)
                                property("border", "unset")
                            }
                        })
                    }
                    state.uafState.athleticsLevelUpRate.forEach { (level, rate) ->
                        Tr {
                            Td { Text(level.toString()) }
                            Td { Text(rate.toPercentString(4)) }
                            Td({ style { property("border", "unset") } }) {
                                Div({
                                    style {
                                        width((rate * 1000).px)
                                        height(16.px)
                                    }
                                    classes("tertiary")
                                })
                            }
                        }
                    }
                }
            }
            Ul {
                Li { Text("競技レベルが最も大きく上がるトレーニングを押す場合の期待値です") }
                Li { Text("計算時間がかかるため、計算ボタンを押すまで更新されません") }
                Li { Text("友人/グループは、1箇所に1人のみの制限が入っていないため、複数編成の場合正確ではありません") }
                Li { Text("つるぎとメイの固有には未対応です") }
            }
        }
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
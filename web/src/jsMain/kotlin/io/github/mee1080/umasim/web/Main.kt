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
package io.github.mee1080.umasim.web

import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.web.components.GroupedSelect
import io.github.mee1080.umasim.web.components.LabeledCheckbox
import io.github.mee1080.umasim.web.components.LabeledRadioGroup
import io.github.mee1080.umasim.web.components.LabeledSelect
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.colspan
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLInputElement

fun main() {
    StoreLoader.load()
    val model = ViewModel()

    renderComposable(rootElementId = "root") {
        Style(AppStyle)
        H2 { Text("育成キャラ") }
        LabeledSelect("", model.displayCharaList, model.selectedChara, model::updateChara)
        H2 { Text("サポートカード") }
        Div {
            (0..1).forEach { row ->
                Div({ style { display(DisplayStyle.Flex) } }) {
                    model.supportSelectionList.slice((row * 3)..(row * 3 + 2)).forEachIndexed { offset, item ->
                        val index = row * 3 + offset
                        Div {
                            GroupedSelect("", model.displaySupportList, item.selectedSupport, item::updateSupport)
                            LabeledRadioGroup(
                                "talent$index",
                                "上限解放：",
                                model.supportTalentList,
                                item.supportTalent,
                                item::updateSupportTalent
                            )
                            Div {
                                LabeledCheckbox("join$index", "練習参加", item.join, item::updateJoin)
                                LabeledCheckbox("friend$index", "友情", item.friend, item::updateFriend)
                            }
                        }
                    }
                }
            }
        }
        H2 { Text("トレーニング上昇量") }
        LabeledRadioGroup("motivation", "やる気：", model.motivationList, model.motivation, model::updateMotivation)
        LabeledRadioGroup(
            "training",
            "種別　：",
            model.displayTrainingTypeList,
            model.selectedTrainingType,
            model::updateTrainingType
        )
        LabeledRadioGroup("level", "レベル：", model.trainingLevelList, model.trainingLevel, model::updateTrainingLevel)
        Div {
            Table({ classes(AppStyle.table) }) {
                Tr {
                    Th({ classes(AppStyle.tableHeader) }) { Text("スピード") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("スタミナ") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("パワー") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("根性") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("賢さ") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("スキルPt") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("体力") }
                }
                Tr {
                    Td({ classes(AppStyle.tableValue) }) { Text(model.trainingResult.speed.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.trainingResult.stamina.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.trainingResult.power.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.trainingResult.guts.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.trainingResult.wisdom.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.trainingResult.skillPt.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.trainingResult.hp.toString()) }
                }
            }
        }
        H2 { Text("編成情報") }
        Div { Text("レースボーナス合計：${model.totalRaceBonus}") }
        Div { Text("ファンボーナス合計：${model.totalFanBonus}") }
        H2 { Text("シミュレーション") }
        Div {
            LabeledSelect("モード", model.displaySimulationModeList, model.simulationMode, model::updateSimulationMode)
            Div({ style { padding(8.px) } }) {
                Text("ターン数")
                Input(InputType.Number, model.simulationTurn.toString()) {
                    onChange { model.updateSimulationTurn(it.target<HTMLInputElement>().value.toInt()) }
                }
            }
            Div({ style { padding(8.px) } }) {
                Button({ onClick { model.doSimulation() } }) { Text("シミュレーション実行（β版）") }
            }
        }
        Div {
            Table({ classes(AppStyle.table) }) {
                Tr {
                    Th({ classes(AppStyle.tableHeader) }) { Text("スピード") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("スタミナ") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("パワー") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("根性") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("賢さ") }
                    Th({ classes(AppStyle.tableHeader) }) { Text("スキルPt") }
                }
                Tr {
                    Td({ classes(AppStyle.tableValue) }) { Text(model.simulationResult.speed.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.simulationResult.stamina.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.simulationResult.power.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.simulationResult.guts.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.simulationResult.wisdom.toString()) }
                    Td({ classes(AppStyle.tableValue) }) { Text(model.simulationResult.skillPt.toString()) }
                }
                Tr {
                    Th({ classes(AppStyle.tableHeader) }) { Text("ヒント") }
                    Td({
                        classes(AppStyle.tableValue)
                        style { property("text-align", "left") }
                        colspan(5)
                    }) {
                        Text(model.simulationResult.skillHint.map { "${it.key}:${it.value}" }.joinToString("\n"))
                    }
                }
                Tr {
                    Th({ classes(AppStyle.tableHeader) }) { Text("行動履歴") }
                    Td({
                        classes(AppStyle.tableValue)
                        style { property("text-align", "left") }
                        colspan(5)
                    }) {
                        model.simulationHistory.forEachIndexed { index, item ->
                            Div { Text("${index+1}: $item") }
                        }
                    }
                }
            }
        }
    }
}
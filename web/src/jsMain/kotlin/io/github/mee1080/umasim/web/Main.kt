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

import io.github.mee1080.umasim.data.StatusType
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
import kotlin.math.roundToInt

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
                        Div({
                            when (item.card?.type) {
                                StatusType.SPEED -> Color.RGB(69, 196, 255)
                                StatusType.STAMINA -> Color.RGB(255, 144, 127)
                                StatusType.POWER -> Color.RGB(255, 185, 21)
                                StatusType.GUTS -> Color.RGB(255, 144, 186)
                                StatusType.WISDOM -> Color.RGB(32, 216, 169)
                                StatusType.FRIEND -> Color.RGB(255, 211, 108)
                                else -> null
                            }?.let {
                                style {
                                    property("background", "linear-gradient(170deg, #ffffff00, #ffffff00 70%, $it)")
                                }
                            }
                        }) {
                            GroupedSelect(
                                "",
                                model.displaySupportList,
                                item.selectedSupport,
                                {
                                    classes(AppStyle.supportCard)
                                    console.log("${item.friend} ${item.card?.type} ${model.selectedTrainingType}")
                                    if (item.friendTraining) {
                                        console.log("friend")
                                        classes(AppStyle.friendSupportCard)
                                    }
                                },
                                item::updateSupport
                            ) {
                                Div({ classes("after") })
                            }
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
                    Th { Text("スピード") }
                    Th { Text("スタミナ") }
                    Th { Text("パワー") }
                    Th { Text("根性") }
                    Th { Text("賢さ") }
                    Th { Text("スキルPt") }
                    Th { Text("体力") }
                }
                Tr {
                    Td { Text(model.trainingResult.speed.toString()) }
                    Td { Text(model.trainingResult.stamina.toString()) }
                    Td { Text(model.trainingResult.power.toString()) }
                    Td { Text(model.trainingResult.guts.toString()) }
                    Td { Text(model.trainingResult.wisdom.toString()) }
                    Td { Text(model.trainingResult.skillPt.toString()) }
                    Td { Text(model.trainingResult.hp.toString()) }
                }
            }
        }
        H2 { Text("編成情報") }
        H3 { Text("レースボーナス合計：${model.totalRaceBonus}") }
        H3 { Text("ファンボーナス合計：${model.totalFanBonus}") }
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
                    Td { Text(model.initialStatus.speed.toString()) }
                    Td { Text(model.initialStatus.stamina.toString()) }
                    Td { Text(model.initialStatus.power.toString()) }
                    Td { Text(model.initialStatus.guts.toString()) }
                    Td { Text(model.initialStatus.wisdom.toString()) }
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
                            property("width", "unset")
                        }
                    }) { }
                    Th { Text("得意練習配置率") }
                    Th { Text("初期絆") }
                    Th { Text("必要絆上げ回数") }
                    Th { Text("ヒント発生率") }
                }
                model.supportSelectionList.filter { it.isSelected }.forEach {
                    Tr {
                        Td({
                            style {
                                property("width", "unset")
                            }
                        }) { Text(it.name) }
                        Td { Text("${(it.specialtyRate * 1000).roundToInt() / 10.0}%") }
                        Td { Text(it.initialRelation.toString()) }
                        Td { Text(it.relationUpCount.toString()) }
                        Td { Text("${(it.hintRate * 1000).roundToInt() / 10.0}%") }
                    }
                }
            }
            Div { Small { Text("得意練習配置率とヒント発生率は推定値、必要絆上げ回数はイベントとヒント除く") } }
        }
        H3 { Text("獲得可能スキルヒント（イベント除く）") }
        Div {
            model.availableHint.forEach {
                Div { Text("${it.key} ： ${it.value.joinToString(", ")}") }
            }
        }
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
                    Th { Text("スピード") }
                    Th { Text("スタミナ") }
                    Th { Text("パワー") }
                    Th { Text("根性") }
                    Th { Text("賢さ") }
                    Th { Text("スキルPt") }
                }
                Tr {
                    Td { Text(model.simulationResult.speed.toString()) }
                    Td { Text(model.simulationResult.stamina.toString()) }
                    Td { Text(model.simulationResult.power.toString()) }
                    Td { Text(model.simulationResult.guts.toString()) }
                    Td { Text(model.simulationResult.wisdom.toString()) }
                    Td { Text(model.simulationResult.skillPt.toString()) }
                }
                Tr {
                    Th { Text("ヒント") }
                    Td({
                        style { property("text-align", "left") }
                        colspan(5)
                    }) {
                        Text(model.simulationResult.skillHint.map { "${it.key}:${it.value}" }.joinToString("\n"))
                    }
                }
                Tr {
                    Th { Text("行動履歴") }
                    Td({
                        style { property("text-align", "left") }
                        colspan(5)
                    }) {
                        model.simulationHistory.forEachIndexed { index, item ->
                            Div { Text("${index + 1}: $item") }
                        }
                    }
                }
            }
        }
    }
}
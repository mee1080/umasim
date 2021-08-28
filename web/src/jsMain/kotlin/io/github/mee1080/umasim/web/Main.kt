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
import io.github.mee1080.umasim.web.components.*
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlin.math.roundToInt

fun main() {
    StoreLoader.load()
    val model = ViewModel()

    renderComposable(rootElementId = "root") {
        Style(AppStyle)
        H2 { Text("育成キャラ") }
        LabeledSelect("", model.displayCharaList, model.selectedChara, model::updateChara)
        H2 { Text("サポートカード") }
        Div({ style { paddingBottom(16.px) } }) {
            TextInput {
//                value(model.supportFilter)
                placeholder("カード名、スキルヒントでフィルタ (空白区切りでAnd検索)")
                size(60)
                onInput { model.updateSupportFilter(it.value) }
            }
            Button({
                if (model.supportFilterApplied) {
                    disabled()
                }
                onClick { model.applyFilter() }
            }) { Text("フィルタ適用") }
//            Button({ onClick { model.clearFilter() } }) { Text("クリア") }
        }
        Div {
            (0..1).forEach { row ->
                Div({ classes(AppStyle.supportCardArea) }) {
                    model.supportSelectionList.slice((row * 3)..(row * 3 + 2)).forEachIndexed { offset, item ->
                        val index = row * 3 + offset
                        Div({
                            when (item.card?.type) {
                                StatusType.SPEED -> rgb(69, 196, 255)
                                StatusType.STAMINA -> rgb(255, 144, 127)
                                StatusType.POWER -> rgb(255, 185, 21)
                                StatusType.GUTS -> rgb(255, 144, 186)
                                StatusType.WISDOM -> rgb(32, 216, 169)
                                StatusType.FRIEND -> rgb(255, 211, 108)
                                else -> null
                            }?.let {
                                style {
                                    background("linear-gradient(170deg, #ffffff00, #ffffff00 70%, $it)")
                                }
                            }
                        }) {
                            GroupedSelect(
                                "",
                                item.supportList,
                                item.selectedSupport,
                                {
                                    classes(AppStyle.supportCard)
                                    if (item.friendTraining) {
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
        LabeledRadioGroup("scenario", "シナリオ：", model.scenarioList, model.selectedScenario, model::updateScenario)
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
                    Th { Text("5ステ合計") }
                }
                Tr {
                    Td { Text(model.trainingResult.speed.toString()) }
                    Td { Text(model.trainingResult.stamina.toString()) }
                    Td { Text(model.trainingResult.power.toString()) }
                    Td { Text(model.trainingResult.guts.toString()) }
                    Td { Text(model.trainingResult.wisdom.toString()) }
                    Td { Text(model.trainingResult.skillPt.toString()) }
                    Td { Text(model.trainingResult.hp.toString()) }
                    Td { Text(model.trainingResult.statusTotal.toString()) }
                }
            }
        }
        LabeledCheckbox("trainingParamTest", "トレーニング設定調査", model.trainingParamTest != null) {
            model.updateTrainingParamTest(it)
        }
        model.trainingParamTest?.let {
            TrainingParamTest(it)
        }
        if (model.trainingImpact.isNotEmpty()) {
            H3 { Text("サポカ影響度") }
            Div {
                Table({ classes(AppStyle.table) }) {
                    Tr {
                        Th({
                            style {
                                property("border", "none")
                                property("width", "unset")
                            }
                        }) { }
                        Th { Text("スピード") }
                        Th { Text("スタミナ") }
                        Th { Text("パワー") }
                        Th { Text("根性") }
                        Th { Text("賢さ") }
                        Th { Text("スキルPt") }
                        Th { Text("体力") }
                        Th { Text("5ステ合計") }
                    }
                    model.trainingImpact.forEach { (name, status) ->
                        Tr {
                            Td({
                                style {
                                    property("width", "unset")
                                }
                            }) { Text(name) }
                            Td { Text(status.speed.toString()) }
                            Td { Text(status.stamina.toString()) }
                            Td { Text(status.power.toString()) }
                            Td { Text(status.guts.toString()) }
                            Td { Text(status.wisdom.toString()) }
                            Td { Text(status.skillPt.toString()) }
                            Td { Text(status.hp.toString()) }
                            Td { Text(status.statusTotal.toString()) }
                        }
                    }
                }
            }
            Div { Text("※計算式： 上昇量 - 対象カードが練習不参加時の上昇量") }
        }
        H3 { Text("期待値（練習配置率を考慮）") }
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
                    Th { Text("5ステ合計") }
                }
                Tr {
                    Td { Text(((model.expectedResult.speed * 100).roundToInt() / 100.0).toString()) }
                    Td { Text(((model.expectedResult.stamina * 100).roundToInt() / 100.0).toString()) }
                    Td { Text(((model.expectedResult.power * 100).roundToInt() / 100.0).toString()) }
                    Td { Text(((model.expectedResult.guts * 100).roundToInt() / 100.0).toString()) }
                    Td { Text(((model.expectedResult.wisdom * 100).roundToInt() / 100.0).toString()) }
                    Td { Text(((model.expectedResult.skillPt * 100).roundToInt() / 100.0).toString()) }
                    Td { Text(((model.expectedResult.hp * 100).roundToInt() / 100.0).toString()) }
                    Td { Text(((model.expectedResult.statusTotal * 100).roundToInt() / 100.0).toString()) }
                }
            }
            Div { Text("※練習参加チェックボックスを無視して、練習配置率に応じて参加/不参加を決めた場合の期待値") }
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
            Div { Text("※得意練習配置率とヒント発生率は推定値、必要絆上げ回数はイベントとヒント除く") }
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
                NumberInput {
                    value(model.simulationTurn.toString())
                    onInput { model.updateSimulationTurn(it.value?.toInt() ?: 0) }
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
                        style { textAlign("left") }
                        colspan(5)
                    }) {
                        Text(model.simulationResult.skillHint.map { "${it.key}:${it.value}" }.joinToString("\n"))
                    }
                }
                Tr {
                    Th { Text("行動履歴") }
                    Td({
                        style { textAlign("left") }
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
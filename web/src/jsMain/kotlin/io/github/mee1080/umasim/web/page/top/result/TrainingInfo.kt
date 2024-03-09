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
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.UafGenre
import io.github.mee1080.umasim.web.components.atoms.*
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.components.parts.SliderEntry
import io.github.mee1080.umasim.web.round
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.state.WebConstants.trainingTypeList
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.unsetWidth
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import kotlin.math.roundToInt

@Composable
fun TrainingInfo(model: ViewModel, state: State) {
    HideBlock("トレーニング上昇量", true) {
        if (state.scenario == Scenario.UAF) {
            val uafState = state.uafState
            DivFlexCenter {
                Text("種別　：")
                MdRadioGroup(
                    trainingTypeList,
                    uafState.selectedTrainingType,
                    onSelect = { model.updateUaf { copy(selectedTrainingType = it) } },
                    itemToLabel = { it.displayName },
                )
            }
            DivFlexCenter {
                Text("ジャンル　：")
                MdRadioGroup(
                    UafGenre.entries,
                    uafState.trainingGenre,
                    onSelect = { model.updateUaf { copy(trainingGenre = it) } },
                    itemToLabel = { it.longDisplayName },
                )
            }
            DivFlexCenter({ style { fontWeight("bold") } }) {
                Text(uafState.trainingName)
            }
            DivFlexCenter {
                MdCheckbox(uafState.linkSpeed || uafState.selectedTrainingType == StatusType.SPEED) {
                    if (uafState.selectedTrainingType == StatusType.SPEED) disabled()
                    onChange { model.updateUaf { copy(linkSpeed = it) } }
                }
                Text("スピード：")
                SliderEntry("競技Lv", uafState.speedAthleticLevel, 1, 100) {
                    model.updateUaf { copy(speedAthleticLevel = it.toInt()) }
                }
                Div({ style { marginLeft(8.px) } }) { Text("上昇量") }
                MdOutlinedIntTextField(uafState.speedAthleticLevelUp) {
                    onInput { value -> value?.let { model.updateUaf { copy(speedAthleticLevelUp = it) } } }
                }
            }
            DivFlexCenter {
                MdCheckbox(uafState.linkStamina || uafState.selectedTrainingType == StatusType.STAMINA) {
                    if (uafState.selectedTrainingType == StatusType.STAMINA) disabled()
                    onChange { model.updateUaf { copy(linkStamina = it) } }
                }
                Text("スタミナ：")
                SliderEntry("競技Lv", uafState.staminaAthleticLevel, 1, 100) {
                    model.updateUaf { copy(staminaAthleticLevel = it.toInt()) }
                }
                Div({ style { marginLeft(8.px) } }) { Text("上昇量") }
                MdOutlinedIntTextField(uafState.staminaAthleticLevelUp) {
                    onInput { value -> value?.let { model.updateUaf { copy(staminaAthleticLevelUp = it) } } }
                }
            }
            DivFlexCenter {
                MdCheckbox(uafState.linkPower || uafState.selectedTrainingType == StatusType.POWER) {
                    if (uafState.selectedTrainingType == StatusType.POWER) disabled()
                    onChange { model.updateUaf { copy(linkPower = it) } }
                }
                Text("パワー：")
                SliderEntry("競技Lv", uafState.powerAthleticLevel, 1, 100) {
                    model.updateUaf { copy(powerAthleticLevel = it.toInt()) }
                }
                Div({ style { marginLeft(8.px) } }) { Text("上昇量") }
                MdOutlinedIntTextField(uafState.powerAthleticLevelUp) {
                    onInput { value -> value?.let { model.updateUaf { copy(powerAthleticLevelUp = it) } } }
                }
            }
            DivFlexCenter {
                MdCheckbox(uafState.linkGuts || uafState.selectedTrainingType == StatusType.GUTS) {
                    if (uafState.selectedTrainingType == StatusType.GUTS) disabled()
                    onChange { model.updateUaf { copy(linkGuts = it) } }
                }
                Text("根性：")
                SliderEntry("競技Lv", uafState.gutsAthleticLevel, 1, 100) {
                    model.updateUaf { copy(gutsAthleticLevel = it.toInt()) }
                }
                Div({ style { marginLeft(8.px) } }) { Text("上昇量") }
                MdOutlinedIntTextField(uafState.gutsAthleticLevelUp) {
                    onInput { value -> value?.let { model.updateUaf { copy(gutsAthleticLevelUp = it) } } }
                }
            }
            DivFlexCenter {
                MdCheckbox(uafState.linkWisdom || uafState.selectedTrainingType == StatusType.WISDOM) {
                    if (uafState.selectedTrainingType == StatusType.WISDOM) disabled()
                    onChange { model.updateUaf { copy(linkWisdom = it) } }
                }
                Text("賢さ：")
                SliderEntry("競技Lv", uafState.wisdomAthleticLevel, 1, 100) {
                    model.updateUaf { copy(wisdomAthleticLevel = it.toInt()) }
                }
                Div({ style { marginLeft(8.px) } }) { Text("上昇量") }
                MdOutlinedIntTextField(uafState.wisdomAthleticLevelUp) {
                    onInput { value -> value?.let { model.updateUaf { copy(wisdomAthleticLevelUp = it) } } }
                }
            }
        } else {
            DivFlexCenter {
                Text("種別　：")
                MdRadioGroup(
                    trainingTypeList,
                    state.selectedTrainingType,
                    onSelect = model::updateTrainingType,
                    itemToLabel = { it.displayName },
                )
            }
            DivFlexCenter {
                Text("レベル：")
                MdRadioGroup(
                    listOf(1, 2, 3, 4, 5),
                    state.trainingLevel,
                    onSelect = model::updateTrainingLevel,
                )
            }
        }
        DivFlexCenter {
            Text("やる気：")
            MdRadioGroup(
                listOf(2, 1, 0, -1, -2),
                state.motivation,
                onSelect = model::updateMotivation,
                itemToLabel = { WebConstants.motivationMap[it] ?: "" },
            )
        }
        DivFlexCenter {
            MdCheckbox("夏合宿", state.isLevelUpTurn) {
                onChange { model.update { copy(isLevelUpTurn = it) } }
            }
        }
        if (state.scenario == Scenario.URA) {
            DivFlexCenter {
                Text("ハッピーミーク：")
                MdCheckbox("参加", state.teamJoinCount >= 1) {
                    onChange { model.updateSpecialMember(it) }
                }
            }
        }
        if (state.scenario.guestMember) {
            SliderEntry("サポカ外参加人数：", state.teamJoinCount, 0, 5) {
                model.updateTeamJoinCount(it.toInt())
            }
        }
        if (state.scenario == Scenario.CLIMAX) {
            DivFlexCenter {
                Text("メガホン：")
                MdRadioGroup(
                    WebConstants.shopItemMegaphone,
                    state.shopItemMegaphone,
                    onSelect = model::updateShopItemMegaphone,
                    itemToLabel = { it.name }
                )
            }
            DivFlexCenter {
                Text("アンクルウェイト：")
                MdRadioGroup(
                    WebConstants.shopItemWeight,
                    state.shopItemWeight,
                    onSelect = model::updateShopItemWeight,
                    itemToLabel = { it.name }
                )
            }
        }
        Div({ style { marginTop(16.px) } }) {
            Table({ classes(AppStyle.table) }) {
                Tr {
                    if (state.scenario != Scenario.URA) {
                        Th({ style { property("border", "none") } }) { }
                    }
                    Th { Text("スピード") }
                    Th { Text("スタミナ") }
                    Th { Text("パワー") }
                    Th { Text("根性") }
                    Th { Text("賢さ") }
                    Th { Text("スキルPt") }
                    Th { Text("体力") }
                    Th { Text("5ステ合計") }
                    Th { Text("5ステ+SP") }
                    if (state.scenario == Scenario.GRAND_LIVE) {
                        Th({ style { width(120.px) } }) { Text("パフォーマンス") }
                    }
                }
                Tr {
                    if (state.scenario != Scenario.URA) {
                        Th { Text("基本") }
                    }
                    Td { Text(state.trainingResult.speed.toString()) }
                    Td { Text(state.trainingResult.stamina.toString()) }
                    Td { Text(state.trainingResult.power.toString()) }
                    Td { Text(state.trainingResult.guts.toString()) }
                    Td { Text(state.trainingResult.wisdom.toString()) }
                    Td { Text(state.trainingResult.skillPt.toString()) }
                    Td { Text(state.trainingResult.hp.toString()) }
                    Td { Text(state.trainingResult.statusTotal.toString()) }
                    Td { Text(state.trainingResult.totalPlusSkillPt.toString()) }
                    if (state.scenario == Scenario.GRAND_LIVE) {
                        Td { Text(state.trainingPerformanceValue.toString()) }
                    }
                }
                if (state.scenario != Scenario.URA) {
                    Tr {
                        val label = when (state.scenario) {
                            Scenario.CLIMAX -> "アイテム"
                            Scenario.AOHARU -> "アオハル"
                            else -> "ボーナス"
                        }
                        Th { Text(label) }
                        Td { Text(state.trainingItemBonus.speed.toString()) }
                        Td { Text(state.trainingItemBonus.stamina.toString()) }
                        Td { Text(state.trainingItemBonus.power.toString()) }
                        Td { Text(state.trainingItemBonus.guts.toString()) }
                        Td { Text(state.trainingItemBonus.wisdom.toString()) }
                        Td { Text(state.trainingItemBonus.skillPt.toString()) }
                        Td { Text(state.trainingItemBonus.hp.toString()) }
                        Td { Text(state.trainingItemBonus.statusTotal.toString()) }
                        Td { Text(state.trainingItemBonus.totalPlusSkillPt.toString()) }
                        if (state.scenario == Scenario.GRAND_LIVE) {
                            Td { Text(if (state.friendTraining) "×2" else "-") }
                        }
                    }
                    Tr {
                        Th { Text("合計") }
                        val totalStatus = state.trainingResult + state.trainingItemBonus
                        Td { Text(totalStatus.speed.toString()) }
                        Td { Text(totalStatus.stamina.toString()) }
                        Td { Text(totalStatus.power.toString()) }
                        Td { Text(totalStatus.guts.toString()) }
                        Td { Text(totalStatus.wisdom.toString()) }
                        Td { Text(totalStatus.skillPt.toString()) }
                        Td { Text(totalStatus.hp.toString()) }
                        Td { Text(totalStatus.statusTotal.toString()) }
                        Td { Text(totalStatus.totalPlusSkillPt.toString()) }
                        if (state.scenario == Scenario.GRAND_LIVE) {
                            Td { Text(((if (state.friendTraining) 2 else 1) * state.trainingPerformanceValue).toString()) }
                        }
                    }
                }
            }
        }
        Div({ style { marginTop(16.px) } }) {
            H3 { Text("切り捨て前") }
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
                    Td { Text(state.rawTrainingResult.speed.round(4).toString()) }
                    Td { Text(state.rawTrainingResult.stamina.round(4).toString()) }
                    Td { Text(state.rawTrainingResult.power.round(4).toString()) }
                    Td { Text(state.rawTrainingResult.guts.round(4).toString()) }
                    Td { Text(state.rawTrainingResult.wisdom.round(4).toString()) }
                    Td { Text(state.rawTrainingResult.skillPt.round(4).toString()) }
                }
            }
        }
        if (!state.scenario.guestMember) {
            H3 { Text("上振れ度: ${(state.upperRate * 10000.0).roundToInt() / 100.0}% (クライマックスでホイッスルを使って上昇量合計が今より低くなる確率)") }
        }
        if (state.trainingImpact.isNotEmpty()) {
            NestedHideBlock("サポカ影響度") {
                Div {
                    Table({ classes(AppStyle.table) }) {
                        Tr {
                            Th({
                                style {
                                    property("border", "none")
                                }
                                unsetWidth()
                            }) { }
                            Th { Text("スピード") }
                            Th { Text("スタミナ") }
                            Th { Text("パワー") }
                            Th { Text("根性") }
                            Th { Text("賢さ") }
                            Th { Text("スキルPt") }
                            Th { Text("体力") }
                            Th { Text("5ステ合計") }
                            Th { Text("5ステ+SP") }
                        }
                        state.trainingImpact.forEach { (name, status) ->
                            Tr {
                                Td({
                                    unsetWidth()
                                }) { Text(name) }
                                Td { Text(status.speed.toString()) }
                                Td { Text(status.stamina.toString()) }
                                Td { Text(status.power.toString()) }
                                Td { Text(status.guts.toString()) }
                                Td { Text(status.wisdom.toString()) }
                                Td { Text(status.skillPt.toString()) }
                                Td { Text(status.hp.toString()) }
                                Td { Text(status.statusTotal.toString()) }
                                Td { Text(status.totalPlusSkillPt.toString()) }
                            }
                        }
                    }
                }
                Div { Text("※計算式： 上昇量 - 対象カードが練習不参加時の上昇量") }
            }
        }
        H3 { Text("友情トレーニング発生率: ${(state.friendProbability * 10000.0).roundToInt() / 100.0}%") }
        NestedHideBlock("期待値（練習配置率を考慮）") {
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
                        Th { Text("5ステ+SP") }
                    }
                    Tr {
                        Td { Text(((state.expectedResult.speed * 100).roundToInt() / 100.0).toString()) }
                        Td { Text(((state.expectedResult.stamina * 100).roundToInt() / 100.0).toString()) }
                        Td { Text(((state.expectedResult.power * 100).roundToInt() / 100.0).toString()) }
                        Td { Text(((state.expectedResult.guts * 100).roundToInt() / 100.0).toString()) }
                        Td { Text(((state.expectedResult.wisdom * 100).roundToInt() / 100.0).toString()) }
                        Td { Text(((state.expectedResult.skillPt * 100).roundToInt() / 100.0).toString()) }
                        Td { Text(((state.expectedResult.hp * 100).roundToInt() / 100.0).toString()) }
                        Td { Text(((state.expectedResult.statusTotal * 100).roundToInt() / 100.0).toString()) }
                        Td { Text(((state.expectedResult.totalPlusSkillPt * 100).roundToInt() / 100.0).toString()) }
                    }
                }
                Div { Text("※練習参加チェックボックスを無視して、練習配置率に応じて参加/不参加を決めた場合の期待値") }
            }
        }
    }
}
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
import io.github.mee1080.umasim.web.components.LabeledRadioGroup
import io.github.mee1080.umasim.web.components.material.MwcSlider
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.unsetWidth
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import kotlin.math.roundToInt

@Composable
fun TrainingInfo(model: ViewModel, state: State) {

    LabeledRadioGroup("motivation", "やる気：", WebConstants.motivationList, state.motivation, model::updateMotivation)
    LabeledRadioGroup(
        "training",
        "種別　：",
        WebConstants.displayTrainingTypeList,
        state.selectedTrainingType,
        model::updateTrainingType
    )
    LabeledRadioGroup("level", "レベル：", WebConstants.trainingLevelList, state.trainingLevel, model::updateTrainingLevel)
    Div {
        Text("ファン数：")
        TextInput(state.fanCount) {
            size(10)
            onInput { model.updateFanCount(it.value) }
        }
    }
    Div({
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
        }
    }) {
        Span { Text("体力：") }
        MwcSlider(state.hp, 0, 120) {
            onInput { model.updateHp(it.toInt()) }
            style { width(300.px) }
        }
        Span { Text(state.hp.toString()) }
    }
    Div({
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
        }
    }) {
        Span { Text("体力最大値：") }
        MwcSlider(state.maxHp, 100, 120) {
            onInput { model.updateMaxHp(it.toInt()) }
            style { width(300.px) }
        }
        Span { Text(state.maxHp.toString()) }
    }
    Div({
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
        }
    }) {
        Span { Text("絆合計：") }
        MwcSlider(state.totalRelation, 0, 600) {
            onInput { model.updateTotalRelation(it.toInt()) }
            style { width(300.px) }
        }
        Span { Text(state.totalRelation.toString()) }
    }
    if (state.scenario == Scenario.CLIMAX) {
        LabeledRadioGroup(
            "shopItemMegaphone",
            "メガホン：",
            WebConstants.shopItemMegaphoneNames,
            state.shopItemMegaphone,
            model::updateShopItemMegaphone
        )
        LabeledRadioGroup(
            "shopItemWeight",
            "アンクルウェイト：",
            WebConstants.shopItemWeightNames,
            state.shopItemWeight,
            model::updateShopItemWeight
        )
    }
    if (state.scenario == Scenario.GRAND_LIVE) {
        Div {
            Span { Text("トレーニング上昇量：") }
            Text("スピード")
            TextInput(state.trainingLiveState.speed) {
                size(10)
                onInput { model.updateLiveSpeed(it.value) }
            }
            Text("スタミナ")
            TextInput(state.trainingLiveState.stamina) {
                size(10)
                onInput { model.updateLiveStamina(it.value) }
            }
            Text("パワー")
            TextInput(state.trainingLiveState.power) {
                size(10)
                onInput { model.updateLivePower(it.value) }
            }
            Text("根性")
            TextInput(state.trainingLiveState.guts) {
                size(10)
                onInput { model.updateLiveGuts(it.value) }
            }
            Text("賢さ")
            TextInput(state.trainingLiveState.wisdom) {
                size(10)
                onInput { model.updateLiveWisdom(it.value) }
            }
        }
        Div {
            Span { Text("友情トレーニング獲得量アップ：") }
            TextInput(state.trainingLiveState.friendTrainingUpInput) {
                size(10)
                onInput { model.updateLiveFriend(it.value) }
            }
        }
        Div {
            Span { Text("得意率アップ：") }
            TextInput(state.trainingLiveState.specialityRateUpInput) {
                size(10)
                onInput { model.updateLiveSpecialityRate(it.value) }
            }
        }
    }
    Div {
        Table({ classes(AppStyle.table) }) {
            Tr {
                if (state.scenario == Scenario.CLIMAX) {
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
            }
            Tr {
                if (state.scenario == Scenario.CLIMAX) {
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
            }
            if (state.scenario == Scenario.CLIMAX) {
                Tr {
                    Th { Text("アイテム") }
                    Td { Text(state.trainingItemBonus.speed.toString()) }
                    Td { Text(state.trainingItemBonus.stamina.toString()) }
                    Td { Text(state.trainingItemBonus.power.toString()) }
                    Td { Text(state.trainingItemBonus.guts.toString()) }
                    Td { Text(state.trainingItemBonus.wisdom.toString()) }
                    Td { Text(state.trainingItemBonus.skillPt.toString()) }
                    Td { Text(state.trainingItemBonus.hp.toString()) }
                    Td { Text(state.trainingItemBonus.statusTotal.toString()) }
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
                }
            }
        }
    }
    H3 { Text("上振れ度: ${(state.upperRate * 10000.0).roundToInt() / 100.0}% (クライマックスでホイッスルを使って上昇量合計が今より低くなる確率)") }
//    H3 { Text("Status / Coin: ${(state.coinRate * 10000.0).roundToInt() / 10000.0}") }
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
                Td { Text(((state.expectedResult.speed * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.stamina * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.power * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.guts * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.wisdom * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.skillPt * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.hp * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.statusTotal * 100).roundToInt() / 100.0).toString()) }
            }
        }
        Div { Text("※練習参加チェックボックスを無視して、練習配置率に応じて参加/不参加を決めた場合の期待値") }
    }
    if (state.trainingImpact.isNotEmpty()) {
        H3 { Text("サポカ影響度") }
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
                    }
                }
            }
        }
        Div { Text("※計算式： 上昇量 - 対象カードが練習不参加時の上昇量") }
    }
}
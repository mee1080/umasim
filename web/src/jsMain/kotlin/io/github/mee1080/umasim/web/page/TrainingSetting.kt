/*
 * Copyright 2022 mee1080
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
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput

@Composable
fun TrainingSetting(model: ViewModel, state: State) {
    LabeledRadioGroup(
        "motivation",
        "やる気：",
        WebConstants.motivationList,
        state.motivation,
        model::updateMotivation,
    )
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
    Div({
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
        }
    }) {
        Span { Text("速度スキル数：") }
        MwcSlider(state.speedSkillCount, 0, 5) {
            onInput { model.updateSpeedSkillCount(it.toInt()) }
            style { width(300.px) }
        }
        Span { Text(state.speedSkillCount.toString()) }
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
            Text("スキルPt")
            TextInput(state.trainingLiveState.skillPt) {
                size(10)
                onInput { model.updateLiveSkillPt(it.value) }
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
            Span { Text("※サポカの得意率に加算で実装") }
        }
    }
}
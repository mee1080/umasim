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
package io.github.mee1080.umasim.web.page.top.result

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.web.components.LabeledRadioGroup
import io.github.mee1080.umasim.web.components.atoms.MdFilledButton
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.round
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun ExpectedStatusDisplay(model: ViewModel, state: State) {
    HideBlock("トレーニング期待値") {
        val expectedState = state.expectedState
        val status = expectedState.status
        if (state.scenario == Scenario.AOHARU || state.scenario == Scenario.GRAND_LIVE) {
            Div {
                H3 { Text("サポカ外加入人数") }
                Div {
                    Small { Text("数値を増やすと急激に計算時間が増えます。一気に増やさず少しずつ増やすのをお勧めします。") }
                }
                TextInput(expectedState.teamJoinCount) {
                    size(10)
                    onInput { model.updateExpectedState { copy(teamJoinCount = it.value) } }
                }
            }
        }
        Div {
            H3 { Text("トレーニングレベル") }
            Div {
                Small { Text("「なし」を選ぶとそのトレーニングは行いません。その分計算時間が減ります。") }
            }
            LabeledRadioGroup(
                "speed", "スピード: ",
                listOf(0 to "なし") + (1..5).map { it to it.toString() },
                expectedState.levelSpeed
            ) {
                model.updateExpectedState { copy(levelSpeed = it) }
            }
            LabeledRadioGroup(
                "stamina", "スタミナ: ",
                listOf(0 to "なし") + (1..5).map { it to it.toString() },
                expectedState.levelStamina
            ) {
                model.updateExpectedState { copy(levelStamina = it) }
            }
            LabeledRadioGroup(
                "power", "パワー: ",
                listOf(0 to "なし") + (1..5).map { it to it.toString() },
                expectedState.levelPower
            ) {
                model.updateExpectedState { copy(levelPower = it) }
            }
            LabeledRadioGroup(
                "guts", "根性: ",
                listOf(0 to "なし") + (1..5).map { it to it.toString() },
                expectedState.levelGuts
            ) {
                model.updateExpectedState { copy(levelGuts = it) }
            }
            LabeledRadioGroup(
                "wisdom", "賢さ: ",
                listOf(0 to "なし") + (1..5).map { it to it.toString() },
                expectedState.levelWisdom
            ) {
                model.updateExpectedState { copy(levelWisdom = it) }
            }
        }
        Div {
            H3 { Text("評価値係数") }
            Div {
                Small { Text("上昇量に評価値係数を掛けた合計が一番高いトレーニングを選択します。") }
            }
            Div {
                Text("スピード")
                TextInput(expectedState.evaluateSpeed) {
                    size(10)
                    onInput { model.updateExpectedState { copy(evaluateSpeed = it.value) } }
                }
                Text("スタミナ")
                TextInput(expectedState.evaluateStamina) {
                    size(10)
                    onInput { model.updateExpectedState { copy(evaluateStamina = it.value) } }
                }
                Text("パワー")
                TextInput(expectedState.evaluatePower) {
                    size(10)
                    onInput { model.updateExpectedState { copy(evaluatePower = it.value) } }
                }
                Text("根性")
                TextInput(expectedState.evaluateGuts) {
                    size(10)
                    onInput { model.updateExpectedState { copy(evaluateGuts = it.value) } }
                }
                Text("賢さ")
                TextInput(expectedState.evaluateWisdom) {
                    size(10)
                    onInput { model.updateExpectedState { copy(evaluateWisdom = it.value) } }
                }
            }
            Div {
                Text("スキルPt")
                TextInput(expectedState.evaluateSkillPt) {
                    size(10)
                    onInput { model.updateExpectedState { copy(evaluateSkillPt = it.value) } }
                }
                Text("体力")
                TextInput(expectedState.evaluateHp) {
                    size(10)
                    onInput { model.updateExpectedState { copy(evaluateHp = it.value) } }
                }
                if (state.scenario == Scenario.GRAND_LIVE) {
                    Text("パフォーマンス")
                    TextInput(expectedState.evaluatePerformance) {
                        size(10)
                        onInput { model.updateExpectedState { copy(evaluatePerformance = it.value) } }
                    }
                }
                Text("絆（80未満時）")
                TextInput(expectedState.evaluateRelation) {
                    size(10)
                    onInput { model.updateExpectedState { copy(evaluateRelation = it.value) } }
                }
            }
        }
        Div({ style { marginTop(16.px) } }) {
            MdFilledButton("計算") {
                onClick { model.calculateExpected() }
            }
        }
        Div {
            Small({ style { color(Color.white) } }) { Text("※サポカ外100人とかやるなよ絶対だぞ") }
        }
        if (status != null) {
            ExpectedStatusTable(state.scenario, status, expectedState.typeRateList) {
                expectedState.evaluate(it)
            }
            Div {
                Small { Text("+お休み: 減った体力を50回復のお休みで回復させた場合の期待値") }
            }
        }
        if (expectedState.statusHistory.isNotEmpty()) {
            H3 { Text("履歴") }
            expectedState.statusHistory.forEach { entry ->
                ExpectedStatusTable(state.scenario, entry.first, entry.second) {
                    expectedState.evaluate(it)
                }
            }
        }
    }
}

@Composable
private fun ExpectedStatusTable(
    scenario: Scenario,
    status: ExpectedStatus,
    typeRateList: List<Pair<StatusType, Double>>,
    evaluate: (ExpectedStatus) -> Double,
) {
    Div({ style { marginTop(16.px) } }) {
        Div {
            Text("トレーニング選択割合 " + typeRateList.joinToString(", ") { "${it.first.displayName}: ${(it.second * 100).round()}%" })
        }
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th({ style { property("border", "none") } }) { }
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
                Th { Text("スキルPt") }
                Th { Text("体力") }
                if (scenario == Scenario.GRAND_LIVE) {
                    Th { Text("パフォ") }
                }
                Th { Text("5ステ合計") }
                Th { Text("+スキルPt") }
                if (scenario == Scenario.GRAND_LIVE) {
                    Th({ style { width(90.px) } }) { Text("+パフォ/2") }
                }
                Th { Text("評価値") }
            }
            Tr {
                Th { Text("期待値") }
                Td { Text(status.speed.round().toString()) }
                Td { Text(status.stamina.round().toString()) }
                Td { Text(status.power.round().toString()) }
                Td { Text(status.guts.round().toString()) }
                Td { Text(status.wisdom.round().toString()) }
                Td { Text(status.skillPt.round().toString()) }
                Td { Text(status.hp.round().toString()) }
                if (scenario == Scenario.GRAND_LIVE) {
                    Td { Text(status.performance?.totalValue?.round().toString()) }
                }
                Td { Text(status.statusTotal.round().toString()) }
                Td { Text((status.totalPlusSkillPt).round().toString()) }
                if (scenario == Scenario.GRAND_LIVE) {
                    Td { Text(status.totalPlusSkillPtPerformance.round().toString()) }
                }
                Td { Text(evaluate(status).round().toString()) }
            }
            Tr {
                val sleep = status.hpToSleep()
                Th { Text("+お休み") }
                Td { Text(sleep.speed.round().toString()) }
                Td { Text(sleep.stamina.round().toString()) }
                Td { Text(sleep.power.round().toString()) }
                Td { Text(sleep.guts.round().toString()) }
                Td { Text(sleep.wisdom.round().toString()) }
                Td { Text(sleep.skillPt.round().toString()) }
                Td { Text(sleep.hp.round().toString()) }
                if (scenario == Scenario.GRAND_LIVE) {
                    Td { Text(sleep.performance?.totalValue?.round().toString()) }
                }
                Td { Text(sleep.statusTotal.round().toString()) }
                Td { Text((sleep.totalPlusSkillPt).round().toString()) }
                if (scenario == Scenario.GRAND_LIVE) {
                    Td { Text(sleep.totalPlusSkillPtPerformance.round().toString()) }
                }
                Td { Text(evaluate(sleep).round().toString()) }
            }
        }
    }
}
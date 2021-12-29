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
import io.github.mee1080.umasim.web.components.GroupedSelect
import io.github.mee1080.umasim.web.components.LabeledCheckbox
import io.github.mee1080.umasim.web.components.LabeledRadioGroup
import io.github.mee1080.umasim.web.onClickOrTouch
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun SupportSelect(model: ViewModel, state: State) {
    H2 { Text("サポートカード") }
    Div({ style { paddingBottom(16.px) } }) {
        TextInput(state.supportFilter) {
            placeholder("カード名、スキルヒントでフィルタ (空白区切りでAnd検索)")
            size(60)
            onInput { model.updateSupportFilter(it.value) }
        }
        Button({
            if (state.supportFilterApplied) {
                disabled()
            }
            onClickOrTouch { model.applyFilter() }
        }) { Text("フィルタ適用") }
    }
    Div {
        (0..1).forEach { row ->
            Div({ classes(AppStyle.supportCardArea) }) {
                state.supportSelectionList.slice((row * 3)..(row * 3 + 2)).forEachIndexed { offset, item ->
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
                            state.getSupportSelection(index),
                            item.selectedSupport,
                            {
                                classes(AppStyle.supportCard)
                                if (state.isFriendTraining(index)) {
                                    classes(AppStyle.friendSupportCard)
                                }
                            },
                            { model.updateSupport(index, it) },
                        ) {
                            Div({ classes("after") })
                        }
                        LabeledRadioGroup(
                            "talent$index",
                            "上限解放：",
                            WebConstants.supportTalentList,
                            item.supportTalent,
                        ) { model.updateSupportTalent(index, it) }
                        Div {
                            LabeledCheckbox("join$index", "練習参加", item.join) { model.updateJoin(index, it) }
                            LabeledCheckbox("friend$index", "友情", item.friend) { model.updateFriend(index, it) }
                        }
                    }
                }
            }
        }
    }
    if (state.scenario == Scenario.AOHARU) {
        H3 { Text("サポカ外参加人数") }
        Div {
            Button({ onClickOrTouch { model.updateTeamJoinCount(-1) } }) { Text("-") }
            Span({ style { padding(8.px) } }) { Text(state.teamJoinCount.toString()) }
            Button({ onClickOrTouch { model.updateTeamJoinCount(1) } }) { Text("+") }
        }
    }
}
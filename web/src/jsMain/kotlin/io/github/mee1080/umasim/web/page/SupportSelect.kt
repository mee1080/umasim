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
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.web.components.LabeledCheckbox
import io.github.mee1080.umasim.web.components.LabeledRadio
import io.github.mee1080.umasim.web.components.LabeledRadioGroup
import io.github.mee1080.umasim.web.components.material.*
import io.github.mee1080.umasim.web.onClickOrTouch
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.state.displayName
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLSelectElement

@Composable
fun SupportSelect(model: ViewModel, state: State) {
    H2 { Text("サポートカード") }
    Div({ style { paddingBottom(16.px) } }) {
        Div({
            style {
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
            }
        }) {
            Text("保存: ")
            MwcTextField(state.supportSaveName) {
                placeholder("保存名")
                size(40)
                outlined()
                onInput { model.updateSupportSaveName(it) }
            }
            MwcButton({
                if (state.supportSaveName.isEmpty()) disabled()
                onClickOrTouch { model.saveSupport() }
                raised()
            }) {
                Text(if (state.supportLoadList.contains(state.supportSaveName)) "上書保存" else "新規保存")
            }
        }
        Div({
            style {
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
            }
        }) {
            Text("読込: ")
            MwcSelect(
                state.supportLoadList,
                state.supportLoadName,
                onSelect = model::updateSupportLoadName,
            )
            MwcButton({
                if (state.supportLoadName.isEmpty()) disabled()
                onClick { model.loadSupport() }
                raised()
            }) { Text("読込") }
        }
    }
    Div({
        style {
            paddingBottom(16.px)
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
        }
    }) {
        MwcTextField(state.supportFilter) {
            placeholder("カード名、スキルヒントでフィルタ (空白区切りでAnd検索)")
            size(60)
            outlined()
            onInput { model.updateSupportFilter(it) }
        }
        MwcButton({
            if (state.supportFilterApplied) disabled()
            onClickOrTouch { model.applyFilter() }
            raised()
        }) { Text("フィルタ適用") }
    }
    Div({
        style {
            paddingBottom(16.px)
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
        }
    }) {
        Text("ソート順: ")
        MwcSelect(
            WebConstants.supportSortOrder,
            state.supportSortOrder,
            onSelect = model::updateSorOrder,
            itemToValue = { it.label },
        )
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
                            StatusType.GROUP -> rgb(149, 243, 86)
                            else -> null
                        }?.let {
                            style {
                                background("linear-gradient(170deg, #ffffff00, #ffffff00 70%, $it)")
                            }
                        }
                    }) {
                        Div {
                            val selection = WebConstants.displayStatusTypeList
                            val selectedValue = item.statusType
                            Select({
                                prop(
                                    { e: HTMLSelectElement, v -> e.selectedIndex = v },
                                    selection.indexOfFirst { it == selectedValue }
                                )
                                onChange { model.updateSupportType(index, selection[it.value!!.toInt()]) }
                            }) {
                                selection.forEachIndexed { index, statusType ->
                                    Option(
                                        index.toString(),
                                        { if (statusType == selectedValue) selected() }
                                    ) { Text(statusType.displayName) }
                                }
                            }
                        }
                        Div({
                            classes(AppStyle.supportCard)
                            if (state.isFriendTraining(index)) {
                                classes(AppStyle.friendSupportCard)
                            }
                        }) {
                            val selection = state.getSupportSelection(index)
                            val selectedValue = item.selectedSupport
                            Select({
                                prop(
                                    { e: HTMLSelectElement, v -> e.selectedIndex = v },
                                    selection.indexOfFirst { it.first == selectedValue }
                                )
                                onChange { model.updateSupport(index, it.value!!.toInt()) }
                            }) {
                                selection.forEach { (index, card) ->
                                    Option(
                                        index.toString(),
                                        { if (index == selectedValue) selected() }
                                    ) { Text(card.displayName() + state.supportSortOrder.toInfo(card)) }
                                }
                            }
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
                        }
                        Div {
                            Text("絆")
                            item.relationSelection.forEach {
                                LabeledRadio(
                                    "relation$index",
                                    "$it",
                                    "$it",
                                    item.relation == it
                                ) { model.updateRelation(index, it) }
                            }
                        }
                        Div {
                            if (item.statusType == StatusType.GROUP) {
                                LabeledCheckbox("passion$index", "情熱ゾーン", item.passion) {
                                    model.updatePassion(index, it)
                                }
                            }
                        }
                        Div {
                            if (item.card?.specialUnique?.any { it.needCheckFriendCount } == true) {
                                Text("友情回数")
                                (0..5).forEach {
                                    LabeledRadio(
                                        "friendCount$index",
                                        "$it",
                                        "$it",
                                        item.friendCount == it
                                    ) { model.updateFriendCount(index, it) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    Div {
        MwcButton({
            onClickOrTouch {
                state.supportSelectionList.indices.forEach { model.updateRelation(it, 0) }
            }
        }) {
            Text("全員絆0")
        }
        MwcButton({
            onClickOrTouch {
                state.supportSelectionList.indices.forEach { model.updateRelation(it, 80) }
            }
        }) {
            Text("全員絆80")
        }
    }
}
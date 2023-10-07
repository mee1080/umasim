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
package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.web.components.atoms.*
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.SupportSelection
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.state.displayName
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Option
import org.jetbrains.compose.web.dom.Select
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLSelectElement

@Composable
fun SupportSelect(model: ViewModel, state: State) {
    HideBlock(
        header = { Text("サポートカード") },
        initialOpen = true,
        headerClosed = {
            Div({
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                }
            }) {
                Div { Text("サポートカード:") }
                Div({
                    style {
                        display(DisplayStyle.Flex)
                        flexWrap(FlexWrap.Wrap)
                        columnGap(16.px)
                    }
                }) {
                    state.supportSelectionList.forEach {
                        Div { Text(it.name) }
                    }
                }
            }
        }
    ) {
        NestedHideBlock("保存・読み込み") {
            Div({
                style {
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Center)
                }
            }) {
                Text("保存: ")
                MdOutlinedTextField(state.supportSaveName) {
                    placeholder("保存名")
                    onInput { model.updateSupportSaveName(it) }
                    style {
                        width(400.px)
                    }
                }
                MdFilledButton(if (state.supportLoadList.contains(state.supportSaveName)) "上書保存" else "新規保存") {
                    if (state.supportSaveName.isEmpty()) disabled()
                    onClick { model.saveSupport() }
                }
            }
            Div({
                style {
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Center)
                }
            }) {
                Text("読込: ")
                MdOutlinedSelect(
                    state.supportLoadList,
                    state.supportLoadName,
                    onSelect = model::updateSupportLoadName,
                )
                MdFilledButton("読込") {
                    if (state.supportLoadName.isEmpty()) disabled()
                    onClick { model.loadSupport() }
                }
            }
        }
        Div({
            style {
                paddingBottom(16.px)
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
            }
        }) {
            MdOutlinedTextField(state.supportFilter) {
                placeholder("カード名、スキルヒントでフィルタ (空白区切りでAnd検索)")
                onInput { model.updateSupportFilter(it) }
                style {
                    width(400.px)
                }
            }
            MdFilledTonalButton("フィルタ適用") {
                if (state.supportFilterApplied) disabled()
                onClick { model.applyFilter() }
            }
        }
        Div({
            style {
                paddingBottom(16.px)
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
            }
        }) {
            Text("ソート順: ")
            MdOutlinedSelect(
                WebConstants.supportSortOrder,
                state.supportSortOrder,
                onSelect = model::updateSorOrder,
                itemToValue = { it.label },
            )
        }
        Div({ classes(AppStyle.supportCardArea) }) {
            state.supportSelectionList.forEachIndexed { index, item ->
                SupportCardBlock(item, index, model, state)
            }
        }
        Div {
            MdTextButton("全員絆0") {
                onClick {
                    state.supportSelectionList.indices.forEach { model.updateRelation(it, 0) }
                }
            }
            MdTextButton("全員絆80") {
                onClick {
                    state.supportSelectionList.indices.forEach { model.updateRelation(it, 80) }
                }
            }
            MdTextButton("全員不参加") {
                onClick {
                    state.supportSelectionList.indices.forEach { model.updateJoin(it, false) }
                }
            }
        }
    }
}

@Composable
fun SupportCardBlock(item: SupportSelection, index: Int, model: ViewModel, state: State) {
    Card({
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
                if (state.isFriendTraining(index)) {
                    background("linear-gradient(170deg, #ffffff00, #ffffff00 70%, $it), linear-gradient(to right, #e0c00020, #ea433520)")
                } else {
                    background("linear-gradient(170deg, #ffffff00, #ffffff00 70%, $it)")
                }
            }
        }
    }) {
        Div {
            val selection = WebConstants.displayStatusTypeList
            val selectedValue = item.statusType
            Select({
                prop({ e: HTMLSelectElement, v -> e.selectedIndex = v },
                    selection.indexOfFirst { it == selectedValue })
                onChange { model.updateSupportType(index, selection[it.value!!.toInt()]) }
            }) {
                selection.forEachIndexed { index, statusType ->
                    Option(index.toString(), { if (statusType == selectedValue) selected() }) {
                        Text(statusType.displayName)
                    }
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
                prop({ e: HTMLSelectElement, v -> e.selectedIndex = v },
                    selection.indexOfFirst { it.first == selectedValue })
                onChange { model.updateSupport(index, it.value!!.toInt()) }
            }) {
                selection.forEach { (index, card) ->
                    Option(index.toString(), { if (index == selectedValue) selected() }) {
                        Text(card.displayName() + state.supportSortOrder.toInfo(card))
                    }
                }
            }
        }
        DivFlexCenter {
            Text("上限解放：")
            MdRadioGroup(listOf(0, 1, 2, 3, 4), item.supportTalent, onSelect = {
                model.updateSupportTalent(index, it)
            })
        }
        Div {
            MdCheckbox("練習参加", item.join) {
                onChange { model.updateJoin(index, it) }
            }
        }
        DivFlexCenter {
            Text("絆：")
            MdRadioGroup(item.relationSelection, item.relation, onSelect = {
                model.updateRelation(index, it)
            })
        }
        Div {
            if (item.statusType == StatusType.GROUP) {
                MdCheckbox("情熱ゾーン", item.passion) {
                    onChange { model.updatePassion(index, it) }
                }
            }
        }
        if (item.card?.specialUnique?.any { it.needCheckFriendCount } == true) {
            DivFlexCenter {
                Text("友情回数：")
                MdRadioGroup(listOf(0, 1, 2, 3, 4, 5), item.friendCount, onSelect = {
                    model.updateFriendCount(index, it)
                })
            }
        }
    }
}
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
package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.web.components.atoms.Card
import io.github.mee1080.umasim.web.components.atoms.MdClass
import io.github.mee1080.umasim.web.components.atoms.MdFilledButton
import io.github.mee1080.umasim.web.page.share.StatusTable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun SelectionBlock(
    selection: List<Action>,
    onSelect: (SelectedAction) -> Unit,
) {
    Div({
        classes(MdClass.surface, MdClass.onSurfaceText)
        style {
            height(0.px)
            flexGrow(1)
            overflowY("scroll")
            padding(16.px)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            rowGap(8.px)
        }
    }) {
        selection.forEach { action ->
            Card({
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    rowGap(4.px)
                }
            }) {
                MdFilledButton(action.name) {
                    onClick { onSelect(SelectedAction(action)) }
                }
                when (action) {
                    is Outing -> {
                        Div { Text(action.support?.charaName ?: "育成キャラ") }
                    }

                    is Race -> {
                        Div { Text(action.raceName) }
                    }

                    is SSMatch -> {
                        Div {
                            action.member.forEach {
                                MemberState(it)
                            }
                        }
                    }

                    is Sleep -> {}

                    is Training -> {
                        Div {
                            action.member.forEach {
                                MemberState(it, it.isFriendTraining(action.type))
                            }
                        }
                        val totalRate = action.totalRate
                        action.candidates.forEach {
                            Div {
                                if (it.first.success) {
                                    Div { Text("失敗率：${100.0 * (totalRate - it.second) / totalRate}%") }
                                    StatusTable(it.first.status)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

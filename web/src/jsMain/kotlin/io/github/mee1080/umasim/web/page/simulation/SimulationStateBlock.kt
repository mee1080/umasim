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
import io.github.mee1080.umasim.data.turnToString
import io.github.mee1080.umasim.simulation2.SimulationState
import io.github.mee1080.umasim.web.components.atoms.MdClass
import io.github.mee1080.umasim.web.components.atoms.MdSysColor
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.page.share.StatusTable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun SimulationStateBlock(state: SimulationState) {
    Div({
        classes(MdClass.surface, MdClass.onSurfaceText)
        style {
            padding(16.px)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            rowGap(8.px)
        }
    }) {
        Div { Text(turnToString(state.turn)) }
        DivFlexCenter {
            Text("体力：")
            Div({
                style {
                    height(24.px)
                    flexGrow(1)
                }
            }) {
                Div({
                    style {
                        width((state.status.maxHp / 1.2).percent)
                        height(100.percent)
                        border(1.px, LineStyle.Solid, MdSysColor.outline.value)
                    }
                }) {
                    Div({
                        style {
                            backgroundColor(MdSysColor.tertiary.value)
                            width((100.0 * state.status.hp / state.status.maxHp).percent)
                            height(100.percent)
                        }
                    })
                }
            }
        }
        StatusTable(state.status, summary = true)
        state.lArcStatus?.let {
            Div {
                Text("サポーターPt=${it.supporterPt}/${it.memberSupporterPt}, 期待度=${it.expectationLevel} 適性Pt=${it.aptitudePt}, SSマッチ=${it.totalSSMatchCount}回, 海外適性=${it.overseasTurfAptitude}/${it.longchampAptitude}/${it.lifeRhythm}/${it.nutritionManagement}/${it.frenchSkill}/${it.overseasExpedition}/${it.strongHeart}/${it.mentalStrength}/${it.hopeOfLArc}/${it.consecutiveVictories})")
            }
        }
        HideBlock("サポートカード") {
            state.support.forEach {
                MemberState(it)
            }
        }
    }
}
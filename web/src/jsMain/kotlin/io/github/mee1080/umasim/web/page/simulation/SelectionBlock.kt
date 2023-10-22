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
import io.github.mee1080.umasim.data.LArcAptitude
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.web.components.atoms.Card
import io.github.mee1080.umasim.web.components.atoms.MdClass
import io.github.mee1080.umasim.web.components.atoms.MdFilledButton
import io.github.mee1080.umasim.web.components.atoms.tertiary
import io.github.mee1080.umasim.web.page.share.StatusTable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun SelectionBlock(
    selection: List<Action>,
    onSelect: (Action) -> Unit,
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
                    if (!action.turnChange) {
                        tertiary()
                    }
                    onClick { onSelect(action) }
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
                            val result = it.first as StatusActionResult
                            if (result.success) {
                                Div {
                                    Div { Text("失敗率：${100.0 * (totalRate - it.second) / totalRate}%") }
                                    val aptitudePt = (result.scenarioActionParam as? LArcActionParam)?.aptitudePt
                                    if (aptitudePt != null && aptitudePt > 0) {
                                        Div { Text("適性Pt：$aptitudePt") }
                                    }
                                    StatusTable(result.status)
                                }
                            }
                        }
                    }

                    is GmActivateWisdom -> {}

                    is ClimaxBuyUseItem -> {
                        // TODO
                    }

                    is LArcGetAptitude -> {
                        val description = when (action.result.aptitude) {
                            LArcAptitude.OverseasTurfAptitude -> "Lv2:芝適性A / Lv3海外根性トレーニング効果+50%"
                            LArcAptitude.LongchampAptitude -> "Lv2:中距離適性A / Lv3海外スタミナトレーニング効果+50%"
                            LArcAptitude.LifeRhythm -> "Lv2:パワー+200 / Lv3海外パワートレーニング効果+50%"
                            LArcAptitude.NutritionManagement -> "Lv2:スピード+100 / Lv3海外スピードトレーニング効果+50%"
                            LArcAptitude.FrenchSkill -> "Lv2:賢さ+200 / Lv3海外賢さトレーニング効果+50%"
                            LArcAptitude.OverseasExpedition -> "Lv2:スタミナ+200 / Lv3トレーニングスキルPt+10"
                            LArcAptitude.StrongHeart -> "Lv2:スタミナ+200 / Lv3海外トレーニング体力消費軽減20%"
                            LArcAptitude.MentalStrength -> "Lv2:根性+200 / Lv3友情トレーニング効果+20%"
                            LArcAptitude.HopeOfLArc -> ""
                            LArcAptitude.ConsecutiveVictories -> "凱旋門賞獲得アップ"
                        }
                        Div { Text(description) }
                    }

                    is LiveGetLesson -> {}
                }
            }
        }
    }
}

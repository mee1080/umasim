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
import io.github.mee1080.utility.roundToString
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun SelectionBlock(
    state: SimulationState,
    selection: List<Action>,
    aiSelection: Int,
    aiScore: List<Double>,
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
        selection.forEachIndexed { index, action ->
            val uafStatus = state.uafStatus
            val (actionName, uafAthletic, uafAthleticLevel) = if (uafStatus != null && action is Training) {
                val athletic = uafStatus.trainingAthletics[action.type]!!
                val level = uafStatus.athleticsLevel[athletic]!!
                Triple("トレーニング(${action.type.displayName}/${athletic.longDisplayName}Lv$level)", athletic, level)
            } else Triple(action.name, null, 0)
            Card({
                style {
                    position(Position.Relative)
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    rowGap(4.px)
                    if (uafAthletic != null) {
                        background(uafAthletic.genre.colorCode)
                    }
                    if (action is UafConsult) {
                        background("linear-gradient(to right, ${action.result.from.colorCode} 0 50%, ${action.result.to.colorCode} 0 50%)")
                    }
                }
            }) {
                MdFilledButton(actionName) {
                    if (!action.turnChange) {
                        tertiary()
                    }
                    onClick { onSelect(action) }
                }
                when (action) {
                    NoAction -> {}

                    is Outing -> {
                        CookMaterialInfo(action)
                    }

                    is Race -> {
                        CookMaterialInfo(action)
                    }

                    is SSMatch -> {
                        Div {
                            action.member.forEach {
                                MemberState(it)
                            }
                        }
                        Div { Text("サポーターPt：${((action.result as StatusActionResult).scenarioActionParam as LArcActionParam).supporterPt}") }
                        StatusTable(action.result.status)
                    }

                    is Sleep -> {
                        CookMaterialInfo(action)
                    }

                    is Training -> {
                        CookMaterialInfo(action)
                        if (uafStatus != null && uafAthletic != null) {
                            val actionResult = action.candidates[0].first as? StatusActionResult
                            val param = actionResult?.scenarioActionParam as UafScenarioActionParam?
                            val levelUp = param!!.athleticsLevelUp[action.type]!!
                            val levelUpTotal = param.athleticsLevelUp.values.sum()
                            val rest = 50 - (uafStatus.genreLevel[uafAthletic.genre]!! % 50)
                            Div {
                                Text("競技レベル：$uafAthleticLevel + $levelUp (あと${rest} + $levelUpTotal)")
                            }
                        }
                        Div {
                            action.member.forEach {
                                MemberState(it, it.isFriendTraining(action.type), training = true)
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

                    is UafConsult -> {}

                    is CookActivateDish -> {}

                    is CookMaterialLevelUp -> {}
                }
                val targetAiScore = aiScore.getOrNull(index)
                if (aiSelection == index || targetAiScore != null) {
                    Div({
                        classes(MdClass.surfaceContainerHigh, MdClass.onSurfaceText)
                        style {
                            position(Position.Absolute)
                            padding(4.px)
                            borderRadius(4.px)
                            right(8.px)
                            top(8.px)
                        }
                    }) {
                        if (targetAiScore != null) {
                            Text("AIスコア：${targetAiScore.roundToString(2)}")
                        }
                        if (aiSelection == index) {
                            Span({ style { fontWeight("bold") } }) { Text("[選択]") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CookMaterialInfo(action: Action) {
    val target = action.candidates.first().first as? StatusActionResult ?: return
    val param = target.scenarioActionParam as? CookActionParam ?: return
    Div {
        Text(param.stamp.toString())
    }
}
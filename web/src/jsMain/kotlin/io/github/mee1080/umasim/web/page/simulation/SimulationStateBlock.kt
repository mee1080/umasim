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
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.data.turnToString
import io.github.mee1080.umasim.scenario.cook.CookMaterial
import io.github.mee1080.umasim.scenario.mecha.MechaChipType
import io.github.mee1080.umasim.scenario.mecha.maxLearningLevel
import io.github.mee1080.umasim.scenario.uaf.UafAthletic
import io.github.mee1080.umasim.scenario.uaf.UafGenre
import io.github.mee1080.umasim.simulation2.SimulationState
import io.github.mee1080.umasim.web.components.atoms.MdClass
import io.github.mee1080.umasim.web.components.atoms.MdSysColor
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.page.share.StatusTable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.math.max
import kotlin.math.min

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
        Div {
            if (state.turn > 12) {
                Text(turnToString(state.turn))
            }
            state.nextGoalRace?.let {
                Text(" 目標まであと ${it.turn - state.turn} ターン [${it.name}]")
            }
        }
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
        if (state.refreshTurn > 0 || state.condition.isNotEmpty()) {
            Div {
                Text("状態：")
                if (state.refreshTurn > 0) Text("リフレッシュの心得, ")
                Text(state.condition.joinToString(", "))
            }
        }
        state.lArcStatus?.let {
            Div {
                Text("サポーターPt=${it.supporterPt}/${it.memberSupporterPt}, 期待度=${it.expectationLevel} 適性Pt=${it.aptitudePt}, SSマッチ=${it.totalSSMatchCount}回, 海外適性=${it.overseasTurfAptitude}/${it.longchampAptitude}/${it.lifeRhythm}/${it.nutritionManagement}/${it.frenchSkill}/${it.overseasExpedition}/${it.strongHeart}/${it.mentalStrength}/${it.hopeOfLArc}/${it.consecutiveVictories})")
            }
        }
        state.uafStatus?.let { uafStatus ->
            Div {
                val goalLevel = max(10, min(50, ((state.turn - 1) / 12) * 10))
                Text("相談 ${uafStatus.consultCount}")
                UafGenre.entries.forEach { genre ->
                    Text(" | ${genre.colorName}Lv${uafStatus.genreLevel[genre]}(")
                    UafAthletic.byGenre[genre]?.forEachIndexed { index, uafAthletic ->
                        if (index > 0) Text("/")
                        val level = uafStatus.athleticsLevel[uafAthletic]!!
                        Span({
                            if (level >= goalLevel) {
                                style { fontWeight("bold") }
                            }
                        }) { Text(level.toString()) }
                    }
                    Text(")")
                    if (uafStatus.heatUp[genre]!! > 0) {
                        Span({ style { fontWeight("bold") } }) {
                            Text("【ヒートアップ ${uafStatus.heatUp[genre]}ターン】")
                        }
                    }
                }
            }
        }
        state.cookStatus?.let { cookStatus ->
            Div {
                Text("畑Pt:${cookStatus.gardenPoint} | お料理Pt:${cookStatus.cookPoint} | お料理ゲージ:${cookStatus.cookGauge}")
            }
            Div {
                Text("野菜Lv:")
                Text(CookMaterial.entries.joinToString("/") { cookStatus.materialLevel[it]!!.toString() })
                Text(" | 野菜数:")
                Text(CookMaterial.entries.joinToString("/") {
                    "${cookStatus.materialCount[it]!!}+${cookStatus.pendingMaterials[it]!!}"
                })
                Text(" | スタンプ:")
                Text(cookStatus.currentStamp.joinToString("/"))
            }
        }
        state.mechaStatus?.let { mechaStatus ->
            Div {
                val chipLevels = MechaChipType.entries.joinToString(" / ") { type ->
                    (0..2).joinToString(", ") {
                        "${type.chipNames[it]} ${mechaStatus.chipLevels[type]!![it]}pt"
                    }
                }
                Text("チューニング: $chipLevels")
            }
            Div {
                var total = 0
                val learningLevels = trainingType.joinToString(", ") {
                    val value = mechaStatus.learningLevels[it]!!
                    total += value
                    value.toString()
                }
                val targetTotal = when {
                    state.turn > 72 -> null
                    state.turn > 60 -> 2400
                    state.turn > 48 -> 1900
                    state.turn > 36 -> 1400
                    state.turn > 24 -> 1000
                    else -> 600
                }?.let { " / $it" } ?: ""
                Text("研究Lv: $learningLevels / ${maxLearningLevel(state.turn)} (合計 $total$targetTotal)")
            }
            Div {
                Text("オーバードライブゲージ: ${mechaStatus.overdriveGauge} / 6")
                if (mechaStatus.overdrive) {
                    Text(" 【発動中】")
                } else if (mechaStatus.overdriveGauge >= 3) {
                    Text(" （発動可能）")
                }
            }
        }
        state.legendStatus?.let {
            LegendStateBlock(it)
        }
        state.mujintoStatus?.let {
            MujintoStateBlock(it)
        }
        state.onsenStatus?.let {
            OnsenStateBlock(it, state.status)
        }
        HideBlock("サポートカード") {
            state.support.forEach {
                MemberState(it)
            }
        }
    }
}

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
package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.Founder
import io.github.mee1080.umasim.data.Knowledge
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.trainingTypeOrSkill
import io.github.mee1080.umasim.web.components.LabeledRadio
import io.github.mee1080.umasim.web.components.atoms.MdCheckbox
import io.github.mee1080.umasim.web.components.atoms.MdTextButton
import io.github.mee1080.umasim.web.components.atoms.onChange
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.components.parts.SliderEntry
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLSelectElement

@Composable
fun TrainingSetting(model: ViewModel, state: State) {
    HideBlock("トレーニング設定", true) {
        NestedHideBlock("特殊固有") {
            SliderEntry("ファン数：", state.fanCount, 0, 200000, 10000) {
                model.updateFanCount(it.toInt())
            }
            SliderEntry("体力：", state.hp, 0, 120) {
                model.updateHp(it.toInt())
            }
            SliderEntry("体力最大値：", state.maxHp, 100, 120) {
                model.updateMaxHp(it.toInt())
            }
            SliderEntry("絆合計：", state.totalRelation, 0, 600) {
                model.updateTotalRelation(it.toInt())
            }
            SliderEntry("速度スキル数：", state.speedSkillCount, 0, 5) {
                model.updateSpeedSkillCount(it.toInt())
            }
            SliderEntry("回復スキル数：", state.healSkillCount, 0, 3) {
                model.updateHealSkillCount(it.toInt())
            }
            SliderEntry("合計トレLv：", state.totalTrainingLevel, 5, 20) {
                model.updateTotalTrainingLevel(it.toInt())
            }
        }
        if (state.scenario == Scenario.GRAND_LIVE) {
            NestedHideBlock("グランドライブ") {
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
        if (state.scenario == Scenario.GM) {
            NestedHideBlock("グランドマスターズ") {
                H3 { Text("知識表") }
                Div {
                    for (i in 12..13) {
                        KnowledgeTable(model, i, state.gmState.knowledgeTable[i])
                    }
                }
                Div {
                    for (i in 8..11) {
                        KnowledgeTable(model, i, state.gmState.knowledgeTable[i])
                    }
                }
                Div {
                    for (i in 0..7) {
                        KnowledgeTable(model, i, state.gmState.knowledgeTable[i])
                    }
                }
                Div {
                    MdTextButton("クリア") {
                        onClick { model.clearGmKnowledge() }
                    }
                }

                H3 { Text("女神の叡智") }
                val wisdomSelection = listOf(null) + Founder.entries
                val selectedWisdom = state.gmState.wisdom
                Select({
                    prop(
                        { e: HTMLSelectElement, v -> e.selectedIndex = v },
                        wisdomSelection.indexOfFirst { it == selectedWisdom }
                    )
                    onChange { model.updateGmWisdom(wisdomSelection[it.value!!.toInt()]) }
                }) {
                    wisdomSelection.forEachIndexed { index, wisdom ->
                        Option(
                            index.toString(),
                            { if (wisdom == selectedWisdom) selected() }
                        ) { Text(wisdom?.longName ?: "なし") }
                    }
                }

                H3 { Text("知識Lv") }
                Founder.entries.forEach { founder ->
                    SliderEntry("${founder.longName}：", state.gmState.wisdomLevel[founder]!!, 0, 5) {
                        model.updateGmWisdomLevel(founder, it.toInt())
                    }
                }
            }
        }
        if (state.scenario == Scenario.LARC) {
            NestedHideBlock("プロジェクトL'Arc") {
                val lArcState = state.lArcState
                SliderEntry("期待度：", lArcState.expectations, 0, 200) {
                    model.updateLArc { copy(expectations = it.toInt()) }
                }
                SliderEntry("海外洋芝適性：", lArcState.overseasTurfAptitude, 0, 3) {
                    model.updateLArc { copy(overseasTurfAptitude = it.toInt()) }
                }
                SliderEntry("ロンシャン適性：", lArcState.longchampAptitude, 0, 3) {
                    model.updateLArc { copy(longchampAptitude = it.toInt()) }
                }
                SliderEntry("生活リズム：", lArcState.lifeRhythm, 0, 3) {
                    model.updateLArc { copy(lifeRhythm = it.toInt()) }
                }
                SliderEntry("栄養管理：", lArcState.nutritionManagement, 0, 3) {
                    model.updateLArc { copy(nutritionManagement = it.toInt()) }
                }
                SliderEntry("フランス語力：", lArcState.frenchSkill, 0, 3) {
                    model.updateLArc { copy(frenchSkill = it.toInt()) }
                }
                SliderEntry("海外遠征：", lArcState.overseasExpedition, 0, 3) {
                    model.updateLArc { copy(overseasExpedition = it.toInt()) }
                }
                SliderEntry("強心臓：", lArcState.strongHeart, 0, 3) {
                    model.updateLArc { copy(strongHeart = it.toInt()) }
                }
                SliderEntry("精神力：", lArcState.mentalStrength, 0, 3) {
                    model.updateLArc { copy(mentalStrength = it.toInt()) }
                }
                SliderEntry("L’Arcの希望：", lArcState.hopeOfLArc, 0, 3) {
                    model.updateLArc { copy(hopeOfLArc = it.toInt()) }
                }
                DivFlexCenter {
                    MdTextButton("海外適性すべて0") { onClick { model.setAllAptitude(0) } }
                    MdTextButton("海外適性すべて1") { onClick { model.setAllAptitude(1) } }
                    MdTextButton("海外適性すべて2") { onClick { model.setAllAptitude(2) } }
                    MdTextButton("海外適性すべて3") { onClick { model.setAllAptitude(3) } }
                }
                DivFlexCenter {
                    MdCheckbox("海外遠征中", lArcState.overseas) {
                        onChange { model.updateLArc { copy(overseas = it) } }
                    }
                }
            }
        }
    }
}

@Composable
fun KnowledgeTable(model: ViewModel, index: Int, knowledge: Knowledge?) {
    Div({
        style {
            paddingLeft(16.px)
            flexDirection(FlexDirection.Column)
            display(DisplayStyle.LegacyInlineFlex)
        }
    }) {
        val typeSelection = listOf(null, *trainingTypeOrSkill)
        val selectedType = knowledge?.type
        Select({
            prop(
                { e: HTMLSelectElement, v -> e.selectedIndex = v },
                typeSelection.indexOfFirst { it == selectedType }
            )
            onChange { model.updateGmKnowledgeType(index, typeSelection[it.value!!.toInt()]) }
        }) {
            typeSelection.forEachIndexed { index, type ->
                Option(
                    index.toString(),
                    { if (type == selectedType) selected() }
                ) { Text(type?.displayName ?: "なし") }
            }
        }

        if (index >= 8) {
            Div {
                val selectedBonus = knowledge?.bonus ?: 2
                for (i in 2..3) {
                    LabeledRadio("knowledgeBonus$index", "$i", "$i", selectedBonus == i) {
                        model.updateGmKnowledgeBonus(index, i)
                    }
                }
            }
        }
    }
}
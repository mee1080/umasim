package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.trainingTypeOrSkill
import io.github.mee1080.umasim.scenario.gm.Founder
import io.github.mee1080.umasim.scenario.gm.Knowledge
import io.github.mee1080.umasim.web.components.LabeledRadio
import io.github.mee1080.umasim.web.components.atoms.MdTextButton
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.components.parts.SliderEntry
import io.github.mee1080.umasim.web.state.GmState
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLSelectElement

@Composable
fun GmTrainingSetting(model: ViewModel, state: GmState) {
    NestedHideBlock("グランドマスターズ") {
        H3 { Text("知識表") }
        Div {
            for (i in 12..13) {
                KnowledgeTable(model, i, state.knowledgeTable[i])
            }
        }
        Div {
            for (i in 8..11) {
                KnowledgeTable(model, i, state.knowledgeTable[i])
            }
        }
        Div {
            for (i in 0..7) {
                KnowledgeTable(model, i, state.knowledgeTable[i])
            }
        }
        Div {
            MdTextButton("クリア") {
                onClick { model.clearGmKnowledge() }
            }
        }

        H3 { Text("女神の叡智") }
        val wisdomSelection = listOf(null) + Founder.entries
        val selectedWisdom = state.wisdom
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
            SliderEntry("${founder.longName}：", state.wisdomLevel[founder]!!, 0, 5) {
                model.updateGmWisdomLevel(founder, it.toInt())
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

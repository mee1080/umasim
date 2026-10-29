package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiPerformance
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiPresentationResult
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiTeaching
import io.github.mee1080.umasim.web.components.atoms.*
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.components.parts.SliderEntry
import io.github.mee1080.umasim.web.state.ShuntaisaiState
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text

@Composable
fun ShuntaisaiTrainingSetting(model: ViewModel, state: ShuntaisaiState) {
    NestedHideBlock(Scenario.SHUNTAISAI.displayName) {
        H4 { Text("時期") }
        DivFlexCenter({
            style {
                flexWrap(FlexWrap.Wrap)
                rowGap(8.px)
            }
        }) {
            listOf(
                "ジュニア" to 1,
                "クラシック" to 25,
                "シニア" to 49,
                "ファイナルズ" to 73,
            ).forEach { (label, turn) ->
                Label(attrs = {
                    style {
                        marginRight(16.px)
                        display(DisplayStyle.Flex)
                        alignItems(AlignItems.Center)
                        cursor("pointer")
                    }
                }) {
                    MdRadio(state.turn == turn) {
                        onSelect { model.updateShuntaisai { copy(turn = turn) } }
                    }
                    Text(label)
                }
            }
        }

        if (state.period >= 1) {
            MdFilledSelect(
                selection = ShuntaisaiPresentationResult.entries.reversed(),
                selectedItem = state.presentationResult,
                attrs = {
                    label("前回の進捗発表会")
                    style { width(100.percent) }
                },
                onSelect = { model.updateShuntaisai { copy(presentationResult = it) } },
                itemToValue = { it.name },
                itemToDisplayText = { it.displayName },
            )
        }

        H4 { Text("演目") }
        val teachingSelection = ShuntaisaiTeaching.entries.filter { it.period <= minOf(2, state.period) }
        ShuntaisaiPerformance.entries.forEach { performance ->
            val type = performance.type
            Div({
                style {
                    marginTop(8.px)
                    padding(8.px)
                    border(1.px, LineStyle.Solid, Color.lightgray)
                    borderRadius(4.px)
                }
            }) {
                Div({ style { fontWeight("bold") } }) {
                    Text("${performance.displayName}（${type.displayName}）")
                }
                SliderEntry("演目Lv：", state.levels[type] ?: 1, 1, state.maxLevel) {
                    model.updateShuntaisai { copy(levels = levels + (type to it.toInt())) }
                }
                MdFilledSelect(
                    selection = listOf("none") + teachingSelection.map { it.name },
                    selectedItem = state.teachings[type]?.name ?: "none",
                    attrs = {
                        label("教え")
                        style { width(100.percent) }
                    },
                    onSelect = { name ->
                        val teaching = ShuntaisaiTeaching.entries.firstOrNull { it.name == name }
                        model.updateShuntaisai {
                            copy(teachings = if (teaching == null) teachings - type else teachings + (type to teaching))
                        }
                    },
                    itemToValue = { it },
                    itemToDisplayText = { name ->
                        ShuntaisaiTeaching.entries.firstOrNull { it.name == name }?.let {
                            "${it.displayName}：${it.description}"
                        } ?: "なし"
                    },
                )
                val teaching = state.teachings[type]
                if (teaching != null && teaching.period >= 1) {
                    MdFilledSelect(
                        selection = listOf("none") + ShuntaisaiTeaching.entries.filter { it.period < teaching.period }
                            .map { it.name },
                        selectedItem = state.transformedFrom[type]?.name ?: "none",
                        attrs = {
                            label("転成元の教え")
                            style {
                                width(100.percent)
                                marginTop(8.px)
                            }
                        },
                        onSelect = { name ->
                            val from = ShuntaisaiTeaching.entries.firstOrNull { it.name == name }
                            model.updateShuntaisai {
                                copy(
                                    transformedFrom = if (from == null) transformedFrom - type else {
                                        transformedFrom + (type to from)
                                    }
                                )
                            }
                        },
                        itemToValue = { it },
                        itemToDisplayText = { name ->
                            ShuntaisaiTeaching.entries.firstOrNull { it.name == name }?.displayName ?: "なし"
                        },
                    )
                }
            }
        }
    }
}

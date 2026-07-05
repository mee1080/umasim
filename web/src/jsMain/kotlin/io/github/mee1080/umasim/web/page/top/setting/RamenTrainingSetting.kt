package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.ramen.ramenRegionSelection
import io.github.mee1080.umasim.web.components.atoms.MdFilledSelect
import io.github.mee1080.umasim.web.components.atoms.MdRadio
import io.github.mee1080.umasim.web.components.atoms.label
import io.github.mee1080.umasim.web.components.atoms.onSelect
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.components.parts.SliderEntry
import io.github.mee1080.umasim.web.state.RamenState
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text

@Composable
fun RamenTrainingSetting(model: ViewModel, state: RamenState) {
    NestedHideBlock(Scenario.RAMEN.displayName) {
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
                "ファイナルズ" to 73
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
                        onSelect {
                            model.updateRamen {
                                val nextPeriod = (turn - 1) / 24
                                val currentPeriod = (this.turn - 1) / 24
                                val nextRegion = if (nextPeriod == currentPeriod) activeTastingRegion else null
                                copy(turn = turn, activeTastingRegion = nextRegion)
                            }
                        }
                    }
                    Text(label)
                }
            }
        }

        SliderEntry("盛り上がりPt：", state.excitementPt, 0, 5000, 250) {
            model.updateRamen { copy(excitementPt = it.toInt()) }
        }

        H4 { Text("試食会地域") }
        val period = (state.turn - 1) / 24
        val selection = ramenRegionSelection[period]

        // MdFilledSelect requires non-null T. Using a wrapper or just filtering nulls.
        // But we want "None" (null) to be selectable.
        // Let's use RamenRegion enum name and "none" as values.

        MdFilledSelect(
            selection = listOf("none") + selection.map { it.name },
            selectedItem = state.activeTastingRegion?.name ?: "none",
            attrs = {
                label("地域を選択")
                style { width(100.percent) }
            },
            onSelect = { name ->
                val region = selection.firstOrNull { it.name == name }
                model.updateRamen { copy(activeTastingRegion = region) }
            },
            itemToValue = { it },
            itemToDisplayText = { name ->
                if (name == "none") "なし"
                else selection.first { it.name == name }.displayName
            }
        )
    }
}

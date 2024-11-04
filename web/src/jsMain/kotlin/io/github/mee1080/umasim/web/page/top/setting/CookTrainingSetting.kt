package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.web.components.atoms.MdRadioGroup
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.state.CookState
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

@Composable
fun CookTrainingSetting(model: ViewModel, state: CookState) {
    NestedHideBlock(Scenario.COOK.displayName) {
        H4 { Text("お料理ポイント") }
        DivFlexCenter {
            MdRadioGroup(
                selection = WebConstants.cookCookPoint,
                selectedItem = state.cookPoint,
                onSelect = { model.updateCook { copy(cookPoint = it) } },
            )
        }
        H4 { Text("料理") }
        DivFlexCenter {
            MdRadioGroup(
                selection = List(5) { it - 1 },
                selectedItem = state.phase,
                onSelect = { model.updateCook { copy(phase = it) } },
                itemToLabel = { WebConstants.cookPhase[it]!! },
            )
        }
        if (state.phase >= 1) {
            DivFlexCenter {
                val (label, results) = if (state.phase == 3) {
                    "大豊食祭結果" to WebConstants.cookResult2
                } else {
                    "試食会結果" to WebConstants.cookResult1
                }
                Text(label)
                MdRadioGroup(
                    selection = List(3) { it },
                    selectedItem = state.dishRank,
                    onSelect = { model.updateCook { copy(dishRank = it) } },
                    itemToLabel = { results[it]!! },
                )
            }
            DivFlexCenter {
                if (state.phase == 3) {
                    Text("野菜Lv5個数")
                } else {
                    Text("野菜Lv")
                }
                MdRadioGroup(
                    selection = List(6) { it },
                    selectedItem = state.materialLevel,
                    onSelect = { model.updateCook { copy(materialLevel = it) } },
                )
            }
        }
    }
}
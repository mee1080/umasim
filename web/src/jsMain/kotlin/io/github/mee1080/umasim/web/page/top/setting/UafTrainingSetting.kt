package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.uaf.UafGenre
import io.github.mee1080.umasim.web.components.atoms.MdCheckbox
import io.github.mee1080.umasim.web.components.atoms.MdRadioGroup
import io.github.mee1080.umasim.web.components.atoms.onChange
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.state.UafState
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

@Composable
fun UafTrainingSetting(model: ViewModel, state: UafState) {
    NestedHideBlock(Scenario.UAF.displayName) {
        H4 { Text("大会ボーナス") }
        DivFlexCenter {
            Text(UafGenre.Blue.longDisplayName)
            MdRadioGroup(
                selection = WebConstants.uafFestivalBonus,
                selectedItem = state.blueFestivalBonus,
                onSelect = { model.updateUaf { copy(blueFestivalBonus = it) } },
                itemToLabel = { WebConstants.uafFestivalBonusValue[it]!! },
            )
        }
        DivFlexCenter {
            Text(UafGenre.Red.longDisplayName)
            MdRadioGroup(
                selection = WebConstants.uafFestivalBonus,
                selectedItem = state.redFestivalBonus,
                onSelect = { model.updateUaf { copy(redFestivalBonus = it) } },
                itemToLabel = { WebConstants.uafFestivalBonusValue[it]!! },
            )
        }
        DivFlexCenter {
            Text(UafGenre.Yellow.longDisplayName)
            MdRadioGroup(
                selection = WebConstants.uafFestivalBonus,
                selectedItem = state.yellowFestivalBonus,
                onSelect = { model.updateUaf { copy(yellowFestivalBonus = it) } },
                itemToLabel = { WebConstants.uafFestivalBonusValue[it]!! },
            )
        }
        H4 { Text("ヒートアップ効果") }
        DivFlexCenter {
            MdCheckbox(UafGenre.Blue.longDisplayName, state.heatUpBlue) {
                onChange { model.updateUaf { copy(heatUpBlue = it) } }
            }
            MdCheckbox(UafGenre.Red.longDisplayName, state.heatUpRed) {
                onChange { model.updateUaf { copy(heatUpRed = it) } }
            }
            MdCheckbox(UafGenre.Yellow.longDisplayName, state.heatUpYellow) {
                onChange { model.updateUaf { copy(heatUpYellow = it) } }
            }
        }
    }
}
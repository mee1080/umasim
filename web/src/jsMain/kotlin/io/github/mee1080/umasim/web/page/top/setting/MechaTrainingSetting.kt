package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.web.components.atoms.MdCheckbox
import io.github.mee1080.umasim.web.components.atoms.MdOutlinedNumberTextField
import io.github.mee1080.umasim.web.components.atoms.MdRadioGroup
import io.github.mee1080.umasim.web.components.atoms.onChange
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.components.parts.SliderEntry
import io.github.mee1080.umasim.web.state.MechaPhase
import io.github.mee1080.umasim.web.state.MechaState
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.columnGap
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

@Composable
fun MechaTrainingSetting(model: ViewModel, state: MechaState) {
    NestedHideBlock(Scenario.MECHA.displayName) {
        H4 { Text("時期") }
        DivFlexCenter {
            MdRadioGroup(
                selection = MechaPhase.entries,
                selectedItem = state.phase,
                onSelect = { model.updateMecha { copy(phase = it) } },
                itemToLabel = { it.label },
            )
        }

        H4 { Text("チューニング") }
        SliderEntry("頭部：賢さ研究", state.chipLevelHead1, 0, 5) {
            model.updateMecha { copy(chipLevelHead1 = it.toInt()) }
        }
        SliderEntry("頭部：スキルヒント", state.chipLevelHead2, 0, 5) {
            model.updateMecha { copy(chipLevelHead2 = it.toInt()) }
        }
        SliderEntry("頭部：得意率アップ", state.chipLevelHead3, 0, 5) {
            model.updateMecha { copy(chipLevelHead3 = it.toInt()) }
        }
        SliderEntry("胸部：スタミナ研究", state.chipLevelBody1, 0, 5) {
            model.updateMecha { copy(chipLevelBody1 = it.toInt()) }
        }
        SliderEntry("胸部：根性研究", state.chipLevelBody2, 0, 5) {
            model.updateMecha { copy(chipLevelBody2 = it.toInt()) }
        }
        SliderEntry("胸部：友情強化", state.chipLevelBody3, 0, 5) {
            model.updateMecha { copy(chipLevelBody3 = it.toInt()) }
        }
        SliderEntry("脚部：スピード研究", state.chipLevelLeg1, 0, 5) {
            model.updateMecha { copy(chipLevelLeg1 = it.toInt()) }
        }
        SliderEntry("脚部：パワー研究", state.chipLevelLeg2, 0, 5) {
            model.updateMecha { copy(chipLevelLeg2 = it.toInt()) }
        }
        SliderEntry("脚部：スキルPt", state.chipLevelLeg3, 0, 5) {
            model.updateMecha { copy(chipLevelLeg3 = it.toInt()) }
        }

        H4 { Text("研究Lv") }
        DivFlexCenter({ style { columnGap(8.px) } }) {
            Text("スピード")
            MdOutlinedNumberTextField(state.learningSpeed) {
                onInput { if (it != null) model.updateMecha { copy(learningSpeed = it.toInt()) } }
            }
        }
        DivFlexCenter({ style { columnGap(8.px) } }) {
            Text("スタミナ")
            MdOutlinedNumberTextField(state.learningStamina) {
                onInput { if (it != null) model.updateMecha { copy(learningStamina = it.toInt()) } }
            }
        }
        DivFlexCenter({ style { columnGap(8.px) } }) {
            Text("パワー")
            MdOutlinedNumberTextField(state.learningPower) {
                onInput { if (it != null) model.updateMecha { copy(learningPower = it.toInt()) } }
            }
        }
        DivFlexCenter({ style { columnGap(8.px) } }) {
            Text("根性")
            MdOutlinedNumberTextField(state.learningGuts) {
                onInput { if (it != null) model.updateMecha { copy(learningGuts = it.toInt()) } }
            }
        }
        DivFlexCenter({ style { columnGap(8.px) } }) {
            Text("賢さ")
            MdOutlinedNumberTextField(state.learningWisdom) {
                onInput { if (it != null) model.updateMecha { copy(learningWisdom = it.toInt()) } }
            }
        }

        DivFlexCenter {
            MdCheckbox("メカギア", state.gear) {
                onChange { model.updateMecha { copy(gear = it) } }
            }
            MdCheckbox("オーバードライブ", state.overdrive) {
                onChange { model.updateMecha { copy(overdrive = it) } }
            }
        }
    }
}
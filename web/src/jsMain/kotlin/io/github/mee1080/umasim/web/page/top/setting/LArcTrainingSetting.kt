package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.atoms.MdCheckbox
import io.github.mee1080.umasim.web.components.atoms.MdTextButton
import io.github.mee1080.umasim.web.components.atoms.onChange
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.components.parts.SliderEntry
import io.github.mee1080.umasim.web.state.LArcState
import io.github.mee1080.umasim.web.vm.ViewModel

@Composable
fun LArcTrainingSetting(model: ViewModel, state: LArcState) {
    NestedHideBlock("プロジェクトL'Arc") {
        SliderEntry("期待度：", state.expectations, 0, 200) {
            model.updateLArc { copy(expectations = it.toInt()) }
        }
        SliderEntry("海外洋芝適性：", state.overseasTurfAptitude, 0, 3) {
            model.updateLArc { copy(overseasTurfAptitude = it.toInt()) }
        }
        SliderEntry("ロンシャン適性：", state.longchampAptitude, 0, 3) {
            model.updateLArc { copy(longchampAptitude = it.toInt()) }
        }
        SliderEntry("生活リズム：", state.lifeRhythm, 0, 3) {
            model.updateLArc { copy(lifeRhythm = it.toInt()) }
        }
        SliderEntry("栄養管理：", state.nutritionManagement, 0, 3) {
            model.updateLArc { copy(nutritionManagement = it.toInt()) }
        }
        SliderEntry("フランス語力：", state.frenchSkill, 0, 3) {
            model.updateLArc { copy(frenchSkill = it.toInt()) }
        }
        SliderEntry("海外遠征：", state.overseasExpedition, 0, 3) {
            model.updateLArc { copy(overseasExpedition = it.toInt()) }
        }
        SliderEntry("強心臓：", state.strongHeart, 0, 3) {
            model.updateLArc { copy(strongHeart = it.toInt()) }
        }
        SliderEntry("精神力：", state.mentalStrength, 0, 3) {
            model.updateLArc { copy(mentalStrength = it.toInt()) }
        }
        SliderEntry("L’Arcの希望：", state.hopeOfLArc, 0, 3) {
            model.updateLArc { copy(hopeOfLArc = it.toInt()) }
        }
        DivFlexCenter {
            MdTextButton("海外適性すべて0") { onClick { model.setAllAptitude(0) } }
            MdTextButton("海外適性すべて1") { onClick { model.setAllAptitude(1) } }
            MdTextButton("海外適性すべて2") { onClick { model.setAllAptitude(2) } }
            MdTextButton("海外適性すべて3") { onClick { model.setAllAptitude(3) } }
        }
        DivFlexCenter {
            MdCheckbox("海外遠征中", state.overseas) {
                onChange { model.updateLArc { copy(overseas = it) } }
            }
        }
    }
}
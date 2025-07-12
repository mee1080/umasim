package io.github.mee1080.umasim.web.page.top.result

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.web.components.atoms.MdOutlinedSelect
import io.github.mee1080.umasim.web.components.atoms.MdRadioGroup
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.components.parts.SliderEntry
import io.github.mee1080.umasim.web.state.*
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import io.github.mee1080.utility.roundToString
import org.jetbrains.compose.web.css.columnGap
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*

@Composable
fun MujintoTrainingInfo(model: ViewModel, state: State) {
    HideBlock("島トレ", true) {
        MujintoTrainingSetting(model, state)
        MujintoTrainingResult(state.mujintoState)
    }
}

@Composable
fun MujintoTrainingSetting(model: ViewModel, state: State) {
    val mujintoState = state.mujintoState
    H3 { Text("施設レベル/ゲスト人数") }
    mujintoFacilityType.forEach { type ->
        H4 { Text(type.facilityName) }
        DivFlexCenter {
            SliderEntry(
                label = "施設Lv：",
                value = mujintoState.facilityLevel[type] ?: 0,
                min = 0,
                max = if (type == StatusType.FRIEND) 3 else 5,
            ) {
                model.updateMujinto { updateFacilityLevel(type, it.toInt()) }
            }
            if (type != StatusType.FRIEND) {
                MdRadioGroup(
                    selection = listOf(false, true),
                    selectedItem = mujintoState.facilityJukuren[type] ?: false,
                    onSelect = { model.updateMujinto { updateFacilityJukuren(type, it) } },
                    itemToLabel = { if (it) "熟練技巧" else "本能開放" },
                )
            }
        }
        SliderEntry("ゲスト配置数：", mujintoState.guestCount[type] ?: 0, 0, 5) {
            model.updateMujinto { updateGuestCount(type, it.toInt()) }
        }
    }
    H3 { Text("サポカ配置") }
    state.supportSelectionList.forEachIndexed { index, selection ->
        selection.card?.let { card ->
            DivFlexCenter({
                style {
                    columnGap(12.px)
                }
            }) {
                MdOutlinedSelect(
                    selection = mujintoPositionSelection,
                    selectedItem = mujintoState.supportPosition.getOrElse(index) { StatusType.NONE },
                    onSelect = { model.updateMujinto { updatePosition(index, it) } },
                    itemToDisplayText = { it.facilityName }
                )
                Text("${card.name}：")
            }
        }
    }
}

@Composable
fun MujintoTrainingResult(state: MujintoState) {
    H3 { Text("島トレ上昇値") }
    Div({ style { marginTop(16.px) } }) {
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th({ style { property("border", "none") } }) { }
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
                Th { Text("スキルPt") }
                Th { Text("体力") }
                Th { Text("5ステ合計") }
                Th { Text("5ステ+SP") }
            }
            Tr {
                Th { Text("基本") }
                Td { Text(state.baseTrainingResult.speed.toString()) }
                Td { Text(state.baseTrainingResult.stamina.toString()) }
                Td { Text(state.baseTrainingResult.power.toString()) }
                Td { Text(state.baseTrainingResult.guts.toString()) }
                Td { Text(state.baseTrainingResult.wisdom.toString()) }
                Td { Text(state.baseTrainingResult.skillPt.toString()) }
                Td { Text(state.baseTrainingResult.hp.toString()) }
                Td { Text(state.baseTrainingResult.statusTotal.toString()) }
                Td { Text(state.baseTrainingResult.totalPlusSkillPt.toString()) }
            }
            Tr {
                Th { Text("追加") }
                Td { Text(state.additionalTrainingResult.speed.toString()) }
                Td { Text(state.additionalTrainingResult.stamina.toString()) }
                Td { Text(state.additionalTrainingResult.power.toString()) }
                Td { Text(state.additionalTrainingResult.guts.toString()) }
                Td { Text(state.additionalTrainingResult.wisdom.toString()) }
                Td { Text(state.additionalTrainingResult.skillPt.toString()) }
                Td { Text(state.additionalTrainingResult.hp.toString()) }
                Td { Text(state.additionalTrainingResult.statusTotal.toString()) }
                Td { Text(state.additionalTrainingResult.totalPlusSkillPt.toString()) }
            }
            Tr {
                Th { Text("合計") }
                val totalStatus = state.baseTrainingResult + state.additionalTrainingResult
                Td { Text(totalStatus.speed.toString()) }
                Td { Text(totalStatus.stamina.toString()) }
                Td { Text(totalStatus.power.toString()) }
                Td { Text(totalStatus.guts.toString()) }
                Td { Text(totalStatus.wisdom.toString()) }
                Td { Text(totalStatus.skillPt.toString()) }
                Td { Text(totalStatus.hp.toString()) }
                Td { Text(totalStatus.statusTotal.toString()) }
                Td { Text(totalStatus.totalPlusSkillPt.toString()) }
            }
        }
    }
    Div({ style { marginTop(16.px) } }) {
        H3 { Text("切り捨て前") }
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
                Th { Text("スキルPt") }
            }
            Tr {
                Td { Text(state.rawTrainingResult.speed.roundToString(4)) }
                Td { Text(state.rawTrainingResult.stamina.roundToString(4)) }
                Td { Text(state.rawTrainingResult.power.roundToString(4)) }
                Td { Text(state.rawTrainingResult.guts.roundToString(4)) }
                Td { Text(state.rawTrainingResult.wisdom.roundToString(4)) }
                Td { Text(state.rawTrainingResult.skillPt.roundToString(4)) }
            }
        }
    }
}
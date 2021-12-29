package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.LabeledRadioGroup
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.unsetWidth
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.dom.*
import kotlin.math.roundToInt

@Composable
fun TrainingInfo(model: ViewModel, state: State) {
    H2 { Text("トレーニング上昇量") }
    LabeledRadioGroup("motivation", "やる気：", WebConstants.motivationList, state.motivation, model::updateMotivation)
    LabeledRadioGroup(
        "training",
        "種別　：",
        WebConstants.displayTrainingTypeList,
        state.selectedTrainingType,
        model::updateTrainingType
    )
    LabeledRadioGroup("level", "レベル：", WebConstants.trainingLevelList, state.trainingLevel, model::updateTrainingLevel)
    Div {
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
                Th { Text("スキルPt") }
                Th { Text("体力") }
                Th { Text("5ステ合計") }
            }
            Tr {
                Td { Text(state.trainingResult.speed.toString()) }
                Td { Text(state.trainingResult.stamina.toString()) }
                Td { Text(state.trainingResult.power.toString()) }
                Td { Text(state.trainingResult.guts.toString()) }
                Td { Text(state.trainingResult.wisdom.toString()) }
                Td { Text(state.trainingResult.skillPt.toString()) }
                Td { Text(state.trainingResult.hp.toString()) }
                Td { Text(state.trainingResult.statusTotal.toString()) }
            }
        }
    }
//        LabeledCheckbox("trainingParamTest", "トレーニング設定調査", model.trainingParamTest != null) {
//            model.updateTrainingParamTest(it)
//        }
//        model.trainingParamTest?.let {
//            TrainingParamTest(it)
//        }
    if (state.trainingImpact.isNotEmpty()) {
        H3 { Text("サポカ影響度") }
        Div {
            Table({ classes(AppStyle.table) }) {
                Tr {
                    Th({
                        style {
                            property("border", "none")
                        }
                        unsetWidth()
                    }) { }
                    Th { Text("スピード") }
                    Th { Text("スタミナ") }
                    Th { Text("パワー") }
                    Th { Text("根性") }
                    Th { Text("賢さ") }
                    Th { Text("スキルPt") }
                    Th { Text("体力") }
                    Th { Text("5ステ合計") }
                }
                state.trainingImpact.forEach { (name, status) ->
                    Tr {
                        Td({
                            unsetWidth()
                        }) { Text(name) }
                        Td { Text(status.speed.toString()) }
                        Td { Text(status.stamina.toString()) }
                        Td { Text(status.power.toString()) }
                        Td { Text(status.guts.toString()) }
                        Td { Text(status.wisdom.toString()) }
                        Td { Text(status.skillPt.toString()) }
                        Td { Text(status.hp.toString()) }
                        Td { Text(status.statusTotal.toString()) }
                    }
                }
            }
        }
        Div { Text("※計算式： 上昇量 - 対象カードが練習不参加時の上昇量") }
    }
    H3 { Text("期待値（練習配置率を考慮）") }
    Div {
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
                Th { Text("スキルPt") }
                Th { Text("体力") }
                Th { Text("5ステ合計") }
            }
            Tr {
                Td { Text(((state.expectedResult.speed * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.stamina * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.power * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.guts * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.wisdom * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.skillPt * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.hp * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.statusTotal * 100).roundToInt() / 100.0).toString()) }
            }
        }
        Div { Text("※練習参加チェックボックスを無視して、練習配置率に応じて参加/不参加を決めた場合の期待値") }
    }
}
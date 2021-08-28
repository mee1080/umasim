package io.github.mee1080.umasim.web.components

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.TrainingParamTestModel
import org.jetbrains.compose.web.dom.*

@Composable
fun TrainingParamTest(model: TrainingParamTestModel) {
    Table({ classes(AppStyle.table) }) {
        Tr {
            Th({
                style {
                    property("border", "none")
                    property("width", "unset")
                }
            }) { }
            Th { Text("スピード") }
            Th { Text("スタミナ") }
            Th { Text("パワー") }
            Th { Text("根性") }
            Th { Text("賢さ") }
            Th { Text("スキルPt") }
        }
        Tr {
            Td { Text("設定") }
            Td { TextInput(model.speed.toString()) { onInput { model.speed = it.value.toIntOrNull() ?: 0 } } }
            Td { TextInput(model.stamina.toString()) { onInput { model.stamina = it.value.toIntOrNull() ?: 0 } } }
            Td { TextInput(model.power.toString()) { onInput { model.power = it.value.toIntOrNull() ?: 0 } } }
            Td { TextInput(model.guts.toString()) { onInput { model.guts = it.value.toIntOrNull() ?: 0 } } }
            Td { TextInput(model.wisdom.toString()) { onInput { model.wisdom = it.value.toIntOrNull() ?: 0 } } }
            Td { TextInput(model.skillPt.toString()) { onInput { model.skillPt = it.value.toIntOrNull() ?: 0 } } }
        }
        Tr {
            Td { Text("結果") }
            Td { Text(model.result.speed.toString()) }
            Td { Text(model.result.stamina.toString()) }
            Td { Text(model.result.power.toString()) }
            Td { Text(model.result.guts.toString()) }
            Td { Text(model.result.wisdom.toString()) }
            Td { Text(model.result.skillPt.toString()) }
        }
    }
}
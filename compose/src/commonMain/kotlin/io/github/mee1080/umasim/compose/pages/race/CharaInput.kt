package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.IntTextField
import io.github.mee1080.umasim.compose.common.atoms.SelectBox
import io.github.mee1080.umasim.race.calc2.UmaStatus
import io.github.mee1080.umasim.race.data.Style
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.setStatus
import io.github.mee1080.umasim.store.operation.setStyle

@Composable
fun CharaInput(state: AppState, dispatch: OperationDispatcher<AppState>) {
    val chara = state.setting.umaStatus
    CharaStatus(chara, dispatch)
}

private val styleList = listOf(Style.NIGE, Style.SEN, Style.SASI, Style.OI)

@Composable
private fun CharaStatus(chara: UmaStatus, dispatch: OperationDispatcher<AppState>) {
    Row {
        StatusTextField(chara.speed, "スピード") { dispatch(setStatus(speed = it)) }
        StatusTextField(chara.stamina, "スタミナ") { dispatch(setStatus(stamina = it)) }
        StatusTextField(chara.power, "パワー") { dispatch(setStatus(power = it)) }
        StatusTextField(chara.guts, "根性") { dispatch(setStatus(guts = it)) }
        StatusTextField(chara.wisdom, "賢さ") { dispatch(setStatus(wisdom = it)) }
    }
    Row {
        SelectBox(
            styleList, chara.style,
            onSelect = { dispatch(setStyle(it)) },
            modifier = Modifier.width(196.dp),
            label = { Text("作戦") },
            itemToString = { it.text },
        )
    }
    Row {
        Text("スピード：${chara.speed}")
        Text("スタミナ：${chara.stamina}")
        Text("パワー：${chara.power}")
        Text("根性：${chara.guts}")
        Text("賢さ：${chara.wisdom}")
        Text("作戦：${chara.style.text}")
    }
}

private val statusTextFieldModifier = Modifier.width(128.dp).padding(16.dp)

@Composable
private fun StatusTextField(value: Int, labelText: String, onValueChange: (Int) -> Unit) {
    IntTextField(
        value = value,
        modifier = statusTextFieldModifier,
        label = { Text(labelText) },
        onValueChange = onValueChange
    )
}
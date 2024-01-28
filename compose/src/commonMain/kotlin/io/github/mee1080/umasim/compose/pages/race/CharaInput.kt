package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.IntTextField
import io.github.mee1080.umasim.compose.common.atoms.SelectBox
import io.github.mee1080.umasim.compose.common.parts.HideBlock
import io.github.mee1080.umasim.race.calc2.UmaStatus
import io.github.mee1080.umasim.race.data.Condition
import io.github.mee1080.umasim.race.data.FitRank
import io.github.mee1080.umasim.race.data.Style
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.setCondition
import io.github.mee1080.umasim.store.operation.setFit
import io.github.mee1080.umasim.store.operation.setStatus
import io.github.mee1080.umasim.store.operation.setStyle

@Composable
fun CharaInput(state: AppState, dispatch: OperationDispatcher<AppState>) {
    val chara = state.setting.umaStatus
    HideBlock(
        header = { Text("ステータス") },
        initialOpen = true,
        headerClosed = { Text("ステータス：${chara.speed}/${chara.stamina}/${chara.power}/${chara.guts}/${chara.wisdom} ${chara.style.text} ${chara.surfaceFit}/${chara.distanceFit}/${chara.styleFit} ${chara.condition.label}") },
    ) {
        CharaStatus(chara, dispatch)
    }
}

private val styleList = listOf(Style.NIGE, Style.SEN, Style.SASI, Style.OI)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CharaStatus(chara: UmaStatus, dispatch: OperationDispatcher<AppState>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FlowRow {
            StatusTextField(chara.speed, "スピード") { dispatch(setStatus(speed = it)) }
            StatusTextField(chara.stamina, "スタミナ") { dispatch(setStatus(stamina = it)) }
            StatusTextField(chara.power, "パワー") { dispatch(setStatus(power = it)) }
            StatusTextField(chara.guts, "根性") { dispatch(setStatus(guts = it)) }
            StatusTextField(chara.wisdom, "賢さ") { dispatch(setStatus(wisdom = it)) }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SelectBox(
                styleList, chara.style,
                onSelect = { dispatch(setStyle(it)) },
                modifier = Modifier.width(160.dp),
                label = { Text("作戦") },
                itemToString = { it.text },
            )
            SelectBox(
                FitRank.entries, chara.surfaceFit,
                onSelect = { dispatch(setFit(surface = it)) },
                modifier = Modifier.width(128.dp),
                label = { Text("バ場適性") },
                itemToString = { it.name },
            )
            SelectBox(
                FitRank.entries, chara.distanceFit,
                onSelect = { dispatch(setFit(distance = it)) },
                modifier = Modifier.width(128.dp),
                label = { Text("距離適性") },
                itemToString = { it.name },
            )
            SelectBox(
                FitRank.entries, chara.styleFit,
                onSelect = { dispatch(setFit(style = it)) },
                modifier = Modifier.width(128.dp),
                label = { Text("脚質適性") },
                itemToString = { it.name },
            )
            SelectBox(
                Condition.entries, chara.condition,
                onSelect = { dispatch(setCondition(it)) },
                modifier = Modifier.width(190.dp),
                label = { Text("調子") },
                itemToString = { it.label },
            )
        }
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
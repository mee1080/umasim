package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.IntTextField
import io.github.mee1080.umasim.compose.common.atoms.SelectBox
import io.github.mee1080.umasim.compose.common.parts.HideBlock
import io.github.mee1080.umasim.race.data.RandomPosition
import io.github.mee1080.umasim.race.data.SkillActivateAdjustment
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.setRandomPosition
import io.github.mee1080.umasim.store.operation.setSimulationCount
import io.github.mee1080.umasim.store.operation.setSkillActivateAdjustment

@Composable
fun SettingInput(state: AppState, dispatch: OperationDispatcher<AppState>) {
    HideBlock(
        header = { Text("その他設定") },
        initialOpen = true,
    ) {
        OtherSetting(state, dispatch)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OtherSetting(state: AppState, dispatch: OperationDispatcher<AppState>) {
    val setting = state.setting
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        IntTextField(
            value = state.simulationCount,
            modifier = Modifier.width(128.dp),
            label = { Text("回数") },
            onValueChange = { dispatch(setSimulationCount(it)) }
        )
        SelectBox(
            SkillActivateAdjustment.entries, setting.skillActivateAdjustment,
            onSelect = { dispatch(setSkillActivateAdjustment(it)) },
            modifier = Modifier.width(256.dp),
            label = { Text("スキル発動率修正") },
            itemToString = { it.label },
        )
        SelectBox(
            RandomPosition.entries, setting.randomPosition,
            onSelect = { dispatch(setRandomPosition(it)) },
            modifier = Modifier.width(256.dp),
            label = { Text("ランダム区間") },
            itemToString = { it.label },
        )
    }
}
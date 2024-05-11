package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.IntTextField
import io.github.mee1080.umasim.compose.common.atoms.SelectBox
import io.github.mee1080.umasim.compose.common.parts.HideBlock
import io.github.mee1080.umasim.race.data.RandomPosition
import io.github.mee1080.umasim.race.data.SkillActivateAdjustment
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.*

@Composable
fun SettingInput(state: AppState, dispatch: OperationDispatcher<AppState>) {
    HideBlock(
        header = { Text("その他設定") },
        initialOpen = true,
    ) {
        OtherSetting(state, dispatch)
    }
}

private val popularitySelection = List(18) { it + 1 }

private val gateNumberSelection = List(21) { it - 2 }

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OtherSetting(state: AppState, dispatch: OperationDispatcher<AppState>) {
    val setting by derivedStateOf { state.setting }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SelectBox(
                popularitySelection, setting.popularity,
                onSelect = { dispatch(setPopularity(it)) },
                modifier = Modifier.width(128.dp),
                label = { Text("人気") },
            )
            SelectBox(
                gateNumberSelection, setting.gateNumber,
                onSelect = { dispatch(setGateNumber(it)) },
                modifier = Modifier.width(256.dp),
                label = { Text("ゲート番号") },
                itemToString = {
                    when (it) {
                        0 -> "ランダム"
                        -1 -> "内枠"
                        -2 -> "外枠"
                        else -> it.toString()
                    }
                },
            )
        }
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
}
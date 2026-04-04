package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.parts.HideBlock
import io.github.mee1080.umasim.compose.common.parts.NumberInput
import io.github.mee1080.umasim.race.calc2.DebuffType
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.setDebuffCount

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DebuffInput(state: AppState, dispatch: OperationDispatcher<AppState>) {
    val debuffSetting = state.setting.debuffCounts
    HideBlock(
        header = { Text("デバフ") },
        initialOpen = false,
        headerClosed = {
            val summary = debuffSetting.filterValues { it > 0 }
                .map { "${it.key.label}x${it.value}" }
                .joinToString(", ")
            Text("デバフ：$summary")
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DebuffType.entries.filter {
                it.distanceType == null || it.distanceType == state.setting.trackDetail.distanceType
            }.forEach { type ->
                Row {
                    Text(type.label)
                    NumberInput(
                        value = debuffSetting[type] ?: 0,
                        onValueChange = { dispatch(setDebuffCount(type, it)) },
                        min = 0,
                        max = 17,
                    )
                }
            }
        }
    }
}

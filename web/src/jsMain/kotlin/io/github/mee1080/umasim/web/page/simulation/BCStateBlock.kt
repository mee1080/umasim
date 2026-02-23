package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.bc.BCStatus
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun BCStateBlock(bcStatus: BCStatus) {
    Div {
        val text = bcStatus.teamMember.joinToString(", ") {
            "${it.charaName} ${it.memberRankString} ${it.dreamGauge}/3"
        }
        Text(text)
    }
    Div {
        val parameter = bcStatus.teamParameter.entries.joinToString(", ") { "${it.key.displayName}: ${it.value}" }
        Text("DREAMSトレーニング ${bcStatus.dreamsTrainingCount}回 / DP ${bcStatus.dreamsPoint} / $parameter")
    }
}

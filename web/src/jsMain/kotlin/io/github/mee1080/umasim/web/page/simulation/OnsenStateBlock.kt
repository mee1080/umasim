package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.onsen.OnsenCalculator
import io.github.mee1080.umasim.scenario.onsen.OnsenStatus
import io.github.mee1080.umasim.scenario.onsen.StratumType
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.columnGap
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun OnsenStateBlock(onsenStatus: OnsenStatus, status: Status) {
    onsenStatus.selectedGensen?.let { gensen ->
        Div({
            style {
                display(DisplayStyle.Flex)
                columnGap(16.px)
            }
        }) {
            val digPower = StratumType.entries.joinToString(" / ") {
                "${it.displayName}:${OnsenCalculator.calcDigPower(onsenStatus, status, it)}"
            }
            Div { Text("掘削力：${digPower}") }
            val equipment = onsenStatus.equipmentLevel.entries.joinToString(" / ") {
                "${it.key.displayName}:${it.value}"
            }
            Div { Text("装備：${equipment}") }
            val stratum = onsenStatus.currentStratum?.first?.displayName ?: "完了"
            Div { Text("掘削中：${gensen.name} ($stratum ${onsenStatus.digProgress}/${gensen.totalProgress})") }
        }
    }
    Div {
        Text("掘削済み：${onsenStatus.excavatedGensen.joinToString(", ") { it.name }}")
    }
    Div {
        val onsenActive = if (onsenStatus.onsenActiveTurn > 0) "【入浴中 ${onsenStatus.onsenActiveTurn}ターン】" else ""
        val superRecovery = if (onsenStatus.superRecoveryAvailable) " (超回復)" else ""
        Text("${onsenActive}入浴券：${onsenStatus.onsenTicket}$superRecovery")
    }
}

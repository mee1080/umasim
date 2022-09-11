package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.web.round
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*

@Composable
fun ExpectedStatusDisplay(model: ViewModel, state: State) {
    val status = state.expectedState.status
    Div {
        Button({
            onClick { model.calculateExpected() }
        }) {
            Text("計算")
        }
    }
    Div({ style { marginTop(16.px) } }) {
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
                Th { Text("スキルPt") }
                Th { Text("体力") }
                if (state.scenario == Scenario.GRAND_LIVE) {
                    Th { Text("パフォ") }
                }
                Th { Text("5ステ合計") }
                Th { Text("+スキルPt") }
                if (state.scenario == Scenario.GRAND_LIVE) {
                    Th { Text("+パフォ/2") }
                }
            }
            Tr {
                Td { Text(status.speed.round().toString()) }
                Td { Text(status.stamina.round().toString()) }
                Td { Text(status.power.round().toString()) }
                Td { Text(status.guts.round().toString()) }
                Td { Text(status.wisdom.round().toString()) }
                Td { Text(status.skillPt.round().toString()) }
                Td { Text(status.hp.round().toString()) }
                if (state.scenario == Scenario.GRAND_LIVE) {
                    Td { Text(status.performance?.totalValue?.round().toString()) }
                }
                Td { Text(status.statusTotal.round().toString()) }
                Td { Text((status.totalPlusSkillPt).round().toString()) }
                if (state.scenario == Scenario.GRAND_LIVE) {
                    Td { Text(status.totalPlusSkillPtPerformance.round().toString()) }
                }
            }
        }
    }
}
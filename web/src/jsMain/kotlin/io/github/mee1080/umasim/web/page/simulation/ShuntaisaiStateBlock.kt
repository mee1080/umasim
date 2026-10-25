package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiPerformance
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiStatus
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiTeaching
import io.github.mee1080.umasim.scenario.shuntaisai.shuntaisaiMaxGauge
import io.github.mee1080.umasim.simulation2.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun ShuntaisaiStateBlock(shuntaisaiStatus: ShuntaisaiStatus) {
    trainingType.forEach { type ->
        val performance = ShuntaisaiPerformance.of(type) ?: return@forEach
        Div {
            val text = buildString {
                append("${performance.displayName}: Lv${shuntaisaiStatus.level(type)}/${shuntaisaiStatus.maxLevel}")
                append(", ゲージ ${shuntaisaiStatus.gauge(type)}/$shuntaisaiMaxGauge")
                if (shuntaisaiStatus.isPractice(type)) append("【演目稽古】")
                shuntaisaiStatus.teachings[type]?.let { append(", 教え: ${it.displayName}") }
            }
            Text(text)
        }
    }
    if (shuntaisaiStatus.presentationResults.isNotEmpty()) {
        Div {
            Text("進捗発表会: ${shuntaisaiStatus.presentationResults.joinToString("/") { it.displayName }}")
        }
    }
}

@Composable
fun ShuntaisaiParamInfo(state: SimulationState, action: Action) {
    val shuntaisaiStatus = state.shuntaisaiStatus ?: return
    val param = action.candidates.firstOrNull { it.first.success }
        ?.first?.scenarioActionParam as? ShuntaisaiActionParam ?: return
    param.practice?.let { type ->
        Div {
            Text("演目稽古: ${ShuntaisaiPerformance.of(type)?.displayName} Lv${shuntaisaiStatus.level(type)}")
        }
    }
    if (param.gaugeUp.isNotEmpty()) {
        Div {
            val list = param.gaugeUp.entries.map { (type, value) ->
                val after = (shuntaisaiStatus.gauge(type) + value).coerceAtMost(shuntaisaiMaxGauge)
                "${ShuntaisaiPerformance.of(type)?.displayName}+$value ($after)"
            }
            Text(list.joinToString(", "))
        }
    }
}

@Composable
fun ShuntaisaiTeachingInfo(teaching: ShuntaisaiTeaching, consumed: ShuntaisaiTeaching? = null) {
    Div {
        Text("${teaching.displayName}の教え：${teaching.description}")
    }
    if (consumed != null) {
        Div {
            Text("転成元：${consumed.displayName}の教え（${consumed.description}）")
        }
    }
}

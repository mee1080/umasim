package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiPerformance
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiStatus
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiTeaching
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiTeachingSlot
import io.github.mee1080.umasim.scenario.shuntaisai.shuntaisaiMaxGauge
import io.github.mee1080.umasim.scenario.shuntaisai.shuntaisaiPerformance
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.web.components.atoms.MdClass
import io.github.mee1080.umasim.web.components.atoms.MdSysColor
import io.github.mee1080.umasim.web.components.lib.gridTemplateColumns
import io.github.mee1080.umasim.web.components.lib.repeatGrid
import io.github.mee1080.umasim.web.style.AppStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

/**
 * 演目Lvとゲージ、教えを、ステータス表と同じ列幅で1行ずつ表示する
 */
@Composable
fun ShuntaisaiStateBlock(shuntaisaiStatus: ShuntaisaiStatus) {
    Div({
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            columnGap(4.px)
        }
    }) {
        Div({
            classes(AppStyle.statusTable)
            style { gridTemplateColumns(repeatGrid(5, 64.px)) }
        }) {
            trainingType.forEach { type ->
                ShuntaisaiPerformanceCell(shuntaisaiStatus, type)
            }
            ShuntaisaiTeachingCells(shuntaisaiStatus.teachings)
        }
        Div({ classes(MdClass.labelSmall) }) {
            Text("上限Lv${shuntaisaiStatus.maxLevel}")
        }
    }
    if (shuntaisaiStatus.reassigningTeachings.isNotEmpty()) {
        Div {
            Div { Text("付け替え前の教え：") }
            Div({
                classes(AppStyle.statusTable)
                style { gridTemplateColumns(repeatGrid(5, 64.px)) }
            }) {
                ShuntaisaiTeachingCells(shuntaisaiStatus.reassigningTeachings)
            }
        }
    }
    if (shuntaisaiStatus.presentationResults.isNotEmpty()) {
        Div {
            Text("進捗発表会: ${shuntaisaiStatus.presentationResults.joinToString("/") { it.displayName }}")
        }
    }
}

@Composable
private fun ShuntaisaiPerformanceCell(shuntaisaiStatus: ShuntaisaiStatus, type: StatusType) {
    val practice = shuntaisaiStatus.isPractice(type)
    Div({
        title(ShuntaisaiPerformance.of(type)?.displayName ?: "")
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
            columnGap(2.px)
            if (practice) {
                backgroundColor(MdSysColor.tertiaryContainer.value)
                color(MdSysColor.onTertiaryContainer.value)
                fontWeight("bold")
            }
        }
    }) {
        Text("Lv${shuntaisaiStatus.level(type)}")
        val gauge = shuntaisaiStatus.gauge(type)
        repeat(shuntaisaiMaxGauge) { index ->
            Div({
                style {
                    width(4.px)
                    height(10.px)
                    border(1.px, LineStyle.Solid, MdSysColor.outline.value)
                    if (index < gauge) backgroundColor(MdSysColor.tertiary.value)
                }
            })
        }
    }
}

@Composable
private fun ShuntaisaiTeachingCells(teachings: Map<StatusType, ShuntaisaiTeachingSlot>) {
    trainingType.forEach { type ->
        Div({
            classes(MdClass.labelSmall)
            style { textAlign("center") }
        }) {
            Text(teachings[type]?.displayName ?: "-")
        }
    }
}

/**
 * トレーニング参加メンバーの担当演目と、その演目の教え
 */
fun ShuntaisaiStatus.memberLabel(member: MemberState): String? {
    val type = member.shuntaisaiPerformance ?: return null
    val performance = ShuntaisaiPerformance.of(type)?.displayName ?: return null
    val teaching = teachings[type]?.displayName ?: return "[$performance]"
    return "[$performance/$teaching]"
}

@Composable
fun ShuntaisaiParamInfo(state: SimulationState, action: Action) {
    val shuntaisaiStatus = state.shuntaisaiStatus ?: return
    ShuntaisaiOutingInfo(shuntaisaiStatus, action)
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

/**
 * 吾妻のお出かけ（2～6段階目）で演目Lvが上がる演目
 */
@Composable
private fun ShuntaisaiOutingInfo(shuntaisaiStatus: ShuntaisaiStatus, action: Action) {
    if (action !is Outing) return
    val support = action.support ?: return
    if (support.charaName != "吾妻" || support.supportState?.outingStep !in 2..6) return
    val target = trainingType.minByOrNull { shuntaisaiStatus.level(it) } ?: return
    val level = shuntaisaiStatus.level(target)
    if (level >= shuntaisaiStatus.maxLevel) return
    Div {
        Text("演目Lvアップ: ${ShuntaisaiPerformance.of(target)?.displayName} Lv$level → Lv${level + 1}")
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

@Composable
fun ShuntaisaiReassignInfo(state: SimulationState, action: ShuntaisaiAssignTeaching) {
    val current = state.shuntaisaiStatus?.reassigningTeachings?.entries
        ?.firstOrNull { it.value.teaching == action.teaching }?.key ?: return
    Div {
        val suffix = if (current == action.target) "（変更なし）" else ""
        Text("現在の割り当て：${ShuntaisaiPerformance.of(current)?.displayName}$suffix")
    }
}

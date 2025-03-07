package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.*
import io.github.mee1080.umasim.scenario.legend.LegendBuff
import io.github.mee1080.umasim.scenario.legend.LegendMember
import io.github.mee1080.umasim.scenario.legend.LegendMemberState
import io.github.mee1080.umasim.scenario.legend.LegendStatus
import io.github.mee1080.umasim.simulation2.Action
import io.github.mee1080.umasim.simulation2.LegendActionParam
import io.github.mee1080.umasim.web.components.atoms.MdDialog
import io.github.mee1080.umasim.web.components.atoms.MdSysColor
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

private val legendColor = mapOf(
    LegendMember.Blue to rgb(192, 192, 255),
    LegendMember.Green to rgb(192, 255, 192),
    LegendMember.Red to rgb(255, 192, 192),
)

@Composable
fun LegendStateBlock(legendStatus: LegendStatus) {
    var displayBuff by remember { mutableStateOf<LegendBuff?>(null) }
    Div({
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            columnGap(8.px)
        }
    }) {
        BuffGauge(LegendMember.Blue, legendStatus.buffGauge[LegendMember.Blue]!!)
        BuffGauge(LegendMember.Green, legendStatus.buffGauge[LegendMember.Green]!!)
        BuffGauge(LegendMember.Red, legendStatus.buffGauge[LegendMember.Red]!!)
    }
    Div({
        style {
            display(DisplayStyle.Flex)
            columnGap(8.px)
            rowGap(8.px)
            flexWrap(FlexWrap.Wrap)
        }
    }) {
        legendStatus.buffList.forEach { buff ->
            Div({
                style {
                    backgroundColor(if (buff.enabled) legendColor[buff.buff.member]!! else rgb(192, 192, 192))
                    padding(4.px)
                    borderRadius(4.px)
                }
                onClick {
                    displayBuff = buff.buff
                }
            }) {
                Text(buff.buff.name)
                if (buff.coolTime > 0) {
                    Text("[${buff.coolTime}]")
                }
            }
        }
    }
    legendStatus.mastery?.let { mastery ->
        Div {
            Text("[${mastery.displayName}の導き]")
            if (legendStatus.specialStateTurn < 0) {
                Text("${mastery.specialStateName}まで あと ${-legendStatus.specialStateTurn} 回")
            } else if (legendStatus.specialStateTurn > 0) {
                Text(mastery.specialStateName)
                if (mastery == LegendMember.Blue) {
                    Text(" 残り ${legendStatus.specialStateTurn} ターン")
                } else {
                    Text(" ${legendStatus.specialStateTurn} ターン目")
                }
            }
        }
    }
    MdDialog(
        open = displayBuff != null,
        onPrimaryButton = { displayBuff = null },
        onClosed = { displayBuff = null },
    ) {
        Text(displayBuff?.description ?: "")
    }
}

@Composable
private fun BuffGauge(member: LegendMember, gauge: Int) {
    Div({
        style {
            backgroundColor(legendColor[member]!!)
            padding(4.px)
            borderRadius(4.px)
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
        }
    }) {
        Text(member.color)
        repeat(8) {
            Div({
                style {
                    width(8.px)
                    height(if (it == 1 || it == 3 || it == 7) 8.px else 4.px)
                    borderRadius(2.px)
                    backgroundColor(if (gauge > it) rgb(255, 255, 192) else rgb(128, 128, 128))
                }
            })
        }
    }
}

@Composable
fun LegendInfo(action: Action) {
    val target = action.candidates.first().first
    val param = target.scenarioActionParam as? LegendActionParam ?: return
    Div({
        style {
            backgroundColor(legendColor[param.legendMember]!!)
            padding(4.px)
            borderRadius(4.px)
        }
    }) {
        Text(param.toShortString())
    }
}

@Composable
fun LegendMemberStateBlock(state: LegendMemberState) {
    if (state.bestFriendLevel == 0) return
    Div({
        style {
            display(DisplayStyle.LegacyInlineFlex)
        }
    }) {
        Div {
            Text("親友Lv${state.bestFriendLevel}")
        }
        Div({
            style {
                width(32.px)
                height(24.px)
                border(1.px, LineStyle.Solid, MdSysColor.outline.value)
            }
        }) {
            Div({
                style {
                    width((state.bestFriendGauge * 5).percent)
                    height(100.percent)
                    backgroundColor(Color.yellow)
                }
            })
        }
        Div({
            style { marginRight(16.px) }
        }) {
            Text(state.bestFriendGauge.toString())
        }
    }
}

package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.LArcMemberState
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation2.MemberState
import io.github.mee1080.umasim.web.components.atoms.MdSysColor
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun MemberState(state: MemberState, friend: Boolean = false, training: Boolean = false) {
    DivFlexCenter({
        style {
            margin(4.px, 0.px)
            if (friend) {
                background("linear-gradient(to right, #e0c00040, #ea433540)")
            }
        }
    }) {
        if (!state.guest) {
            Div({
                style {
                    width(64.px)
                    height(24.px)
                    border(1.px, LineStyle.Solid, MdSysColor.outline.value)
                }
            }) {
                Div({
                    style {
                        width(state.relation.percent)
                        height(100.percent)
                        when {
                            state.relation == 100 -> backgroundColor(Color.gold)
                            state.relation >= 80 -> backgroundColor(Color.orange)
                            state.relation >= 60 -> backgroundColor(Color.greenyellow)
                            else -> backgroundColor(Color.blue)
                        }
                    }
                })
            }
            Div({
                style { marginRight(16.px) }
            }) {
                Text(state.relation.toString())
            }
        }
        when (val scenarioState = state.scenarioState) {
            is LArcMemberState -> LArcMemberState(scenarioState)
        }
        if (training && state.hint) {
            Div({
                style {
                    color(Color.white)
                    backgroundColor(rgb(220, 0, 0))
                    width(24.px)
                    height(24.px)
                    textAlign("center")
                    marginRight(4.px)
                    fontWeight("bold")
                    borderRadius(50.percent)
                }
            }) { Text("！") }
        }
        if (training) {
            Div { Text(state.charaName) }
        } else {
            Div { Text(state.name) }

        }
    }
}

@Composable
private fun LArcMemberState(state: LArcMemberState) {
    if (state.starType != StatusType.NONE) {
        Div({
            style {
                display(DisplayStyle.Flex)
                border(1.px, LineStyle.Solid, MdSysColor.outline.value)
                backgroundColor(Color.black)
                width(24.px)
                height(26.px)
                rowGap(1.px)
                flexDirection(FlexDirection.Column)
            }
        }) {
            (2 downTo 0).forEach {
                Div({
                    style {
                        width(100.percent)
                        height(8.px)
                        if (it < state.starGauge) {
                            backgroundColor(Color.yellow)
                        }
                    }
                })
            }
        }
        Div({
            style { marginRight(16.px) }
        }) {
            val text = buildString {
                append(state.nextStarEffect[0].displayName)
                append(' ')
                append(state.starType.displayName.substring(0, 2))
                append("Lv")
                append(state.starLevel)
            }
            Text(text)
        }
    }
}

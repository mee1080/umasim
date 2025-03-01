package io.github.mee1080.umasim.web.page.legends

import androidx.compose.runtime.*
import io.github.mee1080.umasim.data.motivationToString
import io.github.mee1080.umasim.scenario.legend.LegendMember
import io.github.mee1080.umasim.web.components.atoms.*
import io.github.mee1080.utility.mapIf
import io.github.mee1080.utility.replaced
import io.github.mee1080.utility.roundToString
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun LegendsCalcPage() {
    var state by remember { mutableStateOf(LegendsCalcState().calcRate()) }
    Div({
        style {
            display(DisplayStyle.Flex)
        }
    }) {
        Div({
            style {
                flexGrow(1)
                padding(8.px)
            }
        }) { SettingPanel(state) { state = it } }
        Div({
            style {
                width(360.px)
                padding(8.px)
            }
        }) { BuffPanel(state) { state = it } }
    }
}

@Composable
private fun SettingPanel(state: LegendsCalcState, update: (LegendsCalcState) -> Unit) {
    H2 { Text("現在の倍率：${state.currentFactor.totalFactor.roundToString(4)}") }
    Div { Text("友情：${state.currentFactor.friendFactor.roundToString(4)}") }
    Div { Text("やる気：${state.currentFactor.motivationFactor.roundToString(4)}") }
    Div { Text("トレ効果：${state.currentFactor.trainingFactor.roundToString(4)}") }

    H2 { Text("設定") }

    Div { Text("やる気：${motivationToString(state.motivation)}") }
    MdSlider(
        value = state.motivation,
        max = 2,
        min = -2,
    ) {
        style { width(100.percent) }
        onInput { update(state.update { copy(motivation = it.toInt()) }) }
    }
    Div { Text("導き") }
    specialStateSelection.forEach { (label, value) ->
        MdRadio(label, state.specialState == value) {
            style { padding(8.px) }
            onSelect {
                update(state.update { copy(specialState = value) })
            }
        }
    }

    H3 { Text("参加サポカ/協力者") }

    if (state.specialState == LegendMember.Red) {
        state.bestFriendList.forEachIndexed { index, friend ->
            Div { Text("${index + 1}人目 親友Lv${if (friend.member.bestFriendLevel == 0) "なし" else friend.member.bestFriendLevel}") }
            MdSlider(
                value = friend.member.bestFriendLevel,
                max = 9,
                min = 0,
            ) {
                style { width(100.percent) }
                onInput {
                    update(state.update {
                        copy(
                            bestFriendList = bestFriendList.replaced(
                                index,
                                friend.copy(member = friend.member.copy(bestFriendLevel = it.toInt())),
                            )
                        )
                    })
                }
            }
            Div {
                MdCheckbox("ゲスト", friend.member.guest) {
                    onChange {
                        update(state.update {
                            copy(
                                bestFriendList = bestFriendList.replaced(
                                    index,
                                    friend.copy(member = friend.member.copy(guest = it)),
                                )
                            )
                        })
                    }
                }
                MdCheckbox("参加", friend.join) {
                    onChange {
                        update(state.update {
                            copy(
                                bestFriendList = bestFriendList.replaced(
                                    index,
                                    friend.copy(join = it),
                                )
                            )
                        })
                    }
                }
                MdCheckbox("友情", friend.friend) {
                    onChange {
                        update(state.update {
                            copy(
                                bestFriendList = bestFriendList.replaced(
                                    index,
                                    friend.copy(friend = it),
                                )
                            )
                        })
                    }
                }
            }
        }
    } else {
        Div { Text("人数：${state.memberCount}") }
        Div {
            MdSlider(
                value = state.memberCount,
                max = 5,
                min = 0,
            ) {
                style { width(100.percent) }
                onInput { update(state.update { copy(memberCount = it.toInt()) }) }
            }
        }
        Div { Text("友情人数：${state.friendCount}") }
        Div {
            MdSlider(
                value = state.friendCount,
                max = 5,
                min = 0,
            ) {
                style { width(100.percent) }
                onInput { update(state.update { copy(friendCount = it.toInt()) }) }
            }
        }
    }
    Div { Text("やる気効果合計：${state.supportMotivationBonus}") }
    Div {
        MdSlider(
            value = state.supportMotivationBonus,
            max = 400,
            min = 0,
        ) {
            style { width(100.percent) }
            onInput { update(state.update { copy(supportMotivationBonus = it.toInt()) }) }
        }
    }
    Div { Text("トレ効果合計：${state.supportTrainingBonus}") }
    Div {
        MdSlider(
            value = state.supportTrainingBonus,
            max = 120,
            min = 0,
        ) {
            style { width(100.percent) }
            onInput { update(state.update { copy(supportTrainingBonus = it.toInt()) }) }
        }
    }
}

@Composable
private fun BuffPanel(state: LegendsCalcState, update: (LegendsCalcState) -> Unit) {
    H2 { Text("心得効率（クリックで獲得）") }
    Table {
        state.sortedBuffList.forEach { buff ->
            Tr({
                onClick {
                    update(state.update {
                        copy(buffList = buffList.mapIf({ it.buff.name == buff.buff.name }) {
                            it.copy(checked = !it.checked)
                        })
                    })
                }
                style {
                    cursor("pointer")
                }
            }) {
                Td({
                    style {
                        backgroundColor(
                            when (buff.buff.member) {
                                LegendMember.Blue -> rgb(192, 192, 255)
                                LegendMember.Green -> rgb(192, 255, 192)
                                LegendMember.Red -> rgb(255, 192, 192)
                            }
                        )
                    }
                }) {
                    Text("[${buff.buff.member.color}${buff.buff.rank}] ${buff.buff.name}")
                }
                Td({
                    style { width(120.px) }
                }) {
                    if (buff.checked) {
                        Text("獲得済み")
                    } else {
                        Div({
                            style {
                                width(((buff.factor - state.currentFactor.totalFactor) / (state.maxFactor - state.currentFactor.totalFactor) * 100).percent)
                                backgroundColor(rgb(255, 255, 0))
                                whiteSpace("nowrap")
                            }
                        }) {
                            Text(buff.factor.roundToString(4))
                        }
                    }
                }
            }
        }
    }
}

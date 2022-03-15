package io.github.mee1080.umasim.web.page.rotation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.github.mee1080.umasim.data.RaceGrade
import io.github.mee1080.umasim.data.turnToString
import io.github.mee1080.umasim.rotation.RaceRotationCalculator
import io.github.mee1080.umasim.web.components.LabeledNumberInput
import io.github.mee1080.umasim.web.components.LabeledSelect
import io.github.mee1080.umasim.web.onClickOrTouch
import io.github.mee1080.umasim.web.state.RotationState
import io.github.mee1080.umasim.web.vm.RotationViewModel
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun RotationPage(model: RotationViewModel, state: RotationState?) {
    if (state == null) {
        LaunchedEffect("InitRotation") {
            model.init()
        }
        return
    }
    Div({
        style {
            display(DisplayStyle.Flex)
        }
    }) {
        Div {
            H3 { Text("設定") }
            state.groundSetting.forEach { entry ->
                LabeledSelect(
                    entry.key.displayName,
                    RaceRotationCalculator.displayRankList,
                    entry.value.displayName
                ) {
                    model.updateGroundSetting(entry.key, it)
                }
            }
            state.distanceSetting.forEach { entry ->
                LabeledSelect(
                    entry.key.displayName,
                    RaceRotationCalculator.displayRankList,
                    entry.value.displayName
                ) {
                    model.updateDistanceSetting(entry.key, it)
                }
            }
            H4 { Text("おすすめ基準値") }
            LabeledNumberInput("適性A", state.option.rankA) {
                model.updateOption(state.option.copy(rankA = it.toInt()))
            }
            LabeledNumberInput("適性B", state.option.rankB) {
                model.updateOption(state.option.copy(rankB = it.toInt()))
            }
            LabeledNumberInput("適性C", state.option.rankC) {
                model.updateOption(state.option.copy(rankC = it.toInt()))
            }
            LabeledNumberInput("GI", state.option.gradeG1) {
                model.updateOption(state.option.copy(gradeG1 = it.toInt()))
            }
            LabeledNumberInput("GII/GIII", state.option.gradeG2G3) {
                model.updateOption(state.option.copy(gradeG2G3 = it.toInt()))
            }
            LabeledNumberInput("連続出走2", state.option.continue2) {
                model.updateOption(state.option.copy(continue2 = it.toInt()))
            }
            LabeledNumberInput("連続出走3", state.option.continue3) {
                model.updateOption(state.option.copy(continue3 = it.toInt()))
            }
            LabeledNumberInput("連続出走4以上", state.option.continue4) {
                model.updateOption(state.option.copy(continue4 = it.toInt()))
            }
            Button({
                onClickOrTouch { model.updateOption(RaceRotationCalculator.Option()) }
            }) { Text("デフォルトに戻す") }
        }
        Div {
            H3 { Text("おすすめ(クリックで追加)") }
            Div({
                style {
                    overflowY("scroll")
                    height(80.vh)
                }
            }) {
                val list = if (state.calcState.recommendation.size <= 20) state.calcState.recommendation else {
                    state.calcState.recommendation.slice(0..19)
                }
                list.forEach { entry ->
                    val race = entry.first
                    Div({
                        style {
                            margin(8.px)
                            val background = when (race.grade) {
                                RaceGrade.G1 -> Color.lightblue
                                RaceGrade.G2 -> Color.lightpink
                                RaceGrade.G3 -> Color.lightgreen
                                else -> Color.lightyellow
                            }
                            backgroundColor(background)
                            cursor("pointer")
                        }
                        onClickOrTouch {
                            model.selectRace(race.turn, race.name)
                        }
                    }) {
                        Div({ style { fontSize(150.percent) } }) { Text(race.name) }
                        Div { Text("(${turnToString(race.turn)} ${race.grade.name} ${race.ground.displayName} ${race.distance})") }
                        entry.third.forEach {
                            when (it.second) {
                                null -> {
                                    Div({
                                        style {
                                            color(Color.gray)
                                        }
                                    }) { Text("${it.first} 達成不可") }
                                }
                                0 -> {
                                    Div({
                                        style {
                                            fontWeight("bold")
                                            color(Color.red)
                                        }
                                    }) { Text("${it.first} 達成") }
                                }
                                else -> {
                                    Div { Text("${it.first} あと${it.second}") }
                                }
                            }
                        }
                    }
                }
            }
        }
        Div {
            H3 { Text("出走レース") }
            Div {
                Text("出走数 ${state.raceCount}")
                Button({ onClickOrTouch { model.resetRace() } }) { Text("クリア") }
            }
            Div({
                style {
                    overflowY("scroll")
                    height(80.vh)
                }
            }) {
                state.raceSelection.forEachIndexed { turn, raceEntries ->
                    if (raceEntries.isNotEmpty()) {
                        Div {
                            LabeledSelect(
                                turnToString(turn),
                                listOf("") + raceEntries.map { it.name },
                                state.selectedRace[turn] ?: "",
                            ) {
                                model.selectRace(turn, it)
                            }
                        }
                    }
                }
            }
        }
        Div {
            H3 { Text("合計ステータス: ${state.calcState.totalStatus}") }
            state.calcState.currentAchievement.forEach {
                Div {
                    when (it.value) {
                        null -> {
                            Div({
                                style {
                                    color(Color.gray)
                                }
                            }) { Text("${it.key} 達成不可") }
                        }
                        0 -> {
                            Div({
                                style {
                                    fontWeight("bold")
                                    color(Color.red)
                                }
                            }) { Text("${it.key} 達成") }
                        }
                        else -> {
                            Div { Text("${it.key} あと${it.value}") }
                        }
                    }
                }
            }
        }
    }
}
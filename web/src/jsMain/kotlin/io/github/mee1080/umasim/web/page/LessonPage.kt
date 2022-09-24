package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.LabeledRadioGroup
import io.github.mee1080.umasim.web.components.material.MwcButton
import io.github.mee1080.umasim.web.components.material.MwcSlider
import io.github.mee1080.umasim.web.components.material.raised
import io.github.mee1080.umasim.web.onClickOrTouch
import io.github.mee1080.umasim.web.round
import io.github.mee1080.umasim.web.state.LessonState
import io.github.mee1080.umasim.web.vm.LessonViewModel
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun LessonPage(model: LessonViewModel, state: LessonState) {
    H2 { Text("テクニックレッスン突破確率計算器") }
    LabeledRadioGroup(
        "period",
        "時期： ",
        state.periodList,
        state.periodIndex,
    ) {
        model.update { copy(periodIndex = it) }
    }
    Div {
        Span { Text("現在パフォーマンス： ") }
        Text("Da")
        TextInput(state.dance) {
            size(5)
            onInput { model.update { copy(dance = it.value) } }
        }
        Text("Pa")
        TextInput(state.passion) {
            size(5)
            onInput { model.update { copy(passion = it.value) } }
        }
        Text("Vo")
        TextInput(state.vocal) {
            size(5)
            onInput { model.update { copy(vocal = it.value) } }
        }
        Text("Vi")
        TextInput(state.visual) {
            size(5)
            onInput { model.update { copy(visual = it.value) } }
        }
        Text("Me")
        TextInput(state.mental) {
            size(5)
            onInput { model.update { copy(mental = it.value) } }
        }
    }
    Div({
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
        }
    }) {
        Span { Text("計算回数: ") }
        MwcSlider(state.stepCount, 1, 5) {
            onInput { model.update { copy(stepCount = it.toInt()) } }
            style { width(300.px) }
        }
        Span { Text(state.stepCount.toString()) }
    }
    Div {
        Span { Text("計算省略閾値: ") }
        TextInput(state.threshold) {
            size(10)
            onInput { model.update { copy(threshold = it.value) } }
        }
        Div {
            Small { Text("0.001 → 確率が0.1%以下の場合、計算を省略する") }
        }
    }
    Div({ style { margin(16.px) } }) {
        MwcButton({
            onClickOrTouch { model.calculate() }
            raised()
        }) {
            Text("計算実行")
        }
    }
    state.message?.let { message ->
        Div { Text(message) }
    }
    state.result.forEachIndexed { index, value ->
        Div { Text("${index + 1}回: ${(value * 100).round()}%") }
    }
    Div({ style { margin(32.px) } }) {
        Ul {
            Li { Text("計算省略閾値を設定した場合、計算が早くなりますが、誤差が発生します（0.001で数%）") }
            Li { Text("出現確率は予想のため、誤差があります（おそらく最大10%程度）") }
            Li {
                A(
                    href = "https://github.com/mee1080/umasim/blob/main/data/grand_live_memo.md#%E3%83%86%E3%82%AF%E3%83%8B%E3%83%83%E3%82%AF%E5%87%BA%E7%8F%BE%E7%8E%87",
                    attrs = {
                        target(ATarget.Blank)
                        attr("rel", "noreferrer noopener")
                    }
                ) { Text("出現確率予想詳細") }
            }
        }
    }
}
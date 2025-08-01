package io.github.mee1080.umasim.web.page.graph

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.atoms.*
import io.github.mee1080.umasim.web.components.lib.ScopedStyleSheet
import io.github.mee1080.umasim.web.components.lib.install
import io.github.mee1080.umasim.web.vm.GraphViewModel
import io.github.mee1080.utility.roundToString
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

@Composable
fun FactorInput(
    viewModel: GraphViewModel,
    factorList: List<GraphFactor>,
) {
    Div {
        factorList.forEachIndexed { index, factor ->
            val background = if (index % 2 == 0) {
                MdClass.surfaceContainerLowest
            } else {
                MdClass.surfaceContainerLow
            }
            Div({ classes(background, S.factorRow) }) {
                Div {
                    MdOutlinedSelect(
                        selection = graphFactorTemplates,
                        selectedItem = factor.template,
                        onSelect = { viewModel.selectFactorTemplate(index, it) },
                        itemToValue = { it.id.toString() },
                        itemToDisplayText = { it.name }
                    )
                    Div { Text(factor.coefficient.roundToString(2)) }
                }
                Div({ classes(S.factorInputWrapper) }) {
                    MdOutlinedTextField(
                        value = if (factor.template.isManualInput) factor.expressionInput else factor.template.expressionInput,
                        attrs = {
                            onInput { viewModel.updateExpression(index, it) }
                            classes(S.factorInputText)
                            if (!factor.template.isManualInput) readOnly()
                        },
                    )
                    Div({ classes(S.factorInputError) }) {
                        Text(factor.expressionError)
                    }
                    MdSlider(
                        value = factor.coefficient,
                        min = 0f,
                        max = 1f,
                        attrs = {
                            step(0.01f)
                            onInput { viewModel.updateCoefficient(index, it.toDouble()) }
                        }
                    )
                }
                Div {
                    MdIconButton("delete") {
                        onClick { viewModel.deleteGraphFactor(index) }
                    }
                }
            }
        }
        Div {
            MdFilledButton("追加") {
                onClick { viewModel.addGraphFactor() }
            }
        }
        Description()
    }
}

@Composable
private fun Description() {
    Div({ classes(S.description) }) {
        H3 { Text("式の入力方法") }

        H4 { Text("シミュレーション結果変数") }
        Div { Text("speed: スピード") }
        Div { Text("stamina: スタミナ") }
        Div { Text("power: パワー") }
        Div { Text("guts: 根性") }
        Div { Text("wisdom: 賢さ") }
        Div { Text("skillPt: スキルPt") }
        Div { Text("totalHintLevel: ヒントLv合計") }

        H4 { Text("サポカ能力変数") }
        Div { Text("initialRelation: 初期絆") }
        Div { Text("initialSpeed: 初期スピード") }
        Div { Text("initialStamina: 初期スタミナ") }
        Div { Text("initialPower: 初期パワー") }
        Div { Text("initialGuts: 初期根性") }
        Div { Text("initialWisdom: 初期賢さ") }
        Div { Text("initialSkillPt: 初期スキルPt") }
        Div { Text("friend: 友情ボーナス") }
        Div { Text("friend2: 友情ボーナス（特殊固有あり）") }
        Div { Text("motivation: やる気効果") }
        Div { Text("motivation2: やる気効果（特殊固有あり）") }
        Div { Text("training: トレーニング効果") }
        Div { Text("training2: トレーニング効果（特殊固有あり）") }
        Div { Text("speedBonus: スビードボーナス") }
        Div { Text("speedBonus2: スビードボーナス（特殊固有あり）") }
        Div { Text("staminaBonus: スタミナボーナス") }
        Div { Text("staminaBonus2: スタミナボーナス（特殊固有あり）") }
        Div { Text("powerBonus: パワーボーナス") }
        Div { Text("powerBonus2: パワーボーナス（特殊固有あり）") }
        Div { Text("gutsBonus: 根性ボーナス") }
        Div { Text("gutsBonus2: 根性ボーナス（特殊固有あり）") }
        Div { Text("wisdomBonus: 賢さボーナス") }
        Div { Text("wisdomBonus2: 賢さボーナス（特殊固有あり）") }
        Div { Text("skillPtBonus: スキルPtボーナス") }
        Div { Text("skillPtBonus2: スキルPtボーナス（特殊固有あり）") }
        Div { Text("race: レースボーナス") }
        Div { Text("fan: ファン数ボーナス") }
        Div { Text("specialityRate: 得意率") }
        Div { Text("specialityRate2: 得意率（特殊固有あり）") }
        Div { Text("hintLevel: ヒントLv") }
        Div { Text("hintFrequency: ヒント発生率") }
        Div { Text("wisdomFriendRecovery: 賢さ友情回復量") }
        Div { Text("wisdomFriendRecovery2: 賢さ友情回復量（特殊固有あり）") }

        H4 { Text("演算子") }
        Div { Text("+: 足し算") }
        Div { Text("-: 引き算") }
        Div { Text("*: 掛け算") }
        Div { Text("/: 割り算") }
        Div { Text("%: 剰余") }
        Div { Text("^: 累乗") }

        H4 { Text("関数") }
        Div { Text("max(a,b,...): 最大値") }
        Div { Text("min(a,b,...): 最小値") }
        Div { Text("sqrt(a): aの二乗根") }
        Div { Text("log(a,b): bを底とするaの対数（a省略時はe）") }
        Div { Text("log10(a): 10を底とするaの対数") }
        Div { Text("log2(a): 2を底とするaの対数") }
        Div { Text("floor(a): a以下の最大の整数") }
        Div { Text("ceil(a): a以上の最小の整数") }
        Div { Text("round(a): aを四捨五入した整数") }
        Div { Text("abs(a): aの絶対値") }
    }
}

private val S = object : ScopedStyleSheet() {

    val factorRow by style {
        padding(8.px)
        display(DisplayStyle.Flex)
    }

    val factorInputWrapper by style {
        flexGrow(1)
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
    }

    val factorInputText by style {
        width(100.percent)
    }

    val factorInputError by style {
        color(Color.red)
    }

    val description by style {
        margin(16.px)
        padding(8.px)
        backgroundColor(Color.lightgreen)
    }
}.install()

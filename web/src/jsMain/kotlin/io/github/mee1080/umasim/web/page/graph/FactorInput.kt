package io.github.mee1080.umasim.web.page.graph

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.atoms.*
import io.github.mee1080.umasim.web.components.lib.ScopedStyleSheet
import io.github.mee1080.umasim.web.components.lib.install
import io.github.mee1080.umasim.web.vm.GraphViewModel
import io.github.mee1080.utility.roundToString
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
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
}.install()

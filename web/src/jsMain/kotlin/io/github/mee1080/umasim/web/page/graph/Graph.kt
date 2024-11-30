package io.github.mee1080.umasim.web.page.graph

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.atoms.MdClass
import io.github.mee1080.umasim.web.components.atoms.MdSysColor
import io.github.mee1080.umasim.web.components.lib.CSSVarProperty
import io.github.mee1080.umasim.web.components.lib.ScopedStyleSheet
import io.github.mee1080.umasim.web.components.lib.install
import io.github.mee1080.umasim.web.vm.GraphViewModel
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

private val barColors = listOf(
    rgb(91, 155, 213),
    rgb(237, 125, 49),
    rgb(165, 165, 165),
    rgb(255, 192, 0),
    rgb(68, 114, 196),
)

private val legends = barColors.mapIndexed { index, color ->
    "${index}凸" to color
}

@Composable
fun Graph(
    viewModel: GraphViewModel,
    dataList: List<GraphRowDisplayData>,
    labels: List<String>,
    slim: Boolean = false,
) {
    Div({
        style {
            paddingStartVar = if (slim) 24.px else 420.px
            paddingEndVar = if (slim) 24.px else 32.px
        }
    }) {
        HorizontalAxisLabels(labels)
        GraphArea(viewModel, dataList, labels.size, slim)
        GraphLegend(legends)
    }
}

@Composable
private fun GraphArea(
    viewModel: GraphViewModel,
    dataList: List<GraphRowDisplayData>,
    labelCount: Int,
    slim: Boolean,
) {
    dataList.forEach { data ->
        Div({ classes(S.graphRow) }) {
            Div({
                classes(S.verticalAxisLabelText)
                onClick { viewModel.openGraphDataDialog(data, 4) }
            }) {
                if (!slim) Text(data.label)
            }
            Div({ classes(S.barArea) }) {
                GraphScale(labelCount)
                GraphBar(viewModel, data)
                if (slim) GraphLabel(data.label)
            }
        }
    }
}

@Composable
private fun GraphBar(
    viewModel: GraphViewModel,
    data: GraphRowDisplayData,
) {
    Div({ style { display(DisplayStyle.Flex) } }) {
        var currentRate = 0.0
        data.rates.forEachIndexed { index, rate ->
            Div({
                classes(S.graphBar)
                style {
                    backgroundColor(barColors[index])
                    left((currentRate * 100.0).percent)
                    if (rate > currentRate) {
                        width(((rate - currentRate) * 100.0).percent)
                        currentRate = rate
                    }
                }
                onClick {
                    viewModel.openGraphDataDialog(data, index)
                }
            })
        }
    }
}

@Composable
private fun HorizontalAxisLabels(
    labels: List<String>,
) {
    Div({ classes(S.horizontalAxisLabel, MdClass.labelSmall) }) {
        labels.forEach {
            Div({ classes(S.horizontalAxisLabelText) }) {
                Text(it)
            }
        }
    }
}

@Composable
private fun GraphScale(
    count: Int,
) {
    Div({ classes(S.scale) }) {
        repeat(count) {
            Div({ classes(MdClass.outlineVariant, S.scaleLine) })
        }
    }
}

@Composable
private fun GraphLabel(text: String) {
    Div({ classes(S.graphLabel) }) {
        Text(text)
    }
}

@Composable
private fun GraphLegend(
    legends: List<Pair<String, CSSColorValue>>,
) {
    Div({ classes(MdClass.labelMedium, S.legendWrapper) }) {
        Div({ classes(S.legendBox) }) {
            legends.forEach { (label, color) ->
                Div({ classes(S.legendItem) }) {
                    Div({
                        classes(S.legendColor)
                        style { backgroundColor(color) }
                    })
                    Text(label)
                }
            }
        }
    }
}

private var StyleScope.paddingStartVar by CSSVarProperty<CSSUnitValue>()

private var StyleScope.paddingEndVar by CSSVarProperty<CSSUnitValue>()

private val S = object : ScopedStyleSheet() {

    val graphRow by style {
        height(32.px)
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        paddingRight(paddingEndVar)
    }

    val barArea by style {
        flexGrow(1)
        position(Position.Relative)
        height(32.px)
    }

    val graphBar by style {
        position(Position.Absolute)
        top(4.px)
        height(24.px)
    }

    val horizontalAxisLabel by style {
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.SpaceBetween)
        paddingLeft(paddingStartVar - 32.px)
        paddingRight(paddingEndVar - 32.px)
    }

    val horizontalAxisLabelText by style {
        width(64.px)
        textAlign("center")
        whiteSpace("nowrap")
    }

    val verticalAxisLabelText by style {
        width(paddingStartVar)
    }

    val scale by style {
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.SpaceBetween)
        height(100.percent)
    }

    val scaleLine by style {
        width(1.px)
        height(100.percent)
    }

    val graphLabel by style {
        position(Position.Absolute)
        top(4.px)
        left(4.px)
    }

    val legendWrapper by style {
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
        padding(16.px)
    }

    val legendBox by style {
        border {
            width(1.px)
            style(LineStyle.Solid)
            color(MdSysColor.outline.value)
        }
        padding(8.px)
        display(DisplayStyle.Flex)
        columnGap(16.px)
    }

    val legendItem by style {
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        columnGap(4.px)
    }

    val legendColor by style {
        width(16.px)
        height(16.px)
    }
}.install()

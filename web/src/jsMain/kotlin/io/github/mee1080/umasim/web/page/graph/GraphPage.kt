package io.github.mee1080.umasim.web.page.graph

import androidx.compose.runtime.*
import io.github.mee1080.umasim.web.components.atoms.*
import io.github.mee1080.umasim.web.components.lib.ScopedStyleSheet
import io.github.mee1080.umasim.web.components.lib.install
import io.github.mee1080.umasim.web.components.lib.onDocumentHidden
import io.github.mee1080.umasim.web.components.lib.onWindowResize
import io.github.mee1080.umasim.web.vm.GraphViewModel
import io.github.mee1080.utility.roundToString
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.Draggable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun GraphPage(
    viewModel: GraphViewModel,
    state: GraphState,
) {
    var slim by remember { mutableStateOf(window.innerWidth < 800) }
    onWindowResize {
        slim = window.innerWidth < 800
    }
    onDocumentHidden {
        viewModel.saveFactorList()
    }
    LaunchedEffect(Unit) {
        viewModel.generateGraphData()
    }
    MainArea(viewModel, state, slim)
    MdDialog(
        state.dialogData != null,
        onClosed = { viewModel.closeGraphDialog() },
    ) {
        val dialogData = state.dialogData ?: return@MdDialog
        val entry = dialogData.first.data.getOrNull(dialogData.second) ?: return@MdDialog
        Div { Text("${dialogData.first.label} (${dialogData.second})") }
        entry.params.forEach {
            Div { Text("${it.key} : ${it.value.roundToString(4)}") }
        }
    }
    MdDialog(state.filterDialog, onClosed = { viewModel.closeGraphDialog() }) {
        state.baseData.forEachIndexed { index, data ->
            Div {
                MdCheckbox(data.label, data.visible) {
                    onChange { viewModel.toggleGraphVisibility(index) }
                }
            }
        }
    }
}

@Composable
private fun MainArea(
    viewModel: GraphViewModel,
    state: GraphState,
    slim: Boolean,
) {
    var divider by remember { mutableStateOf<Double?>(null) }
    Div({ classes(S.wrapper) }) {
        Div({ classes(S.header) }) {
            MdFilledButton("フィルタ") {
                onClick { viewModel.openGraphFilterDialog() }
            }
            MdRadioGroup(
                selection = GraphSortOrder.entries,
                selectedItem = state.sortOrder,
                onSelect = { viewModel.setGraphSortOrder(it) },
                itemToLabel = { it.displayName },
            )
            MdCheckbox("上下分割", divider != null) {
                onChange { divider = if (it) 0.0 else null }
            }
        }
        val dividerValue = divider
        if (dividerValue == null) {
            Graph(viewModel, state.displayData, state.labels, slim)
            FactorInput(viewModel, state.factorList)
        } else {
            Div({ classes(S.dividedGraph) }) {
                Graph(viewModel, state.displayData, state.labels, slim)
            }
            MdDivider {
                classes(S.divider)
                draggable(Draggable.True)
                var current = 0.0
                onDragStart { current = it.pageY }
                onDrag {
                    if (it.pageY > 100.0) {
                        divider = (divider ?: 0.0) + current - it.pageY
                    }
                    current = it.pageY
                }
            }
            Div({
                classes(S.dividedFactor)
                style { height(dividerValue.px + 50.percent) }
            }) {
                FactorInput(viewModel, state.factorList)
            }
        }
    }
}

private val S = object : ScopedStyleSheet() {

    val wrapper by style {
        height(100.percent)
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
    }

    val header by style {
        display(DisplayStyle.Flex)
    }

    val dividedGraph by style {
        flexGrow(1)
        flexShrink(1)
        overflowY("scroll")
    }

    val divider by style {
        height(8.px)
        flexShrink(0)
        cursor("row-resize")
    }

    val dividedFactor by style {
        flexShrink(0)
        overflowY("scroll")
    }
}.install()

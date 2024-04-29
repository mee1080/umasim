package io.github.mee1080.umasim.compose.common.parts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.mee1080.utility.applyIfNotNull

@Composable
fun LinedTable(
    rowCount: Int,
    columnCount: Int,
    modifier: Modifier = Modifier,
    cellPadding: Dp = 4.dp,
    cellBackground: Color = MaterialTheme.colorScheme.surface,
    headerBackground: Color = cellBackground,
    borderWidth: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    content: @Composable BoxScope.(row: Int, column: Int) -> Unit
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
    ) {
        repeat(columnCount) { column ->
            val background = if (column == 0) headerBackground else cellBackground
            val contentColor = MaterialTheme.colorScheme.contentColorFor(background)
            val cellModifier = Modifier
                .fillMaxWidth()
                .background(background)
                .padding(cellPadding)
            VerticalDivider(Modifier.fillMaxHeight(), borderWidth, borderColor)
            Column(
                modifier = Modifier.width(IntrinsicSize.Max),
            ) {
                repeat(rowCount) { row ->
                    HorizontalDivider(Modifier.fillMaxWidth(), borderWidth, borderColor)
                    Box(cellModifier) {
                        CompositionLocalProvider(LocalContentColor provides contentColor) {
                            content(row, column)
                        }
                    }
                }
                HorizontalDivider(Modifier.fillMaxWidth(), borderWidth, borderColor)
            }
        }
        VerticalDivider(Modifier.fillMaxHeight(), borderWidth, borderColor)
    }
}

@Composable
fun Table(
    rowCount: Int,
    columnCount: Int,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    cellHorizontalMargin: Dp = 2.dp,
    cellVerticalMargin: Dp = 2.dp,
    cellBackground: Color = MaterialTheme.colorScheme.surface,
    content: @Composable BoxScope.(row: Int, col: Int) -> Unit
) {
    Column(modifier) {
        val scrollState = if (scrollable) rememberScrollState() else null
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .applyIfNotNull(scrollState) { horizontalScroll(it) },
            horizontalArrangement = Arrangement.spacedBy(cellHorizontalMargin),
        ) {
            for (col in 0..<columnCount) {
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    verticalArrangement = Arrangement.spacedBy(cellVerticalMargin),
                ) {
                    for (row in 0..<rowCount) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(cellBackground),
                        ) {
                            content(row, col)
                        }
                    }
                }
            }
        }
        if (scrollState != null) {
            HorizontalScrollbar(rememberScrollbarAdapter(scrollState), Modifier.fillMaxWidth())
        }
    }
}
package io.github.mee1080.umasim.compose.common.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Table(
    rowCount: Int,
    columnCount: Int,
    modifier: Modifier = Modifier,
    cellPadding: Dp = 4.dp,
    cellBackground: Color = MaterialTheme.colorScheme.surfaceVariant,
    headerBackground: Color = cellBackground,
    rowGap: Dp = 1.dp,
    columnGap: Dp = 1.dp,
    content: @Composable BoxScope.(row: Int, column: Int) -> Unit
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(columnGap),
    ) {
        repeat(columnCount) { column ->
            val background = if (column == 0) headerBackground else cellBackground
            val contentColor = MaterialTheme.colorScheme.contentColorFor(background)
            val cellModifier = Modifier
                .fillMaxWidth()
                .background(if (column == 0) headerBackground else cellBackground)
                .padding(cellPadding)
            Column(
                modifier = Modifier.width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.spacedBy(rowGap, Alignment.CenterVertically),
            ) {
                repeat(rowCount) { row ->
                    Box(cellModifier) {
                        CompositionLocalProvider(LocalContentColor provides contentColor) {
                            content(row, column)
                        }
                    }
                }
            }
        }
    }
}

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
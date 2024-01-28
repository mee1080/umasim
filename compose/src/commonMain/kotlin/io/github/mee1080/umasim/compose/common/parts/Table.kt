package io.github.mee1080.umasim.compose.common.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
    rowGap: Dp = 0.dp,
    columnGap: Dp = 0.dp,
    content: @Composable RowScope.(row: Int, column: Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(rowGap),
    ) {
        repeat(rowCount) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(columnGap, Alignment.CenterHorizontally),
            ) {
                repeat(columnCount) { column ->
                    content(row, column)
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
    lineWidth: Dp = 1.dp,
    lineColor: Color = MaterialTheme.colorScheme.outlineVariant,
    content: @Composable RowScope.(row: Int, column: Int) -> Unit
) {
    Table(
        rowCount = rowCount,
        columnCount = columnCount,
        modifier = modifier.background(lineColor).border(lineWidth, lineColor),
        rowGap = lineWidth,
        columnGap = lineWidth,
        content = content,
    )
}
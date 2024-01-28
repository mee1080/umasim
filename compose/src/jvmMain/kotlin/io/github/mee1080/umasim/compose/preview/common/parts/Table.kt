package io.github.mee1080.umasim.compose.preview.common.parts

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.mee1080.umasim.compose.common.parts.LinedTable
import io.github.mee1080.umasim.compose.common.parts.Table
import io.github.mee1080.umasim.compose.theme.AppTheme

@Preview
@Composable
fun PreviewTable() {
    AppTheme {
        Column {
            Table(3, 5) { row, column -> Text("[$row-$column]") }
            LinedTable(3, 5) { row, column ->
                Text("[$row-$column]", Modifier.background(MaterialTheme.colorScheme.surface))
            }
        }
    }
}
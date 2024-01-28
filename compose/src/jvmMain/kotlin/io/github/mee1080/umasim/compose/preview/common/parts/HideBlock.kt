package io.github.mee1080.umasim.compose.preview.common.parts

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.compose.common.parts.HideBlock
import io.github.mee1080.umasim.compose.theme.AppTheme

@Preview
@Composable
fun PreviewHideBlock() {
    AppTheme {
        Column {
            HideBlock(false, {}, { Text("テスト") }) {
                Text("コンテンツ")
            }
            HideBlock(true, {}, { Text("テスト") }) {
                Text("コンテンツ")
            }
            HideBlock(
                true, {}, { Text("テスト") },
                headerBackground = MaterialTheme.colorScheme.primary,
                contentBackGround = MaterialTheme.colorScheme.secondary,
            ) {
                Text("コンテンツ")
            }
        }
    }
}
package io.github.mee1080.umasim.compose.preview.common.atoms

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.compose.common.atoms.MyButton
import io.github.mee1080.umasim.compose.theme.AppTheme

@Preview
@Composable
fun PreviewMyButton() {
    AppTheme {
        MyButton({}) { Text("テスト") }
    }
}
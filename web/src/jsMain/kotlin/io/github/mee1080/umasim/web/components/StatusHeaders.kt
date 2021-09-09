package io.github.mee1080.umasim.web.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th

@Composable
fun StatusHeaders() {
    Th { Text("スピード") }
    Th { Text("スタミナ") }
    Th { Text("パワー") }
    Th { Text("根性") }
    Th { Text("賢さ") }
}
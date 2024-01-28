package io.github.mee1080.umasim.compose.common.lib

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

fun String.asComposable(): @Composable () -> Unit = { Text(this) }

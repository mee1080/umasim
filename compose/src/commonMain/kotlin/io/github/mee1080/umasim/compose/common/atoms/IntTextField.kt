package io.github.mee1080.umasim.compose.common.atoms

import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IntTextField(
    value: Int,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    onValueChange: (Int) -> Unit,
) {
    var textValue by remember { mutableStateOf("") }
    LaunchedEffect(value) {
        textValue = value.toString()
    }
    OutlinedTextField(
        value = textValue,
        modifier = modifier.width(96.dp),
        label = label,
        singleLine = true,
        onValueChange = { input ->
            textValue = input
            input.toIntOrNull()?.let(onValueChange)
        },
    )
}
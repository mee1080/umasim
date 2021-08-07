package io.github.mee1080.umasim.gui.component

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*

@Composable
fun <T> Spinner(
    initialLabel: String,
    candidates: List<T>,
    onSelect: (T) -> Unit = {},
    candidateToContent: (T) -> String = { it.toString() },
) {
    var expanded by remember { mutableStateOf(false) }
    var label by remember { mutableStateOf(initialLabel) }
    Button(onClick = { expanded = !expanded }) {
        Text(label)
        Icon(Icons.Filled.ArrowDropDown, null)
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        candidates.forEach { candidate ->
            DropdownMenuItem(onClick = {
                expanded = false
                label = candidateToContent(candidate)
                onSelect(candidate)
            }) {
                Text(text = candidateToContent(candidate))
            }
        }
    }
}
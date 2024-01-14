package io.github.mee1080.umasim.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> SelectBox(
    items: List<T>,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    itemToString: (T) -> String = { it.toString() },
    itemToMenuContent: @Composable (T) -> Unit = { Text(itemToString(it)) },
) {
    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        var expanded by remember { mutableStateOf(false) }
        Button(
            onClick = { expanded = true },
        ) {
            selectedItem?.let { itemToMenuContent(it) } ?: Text("未選択")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(),
        ) {
            items.forEach {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onSelect(it)
                    },
                ) {
                    itemToMenuContent(it)
                }
            }
        }
    }
}
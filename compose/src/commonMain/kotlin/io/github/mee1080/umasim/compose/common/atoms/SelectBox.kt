package io.github.mee1080.umasim.compose.common.atoms

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Composable
fun <T> SelectBox(
    items: List<T>,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    outlined: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    itemToString: (T) -> String = { it.toString() },
    itemToMenuContent: @Composable (T) -> Unit = { Text(itemToString(it)) },
) {
    var expanded by remember { mutableStateOf(false) }
    SelectBox(
        expanded, { expanded = it },
        items, selectedItem, onSelect, modifier, outlined, label, itemToString, itemToMenuContent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectBox(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<T>,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    outlined: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    itemToString: (T) -> String = { it.toString() },
    itemToMenuContent: @Composable (T) -> Unit = { Text(itemToString(it)) },
) {
    ExposedDropdownMenuBox(expanded, onExpandedChange, modifier) {
        if (outlined) {
            OutlinedTextField(
                value = selectedItem?.let(itemToString) ?: "",
                onValueChange = {},
                label = label,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true)
            )
        } else {
            TextField(
                value = selectedItem?.let(itemToString) ?: "",
                onValueChange = {},
                label = label,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true)
            )
        }
        ExposedDropdownMenu(expanded, { onExpandedChange(false) }) {
            items.forEach {
                DropdownMenuItem(
                    text = { itemToMenuContent(it) },
                    onClick = {
                        onSelect(it)
                        onExpandedChange(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
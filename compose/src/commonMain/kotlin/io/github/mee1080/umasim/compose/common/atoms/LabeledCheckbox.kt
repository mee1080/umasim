package io.github.mee1080.umasim.compose.common.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LabeledCheckbox(
    selected: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    label: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.clickable(enabled) { onCheckedChange?.invoke(!selected) }.padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(selected, onCheckedChange, Modifier, enabled, colors, interactionSource)
        Box(
            contentAlignment = Alignment.CenterStart,
        ) {
            label()
        }
    }
}

package io.github.mee1080.umasim.compose.common.parts

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WithTooltip(
    tooltip: String,
    modifier: Modifier = Modifier,
    isPersistent: Boolean = true,
    content: @Composable () -> Unit,
) {
    WithTooltip({ Text(tooltip) }, modifier, isPersistent, content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithTooltip(
    tooltip: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isPersistent: Boolean = true,
    content: @Composable () -> Unit,
) {
    Box(modifier) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                TooltipAnchorPosition.Above,
            ),
            tooltip = { PlainTooltip(content = tooltip) },
            state = rememberTooltipState(isPersistent = isPersistent),
            modifier = modifier,
            content = content,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithRichTooltip(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    action: @Composable () -> Unit = {},
    isPersistent: Boolean = true,
    content: @Composable () -> Unit,
) {
    Box(modifier) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                TooltipAnchorPosition.Above,
            ),
            state = rememberTooltipState(isPersistent = isPersistent),
            tooltip = {
                RichTooltip(
                    title = title,
                    action = action,
                    text = text,
                )
            },
            content = content,
        )
    }
}

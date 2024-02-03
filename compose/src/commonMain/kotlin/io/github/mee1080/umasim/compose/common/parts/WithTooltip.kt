package io.github.mee1080.umasim.compose.common.parts

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.mee1080.umasim.compose.common.lib.asComposable
import kotlinx.coroutines.delay

@Composable
fun WithTooltip(
    tooltip: String,
    modifier: Modifier = Modifier,
    delayTime: Long = 0L,
    content: @Composable WithTooltipScope.() -> Unit,
) {
    WithTooltip(tooltip.asComposable(), modifier, delayTime, content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithTooltip(
    tooltip: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    delayTime: Long = 0L,
    content: @Composable WithTooltipScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val tooltipState = remember { PlainTooltipState() }
    val hovered by interactionSource.collectIsHoveredAsState()
    LaunchedEffect(hovered) {
        if (hovered) {
            if (delayTime > 0L) delay(delayTime)
            tooltipState.show()
        } else {
            tooltipState.dismiss()
        }
    }
    Box(modifier) {
        PlainTooltipBox(
            tooltip = tooltip,
            tooltipState = tooltipState,
        ) {
            WithTooltipScopeImpl(this, interactionSource).content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithPersistentTooltip(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    action: @Composable () -> Unit = {},
    delayTime: Long = 0L,
    content: @Composable WithTooltipScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val tooltipState = remember { RichTooltipState() }
    val hovered by interactionSource.collectIsHoveredAsState()
    LaunchedEffect(hovered) {
        if (hovered) {
            if (delayTime > 0L) delay(delayTime)
            tooltipState.show()
        } else {
            tooltipState.dismiss()
        }
    }
    Box(modifier) {
        val localContentColor = LocalContentColor.current
        val localTextStyle = LocalTextStyle.current
        RichTooltipBox(
            text = text,
            tooltipState = tooltipState,
            title = title,
            action = {
                CompositionLocalProvider(
                    LocalContentColor provides localContentColor,
                    LocalTextStyle provides localTextStyle,
                    content = action,
                )
            },
        ) {
            WithTooltipScopeImpl(this, interactionSource).content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
interface WithTooltipScope : TooltipBoxScope {
    fun Modifier.applyTooltip(): Modifier
}

@OptIn(ExperimentalMaterial3Api::class)
private class WithTooltipScopeImpl(
    parent: TooltipBoxScope,
    private val interactionSource: MutableInteractionSource,
) : WithTooltipScope, TooltipBoxScope by parent {

    override fun Modifier.applyTooltip(): Modifier {
        return tooltipAnchor().hoverable(interactionSource)
    }
}

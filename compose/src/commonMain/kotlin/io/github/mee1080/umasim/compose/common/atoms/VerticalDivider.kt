package io.github.mee1080.umasim.compose.common.atoms

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
    dragAreaWidth: Dp = 16.dp,
    onDrag: ((Float) -> Unit)? = null,
) {
    val targetThickness = if (thickness == Dp.Hairline) {
        (1f / LocalDensity.current.density).dp
    } else {
        thickness
    }
    if (onDrag == null) {
        Box(
            modifier
                .fillMaxHeight()
                .width(targetThickness)
                .background(color)
        )
    } else {
        Box(
            modifier
                .fillMaxHeight()
                .width(targetThickness + dragAreaWidth)
                .onDrag { onDrag(it.x) }
                .pointerHoverIcon(PointerIcon.Hand)
                .padding(horizontal = dragAreaWidth / 2)
                .background(color)
        )
    }
}
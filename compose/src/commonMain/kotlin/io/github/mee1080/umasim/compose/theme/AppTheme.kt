package io.github.mee1080.umasim.compose.theme

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.LoadedFont
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.lib.defaultFontResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AppTheme(
    fontResource: String = defaultFontResource,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val fontPath = "font/$fontResource"
    val font = Font(FontResource(fontPath))
    if (font is LoadedFont && font.identity == fontPath) {
        val fontFamily = FontFamily(font)
        val typography = Typography(
            displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = fontFamily),
            displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = fontFamily),
            displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = fontFamily),
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = fontFamily),
            titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = fontFamily),
            titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = fontFamily),
            titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = fontFamily),
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = fontFamily),
            bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = fontFamily),
            bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = fontFamily),
            labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = fontFamily),
            labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = fontFamily),
            labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = fontFamily),
        )

        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
        ) {
            CompositionLocalProvider(
                LocalScrollbarStyle provides LocalScrollbarStyle.current.copy(thickness = 16.dp),
                content = content
            )
        }
    } else {
        Box(Modifier.fillMaxSize()) {
            Text("Loading...", modifier = Modifier.align(Alignment.Center))
        }
    }
}
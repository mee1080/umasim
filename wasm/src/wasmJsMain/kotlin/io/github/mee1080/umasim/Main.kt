import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.mee1080.umasim.ui.App
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
fun main() {
    CanvasBasedWindow("ウマ娘レースエミュレータ") {
        var fontFamily by remember { mutableStateOf<FontFamily?>(null) }
        LaunchedEffect(Unit) {
            runCatching {
                resource("NotoSansJP-Regular.ttf").readBytes()
            }.onSuccess {
                fontFamily = FontFamily(
                    Font(
                        identity = "NotoSansJP-Regular",
                        data = it,
                    )
                )
            }
        }
        fontFamily?.let {
            MaterialTheme(
                typography = Typography(
                    defaultFontFamily = it
                )
            ) {
                ProvideTextStyle(LocalTextStyle.current.copy(letterSpacing = 0.sp)) {
                    App()
                }
            }
        } ?: Text("Loading...")
    }
}

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("WasmTest") {
        androidx.compose.material.MaterialTheme(
//            colorScheme = androidx.compose.material3.MaterialTheme.colorScheme.copy(
//                background = Color(0xFFFFFFFF),
//                onBackground = Color(0xFF19191C),
//            )
        ) {
            ProvideTextStyle(LocalTextStyle.current.copy(letterSpacing = 0.sp)) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column {
                        Text("Hello, Wasm!")
                    }
                }
            }
        }
    }
}
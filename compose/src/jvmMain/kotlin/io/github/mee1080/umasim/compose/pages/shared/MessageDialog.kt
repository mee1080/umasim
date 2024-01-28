//package io.github.mee1080.umasim.compose.pages.shared
//
//import androidx.compose.desktop.ui.tooling.preview.Preview
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.DpSize
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.DialogState
//import androidx.compose.ui.window.DialogWindow
//import com.example.composebp.compose.theme.AppTheme
//import com.example.composebp.ui.framework.OperationDispatcher
//import com.example.composebp.ui.operation.CloseDialog
//import com.example.composebp.ui.state.AppState
//import com.example.composebp.ui.state.MessageDialogState
//
//@Composable
//actual fun MessageDialog(
//    state: MessageDialogState?,
//    receiver: OperationDispatcher<AppState>,
//) {
//    if (state == null) return
//    DialogWindow(
//        onCloseRequest = { receiver(CloseDialog()) },
//        state = DialogState(
//            size = DpSize(200.dp, 160.dp),
//        ),
//        title = "Message",
//    ) {
//        MessageDialogContent(state, receiver)
//    }
//}
//
//@Composable
//private fun MessageDialogContent(
//    dialog: MessageDialogState,
//    receiver: OperationDispatcher<AppState>,
//) {
//    Box(Modifier.fillMaxSize().padding(8.dp)) {
//        Text(dialog.message, Modifier.align(Alignment.TopCenter))
//        Button(
//            modifier = Modifier.align(Alignment.BottomCenter),
//            onClick = { dialog.onPositive()?.let { receiver(it) } },
//        ) {
//            Text("OK")
//        }
//    }
//}
//
//@Preview
//@Composable
//fun Empty() {
//    MessageDialog(null, OperationDispatcher.empty())
//}
//
//@Preview
//@Composable
//fun Message() {
//    AppTheme {
//        Box(
//            Modifier
//                .padding(32.dp)
//                .size(200.dp, 160.dp)
//                .border(2.dp, Color.Gray)
//        ) {
//            MessageDialogContent(
//                MessageDialogState(
//                    message = "テスト",
//                ), OperationDispatcher.empty()
//            )
//        }
//    }
//}
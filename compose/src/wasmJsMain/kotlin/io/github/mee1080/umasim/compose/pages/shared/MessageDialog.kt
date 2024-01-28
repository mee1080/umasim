//package io.github.mee1080.umasim.compose.pages.shared
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import kotlinx.browser.window
//
//@Composable
//actual fun MessageDialog(
//    state: MessageDialogState?,
//    receiver: OperationDispatcher<AppState>
//) {
//    if (state == null) return
//    LaunchedEffect(state) {
//        val onPositive = state.onPositive()
//        if (onPositive == null) {
//            window.alert(state.message)
//            receiver(CloseDialog())
//        } else {
//            if (window.confirm(state.message)) {
//                receiver(onPositive)
//            }
//            receiver(CloseDialog())
//        }
//    }
//}
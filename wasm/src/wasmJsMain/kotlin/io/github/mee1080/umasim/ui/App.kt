package io.github.mee1080.umasim.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import io.github.mee1080.umasim.store.AppContextImpl
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.framework.StateHolder
import io.github.mee1080.umasim.ui.blocks.CharaInput
import io.github.mee1080.umasim.ui.blocks.Footer
import io.github.mee1080.umasim.ui.blocks.Header
import kotlinx.coroutines.Dispatchers

@Composable
fun App() {
    val stateHolder = remember {
        StateHolder(AppContextImpl(), Dispatchers.Main, Dispatchers.Default, AppState())
    }
    val coroutineScope = rememberCoroutineScope()
    val dispatch = remember { OperationDispatcher(stateHolder, coroutineScope) }
    val state = stateHolder.state.collectAsState().value
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Header()
            CharaInput(state, dispatch)
            Footer()
        }
    }
}
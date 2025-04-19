package io.github.mee1080.umasim.compose

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.mee1080.umasim.compose.common.lib.asyncDispatcher
import io.github.mee1080.umasim.compose.common.lib.mainDispatcher
import io.github.mee1080.umasim.compose.pages.race.RacePage
import io.github.mee1080.umasim.compose.theme.AppTheme
import io.github.mee1080.umasim.race.data.loadRecentEventTrackList
import io.github.mee1080.umasim.race.data2.loadSkillData
import io.github.mee1080.umasim.store.AppContextImpl
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.framework.StateHolder
import io.github.mee1080.umasim.store.loadSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun App() {
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        listOf(
            launch(asyncDispatcher) { loadRecentEventTrackList() },
            launch(asyncDispatcher) { loadSkillData() },
        ).forEach {
            it.join()
        }
        loading = false
    }
    AppTheme(loading, darkTheme = false) {
        val stateHolder = remember {
            StateHolder(AppContextImpl(), mainDispatcher, asyncDispatcher, AppState().loadSetting())
        }
        val coroutineScope = rememberCoroutineScope()
        val dispatch = remember { OperationDispatcher(stateHolder, coroutineScope) }
        val state = stateHolder.state.collectAsState().value

        val scrollState = rememberScrollState()
        Row(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxHeight().weight(1f).verticalScroll(scrollState),
            ) {
                RacePage(state, dispatch)
            }
            VerticalScrollbar(rememberScrollbarAdapter(scrollState), Modifier.fillMaxHeight())
        }
    }
}
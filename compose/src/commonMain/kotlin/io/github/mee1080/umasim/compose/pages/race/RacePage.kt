package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher

@Composable
fun RacePage(state: AppState, dispatch: OperationDispatcher<AppState>) {
    Column(
        Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header()
        CharaInput(state, dispatch)
        CourseInput(state, dispatch)
        SkillInput(state, dispatch)
        Footer()
    }
}
package io.github.mee1080.umasim.store

import androidx.compose.runtime.Stable
import io.github.mee1080.umasim.race.calc2.RaceSetting
import io.github.mee1080.umasim.store.framework.State

@Stable
data class AppState(
    val setting: RaceSetting = RaceSetting(),
) : State
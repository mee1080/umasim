package io.github.mee1080.umasim.store

import androidx.compose.runtime.Stable
import io.github.mee1080.umasim.race.calc2.RaceSetting
import io.github.mee1080.umasim.store.framework.State

const val NOT_SELECTED = "(未選択)"

@Stable
data class AppState(
    val setting: RaceSetting = RaceSetting(),
    val charaName:String = NOT_SELECTED,
    val skillIdSet: Set<String> = emptySet(),
) : State
package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.calc2.RaceSetting
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.saveSetting

internal fun AppState.updateSetting(action: (RaceSetting) -> RaceSetting): AppState {
    return copy(setting = action(setting)).also { it.saveSetting() }
}
package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.UmaStatus
import io.github.mee1080.umasim.race.data.Style
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.DirectOperation

private fun AppState.updateUmaStatus(action: (UmaStatus) -> UmaStatus): AppState {
    return copy(setting = setting.copy(umaStatus = action(setting.umaStatus)))
}

fun setStatus(
    speed: Int? = null,
    stamina: Int? = null,
    power: Int? = null,
    guts: Int? = null,
    wisdom: Int? = null,
) = DirectOperation<AppState> { state ->
    state.updateUmaStatus {
        it.copy(
            speed = speed ?: it.speed,
            stamina = stamina ?: it.stamina,
            power = power ?: it.power,
            guts = guts ?: it.guts,
            wisdom = wisdom ?: it.wisdom,
        )
    }
}

fun setStyle(style: Style) = DirectOperation<AppState> { state ->
    state.updateUmaStatus { it.copy(style = style) }
}

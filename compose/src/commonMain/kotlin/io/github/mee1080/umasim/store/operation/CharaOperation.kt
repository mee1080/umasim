package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.calc2.UmaStatus
import io.github.mee1080.umasim.race.data.Condition
import io.github.mee1080.umasim.race.data.FitRank
import io.github.mee1080.umasim.race.data.Style
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.DirectOperation

private fun AppState.updateUmaStatus(action: (UmaStatus) -> UmaStatus): AppState {
    return updateSetting { it.copy(umaStatus = action(it.umaStatus)) }
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

fun setFit(
    surface: FitRank? = null,
    distance: FitRank? = null,
    style: FitRank? = null,
) = DirectOperation<AppState> { state ->
    state.updateUmaStatus {
        it.copy(
            surfaceFit = surface ?: it.surfaceFit,
            distanceFit = distance ?: it.distanceFit,
            styleFit = style ?: it.styleFit,
        )
    }
}

fun setCondition(condition: Condition) = DirectOperation<AppState> { state ->
    state.updateUmaStatus { it.copy(condition = condition) }
}
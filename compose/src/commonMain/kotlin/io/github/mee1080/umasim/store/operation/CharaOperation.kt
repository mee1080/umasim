package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.calc2.UmaStatus
import io.github.mee1080.umasim.race.data.Condition
import io.github.mee1080.umasim.race.data.FitRank
import io.github.mee1080.umasim.race.data.Style
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.DirectOperation

fun AppState.updateUmaStatus(virtual: Boolean, action: (UmaStatus) -> UmaStatus): AppState {
    return if (virtual) {
        updateSetting { it.copy(virtualLeader = action(it.virtualLeader)) }
    } else {
        updateSetting { it.copy(umaStatus = action(it.umaStatus)) }
    }
}

fun setStatus(
    virtual: Boolean,
    speed: Int? = null,
    stamina: Int? = null,
    power: Int? = null,
    guts: Int? = null,
    wisdom: Int? = null,
) = DirectOperation<AppState> { state ->
    state.updateUmaStatus(virtual) {
        it.copy(
            speed = speed ?: it.speed,
            stamina = stamina ?: it.stamina,
            power = power ?: it.power,
            guts = guts ?: it.guts,
            wisdom = wisdom ?: it.wisdom,
        )
    }
}

fun setStyle(virtual: Boolean, style: Style) = DirectOperation<AppState> { state ->
    state.updateUmaStatus(virtual) { it.copy(style = style) }
}

fun setFit(
    virtual: Boolean,
    surface: FitRank? = null,
    distance: FitRank? = null,
    style: FitRank? = null,
) = DirectOperation<AppState> { state ->
    state.updateUmaStatus(virtual) {
        it.copy(
            surfaceFit = surface ?: it.surfaceFit,
            distanceFit = distance ?: it.distanceFit,
            styleFit = style ?: it.styleFit,
        )
    }
}

fun setCondition(virtual: Boolean, condition: Condition) = DirectOperation<AppState> { state ->
    state.updateUmaStatus(virtual) { it.copy(condition = condition) }
}

fun setPopularity(virtual: Boolean, value: Int) = DirectOperation<AppState> { state ->
    state.updateUmaStatus(virtual) { it.copy(popularity = value) }
}

fun setGateNumber(virtual: Boolean, value: Int) = DirectOperation<AppState> { state ->
    state.updateUmaStatus(virtual) { it.copy(gateNumber = value) }
}

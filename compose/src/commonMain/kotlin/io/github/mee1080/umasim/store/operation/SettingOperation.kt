package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.data.RandomPosition
import io.github.mee1080.umasim.race.data.SkillActivateAdjustment
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.DirectOperation

fun setSimulationCount(value: Int) = DirectOperation<AppState> { state ->
    state.copy(simulationCount = value)
}

fun setSkillActivateAdjustment(value: SkillActivateAdjustment) = DirectOperation<AppState> { state ->
    state.updateSetting { it.copy(skillActivateAdjustment = value) }
}

fun setRandomPosition(value: RandomPosition) = DirectOperation<AppState> { state ->
    state.updateSetting { it.copy(randomPosition = value) }
}

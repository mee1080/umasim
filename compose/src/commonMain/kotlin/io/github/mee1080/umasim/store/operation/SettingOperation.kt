package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.data.PositionKeepMode
import io.github.mee1080.umasim.race.data.RandomPosition
import io.github.mee1080.umasim.race.data.SkillActivateAdjustment
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.SimulationMode
import io.github.mee1080.umasim.store.framework.DirectOperation
import io.github.mee1080.umasim.store.saveSetting

fun setSimulationCount(value: Int) = DirectOperation<AppState> { state ->
    state.copy(simulationCount = value).also { it.saveSetting() }
}

fun setSkillActivateAdjustment(value: SkillActivateAdjustment) = DirectOperation<AppState> { state ->
    state.updateSetting { it.copy(skillActivateAdjustment = value) }
}

fun setRandomPosition(value: RandomPosition) = DirectOperation<AppState> { state ->
    state.updateSetting { it.copy(randomPosition = value) }
}

fun setSimulationMode(value: SimulationMode) = DirectOperation<AppState> { state ->
    state.copy(
        simulationMode = value,
        contributionTargets = state.contributionTargets.filterNot { it.startsWith("/") }.toSet(),
    ).also { it.saveSetting() }
}

fun setContributionTarget(id: String, value: Boolean) = DirectOperation<AppState> { state ->
    if (value) {
        state.copy(contributionTargets = state.contributionTargets + id)
    } else {
        state.copy(contributionTargets = state.contributionTargets - id)
    }.also { it.saveSetting() }
}

fun setPositionKeepMode(value: PositionKeepMode) = DirectOperation<AppState> { state ->
    state.updateSetting { it.copy(positionKeepMode = value) }
}

fun setPositionKeepRate(value: Int) = DirectOperation<AppState> { state ->
    state.updateSetting { it.copy(positionKeepRate = value) }
}

fun setFullSpurtCoef(value: Double) = DirectOperation<AppState> { state ->
    state.updateSetting { it.copy(fullSpurtCoef = value) }
}

fun setThreadCount(value: Int) = DirectOperation<AppState> { state ->
    state.copy(threadCount = value).also { it.saveSetting() }
}

fun setSkillLaneChangeRate(value: Double) = DirectOperation<AppState> { state ->
    state.updateSystemSetting { it.copy(skillLaneChangeRate = value) }
}

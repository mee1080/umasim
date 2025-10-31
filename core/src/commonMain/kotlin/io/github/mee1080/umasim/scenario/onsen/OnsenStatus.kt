package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState

fun SimulationState.updateOnsenStatus(update: OnsenStatus.() -> OnsenStatus): SimulationState {
    val onsenStatus = this.onsenStatus ?: return this
    return copy(scenarioStatus = onsenStatus.update())
}

enum class StratumType {
    SAND,
    SOIL,
    ROCK
}

data class OnsenStatus(
    val excavatedGensen: Set<String> = emptySet(),
    val equipmentLevel: Map<StratumType, Int> = mapOf(
        StratumType.SAND to 1,
        StratumType.SOIL to 1,
        StratumType.ROCK to 1
    ),
    val onsenTicket: Int = 0,
    val excavationProgress: Map<StratumType, Int> = mapOf(
        StratumType.SAND to 0,
        StratumType.SOIL to 0,
        StratumType.ROCK to 0
    ),
    val selectedGensen: Gensen? = null,
    val superRecoveryAvailable: Boolean = false,
    val hoshinaRank: Int = 0,
) : ScenarioStatus {

    constructor(support: List<SupportCard>) : this(
        hoshinaRank = support.firstOrNull { it.chara == "保科健子" }?.rarity ?: 0
    )

    val ryokanRank: Int
        get() = when (excavatedGensen.size) {
            in 0..2 -> 1
            in 3..4 -> 2
            5 -> 3
            6 -> 4
            else -> if ("伝説の秘湯" in excavatedGensen) 5 else 4
        }
}

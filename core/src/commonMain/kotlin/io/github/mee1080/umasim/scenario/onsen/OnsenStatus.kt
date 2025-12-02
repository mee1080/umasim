package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.simulation2.*

fun SimulationState.updateOnsenStatus(update: OnsenStatus.() -> OnsenStatus): SimulationState {
    val onsenStatus = this.onsenStatus ?: return this
    return copy(scenarioStatus = onsenStatus.update())
}

suspend fun SimulationState.selectGensen(selector: ActionSelector): SimulationState {
    val onsenStatus = onsenStatus ?: return this

    val gensenCandidates = gensenData.values
        .filter { it.turn < turn && it !in onsenStatus.excavatedGensen }
        .map { OnsenSelectGensen(it) }
    val selectedGensen = selector.select(this, gensenCandidates) as OnsenSelectGensen
    val newState = OnsenCalculator.applyScenarioAction(this, selectedGensen.result)

    val equipmentCandidates = StratumType.entries
        .filter { (onsenStatus.equipmentLevel[it] ?: 0) < 6 }
        .map { OnsenSelectEquipment(it) }
    val selectedEquipment = selector.select(this, equipmentCandidates) as OnsenSelectEquipment
    return OnsenCalculator.applyScenarioAction(newState, selectedEquipment.result)
}

data class OnsenStatus(
    val selectedGensen: Gensen? = null,
    val digProgress: Int = 0,
    val excavatedGensen: Set<Gensen> = setOf(gensenData["ゆこまの湯"]!!),
    val suspendedGensen: Map<String, Int> = emptyMap(),
    val equipmentLevel: Map<StratumType, Int> = mapOf(
        StratumType.SAND to 1,
        StratumType.SOIL to 1,
        StratumType.ROCK to 1
    ),
    val onsenTicket: Int = 2,
    val superRecoveryUsedHp: Int = 0,
    val superRecoveryAvailable: Boolean = false,
    val onsenActiveTurn: Int = 0,
    val hoshinaRarity: Int = 0,
    val factorDigPower: Map<StratumType, Int> = emptyMap(),
    val excavatedGensenContinuousEffect: GensenContinuousEffect = GensenContinuousEffect(),
    val totalGensenContinuousEffect: GensenContinuousEffect = GensenContinuousEffect(),
    val fixedDigFinishTurns: List<Int>? = null,
) : ScenarioStatus {

    constructor(support: List<SupportCard>, factor: List<Pair<StatusType, Int>>) : this(
        hoshinaRarity = support.firstOrNull { it.chara == "保科健子" }?.rarity ?: 0,
        factorDigPower = StratumType.entries.associateWith { stratumType ->
            stratumToStatus[stratumType]!!.mapIndexed { index, type ->
                factor.count { it.first == type } * factorToDigPower[index]
            }.sum() + stratumToScenarioLink[stratumType]!!.count { name ->
                support.any { it.chara == name }
            } * 10
        },
    )

    val ryokanRank
        get() = when (excavatedGensen.size) {
            7, 8, 9, 10 -> if (excavatedGensen.any { it.name == "伝説の秘湯" }) 4 else 3
            6 -> 3
            5 -> 2
            4, 3 -> 1
            else -> 0
        }

    val ryokanBonus get() = ryokanRankBonus[ryokanRank]

    val currentStratum by lazy {
        if (selectedGensen == null) return@lazy null
        var totalProgress = 0
        for (stratum in selectedGensen.strata) {
            totalProgress += stratum.second
            if (digProgress < totalProgress) {
                val rest = totalProgress - digProgress
                return@lazy Triple(stratum.first, stratum.second - rest, rest)
            }
        }
        null
    }

    val nextStratumType by lazy {
        val strata = selectedGensen?.strata ?: return@lazy null
        val current = currentStratum?.first ?: return@lazy null
        strata.getOrNull(strata.indexOfFirst { it.first == current } + 1)?.first
    }

    val totalGensenImmediateEffectHp by lazy {
        excavatedGensen.sumOf { it.immediateEffectHp }
    }

    fun digFinished(turn: Int) = fixedDigFinishTurns?.firstOrNull()?.let { turn >= it }
        ?: (digProgress >= (selectedGensen?.totalProgress ?: 0))
}

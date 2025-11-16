package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.onsen.StratumType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

class OnsenActionSelector(
    val option: Option,
) : ActionSelector {

    companion object {
        const val DEBUG = false
        const val FORCE = 10000000.0
    }

    @Serializable
    data class Option(
        val status: Int = 10,
        val skillPt: Int = 100,
        val hp: Int = 1,
        val motivation: Int = 100,
        val relation: Int = 1000,
        val risk: Int = 150,
        val keepHp: Int = 90,
        val dig: Int = 350,

        val gensenOrder: List<String> = listOf(
            "秘湯ゆこま",
            "明晰の湯",
            "疾駆の湯",
            "堅忍の湯",
            "天翔の古湯",
            "剛脚の古湯",
            "駿閃の古湯",
            "伝説の秘湯",
            "健壮の古湯",
        ),

        val equipmentOrder: List<Pair<StratumType, Int>> = listOf(
            StratumType.ROCK to 2,
            StratumType.SAND to 2,
            StratumType.SOIL to 3,
            StratumType.ROCK to 3,
            StratumType.SAND to 6,
            StratumType.SOIL to 6,
        ),

        val forceOutingTurn: List<Int> = listOf(
            29, 43, 46, 55,
        ),

        val forceBathingTurn: List<Int> = listOf(
            12, 22, 25, 27, 30, 32, 34, 36, 38, 40, 44, 47,
            49, 51, 53, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76, 78,
        ),

        val fixBathingStartTurn: Int = 25,

        val keepTicketCount: List<Pair<Int, Int>> = listOf(
            13 to 1,
            20 to 3,
        ),
    ) : SerializableActionSelectorGenerator {
        override fun generateSelector() = OnsenActionSelector(this)
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    override suspend fun select(
        state: SimulationState,
        selection: List<Action>,
    ) = selectWithScore(state, selection).first

    override suspend fun selectWithScore(
        state: SimulationState,
        selection: List<Action>,
    ): Triple<Action, List<Double>, Double> {
        val scores = selection.map { it to calc(state, it) }
        val selected = scores.maxBy { it.second }
        return Triple(selected.first, scores.map { it.second }, selected.second)
    }

    private fun canUseTicket(state: SimulationState): Boolean {
        val onsenStatus = state.onsenStatus ?: return false
        val keep = option.keepTicketCount.lastOrNull { state.turn >= it.first }?.second ?: 0
        if (DEBUG) println("canUseTicket keep=$keep ticket=${onsenStatus.onsenTicket}")
        return onsenStatus.onsenTicket > keep
    }

    private fun calc(state: SimulationState, action: Action): Double {
        return when (action) {
            is OnsenSelectGensen -> calcGensen(action)
            is OnsenSelectEquipment -> calcEquipment(state, action)
            is OnsenBathing -> calcBathing(state)
            is OnsenPR -> 0.0
            is Sleep if state.turn >= 3 -> 0.0
            is Outing -> calcOuting(state, action)
            is Race if !state.isGoalRaceTurn -> 0.0
            else -> calcActionScore(state, action)
        }
    }

    private fun calcGensen(action: OnsenSelectGensen): Double {
        val order = option.gensenOrder.indexOf(action.gensen.name)
        return if (order < 0) 0.0 else 100.0 - order
    }

    private fun calcEquipment(state: SimulationState, action: OnsenSelectEquipment): Double {
        val onsenStatus = state.onsenStatus ?: return 0.0
        val order = option.equipmentOrder.indexOfFirst {
            action.equipment == it.first && onsenStatus.equipmentLevel[it.first]!! < it.second
        }
        return if (order < 0) 0.0 else 100.0 - order
    }

    private fun calcBathing(state: SimulationState): Double {
        if (option.forceBathingTurn.contains(state.turn)) return FORCE
        if (state.turn >= option.fixBathingStartTurn) return 0.0
        if (state.status.hp < 50 && canUseTicket(state)) return FORCE
        return 0.0
    }

    private fun calcOuting(state: SimulationState, action: Outing): Double {
        if (action.support == null) return 0.0
        if (option.forceOutingTurn.contains(state.turn)) return FORCE
        val step = action.support.supportState?.outingStep ?: Int.MAX_VALUE
        val lastFreeStep = 6 - option.forceOutingTurn.size
        if (step > lastFreeStep) return 0.0
        val onsenStatus = state.onsenStatus ?: return 0.0
        if (onsenStatus.onsenTicket <= 1) return FORCE
        return 0.0
    }

    private fun calcActionScore(state: SimulationState, action: Action): Double {
        val total = action.candidates.sumOf { (result, rate) ->
            rate * if (result.success) 100 else option.risk
        }.toDouble()
        return action.candidates.sumOf { (result, rate) ->
            val statusResult = result as? StatusActionResult ?: return@sumOf 0.0
            val statusScore = calcStatusScore(statusResult.status)
            val relationScore = if (action is Training && result.success) {
                action.support.sumOf {
                    if (it.relation < it.card.requiredRelation) option.relation else 0
                }
            } else 0
            val scenarioScore = ((result.scenarioActionParam as? OnsenActionParam)?.digPoint ?: 0) * option.dig
            val restHp = state.status.hp + result.status.hp
            val keepHpFactor = if (restHp < 50 && state.turn < option.fixBathingStartTurn && !canUseTicket(state)) {
                option.keepHp
            } else 100
            val totalScore = (statusScore + relationScore + scenarioScore) * keepHpFactor
            if (DEBUG) println("  $rate ($statusScore + $relationScore + $scenarioScore) * $keepHpFactor = $totalScore $result $rate/$total")
            totalScore * rate / total
        }
    }

    private fun calcStatusScore(status: Status): Int {
        return status.speed * option.status +
                status.stamina * option.status +
                status.power * option.status +
                status.guts * option.status +
                status.wisdom * option.status +
                status.skillPt * option.skillPt +
                status.hp * option.hp +
                status.motivation * option.motivation
    }
}

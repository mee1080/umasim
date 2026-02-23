package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

class BCActionSelector(
    val option: Option,
) : ActionSelector {

    companion object {
        const val DEBUG = true
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
    ) : SerializableActionSelectorGenerator {
        override fun generateSelector() = BCActionSelector(this)
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

    private fun calc(state: SimulationState, action: Action): Double {
        return when (action) {
            is Race if !state.isGoalRaceTurn -> 0.0
            else -> calcActionScore(state, action)
        }
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
            val scenarioScore = 0
            val restHp = state.status.hp + result.status.hp
            val keepHpFactor = if (restHp < 50) option.keepHp else 100
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

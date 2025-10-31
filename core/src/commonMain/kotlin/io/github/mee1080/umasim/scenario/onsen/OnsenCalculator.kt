package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.floor

object OnsenCalculator : ScenarioCalculator {
    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        val onsenStatus = info.onsenStatus ?: return Status()
        val gensenEffect = onsenStatus.selectedGensen?.continuousEffect ?: return Status()

        val trainingEffect = gensenEffect.trainingEffect
        return Status(
            speed = base.speed * trainingEffect / 100,
            stamina = base.stamina * trainingEffect / 100,
            power = base.power * trainingEffect / 100,
            guts = base.guts * trainingEffect / 100,
            wisdom = base.wisdom * trainingEffect / 100,
            skillPt = base.skillPt * trainingEffect / 100,
        )
    }

    override fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> {
        return baseActions.map { action ->
            val excavationPoint = calcExcavationPoint(state, action)
            action.addScenarioActionParam(OnsenActionParam(excavationPoint))
        }
    }

    private fun calcExcavationPoint(state: SimulationState, action: Action): Int {
        val basePoint = when (action) {
            is Training -> 25 + action.member.count { !it.guest }
            is Race -> if (action.goal) 25 else 15
            is Rest -> 15
            is GoOut -> if (action.charaName == "保科健子") 25 else 15
            is PrAction -> 10 + action.member.size
            else -> 0
        }
        if (state.isSummerCamp) return 0

        val power = StratumType.values().sumOf { type ->
            calcExcavationPower(state, type)
        }

        return floor(basePoint * (1 + power / 100.0)).toInt()
    }

    private fun calcExcavationPower(state: SimulationState, type: StratumType): Int {
        val onsenStatus = state.onsenStatus ?: return 0
        val stats = stratumToBaseStats[type] ?: return 0

        val statPower = stats.mapIndexed { index, statusType ->
            val rank = getStatRank(state.status.get(statusType))
            statToExcavationPower[rank][index]
        }.sum()

        val equipmentPower = equipmentLevelBonus[onsenStatus.equipmentLevel[type] ?: 0]

        val factorPower = state.deck.blueFactor.count { it in stats } * 10 // Simplified

        return statPower + equipmentPower + factorPower
    }

    private fun getStatRank(value: Int): Int {
        return when {
            value >= 1200 -> 7
            value >= 1100 -> 6
            value >= 900 -> 5
            value >= 600 -> 4
            value >= 400 -> 3
            value >= 300 -> 2
            value >= 150 -> 1
            else -> 0
        }
    }


    override fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean,
    ): Array<Action> {
        val onsenStatus = state.onsenStatus ?: return emptyArray()
        if (onsenStatus.bathingTickets > 0) {
            return arrayOf(OnsenBathing())
        }
        return emptyArray()
    }

    suspend fun applyScenarioAction(
        state: SimulationState,
        result: OnsenActionResult,
        selector: ActionSelector,
    ): SimulationState {
        return when (result) {
            is OnsenBathingResult -> {
                val onsenStatus = state.onsenStatus ?: return state
                val gensen = onsenStatus.selectedGensen ?: return state
                state.updateOnsenStatus { copy(bathingTickets = bathingTickets - 1) }
                    .addStatus(gensen.immediateEffect)
            }
            else -> state
        }
    }
}

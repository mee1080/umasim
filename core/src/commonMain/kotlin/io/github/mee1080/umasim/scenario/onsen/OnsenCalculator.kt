package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
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
            when (action) {
                // トレーニング
                is Training -> {
                    action.copy(
                        candidates = action.addScenarioActionParam(
                            OnsenActionParam(
                                digPoint = calcDigPoint(state, 25 + action.member.size)
                            )
                        )
                    )
                }

                // PR活動
                is OnsenPR -> {
                    action.copy(
                        digPoint = calcDigPoint(state, 10 + action.member.size),
                    )
                }

                // レース
                is Race -> {
                    action.copy(
                        result = action.result.addScenarioActionParam(
                            OnsenActionParam(
                                digPoint = calcDigPoint(state, if (action.goal) 25 else 15)
                            )
                        )
                    )
                }

                // お休み
                is Sleep -> {
                    action.copy(
                        candidates = action.addScenarioActionParam(
                            OnsenActionParam(
                                digPoint = calcDigPoint(state, 15)
                            )
                        )
                    )
                }

                // お出かけ
                is Outing -> {
                    action.copy(
                        candidates = action.addScenarioActionParam(
                            OnsenActionParam(
                                digPoint = calcDigPoint(state, if (action.support?.charaName == "保科健子") 25 else 15),
                            )
                        )
                    )
                }

                else -> action
            }
        }
    }

    private fun calcDigPoint(state: SimulationState, basePoint: Int): Int {
        if (state.isLevelUpTurn) return 0

        val power = StratumType.entries.sumOf { type ->
            calcDigPower(state, type)
        }

        return floor(basePoint * (1 + power / 100.0)).toInt()
    }

    private fun calcDigPower(state: SimulationState, type: StratumType): Int {
        val onsenStatus = state.onsenStatus ?: return 0
        val stats = stratumToBaseStats[type] ?: return 0

        val statPower = stats.mapIndexed { index, statusType ->
            val rank = getStatRank(state.status.get(statusType))
            statToExcavationPower[rank][index]
        }.sum()

        val equipmentPower = equipmentLevelBonus[onsenStatus.equipmentLevel[type] ?: 0]

        val factorPower = state.factor.count { it.first in stats } * 10 // Simplified

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
        if (onsenStatus.onsenTicket > 0) {
            return arrayOf(OnsenBathing(Status()))
        }
        return emptyArray()
    }

    suspend fun applyScenarioAction(
        state: SimulationState,
        result: OnsenActionResult,
        selector: ActionSelector,
    ): SimulationState {
        // TODO
        return state
    }

    fun applyScenarioActionParam(
        state: SimulationState,
        result: ActionResult,
        params: OnsenActionParam,
    ): SimulationState {
        if (!result.success) return state
        val onsenStatus = state.onsenStatus ?: return state
        val newProgress = onsenStatus.excavationProgress.toMutableMap()
        onsenStatus.selectedGensen?.strata?.keys?.forEach {
            newProgress[it] = (newProgress[it] ?: 0) + params.digPoint
        }
        return state.updateOnsenStatus { copy(excavationProgress = newProgress) }
    }
}

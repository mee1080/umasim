package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.mapIf
import io.github.mee1080.utility.replaced
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min
import kotlin.random.Random
import kotlin.random.nextInt

object OnsenCalculator : ScenarioCalculator {
    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        val onsenStatus = info.onsenStatus ?: return Status()
        if (onsenStatus.onsenActiveTurn == 0) return Status()
        val gensenEffect = onsenStatus.totalGensenContinuousEffect

        val trainingEffect = gensenEffect.trainingEffect
        val friendBonus = if (friendTraining) gensenEffect.friendBonus[info.training.type] ?: 0 else 0
        if (Calculator.DEBUG) {
            println("trainingEffect: $trainingEffect, friendBonus: $friendBonus")
        }
        return Status(
            speed = calcSingleStatus(base.speed, trainingEffect, friendBonus),
            stamina = calcSingleStatus(base.stamina, trainingEffect, friendBonus),
            power = calcSingleStatus(base.power, trainingEffect, friendBonus),
            guts = calcSingleStatus(base.guts, trainingEffect, friendBonus),
            wisdom = calcSingleStatus(base.wisdom, trainingEffect, friendBonus),
            skillPt = calcSingleStatus(base.skillPt, trainingEffect, friendBonus),
        )
    }

    private fun calcSingleStatus(base: Int, trainingEffect: Int, friendBonus: Int): Int {
        return base * (100 + trainingEffect) * (100 + friendBonus) / 10000 - base
    }

    override fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> {
        val onsenStatus = state.onsenStatus ?: return baseActions
        return baseActions.map { action ->
            when (action) {
                // トレーニング
                is Training -> {
                    val digResult = calcDigResult(state, 25 + action.member.size)
                    action.copy(
                        candidates = action.addScenarioActionParam(
                            digResult.copy(digBonus = Status())
                        ).mapIf({ it.first.success }) {
                            (it.first as StatusActionResult).copy(
                                status = it.first.status + digResult.digBonus
                            ) to it.second
                        }
                    )
                }

                // PR活動
                is OnsenPR -> {
                    action.copy(
                        result = action.result.addScenarioActionParam(
                            calcDigResult(state, 10 + action.memberCount).copy(
                                onsenTicket = if (onsenStatus.onsenTicket >= 3) 0 else 1,
                            )
                        ),
                    )
                }

                // レース
                is Race -> {
                    action.copy(
                        result = action.result.addScenarioActionParam(
                            calcDigResult(state, if (action.goal) 25 else 15)
                        )
                    )
                }

                // お休み
                is Sleep -> {
                    action.copy(
                        candidates = action.addScenarioActionParam(
                            calcDigResult(state, 15)
                        )
                    )
                }

                // お出かけ
                is Outing -> {
                    action.copy(
                        candidates = action.addScenarioActionParam(
                            calcDigResult(state, if (action.support?.charaName == "保科健子") 25 else 15),
                        )
                    )
                }

                else -> action
            }
        }
    }

    private fun calcDigResult(state: SimulationState, basePoint: Int): OnsenActionParam {
        if (state.isLevelUpTurn) return OnsenActionParam()
        val onsenStatus = state.onsenStatus ?: return OnsenActionParam()
        val (stratumType, progress, rest) = onsenStatus.currentStratum ?: return OnsenActionParam()

        val power = calcDigPower(state, stratumType)
        var digPoint = floor(basePoint * (100 + power) / 100.0).toInt()
        var digBonus = calcDigBonus(stratumType, progress, rest, digPoint)
        if (digPoint > rest) {
            val next = onsenStatus.nextStratumType
            if (next == null) {
                digPoint = rest
            } else {
                val usedBasePoint = ceil(rest * 100 / (100.0 + power)).toInt()
                val nextBasePoint = basePoint - usedBasePoint
                val nextPower = calcDigPower(state, next)
                val nextDigPoint = floor(nextBasePoint * (100 + nextPower) / 100.0).toInt()
                digPoint = rest + nextDigPoint
                digBonus += calcDigBonus(next, 0, Int.MAX_VALUE, nextDigPoint)
            }
        }

        return OnsenActionParam(
            digPoint = digPoint,
            digBonus = digBonus,
        )
    }

    private fun calcDigPower(state: SimulationState, type: StratumType): Int {
        val onsenStatus = state.onsenStatus ?: return 0
        val stats = stratumToStatus[type] ?: return 0

        val statusPower = stats.mapIndexed { index, statusType ->
            val rank = getStatusRank(state.status.get(statusType))
            statusToDigPower[rank][index]
        }.sum()

        val equipmentPower = equipmentLevelBonus[onsenStatus.equipmentLevel[type] ?: 0]

        val factorPower = onsenStatus.factorDigPower[type] ?: 0

        return statusPower + equipmentPower + factorPower
    }

    fun calcDigBonus(type: StratumType, progress: Int, rest: Int, point: Int): Status {
        val count = (progress + min(rest, point)) / 30 - progress / 30
        return if (count == 0) Status() else digBonus[type]!! * count
    }

    private fun getStatusRank(value: Int): Int {
        return when {
            value >= 1200 -> 7 // UG-
            value >= 1000 -> 6 // S-SS
            value >= 600 -> 5 // B-A
            value >= 400 -> 4 // C
            value >= 300 -> 3 // D
            value >= 200 -> 2 // E
            value >= 100 -> 1 // F
            else -> 0 // G
        }
    }

    override fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean,
    ): Array<Action> {
        val onsenStatus = state.onsenStatus ?: return emptyArray()
        return buildList {
            if (state.turn >= 3) {
                add(OnsenPR(Random.nextInt(0..5), StatusActionResult(Status(6, 6, 6, 6, 6, 15, -20))))
            }
            if (onsenStatus.onsenTicket > 0 && onsenStatus.onsenActiveTurn == 0) {
                add(OnsenBathing(Status()))
            }
        }.toTypedArray()
    }

    fun applyScenarioAction(
        state: SimulationState,
        result: OnsenActionResult,
    ): SimulationState {
        return when (result) {
            is OnsenSelectGensenResult -> {
                state.updateOnsenStatus {
                    val newSuspendedGensen = if (selectedGensen != null && digProgress < selectedGensen.totalProgress) {
                        suspendedGensen + (selectedGensen to digProgress)
                    } else suspendedGensen
                    val progress = suspendedGensen[result.gensen] ?: 0
                    copy(
                        selectedGensen = result.gensen,
                        digProgress = progress,
                        suspendedGensen = newSuspendedGensen,
                        onsenTicket = min(3, onsenTicket + onsenTicketOnDig[hoshinaRarity]),
                    )
                }
            }

            is OnsenSelectEquipmentResult -> {
                state.updateOnsenStatus {
                    copy(equipmentLevel = equipmentLevel.replaced(result.equipment) {
                        equipmentLevel[result.equipment]!! + 1
                    })
                }
            }
        }
    }

    suspend fun applyScenarioActionParam(
        state: SimulationState,
        result: ActionResult,
        params: OnsenActionParam,
        selector: ActionSelector,
    ): SimulationState {
        if (!result.success) return state
        var newState = state
            .updateOnsenStatus {
                copy(
                    onsenTicket = min(3, onsenTicket + params.onsenTicket),
                    digProgress = digProgress + params.digPoint,
                )
            }
            .addStatus(params.digBonus)
        val newOnsenStatus = newState.onsenStatus ?: return newState
        if (newOnsenStatus.digProgress >= (newOnsenStatus.selectedGensen?.totalProgress ?: Int.MAX_VALUE)) {
            // TODO 入浴1ターン目に掘削完了しても、2ターン目の効果は追加されない
            newState = newState.selectGensen(selector)
        }
        return newState
    }

    override fun updateOnAddStatus(
        state: SimulationState,
        status: Status
    ): SimulationState {
        // TODO 超回復判定修正
        return if (status.hp != 0 && Random.nextDouble() < 0.05) {
            state.updateOnsenStatus {
                copy(superRecoveryAvailable = true)
            }
        } else state
    }
}

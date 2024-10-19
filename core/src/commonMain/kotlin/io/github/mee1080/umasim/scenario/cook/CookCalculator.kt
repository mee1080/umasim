package io.github.mee1080.umasim.scenario.cook

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.min

object CookCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        return calcCookStatus(info, base)
    }

    private fun calcCookStatus(
        info: Calculator.CalcInfo,
        base: Status,
    ): Status {
        val cookStatus = info.cookStatus ?: return Status()
        val cookPointEffect = cookStatus.cookPointEffect
        val trainingType = info.training.type
        val dishFactor = cookStatus.activatedDishModified?.let {
            if (it.trainingTarget.contains(trainingType)) it else null
        }?.trainingFactor ?: 0
        return Status(
            speed = calcCookStatusSingle(StatusType.SPEED, base.speed, cookPointEffect, dishFactor),
            stamina = calcCookStatusSingle(StatusType.STAMINA, base.stamina, cookPointEffect, dishFactor),
            power = calcCookStatusSingle(StatusType.POWER, base.power, cookPointEffect, dishFactor),
            guts = calcCookStatusSingle(StatusType.GUTS, base.guts, cookPointEffect, dishFactor),
            wisdom = calcCookStatusSingle(StatusType.WISDOM, base.wisdom, cookPointEffect, dishFactor),
            skillPt = calcCookStatusSingle(StatusType.SKILL, base.skillPt, cookPointEffect, dishFactor),
        )
    }

    private fun calcCookStatusSingle(
        target: StatusType,
        baseValue: Int,
        cookPointEffect: CookPointEffect,
        dishFactor: Int,
    ): Int {
        val trainingFactor = (100 + cookPointEffect.trainingFactor + dishFactor) / 100.0
        val skillFactor = if (target == StatusType.SKILL) (100 + cookPointEffect.skillPtFactor) / 100.0 else 1.0
        val total = (baseValue * trainingFactor * skillFactor).toInt()
        return min(100, total - baseValue)
    }

    override fun applyScenarioRaceBonus(state: SimulationState, base: Status): Status {
        val cookStatus = state.cookStatus ?: return base
        var status = base
        status = status.copy(
            fanCount = (status.fanCount * (1 + cookStatus.cookPointEffect.fanBonus / 100.0)).toInt(),
        )
        val dish = cookStatus.activatedDishModified
        if (dish != null) {
            status = status.copy(
                speed = (status.speed * (1 + dish.raceBonus / 100.0)).toInt(),
                stamina = (status.stamina * (1 + dish.raceBonus / 100.0)).toInt(),
                power = (status.power * (1 + dish.raceBonus / 100.0)).toInt(),
                guts = (status.guts * (1 + dish.raceBonus / 100.0)).toInt(),
                wisdom = (status.wisdom * (1 + dish.raceBonus / 100.0)).toInt(),
                skillPt = (status.skillPt * (1 + dish.raceBonus / 100.0)).toInt(),
            )
        }
        return status
    }

    override fun predictScenarioActionParams(state: SimulationState, baseActions: List<Action>): List<Action> {
        val cookStatus = state.cookStatus ?: return baseActions
        return baseActions.map {
            when (it) {
                is Training -> {
                    val stamp = CookStamp(
                        material = statusTypeToCookMaterial[it.type]!!,
                        fullPower = it.friendTraining,
                        plus = it.member.size + it.support.count { member -> !member.guest && member.isScenarioLink },
                    )
                    val failureStamp = stamp.copy(fullPower = false)
                    it.copy(
                        candidates = it.addScenarioActionParam(
                            CookActionParam(stamp),
                            CookActionParam(failureStamp)
                        )
                    )
                }

                is Sleep -> {
                    it.copy(candidates = it.addScenarioActionParam(CookActionParam(cookStatus.sleepStamp)))
                }

                is Outing -> {
                    val stamp = if (it.support?.charaName == "秋川理事長") {
                        cookStatus.sleepStamp.copy(fullPower = true)
                    } else cookStatus.sleepStamp
                    it.copy(candidates = it.addScenarioActionParam(CookActionParam(stamp)))
                }

                is Race -> {
                    it.copy(result = it.result.addScenarioActionParam(CookActionParam(cookStatus.raceStamp)))
                }

                else -> it
            }
        }
    }

    override fun predictScenarioAction(state: SimulationState, goal: Boolean): Array<Action> {
        return predictCookAction(state, false)
    }

    fun predictCookAction(state: SimulationState, beforeEvent: Boolean): Array<Action> {
        val cookStatus = state.cookStatus ?: return emptyArray()
        return buildList {
            if (cookStatus.activatedDish == null && !beforeEvent) {
                cookStatus.availableDishList.forEach {
                    add(CookActivateDish(it))
                }
            }
            if (!state.isLevelUpTurn) {
                cookStatus.requiredGardenPoint.filter { it.value <= cookStatus.gardenPoint }.forEach {
                    add(CookMaterialLevelUp(it.key, cookStatus.materialLevel[it.key]!! + 1))
                }
            }
        }.toTypedArray()
    }

    override fun updateScenarioTurn(state: SimulationState): SimulationState {
        return state.updateCookStatus {
            updateTurn(state.isGoalRaceTurn, state.isLevelUpTurn, state.isLevelUpTurn || state.turn > 72)
        }
    }
}
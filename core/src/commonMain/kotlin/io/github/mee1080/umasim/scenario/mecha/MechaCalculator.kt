/*
 * Copyright 2024 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.mee1080.umasim.scenario.mecha

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.applyIf
import kotlin.math.max
import kotlin.math.min

object MechaCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean
    ): Status {
        val mechaStatus = info.mechaStatus ?: return Status()
        val learningFactor = mechaStatus.learningTrainingFactors[info.training.type] ?: 0
        val gearFactor = mechaStatus.gearFactor(info.training.type)
        val odMemberCountFactor = if (mechaStatus.overdrive) mechaStatus.odMemberCountBonus * info.member.size else 0
        val friendFactor = if (friendTraining) mechaStatus.friendBonus else 0
        val odBaseBonus = if (mechaStatus.overdrive) 25 else 0
        if (Calculator.DEBUG) println("Mecha: $learningFactor $gearFactor $friendFactor $odBaseBonus $odMemberCountFactor")
        val baseFactor = (10000 + learningFactor) / 10000.0 *
                (10000 + gearFactor) / 10000.0 *
                (100 + friendFactor) / 100.0 *
                (100 + odBaseBonus) / 100.0 *
                (100 + odMemberCountFactor) / 100.0
        return Status(
            speed = calcStatusSingle(mechaStatus, baseFactor, StatusType.SPEED, base.speed),
            stamina = calcStatusSingle(mechaStatus, baseFactor, StatusType.STAMINA, base.stamina),
            power = calcStatusSingle(mechaStatus, baseFactor, StatusType.POWER, base.power),
            guts = calcStatusSingle(mechaStatus, baseFactor, StatusType.GUTS, base.guts),
            wisdom = calcStatusSingle(mechaStatus, baseFactor, StatusType.WISDOM, base.wisdom),
            skillPt = calcStatusSingle(mechaStatus, baseFactor, StatusType.SKILL, base.skillPt),
            hp = if (mechaStatus.overdrive) -(base.hp * mechaStatus.odHpCostDown / 100.0).toInt() else 0,
        )
    }

    private fun calcStatusSingle(
        mechaStatus: MechaStatus,
        baseFactor: Double,
        target: StatusType,
        baseValue: Int,
    ): Int {
        if (baseValue == 0) return 0
        val skillPtFactor = if (target == StatusType.SKILL) mechaStatus.skillPt else 0
        val odStatusBonus = if (mechaStatus.overdrive) mechaStatus.odStatusBonus(target) else 0
        if (Calculator.DEBUG) println("  $target $baseValue $baseFactor $skillPtFactor $odStatusBonus")
        val total = baseValue * baseFactor *
                (100 + skillPtFactor) / 100.0 *
                (100 + odStatusBonus) / 100.0
        return min(100, total.toInt() - baseValue)
    }

    override fun predictScenarioAction(state: SimulationState, goal: Boolean): Array<Action> {
        if (goal) return emptyArray()
        val mechaStatus = state.mechaStatus ?: return emptyArray()
        if (!mechaStatus.overdrive && mechaStatus.overdriveGauge >= 3) {
            return arrayOf(MechaOverdrive)
        }
        return emptyArray()
    }

    override fun predictScenarioActionParams(state: SimulationState, baseActions: List<Action>): List<Action> {
        val mechaStatus = state.mechaStatus ?: return baseActions
        val maxGain = calcMaxLearningLevelGain(state.turn, mechaStatus)
        val raceLearningGain = Status(7, 7, 7, 7, 7).adjustRange(maxGain, 0)
        return baseActions.map {
            when (it) {
                is Training -> {
                    val gear = mechaStatus.gearExists[it.type] ?: false
                    val learningLevel = calcLearningGain(
                        mechaStatus, it.type, state.isLevelUpTurn,
                        it.friendTraining, gear, it.member.size,
                    )
                    it.copy(
                        candidates = it.addScenarioActionParam(
                            MechaActionParam(learningLevel.adjustRange(maxGain, 0), if (gear) 1 else 0),
                        )
                    )
                }

                is Race -> {
                    it.copy(
                        result = it.result.applyIf(state.turn <= 72) {
                            addScenarioActionParam(MechaActionParam(raceLearningGain, 1))
                        }
                    )
                }

                is Sleep -> {
                    it.copy(
                        candidates = it.addScenarioActionParam(
                            MechaActionParam(Status(), 1)
                        )
                    )
                }

                is Outing -> {
                    it.copy(
                        candidates = it.addScenarioActionParam(
                            MechaActionParam(Status(), 1)
                        )
                    )
                }

                else -> it
            }
        }
    }

    fun calcLearningGain(
        mechaStatus: MechaStatus,
        main: StatusType,
        levelUpTurn: Boolean,
        friend: Boolean,
        gear: Boolean,
        count: Int,
    ): Status {
        val base = mechaLearningGain[if (levelUpTurn) 1 else 0][if (friend) 2 else if (gear) 1 else 0]
        val (sub1, sub2) = mechaLearningSubStatus[main]!!
        val odBonus = if (mechaStatus.overdrive) mechaStatus.odLearningLevelBonus else 0
        return Status().add(
            main to calcLearningGainSingle(base[0][count], odBonus + mechaStatus.learningBonus[main]!!),
            sub1 to calcLearningGainSingle(base[1][count], odBonus + mechaStatus.learningBonus[sub1]!!),
            sub2 to calcLearningGainSingle(base[2][count], odBonus + mechaStatus.learningBonus[sub2]!!),
        )
    }

    private fun calcLearningGainSingle(base: Int, bonus: Int): Int {
        return if (bonus == 0) base else max(base + 1, (base * (100 + bonus) / 100.0).toInt())
    }

    private fun calcMaxLearningLevelGain(turn: Int, mechaStatus: MechaStatus): Status {
        val maxLearningLevel = maxLearningLevel(turn)
        return Status().add(
            *trainingType.map { it to maxLearningLevel - mechaStatus.learningLevels[it]!! }.toTypedArray()
        )
    }

    fun applyScenarioAction(
        state: SimulationState,
        scenarioAction: MechaActionParam,
    ): SimulationState {
        return state.updateMechaStatus {
            copy(
                learningLevels = learningLevels.mapValues { (type, value) ->
                    value + scenarioAction.learningLevel.get(type)
                },
                overdriveGauge = min(6, overdriveGauge + scenarioAction.overdriveGage),
            )
        }
    }

    override fun updateScenarioTurn(state: SimulationState): SimulationState {
        return state.updateMechaStatus {
            updateTurn(state.turn)
        }
    }

    override fun getSpecialityRateUp(
        state: SimulationState,
        cardType: StatusType
    ): Int {
        return state.mechaStatus?.specialityRate ?: 0
    }

    override fun getHintFrequencyUp(
        state: SimulationState,
        position: StatusType
    ): Int {
        return state.mechaStatus?.hintFrequency ?: 0
    }

    override fun isAllSupportHint(
        state: SimulationState,
        position: StatusType
    ): Boolean {
        return state.mechaStatus?.allSupportHint ?: false
    }

    override fun getTrainingRelationBonus(
        state: SimulationState,
    ): Int {
        return state.mechaStatus?.trainingRelationBonus ?: 0
    }
}

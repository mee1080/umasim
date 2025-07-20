/*
 * Copyright 2025 mee1080
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
package io.github.mee1080.umasim.scenario.legend

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.applyIf
import io.github.mee1080.utility.mapIf
import io.github.mee1080.utility.replaced
import kotlin.math.min
import kotlin.random.Random

object LegendCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        val legendState = info.legendStatus ?: return Status()
        val trainingType = info.training.type
        val friendMember = info.member.filter {
            it.card.type == trainingType && (it.relation >= 80 || (it.scenarioState as LegendMemberState).bestFriendGauge >= 20)
        }
        val bestFriend = legendState.mastery == LegendMember.Red
        val effect = legendState.getBuffEffect(info.member.size, friendMember.size)
        val friendTrainingReChecked = friendTraining || (bestFriend && info.member.any {
            val memberState = it.scenarioState as LegendMemberState
            memberState.guest && it.card.type == info.training.type && memberState.bestFriendGauge >= 20
        })
        val baseFriendBonus = if (friendTrainingReChecked) effect.friendBonus else 0
        val bestFriendFriendBonus = if (bestFriend) {
            friendMember.sumOf { (it.scenarioState as LegendMemberState).friendBonus }
        } else 0
        val friendFactor = (baseFriendBonus + bestFriendFriendBonus + 100) / 100.0
        val motivationBonus = effect.motivationBonus
        val baseTrainingBonus = effect.trainingBonus
        val bestFriendTrainingBonus = if (bestFriend) info.bestFriendTrainingBonus() else 0
        if (Calculator.DEBUG) println("Legend: friend=$baseFriendBonus/$bestFriendFriendBonus/$friendFactor motivation=$motivationBonus training=$baseTrainingBonus/$bestFriendTrainingBonus")
        val bonus = Calculator.ScenarioCalcBonus(
            friendFactor = friendFactor,
            motivationBonus = motivationBonus,
            trainingBonus = baseTrainingBonus + bestFriendTrainingBonus,
        )
        return Status(
            speed = Calculator.calcTrainingStatus(
                info, StatusType.SPEED, friendTrainingReChecked,
                bonus = bonus, maxValue = 100.0 + base.speed,
            ).toInt() - base.speed,
            stamina = Calculator.calcTrainingStatus(
                info, StatusType.STAMINA, friendTrainingReChecked,
                bonus = bonus, maxValue = 100.0 + base.stamina,
            ).toInt() - base.stamina,
            power = Calculator.calcTrainingStatus(
                info, StatusType.POWER, friendTrainingReChecked,
                bonus = bonus, maxValue = 100.0 + base.power,
            ).toInt() - base.power,
            guts = Calculator.calcTrainingStatus(
                info, StatusType.GUTS, friendTrainingReChecked,
                bonus = bonus, maxValue = 100.0 + base.guts,
            ).toInt() - base.guts,
            wisdom = Calculator.calcTrainingStatus(
                info, StatusType.WISDOM, friendTrainingReChecked,
                bonus = bonus, maxValue = 100.0 + base.wisdom,
            ).toInt() - base.wisdom,
            skillPt = Calculator.calcTrainingStatus(
                info, StatusType.SKILL, friendTrainingReChecked,
                bonus = bonus, maxValue = 100.0 + base.skillPt,
            ).toInt() - base.skillPt,
            hp = base.hp * (100 - effect.hpCost) / 100 - base.hp
        )
    }

    private fun Calculator.CalcInfo.bestFriendTrainingBonus(): Int {
        return member.sumOf { (it.scenarioState as LegendMemberState).trainingBonus }
    }

    override fun predictScenarioActionParams(state: SimulationState, baseActions: List<Action>): List<Action> {
        val trainingLegends = selectTrainingLegends()
        val raceLegend = LegendMember.entries.random()
        return baseActions.map {
            when (it) {
                is Training -> {
                    val param = LegendActionParam(
                        legendMember = trainingLegends[it.type.ordinal],
                        gauge = if (it.friendTraining) 3 else 1,
                    )
                    it.copy(
                        candidates = it.addScenarioActionParam(param, param)
                    )
                }

                is Sleep -> it.copy(
                    candidates = it.addScenarioActionParam(
                        LegendActionParam(
                            legendMember = LegendMember.entries.random(),
                            gauge = 1,
                        )
                    )
                )

                is Outing -> it.copy(
                    candidates = it.addScenarioActionParam(
                        LegendActionParam(
                            legendMember = LegendMember.entries.random(),
                            gauge = 1,
                        )
                    )
                )

                is Race -> it.copy(
                    result = it.result.addScenarioActionParam(
                        LegendActionParam(
                            legendMember = raceLegend,
                            gauge = 1,
                        )
                    )
                )

                else -> it
            }
        }
    }

    private fun selectTrainingLegends(): List<LegendMember> {
        var list: List<LegendMember>
        do {
            list = List(5) { LegendMember.entries.random() }
        } while (!LegendMember.entries.all { list.contains(it) })
        return list
    }

    fun applyScenarioActionParam(
        state: SimulationState,
        action: Action,
        result: ActionResult,
        params: LegendActionParam,
    ): SimulationState {
        val legendStatus = state.legendStatus ?: return state
        if (Calculator.DEBUG) println("T${state.turn} applyScenarioActionParam: ${action.toShortString()} $params")
        return state
            .applyIf(action.turnChange) {
                updateAfterActionBuff(action, result)
                    .applyIf(legendStatus.mastery == LegendMember.Blue) {
                        applyBlueMasteryAction()
                    }
                    .applyIf(legendStatus.mastery == LegendMember.Green) {
                        applyGreenMasteryAction(action)
                    }
                    .applyIf(legendStatus.mastery == LegendMember.Red) {
                        applyRedMasteryAction(action, result)
                    }
            }
            .updateLegendStatus {
                val member = params.legendMember
                val newGauge = min(8, buffGauge[member]!! + params.gauge)
                copy(buffGauge = buffGauge.replaced(member, newGauge))
            }
    }

    private fun SimulationState.applyBlueMasteryAction(): SimulationState {
        val legendStatus = legendStatus ?: return this
        if (Calculator.DEBUG) println("T$turn applyBlueMasteryAction: ${legendStatus.specialStateTurn}")
        return if (legendStatus.specialStateTurn == 1) {
            updateLegendStatus {
                copy(specialStateTurn = -3)
            }.addStatus(Status(motivation = 4), applyScenario = false)
        } else if (legendStatus.specialStateTurn >= 2 && turn != legendStatus.specialStateStartTurn) {
            updateLegendStatus {
                copy(specialStateTurn = specialStateTurn - 1)
            }
        } else this
    }

    private fun SimulationState.applyBlueMasteryAddMotivation(): SimulationState {
        val legendStatus = legendStatus ?: return this
        if (Calculator.DEBUG) println("T$turn applyBlueMasteryAddMotivation: ${legendStatus.specialStateTurn}")
        return if (legendStatus.specialStateTurn == -1) {
            updateLegendStatus {
                copy(specialStateTurn = 3, restContinueCount = 2, specialStateStartTurn = turn)
            }.addStatus(Status(motivation = 5), applyScenario = false)
        } else if (legendStatus.specialStateTurn < 0) {
            updateLegendStatus {
                copy(specialStateTurn = specialStateTurn + 1)
            }
        } else if (legendStatus.restContinueCount > 0 && turn != legendStatus.specialStateStartTurn) {
            updateLegendStatus {
                copy(
                    specialStateTurn = specialStateTurn + 1,
                    restContinueCount = restContinueCount - 1,
                )
            }
        } else this
    }

    private fun SimulationState.updateAfterActionBuff(action: Action, result: ActionResult): SimulationState {
        val legendStatus = legendStatus ?: return this
        var newState = this
        val newBuffList = legendStatus.buffList.map { buffState ->
            val condition = buffState.buff.condition ?: return@map buffState
            if (buffState.coolTime > 0) {
                buffState.copy(coolTime = buffState.coolTime - 1)
            } else {
                buffState.applyIf({ it.enabled && condition.deactivateAfterAction(action, result) }) {
                    copy(enabled = false, coolTime = buff.coolTime)
                }.applyIf({ !it.enabled && it.coolTime == 0 && condition.activateAfterAction(action, result) }) {
                    if (buff.effect.motivationUp > 0) {
                        newState = newState.addStatus(Status(motivation = buff.effect.motivationUp))
                    }
                    if (buff.effect.relationUp > 0 && action is Training) {
                        newState = newState.addRelation(buff.effect.relationUp) { member ->
                            action.member.contains(member)
                        }
                    }
                    if (buff.instant) {
                        copy(coolTime = buff.coolTime)
                    } else {
                        copy(enabled = true)
                    }
                }
            }
        }
        return newState.updateLegendStatus {
            copy(buffList = newBuffList)
        }
    }

    private fun SimulationState.applyGreenMasteryAction(action: Action): SimulationState {
        val legendStatus = legendStatus ?: return this
        return if (legendStatus.specialStateTurn > 0) {
            if (action is Training) {
                val failureRate = action.failureRate + when (legendStatus.specialStateTurn) {
                    7 -> 100
                    6 -> 60
                    5 -> 30
                    else -> 0
                }
                if (failureRate > Random.nextInt(100)) {
                    if (legendStatus.specialStateTurn >= 7) {
                        addAllStatus(35, 50)
                    } else {
                        addAllStatus(5)
                    }.updateLegendStatus {
                        copy(specialStateTurn = -4)
                    }
                } else {
                    updateLegendStatus {
                        copy(specialStateTurn = specialStateTurn + 1)
                    }
                }
            } else if (action is Race && legendStatus.specialStateTurn < 7) {
                updateLegendStatus {
                    copy(specialStateTurn = specialStateTurn + 1)
                }
            } else {
                when (legendStatus.specialStateTurn) {
                    7 -> addAllStatus(35, 50)
                    6 -> addAllStatus(25, 40)
                    5 -> addAllStatus(15, 30)
                    else -> addAllStatus(5)
                }.updateLegendStatus {
                    copy(specialStateTurn = -4)
                }
            }
        } else {
            if (action is Training) {
                if (legendStatus.specialStateTurn == -1) {
                    updateLegendStatus {
                        copy(specialStateTurn = 1)
                    }
                } else {
                    updateLegendStatus {
                        copy(specialStateTurn = legendStatus.specialStateTurn + 1)
                    }
                }
            } else this
        }
    }

    private fun SimulationState.applyRedMasteryAction(action: Action, result: ActionResult): SimulationState {
        if (action !is Training || !result.success) return this
        var newMember = member
        action.member.forEach { member ->
            if (!member.outingType && member.isFriendTraining(action.type)) {
                newMember = newMember.mapIf({ it.index == member.index }) {
                    val newScenarioState = (it.scenarioState as LegendMemberState)
                        .incrementFriendLevel()
                        .applyIf(member.hint) {
                            addRelation(5 + charmBonus)
                        }
                    it.copy(scenarioState = newScenarioState)
                }
            }
        }
        return copy(member = newMember)
    }

    fun applyScenarioAction(state: SimulationState, result: LegendActionResult): SimulationState {
        return state.updateLegendStatus {
            when (result) {
                is LegendSelectBuffResult -> addBuff(result.buff)
                is LegendDeleteBuffResult -> deleteBuff(result.buff)
            }
        }
    }

    override fun updateOnAddStatus(state: SimulationState, status: Status): SimulationState {
        val legendStatus = state.legendStatus ?: return state
        return if (legendStatus.mastery == LegendMember.Blue && status.motivation > 0) {
            state.applyBlueMasteryAddMotivation()
        } else state
    }

    override fun getSpecialityRateUp(
        state: SimulationState,
        cardType: StatusType
    ): Int {
        return state.legendStatus?.baseBuffEffect?.specialtyRate ?: 0
    }

    override fun getPositionRateUp(state: SimulationState): Int {
        return state.legendStatus?.baseBuffEffect?.positionRate ?: 0
    }

    override fun getHintFrequencyUp(
        state: SimulationState,
        position: StatusType
    ): Int {
        return state.legendStatus?.baseBuffEffect?.hintFrequency ?: 0
    }
}

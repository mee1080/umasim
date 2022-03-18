/*
 * Copyright 2021 mee1080
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
package io.github.mee1080.umasim.simulation

import io.github.mee1080.umasim.data.*
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class Simulator(
    val chara: Chara,
    supportCardList: List<SupportCard>,
    trainingList: List<TrainingBase>,
    val option: Option = Option()
) : SimulationState {

    class Option(
        val levelUpTurns: IntArray = intArrayOf(28, 29, 30, 31, 46, 47, 48, 49),
        val raceBonusStatus: Int = 60,
        val raceBonusSkillPt: Int = 700,
    )

    val supportList = supportCardList.mapIndexed { index, supportCard -> Support(index, supportCard) }

    override val supportInfo get() = supportList.map { Action.SupportInfo(it, it.card.type) }

    val trainingInfo = trainingList
        .groupBy { it.type }
        .mapValues { entry -> TrainingInfo(entry.key, entry.value.sortedBy { it.level }) }

    override var turn = 0

    override var status = Status(
        skillPt = 120,
        hp = 100,
        maxHp = 100,
    ) + chara.initialStatus + supportList
        .map { it.initialStatus }.reduce { acc, status -> acc + status } + supportList.sumOf { it.card.race }.let {
        Status(
            speed = option.raceBonusStatus * it / 100,
            stamina = option.raceBonusStatus * it / 100,
            power = option.raceBonusStatus * it / 100,
            guts = option.raceBonusStatus * it / 100,
            wisdom = option.raceBonusStatus * it / 100,
            skillPt = option.raceBonusSkillPt * it / 100
        )
    }

    override val condition = mutableListOf<String>()

    val history = mutableListOf<Action>()

    val summary get() = Summary(status, history, supportList)

    var supportPosition: Map<StatusType, MutableList<Support>> = emptyMap()

    override var selection = listOf<Action>()

    private val isLevelUpTurn get() = option.levelUpTurns.contains(turn)

    private val overrideTrainingLevel get() = if (isLevelUpTurn) 5 else null

    fun getTrainingSelection(type: StatusType) =
        selection.first { it is Action.Training && it.type == type } as Action.Training

    init {
        nextTurn()
    }

    private fun nextTurn() {
        turn++
        status = status.adjustRange()
        do {
            supportPosition = trainingType.associateWith { mutableListOf() }
            supportList.forEach {
                it.checkHintFriend(status.getSupportRelation(it.index))
                supportPosition[it.selectTraining()]?.add(it)
            }
        } while (supportPosition.values.maxOf { it.size } > 5)
        selection = mutableListOf(
            *(trainingType.map {
                calcTrainingResult(trainingInfo[it]!!, supportPosition[it]!!)
            }).toTypedArray(),
            *(if (isLevelUpTurn) arrayOf(
                Action.Sleep(
                    status,
                    Status(hp = 40, motivation = 1) to 1
                )
            ) else arrayOf(
                Action.Sleep(
                    status,
                    Status(hp = 70) to 25,
                    Status(hp = 50) to 62,
                    Status(hp = 30) to 10,
                    Status(hp = 30, motivation = -1) to 3,
                ),
                Action.Outing(
                    status,
                    null,
                    Status(motivation = 2) to 10,
                    Status(hp = 10, motivation = 1) to 4,
                    Status(hp = 20, motivation = 1) to 2,
                    Status(hp = 30, motivation = 1) to 1,
                    Status(hp = 10, motivation = 1) to 8,
                )
            ))
        )
    }

    fun doTraining(type: StatusType) {
        doAction(getTrainingSelection(type))
    }

    fun doSleep() {
        doAction(selection.first { it is Action.Sleep })
    }

    fun doOuting() {
        doAction(selection.first { it is Action.Outing })
    }

    fun doAction(action: Action) {
        val result = randomSelect(*action.resultCandidate)
        action.result = result
        status += result
        if (action is Action.Training) {
            trainingInfo[action.type]?.let { it.count++ }
        }
        history.add(action)
        nextTurn()
    }

    private fun calcTrainingResult(training: TrainingInfo, support: List<Support>): Action {
        val failureRate = calcTrainingFailureRate(training, support)
        val successStatus =
            Calculator.calcTrainingSuccessStatus(
                chara,
                training,
                overrideTrainingLevel,
                status.motivation,
                support,
                supportTypeCount = 1,
                fanCount = 0,
            ) + Status(supportRelation = calcTrainingRelation(condition.contains("愛嬌○"), support))
        val successCandidate = calcTrainingHint(support)
            .map { successStatus + it to 100 - failureRate }.toTypedArray()
        val failureRateValue = failureRate * successCandidate.size
        val failureCandidate = when {
            failureRate == 0 -> {
                emptyArray()
            }
            training.type == StatusType.WISDOM -> {
                arrayOf(Status(hp = successStatus.hp) to failureRateValue)
            }
            failureRate >= 30 -> {
                val target = trainingType.copyOf().apply { shuffle() }.slice(0..1).map { it to -10 }.toTypedArray()
                arrayOf(
                    Status(hp = 10, motivation = -2).add(
                        training.type to -10,
                        *target
                    ) to failureRateValue
                )
            }
            else -> {
                arrayOf(Status(motivation = -1).add(training.type to -5) to failureRateValue)
            }
        }
        return Action.Training(
            status,
            training.type,
            failureRate,
            overrideTrainingLevel ?: training.level,
            isLevelUpTurn,
            support,
            *successCandidate,
            *failureCandidate,
        )
    }

    private fun calcTrainingHint(support: List<Support>): List<Status> {
        val hintSupportList = support.filter { it.hint }
        if (hintSupportList.isEmpty()) return listOf(Status())
        val hintSupport = hintSupportList.random()
        val hintList = hintSupport.card.skills.filter { status.skillHint[it] != 5 }
            .map { Status(skillHint = mapOf(it to 1 + hintSupport.card.hintLevel)) }.toTypedArray()
        return listOf(*hintList, hintSupport.card.hintStatus)
    }

    private fun calcTrainingFailureRate(training: TrainingInfo, support: List<Support>): Int {
        val base = (status.hp - status.maxHp) * (status.hp * 10 - training.failureRate) / 400.0
        val supported = base * support.map { it.card.failureRate }.fold(1.0) { acc, d -> acc * d }
        val supportedInRange = max(0, min(99, ceil(supported).toInt()))
        val conditioned = supportedInRange + arrayOf(
            "練習ベタ" to 2,
            "練習上手○" to -2,
            "小さなほころび" to 5,
            "大輪の輝き" to -2,
        ).sumOf { if (condition.contains(it.first)) it.second else 0 }
        return max(0, min(100, conditioned))
    }

    private fun calcTrainingRelation(charm: Boolean, support: List<Support>): Map<Int, Int> {
        return support.associate {
            it.index to if (it.card.type.outingType) {
                if (charm) 6 else 4
            } else {
                (if (charm) 9 else 7) + (if (it.hint) {
                    if (charm) 7 else 5
                } else 0)
            }
        }
    }
}

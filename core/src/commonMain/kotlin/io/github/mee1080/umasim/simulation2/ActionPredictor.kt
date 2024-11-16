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
package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun SimulationState.predict(): List<Action> {
    val nextGoalRace = nextGoalRace
    val result = if (nextGoalRace?.turn == turn) {
        predictGoal(nextGoalRace)
    } else {
        predictNormal()
    }
    return scenario.calculator.predictScenarioActionParams(this, result)
}

fun SimulationState.predictGoal(goal: RaceEntry): List<Action> {
    return listOf(predictRace(goal)) + scenario.calculator.predictScenarioAction(this, true)
}

fun SimulationState.predictNormal(): List<Action> {
    val supportPosition = trainingType.associateWith { mutableListOf<MemberState>() }
    member.forEach {
        it.positions.forEach { status ->
            supportPosition[status]!!.add(it)
        }
    }
    return mutableListOf(
        *(predictTrainingResult(supportPosition)),
        *(predictSleep()),
        // TODO 出走可否判定、レース後イベント
        *(if (scenario.calculator.normalRaceBlocked(this)) emptyArray() else {
            Store.raceMap.getOrNull(turn)?.map { predictRace(it, false) }?.toTypedArray() ?: emptyArray()
        }),
        *(scenario.calculator.predictScenarioAction(this, false)),
    )
}

private fun SimulationState.StatusActionResult(
    status: Status,
    scenarioActionParam: ScenarioActionParam? = null,
    success: Boolean = true,
) = StatusActionResult(this.status, status, scenarioActionParam, success)

private fun SimulationState.predictTrainingResult(supportPosition: Map<StatusType, List<MemberState>>): Array<Action> {
    return training.map {
        calcTrainingResult(it, supportPosition[it.type] ?: emptyList())
    }.toTypedArray()
}

private fun SimulationState.calcTrainingResult(
    training: TrainingState,
    support: List<MemberState>,
): Action {
    val currentTraining = uafStatus?.getTraining(training.type, isLevelUpTurn) ?: training.current
    return calcTrainingResult(currentTraining, support)
}

fun SimulationState.calcTrainingResult(
    training: TrainingBase,
    support: List<MemberState>,
): Action {
    val failureRate = calcTrainingFailureRate(training, support)
    val (baseStatus, friend) = Calculator.calcTrainingSuccessStatusAndFriendEnabled(
        baseCalcInfo.copy(
            training = training,
            member = support,
        )
    )
    val successStatus = if (itemAvailable) {
        baseStatus + Calculator.calcItemBonus(
            training.type,
            baseStatus,
            enableItem.list,
        )
    } else baseStatus
    val successCandidate = if (liveAvailable && support.any { it.charaName == "ライトハロー" }) {
        val eventRate = ((100 - failureRate) * 0.4).toInt()
        val eventPerformance =
            (status.performance!! + successStatus.performance!!).minimumType.random().asPerformance(20)
        listOf(
            StatusActionResult(successStatus + Status(performance = eventPerformance)) to eventRate,
            StatusActionResult(successStatus) to 100 - failureRate - eventRate,
        )
    } else listOf(StatusActionResult(successStatus) to 100 - failureRate)
    val failureCandidate = when {
        failureRate == 0 -> {
            emptyList()
        }

        training.type == StatusType.WISDOM -> {
            listOf(StatusActionResult(Status(hp = successStatus.hp), success = false) to failureRate)
        }

        failureRate >= 30 -> {
            val target = trainingType.copyOf().apply { shuffle() }
                .slice(0..1).map { it to -10 }.toTypedArray()
            listOf(
                StatusActionResult(
                    Status(hp = 10, motivation = -2).add(training.type to -10, *target),
                    success = false,
                ) to failureRate
            )
        }

        else -> {
            listOf(
                StatusActionResult(
                    Status(motivation = -1).add(training.type to -5),
                    success = false,
                ) to failureRate
            )
        }
    }
    return Training(
        training.type,
        failureRate,
        training.level,
        support,
        successCandidate + failureCandidate,
        baseStatus,
        friend,
    )
}

private fun SimulationState.calcTrainingFailureRate(training: TrainingBase, support: List<MemberState>): Int {
    if (itemAvailable && enableItem.unique?.name == "健康祈願のお守り") return 0
    if (status.hp >= 100) return 0
    val base = (status.hp - 100) * (status.hp * 10 - training.failureRate) / 400.0
    val supported = base * support.map { it.card.failureRate() }.fold(1.0) { acc, d -> acc * d }
    val supportedInRange = max(0, min(99, ceil(supported).toInt()))
    return max(0, min(100, supportedInRange + conditionFailureRate))
}

fun SimulationState.predictSleep(): Array<Action> {
    return scenario.calculator.predictSleep(this) ?: if (isLevelUpTurn) {
        arrayOf(
            Sleep(
                listOf(
                    StatusActionResult(Status(hp = 40, motivation = 1)) to 1
                )
            )
        )
    } else arrayOf(
        Sleep(
            listOf(
                Status(hp = 70) to 25,
                Status(hp = 50) to 62,
                Status(hp = 30) to 10,
                Status(hp = 30, motivation = -1) to 3,
            ).map {
                StatusActionResult(it.first) to it.second
            }
        ),
        Outing(
            null,
            listOf(
                Status(motivation = 2) to 10,
                Status(hp = 10, motivation = 1) to 4,
                Status(hp = 20, motivation = 1) to 2,
                Status(hp = 30, motivation = 1) to 1,
                Status(hp = 10, motivation = 1) to 8,
            ).map {
                StatusActionResult(it.first) to it.second
            }
        ),
        *(support.filter { (it.supportState?.outingStep ?: 0) in 2..6 }.map {
            Outing(it, listOf(StatusActionResult(Status()) to 1))
        }.toTypedArray())
    )
}

fun SimulationState.predictRace(race: RaceEntry, goal: Boolean = true): Race {
    var status = scenario.calculator.calcBaseRaceStatus(this, race, goal)
        ?: if (goal) when (race.grade) {
            RaceGrade.DEBUT -> raceStatus(3, 3, 30)
            RaceGrade.PRE_OPEN -> raceStatus(3, 3, 30)
            RaceGrade.OPEN -> raceStatus(3, 3, 30)
            RaceGrade.G3 -> raceStatus(4, 3, 35)
            RaceGrade.G2 -> raceStatus(4, 3, 35)
            RaceGrade.G1 -> raceStatus(5, 3, 45)
            RaceGrade.FINALS -> when (race.turn) {
                74 -> raceStatus(5, 10, 40)
                76 -> raceStatus(5, 10, 60)
                78 -> raceStatus(5, 10, 80)
                else -> Status()
            }

            RaceGrade.UNKNOWN -> Status()
        } else when (race.grade) {
            RaceGrade.PRE_OPEN -> raceStatus(1, 5, 30)
            RaceGrade.OPEN -> raceStatus(1, 5, 30)
            RaceGrade.G3 -> raceStatus(1, 8, 35)
            RaceGrade.G2 -> raceStatus(1, 8, 35)
            RaceGrade.G1 -> raceStatus(1, 10, 45)
            else -> Status()
        }
    status += Status(
        hp = if (goal) 0 else -15,
        // やる気は3連続出走以上で強制低下扱い
        motivation = if (!goal && continuousRace) -1 else 0,
        fanCount = raceFanCount(race.getFan),
    )
    status = scenario.calculator.applyScenarioRaceBonus(this, status)
    val scenarioParam = scenario.calculator.raceScenarioActionParam(this, race, goal)
    return Race(goal, race.name, race.grade, StatusActionResult(status, scenarioParam))
}

fun SimulationState.raceStatus(count: Int, value: Int, skillPt: Int): Status {
    val bonusValue = value * (100 + totalRaceBonus) / 100
    val targets = randomTrainingType(count).map { it to bonusValue }.toTypedArray()
    return Status(skillPt = skillPt * (100 + totalRaceBonus) / 100).add(*targets)
}

fun SimulationState.raceFanCount(base: Int): Int {
    return (base * Random.nextDouble(0.01, 0.0109) * (100 + totalFanBonus)).toInt()
}

fun MultipleAction.addScenarioActionParam(
    scenarioActionParam: ScenarioActionParam,
    failureScenarioActionParam: ScenarioActionParam? = null,
): List<Pair<ActionResult, Int>> {
    return candidates.map {
        it.first.addScenarioActionParam(
            scenarioActionParam,
            failureScenarioActionParam,
        ) to it.second
    }
}

fun ActionResult.addScenarioActionParam(
    scenarioActionParam: ScenarioActionParam,
    failureScenarioActionParam: ScenarioActionParam? = null,
): ActionResult {
    return when (this) {
        is StatusActionResult -> copy(scenarioActionParam = if (success) scenarioActionParam else failureScenarioActionParam)
        else -> this
    }
}

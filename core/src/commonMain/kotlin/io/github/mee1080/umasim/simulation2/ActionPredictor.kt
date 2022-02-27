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

fun SimulationState.predict(turn: Int): List<Action> {
    return goalRace.find { it.turn == turn }?.let { listOf(predictRace(it)) } ?: predictNormal()
}

fun SimulationState.predictNormal(): List<Action> {
    val supportPosition = member.groupBy { it.position }
    return mutableListOf(
        *(training.map {
            calcTrainingResult(it, supportPosition[it.type] ?: emptyList())
        }).toTypedArray(),
        *(if (isLevelUpTurn) arrayOf(
            Sleep(
                listOf(
                    Status(hp = 40, motivation = 1) to 1
                )
            )
        ) else arrayOf(
            Sleep(
                listOf(
                    Status(hp = 70) to 25,
                    Status(hp = 50) to 62,
                    Status(hp = 30) to 10,
                    Status(hp = 30, motivation = -1) to 3,
                )
            ),
            Outing(
                null,
                listOf(
                    Status(motivation = 2) to 10,
                    Status(hp = 10, motivation = 1) to 4,
                    Status(hp = 20, motivation = 1) to 2,
                    Status(hp = 30, motivation = 1) to 1,
                    Status(hp = 10, motivation = 1) to 8,
                )
            )
        )),
        // TODO 出走可否判定、レース後イベント
//        *(Store.raceMap.getOrNull(turn)?.map { predictRace(it, false) }?.toTypedArray() ?: arrayOf())
    ).map { adjustRange(it) }
}

private fun SimulationState.calcTrainingResult(
    training: TrainingState,
    support: List<MemberState>,
): Action {
    val failureRate = calcTrainingFailureRate(training.current, support)
    val successStatus =
        Calculator.calcTrainingSuccessStatus(
            chara,
            training.current,
            status.motivation,
            support,
            scenario,
            supportTypeCount,
            status.fanCount,
        )
    val successCandidate = listOf(successStatus to 100 - failureRate)
    val failureCandidate = when {
        failureRate == 0 -> {
            emptyList()
        }
        training.type == StatusType.WISDOM -> {
            listOf(Status(hp = successStatus.hp) to failureRate)
        }
        failureRate >= 30 -> {
            val target = trainingType.copyOf().apply { shuffle() }
                .slice(0..1).map { it to -10 }.toTypedArray()
            listOf(
                Status(hp = 10, motivation = -2).add(training.type to -10, *target) to failureRate
            )
        }
        else -> {
            listOf(Status(motivation = -1).add(training.type to -5) to failureRate)
        }
    }
    return Training(
        training.type,
        failureRate,
        training.currentLevel,
        support,
        successCandidate + failureCandidate
    )
}

private fun SimulationState.calcTrainingFailureRate(training: TrainingBase, support: List<MemberState>): Int {
    val base = (status.hp - status.maxHp) * (status.hp * 10 - training.failureRate) / 400.0
    val supported = base * support.map { it.card.failureRate }.fold(1.0) { acc, d -> acc * d }
    val supportedInRange = max(0, min(99, ceil(supported).toInt()))
    return max(0, min(100, supportedInRange + conditionFailureRate))
}

fun SimulationState.adjustRange(action: Action) = action.updateCandidate(
    action.resultCandidate.map { (status + it.first).adjustRange() - status to it.second }
)

fun SimulationState.predictRace(race: RaceEntry, goal: Boolean = true): Race {
    val status = when (race.grade) {
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
    } + Status(
        hp = if (goal) 0 else -15,
        fanCount = (race.getFan * Random.nextDouble(0.01, 0.0109) * (100 + totalFanBonus)).toInt(),
    )
    return Race(goal, race.name, race.grade, listOf(status to 1))
}

fun SimulationState.raceStatus(count: Int, value: Int, skillPt: Int): Status {
    val bonusValue = value * (100 + totalRaceBonus) / 100
    val targets = randomTrainingType(count).map { it to bonusValue }.toTypedArray()
    return Status(skillPt = skillPt * (100 + totalRaceBonus) / 100).add(*targets)
}
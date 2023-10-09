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
import io.github.mee1080.umasim.util.applyIf
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun SimulationState.predict(turn: Int): List<Action> {
    val result = goalRace.find { it.turn == turn }?.let { listOf(predictRace(it)) } ?: predictNormal()
    return predictScenarioActionParams(result)
}

fun SimulationState.predictNormal(): List<Action> {
    val supportPosition = trainingType.associateWith { mutableListOf<MemberState>() }
    member.forEach {
        if (it.position != StatusType.NONE) supportPosition[it.position]!!.add(it)
        if (it.secondPosition != StatusType.NONE) supportPosition[it.secondPosition]!!.add(it)
    }
    return mutableListOf(
        *(training.map {
            calcTrainingResult(it, supportPosition[it.type] ?: emptyList())
        }).toTypedArray(),
        *(predictSSMatch()),
        *(predictSleep()),
        // TODO 出走可否判定、レース後イベント
        *(if (scenario == Scenario.LARC) emptyArray() else {
            Store.raceMap.getOrNull(turn)?.map { predictRace(it, false) }?.toTypedArray() ?: emptyArray()
        }),
    )
}

private fun SimulationState.StatusActionResult(
    status: Status,
    scenarioActionParam: ScenarioActionParam? = null,
    success: Boolean = true,
) = StatusActionResult(this.status, status, scenarioActionParam, success)

private fun SimulationState.calcTrainingResult(
    training: TrainingState,
    support: List<MemberState>,
): Action {
    val failureRate = calcTrainingFailureRate(training.current, support)
    val (baseStatus, friend) = Calculator.calcTrainingSuccessStatusAndFriendEnabled(
        Calculator.CalcInfo(
            chara,
            training.current,
            status.motivation,
            support,
            scenario,
            supportTypeCount,
            status.fanCount,
            status,
            totalRelation,
            // TODO スキルPt160ごとに速度スキル1つ取る想定。ヒント取れるかは知らん。
            min(5, status.skillPt / 160),
            // TODO スキルPt160ごとに回復スキル1つ取る想定。ヒント取れるかは知らん。速度と両方編成するとおかしくなる
            min(3, status.skillPt / 160),
            totalTrainingLevel,
            liveStatus,
            gmStatus,
            lArcStatus,
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
        training.currentLevel,
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
    return if (isLevelUpTurn) {
        if (scenario == Scenario.LARC) {
            arrayOf(
                Sleep(
                    listOf(
                        StatusActionResult(Status(hp = 50, motivation = 1), LArcActionParam(aptitudePt = 100)) to 1
                    )
                )
            )
        } else {
            arrayOf(
                Sleep(
                    listOf(
                        StatusActionResult(Status(hp = 40, motivation = 1)) to 1
                    )
                )
            )
        }
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
        )
    )
}

fun SimulationState.predictRace(race: RaceEntry, goal: Boolean = true): Race {
    val climax = scenario == Scenario.CLIMAX
    var status = if (scenario == Scenario.LARC && race.courseName == "ロンシャン") when (turn) {
        41 -> raceStatus(5, 5, 40)
        43 -> raceStatus(5, 7, 50)
        65 -> raceStatus(5, 7, 40)
        67 -> if (lArcStatus!!.consecutiveVictories >= 2) {
            raceStatus(5, 30, 140)
        } else {
            raceStatus(5, 10, 60)
        }

        else -> throw IllegalArgumentException()
    } else if (goal) when (race.grade) {
        RaceGrade.DEBUT -> raceStatus(3, 3, if (climax) 15 else 30)
        RaceGrade.PRE_OPEN -> raceStatus(3, 3, 30)
        RaceGrade.OPEN -> raceStatus(3, 3, 30)
        RaceGrade.G3 -> raceStatus(4, 3, 35)
        RaceGrade.G2 -> raceStatus(4, 3, 35)
        RaceGrade.G1 -> raceStatus(5, 3, 45)
        RaceGrade.FINALS -> if (scenario == Scenario.GM) {
            raceStatus(5, 20, 80)
        } else {
            when (race.turn) {
                74 -> raceStatus(5, 10, if (climax) 30 else 40)
                76 -> raceStatus(5, 10, if (climax) 30 else 60)
                78 -> raceStatus(5, 10, if (climax) 30 else 80)
                else -> Status()
            }
        }

        RaceGrade.UNKNOWN -> Status()
    } else when (race.grade) {
        RaceGrade.PRE_OPEN -> raceStatus(1, 5, if (climax) 20 else 30)
        RaceGrade.OPEN -> raceStatus(1, 5, if (climax) 20 else 30)
        RaceGrade.G3 -> raceStatus(1, 8, if (climax) 25 else 35)
        RaceGrade.G2 -> raceStatus(1, 8, if (climax) 25 else 35)
        RaceGrade.G1 -> raceStatus(1, 10, if (climax) 35 else 45)
        else -> Status()
    }
    status += Status(
        hp = if (goal) 0 else if (climax) -20 else -15,
        fanCount = raceFanCount(race.getFan),
    )
    status = applyScenarioRaceBonus(status)
    return Race(goal, race.name, race.grade, listOf(StatusActionResult(status) to 1))
}

fun SimulationState.raceStatus(count: Int, value: Int, skillPt: Int): Status {
    val bonusValue = value * (100 + totalRaceBonus) / 100
    val targets = randomTrainingType(count).map { it to bonusValue }.toTypedArray()
    return Status(skillPt = skillPt * (100 + totalRaceBonus) / 100).add(*targets)
}

fun SimulationState.raceFanCount(base: Int): Int {
    return (base * Random.nextDouble(0.01, 0.0109) * (100 + totalFanBonus)).toInt()
}

fun SimulationState.applyScenarioRaceBonus(base: Status): Status {
    var status = base
    if (itemAvailable) {
        status = enableItem.raceBonus?.let {
            status.copy(
                speed = (status.speed * (1 + it.raceFactor / 100.0)).toInt(),
                stamina = (status.speed * (1 + it.raceFactor / 100.0)).toInt(),
                power = (status.speed * (1 + it.raceFactor / 100.0)).toInt(),
                guts = (status.speed * (1 + it.raceFactor / 100.0)).toInt(),
                wisdom = (status.speed * (1 + it.raceFactor / 100.0)).toInt(),
            )
        } ?: status
        status = enableItem.fanBonus?.let {
            status.copy(
                fanCount = (status.fanCount * (1 + it.fanFactor / 100.0)).toInt(),
            )
        } ?: status
    }
    if (gmStatus != null && gmStatus.activeWisdom == Founder.Red) {
        status = status.copy(
            speed = (status.speed * (1 + 35 / 100.0)).toInt(),
            stamina = (status.speed * (1 + 35 / 100.0)).toInt(),
            power = (status.speed * (1 + 35 / 100.0)).toInt(),
            guts = (status.speed * (1 + 35 / 100.0)).toInt(),
            wisdom = (status.speed * (1 + 35 / 100.0)).toInt(),
        )
    }
    return status
}

fun SimulationState.predictScenarioActionParams(baseActions: List<Action>): List<Action> {
    return when (scenario) {
        Scenario.GM -> predictGmScenarioActionParams(baseActions)
        Scenario.LARC -> predictLArcScenarioActionParams(baseActions)
        else -> baseActions
    }
}

private val gmTrainingKnowledgeType by lazy {
    trainingType.associateWith { training ->
        if (training == StatusType.GUTS) {
            trainingTypeOrSkill.map {
                it to if (upInTraining(training, it)) 0.85 / 4 else 0.15 / 2
            }
        } else {
            trainingTypeOrSkill.map {
                it to if (upInTraining(training, it)) 0.8 / 3 else 0.2 / 3
            }
        }
    }
}

fun SimulationState.predictGmScenarioActionParams(baseActions: List<Action>): List<Action> {
    val gmStatus = gmStatus ?: return baseActions
    return if (baseActions.size == 1) {
        baseActions.map {
            (it as Race).copy(
                candidates = it.addScenarioActionParam(
                    GmActionParam(
                        Founder.entries.random(), trainingTypeOrSkill.random(), predictKnowledgeCount(1.0),
                    ),
                ),
            )
        }
    } else {
        val trainingFounders = (Founder.entries + Founder.entries).shuffled()
        val sleepFounders = Founder.entries.shuffled()
        val raceKnowledgeType = trainingTypeOrSkill.random()
        baseActions.map {
            when (it) {
                is Training -> {
                    val knowledgeTypeRate = gmTrainingKnowledgeType[it.type]!!
                    val doubleRate = when {
                        !it.friendTraining -> 0.0
                        it.type == StatusType.WISDOM -> 0.45
                        else -> 1.0
                    }
                    it.copy(
                        candidates = it.addScenarioActionParam(
                            GmActionParam(
                                trainingFounders[it.type.ordinal],
                                randomSelectDouble(knowledgeTypeRate),
                                predictKnowledgeCount(doubleRate),
                            ).applyIf(it.support.any { support -> support.charaName == "ダーレーアラビアン" }) {
                                val eventRate =
                                    if (it.support.first { support -> support.charaName == "ダーレーアラビアン" }.supportState?.passion == true) {
                                        1.0
                                    } else {
                                        0.4 * (100 + gmStatus.wisdomHintFrequency) / 100.0
                                    }
                                copy(knowledgeEventRate = eventRate)
                            }
                        ),
                    )
                }

                is Sleep -> it.copy(
                    candidates = it.addScenarioActionParam(
                        GmActionParam(
                            sleepFounders[0], trainingTypeOrSkill.random(), predictKnowledgeCount(0.2)
                        )
                    ),
                )

                is Outing -> it.copy(
                    candidates = it.addScenarioActionParam(
                        GmActionParam(
                            sleepFounders[1], trainingTypeOrSkill.random(), predictKnowledgeCount(0.2)
                        )
                    ),
                )

                is Race -> it.copy(
                    candidates = it.addScenarioActionParam(
                        GmActionParam(
                            sleepFounders[2], raceKnowledgeType, predictKnowledgeCount(0.2)
                        ),
                    )
                )

                else -> it
            }
        }
    }
}

private fun SimulationState.predictKnowledgeCount(doubleRate: Double): Int {
    return when (gmStatus?.knowledgeFragmentCount) {
        8, null -> 0
        7 -> 1
        else -> randomSelectPercent(doubleRate, 2, 1)
    }
}

private fun SimulationState.predictLArcScenarioActionParams(baseActions: List<Action>): List<Action> {
    return baseActions.map {
        when (it) {
            is Training -> {
                val param = if (isLevelUpTurn) LArcActionParam(
                    aptitudePt = 50 + it.member.size * 20 + it.friendCount * 20 - (if (it.type == StatusType.WISDOM) 20 else 0),
                    mayEventChance = it.member.any { member -> member.charaName == "佐岳メイ" },
                ) else LArcActionParam(
                    mayEventChance = it.member.any { member -> member.charaName == "佐岳メイ" }
                )
                it.copy(candidates = it.addScenarioActionParam(param))
            }

            is Race -> {
                val supporterPt = when (turn) {
                    41, 65 -> 2000
                    43 -> 3000
                    67 -> 0
                    else -> when (it.grade) {
                        RaceGrade.G1 -> 1300
                        RaceGrade.G2 -> 900
                        RaceGrade.G3 -> 700
                        else -> 0
                    }
                }
                it.copy(candidates = it.addScenarioActionParam(LArcActionParam(supporterPt = supporterPt)))
            }

            else -> it
        }
    }
}

private fun SimulationState.predictSSMatch(): Array<Action> {
    val lArcStatus = lArcStatus ?: return emptyArray()
    if (isLevelUpTurn) return emptyArray()
    val joinMember = lArcStatus.ssMatchMember
    if (joinMember.isEmpty()) return emptyArray()
    val supporterRank = (listOf(-1 to lArcStatus.supporterPt) + member
        .filter { !it.outingType }
        .map { it.index to (it.scenarioState as LArcMemberState).supporterPt })
        .sortedByDescending { it.second }
        .mapIndexed { order, (index, _) -> index to order }
        .toMap()

    var status = Status()
    var lArcParam = LArcActionParam()
    joinMember.forEach {
        val memberState = it.scenarioState as LArcMemberState
        val effect = memberState.nextStarEffect[0]
        status += Status(
            speed = memberState.predictSSMatchStatus(StatusType.SPEED, it.isScenarioLink),
            stamina = memberState.predictSSMatchStatus(StatusType.STAMINA, it.isScenarioLink),
            power = memberState.predictSSMatchStatus(StatusType.POWER, it.isScenarioLink),
            guts = memberState.predictSSMatchStatus(StatusType.GUTS, it.isScenarioLink),
            wisdom = memberState.predictSSMatchStatus(StatusType.WISDOM, it.isScenarioLink),
            skillPt = 5,
        ) + when (effect) {
            StarEffect.Status -> Status().add(memberState.starType to (if (it.isScenarioLink) 15 else 10))
            StarEffect.SkillPt -> Status(skillPt = 20)
            StarEffect.Hp -> Status(hp = 20)
            StarEffect.MaxHp -> Status(hp = 20, maxHp = 4)
            StarEffect.Motivation -> Status(hp = 20, motivation = 1)
            else -> Status()
        }
        val rank = supporterRank[it.index]!! + supporterRank[-1]!! + 2
        lArcParam += LArcActionParam(
            supporterPt = 800 + (13 - rank) * (if (rank >= 13) 10 else 15),
            aptitudePt = 10,
        ) + when (effect) {
            StarEffect.AptitudePt -> LArcActionParam(aptitudePt = 50)
            StarEffect.StarGauge -> LArcActionParam(starGaugeMember = setOf(it))
            StarEffect.Aikyou -> LArcActionParam(condition = setOf("愛嬌○"))
            StarEffect.GoodTraining -> LArcActionParam(condition = setOf("練習上手○"))
            else -> LArcActionParam()
        }
        // スキルヒントは無視
    }
    val isSSSMatch = lArcStatus.isSSSMatch == true
    if (isSSSMatch) {
        status += Status(15, 15, 15, 15, 15, 25)
        lArcParam += LArcActionParam(supporterPt = lArcParam.supporterPt)
    }
    return arrayOf(SSMatch(isSSSMatch, joinMember, listOf(StatusActionResult(status, lArcParam) to 1)))
}

private fun LArcMemberState.predictSSMatchStatus(type: StatusType, scenarioLink: Boolean): Int {
    val baseValue = min(5, max(1, (status.get(type) / 150)))
    val typeValue = when {
        type != starType -> 0
        scenarioLink -> 6
        else -> 4
    }
    return baseValue + typeValue
}
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
import kotlin.math.min

object Calculator {

    fun calcTrainingSuccessStatus(
        chara: Chara,
        training: TrainingInfo,
        trainingLevel: Int?,
        motivation: Int,
        support: List<Support>,
        scenario: Scenario = Scenario.URA,
        teamJoinCount: Int = 0,
        supportTypeCount: Int,
        fanCount: Int,
    ) = Status(
        speed = calcTrainingStatus(
            chara,
            training,
            trainingLevel,
            motivation,
            support,
            StatusType.SPEED,
            scenario,
            teamJoinCount,
            supportTypeCount,
            fanCount,
        ),
        stamina = calcTrainingStatus(
            chara,
            training,
            trainingLevel,
            motivation,
            support,
            StatusType.STAMINA,
            scenario,
            teamJoinCount,
            supportTypeCount,
            fanCount,
        ),
        power = calcTrainingStatus(
            chara,
            training,
            trainingLevel,
            motivation,
            support,
            StatusType.POWER,
            scenario,
            teamJoinCount,
            supportTypeCount,
            fanCount,
        ),
        guts = calcTrainingStatus(
            chara,
            training,
            trainingLevel,
            motivation,
            support,
            StatusType.GUTS,
            scenario,
            teamJoinCount,
            supportTypeCount,
            fanCount,
        ),
        wisdom = calcTrainingStatus(
            chara,
            training,
            trainingLevel,
            motivation,
            support,
            StatusType.WISDOM,
            scenario,
            teamJoinCount,
            supportTypeCount,
            fanCount,
        ),
        skillPt = calcTrainingStatus(
            chara,
            training,
            trainingLevel,
            motivation,
            support,
            StatusType.SKILL,
            scenario,
            teamJoinCount,
            supportTypeCount,
            fanCount,
        ),
        hp = calcTrainingHp(training, trainingLevel, support),
    )

    fun calcTrainingSuccessStatus(
        chara: Chara,
        type: StatusType,
        motivation: Int,
        support: List<Support>,
        status: Status,
        supportTypeCount: Int,
        fanCount: Int,
    ) = calcTrainingSuccessStatus(
        chara,
        TrainingInfo(TrainingBase(Scenario.URA, type, 1, 0, status)),
        1,
        motivation,
        support,
        supportTypeCount = supportTypeCount,
        fanCount = fanCount,
    )

    private fun calcTrainingStatus(
        chara: Chara,
        training: TrainingInfo,
        trainingLevel: Int?,
        motivation: Int,
        support: List<Support>,
        type: StatusType,
        scenario: Scenario = Scenario.URA,
        teamJoinCount: Int = 0,
        supportTypeCount: Int,
        fanCount: Int,
    ): Int {
        val baseStatus = training.getBaseStatus(trainingLevel).get(type)
        if (baseStatus == 0) return 0
        val base = baseStatus + support.sumOf { it.card.getBaseBonus(type, it.relation) }
        val charaBonus = chara.getBonus(type) / 100.0
        val friend = support
            .map { it.getFriendBonus(training.type) }
            .fold(1.0) { acc, d -> acc * d }
        val motivationBonus =
            1 + motivation / 10.0 * (1 + support.sumOf { it.card.motivationFactor(it.relation) } / 100.0)
        val trainingBonus =
            1 + support.sumOf {
                it.card.trainingFactor(
                    training.type,
                    it.relation,
                    supportTypeCount,
                    fanCount,
                    Status(maxHp = 100, hp = 100),
                )
            } / 100.0
        val count = 1 + support.size * 0.05 + if (scenario == Scenario.AOHARU) {
            teamJoinCount * 0.05
        } else 0.0
//        println("$type $base * $charaBonus * $friend * motivationBonus * $trainingBonus * $count")
        return min(100, (base * charaBonus * friend * motivationBonus * trainingBonus * count).toInt())
    }

    private fun calcTrainingHp(training: TrainingInfo, trainingLevel: Int?, support: List<Support>): Int {
        val baseHp = training.getBaseStatus(trainingLevel).hp
        return when {
            baseHp == 0 -> 0
            training.type == StatusType.WISDOM -> {
                baseHp + support.sumOf { it.wisdomFriendRecovery }
            }
            else -> {
                // TODO 計算式不明のため近似値、体力消費ダウン複数所持の場合は未検証
                baseHp - (baseHp * support.sumOf { it.card.hpCost } / 100.0).toInt()
            }
        }
    }

    fun calcCardPositionSelection(card: SupportCard): Array<Pair<StatusType, Int>> {
        if (card.type.outingType) {
            return arrayOf(
                StatusType.SPEED to 1,
                StatusType.STAMINA to 1,
                StatusType.POWER to 1,
                StatusType.GUTS to 1,
                StatusType.WISDOM to 1,
                StatusType.NONE to 1,
            )
        }
        val mainRate = card.specialtyRate
        val otherRate = 10000
        val noneRate = 5000
        return arrayOf(
            StatusType.SPEED to if (card.type == StatusType.SPEED) mainRate else otherRate,
            StatusType.STAMINA to if (card.type == StatusType.STAMINA) mainRate else otherRate,
            StatusType.POWER to if (card.type == StatusType.POWER) mainRate else otherRate,
            StatusType.GUTS to if (card.type == StatusType.GUTS) mainRate else otherRate,
            StatusType.WISDOM to if (card.type == StatusType.WISDOM) mainRate else otherRate,
            StatusType.NONE to noneRate,
        )
    }

    fun calcExpectedTrainingStatus(
        chara: Chara,
        training: TrainingInfo,
        trainingLevel: Int?,
        motivation: Int,
        support: List<Support>,
        scenario: Scenario = Scenario.URA,
        teamJoinCount: Int = 0,
        supportTypeCount: Int,
        fanCount: Int,
    ): Pair<ExpectedStatus, List<Pair<Double, Status>>> {
        var result = ExpectedStatus()
        val detail = mutableListOf<Pair<Double, Status>>()
        if (support.isEmpty()) {
            result = addExpectedStatus(
                result,
                detail,
                1.0,
                calcTrainingSuccessStatus(
                    chara,
                    training,
                    trainingLevel,
                    motivation,
                    support,
                    scenario,
                    teamJoinCount,
                    supportTypeCount,
                    fanCount,
                )
            )
        } else {
            val joinRate = support.map {
                calcRate(training.type, *calcCardPositionSelection(it.card))
            }
            val allJoinRate = if (support.size < 6) 0.0 else joinRate.fold(1.0) { acc, d -> acc * d }
            var patterns = mutableListOf(arrayOf(true), arrayOf(false))
            repeat(support.size - 1) {
                val newPattern = mutableListOf<Array<Boolean>>()
                patterns.forEach {
                    newPattern.add(arrayOf(*it, true))
                    newPattern.add(arrayOf(*it, false))
                }
                patterns = newPattern
            }
            patterns.forEach { pattern ->
                if (pattern.count { it } < 6) {
                    var rate = pattern
                        .mapIndexed { index, join -> if (join) joinRate[index] else 1.0 - joinRate[index] }
                        .fold(1.0) { acc, d -> acc * d }
                    rate += rate * allJoinRate
                    val joinSupport = support.filterIndexed { index, _ -> pattern[index] }
                    result = addExpectedStatus(
                        result,
                        detail,
                        rate,
                        calcTrainingSuccessStatus(
                            chara,
                            training,
                            trainingLevel,
                            motivation,
                            joinSupport,
                            scenario,
                            teamJoinCount,
                            supportTypeCount,
                            fanCount,
                        )
                    )
                }
            }
        }
        return result to detail
    }

    private fun addExpectedStatus(
        result: ExpectedStatus,
        detail: MutableList<Pair<Double, Status>>,
        rate: Double,
        status: Status
    ): ExpectedStatus {
        detail.add(rate to status)
        return result.add(rate, status)
    }
}
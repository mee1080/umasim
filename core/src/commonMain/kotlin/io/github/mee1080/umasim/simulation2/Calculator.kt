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
import io.github.mee1080.umasim.simulation.ExpectedStatus
import kotlin.math.min

object Calculator {

    fun calcTrainingSuccessStatus(
        chara: Chara,
        training: TrainingBase,
        motivation: Int,
        member: List<MemberState>,
        scenario: Scenario = Scenario.URA,
    ) = Status(
        speed = calcTrainingStatus(
            chara,
            training,
            motivation,
            member,
            StatusType.SPEED,
            scenario,
        ),
        stamina = calcTrainingStatus(
            chara,
            training,
            motivation,
            member,
            StatusType.STAMINA,
            scenario,
        ),
        power = calcTrainingStatus(
            chara,
            training,
            motivation,
            member,
            StatusType.POWER,
            scenario,
        ),
        guts = calcTrainingStatus(
            chara,
            training,
            motivation,
            member,
            StatusType.GUTS,
            scenario,
        ),
        wisdom = calcTrainingStatus(
            chara,
            training,
            motivation,
            member,
            StatusType.WISDOM,
            scenario,
        ),
        skillPt = calcTrainingStatus(
            chara,
            training,
            motivation,
            member,
            StatusType.SKILL,
            scenario,
        ),
        hp = calcTrainingHp(training, member),
    ) + calcScenarioStatus(training, member, scenario)

    fun calcTrainingSuccessStatus(
        chara: Chara,
        type: StatusType,
        motivation: Int,
        member: List<MemberState>,
        status: Status,
    ) = calcTrainingSuccessStatus(
        chara,
        TrainingBase(Scenario.URA, type, 1, 0, status),
        motivation,
        member,
    )

    private fun calcTrainingStatus(
        chara: Chara,
        training: TrainingBase,
        motivation: Int,
        member: List<MemberState>,
        type: StatusType,
        scenario: Scenario = Scenario.URA,
    ): Int {
        val baseStatus = training.status.get(type)
        if (baseStatus == 0) return 0
        val support = member.filter { !it.guest }
        val base = baseStatus + support.sumOf { it.card.getBaseBonus(type) }
        val charaBonus = chara.getBonus(type) / 100.0
        val friend = support
            .map { it.getFriendBonus(training.type) }
            .fold(1.0) { acc, d -> acc * d }
        val motivationBonus = 1 + motivation / 10.0 * (1 + support.sumOf { it.card.motivationFactor } / 100.0)
        val trainingBonus = 1 + support.sumOf { it.card.trainingFactor } / 100.0
        val count = 1 + member.size * 0.05
//        println("$type $base * $charaBonus * $friend * motivationBonus * $trainingBonus * $count")
        return min(100, (base * charaBonus * friend * motivationBonus * trainingBonus * count).toInt())
    }

    private fun calcTrainingHp(training: TrainingBase, support: List<MemberState>): Int {
        val baseHp = training.status.hp
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
        if (card.type == StatusType.FRIEND) {
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
        training: TrainingBase,
        motivation: Int,
        member: List<MemberState>,
        scenario: Scenario = Scenario.URA,
    ): Pair<ExpectedStatus, List<Pair<Double, Status>>> {
        var result = ExpectedStatus()
        val detail = mutableListOf<Pair<Double, Status>>()
        if (member.isEmpty()) {
            result = addExpectedStatus(
                result,
                detail,
                1.0,
                calcTrainingSuccessStatus(chara, training, motivation, member, scenario)
            )
        } else {
            val joinRate = member.map {
                calcRate(training.type, *calcCardPositionSelection(it.card))
            }
            val allJoinRate = if (member.size < 6) 0.0 else joinRate.fold(1.0) { acc, d -> acc * d }
            var patterns = mutableListOf(arrayOf(true), arrayOf(false))
            repeat(member.size - 1) {
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
                    val joinSupport = member.filterIndexed { index, _ -> pattern[index] }
                    result = addExpectedStatus(
                        result,
                        detail,
                        rate,
                        calcTrainingSuccessStatus(
                            chara,
                            training,
                            motivation,
                            joinSupport,
                            scenario,
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

    private fun calcScenarioStatus(
        training: TrainingBase,
        member: List<MemberState>,
        scenario: Scenario,
    ) = when (scenario) {
        Scenario.URA -> Status()
        Scenario.AOHARU -> calcAoharuStatus(training, member)
    }

    private fun calcAoharuStatus(
        training: TrainingBase,
        member: List<MemberState>,
    ): Status {
        val states = member.mapNotNull { it.scenarioState as? AoharuMemberState }
        val aoharuMember = states.filter { it.aoharuIcon && !it.aoharuBurn }
        val aoharuCount = aoharuMember.size
        val linkCount = aoharuMember.count { Store.isScenarioLink(Scenario.AOHARU, it.member.chara) }
        val aoharuTraining = Store.Aoharu.getTraining(training.type, aoharuCount)?.status?.let {
            it.copy(
                speed = if (it.speed == 0) 0 else it.speed + linkCount,
                stamina = if (it.stamina == 0) 0 else it.stamina + linkCount,
                power = if (it.power == 0) 0 else it.power + linkCount,
                guts = if (it.guts == 0) 0 else it.guts + linkCount,
                wisdom = if (it.wisdom == 0) 0 else it.wisdom + linkCount,
            )
        } ?: Status()
        val burn = states.filter { it.aoharuBurn }
            .map { Store.Aoharu.getBurn(training.type, Store.isScenarioLink(Scenario.AOHARU, it.member.chara)) }
            .map { it.status }
        return burn.fold(aoharuTraining) { acc, status -> acc + status }
    }
}
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
import kotlin.math.min
import kotlin.native.concurrent.ThreadLocal
import kotlin.random.Random

object Calculator {

    data class CalcInfo(
        val chara: Chara,
        // TODO 叡智の効果で基本値変動あり
        val training: TrainingBase,
        val motivation: Int,
        val member: List<MemberState>,
        val scenario: Scenario,
        val supportTypeCount: Int,
        val fanCount: Int,
        val currentStatus: Status,
        val totalRelation: Int,
        val speedSkillCount: Int,
        val liveStatus: TrainingLiveStatus?,
        val gmStatus: GmStatus?,
    ) {
        fun setTeamMember(teamJoinCount: Int) = copy(
            member = member + if (scenario == Scenario.URA || scenario == Scenario.AOHARU || scenario == Scenario.GRAND_LIVE) createTeamMemberState(
                teamJoinCount,
                scenario,
            ) else emptyList()
        )

        val support by lazy { member.filter { !it.guest } }

        val allFriend get() = gmStatus?.allFriend == true
    }

    fun calcTrainingSuccessStatus(
        info: CalcInfo,
    ): Status = calcTrainingSuccessStatusSeparated(info).let { it.first + it.second }

    fun calcTrainingSuccessStatusSeparated(
        info: CalcInfo,
    ): Pair<Status, Status> {
        val friendTraining = if (info.allFriend) {
            info.support.any { it.card.type != StatusType.FRIEND }
        } else {
            info.support.any { it.isFriendTraining(info.training.type) }
        }
        val base = Status(
            speed = calcTrainingStatus(info, StatusType.SPEED, friendTraining),
            stamina = calcTrainingStatus(info, StatusType.STAMINA, friendTraining),
            power = calcTrainingStatus(info, StatusType.POWER, friendTraining),
            guts = calcTrainingStatus(info, StatusType.GUTS, friendTraining),
            wisdom = calcTrainingStatus(info, StatusType.WISDOM, friendTraining),
            skillPt = calcTrainingStatus(info, StatusType.SKILL, friendTraining),
            hp = calcTrainingHp(info.training, info.member, friendTraining),
        )
        return base to calcScenarioStatus(info, base, friendTraining)
    }

    private fun calcTrainingStatus(
        info: CalcInfo,
        targetType: StatusType,
        friendTraining: Boolean,
    ): Int {
        val baseStatus = info.training.status.get(targetType)
        if (baseStatus == 0) return 0
        val support = info.support
        val base = baseStatus + support.sumOf { it.card.getBaseBonus(targetType, it.relation) }
        val charaBonus = info.chara.getBonus(targetType) / 100.0
        val friend = if (info.allFriend) {
            support.map { it.getFriendBonusAll(info.currentStatus) }
        } else {
            support.map { it.getFriendBonus(info.training.type, info.currentStatus) }
        }.fold(1.0) { acc, d -> acc * d }
        val motivationBonus =
            1 + info.motivation / 10.0 * (1 + support.sumOf {
                it.card.motivationFactor(it.relation, friendTraining)
            } / 100.0)
        val trainingBonus =
            1 + support.sumOf {
                it.card.trainingFactor(
                    info.training.type,
                    info.training.level,
                    it.relation,
                    info.supportTypeCount,
                    info.fanCount,
                    info.currentStatus,
                    info.totalRelation,
                    support.size,
                    info.speedSkillCount,
                )
            } / 100.0
        val count = 1 + info.member.size * 0.05
//        println("$type $base * $charaBonus * $friend * motivationBonus * $trainingBonus * $count")
        return min(100, (base * charaBonus * friend * motivationBonus * trainingBonus * count).toInt())
    }

    private fun calcTrainingHp(
        training: TrainingBase,
        support: List<MemberState>,
        friendTraining: Boolean,
    ): Int {
        val baseHp = training.status.hp
        return when {
            baseHp == 0 -> 0
            training.type == StatusType.WISDOM -> {
                baseHp + support.sumOf { it.wisdomFriendRecovery }
            }

            else -> {
                baseHp - (baseHp * support.sumOf {
                    it.card.hpCost(friendTraining)
                } / 100.0).toInt()
            }
        }
    }

    fun calcCardPositionSelection(card: SupportCard, bonus: Int): Array<Pair<StatusType, Int>> {
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
        val mainRate = card.specialtyRate(bonus)
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
        info: CalcInfo,
        teamJoinCount: Int,
    ) = calcExpectedTrainingStatus(
        info.copy(
            member = info.member + if (info.scenario == Scenario.AOHARU) createTeamMemberState(
                teamJoinCount,
                info.scenario
            ) else emptyList(),
        ),
    )

    data class ExpectedStatusKey(
        val charaId: Int,
        val trainingType: StatusType,
        val trainingLevel: Int,
        val motivation: Int,
        val member: List<Triple<Int, Int, Int>>,
        val scenario: Scenario,
        val supportTypeCount: Int,
        val fanCountLevel: Int,
        val liveStatus: TrainingLiveStatus?,
        val gmStatus: GmStatus?,
    )

    @ThreadLocal
    private val expectedStatusCache =
        mutableMapOf<ExpectedStatusKey, Pair<ExpectedStatus, List<Pair<Double, Status>>>>()

    fun calcExpectedTrainingStatus(
        info: CalcInfo,
    ): Pair<ExpectedStatus, List<Pair<Double, Status>>> {
        val key = ExpectedStatusKey(
            info.chara.id, info.training.type, info.training.level, info.motivation,
            info.member.map {
                Triple(
                    it.card.id,
                    it.card.talent,
                    it.card.targetRelation.last { target -> target <= it.relation }
                )
            },
            info.scenario, info.supportTypeCount, info.fanCount / 10000,
            info.liveStatus,
            info.gmStatus,
        )
        val cached = expectedStatusCache[key]
        if (cached != null) {
            return cached
        }
        var status = ExpectedStatus()
        val detail = mutableListOf<Pair<Double, Status>>()
        if (info.member.isEmpty()) {
            status = addExpectedStatus(
                status,
                detail,
                1.0,
                calcTrainingSuccessStatus(info)
            )
        } else {
            val specialityRateBonus = info.liveStatus?.specialityRateUp ?: 0
            val joinRate = info.member.map {
                calcRate(info.training.type, *calcCardPositionSelection(it.card, specialityRateBonus))
            }
            val allJoinRate = if (info.member.size < 6) 0.0 else joinRate.fold(1.0) { acc, d -> acc * d }
            var patterns = mutableListOf(arrayOf(true), arrayOf(false))
            repeat(info.member.size - 1) {
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
                    val joinSupport = info.member.filterIndexed { index, _ -> pattern[index] }
                    status = addExpectedStatus(
                        status,
                        detail,
                        rate,
                        calcTrainingSuccessStatus(info.copy(member = joinSupport)),
                    )
                }
            }
        }
        val result = status to detail
        expectedStatusCache[key] = result
        return result
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
        info: CalcInfo,
        base: Status,
        friendTraining: Boolean,
    ) = when (info.scenario) {
        Scenario.URA -> Status()
        Scenario.AOHARU -> calcAoharuStatus(info.training, info.member)
        Scenario.CLIMAX -> Status()
        Scenario.GRAND_LIVE -> calcLiveStatus(info, base, friendTraining)
        Scenario.GM -> calcGmStatus(info, base)
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

    private fun calcLiveStatus(
        info: CalcInfo,
        base: Status,
        friendTraining: Boolean,
    ): Status {
        return if (info.liveStatus != null) {
            val performanceValue = calcPerformanceValue(info)
            val firstPerformanceType = selectFirstPerformanceType(info)
            val trainingUp = Status(
                speed = calcLiveStatusSingle(info, StatusType.SPEED),
                stamina = calcLiveStatusSingle(info, StatusType.STAMINA),
                power = calcLiveStatusSingle(info, StatusType.POWER),
                guts = calcLiveStatusSingle(info, StatusType.GUTS),
                wisdom = calcLiveStatusSingle(info, StatusType.WISDOM),
                skillPt = calcLiveStatusSingle(info, StatusType.SKILL),
                performance = firstPerformanceType.asPerformance(performanceValue),
            )
            val friendUp = if (friendTraining) {
                val calc = base + trainingUp
                val secondPerformanceType = selectSecondPerformanceType(info, firstPerformanceType)
                Status(
                    speed = calcTrainingUp(calc.speed, info.liveStatus.friendTrainingUp),
                    stamina = calcTrainingUp(calc.stamina, info.liveStatus.friendTrainingUp),
                    power = calcTrainingUp(calc.power, info.liveStatus.friendTrainingUp),
                    guts = calcTrainingUp(calc.guts, info.liveStatus.friendTrainingUp),
                    wisdom = calcTrainingUp(calc.wisdom, info.liveStatus.friendTrainingUp),
                    skillPt = calcTrainingUp(calc.skillPt, info.liveStatus.friendTrainingUp),
                    performance = secondPerformanceType.asPerformance(performanceValue),
                )
            } else Status()
            trainingUp + friendUp
        } else Status()
    }

    private fun calcLiveStatusSingle(
        info: CalcInfo,
        target: StatusType,
    ) = if (upInTraining(info.training.type, target)) info.liveStatus?.trainingUp(target) ?: 0 else 0

    private fun calcTrainingUp(value: Int, up: Int) = ((value * up) + 50) / 100

    fun calcPerformanceValue(
        info: CalcInfo,
    ): Int {
        val base = if (info.training.type == StatusType.WISDOM) 5 else 9
        val link = 2 * info.member.count { !it.guest && Store.isScenarioLink(info.scenario, it.charaName) }
        return (base + info.training.level) * when (info.member.size) {
            1 -> 11
            2 -> 13
            3 -> 15
            4 -> 17
            5 -> 20
            else -> 10
        } / 10 + link
    }

    private fun selectFirstPerformanceType(
        info: CalcInfo,
    ): PerformanceType {
        return randomSelect(firstPerformanceRate[info.training.type]!!)
    }

    private fun selectSecondPerformanceType(
        info: CalcInfo,
        firstType: PerformanceType,
    ): PerformanceType {
        val minimumType = info.currentStatus.performance?.minimumType ?: PerformanceType.values().asList()
        return if (!minimumType.contains(firstType) && Random.nextInt(100) >= 85) {
            minimumType.random()
        } else {
            return randomSelect(firstPerformanceRate[info.training.type]!!.filterNot { it.first == firstType })
        }
    }

    fun calcItemBonus(trainingType: StatusType, status: Status, item: List<ShopItem>): Status {
        val statusFactor = item.sumOf {
            when (it) {
                is MegaphoneItem -> it.trainingFactor
                is WeightItem -> if (it.type == trainingType) it.trainingFactor else 0
                else -> 0
            }
        }
        if (statusFactor == 0) return Status()
        val hpFactor = item.sumOf { if (it is WeightItem && it.type == trainingType) it.hpFactor else 0 }
        return Status(
            speed = (status.speed * statusFactor / 100.0).toInt(),
            stamina = (status.stamina * statusFactor / 100.0).toInt(),
            power = (status.power * statusFactor / 100.0).toInt(),
            guts = (status.guts * statusFactor / 100.0).toInt(),
            wisdom = (status.wisdom * statusFactor / 100.0).toInt(),
            skillPt = (status.skillPt * statusFactor / 100.0).toInt(),
            hp = (status.hp * hpFactor / 100.0).toInt(),
        )
    }

    private fun calcGmStatus(
        info: CalcInfo,
        base: Status
    ): Status {
        val gmStatus = info.gmStatus ?: return Status()
        return Status(
            speed = calcGmStatusSingle(gmStatus, StatusType.SPEED, base.speed),
            stamina = calcGmStatusSingle(gmStatus, StatusType.STAMINA, base.stamina),
            power = calcGmStatusSingle(gmStatus, StatusType.POWER, base.power),
            guts = calcGmStatusSingle(gmStatus, StatusType.GUTS, base.guts),
            wisdom = calcGmStatusSingle(gmStatus, StatusType.SKILL, base.wisdom),
            // TODO 体力消費ダウン計算式
            hp = -(base.hp * gmStatus.wisdomHpCost / 100.0).toInt()
        )
    }

    private fun calcGmStatusSingle(
        gmStatus: GmStatus,
        target: StatusType,
        baseValue: Int,
    ): Int {
        if (baseValue == 0) return 0
        // TODO トレ効果計算式
        val bonus = gmStatus.getStatusBonus(target)
        val factor = ((baseValue + bonus) * gmStatus.wisdomTrainingFactor / 100.0).toInt()
        return bonus + factor
    }
}
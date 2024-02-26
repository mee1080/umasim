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
        val training: TrainingBase,
        val motivation: Int,
        val member: List<MemberState>,
        val scenario: Scenario,
        val supportTypeCount: Int,
        val fanCount: Int,
        val currentStatus: Status,
        val totalRelation: Int,
        val speedSkillCount: Int,
        val healSkillCount: Int,
        val accelSkillCount: Int,
        val totalTrainingLevel: Int,
        val liveStatus: TrainingLiveStatus?,
        val gmStatus: GmStatus?,
        val lArcStatus: LArcStatus?,
        val uafStatus: UafStatus?,
    ) {
        fun setTeamMember(teamJoinCount: Int) = copy(
            member = member + if (scenario == Scenario.URA || scenario.guestMember) createTeamMemberState(
                teamJoinCount,
                scenario,
            ) else emptyList()
        )

        val support by lazy { member.filter { !it.guest } }

        val allFriend get() = gmStatus?.allFriend == true

        fun baseSpecialUniqueCondition(
            trainingSupportCount: Int,
            friendTraining: Boolean,
        ) = SpecialUniqueCondition(
            trainingType = training.type,
            trainingLevel = training.level,
            totalTrainingLevel = totalTrainingLevel,
            relation = 0,
            supportTypeCount = supportTypeCount,
            fanCount = fanCount,
            status = currentStatus,
            totalRelation = totalRelation,
            trainingSupportCount = trainingSupportCount,
            speedSkillCount = speedSkillCount,
            healSkillCount = healSkillCount,
            accelSkillCount = accelSkillCount,
            friendTraining = friendTraining,
            friendCount = 0,
        )
    }

    fun calcTrainingSuccessStatus(
        info: CalcInfo,
    ): Status = calcTrainingSuccessStatusSeparated(info).let { it.first.first + it.second }

    fun calcTrainingSuccessStatusAndFriendEnabled(
        info: CalcInfo,
    ): Pair<Status, Boolean> = calcTrainingSuccessStatusSeparated(info).let { it.first.first + it.second to it.third }

    fun calcTrainingSuccessStatusSeparated(
        info: CalcInfo,
    ): Triple<Pair<Status, ExpectedStatus>, Status, Boolean> {
        val friendTraining = if (info.allFriend) {
            info.support.any { it.card.type != StatusType.FRIEND }
        } else {
            info.support.any { it.isFriendTraining(info.training.type) }
        }
        val hp = calcTrainingHp(info, friendTraining)
        val raw = ExpectedStatus(
            speed = calcTrainingStatus(info, StatusType.SPEED, friendTraining),
            stamina = calcTrainingStatus(info, StatusType.STAMINA, friendTraining),
            power = calcTrainingStatus(info, StatusType.POWER, friendTraining),
            guts = calcTrainingStatus(info, StatusType.GUTS, friendTraining),
            wisdom = calcTrainingStatus(info, StatusType.WISDOM, friendTraining),
            skillPt = calcTrainingStatus(info, StatusType.SKILL, friendTraining),
            hp = hp.toDouble(),
        )
        val base = Status(
            speed = raw.speed.toInt(),
            stamina = raw.stamina.toInt(),
            power = raw.power.toInt(),
            guts = raw.guts.toInt(),
            wisdom = raw.wisdom.toInt(),
            skillPt = raw.skillPt.toInt(),
            hp = hp,
        )
        return Triple(base to raw, calcScenarioStatus(info, base, raw, friendTraining), friendTraining)
    }

    private fun SpecialUniqueCondition.applyMember(member: MemberState) = copy(
        relation = member.relation,
        friendCount = member.friendCount,
    )

    private fun calcTrainingStatus(
        info: CalcInfo,
        targetType: StatusType,
        friendTraining: Boolean,
        ignoreBaseBonus: Boolean = false,
    ): Double {
        val baseStatus = info.training.status.get(targetType)
        if (baseStatus == 0) return 0.0
        val support = info.support
        val baseCondition = info.baseSpecialUniqueCondition(
            trainingSupportCount = support.size,
            friendTraining = friendTraining,
        )
        val base = baseStatus + if (ignoreBaseBonus) 0 else support.sumOf {
            it.card.getBaseBonus(targetType, baseCondition.applyMember(it))
        }
        val charaBonus = info.chara.getBonus(targetType) / 100.0
        val friend = support.map {
            if (info.allFriend || it.isFriendTraining(info.training.type)) {
                it.card.friendFactor(baseCondition.applyMember(it))
            } else 1.0
        }.fold(1.0) { acc, d -> acc * d }
        val motivationBonus =
            1 + info.motivation / 10.0 * (1 + support.sumOf {
                it.card.motivationFactor(baseCondition.applyMember(it))
            } / 100.0)
        val trainingBonus =
            1 + support.sumOf {
                it.card.trainingFactor(baseCondition.applyMember(it))
            } / 100.0
        val count = 1 + info.member.size * 0.05
        println("$targetType $baseStatus ${base * charaBonus * friend * motivationBonus * trainingBonus * count} $base * $charaBonus * $friend * $motivationBonus * $trainingBonus * $count")
        return min(100.0, base * charaBonus * friend * motivationBonus * trainingBonus * count)
    }

    private fun calcTrainingHp(
        info: CalcInfo,
        friendTraining: Boolean,
    ): Int {
        val baseHp = info.training.status.hp
        val baseCondition = info.baseSpecialUniqueCondition(
            trainingSupportCount = info.support.size,
            friendTraining = friendTraining,
        )
        return when {
            baseHp == 0 -> 0

            info.training.type == StatusType.WISDOM -> {
                baseHp + info.support.sumOf {
                    if (it.isFriendTraining(StatusType.WISDOM)) {
                        it.card.wisdomFriendRecovery(baseCondition.applyMember(it))
                    } else 0
                }
            }

            else -> {
                baseHp - (baseHp * info.support.sumOf {
                    it.card.hpCost(baseCondition.applyMember(it))
                } / 100.0).toInt()
            }
        }
    }

    fun calcCardPositionSelection(info: CalcInfo, member: MemberState, bonus: Int): Array<Pair<StatusType, Int>> {
        val card = member.card
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
        val mainRate = card.specialtyRate(bonus, info.baseSpecialUniqueCondition(0, false).applyMember(member))
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
        val lArcStatus: LArcStatus?,
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
            info.lArcStatus,
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
                calcRate(info.training.type, *calcCardPositionSelection(info, it, specialityRateBonus))
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
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ) = when (info.scenario) {
        Scenario.URA -> Status()
        Scenario.AOHARU -> calcAoharuStatus(info.training, info.member)
        Scenario.CLIMAX -> Status()
        Scenario.GRAND_LIVE -> calcLiveStatus(info, base, friendTraining)
        Scenario.GM -> calcGmStatus(info, base)
        Scenario.LARC -> calcLArcStatus(info, base, friendTraining)
        Scenario.UAF -> calcUafStatus(info, raw, friendTraining)
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
        val minimumType = info.currentStatus.performance?.minimumType ?: PerformanceType.entries
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
            wisdom = calcGmStatusSingle(gmStatus, StatusType.WISDOM, base.wisdom),
            skillPt = calcGmStatusSingle(gmStatus, StatusType.SKILL, base.skillPt),
            // TODO 体力消費ダウン計算式
            hp = -(base.hp * gmStatus.wisdomHpCost / 100.0).toInt()
        ) + calcGmStatusHint(info.member, gmStatus)
    }

    private fun calcGmStatusSingle(
        gmStatus: GmStatus,
        target: StatusType,
        baseValue: Int,
    ): Int {
        if (baseValue == 0) return 0
        val bonus = gmStatus.getStatusBonus(target)
        val factor = ((baseValue + bonus) * gmStatus.wisdomTrainingFactor / 100.0).toInt()
        return bonus + factor
    }

    private fun calcGmStatusHint(
        member: List<MemberState>,
        gmStatus: GmStatus,
    ): Status {
        if (!gmStatus.hintFrequencyUp) return Status()
        val hintSupportList = member.filter { !it.outingType }
        val hintStatus = hintSupportList.map { hintSupport ->
            val base = hintSupport.card.hintStatus
            // ヒント全潰しは無視
            if (hintSupport.card.skills.isNotEmpty()) base else base + base
        }.fold(Status()) { acc, status ->
            acc + status
        }
        return hintStatus + Status(
            speed = if (hintStatus.speed > 0) gmStatus.getStatusBonus(StatusType.SPEED) else 0,
            stamina = if (hintStatus.stamina > 0) gmStatus.getStatusBonus(StatusType.STAMINA) else 0,
            power = if (hintStatus.power > 0) gmStatus.getStatusBonus(StatusType.POWER) else 0,
            guts = if (hintStatus.guts > 0) gmStatus.getStatusBonus(StatusType.GUTS) else 0,
            wisdom = if (hintStatus.wisdom > 0) gmStatus.getStatusBonus(StatusType.WISDOM) else 0,
            skillPt = hintSupportList.size * 20,
        )
    }

    private fun calcLArcStatus(
        info: CalcInfo,
        base: Status,
        friendTraining: Boolean,
    ): Status {
        val lArcStatus = info.lArcStatus ?: return Status()
        val trainingType = info.training.type
        val overseas = info.training.level == 6
        return Status(
            speed = calcLArcStatusSingle(
                lArcStatus,
                trainingType,
                StatusType.SPEED,
                base.speed,
                overseas,
                friendTraining
            ),
            stamina = calcLArcStatusSingle(
                lArcStatus,
                trainingType,
                StatusType.STAMINA,
                base.stamina,
                overseas,
                friendTraining
            ),
            power = calcLArcStatusSingle(
                lArcStatus,
                trainingType,
                StatusType.POWER,
                base.power,
                overseas,
                friendTraining
            ),
            guts = calcLArcStatusSingle(lArcStatus, trainingType, StatusType.GUTS, base.guts, overseas, friendTraining),
            wisdom = calcLArcStatusSingle(
                lArcStatus,
                trainingType,
                StatusType.WISDOM,
                base.wisdom,
                overseas,
                friendTraining
            ),
            skillPt = calcLArcStatusSingle(
                lArcStatus,
                trainingType,
                StatusType.SKILL,
                base.skillPt,
                overseas,
                friendTraining
            ),
            hp = -(base.hp * lArcStatus.hpCost(overseas) / 100.0).toInt()
        )
    }

    private fun calcLArcStatusSingle(
        lArcStatus: LArcStatus,
        trainingType: StatusType,
        target: StatusType,
        baseValue: Int,
        overseas: Boolean,
        friendTraining: Boolean,
    ): Int {
        if (baseValue == 0) return 0
        val bonus = lArcStatus.getStatusBonus(target)
        val factor = (baseValue + bonus) * lArcStatus.getTrainingFactor(trainingType, overseas) / 100.0
        val friend = if (friendTraining) {
            (baseValue + bonus + factor) * lArcStatus.friendFactor / 100.0
        } else 0.0
        return bonus + (factor + friend).toInt()
    }

    private fun calcUafStatus(
        info: CalcInfo,
        raw: ExpectedStatus,
        isFriendTraining: Boolean,
    ): Status {
        val uafStatus = info.uafStatus ?: return Status()
        val trainingType = info.training.type
        val targetAthletic = uafStatus.trainingAthletics[trainingType]!!
        val linkAthletics = uafStatus.trainingAthletics.filter {
            it.key != trainingType && it.value.genre == targetAthletic.genre
        }
        return Status(
            speed = calcUafStatusSingle(info, linkAthletics, StatusType.SPEED, raw.speed, isFriendTraining),
            stamina = calcUafStatusSingle(info, linkAthletics, StatusType.STAMINA, raw.stamina, isFriendTraining),
            power = calcUafStatusSingle(info, linkAthletics, StatusType.POWER, raw.power, isFriendTraining),
            guts = calcUafStatusSingle(info, linkAthletics, StatusType.GUTS, raw.guts, isFriendTraining),
            wisdom = calcUafStatusSingle(info, linkAthletics, StatusType.WISDOM, raw.wisdom, isFriendTraining),
            skillPt = calcUafStatusSingle(info, linkAthletics, StatusType.SKILL, raw.skillPt, isFriendTraining),
            hp = when (linkAthletics.size) {
                4 -> -3
                3, 2 -> -2
                1 -> -1
                else -> 0
            }
        )
    }

    private fun calcUafStatusSingle(
        info: CalcInfo,
        linkAthletics: Map<StatusType, UafAthletic>,
        target: StatusType,
        baseValue: Double,
        isFriendTraining: Boolean,
    ): Int {
        val uafStatus = info.uafStatus ?: return 0
        val trainingType = info.training.type
        val targetLink = linkAthletics[target]
        // リンク先の基本値上昇：各リンク先について、FLOOR((リンク先トレLv+1)/2)？
        val linkBonus = when (target) {
            trainingType -> 0
            StatusType.SKILL -> linkAthletics.size
            else -> targetLink?.let {
                (uafStatus.trainingLevel(it) + 1) / 2
            } ?: 0
        }
        val training = info.training.copy(status = info.training.status.add(target to linkBonus))
        val scenarioInfo = info.copy(training = training)
        var total = calcTrainingStatus(scenarioInfo, target, isFriendTraining, baseValue == 0.0)
        val baseInt = baseValue.toInt()
        // リンク数によって基本上昇量(切り捨て前)に倍率がかかる
        if (target == trainingType) {
            val baseFactor = when (linkAthletics.size) {
                4 -> 0.3
                3 -> 0.25
                2 -> 0.2
                1 -> 0.1
                else -> 0.0
            }
            val typeFactor = when (target) {
                trainingType -> 1.0
                StatusType.SKILL -> 1.0 / 3.0
                else -> 2.0 / 3.0
            }
            total *= (baseFactor * typeFactor) + 1.0
        }
        // ヒートアップ効果
        if (uafStatus.heatUp[UafGenre.Blue]!! > 0) {
            total += if (target == StatusType.SKILL) {
                // スキルPt：20
                20
            } else {
                // 5ステ：対応する箇所のトレーニングの競技Lv上昇量÷2+1（切り捨て）
                uafStatus.athleticsLevelUp[target]!! / 2 + 1
            }
        }
        if (uafStatus.heatUp[UafGenre.Red]!! > 0 && target == trainingType) {
            val heatUpRedFactor = when (linkAthletics.size) {
                4 -> 1.6
                3 -> 1.54
                2 -> 1.45
                1 -> 1.3
                else -> 1.0
            }
            total *= heatUpRedFactor
        }
        // 大会ボーナス
        val festivalFactor = 1.0 + uafStatus.festivalBonus / 100.0
        total *= festivalFactor
        return (total - baseInt).toInt()
    }
}
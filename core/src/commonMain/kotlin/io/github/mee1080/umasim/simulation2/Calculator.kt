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
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.climax.MegaphoneItem
import io.github.mee1080.umasim.scenario.climax.ShopItem
import io.github.mee1080.umasim.scenario.climax.WeightItem
import io.github.mee1080.umasim.scenario.cook.CookStatus
import io.github.mee1080.umasim.scenario.gm.GmStatus
import io.github.mee1080.umasim.scenario.larc.LArcStatus
import io.github.mee1080.umasim.scenario.legend.LegendStatus
import io.github.mee1080.umasim.scenario.live.TrainingLiveStatus
import io.github.mee1080.umasim.scenario.mecha.MechaStatus
import io.github.mee1080.umasim.scenario.mujinto.MujintoStatus
import io.github.mee1080.umasim.scenario.onsen.OnsenStatus
import io.github.mee1080.umasim.scenario.uaf.UafStatus
import kotlin.math.min
import kotlin.native.concurrent.ThreadLocal

object Calculator {

    var DEBUG = false

    data class CalcInfo(
        val chara: Chara,
        val training: TrainingBase,
        val motivation: Int,
        val member: List<MemberState>,
        val scenario: Scenario,
        val supportCount: Map<StatusType, Int>,
        val fanCount: Int,
        val currentStatus: Status,
        val totalRelation: Int,
        val speedSkillCount: Int,
        val healSkillCount: Int,
        val accelSkillCount: Int,
        val totalTrainingLevel: Int,
        val isLevelUpTurn: Boolean,
        val scenarioStatus: ScenarioStatus?,
    ) {
        val liveStatus get() = scenarioStatus as? TrainingLiveStatus
        val gmStatus get() = scenarioStatus as? GmStatus
        val lArcStatus get() = scenarioStatus as? LArcStatus
        val uafStatus get() = scenarioStatus as? UafStatus
        val cookStatus get() = scenarioStatus as? CookStatus
        val mechaStatus get() = scenarioStatus as? MechaStatus
        val legendStatus get() = scenarioStatus as? LegendStatus
        val mujintoStatus get() = scenarioStatus as? MujintoStatus
        val onsenStatus get() = scenarioStatus as? OnsenStatus

        fun setTeamMember(teamJoinCount: Int) = copy(
            member = member + if (scenario == Scenario.URA || scenario.guestMember) createTeamMemberState(
                teamJoinCount,
                scenario,
            ) else emptyList()
        )

        val support by lazy { member.filter { !it.guest } }

        val allFriend get() = gmStatus?.allFriend == true

        val supportTypeCount = supportCount.size

        fun baseSpecialUniqueCondition(
            trainingSupportCount: Int,
            friendTraining: Boolean,
        ) = SpecialUniqueCondition(
            trainingType = training.type,
            trainingLevel = training.displayLevel,
            totalTrainingLevel = totalTrainingLevel,
            relation = 0,
            supportCount = supportCount,
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

    data class ScenarioCalcBonus(
        val friendFactor: Double = 1.0,
        val motivationBonus: Int = 0,
        val trainingBonus: Int = 0,
        val additionalFactor: Double = 1.0,
    )

    fun calcTrainingSuccessStatus(
        info: CalcInfo,
    ): Status = calcTrainingSuccessStatusSeparated(info).let { it.first.first + it.second }

    fun calcTrainingSuccessStatusAndFriendEnabled(
        info: CalcInfo,
        bonus: ScenarioCalcBonus? = null,
    ): Pair<Status, Boolean> = calcTrainingSuccessStatusSeparated(info, bonus).let {
        it.first.first + it.second to it.third
    }

    fun calcTrainingSuccessStatusSeparated(
        info: CalcInfo,
        bonus: ScenarioCalcBonus? = null,
    ): Triple<Pair<Status, ExpectedStatus>, Status, Boolean> {
        val friendTraining = if (info.allFriend) {
            info.support.any { it.card.type != StatusType.FRIEND }
        } else {
            info.support.any { it.isFriendTraining(info.training.type) }
        }
        val hp = calcTrainingHp(info, friendTraining)
        val raw = ExpectedStatus(
            speed = calcTrainingStatus(info, StatusType.SPEED, friendTraining, bonus = bonus),
            stamina = calcTrainingStatus(info, StatusType.STAMINA, friendTraining, bonus = bonus),
            power = calcTrainingStatus(info, StatusType.POWER, friendTraining, bonus = bonus),
            guts = calcTrainingStatus(info, StatusType.GUTS, friendTraining, bonus = bonus),
            wisdom = calcTrainingStatus(info, StatusType.WISDOM, friendTraining, bonus = bonus),
            skillPt = calcTrainingStatus(info, StatusType.SKILL, friendTraining, bonus = bonus),
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
        return Triple(
            base to raw,
            info.scenario.calculator.calcScenarioStatus(info, base, raw, friendTraining),
            friendTraining,
        )
    }

    fun SpecialUniqueCondition.applyMember(member: MemberState) = copy(
        relation = member.relation,
        friendCount = member.friendCount,
    )

    internal fun calcTrainingStatus(
        info: CalcInfo,
        targetType: StatusType,
        friendTraining: Boolean,
        ignoreBaseBonus: Boolean = false,
        bonus: ScenarioCalcBonus? = null,
        maxValue: Double = 100.0,
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
        val charaBonus = if (ignoreBaseBonus) 1.0 else info.chara.getBonus(targetType) / 100.0
        val friend = support.map {
            if (info.allFriend || it.isFriendTraining(info.training.type)) {
                it.card.friendFactor(baseCondition.applyMember(it))
            } else 1.0
        }.fold(1.0) { acc, d -> acc * d } * (bonus?.friendFactor ?: 1.0)
        val motivationBase = when (info.motivation) {
            3 -> 0.55
            else -> info.motivation / 10.0
        }
        val motivationBonus =
            1 + motivationBase * (1 + (support.sumOf {
                it.card.motivationFactor(baseCondition.applyMember(it))
            } + (bonus?.motivationBonus ?: 0)) / 100.0)
        val trainingBonus =
            1 + (support.sumOf {
                it.card.trainingFactor(baseCondition.applyMember(it))
            } + (bonus?.trainingBonus ?: 0)) / 100.0
        val count = 1 + info.member.size * 0.05
        val scenarioFactor = bonus?.additionalFactor ?: 1.0
        if (DEBUG) println("$targetType base=$baseStatus baseBonus=$base chara=$charaBonus friend=$friend motivation=$motivationBonus training=$trainingBonus count=$count scenario=$scenarioFactor")
        val raw = base * charaBonus * friend * motivationBonus * trainingBonus * count * scenarioFactor
        return min(maxValue, raw + 0.0002)
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

    fun calcCardPositionSelection(
        info: CalcInfo,
        member: MemberState,
        specialityRateUp: Int,
        positionRateUp: Int,
        forceSpecialityEnabled: Boolean = false,
    ): Array<Pair<StatusType, Int>> {
        if (forceSpecialityEnabled && member.forceSpeciality) {
            return arrayOf(
                member.card.type to 100,
            )
        }
        val card = member.card
        if (card.type == StatusType.FRIEND) {
            return arrayOf(
                StatusType.SPEED to 100,
                StatusType.STAMINA to 100,
                StatusType.POWER to 100,
                StatusType.GUTS to 100,
                StatusType.WISDOM to 100,
                StatusType.NONE to (100 - positionRateUp),
            )
        }
        val mainRate =
            card.specialtyRate(specialityRateUp, info.baseSpecialUniqueCondition(0, false).applyMember(member))
        val otherRate = 10000
        val noneRate = 50 * (100 - positionRateUp)
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
        specialityRateUp: (type: StatusType) -> Int,
        positionRateUp: Int,
    ) = calcExpectedTrainingStatus(
        info = info.copy(
            member = info.member + if (info.scenario.guestMember) createTeamMemberState(
                teamJoinCount,
                info.scenario
            ) else emptyList(),
        ),
        specialityRateUp = specialityRateUp,
        positionRateUp = positionRateUp,
    )

    private data class ExpectedStatusKey(
        val charaId: Int,
        val trainingType: StatusType,
        val trainingLevel: Int,
        val motivation: Int,
        val member: List<Triple<Int, Int, Int>>,
        val scenario: Scenario,
        val supportTypeCount: Int,
        val fanCountLevel: Int,
        val scenarioStatus: ScenarioStatus?,
    )

    @ThreadLocal
    private val expectedStatusCache =
        mutableMapOf<ExpectedStatusKey, Pair<ExpectedStatus, List<Pair<Double, Status>>>>()

    fun calcExpectedTrainingStatus(
        info: CalcInfo,
        specialityRateUp: (type: StatusType) -> Int,
        positionRateUp: Int,
        noCache: Boolean = false,
    ): Pair<ExpectedStatus, List<Pair<Double, Status>>> {
        var key: ExpectedStatusKey? = null
        if (!noCache) {
            key = ExpectedStatusKey(
                info.chara.id, info.training.type, info.training.level, info.motivation,
                info.member.map {
                    Triple(
                        it.card.id,
                        it.card.talent,
                        it.card.targetRelation.last { target -> target <= it.relation }
                    )
                },
                info.scenario, info.supportTypeCount, info.fanCount / 10000,
                info.scenarioStatus,
            )
            val cached = expectedStatusCache[key]
            if (cached != null) {
                return cached
            }
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
            val joinRate = info.member.map {
                calcRate(
                    info.training.type,
                    *calcCardPositionSelection(info, it, specialityRateUp(it.card.type), positionRateUp)
                )
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
        if (key != null) {
            expectedStatusCache[key] = result
        }
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
}
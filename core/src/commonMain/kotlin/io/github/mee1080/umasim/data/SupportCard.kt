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
package io.github.mee1080.umasim.data

import kotlin.math.max
import kotlin.random.Random

data class SupportCard(
    val id: Int,
    val name: String,
    val chara: String,
    val rarity: Int,
    val talent: Int,
    val maxLevel: Int,
    val type: StatusType,

    val status: SupportStatus,
    val unique: SupportStatus,

    val skills: List<String>,
    val baseHintStatus: Status,

    val specialUnique: List<SupportCardSpecialUnique>,
) {

    data class SupportStatus(
        val friend: Int,
        val motivation: Int,

        val speedBonus: Int,
        val staminaBonus: Int,
        val powerBonus: Int,
        val gutsBonus: Int,
        val wisdomBonus: Int,

        val training: Int,

        val initialSpeed: Int,
        val initialStamina: Int,
        val initialPower: Int,
        val initialGuts: Int,
        val initialWisdom: Int,

        val initialRelation: Int,

        val race: Int,
        val fan: Int,

        val hintLevel: Int,
        val hintFrequency: Int,

        val specialtyRate: Int,

        val eventRecovery: Int,
        val eventEffect: Int,

        val failureRate: Int,
        val hpCost: Int,

        val skillPtBonus: Int,
        val wisdomFriendRecovery: Int,

        val initialSkillPt: Int,
    ) {
        val initialStatus
            get() = Status(
                speed = initialSpeed,
                stamina = initialStamina,
                power = initialPower,
                guts = initialGuts,
                wisdom = initialWisdom,
                skillPt = initialSkillPt,
            )
    }

    val initialRelation = status.initialRelation + unique.initialRelation

    val race = status.race + unique.race

    val fan = status.fan + unique.fan

    fun initialStatus(supportType: List<StatusType>) =
        status.initialStatus + unique.initialStatus + specialUnique.fold(Status()) { total, unique ->
            total + unique.initialStatus(supportType)
        }

    val initialRelationAll = specialUnique.sumOf { it.initialRelationAll }

    val trainingRelationAll = specialUnique.sumOf { it.trainingRelationAll }

    val trainingRelationJoin = specialUnique.sumOf { it.trainingRelationJoin }

    fun friendFactor(
        condition: SpecialUniqueCondition,
    ) = (100 + status.friend) * (100 + unique.friend) * (100 + specialUnique.sumOf {
        it.friendFactor(this, condition)
    }) / 1000000.0

    fun motivationFactor(
        condition: SpecialUniqueCondition,
    ) = status.motivation + unique.motivation + specialUnique.sumOf { it.getMotivation(this, condition) }

    fun getBaseBonus(
        type: StatusType,
        condition: SpecialUniqueCondition,
    ) = when (type) {
        StatusType.SPEED -> status.speedBonus + unique.speedBonus
        StatusType.STAMINA -> status.staminaBonus + unique.staminaBonus
        StatusType.POWER -> status.powerBonus + unique.powerBonus
        StatusType.GUTS -> status.gutsBonus + unique.gutsBonus
        StatusType.WISDOM -> status.wisdomBonus + unique.wisdomBonus
        StatusType.SKILL -> status.skillPtBonus + unique.skillPtBonus
        else -> 0
    } + specialUnique.sumOf { it.getBaseBonus(type, this, condition) }

    fun trainingFactor(
        condition: SpecialUniqueCondition,
    ) = status.training + unique.training + specialUnique.sumOf {
        it.trainingFactor(this, condition)
    }

    fun hpCost(
        condition: SpecialUniqueCondition,
    ) = status.hpCost + unique.hpCost + specialUnique.sumOf {
        it.hpCost(this, condition)
    }

    fun failureRate() =
        (100 - status.failureRate) * (100 - unique.failureRate) * specialUnique.fold(1) { acc, v -> acc * v.failureRate() } / 10000.0

    val hintLevel = status.hintLevel + unique.hintLevel

    // ボーナスは乗算
    fun specialtyRate(
        bonus: Int,
        condition: SpecialUniqueCondition,
    ) = (100 + status.specialtyRate) * (100 + unique.specialtyRate) * (100 + specialUnique.sumOf {
        it.specialityRate(this, condition)
    }) * (100 + bonus) / 10000

    fun wisdomFriendRecovery(
        condition: SpecialUniqueCondition,
    ) = status.wisdomFriendRecovery + unique.wisdomFriendRecovery + specialUnique.sumOf {
        it.wisdomFriendRecovery(this, condition)
    }

    // 2.5+5*(1+ヒント発生率)*(1+固有ヒント発生率)
    val hintFrequency = 0.025 + 0.05 * (100 + status.hintFrequency) * (100 + unique.hintFrequency) / 10000.0

    fun checkHint(frequencyUp: Int? = null): Boolean {
        // ヒント発生率アップは基本値*(100+n)/100
        val frequency = frequencyUp?.let { hintFrequency * (100 + it) / 100.0 } ?: hintFrequency
        return !type.outingType && Random.nextDouble() < frequency
    }

    val eventRecovery = (100 + status.eventRecovery) * (100 + unique.eventRecovery) / 10000.0

    val eventEffect = (100 + status.eventEffect) * (100 + unique.eventEffect) / 10000.0

    val hintStatus = if (id == 30098) Status(stamina = 4, power = 12, skillPt = 2) else baseHintStatus

    val trainingRelation = type.trainingRelation

    fun matches(filters: Collection<String>) = filters.all { filter ->
        name.contains(filter) || type.displayName.contains(filter) || skills.any { it.contains(filter) }
    }

    val targetRelation =
        (listOf(0) + (if (type == StatusType.FRIEND) emptyList() else listOf(80)) + specialUnique.mapNotNull { it.targetRelation })
            .sorted().distinct()

    val requiredRelation = max(targetRelation.maxOrNull() ?: 80, if (type == StatusType.FRIEND) 60 else 0)

    fun hasSecondPosition(relation: Int) = specialUnique.any { it.hasSecondPosition(relation) }

    fun positionRateUp(relation: Int) = specialUnique.sumOf { it.positionRateUp(relation) }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SupportCard

        if (id != other.id) return false
        if (talent != other.talent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + talent
        return result
    }
}

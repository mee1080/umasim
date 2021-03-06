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
    ) {
        val initialStatus
            get() = Status(
                initialSpeed,
                initialStamina,
                initialPower,
                initialGuts,
                initialWisdom,
            )
    }

    val initialRelation = status.initialRelation + unique.initialRelation

    val race = status.race + unique.race

    val fan = status.fan + unique.fan

    fun initialStatus(supportType: List<StatusType>) =
        status.initialStatus + unique.initialStatus + specialUnique.fold(Status()) { total, unique ->
            total + unique.initialStatus(supportType)
        }

    fun friendFactor(
        relation: Int,
        friendCount: Int,
        currentStatus: Status,
    ) = (100 + status.friend) * (100 + unique.friend) * (100 + specialUnique.sumOf {
        it.friendFactor(
            relation,
            friendCount,
            currentStatus
        )
    }) / 1000000.0

    fun motivationFactor(relation: Int) =
        status.motivation + unique.motivation + specialUnique.sumOf { it.getMotivation(relation) }

    fun getBaseBonus(type: StatusType, relation: Int) = when (type) {
        StatusType.SPEED -> status.speedBonus + unique.speedBonus
        StatusType.STAMINA -> status.staminaBonus + unique.staminaBonus
        StatusType.POWER -> status.powerBonus + unique.powerBonus
        StatusType.GUTS -> status.gutsBonus + unique.gutsBonus
        StatusType.WISDOM -> status.wisdomBonus + unique.wisdomBonus
        StatusType.SKILL -> status.skillPtBonus + unique.skillPtBonus
        else -> 0
    } + specialUnique.sumOf { it.getBaseBonus(type, relation) }

    fun trainingFactor(
        trainingType: StatusType,
        trainingLevel: Int,
        relation: Int,
        supportTypeCount: Int,
        fanCount: Int,
        currentStatus: Status,
        totalRelation: Int,
        trainingSupportCount: Int,
    ) = status.training + unique.training + specialUnique.sumOf {
        it.trainingFactor(
            type,
            trainingType,
            trainingLevel,
            relation,
            supportTypeCount,
            fanCount,
            currentStatus,
            totalRelation,
            trainingSupportCount,
        )
    }

    val hpCost = status.hpCost + unique.hpCost

    fun failureRate() =
        (100 - status.failureRate) * (100 - unique.failureRate) * specialUnique.fold(1) { acc, v -> acc * v.failureRate() } / 10000.0

    val hintLevel = status.hintLevel + unique.hintLevel

    val specialtyRate = (100 + status.specialtyRate) * (100 + unique.specialtyRate)

    val wisdomFriendRecovery = status.wisdomFriendRecovery + unique.wisdomFriendRecovery

    // 2.5+5*(1+??????????????????)*(1+????????????????????????)
    val hintFrequency = 0.025 + 0.05 * (100 + status.hintFrequency) * (100 + unique.hintFrequency) / 10000.0

    fun checkHint(): Boolean {
        return !type.outingType && Random.nextDouble() < hintFrequency
    }

    val hintStatus = if (id == 30098) Status(stamina = 4, power = 12, skillPt = 2) else baseHintStatus

    val trainingRelation = type.trainingRelation

    fun matches(filters: Collection<String>) = filters.all { filter ->
        name.contains(filter) || type.displayName.contains(filter) || skills.any { it.contains(filter) }
    }

    val targetRelation = (listOf(0, 80) + specialUnique.mapNotNull { it.targetRelation }).sorted().distinct()

    val requiredRelation = targetRelation.maxOrNull() ?: 80
}

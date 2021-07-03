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
    val hintStatus: Status,
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

    val initialRelation get() = status.initialRelation + unique.initialRelation

    val race get() = status.race + unique.race

    val fan get() = status.fan + unique.fan

    val initialStatus = status.initialStatus + unique.initialStatus

    val friendFactor get() = (100 + status.friend) * (100 + unique.friend) / 10000.0

    val motivationFactor = status.motivation + unique.motivation

    fun getBaseBonus(type: StatusType) = when (type) {
        StatusType.SPEED -> status.speedBonus + unique.speedBonus
        StatusType.STAMINA -> status.staminaBonus + unique.staminaBonus
        StatusType.POWER -> status.powerBonus + unique.powerBonus
        StatusType.GUTS -> status.gutsBonus + unique.gutsBonus
        StatusType.WISDOM -> status.wisdomBonus + unique.wisdomBonus
        StatusType.SKILL -> status.skillPtBonus + unique.skillPtBonus
        else -> 0
    }

    val trainingFactor = status.training + unique.training

    val hpCost = status.hpCost + unique.hpCost

    val failureRate = (100 - status.failureRate) * (100 - unique.failureRate) / 10000.0

    val hintLevel = status.hintLevel + unique.hintLevel

    val specialtyRate = (100 + status.specialtyRate) * (100 + unique.specialtyRate)

    val wisdomFriendRecovery = status.wisdomFriendRecovery + unique.wisdomFriendRecovery

    val hintFrequency = (100 + status.hintFrequency) * (100 + unique.hintFrequency) / 10000.0
}

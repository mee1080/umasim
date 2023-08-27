/*
 * Copyright 2023 mee1080
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

private val baseTrainingFactorMap = (0..200).associateWith {
    when {
        it >= 100 -> it / 10 + 20
        it >= 25 -> it / 5 + 10
        it >= 20 -> 13
        it >= 15 -> 10
        it >= 10 -> 8
        it >= 5 -> 5
        else -> 0
    }
}

data class LArcStatus(
    val supporterPt: Int,
    val memberSupporterPt: Int,
    val aptitudePt: Int,
    val overseasTurfAptitude: Int,
    val longchampAptitude: Int,
    val lifeRhythm: Int,
    val nutritionManagement: Int,
    val frenchSkill: Int,
    val overseasExpedition: Int,
    val strongHeart: Int,
    val mentalStrength: Int,
    val hopeOfLArc: Int,
    val consecutiveVictories: Int,
) {
    private val baseTrainingFactor =
        (baseTrainingFactorMap[(supporterPt + memberSupporterPt) / 1700] ?: 0) + (if (hopeOfLArc >= 1) 5 else 0)

    fun getStatusBonus(type: StatusType): Int {
        return when (type) {
            StatusType.SPEED -> if (nutritionManagement >= 1) 3 else 0
            StatusType.STAMINA -> (if (longchampAptitude >= 1) 3 else 0) + (if (strongHeart >= 1) 3 else 0)
            StatusType.POWER -> if (lifeRhythm >= 1) 3 else 0
            StatusType.GUTS -> (if (overseasTurfAptitude >= 1) 3 else 0) + (if (mentalStrength >= 1) 3 else 0)
            StatusType.WISDOM -> if (frenchSkill >= 1) 3 else 0
            StatusType.SKILL -> if (overseasExpedition >= 3) 20 else (if (overseasExpedition >= 1) 10 else 0)
            else -> 0
        }
    }

    fun getTrainingFactor(trainingType: StatusType, overseas: Boolean): Int {
        return baseTrainingFactor + if (overseas) {
            when (trainingType) {
                StatusType.SPEED -> if (nutritionManagement >= 3) 50 else 0
                StatusType.STAMINA -> if (longchampAptitude >= 3) 50 else 0
                StatusType.POWER -> if (lifeRhythm >= 3) 50 else 0
                StatusType.GUTS -> if (overseasTurfAptitude >= 3) 50 else 0
                StatusType.WISDOM -> if (frenchSkill >= 3) 50 else 0
                else -> 0
            }
        } else 0
    }

    val friendFactor get() = if (mentalStrength >= 3) 20 else 0

    fun hpCost(overseas: Boolean) = if (overseas && strongHeart >= 3) 20 else 0
}
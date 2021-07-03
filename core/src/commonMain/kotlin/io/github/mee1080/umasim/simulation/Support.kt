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

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.data.randomSelect
import kotlin.random.Random

data class Support(val index: Int, val card: SupportCard) {

    val name = card.name

    val initialStatus = card.initialStatus + Status(supportRelation = mapOf(index to card.initialRelation))

    fun getFriendBonus(type: StatusType) = if (isFriendTraining(type)) card.friendFactor else 1.0

    fun isFriendTraining(type: StatusType) = friendTrainingEnabled && type == card.type

    val wisdomFriendRecovery get() = if (isFriendTraining(StatusType.WISDOM)) card.wisdomFriendRecovery else 0

    var hint = false
        private set

    var friendTrainingEnabled = false
        private set

    fun checkHintFriend(relation: Int) {
        hint = checkHint()
        friendTrainingEnabled = relation > 80
    }

    // 2.5+5*(1+ヒント発生率)*(1+固有ヒント発生率)
    private val hintRate get() = 0.025 + 0.05 * card.hintFrequency

    fun checkHint(): Boolean {
        return card.type != StatusType.FRIEND && Random.nextDouble() < hintRate
    }

    fun selectTraining(): StatusType {
        if (card.type == StatusType.FRIEND) {
            return randomSelect(
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
        return randomSelect(
            StatusType.SPEED to if (card.type == StatusType.SPEED) mainRate else otherRate,
            StatusType.STAMINA to if (card.type == StatusType.STAMINA) mainRate else otherRate,
            StatusType.POWER to if (card.type == StatusType.POWER) mainRate else otherRate,
            StatusType.GUTS to if (card.type == StatusType.GUTS) mainRate else otherRate,
            StatusType.WISDOM to if (card.type == StatusType.WISDOM) mainRate else otherRate,
            StatusType.NONE to noneRate,
        )
    }

    override fun toString() = "$name hint=$hint friendTrainingEnabled=$friendTrainingEnabled"
}

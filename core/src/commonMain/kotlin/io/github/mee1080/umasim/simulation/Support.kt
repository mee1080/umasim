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

    val initialStatus = card.initialStatus(emptyList()) + Status(supportRelation = mapOf(index to card.initialRelation))

    fun getFriendBonus(type: StatusType) = if (isFriendTraining(type)) card.friendFactor(0, 0) else 1.0

    fun isFriendTraining(type: StatusType) = friendTrainingEnabled && type == card.type

    val wisdomFriendRecovery get() = if (isFriendTraining(StatusType.WISDOM)) card.wisdomFriendRecovery else 0

    var hint = false
        private set

    var relation = 0

    var friendTrainingEnabled = false

    fun checkHintFriend(relation: Int) {
        this.relation = relation
        hint = checkHint()
        friendTrainingEnabled = relation >= 80
    }

    fun checkHint(): Boolean {
        return !card.type.outingType && Random.nextDouble() < card.hintFrequency
    }

    fun selectTraining(): StatusType {
        return randomSelect(*Calculator.calcCardPositionSelection(card))
    }

    override fun toString() = "$name hint=$hint friendTrainingEnabled=$friendTrainingEnabled"
}

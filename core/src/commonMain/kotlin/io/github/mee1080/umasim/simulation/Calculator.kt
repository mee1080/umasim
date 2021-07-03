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

import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.math.min

object Calculator {

    fun calcTrainingSuccessStatus(
        chara: Chara,
        training: TrainingInfo,
        trainingLevel: Int?,
        motivation: Int,
        support: List<Support>
    ) = Status(
        speed = calcTrainingStatus(chara, training, trainingLevel, motivation, support, StatusType.SPEED),
        stamina = calcTrainingStatus(chara, training, trainingLevel, motivation, support, StatusType.STAMINA),
        power = calcTrainingStatus(chara, training, trainingLevel, motivation, support, StatusType.POWER),
        guts = calcTrainingStatus(chara, training, trainingLevel, motivation, support, StatusType.GUTS),
        wisdom = calcTrainingStatus(chara, training, trainingLevel, motivation, support, StatusType.WISDOM),
        skillPt = calcTrainingStatus(chara, training, trainingLevel, motivation, support, StatusType.SKILL),
        hp = calcTrainingHp(training, trainingLevel, support),
    )

    private fun calcTrainingStatus(
        chara: Chara,
        training: TrainingInfo,
        trainingLevel: Int?,
        motivation: Int,
        support: List<Support>,
        type: StatusType
    ): Int {
        val baseStatus = training.getBaseStatus(trainingLevel).get(type)
        if (baseStatus == 0) return 0
        val base = baseStatus + support.sumOf { it.card.getBaseBonus(type) }
        val charaBonus = chara.getBonus(type) / 100.0
        val friend = support
            .map { it.getFriendBonus(training.type) }
            .fold(1.0) { acc, d -> acc * d }
        val motivationBonus = 1 + motivation / 10.0 * (1 + support.sumOf { it.card.motivationFactor } / 100.0)
        val trainingBonus = 1 + support.sumOf { it.card.trainingFactor } / 100.0
        val count = 1 + support.size * 0.05
//        println("$type $base * $charaBonus * $friend * motivationBonus * $trainingBonus * $count")
        return min(100, (base * charaBonus * friend * motivationBonus * trainingBonus * count).toInt())
    }

    private fun calcTrainingHp(training: TrainingInfo, trainingLevel: Int?, support: List<Support>): Int {
        val baseHp = training.getBaseStatus(trainingLevel).hp
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
}
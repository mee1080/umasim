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

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType

class Evaluator(val summaries: List<Summary>) {

    constructor(summaries: List<Summary>, evaluateSetting: Map<StatusType, Pair<Double, Int>>, rate: Double) : this(
        summaries.sortedByDescending { summary ->
            evaluateSetting.entries.sumOf {
                kotlin.math.min(summary.status.get(it.key), it.value.second) * it.value.first
            }
        }.slice(0 until (summaries.size * rate).toInt())
    )

    companion object {
        val outputStatus = arrayOf(
            StatusType.SPEED to 1.0,
            StatusType.STAMINA to 1.0,
            StatusType.POWER to 1.0,
            StatusType.GUTS to 1.0,
            StatusType.WISDOM to 1.0,
            StatusType.SKILL to 0.4,
        )

        val defaultSetting = mapOf(
            StatusType.SPEED to (1.0 to Int.MAX_VALUE),
            StatusType.STAMINA to (1.0 to Int.MAX_VALUE),
            StatusType.POWER to (1.0 to Int.MAX_VALUE),
            StatusType.GUTS to (1.0 to Int.MAX_VALUE),
            StatusType.WISDOM to (1.0 to Int.MAX_VALUE),
            StatusType.SKILL to (0.4 to Int.MAX_VALUE),
        )
    }

    val results = lazy { summaries.map { it.status } }

    private val status = mutableMapOf<StatusType, List<Int>>()

    private fun getStatus(type: StatusType) = status.getOrPut(type) { results.value.map { it.get(type) } }

    fun max(type: StatusType) = getStatus(type).maxOrNull() ?: 0

    fun average(type: StatusType) = getStatus(type).average()

    fun average() = outputStatus.map { average(it.first) }

    fun upper(type: StatusType, rate: Double) = getStatus(type).sortedDescending()
        .slice(0 until (summaries.size * rate).toInt()).average()

    fun upper(rate: Double) = outputStatus.map { upper(it.first, rate) }

    private fun getStatusSum(vararg type: StatusType) = results.value.map { result ->
        type.sumOf { result.get(it) }
    }

    private fun getStatusSum(vararg typeToFactor: Pair<StatusType, Double>, limit: Int = Int.MAX_VALUE) =
        results.value.map { result ->
            typeToFactor.sumOf { kotlin.math.min(result.get(it.first), limit) * it.second }
        }

    fun getStatusSum(typeToFactorLimit: Map<StatusType, Pair<Double, Int>>) =
        results.value.map { result ->
            typeToFactorLimit.entries.sumOf { kotlin.math.min(result.get(it.key), it.value.second) * it.value.first }
        }

    fun upperSum(rate: Double, vararg type: StatusType) = getStatusSum(*type).sortedDescending()
        .slice(0 until (summaries.size * rate).toInt()).average()

    fun upperSum(rate: Double, vararg typeToFactor: Pair<StatusType, Double>, limit: Int = Int.MAX_VALUE) =
        getStatusSum(*typeToFactor, limit = limit).sortedDescending()
            .slice(0 until (summaries.size * rate).toInt()).average()

    fun upperSum(rate: Double, typeToFactorLimit: Map<StatusType, Pair<Double, Int>>) =
        getStatusSum(typeToFactorLimit).sortedDescending()
            .slice(0 until (summaries.size * rate).toInt()).average()

    fun upperSum(rate: Double, limit: Int = Int.MAX_VALUE) = upperSum(rate, *outputStatus, limit = limit)

    fun averageSum(vararg type: StatusType) = getStatusSum(*type).average()

    fun averageSum(vararg typeToFactor: Pair<StatusType, Double>) = getStatusSum(*typeToFactor).average()

    fun averageSum() = averageSum(*outputStatus)

    fun averageTrainingCount(type: StatusType) = summaries.map { it.trainingCount[type]!! }.average()

    fun averageFriendTrainingCount(type: StatusType) = summaries.map { it.trainingFriendCount[type]!! }
        .fold(IntArray(6)) { acc, value -> acc.mapIndexed { index, i -> i + value[index] }.toIntArray() }
        .map { it / summaries.size.toDouble() }

    fun trainingCount(type: StatusType) =
        listOf(averageTrainingCount(type), *averageFriendTrainingCount(type).toTypedArray())

    fun averageSleepCount() = summaries.map { it.sleepCount }.average()

    fun averageOutingCount() = summaries.map { it.outingCount }.average()

    fun averageRaceCount() = summaries.map { it.raceCount }.average()

    private val skillHints by lazy {
        results.value.map { it.skillHint.values.sum() }
    }

    private val averageSkillHint by lazy { skillHints.average() }

    fun toSummaryString(): String {
        val skillFactor = 1.0
        return arrayOf<Any>(
            // ステータス
            average(StatusType.SPEED),
            average(StatusType.STAMINA),
            average(StatusType.POWER),
            average(StatusType.GUTS),
            average(StatusType.WISDOM),
            average(StatusType.SKILL),
            averageSkillHint,

            // トレーニング
            trainingType.joinToString(",") { type ->
                val averageTrainingCount = averageTrainingCount(type)
                val friendTrainingCount = averageFriendTrainingCount(type)
                "$averageTrainingCount,${friendTrainingCount.joinToString(",")}"
            },

            // スピード評価
            averageSum(StatusType.SPEED to 1.0, StatusType.POWER to 1.0, StatusType.SKILL to skillFactor),

            // スタミナ評価
            averageSum(StatusType.STAMINA to 1.0, StatusType.GUTS to 1.0, StatusType.SKILL to skillFactor),

            // パワー評価
            averageSum(StatusType.STAMINA to 1.0, StatusType.POWER to 1.0, StatusType.SKILL to skillFactor),

            // 根性評価
            averageSum(
                StatusType.SPEED to 1.0,
                StatusType.POWER to 1.0,
                StatusType.GUTS to 1.0,
                StatusType.SKILL to skillFactor
            ),

            // 賢さ評価
            averageSum(StatusType.SPEED to 1.0, StatusType.WISDOM to 1.0, StatusType.SKILL to skillFactor),

            // 全合計
            averageSum(
                StatusType.SPEED to 1.0,
                StatusType.STAMINA to 1.0,
                StatusType.POWER to 1.0,
                StatusType.GUTS to 1.0,
                StatusType.WISDOM to 1.0,
                StatusType.SKILL to skillFactor
            ),

            // お休み・お出かけ・レース
            averageSleepCount(),
            averageOutingCount(),
            averageRaceCount(),
        ).joinToString(",")
//        val averageSpeed = average(StatusType.SPEED)
//        val upperSpeed = upper(StatusType.SPEED, 0.2)
//        val averagePower = average(StatusType.POWER)
//        val upperPower = upper(StatusType.POWER, 0.2)
//        val averageStamina = average(StatusType.STAMINA)
//        val upperStamina = upper(StatusType.STAMINA, 0.2)
//        val averageGuts = average(StatusType.GUTS)
//        val upperGuts = upper(StatusType.GUTS, 0.2)
//        val averageWisdom = average(StatusType.WISDOM)
//        val upperWisdom = upper(StatusType.WISDOM, 0.2)
//        val trainingInfo = trainingType.map { type ->
//            val averageTrainingCount = summaries.map { it.trainingCount[type]!! }.average()
//            val friendTrainingCount = summaries.map { it.trainingFriendCount[type]!! }
//                .fold(IntArray(6)) { acc, value -> acc.mapIndexed { index, i -> i + value[index] }.toIntArray() }
//                .map { it / summaries.size.toDouble() }
//            "$averageTrainingCount,${friendTrainingCount.joinToString(",")}"
//        }.joinToString(",")
//        val averageSpeedPower = averageSum(StatusType.SPEED, StatusType.POWER)
//        val upperSpeedPower = upperSum(0.2, StatusType.SPEED, StatusType.POWER)
//        val averageSpeedStamina = averageSum(StatusType.SPEED, StatusType.STAMINA)
//        val upperSpeedStamina = upperSum(0.2, StatusType.SPEED, StatusType.STAMINA)
//        val averageSpeedPowerStamina = averageSum(StatusType.SPEED, StatusType.POWER, StatusType.STAMINA)
//        val upperSpeedPowerStamina = upperSum(0.2, StatusType.SPEED, StatusType.POWER, StatusType.STAMINA)
//        val averageSpeedPowerStaminaGuts =
//            averageSum(StatusType.SPEED, StatusType.POWER, StatusType.STAMINA, StatusType.GUTS)
//        val upperSpeedPowerStaminaGuts =
//            upperSum(0.2, StatusType.SPEED, StatusType.POWER, StatusType.STAMINA, StatusType.GUTS)
//        return "$averageSpeed,$averageStamina,$averagePower,$averageGuts,$averageWisdom,$upperSpeed,$upperStamina,$upperPower,$upperGuts,$upperWisdom,$trainingInfo,$averageSpeedPower,$upperSpeedPower,$averageSpeedStamina,$upperSpeedStamina,$averageSpeedPowerStamina,$upperSpeedPowerStamina,$averageSpeedPowerStaminaGuts,$upperSpeedPowerStaminaGuts"
    }
}

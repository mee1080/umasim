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

enum class StatusType(val displayName: String) {
    SPEED("スピード"),
    STAMINA("スタミナ"),
    POWER("パワー"),
    GUTS("根性"),
    WISDOM("賢さ"),
    FRIEND("友人"),
    SKILL("スキルPt"),
    NONE("未選択"),
}

val trainingType = arrayOf(StatusType.SPEED, StatusType.STAMINA, StatusType.POWER, StatusType.GUTS, StatusType.WISDOM)

fun toSupportType(value: String) = when (value) {
    "スピード", "S" -> StatusType.SPEED
    "スタミナ", "H" -> StatusType.STAMINA
    "パワー", "P" -> StatusType.POWER
    "根性", "G" -> StatusType.GUTS
    "賢さ", "W" -> StatusType.WISDOM
    "友人" -> StatusType.FRIEND
    "スキルPt" -> StatusType.SKILL
    else -> throw IllegalArgumentException()
}

fun randomSelect(vararg values: Int): Int {
    var random = Random.nextInt(values.sum())
    for (i in values.indices) {
        random -= values[i]
        if (random < 0) return i
    }
    return values.size - 1
}

fun <T> randomSelect(values: Map<T, Int>): T {
    val list = values.entries.toList()
    val index = randomSelect(*(list.map { it.value }.toIntArray()))
    return list[index].key
}

fun <T> randomSelect(vararg values: Pair<T, Int>): T {
    val index = randomSelect(*(values.map { it.second }.toIntArray()))
    return values[index].first
}

fun <T> randomSelect(values: List<Pair<T, Int>>): T {
    val index = randomSelect(*(values.map { it.second }.toIntArray()))
    return values[index].first
}

fun <T> calcRate(value: T, vararg values: Pair<T, Int>): Double {
    val target = values.firstOrNull { it.first == value } ?: return 0.0
    return target.second.toDouble() / values.sumOf { it.second }
}

enum class Scenario(val displayName: String) {
    URA("URA"),
    AOHARU("アオハル(作成途中)"),
}

fun toScenario(value: String) = toScenario(value.toIntOrNull() ?: 0)

fun toScenario(value: Int) = when (value) {
    2 -> Scenario.AOHARU
    else -> Scenario.URA
}

fun turnToString(turn: Int) = buildString {
    append(
        when {
            turn <= 24 -> "ジュニア"
            turn <= 48 -> "クラシック"
            turn <= 72 -> "シニア"
            else -> "ファイナルズ"
        }
    )
    append((turn - 1) % 24 / 2 + 1)
    append("月")
    append(if (turn % 2 == 1) "前半" else "後半")
}

enum class RaceGrade {
    DEBUT, OPEN, G3, G2, G1, FINALS, UNKNOWN,
}

fun toRaceGrade(value: Int) = when {
    value < 100 -> RaceGrade.UNKNOWN
    value < 200 -> RaceGrade.G1
    value < 300 -> RaceGrade.G2
    value < 400 -> RaceGrade.G3
    value < 900 -> RaceGrade.OPEN
    value < 1000 -> RaceGrade.DEBUT
    else -> RaceGrade.UNKNOWN
}
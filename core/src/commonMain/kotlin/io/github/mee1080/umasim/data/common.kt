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

enum class StatusType(val displayName: String, val outingType: Boolean = false) {
    SPEED("スピード"),
    STAMINA("スタミナ"),
    POWER("パワー"),
    GUTS("根性"),
    WISDOM("賢さ"),
    FRIEND("友人", outingType = true),
    GROUP("グループ", outingType = true), SKILL("スキルPt"), NONE("未選択");

    val trainingRelation = if (outingType) 4 else 7
}

val trainingType = arrayOf(StatusType.SPEED, StatusType.STAMINA, StatusType.POWER, StatusType.GUTS, StatusType.WISDOM)

val trainingTypeOrSkill = trainingType + StatusType.SKILL

fun upInTraining(training: StatusType, target: StatusType): Boolean = if (target == StatusType.SKILL) true else {
    when (training) {
        StatusType.SPEED -> target == StatusType.SPEED || target == StatusType.POWER
        StatusType.STAMINA -> target == StatusType.STAMINA || target == StatusType.GUTS
        StatusType.POWER -> target == StatusType.POWER || target == StatusType.STAMINA
        StatusType.GUTS -> target == StatusType.SPEED || target == StatusType.POWER || target == StatusType.GUTS
        StatusType.WISDOM -> target == StatusType.SPEED || target == StatusType.WISDOM
        else -> false
    }
}

fun randomSingleTrainingType() = trainingType.asList().shuffled().first()

fun randomTrainingType(count: Int = 1) = trainingType.asList().shuffled().subList(0, count)

fun toSupportType(value: String) = when (value) {
    "スピード", "S" -> StatusType.SPEED
    "スタミナ", "H" -> StatusType.STAMINA
    "パワー", "P" -> StatusType.POWER
    "根性", "G" -> StatusType.GUTS
    "賢さ", "W" -> StatusType.WISDOM
    "友人" -> StatusType.FRIEND
    "グループ" -> StatusType.GROUP
    "スキルPt" -> StatusType.SKILL
    else -> StatusType.NONE
}

fun random(min: Int, max: Int) = min + Random.nextInt(max - min)

fun randomSelect(vararg values: Int): Int {
    var random = Random.nextInt(values.sum())
    for (i in values.indices) {
        random -= values[i]
        if (random < 0) return i
    }
    return values.size - 1
}

fun randomSelect(vararg values: Double): Int {
    var random = Random.nextDouble(values.sum())
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

fun <T> randomSelectDouble(values: List<Pair<T, Double>>): T {
    val index = randomSelect(*(values.map { it.second }.toDoubleArray()))
    return values[index].first
}

fun <T> calcRate(value: T, vararg values: Pair<T, Int>): Double {
    val target = values.firstOrNull { it.first == value } ?: return 0.0
    return target.second.toDouble() / values.sumOf { it.second }
}

fun <T> randomSelectPercent(percent: Double, success: T, failed: T): T {
    return if (Random.nextDouble() < percent) success else failed
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
    if (turn <= 72) {
        append((turn - 1) % 24 / 2 + 1)
        append("月")
        append(if (turn % 2 == 1) "前半" else "後半")
    } else {
        append(turn - 72)
    }
}

enum class RaceGrade(val displayName: String) {
    DEBUT("メイクデビュー"),
    PRE_OPEN("Pre-OP"),
    OPEN("OP"),
    G3("GIII"),
    G2("GII"),
    G1("GI"),
    FINALS("ファイナルズ"),
    UNKNOWN("不明"),
}

fun toRaceGrade(value: Int) = when {
    value < 100 -> RaceGrade.UNKNOWN
    value < 200 -> RaceGrade.G1
    value < 300 -> RaceGrade.G2
    value < 400 -> RaceGrade.G3
    value < 500 -> RaceGrade.OPEN
    value < 800 -> RaceGrade.PRE_OPEN
    //value < 900 -> RaceGrade.NOT_WIN
    value < 1000 -> RaceGrade.DEBUT
    else -> RaceGrade.UNKNOWN
}

enum class RaceDistance(val displayName: String) {
    SHORT("短距離"), MILE("マイル"), MIDDLE("中距離"), LONG("長距離"), UNKNOWN("不明"),
}

fun toRaceDistance(value: Int) = when {
    value <= 1400 -> RaceDistance.SHORT
    value <= 1800 -> RaceDistance.MILE
    value <= 2400 -> RaceDistance.MIDDLE
    else -> RaceDistance.LONG
}

enum class RaceGround(val displayName: String) {
    TURF("芝"), DIRT("ダート"), UNKNOWN("不明"),
}

fun toRaceGround(value: String) = when (value) {
    "芝" -> RaceGround.TURF
    "ダート" -> RaceGround.DIRT
    else -> RaceGround.UNKNOWN
}

enum class RaceRunningStyle(val displayName: String) {
    NIGE("逃げ"), SENKO("先行"), SASHI("差し"), OIKOMI("追込"),
}

fun motivationToString(motivation: Int) = when (motivation) {
    3 -> "超絶好調"
    2 -> "絶好調"
    1 -> "好調"
    0 -> "普通"
    -1 -> "不調"
    -2 -> "絶不調"
    else -> "不明"
}

abstract class RatedSet<T>(
    val list: List<Pair<T, Int>>,
) {
    val total by lazy { list.sumOf { it.second } }

    val rateList by lazy { list.map { it.first to it.second.toDouble() / total } }

    fun filter(predicate: (T) -> Boolean): RatedSet<T> {
        return getInstance(list.filter { predicate(it.first) })
    }

    protected abstract fun getInstance(newList: List<Pair<T, Int>>): RatedSet<T>
}
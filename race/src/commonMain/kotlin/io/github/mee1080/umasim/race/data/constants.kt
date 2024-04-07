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
/*
 * This file was ported from uma-clock-emu by Romulus Urakagi Tsai(@urakagi)
 * https://github.com/urakagi/uma-clock-emu
 */
package io.github.mee1080.umasim.race.data

enum class Distance {
    SHORT,
    MILE,
    MIDDLE,
    LONG
}

enum class Style(val value: Int, val text: String) {
    NIGE(1, "逃げ"),
    SEN(2, "先行"),
    SASI(3, "差し"),
    OI(4, "追込"),
    OONIGE(1, "大逃げ"),
}

enum class Surface {
    TURF,
    DIRT
}

enum class Condition(val value: Int, val label: String) {
    BEST(5, "絶好調"),
    GOOD(4, "好調"),
    NORMAL(3, "普通"),
    BAD(2, "不調"),
    WORST(1, "絶不調"),
}

enum class FitRank {
    S, A, B, C, D, E, F, G,
}

enum class CourseCondition(val value: Int, val label: String) {
    GOOD(1, "良"),
    YAYAOMO(2, "稍重"),
    OMO(3, "重"),
    BAD(4, "不良"),
}

enum class SkillActivateAdjustment(val value: Int, val label: String) {
    NONE(0, "無"),
    YES(1, "確定発動"),
    ALL(2, "全乱数固定"),
}

enum class RandomPosition(val value: Int, val label: String) {
    RANDOM(0, "ランダム"),
    FASTEST(1, "最速"),
    FAST(2, "1/4"),
    MIDDLE(3, "中間"),
    SLOW(4, "3/4"),
    SLOWEST(5, "最遅"),
}

internal const val framePerSecond = 15
internal const val secondPerFrame = 1.0 / framePerSecond
internal const val startSpeed = 3.0
internal const val maxSpeed = 30.0

/**
 * やる気->ステータス補正倍率
 */
internal val condCoef = mapOf(
    Condition.BEST to 1.04,
    Condition.GOOD to 1.02,
    Condition.NORMAL to 1.0,
    Condition.BAD to 0.98,
    Condition.WORST to 0.96,
)

/**
 * バ場->バ場状態->スピード補正値
 */
internal val surfaceSpeedModify = mapOf(
    1 to mapOf(
        1 to 0,
        2 to 0,
        3 to 0,
        4 to -50
    ),
    2 to mapOf(
        1 to 0,
        2 to 0,
        3 to 0,
        4 to -50
    )
)

/**
 * バ場->バ場状態->パワー補正値
 */
internal val surfacePowerModify = mapOf(
    1 to mapOf(
        1 to 0,
        2 to -50,
        3 to -50,
        4 to -50
    ),
    2 to mapOf(
        1 to -100,
        2 to -50,
        3 to -100,
        4 to -100
    )
)

/**
 * 脚質適性->賢さ補正倍率
 */
internal val styleFitCoef = mapOf(
    FitRank.S to 1.1,
    FitRank.A to 1.0,
    FitRank.B to 0.85,
    FitRank.C to 0.75,
    FitRank.D to 0.6,
    FitRank.E to 0.4,
    FitRank.F to 0.2,
    FitRank.G to 0.1,
)

/**
 * 距離適性->スパート速度補正倍率
 */
internal val distanceFitSpeedCoef = mapOf(
    FitRank.S to 1.05,
    FitRank.A to 1.0,
    FitRank.B to 0.9,
    FitRank.C to 0.8,
    FitRank.D to 0.6,
    FitRank.E to 0.4,
    FitRank.F to 0.2,
    FitRank.G to 0.1,
)

/**
 * 距離適性->加速度補正倍率
 */
internal val distanceFitAccelerateCoef = mapOf(
    FitRank.S to 1.0,
    FitRank.A to 1.0,
    FitRank.B to 1.0,
    FitRank.C to 1.0,
    FitRank.D to 1.0,
    FitRank.E to 0.6,
    FitRank.F to 0.5,
    FitRank.G to 0.4,
)

/**
 * バ場適性->加速度補正倍率
 */
internal val surfaceFitAccelerateCoef = mapOf(
    FitRank.S to 1.05,
    FitRank.A to 1.0,
    FitRank.B to 0.9,
    FitRank.C to 0.8,
    FitRank.D to 0.7,
    FitRank.E to 0.5,
    FitRank.F to 0.3,
    FitRank.G to 0.1,
)

/**
 * 脚質->最大体力補正倍率
 */
private val styleSpCoefData = mapOf(
    Style.NIGE to 0.95,
    Style.SEN to 0.89,
    Style.SASI to 1.0,
    Style.OI to 0.995,
    Style.OONIGE to 0.86
)

val Style.styleSpCoef get() = styleSpCoefData[this]!!

/**
 * 脚質->フェーズ->目標速度補正倍率
 */
private val styleSpeedCoefData = mapOf(
    Style.NIGE to mapOf(
        0 to 1.0,
        1 to 0.98,
        2 to 0.962,
        3 to 0.962,
    ),
    Style.SEN to mapOf(
        0 to 0.978,
        1 to 0.991,
        2 to 0.975,
        3 to 0.975,
    ),
    Style.SASI to mapOf(
        0 to 0.938,
        1 to 0.998,
        2 to 0.994,
        3 to 0.994,
    ),
    Style.OI to mapOf(
        0 to 0.931,
        1 to 1.0,
        2 to 1.0,
        3 to 1.0,
    ),
    Style.OONIGE to mapOf(
        0 to 1.063,
        1 to 0.962,
        2 to 0.95,
        3 to 0.95,
    )
)

val Style.styleSpeedCoef get() = styleSpeedCoefData[this]!!

/**
 * 脚質->フェーズ->加速度補正倍率
 */
private val styleAccelerateCoefData = mapOf(
    Style.NIGE to mapOf(
        0 to 1.0,
        1 to 1.0,
        2 to 0.996,
        3 to 0.996
    ),
    Style.SEN to mapOf(
        0 to 0.985,
        1 to 1.0,
        2 to 0.996,
        3 to 0.996
    ),
    Style.SASI to mapOf(
        0 to 0.975,
        1 to 1.0,
        2 to 1.0,
        3 to 1.0
    ),
    Style.OI to mapOf(
        0 to 0.945,
        1 to 1.0,
        2 to 0.997,
        3 to 0.997
    ),
    Style.OONIGE to mapOf(
        0 to 1.17,
        1 to 0.94,
        2 to 0.956,
        3 to 0.956
    )
)

val Style.styleAccelerateCoef get() = styleAccelerateCoefData[this]!!

/**
 * バ場->バ場状態->体力消費補正値
 */
internal val spConsumptionCoef = mapOf(
    1 to mapOf(
        1 to 1.0,
        2 to 1.0,
        3 to 1.02,
        4 to 1.02
    ),
    2 to mapOf(
        1 to 1.0,
        2 to 1.0,
        3 to 1.01,
        4 to 1.02
    )
)

internal val skillLevelValueDefault = listOf(1.0, 1.0, 1.02, 1.04, 1.06, 1.08, 1.1)

internal val skillLevelValueSpeed = listOf(1.0, 1.0, 1.01, 1.04, 1.07, 1.1, 1.13)

/**
 * ベース脚質->距離->脚色十分加速度補正倍率
 */
internal val conservePowerAccelerationCoef = mapOf(
    Style.NIGE to mapOf(
        Distance.SHORT to 1.0,
        Distance.MILE to 1.0,
        Distance.MIDDLE to 1.0,
        Distance.LONG to 1.0,
    ),
    Style.SEN to mapOf(
        Distance.SHORT to 0.7,
        Distance.MILE to 0.8,
        Distance.MIDDLE to 0.9,
        Distance.LONG to 0.9,
    ),
    Style.SASI to mapOf(
        Distance.SHORT to 0.75,
        Distance.MILE to 0.7,
        Distance.MIDDLE to 0.875,
        Distance.LONG to 1.0,
    ),
    Style.OI to mapOf(
        Distance.SHORT to 0.7,
        Distance.MILE to 0.75,
        Distance.MIDDLE to 0.86,
        Distance.LONG to 0.9,
    ),
)

/**
 * 脚色十分持続時間
 */
internal const val conservePowerBaseFrame = framePerSecond * 3

/**
 * 距離->脚色十分時間補正倍率
 */
internal val conservePowerTimeCoef = mapOf(
    Distance.SHORT to 0.45,
    Distance.MILE to 1.0,
    Distance.MIDDLE to 0.875,
    Distance.LONG to 0.8,
)

/**
 * 脚質->位置取り調整速度補正倍率
 */
internal val positionCompetitionSpeedCoef = mapOf(
    Style.OONIGE to 0.2,
    Style.NIGE to 0.8,
    Style.SEN to 1.0,
    Style.SASI to 1.0,
    Style.OI to 1.0,
)

/**
 * 脚質->位置取り調整体力消費補正倍率
 */
internal val positionCompetitionStaminaCoef = mapOf(
    Style.OONIGE to 1.5,
    Style.NIGE to 1.2,
    Style.SEN to 1.0,
    Style.SASI to 1.0,
    Style.OI to 1.0,
)

/**
 * 距離->位置取り調整体力消費補正倍率
 */
internal fun positionCompetitionDistanceCoef(distance: Int) = when {
    distance < 1401 -> 0.3
    distance < 1801 -> 0.3
    distance < 2101 -> 0.5
    distance < 2201 -> 0.8
    distance < 2401 -> 1.0
    distance < 2601 -> 1.1
    else -> 1.2
}

/**
 * 脚質->リード確保速度補正倍率
 */
internal val secureLeadSpeedCoef = mapOf(
    Style.OONIGE to 0.2,
    Style.NIGE to 1.0,
    Style.SEN to 1.0,
    Style.SASI to 0.8,
    Style.OI to 0.0,
)

/**
 * 脚質->リード確保体力消費補正倍率
 */
internal val secureLeadStaminaCoef = mapOf(
    Style.OONIGE to 1.2,
    Style.NIGE to 1.0,
    Style.SEN to 0.8,
    Style.SASI to 0.8,
    Style.OI to 0.0,
)

/**
 * 距離->リード確保体力消費補正倍率
 */
internal fun secureLeadDistanceCoef(distance: Int) = when {
    distance < 1401 -> 0.3
    distance < 1801 -> 0.3
    distance < 2101 -> 0.5
    distance < 2201 -> 0.8
    distance < 2401 -> 1.0
    distance < 2601 -> 1.1
    else -> 1.2
}

/**
 * 距離->スタミナ勝負補正倍率
 */
internal fun staminaLimitBreakDistanceCoef(distance: Int) = when {
    distance < 2101 -> 0.0
    distance < 2201 -> 0.5
    distance < 2401 -> 1.0
    distance < 2601 -> 1.2
    else -> 1.5
}

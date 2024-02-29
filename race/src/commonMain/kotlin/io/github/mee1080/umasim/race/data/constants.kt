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

internal val frameLength = 1.0 / 15
internal val startSpeed = 3.0
internal val maxSpeed = 30.0

internal val condCoef = mapOf(
    Condition.BEST to 1.04,
    Condition.GOOD to 1.02,
    Condition.NORMAL to 1.0,
    Condition.BAD to 0.98,
    Condition.WORST to 0.96,
)

internal val surfaceSpeedModify = mapOf(
    1 to mapOf(
        0 to 0,
        1 to 0,
        2 to 0,
        3 to 0,
        4 to -50
    ),
    2 to mapOf(
        0 to 0,
        1 to 0,
        2 to 0,
        3 to 0,
        4 to -50
    )
)

internal val surfacePowerModify = mapOf(
    1 to mapOf(
        0 to 0,
        1 to 0,
        2 to -50,
        3 to -50,
        4 to -50
    ),
    2 to mapOf(
        0 to -100,
        1 to -100,
        2 to -50,
        3 to -100,
        4 to -100
    )
)

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

private val styleSpCoefData = mapOf(
    Style.NIGE to 0.95,
    Style.SEN to 0.89,
    Style.SASI to 1.0,
    Style.OI to 0.995,
    Style.OONIGE to 0.86
)

val Style.styleSpCoef get() = styleSpCoefData[this]!!

private val styleSpeedCoefData = mapOf(
    Style.NIGE to mapOf(
        0 to 1.0,
        1 to 0.98,
        2 to 0.962
    ),
    Style.SEN to mapOf(
        0 to 0.978,
        1 to 0.991,
        2 to 0.975,
    ),
    Style.SASI to mapOf(
        0 to 0.938,
        1 to 0.998,
        2 to 0.994
    ),
    Style.OI to mapOf(
        0 to 0.931,
        1 to 1.0,
        2 to 1.0
    ),
    Style.OONIGE to mapOf(
        0 to 1.063,
        1 to 0.962,
        2 to 0.95
    )
)

val Style.styleSpeedCoef get() = styleSpeedCoefData[this]!!

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
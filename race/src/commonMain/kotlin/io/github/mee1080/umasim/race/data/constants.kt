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

enum class Condition(val value: Int) {
    BEST(5), GOOD(4), NORMAL(3), BAD(2), WORST(1),
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
    "S" to 1.1,
    "A" to 1.0,
    "B" to 0.85,
    "C" to 0.75,
    "D" to 0.6,
    "E" to 0.4,
    "F" to 0.2,
    "G" to 0.1
)

internal val distanceFitSpeedCoef = mapOf(
    "S" to 1.05,
    "A" to 1.0,
    "B" to 0.9,
    "C" to 0.8,
    "D" to 0.6,
    "E" to 0.4,
    "F" to 0.2,
    "G" to 0.1
)

internal val distanceFitAccelerateCoef = mapOf(
    "S" to 1.0,
    "A" to 1.0,
    "B" to 1.0,
    "C" to 1.0,
    "D" to 1.0,
    "E" to 0.6,
    "F" to 0.5,
    "G" to 0.4
)

internal val surfaceFitAccelerateCoef = mapOf(
    "S" to 1.05,
    "A" to 1.0,
    "B" to 0.9,
    "C" to 0.8,
    "D" to 0.7,
    "E" to 0.5,
    "F" to 0.3,
    "G" to 0.1
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
    Style.OONIGE.ordinal to mapOf(
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
    Style.OONIGE.ordinal to mapOf(
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
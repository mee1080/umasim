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

enum class Style(val value: Int) {
    NIGE(1),
    SEN(2),
    SASI(3),
    OI(4),
    OONIGE(1),
}

enum class Surface {
    TURF,
    DIRT
}

enum class Condition(val value: Int) {
    BEST(5), GOOD(4), NORMAL(3), BAD(2), WORST(1),
}

internal val frameLength = 1.0f / 15
internal val startSpeed = 3.0f
internal val maxSpeed = 30.0f

internal val condCoef = mapOf(
    Condition.BEST to 1.04f,
    Condition.GOOD to 1.02f,
    Condition.NORMAL to 1.0f,
    Condition.BAD to 0.98f,
    Condition.WORST to 0.96f,
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
    "S" to 1.1f,
    "A" to 1.0f,
    "B" to 0.85f,
    "C" to 0.75f,
    "D" to 0.6f,
    "E" to 0.4f,
    "F" to 0.2f,
    "G" to 0.1f
)

internal val distanceFitSpeedCoef = mapOf(
    "S" to 1.05f,
    "A" to 1.0f,
    "B" to 0.9f,
    "C" to 0.8f,
    "D" to 0.6f,
    "E" to 0.4f,
    "F" to 0.2f,
    "G" to 0.1f
)

internal val distanceFitAccelerateCoef = mapOf(
    "S" to 1.0f,
    "A" to 1.0f,
    "B" to 1.0f,
    "C" to 1.0f,
    "D" to 1.0f,
    "E" to 0.6f,
    "F" to 0.5f,
    "G" to 0.4f
)

internal val surfaceFitAccelerateCoef = mapOf(
    "S" to 1.05f,
    "A" to 1.0f,
    "B" to 0.9f,
    "C" to 0.8f,
    "D" to 0.7f,
    "E" to 0.5f,
    "F" to 0.3f,
    "G" to 0.1f
)

private val styleSpCoefData = mapOf(
    Style.NIGE to 0.95f,
    Style.SEN to 0.89f,
    Style.SASI to 1.0f,
    Style.OI to 0.995f,
    Style.OONIGE to 0.86f
)

val Style.styleSpCoef get() = styleSpCoefData[this]!!

private val styleSpeedCoefData = mapOf(
    Style.NIGE to mapOf(
        0 to 1.0f,
        1 to 0.98f,
        2 to 0.962f
    ),
    Style.SEN to mapOf(
        0 to 0.978f,
        1 to 0.991f,
        2 to 0.975f,
    ),
    Style.SASI to mapOf(
        0 to 0.938f,
        1 to 0.998f,
        2 to 0.994f
    ),
    Style.OI to mapOf(
        0 to 0.931f,
        1 to 1.0f,
        2 to 1.0f
    ),
    Style.OONIGE.ordinal to mapOf(
        0 to 1.063f,
        1 to 0.962f,
        2 to 0.95f
    )
)

val Style.styleSpeedCoef get() = styleSpeedCoefData[this]!!

private val styleAccelerateCoefData = mapOf(
    Style.NIGE to mapOf(
        0 to 1.0f,
        1 to 1.0f,
        2 to 0.996f,
        3 to 0.996f
    ),
    Style.SEN to mapOf(
        0 to 0.985f,
        1 to 1.0f,
        2 to 0.996f,
        3 to 0.996f
    ),
    Style.SASI to mapOf(
        0 to 0.975f,
        1 to 1.0f,
        2 to 1.0f,
        3 to 1.0f
    ),
    Style.OI to mapOf(
        0 to 0.945f,
        1 to 1.0f,
        2 to 0.997f,
        3 to 0.997f
    ),
    Style.OONIGE.ordinal to mapOf(
        0 to 1.17f,
        1 to 0.94f,
        2 to 0.956f,
        3 to 0.956f
    )
)

val Style.styleAccelerateCoef get() = styleAccelerateCoefData[this]!!

internal val spConsumptionCoef = mapOf(
    1 to mapOf(
        1 to 1.0f,
        2 to 1.0f,
        3 to 1.02f,
        4 to 1.02f
    ),
    2 to mapOf(
        1 to 1.0f,
        2 to 1.0f,
        3 to 1.01f,
        4 to 1.02f
    )
)
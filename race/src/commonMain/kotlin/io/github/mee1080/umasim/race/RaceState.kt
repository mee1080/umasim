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
package io.github.mee1080.umasim.race

import io.github.mee1080.umasim.race.data.*
import kotlin.math.*

data class UmaStatus(
    val speed: Int = 1600,
    val stamina: Int = 1000,
    val power: Int = 1300,
    val guts: Int = 1000,
    val wisdom: Int = 1200,
    val condition: Int = 2,
    val style: Style = Style.OI,
    val distanceFit: String = "S",
    val surfaceFit: String = "A",
    val styleFit: String = "A",
    val oonige: Boolean = false,
) {
    val runningStyle get() = if (oonige) Style.OONIGE else style
    val basicRunningStyle get() = style
}

enum class FitRank {
    S, A, B, C, D, E, F, G,
}

data class Track(
    val location: Int = 0,
    val course: Int = 0,
    val surfaceCondition: Int = 1,
)

data class PassiveBonus(
    val speed: Int = 0,
    val stamina: Int = 0,
    val power: Int = 0,
    val guts: Int = 0,
    val wisdom: Int = 0,
    val temptationRate: Int = 0,
) {
    fun add(skill: SkillEffect): PassiveBonus {
        return if (skill.isPassive) copy(
            speed = skill.passiveSpeed ?: 0,
            stamina = skill.passiveStamina ?: 0,
            power = skill.passivePower ?: 0,
            guts = skill.passiveGuts ?: 0,
            wisdom = skill.passiveWisdom ?: 0,
            temptationRate = skill.temptationRate ?: 0
        ) else this
    }
}

data class RaceState(
    val setting: RaceSetting,
    val simulation: RaceSimulationState,
) {
    fun update(simulation: RaceSimulationState): RaceState {
        return copy(simulation = simulation)
    }

    fun getPhase(position: Int): Int {
        return when {
            position < setting.trackDetail.distance / 6.0f -> 0
            position < (setting.trackDetail.distance * 2.0f) / 3 -> 1
            position < (setting.trackDetail.distance * 5.0f) / 6 -> 2
            else -> 3
        }
    }

    val currentPhase get() = getPhase(simulation.position)

    fun getSection(position: Int): Int {
        return floor((position * 24.0f) / setting.courseLength).toInt()
    }

    val currentSection get() = getSection(simulation.position)

    val currentSlope get() = setting.trackDetail.getSlope(simulation.position)

    val targetSpeed: Float
        get() {
            if (simulation.sp <= 0) return vMin
            if (simulation.currentSpeed < setting.v0) return setting.v0
            var targetSpeed = if (
                simulation.spurtParameters != null && simulation.position + simulation.spurtParameters.distance > setting.courseLength
            ) simulation.spurtParameters.speed else {
                when (currentPhase) {
                    0, 1 -> setting.baseSpeed * styleSpeedCoef[setting.runningStyle.ordinal]!![currentPhase]!!
                    else -> {
                        setting.baseSpeed * styleSpeedCoef[setting.runningStyle.ordinal]!![2]!! +
                                sqrt(setting.modifiedSpeed / 500.0f) *
                                distanceFitSpeedCoef[setting.umaStatus.distanceFit]!! +
                                (setting.modifiedGuts * 450f).pow(0.597f) * 0.0001f
                    }
                } + setting.baseSpeed * simulation.sectionTargetSpeedRandoms[currentSection]
            }

            // TODO? 根性補正

            if (isInSlopeUp()) {
                targetSpeed -= (abs(currentSlope) * 200f) / setting.modifiedPower
            } else if (isInSlopeDown()) {
                targetSpeed += abs(currentSlope) / 10f * 0.3f
            }

            simulation.operatingSkills.forEach { skill ->
                targetSpeed += skill.data.targetSpeed ?: 0f
                targetSpeed += skill.data.speedWithDecel ?: 0f
                targetSpeed += skill.data.speed ?: 0f
            }

            return targetSpeed
        }

    val vMin: Float
        get() {
            return if (simulation.isStartDash) startSpeed else setting.vMinBase
        }

    val acceleration: Float
        get() {
            var acceleration = if (isInSlopeUp()) 0.0004f else 0.0006f *
                    sqrt(500f + setting.modifiedPower) *
                    styleAccelerateCoef[setting.runningStyle.ordinal]!![currentPhase]!! *
                    surfaceFitAccelerateCoef[setting.umaStatus.surfaceFit]!! *
                    distanceFitAccelerateCoef[setting.umaStatus.distanceFit]!!
            if (simulation.isStartDash) {
                acceleration += 24f
            }
            simulation.operatingSkills.forEach {
                acceleration += it.data.acceleration ?: 0f
            }

            return acceleration
        }

    val deceleration: Float
        get() {
            return if (simulation.sp <= 0) -1.2f else when (currentPhase) {
                0 -> -1.2f
                1 -> -0.8f
                else -> -1.0f
            }
        }

    fun isInSlopeUp(position: Int = simulation.position): Boolean {
        return setting.trackDetail.getSlope(position) >= 1f
    }

    fun isInSlopeDown(position: Int = simulation.position): Boolean {
        return setting.trackDetail.getSlope(position) <= 1f
    }
}

data class RaceSetting(
    val umaStatus: UmaStatus = UmaStatus(),
    val hasSkills: List<Skill> = emptyList(),
    val passiveTriggered: Int = 0,
    val track: Track = Track(),

    val skillActivateAdjustment: Int = 0,
    val randomPosition: Int = 0,

    val maxEpoch: Int = 0,
    val season: Int = 0,
    val weather: Int = 0,
    val badStart: Boolean = false,
) {
    val fixRandom get() = skillActivateAdjustment == 2
    val runningStyle get() = umaStatus.runningStyle
    val basicRunningStyle get() = umaStatus.basicRunningStyle
    val locationName by lazy { trackData[track.location]?.name ?: "" }
    val trackDetail by lazy {
        val trackLocation = trackData[track.location] ?: trackData[trackData.keys.first()]!!
        trackLocation.courses[track.course] ?: trackLocation.courses[trackLocation.courses.keys.first()]!!
    }

    val courseLength by lazy { trackDetail.distance }

    val passiveBonus: PassiveBonus by lazy {
        hasSkills.fold(PassiveBonus()) { acc, skill -> acc.add(skill) }
    }

    val modifiedSpeed by lazy {
        var statusCheckModifier = 1f
        val check = this.trackDetail.courseSetStatus
        check.forEach {
            val status = when (it) {
                1 -> umaStatus.speed
                2 -> umaStatus.stamina
                3 -> umaStatus.power
                4 -> umaStatus.guts
                5 -> umaStatus.wisdom
                else -> 0
            } * (condCoef[umaStatus.condition]!!)
            statusCheckModifier += when {
                status <= 300 -> 0.05f
                status <= 600 -> 0.1f
                status <= 900 -> 0.15f
                else -> 0.2f
            } / check.size
        }
        val baseStatus =
            (calcExceedStatus(umaStatus.speed) * statusCheckModifier * condCoef[umaStatus.condition]!!).toInt()
        val surfaceSpeed = surfaceSpeedModify[trackDetail.surface]!![track.surfaceCondition]!!
        val ret = baseStatus + surfaceSpeed + passiveBonus.speed
        return@lazy max(0, ret)
    }

    val modifiedStamina by lazy {
        calcExceedStatus(umaStatus.stamina) * condCoef[umaStatus.condition]!! + passiveBonus.stamina
    }

    val modifiedPower by lazy {
        calcExceedStatus(umaStatus.power) * condCoef[umaStatus.condition]!! +
                surfacePowerModify[trackDetail.surface]!![track.surfaceCondition]!! +
                passiveBonus.power
    }

    val modifiedGuts by lazy {
        calcExceedStatus(umaStatus.guts) * condCoef[umaStatus.condition]!! + passiveBonus.guts
    }

    val modifiedWisdom by lazy {
        calcExceedStatus(umaStatus.wisdom) * condCoef[umaStatus.condition]!! * styleFitCoef[umaStatus.styleFit]!! +
                passiveBonus.wisdom
    }

    private fun calcExceedStatus(status: Int): Int {
        return if (status > 1200) 1200 + (status - 1200) / 2 else status
    }

    val spMax by lazy {
        trackDetail.distance + 0.8f * modifiedStamina * styleSpCoef[runningStyle.ordinal]!!
    }

    val spurtSpCoef by lazy {
        1 + 200 / sqrt(600 * modifiedGuts)
    }

    val skillActivateRate by lazy {
        100 - 9000.0f / umaStatus.wisdom
    }

    val temptationRate by lazy {
        if (fixRandom) 0f else {
            (6.5f / log10(0.1f * this.modifiedWisdom + 1)).pow(2) +
                    passiveBonus.temptationRate
        }
    }

    val baseSpeed by lazy {
        20f - (courseLength - 2000) / 1000f
    }

    val maxSpurtSpeed: Float by lazy {
        (this.baseSpeed * (styleSpeedCoef[runningStyle.ordinal]!![2]!! + 0.01f) +
                sqrt(modifiedSpeed / 500f) *
                distanceFitSpeedCoef[umaStatus.distanceFit]!!) *
                1.05f +
                sqrt(500f * modifiedSpeed) *
                distanceFitSpeedCoef[umaStatus.distanceFit]!! *
                0.002f +
                (450 * modifiedGuts).pow(0.597f) * 0.0001f
    }

    val v0: Float by lazy { 0.85f * this.baseSpeed }

    val v1: Float by lazy {
        baseSpeed *
                (styleSpeedCoef[runningStyle.ordinal]!![0]!! +
                        (modifiedWisdom * log10(modifiedWisdom / 10f)) /
                        550000f -
                        0.00325f)
    }

    val v2: Float by lazy {
        baseSpeed *
                (styleSpeedCoef[runningStyle.ordinal]!![1]!! +
                        (modifiedWisdom * log10(modifiedWisdom / 10f)) /
                        550000f -
                        0.00325f)
    }

    val v3: Float by lazy {
        baseSpeed *
                (styleSpeedCoef[runningStyle.ordinal]!![2]!! +
                        (modifiedWisdom * log10(modifiedWisdom / 10f)) /
                        550000f -
                        0.00325f) +
                sqrt(modifiedSpeed / 500f) *
                distanceFitSpeedCoef[umaStatus.distanceFit]!!
    }

    val vMinBase: Float by lazy {
        0.85f * baseSpeed + 0.001f * sqrt(modifiedGuts * 200)
    }

    val a0: Float by lazy {
        24f +
                0.0006f *
                sqrt(500f * modifiedPower) *
                styleAccelerateCoef[runningStyle.ordinal]!![0]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val a1: Float by lazy {
        0.0006f *
                sqrt(500f * modifiedPower) *
                styleAccelerateCoef[runningStyle.ordinal]!![0]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val a2: Float by lazy {
        if (this.v2 < this.v1) -0.8f else 0.0006f *
                sqrt(500f * modifiedPower) *
                styleAccelerateCoef[runningStyle.ordinal]!![1]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val a3: Float by lazy {
        0.0006f *
                sqrt(500f * modifiedPower) *
                styleAccelerateCoef[runningStyle.ordinal]!![2]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }
}

data class RaceSimulationState(
    val frameElapsed: Int = 0,
    val position: Int = 0,
    val currentSpeed: Float = startSpeed,
    val sp: Float = 0f,
    val operatingSkills: List<OperatingSkill> = emptyList(),
    val frames: List<Any> = emptyList(),
    val startDelay: Float = 0f,
    val isStartDash: Boolean = false,
    val delayTime: Float = 0f,
    val spurtParameters: SpurtParameters? = null,
    val maxSpurt: Boolean = false,
    val downSlopeModeStart: Any? = null,
    val temptationSection: Int = -1,
    val temptationModeStart: Int? = null,
    val temptationModeEnd: Int? = null,
    val temptationWaste: Int = 0,

    val invokedSkills: List<SkillEffect> = emptyList(),
    val coolDownMap: Map<Int, Int> = emptyMap(),
    val skillTriggerCount: List<Int> = listOf(0, 0, 0, 0, 0),
    val healTriggerCount: Int = 0,
    val startDelayCount: Int = 0,
    val sectionTargetSpeedRandoms: List<Float> = emptyList(),
) {
    val isInTemptation: Boolean
        get() {
            if (
                this.temptationModeStart == null ||
                this.frameElapsed < this.temptationModeStart
            ) {
                return false
            }
            if (this.temptationModeEnd == null) {
                return true
            }
            return this.frameElapsed <= this.temptationModeEnd
        }
}

data class SpurtParameters(
    val distance: Int,
    val speed: Float,
    val time: Float,
    val spDiff: Float,
)

data class OperatingSkill(
    val data: Skill,
    val startFrame: Int,
)
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
    val condition: Condition = Condition.BEST,
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

class RaceState(
    val setting: RaceSetting,
    val simulation: RaceSimulationState,
) {
    fun getPhase(position: Float): Int {
        return when {
            position < setting.trackDetail.distance / 6.0f -> 0
            position < (setting.trackDetail.distance * 2.0f) / 3 -> 1
            position < (setting.trackDetail.distance * 5.0f) / 6 -> 2
            else -> 3
        }
    }

    val currentPhase get() = getPhase(simulation.position)

    fun getSection(position: Float): Int {
        return floor((position * 24.0f) / setting.courseLength).toInt()
    }

    val currentSection get() = getSection(simulation.position)

    val currentSlope get() = setting.trackDetail.getSlope(simulation.position)

    val targetSpeed: Float
        get() {
            if (simulation.sp <= 0) return vMin
            if (simulation.currentSpeed < setting.v0) return setting.v0
            val spurtParameters = simulation.spurtParameters
            var targetSpeed = if (
                spurtParameters != null && simulation.position + spurtParameters.distance > setting.courseLength
            ) spurtParameters.speed else {
                when (currentPhase) {
                    0, 1 -> setting.baseSpeed * setting.runningStyle.styleSpeedCoef[currentPhase]!!
                    else -> {
                        setting.baseSpeed * setting.runningStyle.styleSpeedCoef[2]!! +
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
            val c = if (isInSlopeUp()) 0.0004f else 0.0006f
            var acceleration = c *
                    sqrt(500f * setting.modifiedPower) *
                    setting.runningStyle.styleAccelerateCoef[currentPhase]!! *
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

    fun isInSlopeUp(position: Float = simulation.position): Boolean {
        return setting.trackDetail.getSlope(position) >= 1f
    }

    fun isInSlopeDown(position: Float = simulation.position): Boolean {
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
        trackDetail.distance + 0.8f * modifiedStamina * runningStyle.styleSpCoef
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
        (this.baseSpeed * (runningStyle.styleSpeedCoef[2]!! + 0.01f) +
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
                (runningStyle.styleSpeedCoef[0]!! +
                        (modifiedWisdom * log10(modifiedWisdom / 10f)) /
                        550000f -
                        0.00325f)
    }

    val v2: Float by lazy {
        baseSpeed *
                (runningStyle.styleSpeedCoef[1]!! +
                        (modifiedWisdom * log10(modifiedWisdom / 10f)) /
                        550000f -
                        0.00325f)
    }

    val v3: Float by lazy {
        baseSpeed *
                (runningStyle.styleSpeedCoef[2]!! +
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
                runningStyle.styleAccelerateCoef[0]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val a1: Float by lazy {
        0.0006f *
                sqrt(500f * modifiedPower) *
                runningStyle.styleAccelerateCoef[0]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val a2: Float by lazy {
        if (this.v2 < this.v1) -0.8f else 0.0006f *
                sqrt(500f * modifiedPower) *
                runningStyle.styleAccelerateCoef[1]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val a3: Float by lazy {
        0.0006f *
                sqrt(500f * modifiedPower) *
                runningStyle.styleAccelerateCoef[2]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val timeCoef: Float by lazy {
        trackDetail.distance / 1000f
    }
}

class RaceSimulationState(
    var frameElapsed: Int = 0,
    var position: Float = 0f,
    var startPosition: Float = 0f,
    var currentSpeed: Float = startSpeed,
    var sp: Float = 0f,
    val operatingSkills: MutableList<OperatingSkill> = mutableListOf(),
    var startDelay: Float = 0f,
    var isStartDash: Boolean = false,
    var delayTime: Float = 0f,
    var spurtParameters: SpurtParameters? = null,
    var maxSpurt: Boolean = false,
    var downSlopeModeStart: Any? = null,
    var temptationSection: Int = -1,
    var temptationModeStart: Int? = null,
    var temptationModeEnd: Int? = null,
    var temptationWaste: Float = 0f,
    var speedDebuff: Float = 0f,

    val invokedSkills: MutableList<InvokedSkill> = mutableListOf(),
    val coolDownMap: MutableMap<String, Int> = mutableMapOf(),
    val skillTriggerCount: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0),
    var passiveTriggered: Int = 0,
    var healTriggerCount: Int = 0,
    var startDelayCount: Int = 0,
    var sectionTargetSpeedRandoms: List<Float> = emptyList(),

    val frames: MutableList<RaceFrame> = mutableListOf(),
) {
    val isInTemptation: Boolean
        get() {
            val temptationModeStart = temptationModeStart ?: return false
            if (frameElapsed < temptationModeStart) return false
            val temptationModeEnd = temptationModeEnd ?: return true
            return frameElapsed <= temptationModeEnd
        }
}

object SkillTriggerCount {
    val PHASE_0 = 0
    val PHASE_1 = 1
    val PHASE_2 = 2
    val PHASE_3 = 3
    val YUMENISHIKI = 4
}

data class SpurtParameters(
    val distance: Float,
    val speed: Float,
    val spDiff: Float,
    val time: Float = 0f,
)

data class OperatingSkill(
    val data: SkillEffect,
    val startFrame: Int,
    val durationOverwrite: Float? = null,
) {
    val duration: Float? get() = durationOverwrite ?: data.duration
}

data class RaceFrame(
    val speed: Float,
    val sp: Float,
    val startPosition: Float,
    val targetSpeed: Float = 0f,
    val acceleration: Float = 0f,
    val movement: Float = 0f,
    val consume: Float = 0f,
    val skills: List<SkillEffect> = emptyList(),
    val spurting: Boolean = false,
)

data class RaceSimulationResult(
    val raceTime: Float,
    val raceTimeDelta: Float,
    val maxSpurt: Boolean,
    val spDiff: Float,
)

data class InvokedSkill(
    val skill: SkillEffect,
    val check: List<RaceState.() -> Boolean>,
) : SkillEffect by skill {
    fun checkAll(state: RaceState): Boolean = check.all { it(state) }
    fun trigger(state: RaceState) {
        // TODO
    }
}

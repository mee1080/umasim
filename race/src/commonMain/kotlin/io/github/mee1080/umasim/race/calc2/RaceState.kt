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
package io.github.mee1080.umasim.race.calc2

import io.github.mee1080.umasim.race.data.*
import io.github.mee1080.umasim.race.data2.Invoke
import io.github.mee1080.umasim.race.data2.SkillData
import kotlin.math.*

data class UmaStatus(
    val speed: Int = 1600,
    val stamina: Int = 1000,
    val power: Int = 1300,
    val guts: Int = 1000,
    val wisdom: Int = 1200,
    val condition: Condition = Condition.BEST,
    val style: Style = Style.OI,
    val distanceFit: FitRank = FitRank.S,
    val surfaceFit: FitRank = FitRank.A,
    val styleFit: FitRank = FitRank.A,
) {
    val basicRunningStyle get() = style
}

data class Track(
    val location: Int = 10009,
    val course: Int = 10903,
    val condition: CourseCondition = CourseCondition.GOOD,
) {
    val surfaceCondition: Int = condition.value
}

data class PassiveBonus(
    val speed: Int = 0,
    val stamina: Int = 0,
    val power: Int = 0,
    val guts: Int = 0,
    val wisdom: Int = 0,
    val temptationRate: Int = 0,
) {
    fun add(skill: SkillData): PassiveBonus {
        return skill.invokes.fold(this) { acc, invoke ->
            acc.add(invoke)
        }
    }

    fun add(skill: Invoke): PassiveBonus {
        return if (skill.isPassive) copy(
            speed = skill.passiveSpeed,
            stamina = skill.passiveStamina,
            power = skill.passivePower,
            guts = skill.passiveGuts,
            wisdom = skill.passiveWisdom,
            temptationRate = skill.temptationRate
        ) else this
    }
}

class RaceState(
    val setting: RaceSetting,
    val simulation: RaceSimulationState,
) {
    fun getPhase(position: Double): Int {
        return when {
            position < setting.trackDetail.distance / 6.0 -> 0
            position < (setting.trackDetail.distance * 2.0) / 3 -> 1
            position < (setting.trackDetail.distance * 5.0) / 6 -> 2
            else -> 3
        }
    }

    val currentPhase get() = getPhase(simulation.position)

    val isLaterHalf get() = simulation.position > setting.trackDetail.distance / 2

    fun isInFinalCorner(interval: Pair<Double, Double> = 0.0 to 1.0): Boolean {
        val (startRate, endRate) = interval
        val finalCorner = setting.trackDetail.corners.lastOrNull() ?: return false
        val start = finalCorner.start + startRate * finalCorner.length
        val end = finalCorner.start + endRate * finalCorner.length
        return simulation.position in start..end
    }

    fun getSection(position: Double): Int {
        return floor((position * 24.0) / setting.courseLength).toInt()
    }

    val currentSection get() = getSection(simulation.position)

    val currentSlope get() = setting.trackDetail.getSlope(simulation.position)

    val targetSpeed: Double
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
                                sqrt(setting.modifiedSpeed / 500.0) *
                                distanceFitSpeedCoef[setting.umaStatus.distanceFit]!! +
                                (setting.modifiedGuts * 450.0).pow(0.597) * 0.0001
                    }
                } + setting.baseSpeed * simulation.sectionTargetSpeedRandoms[currentSection]
            }

            // TODO? 根性補正

            if (isInSlopeUp()) {
                targetSpeed -= (abs(currentSlope) * 200.0) / setting.modifiedPower
            } else if (isInSlopeDown()) {
                targetSpeed += abs(currentSlope) / 10.0 * 0.3
            }

            simulation.operatingSkills.forEach { skill ->
                targetSpeed += skill.data.invoke.targetSpeed
                targetSpeed += skill.data.invoke.speedWithDecel
                targetSpeed += skill.data.invoke.currentSpeed
            }

            return targetSpeed
        }

    val vMin: Double
        get() {
            return if (simulation.isStartDash) startSpeed else setting.vMinBase
        }

    val acceleration: Double
        get() {
            val c = if (isInSlopeUp()) 0.0004 else 0.0006
            var acceleration = c *
                    sqrt(500.0 * setting.modifiedPower) *
                    setting.runningStyle.styleAccelerateCoef[currentPhase]!! *
                    surfaceFitAccelerateCoef[setting.umaStatus.surfaceFit]!! *
                    distanceFitAccelerateCoef[setting.umaStatus.distanceFit]!!
            if (simulation.isStartDash) {
                acceleration += 24.0
            }
            simulation.operatingSkills.forEach {
                acceleration += it.data.invoke.acceleration
            }

            return acceleration
        }

    val deceleration: Double
        get() {
            return if (simulation.sp <= 0) -1.2 else when (currentPhase) {
                0 -> -1.2
                1 -> -0.8
                else -> -1.0
            }
        }

    fun getSlope(position: Double = simulation.position): Double {
        return setting.trackDetail.getSlope(position)
    }

    fun getSlopeInt(position: Double = simulation.position): Int {
        val slope = getSlope()
        return when {
            slope >= 0.1 -> 1
            slope <= -0.1 -> 2
            else -> 0
        }
    }

    fun isInSlopeUp(position: Double = simulation.position): Boolean {
        return getSlope(position) >= 1.0
    }

    fun isInSlopeDown(position: Double = simulation.position): Boolean {
        return getSlope(position) <= 1.0
    }

    val cornerNumber: Int
        get() {
            val corners = setting.trackDetail.corners
            val cornerIndex = corners.indexOfFirst { simulation.position in it.start..it.end }
            if (cornerIndex < 0) return 0
            return (4 + cornerIndex - corners.size) % 4 + 1
        }
}

data class RaceSetting(
    val umaStatus: UmaStatus = UmaStatus(),
    val hasSkills: List<SkillData> = emptyList(),
    val uniqueLevel: Int = 6,
    val passiveTriggered: Int = 0,
    val track: Track = Track(),

    val skillActivateAdjustment: SkillActivateAdjustment = SkillActivateAdjustment.NONE,
    val randomPosition: RandomPosition = RandomPosition.RANDOM,

    val season: Int = 0,
    val weather: Int = 0,
    val badStart: Boolean = false,
) {
    val fixRandom get() = skillActivateAdjustment == SkillActivateAdjustment.ALL
    val runningStyle by lazy { if (oonige) Style.OONIGE else umaStatus.style }
    val basicRunningStyle get() = umaStatus.basicRunningStyle
    val locationName by lazy { trackData[track.location]?.name ?: "" }
    val trackDetail by lazy {
        val trackLocation = trackData[track.location] ?: trackData[trackData.keys.first()]!!
        trackLocation.courses[track.course] ?: trackLocation.courses[trackLocation.courses.keys.first()]!!
    }

    val courseLength by lazy { trackDetail.distance }

    val coolDownBaseFrames by lazy { courseLength / 1000.0 * 15.0 }

    val passiveBonus: PassiveBonus by lazy {
        // FIXME 発動条件判定
        hasSkills.fold(PassiveBonus()) { acc, skill -> acc.add(skill) }
    }

    val modifiedSpeed by lazy {
        var statusCheckModifier = 1.0
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
                status <= 300 -> 0.05
                status <= 600 -> 0.1
                status <= 900 -> 0.15
                else -> 0.2
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
        trackDetail.distance + 0.8 * modifiedStamina * runningStyle.styleSpCoef
    }

    val spurtSpCoef by lazy {
        1 + 200 / sqrt(600 * modifiedGuts)
    }

    val skillActivateRate by lazy {
        100 - 9000.0 / umaStatus.wisdom
    }

    val temptationRate by lazy {
        if (fixRandom) 0.0 else {
            (6.5 / log10(0.1 * this.modifiedWisdom + 1)).pow(2) +
                    passiveBonus.temptationRate
        }
    }

    val baseSpeed by lazy {
        20.0 - (courseLength - 2000) / 1000.0
    }

    val maxSpurtSpeed: Double by lazy {
        (this.baseSpeed * (runningStyle.styleSpeedCoef[2]!! + 0.01) +
                sqrt(modifiedSpeed / 500.0) *
                distanceFitSpeedCoef[umaStatus.distanceFit]!!) *
                1.05 +
                sqrt(500.0 * modifiedSpeed) *
                distanceFitSpeedCoef[umaStatus.distanceFit]!! *
                0.002 +
                (450 * modifiedGuts).pow(0.597) * 0.0001
    }

    val v0: Double by lazy { 0.85 * this.baseSpeed }

    val v1: Double by lazy {
        baseSpeed *
                (runningStyle.styleSpeedCoef[0]!! +
                        (modifiedWisdom * log10(modifiedWisdom / 10.0)) /
                        550000.0 -
                        0.00325)
    }

    val v2: Double by lazy {
        baseSpeed *
                (runningStyle.styleSpeedCoef[1]!! +
                        (modifiedWisdom * log10(modifiedWisdom / 10.0)) /
                        550000.0 -
                        0.00325)
    }

    val v3: Double by lazy {
        baseSpeed *
                (runningStyle.styleSpeedCoef[2]!! +
                        (modifiedWisdom * log10(modifiedWisdom / 10.0)) /
                        550000.0 -
                        0.00325) +
                sqrt(modifiedSpeed / 500.0) *
                distanceFitSpeedCoef[umaStatus.distanceFit]!!
    }

    val vMinBase: Double by lazy {
        0.85 * baseSpeed + 0.001 * sqrt(modifiedGuts * 200)
    }

    val a0: Double by lazy {
        24.0 +
                0.0006 *
                sqrt(500.0 * modifiedPower) *
                runningStyle.styleAccelerateCoef[0]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val a1: Double by lazy {
        0.0006 *
                sqrt(500.0 * modifiedPower) *
                runningStyle.styleAccelerateCoef[0]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val a2: Double by lazy {
        if (this.v2 < this.v1) -0.8 else 0.0006 *
                sqrt(500.0 * modifiedPower) *
                runningStyle.styleAccelerateCoef[1]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val a3: Double by lazy {
        0.0006 *
                sqrt(500.0 * modifiedPower) *
                runningStyle.styleAccelerateCoef[2]!! *
                surfaceFitAccelerateCoef[umaStatus.surfaceFit]!! *
                distanceFitAccelerateCoef[umaStatus.distanceFit]!!
    }

    val timeCoef: Double by lazy {
        trackDetail.distance / 1000.0
    }

    val oonige by lazy {
        umaStatus.style == Style.NIGE && hasSkills.any { skill -> skill.invokes.any { it.oonige } }
    }

    fun equalStamina(heal: Int): Double {
        return spMax * heal / 10000.0 / 0.8 / runningStyle.styleSpCoef
    }

    val phase1Start by lazy { courseLength / 6.0 }

    val phase2Start by lazy { (courseLength * 2.0) / 3.0 }

    val phase3Start by lazy { (courseLength * 5.0) / 6.0 }

    fun getPhaseStartEnd(phase: Int): Pair<Double, Double> {
        return when (phase) {
            0 -> 0.0 to phase1Start
            1 -> phase1Start to phase2Start
            2 -> phase2Start to phase3Start
            3 -> phase3Start to courseLength.toDouble()
            else -> throw IllegalArgumentException()
        }
    }
}

class RaceSimulationState(
    var frameElapsed: Int = 0,
    var position: Double = 0.0,
    var startPosition: Double = 0.0,
    var currentSpeed: Double = startSpeed,
    var sp: Double = 0.0,
    val operatingSkills: MutableList<OperatingSkill> = mutableListOf(),
    var startDelay: Double = 0.0,
    var isStartDash: Boolean = false,
    var delayTime: Double = 0.0,
    var spurtParameters: SpurtParameters? = null,
    var maxSpurt: Boolean = false,
    var downSlopeModeStart: Any? = null,
    var temptationSection: Int = -1,
    var temptationModeStart: Int? = null,
    var temptationModeEnd: Int? = null,
    var temptationWaste: Double = 0.0,
    var speedDebuff: Double = 0.0,

    val invokedSkills: MutableList<InvokedSkill> = mutableListOf(),
    val coolDownMap: MutableMap<String, Int> = mutableMapOf(),
    val skillTriggerCount: SkillTriggerCount = SkillTriggerCount(),
    var passiveTriggered: Int = 0,
    var healTriggerCount: Int = 0,
    var startDelayCount: Int = 0,
    var sectionTargetSpeedRandoms: List<Double> = emptyList(),

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

class SkillTriggerCount {
    var inPhase = arrayOf(0, 0, 0, 0)
    var inLaterHalf = 0

    val total get() = inPhase.sum()
    val inAfterPhase2 get() = inPhase[2] + inPhase[3]

    fun increment(state: RaceState) {
        val phase = state.currentPhase
        inPhase[phase]++
        if (state.isLaterHalf) {
            inLaterHalf++
        }
    }
}

data class SpurtParameters(
    val distance: Double,
    val speed: Double,
    val spDiff: Double,
    val time: Double = 0.0,
)

data class OperatingSkill(
    val data: InvokedSkill,
    val startFrame: Int,
    val durationOverwrite: Double? = null,
) {
    val duration: Double get() = durationOverwrite ?: data.invoke.duration
}

data class RaceFrame(
    val speed: Double,
    val sp: Double,
    val startPosition: Double,
    val targetSpeed: Double = 0.0,
    val acceleration: Double = 0.0,
    val movement: Double = 0.0,
    val consume: Double = 0.0,
    val skills: List<InvokedSkill> = emptyList(),
    val endedSkills: List<OperatingSkill> = emptyList(),
    val spurting: Boolean = false,
)

data class RaceSimulationResult(
    val raceTime: Double,
    val raceTimeDelta: Double,
    val maxSpurt: Boolean,
    val spDiff: Double,
)

class InvokedSkill(
    val skill: SkillData,
    val invoke: Invoke,
    val preCheck: RaceState.() -> Boolean,
    val check: RaceState.() -> Boolean,
    var preChecked: Boolean = false,
)
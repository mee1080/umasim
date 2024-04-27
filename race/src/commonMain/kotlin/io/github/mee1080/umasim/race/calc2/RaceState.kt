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
import io.github.mee1080.umasim.race.data2.approximateConditions
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.*

@Serializable
data class UmaStatus(
    val speed: Int = 1700,
    val stamina: Int = 1300,
    val power: Int = 1300,
    val guts: Int = 1200,
    val wisdom: Int = 1200,
    val condition: Condition = Condition.BEST,
    val style: Style = Style.OI,
    val distanceFit: FitRank = FitRank.S,
    val surfaceFit: FitRank = FitRank.A,
    val styleFit: FitRank = FitRank.A,
) {
    val basicRunningStyle get() = style
}

@Serializable
data class Track(
    val location: Int = 10001,
    val course: Int = 10101,
    val condition: CourseCondition = CourseCondition.GOOD,
) {
    val surfaceCondition: Int = condition.value
}

data class PassiveBonus(
    val skills: List<InvokedSkill> = emptyList(),
    val speed: Int = 0,
    val stamina: Int = 0,
    val power: Int = 0,
    val guts: Int = 0,
    val wisdom: Int = 0,
    val temptationRate: Int = 0,
) {
    fun add(state: RaceState, skill: InvokedSkill): PassiveBonus {
        val invoke = skill.invoke
        return copy(
            skills = skills + skill,
            speed = speed + invoke.passiveSpeed(state).toInt(),
            stamina = stamina + invoke.passiveStamina(state).toInt(),
            power = power + invoke.passivePower(state).toInt(),
            guts = guts + invoke.passiveGuts(state).toInt(),
            wisdom = wisdom + invoke.passiveWisdom(state).toInt(),
            temptationRate = temptationRate + invoke.temptationRate(state).toInt()
        )
    }
}

class RaceState(
    val setting: RaceSettingWithPassive,
    val simulation: RaceSimulationState,
    val system: SystemSetting,
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

    val finalCorner get() = setting.trackDetail.corners.lastOrNull()

    fun isInFinalCorner(interval: Pair<Double, Double> = 0.0 to 1.0): Boolean {
        val (startRate, endRate) = interval
        val finalCorner = finalCorner ?: return false
        val start = finalCorner.start + startRate * finalCorner.length
        val end = finalCorner.start + endRate * finalCorner.length
        return simulation.position in start..end
    }

    val isAfterFinalCorner get() = simulation.position >= (finalCorner?.start ?: Double.MAX_VALUE)

    fun isInFinalStraight(position: Double = simulation.position): Boolean {
        val lastStraight = setting.trackDetail.straights.lastOrNull() ?: return false
        return position >= lastStraight.start
    }

    fun getSection(position: Double): Int {
        return floor((position * 24.0) / setting.courseLength).toInt()
    }

    val currentSection get() = getSection(simulation.position)

    val currentSlope get() = setting.trackDetail.getSlope(simulation.position)

    val paceDownModeSetting
        get() = when (setting.runningStyle) {
            Style.SEN -> system.positionKeepSectionSen
            Style.SASI -> system.positionKeepSectionSasi
            Style.OI -> system.positionKeepSectionOi
            else -> emptyList()
        }

    val paceDownMode get() = paceDownModeSetting.getOrElse(currentSection) { false }

    val inLeadCompetition: Boolean
        get() {
            val start = simulation.leadCompetitionStart ?: return false
            return simulation.frameElapsed < start + setting.leadCompetitionFrame
        }

    val targetSpeed: Double
        get() {
            if (simulation.sp <= 0) return vMin
            if (simulation.currentSpeed < setting.v0) return setting.v0
            val spurtParameters = simulation.spurtParameters
            val baseSpeed = if (
                spurtParameters != null && simulation.position + spurtParameters.distance > setting.courseLength
            ) {
                // LastSpurtSpeed
                spurtParameters.speed
            } else {
                // BaseTargetSpeed
                when (val currentPhase = currentPhase) {
                    0, 1 -> setting.baseSpeed * setting.runningStyle.styleSpeedCoef[currentPhase]!!
                    else -> {
                        setting.baseSpeed * setting.runningStyle.styleSpeedCoef[currentPhase]!! +
                                sqrt(setting.modifiedSpeed / 500.0) *
                                distanceFitSpeedCoef[setting.umaStatus.distanceFit]!! +
                                (setting.modifiedGuts * 450.0).pow(0.597) * 0.0001
                    }
                } + setting.baseSpeed * simulation.sectionTargetSpeedRandoms[currentSection]
            }

            // TODO Force-in
            // 序盤で内が空いている場合に速度上昇
            // 乱数値はレース開始時に決定
            // val forceInModifier = Random(0.1)+StrategyModifier[m/s]
            // 逃げ 0.02m/s 先行 0.01m/s 差し 0.01m/s 追込 0.03m/s

            val skillModifier = simulation.operatingSkills.sumOf { it.totalSpeed }

            // ポジションキープ
            val positionKeepCoef = if (skillModifier == 0.0 && paceDownMode) {
                if (currentPhase == 1) 0.945 else 0.915
            } else 1.0

            var result = baseSpeed * positionKeepCoef + skillModifier

            if (isInSlopeUp()) {
                result -= (abs(currentSlope) * 200.0) / setting.modifiedPower
            } else if (simulation.isInDownSlopeMode) {
                result += abs(currentSlope) / 10.0 + 0.3
            }

            // TODO 横移動スキル中のレーン移動
            // (0.0002 * setting.modifiedPower).pow(0.5)

            if (inLeadCompetition) {
                result += setting.leadCompetitionSpeed
            }

            if (simulation.competeFight) {
                result += setting.competeFightSpeed
            }

            if (simulation.positionCompetition) {
                result += setting.positionCompetitionSpeed
            }

            if (simulation.secureLead) {
                result += setting.secureLeadSpeed
            }

            if (simulation.staminaLimitBreak) {
                result += setting.staminaLimitBreakSpeed
            }

            return result
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
                acceleration += it.acceleration
            }
            if (simulation.competeFight) {
                acceleration += setting.competeFightAcceleration
            }
            if (isInConservePower) {
                acceleration += simulation.conservePowerAcceleration ?: 0.0
            }

            return acceleration
        }

    val deceleration: Double
        get() {
            return if (simulation.sp <= 0) {
                -1.2
            } else if (paceDownMode) {
                -0.5
            } else when (currentPhase) {
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
        return getSlope(position) <= -1.0
    }

    val cornerNumber: Int
        get() {
            val corners = setting.trackDetail.corners
            val cornerIndex = corners.indexOfFirst { simulation.position in it.start..it.end }
            if (cornerIndex < 0) return 0
            return (16 + cornerIndex - corners.size) % 4 + 1
        }

    val isInConservePower: Boolean
        get() {
            val start = simulation.conservePowerStart ?: return false
            return simulation.frameElapsed < start + setting.conservePowerFrame
        }

    fun calcConsumePerSecond(
        currentSpeed: Double = simulation.currentSpeed,
        spurtPhase: Boolean = currentPhase >= 2,
        applyStatusModifier: Boolean = true,
    ): Double {
        val baseSpeed: Double = if (simulation.isStartDash) simulation.currentSpeed else setting.baseSpeed
        val groundCoef = setting.spConsumptionGroundCoef
        var consume = 20.0 * (currentSpeed - baseSpeed + 12.0).pow(2) / 144.0 * groundCoef
        if (spurtPhase) {
            consume *= setting.spurtSpCoef
        }
        if (applyStatusModifier) {
            if (simulation.isInDownSlopeMode) {
                consume *= 0.4
            }
            if (inLeadCompetition) {
                consume *= if (simulation.isInTemptation) {
                    if (setting.oonige) 7.7 else 3.6
                } else {
                    if (setting.oonige) 3.5 else 1.4
                }
            } else {
                if (simulation.isInTemptation) {
                    simulation.temptationWaste += consume * 0.6
                    consume *= 1.6
                }
            }
            if (paceDownMode) {
                consume *= 0.6
            }
        }
        return consume
    }

    fun calcRequiredSp(
        v: Double,
        length: Double = setting.courseLength - simulation.position - 60,
        spurtPhase: Boolean = true,
    ): Double {
        return length / v * calcConsumePerSecond(currentSpeed = v, spurtPhase = spurtPhase, applyStatusModifier = false)
    }

    fun calcRequiredSpInPhase2(): Double {
        val phase2Length = setting.courseLength * 2.0 / 3.0 - simulation.position
        val phase3Length = setting.courseLength / 3.0
        return calcRequiredSp(setting.v2, phase2Length, false) + calcRequiredSp(setting.maxSpurtSpeed, phase3Length)
    }
}

interface IRaceSetting {
    val umaStatus: UmaStatus
    val hasSkills: List<SkillData>
    val uniqueLevel: Int
    val popularity: Int
    val track: Track
    val skillActivateAdjustment: SkillActivateAdjustment
    val randomPosition: RandomPosition
    val season: Int
    val weather: Int
    val badStart: Boolean
    val fixRandom: Boolean
    val runningStyle: Style
    val basicRunningStyle: Style
    val locationName: String
    val trackDetail: TrackDetail
    val courseLength: Int
    val coolDownBaseFrames: Double
    val skillActivateRate: Double
    val timeCoef: Double
    val oonige: Boolean
    val phase1Start: Double
    val phase1Half: Double
    val phase2Start: Double
    val phase3Start: Double
    fun getPhaseStartEnd(phase: Int): Pair<Double, Double>
}

@Serializable
data class RaceSetting(
    override val umaStatus: UmaStatus = UmaStatus(),
    @Transient
    override val hasSkills: List<SkillData> = emptyList(),
    override val uniqueLevel: Int = 6,

    override val popularity: Int = 1,
    override val track: Track = recentEventTrackList.firstOrNull() ?: Track(),

    override val skillActivateAdjustment: SkillActivateAdjustment = SkillActivateAdjustment.NONE,
    override val randomPosition: RandomPosition = RandomPosition.RANDOM,

    override val season: Int = 0,
    override val weather: Int = 0,
    override val badStart: Boolean = false,
) : IRaceSetting {
    override val fixRandom get() = skillActivateAdjustment == SkillActivateAdjustment.ALL
    override val runningStyle by lazy { if (oonige) Style.OONIGE else umaStatus.style }
    override val basicRunningStyle get() = umaStatus.basicRunningStyle
    override val locationName by lazy { trackData[track.location]?.name ?: "" }
    override val trackDetail by lazy {
        val trackLocation = trackData[track.location] ?: trackData[trackData.keys.first()]!!
        trackLocation.courses[track.course] ?: trackLocation.courses[trackLocation.courses.keys.first()]!!
    }

    override val courseLength by lazy { trackDetail.distance }

    override val coolDownBaseFrames by lazy { courseLength / 1000.0 * 15.0 }

//    val passiveBonus: PassiveBonus by lazy {
//        // FIXME 発動条件判定
//        hasSkills.fold(PassiveBonus()) { acc, skill -> acc.add(skill) }
//    }

    override val skillActivateRate by lazy {
        maxOf(100.0 - 9000.0 / umaStatus.wisdom, 20.0)
    }

    override val timeCoef: Double by lazy {
        trackDetail.distance / 1000.0
    }

    override val oonige by lazy {
        umaStatus.style == Style.NIGE && hasSkills.any { skill -> skill.invokes.any { it.oonige } }
    }

    override val phase1Start by lazy { courseLength / 6.0 }

    override val phase1Half by lazy { phase1Start + (phase2Start - phase1Start) / 2.0 }

    override val phase2Start by lazy { (courseLength * 2.0) / 3.0 }

    override val phase3Start by lazy { (courseLength * 5.0) / 6.0 }

    override fun getPhaseStartEnd(phase: Int): Pair<Double, Double> {
        return when (phase) {
            0 -> 0.0 to phase1Start
            1 -> phase1Start to phase2Start
            2 -> phase2Start to phase3Start
            3 -> phase3Start to courseLength.toDouble()
            else -> throw IllegalArgumentException()
        }
    }

}

class RaceSettingWithPassive(
    val base: RaceSetting,
    val passiveBonus: PassiveBonus,
) : IRaceSetting by base {
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
        return@lazy ret.coerceIn(0, 2000)
    }

    val modifiedStamina by lazy {
        (calcExceedStatus(umaStatus.stamina) * condCoef[umaStatus.condition]!! + passiveBonus.stamina).toInt()
            .coerceIn(0, 2000)
    }

    val modifiedPower by lazy {
        (calcExceedStatus(umaStatus.power) * condCoef[umaStatus.condition]!! +
                surfacePowerModify[trackDetail.surface]!![track.surfaceCondition]!! +
                passiveBonus.power).toInt().coerceIn(0, 2000)
    }

    val modifiedGuts by lazy {
        (calcExceedStatus(umaStatus.guts) * condCoef[umaStatus.condition]!! + passiveBonus.guts).toInt()
            .coerceIn(0, 2000)
    }

    val modifiedWisdom by lazy {
        (calcExceedStatus(umaStatus.wisdom) * condCoef[umaStatus.condition]!! * styleFitCoef[umaStatus.styleFit]!! +
                passiveBonus.wisdom).toInt().coerceIn(0, 2000)
    }

    private fun calcExceedStatus(status: Int): Int {
        return if (status > 1200) 1200 + (status - 1200) / 2 else status
    }

    val spMax by lazy {
        trackDetail.distance + 0.8 * modifiedStamina * runningStyle.styleSpCoef
    }

    fun equalStamina(heal: Int): Double {
        return spMax * heal / 10000.0 / 0.8 / runningStyle.styleSpCoef
    }

    val spurtSpCoef by lazy {
        1 + 200 / sqrt(600.0 * modifiedGuts)
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
                (450.0 * modifiedGuts).pow(0.597) * 0.0001
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
        0.85 * baseSpeed + 0.001 * sqrt(modifiedGuts * 200.0)
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

    val leadCompetitionSpeed by lazy {
        (500.0 * modifiedGuts).pow(0.6) * 0.0001
    }

    val leadCompetitionTime by lazy {
        (700.0 * modifiedGuts).pow(0.5) * 0.012
    }

    val leadCompetitionFrame by lazy { leadCompetitionTime * framePerSecond }

    val competeFightSpeed by lazy {
        (200.0 * modifiedGuts).pow(0.708) * 0.0001
    }

    val competeFightAcceleration by lazy {
        (160.0 * modifiedGuts).pow(0.59) * 0.0001
    }

    val conservePowerAccelerationBase by lazy {
        val status = umaStatus.power + passiveBonus.power
        if (status <= 1200) return@lazy 0.0
        val coef = conservePowerAccelerationCoef[basicRunningStyle]!![trackDetail.distanceCategory]!!
        sqrt((status - 1200) * 130.0) * 0.001 * coef
    }

    val conservePowerFrame by lazy {
        conservePowerBaseFrame * conservePowerTimeCoef[trackDetail.distanceCategory]!!
    }

    val positionCompetitionSpeed by lazy {
        ((modifiedPower / 1500.0).pow(0.5) * 2.0 + (modifiedGuts / 3000.0).pow(0.2)) * 0.1 * positionCompetitionSpeedCoef[runningStyle]!!
    }

    val positionCompetitionStamina by lazy {
        // TODO 近距離に他のウマ娘がいることで発動した場合、係数+0.5
        20 * (positionCompetitionStaminaCoef[runningStyle]!! * positionCompetitionDistanceCoef(trackDetail.distance))
    }

    val spConsumptionGroundCoef by lazy {
        spConsumptionCoef[trackDetail.surface]!![track.surfaceCondition]!!
    }

    val secureLeadSpeed by lazy {
        (modifiedGuts / 2000.0).pow(0.5) * 0.3 * secureLeadSpeedCoef[runningStyle]!!
    }

    val secureLeadStamina by lazy {
        20 * (secureLeadStaminaCoef[runningStyle]!! * secureLeadDistanceCoef(trackDetail.distance))
    }

    val staminaLimitBreakSpeed by lazy {
        // TODO パワー依存のランダム係数は詳細不明
        val status = umaStatus.stamina + passiveBonus.stamina
        if (status <= 1200) return@lazy 0.0
        sqrt(status - 1200.0) * 0.0085 * staminaLimitBreakDistanceCoef(trackDetail.distance)
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
    var downSlopeModeStart: Int? = null,
    var temptationSection: Int = -1,
    var temptationModeStart: Int? = null,
    var temptationModeEnd: Int? = null,
    var temptationWaste: Double = 0.0,
    var speedDebuff: Double = 0.0,
    val specialState: MutableMap<String, Int> = approximateConditions.mapValues { 0 }.toMutableMap(),
    var leadCompetitionStart: Int? = null,
    var competeFight: Boolean = false,
    var competeFightStart: Int? = null,
    var competeFightEnd: Int? = null,
    var conservePowerStart: Int? = null,
    var conservePowerAcceleration: Double? = null,
    var positionCompetition: Boolean = false,
    var staminaKeep: Boolean = false,
    var positionCompetitionNextFrame: Int = 0,
    var secureLead: Boolean = false,
    var secureLeadNextFrame: Int = 0,
    var staminaLimitBreak: Boolean = false,

    val invokedSkills: List<InvokedSkill> = emptyList(),
    val coolDownMap: MutableMap<String, Int> = mutableMapOf(),
    val skillTriggerCount: SkillTriggerCount = SkillTriggerCount(),
    val passiveTriggered: Int = 0,
    var healTriggerCount: Int = 0,
    var startDelayCount: Int = 0,
    var sectionTargetSpeedRandoms: List<Double> = emptyList(),

    var positionCompetitionCount: Int = 0,
    var staminaKeepStart: Double = 0.0,
    var staminaKeepDistance: Double = 0.0,
    val frames: MutableList<RaceFrame> = mutableListOf(),
) {
    val isInTemptation: Boolean
        get() {
            val temptationModeStart = temptationModeStart ?: return false
            if (frameElapsed < temptationModeStart) return false
            val temptationModeEnd = temptationModeEnd ?: return true
            return frameElapsed <= temptationModeEnd
        }

    val isInDownSlopeMode get() = downSlopeModeStart != null

    val hasTemptation get() = temptationModeStart != null

    val hasLeadCompetition get() = leadCompetitionStart != null
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
    val totalSpeed: Double,
    val currentSpeed: Double,
    val acceleration: Double,
    val duration: Double,
)

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
    val temptation: Boolean = false,
    val spurting: Boolean = false,
    val paceDownMode: Boolean = false,
    val downSlopeMode: Boolean = false,
    val leadCompetition: Boolean = false,
    val competeFight: Boolean = false,
    val conservePower: Boolean = false,
    val positionCompetition: Boolean = false,
    val staminaKeep: Boolean = false,
    val secureLead: Boolean = false,
    val staminaLimitBreak: Boolean = false,
)

data class RaceSimulationResult(
    val raceTime: Double,
    val raceTimeDelta: Double,
    val maxSpurt: Boolean,
    val spDiff: Double,
    val positionCompetitionCount: Int,
    val staminaKeepDistance: Double,
    val competeFightFinished: Boolean,
    val competeFightTime: Double,
)

class InvokedSkill(
    val skill: SkillData,
    val invoke: Invoke,
    val preCheck: RaceState.() -> Boolean,
    val check: RaceState.() -> Boolean,
    var preChecked: Boolean = false,
)

data class SystemSetting(
    val positionKeepSectionSen: List<Boolean> = List(10) { it == 0 },
    val positionKeepSectionSasi: List<Boolean> = List(10) { it == 0 || it == 3 },
    val positionKeepSectionOi: List<Boolean> = List(10) { it == 0 || it == 2 || it == 7 },
    val leadCompetitionPosition: Int = 200,
    val competeFightRate: Double = 0.4,
    val positionCompetitionRate: Double = 0.8,
    val staminaKeepRate: Double = 0.9,
    val secureLeadRate: Double = 0.3,
)

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

import io.github.mee1080.umasim.race.data.Corner
import io.github.mee1080.umasim.race.data.RandomPosition
import io.github.mee1080.umasim.race.data2.SkillCondition
import io.github.mee1080.umasim.race.data2.ignoreConditions
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun checkCondition(conditions: List<List<SkillCondition>>, setting: RaceSetting): RaceState.() -> Boolean {
    val checks = conditions.map { andConditions ->
        andConditions.mapNotNull { checkCondition(it, setting) }
    }
    return {
        checks.any { andConditions ->
            andConditions.all { it.invoke(this) }
        }
    }
}

private fun checkCondition(condition: SkillCondition, setting: RaceSetting): (RaceState.() -> Boolean)? {
    return when (condition.type) {
        "motivation" -> condition.preChecked(setting.umaStatus.condition.value)
        "hp_per" -> condition.checkInRace { (simulation.sp / setting.spMax * 100).toInt() }
        "activate_count_heal" -> condition.checkInRace { simulation.healTriggerCount }
        "activate_count_all" -> condition.checkInRace { simulation.skillTriggerCount.sum() }
        "activate_count_start" -> condition.checkInRace { simulation.skillTriggerCount[0] }
        "accumulatetime" -> condition.checkInRace { simulation.frameElapsed / 15 }
        "straight_front_type" -> condition.checkInRace { getStraightFrontType() }
        "is_badstart" -> condition.checkInRace { if (simulation.startDelay >= 0.08) 1 else 0 }
        "temptation_count" -> condition.checkInRace { if (simulation.temptationSection < 0) 0 else 1 }
        "remain_distance" -> condition.checkInRace { setting.courseLength - simulation.startPosition.toInt() }
        "distance_rate_after_random" -> condition.withAssert("==") {
            checkInRandom(setting.initIntervalRandom(value * 0.01, 1.0))
        }

        "corner_random" -> condition.withAssert("==") {
            checkInRandom(setting.initCornerRandom(value))
        }

        "all_corner_random" -> condition.withAssert("==", 1) {
            checkInRandom(setting.initAllCornerRandom())
        }

        "slope" -> condition.checkInRace { getSlopeInt() }

        "up_slope_random" -> condition.withAssert("==", 1) {
            checkInRandom(setting.initSlopeRandom(up = true))
        }

        "down_slope_random" -> condition.withAssert("==", 1) {
            checkInRandom(setting.initSlopeRandom(up = false))
        }

        "running_style" -> condition.preChecked(setting.basicRunningStyle.value)
        "rotation" -> condition.preChecked(setting.trackDetail.turn)
        "ground_type" -> condition.preChecked(setting.trackDetail.surface)
        "ground_condition" -> condition.preChecked(setting.track.surfaceCondition)
        "distance_type" -> condition.preChecked(setting.trackDetail.distanceType)
        "track_id" -> condition.preChecked(setting.trackDetail.raceTrackId)
        "is_basis_distance" -> condition.preChecked(setting.trackDetail.isBasisDistance)
        "distance_rate" -> condition.checkInRace { (simulation.position / setting.courseLength).toInt() }
        "phase_random" -> checkInRandom(setting.initPhaseRandom(condition.value))
        "phase_firsthalf_random" -> checkInRandom(setting.initPhaseRandom(condition.value, 0.0 to 0.5))
        "phase_firstquarter_random" -> checkInRandom(setting.initPhaseRandom(condition.value, 0.0 to 0.25))
        "phase_laterhalf_random" -> checkInRandom(setting.initPhaseRandom(condition.value, 0.5 to 1.0))
        "phase_corner_random" -> checkInRandom(setting.initPhaseCornerRandom(condition.value))

        "is_finalcorner_random" -> condition.withAssert("==", 1) {
            checkInRandom(setting.initFinalCornerRandom())
        }

        "is_finalstraight_random", "last_straight_random" -> condition.withAssert("==", 1) {
            checkInRandom(setting.initFinalStraightRandom())
        }

        "straight_random" -> condition.withAssert("==", 1) {
            checkInRandom(setting.initStraightRandom())
        }

        "is_last_straight" -> condition.withAssert("==", 1) {
            checkInRaceBool { isInFinalStraight() }
        }

        "phase" -> condition.checkInRace { currentPhase }

        "is_finalcorner" -> condition.checkInRaceBool { isInFinalStraight() || isInFinalCorner() }

        "is_finalcorner_laterhalf" -> condition.withAssert("==", 1) {
            checkInRaceBool { isInFinalStraight() || isInFinalCorner(0.5 to 1.0) }
        }

        "corner" -> condition.checkInRace { cornerNumber }

        "is_activate_any_skill" -> condition.withAssert("==", 1) {
            condition.checkInRaceBool { simulation.frames.last().skills.isNotEmpty() }
        }

        "is_lastspurt" -> condition.checkInRaceBool { isInSpurt() }

        "lastspurt" -> condition.withAssert("==", 2) {
            { isInSpurt() && simulation.spurtParameters!!.speed == setting.maxSpurtSpeed }
        }

        "base_speed" -> condition.preChecked(setting.umaStatus.speed)
        "base_stamina" -> condition.preChecked(setting.umaStatus.stamina)
        "base_power" -> condition.preChecked(setting.umaStatus.power)
        "base_guts" -> condition.preChecked(setting.umaStatus.guts)
        "base_wiz" -> condition.preChecked(setting.umaStatus.wisdom)
        "course_distance" -> condition.preChecked(setting.courseLength)

        "random_lot" -> condition.withAssert("==") {
            val result = if (setting.fixRandom) true else value > Random.nextInt(100)
            return@withAssert { result }
        }

        "always" -> condition.withAssert("==", 1) { { true } }

        "is_last_straight_onetime" -> condition.withAssert("==", 1) {
            { isInFinalStraight() && !isInFinalStraight(simulation.startPosition) }
        }

        "weather" -> condition.preChecked(setting.weather)

        else -> {
            if (!ignoreConditions.containsKey(condition.type)) {
                println("not supported condition: $condition")
            }
            return null
        }
    }
}

private fun SkillCondition.withAssert(
    operator: String,
    value: Int? = null,
    check: SkillCondition.() -> (RaceState.() -> Boolean)
): RaceState.() -> Boolean {
    return if (this.operator != operator || (value != null && this.value != value)) {
        println("not supported : ${this.type} ${this.operator} ${this.value}")
        return { true }
    } else check()
}

private fun SkillCondition.preChecked(target: Int): RaceState.() -> Boolean {
    val result = check(target)
    return { result }
}

private fun SkillCondition.checkInRaceBool(target: RaceState.() -> Boolean): RaceState.() -> Boolean {
    return { check(if (target()) 1 else 0) }
}

private fun SkillCondition.checkInRace(target: RaceState.() -> Int): RaceState.() -> Boolean {
    return { check(target()) }
}

private fun checkInRandom(area: RandomEntry?): RaceState.() -> Boolean {
    return if (area == null) {
        { false }
    } else {
        { simulation.position in area }
    }
}

private fun checkInRandom(areas: List<RandomEntry>): RaceState.() -> Boolean {
    return { areas.any { simulation.position in it } }
}

/**
 * 0: Not in straight
 * 1: In front of stand
 * 2: Opposite of stand
 */
private fun RaceState.getStraightFrontType(position: Double = simulation.position): Int {
    setting.trackDetail.straights.reversed().forEachIndexed { index, straight ->
        if (position >= straight.start && position <= straight.end) {
            return if (index % 2 == 0) 1 else 2
        }
    }
    return 0
}

private class RandomEntry(start: Double, end: Double) : ClosedFloatingPointRange<Double> by start..end

private fun RaceSetting.chooseRandom(zoneStart: Double, zoneEnd: Double): RandomEntry {
    val rate = when (randomPosition) {
        RandomPosition.RANDOM -> Random.nextDouble()
        RandomPosition.FASTEST -> 0.0
        RandomPosition.FAST -> 0.25
        RandomPosition.MIDDLE -> 0.5
        RandomPosition.SLOW -> 0.75
        RandomPosition.SLOWEST -> 0.98
    }

    val start = rate * (zoneEnd - zoneStart) + zoneStart
    val end = min(start + 10.0, zoneEnd)
    return RandomEntry(start, end)
}

private fun RaceSetting.initCornerRandom(value: Int): RandomEntry? {
    val corners: MutableList<Corner?> = trackDetail.corners.takeLast(4).toMutableList()
    repeat(4 - corners.size) {
        corners.add(0, null)
    }
    val corner = corners.getOrNull(value - 1) ?: return null
    return chooseRandom(corner.start, corner.end)
}

private fun RaceSetting.initAllCornerRandom(): List<RandomEntry> {
    var corners = trackDetail.corners.toMutableList()
    val triggers = mutableListOf<RandomEntry>()

    repeat(4) {
        if (corners.isEmpty()) {
            return@repeat
        }
        val i = Random.nextInt(corners.size)
        val corner = corners[i]
        val trigger = logTrigger(corner.start, corner.start + corner.length)
        triggers.add(trigger)
        if (corner.start + corner.length - trigger.endInclusive >= 10) {
            corners[i] = Corner(trigger.endInclusive, trigger.endInclusive - corner.start)
        } else {
            corners.removeAt(i)
        }
        corners = corners.subList(i, corners.size)
    }
    triggers.sortBy { it.start }
    return triggers
}

private fun logTrigger(min: Double, max: Double): RandomEntry {
    val actualMax = max(min, max - 10.0)
    val start = min + Random.nextDouble() * (actualMax - min)
    val end = start + 10.0
    return RandomEntry(start, end)
}

private fun RaceSetting.initStraightRandom(): List<RandomEntry> {
    val straights = trackDetail.straights
    val straight = straights[Random.nextInt(straights.size)]
    return listOf(chooseRandom(straight.start, straight.end))
}

private fun RaceSetting.initSlopeRandom(up: Boolean): RandomEntry? {
    val slopes = trackDetail.slopes.filter { slope ->
        (slope.slope > 0 && up) || (slope.slope < 0 && !up)
    }
    val slope = slopes.randomOrNull() ?: return null
    return chooseRandom(slope.start, slope.start + slope.length)
}

private fun RaceSetting.initPhaseRandom(phase: Int, options: Pair<Double, Double> = 0.0 to 1.0): RandomEntry {
    val (startRate, endRate) = options
    val (zoneStart, zoneEnd) = getPhaseStartEnd(phase)
    val zoneLength = zoneEnd - zoneStart
    return chooseRandom(zoneStart + zoneLength * startRate, zoneEnd - zoneLength * (1 - endRate))
}

private fun RaceSetting.initFinalCornerRandom(): List<RandomEntry> {
    val finalCorner = trackDetail.corners.lastOrNull() ?: return emptyList()
    return listOf(chooseRandom(finalCorner.start, finalCorner.end))
}

private fun RaceSetting.initPhaseCornerRandom(phase: Int): List<RandomEntry> {
    val (phaseStart, phaseEnd) = getPhaseStartEnd(phase)
    val candidates = trackDetail.corners.mapNotNull { corner ->
        if (corner.end < phaseStart || corner.start > phaseEnd) null else {
            maxOf(corner.start, phaseStart) to minOf(corner.end, phaseEnd)
        }
    }
    if (candidates.isEmpty()) return emptyList()
    val chosen = candidates.random()
    return listOf(chooseRandom(chosen.first, chosen.second))
}

private fun RaceSetting.initFinalStraightRandom(): List<RandomEntry> {
    val finalCorner = trackDetail.corners.lastOrNull() ?: return emptyList()
    return listOf(chooseRandom(finalCorner.end, courseLength.toDouble()))
}

private fun RaceSetting.initIntervalRandom(startRate: Double, endRate: Double): RandomEntry {
    return chooseRandom(courseLength * startRate, courseLength * endRate)
}

private fun RaceState.isInFinalCorner(interval: Pair<Double, Double> = 0.0 to 1.0): Boolean {
    val (startRate, endRate) = interval
    val finalCorner = setting.trackDetail.corners.lastOrNull() ?: return false
    val start = finalCorner.start + startRate * finalCorner.length
    val end = finalCorner.start + endRate * finalCorner.length
    return simulation.position in start..end
}

private fun RaceState.isInFinalStraight(position: Double = simulation.position): Boolean {
    val lastStraight = setting.trackDetail.straights.lastOrNull() ?: return false
    return position >= lastStraight.start
}

private fun RaceState.isInSpurt(): Boolean {
    val spurtParameters = simulation.spurtParameters ?: return false
    return spurtParameters.distance + simulation.position >= setting.courseLength
}

fun RaceState.checkSkillTrigger(): List<InvokedSkill> {
    val skillTriggered = mutableListOf<InvokedSkill>()
    val coolDownMap = simulation.coolDownMap
    simulation.invokedSkills.forEach {
        val coolDownStart = coolDownMap[it.invoke.coolDownId]
        if (coolDownStart == null) {
            if (it.check(this)) {
                triggerSkill(it)
                skillTriggered += it
            }
        } else if (it.invoke.cd > 0.0) {
            if (simulation.frameElapsed - coolDownStart > it.invoke.cd * setting.coolDownBaseFrames) {
                if (it.check(this)) {
                    triggerSkill(it)
                    skillTriggered += it
                }
            }
        }
    }
    return skillTriggered
}

fun RaceState.triggerSkill(skill: InvokedSkill) {
    // TODO
    // skill.trigger
    if (skill.invoke.heal > 0) {
        doHeal(skill.invoke.heal)
    }
    if (skill.invoke.duration > 0.0) {
        simulation.operatingSkills += OperatingSkill(skill, simulation.frameElapsed)
    }
    if (skill.invoke.speedWithDecel > 0.0) {
        simulation.currentSpeed += skill.invoke.speedWithDecel
    }
    simulation.skillTriggerCount[currentPhase]++
    // 特殊スキル誘発カウント
    if (isInFinalCorner() && currentPhase >= 2) {
        simulation.skillTriggerCount[SkillTriggerCount.YUMENISHIKI]++
    }
    simulation.coolDownMap[skill.invoke.coolDownId] = simulation.frameElapsed
}

fun RaceState.doHeal(value: Int): Pair<Double, Double> {
    val heal = (setting.spMax * value) / 10000.0
    simulation.sp += heal
    val waste = max(0.0, simulation.sp - setting.spMax)
    simulation.sp -= waste
    if (value > 0) {
        simulation.healTriggerCount++
    }
    if (this.currentPhase >= 2) {
        simulation.spurtParameters = calcSpurtParameter()
    }
    return heal to waste
}
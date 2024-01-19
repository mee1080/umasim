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
import io.github.mee1080.umasim.race.data.SkillEffect
import io.github.mee1080.umasim.race.data2.SkillCondition
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun checkCondition(conditions: List<List<SkillCondition>>, setting: RaceSetting): RaceState.() -> Boolean {
    val checks = conditions.map { andConditions ->
        andConditions.map { checkCondition(it, setting) }
    }
    return {
        checks.any { andConditions ->
            andConditions.all { it.invoke(this) }
        }
    }
}

@Suppress("LiftReturnOrAssignment")
private fun checkCondition(condition: SkillCondition, setting: RaceSetting): RaceState.() -> Boolean {
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
//        "distance_rate_after_random" -> condition.checkInRace { simulation.distanceRateAfterRandom }
//        "corner_random" -> condition.checkInRace { simulation.cornerRandom }
//        "target_speed_random" -> condition.checkInRace { simulation.targetSpeedRandom }
//        "target_speed" -> condition.checkInRace { simulation.targetSpeed }

        else -> {
            println("not supported condition: $condition")
            return { true }
        }
    }



//    if (condition.distance_rate_after_random != null) {
//        val randoms = setting.initIntervalRandom(condition.distance_rate_after_random * 0.01, 1.0)
//        result += { isInRandom(randoms) }
//    }
//    if (condition.distance_rate_random != null) {
//        val randoms = setting.initIntervalRandom(
//            condition.distance_rate_random[0] * 0.01,
//            condition.distance_rate_random[1] * 0.01
//        )
//        result += { isInRandom(randoms) }
//    }
//    if (condition.corner_random != null) {
//        val randoms = setting.initCornerRandom(condition.corner_random)
//        result += { isInRandom(randoms) }
//    }
//    if (condition.all_corner_random != null) {
//        val randoms = setting.initAllCornerRandom()
//        result += { isInRandom(randoms) }
//    }
//    if (condition.slope != null) {
//        val up = condition.slope == 1
//        result += { (up && isInSlopeUp()) || (!up && isInSlopeDown()) }
//    }
//    if (condition.up_slope_random != null) {
//        val randoms = setting.initSlopeRandom(up = true)
//        result += { isInRandom(randoms) }
//    }
//    if (condition.down_slope_random != null) {
//        val randoms = setting.initSlopeRandom(up = false)
//        result += { isInRandom(randoms) }
//    }
//    if (condition.running_style != null) {
//        result += { condition.running_style.contains(setting.basicRunningStyle.value) }
//    }
//    if (condition.rotation != null) {
//        val value = setting.trackDetail.turn == condition.rotation
//        result += { value }
//    }
//    if (condition.ground_type != null) {
//        val value = setting.trackDetail.surface == condition.ground_type
//        result += { value }
//    }
//    if (condition.ground_condition != null) {
//        val value = condition.ground_condition.contains(setting.track.surfaceCondition)
//        result += { value }
//    }
//    if (condition.distance_type != null) {
//        val value = condition.distance_type.contains(setting.trackDetail.distanceType)
//        result += { value }
//    }
//    if (condition.track_id != null) {
//        val value = condition.track_id.contains(setting.trackDetail.raceTrackId)
//        result += { value }
//    }
//    if (condition.is_basis_distance != null) {
//        val checkBasis = condition.is_basis_distance == 1
//        val isBasis = setting.trackDetail.distance % 400 == 0
//        val value = checkBasis == isBasis
//        result += { value }
//    }
//    if (condition.distance_rate != null) {
//        var values = condition.distance_rate.split(",")
//        if (values.size == 2 && values[0].matches("^\\d+$".toRegex())) {
//            values = listOf(">=${values[0]}", "<=${values[1]}")
//        }
//        values.forEach {
//            val value = it.substring(2).toDouble() * 0.01 * setting.courseLength
//            if (it.startsWith(">=")) {
//                result += { simulation.position >= value }
//            } else if (it.startsWith("<=")) {
//                result += { simulation.position <= value }
//            }
//        }
//    }
//    if (condition.phase_random != null) {
//        val randoms = setting.initPhaseRandom(condition.phase_random)
//        result += { isInRandom(randoms) }
//    }
//    if (condition.phase_firsthalf_random != null) {
//        val randoms = setting.initPhaseRandom(condition.phase_firsthalf_random, 0.0 to 0.5)
//        result += { isInRandom(randoms) }
//    }
//    if (condition.phase_firstquarter_random != null) {
//        val randoms = setting.initPhaseRandom(condition.phase_firstquarter_random, 0.0 to 0.25)
//        result += { isInRandom(randoms) }
//    }
//    if (condition.phase_laterhalf_random != null) {
//        val randoms = setting.initPhaseRandom(condition.phase_laterhalf_random, 0.5 to 1.0)
//        result += { isInRandom(randoms) }
//    }
//    if (condition.phase_corner_random != null) {
//        val randoms = setting.initPhaseCornerRandom(condition.phase_corner_random)
//        result += { isInRandom(randoms) }
//    }
//    if (condition.is_finalcorner_random != null) {
//        val randoms = setting.initFinalCornerRandom()
//        result += { isInRandom(randoms) }
//    }
//    if (condition.is_finalstraight_random != null) {
//        val randoms = setting.initFinalStraightRandom()
//        result += { isInRandom(randoms) }
//    }
//    if (condition.straight_random != null) {
//        val randoms = setting.initStraightRandom()
//        result += { isInRandom(randoms) }
//    }
//    if (condition.is_last_straight != null) {
//        result += { isInFinalStraight() }
//    }
//    if (condition.phase != null) {
//        result += { condition.phase.contains(currentPhase) }
//    }
//    if (condition.is_finalcorner != null) {
//        result += { isInFinalStraight() && isInFinalCorner() }
//    }
//    if (condition.is_finalcorner_laterhalf != null) {
//        result += { isInFinalStraight() && isInFinalCorner(0.5 to 1.0) }
//    }
//    if (condition.corner != null) {
//        when (condition.corner) {
//            0 -> result += { !isInCorner() }
//            1 -> result += { isInCorner() }
//            else -> result += { isInCorner(condition.corner) }
//        }
//    }
//    if (condition.is_activate_any_skill != null) {
//        result += { simulation.skillTriggerCount[SkillTriggerCount.YUMENISHIKI] >= 1 }
//    }
//    if (condition.is_lastspurt != null) {
//        if (condition.is_lastspurt == 1) {
//            result += { isInSpurt() }
//        } else {
//            result += { !isInSpurt() }
//        }
//    }
//    if (condition.lastspurt != null) {
//        if (condition.lastspurt == 1) {
//            result += { isInSpurt() && simulation.spurtParameters!!.speed < setting.maxSpurtSpeed }
//        } else if (condition.lastspurt == 2) {
//            result += { isInSpurt() && simulation.spurtParameters!!.speed == setting.maxSpurtSpeed }
//        }
//    }
//    if (condition.base_speed != null) {
//        val value = setting.umaStatus.speed >= condition.base_speed
//        result += { value }
//    }
//    if (condition.base_stamina != null) {
//        val value = setting.umaStatus.stamina >= condition.base_stamina
//        result += { value }
//    }
//    if (condition.base_power != null) {
//        val value = setting.umaStatus.power >= condition.base_power
//        result += { value }
//    }
//    if (condition.base_guts != null) {
//        val value = setting.umaStatus.guts >= condition.base_guts
//        result += { value }
//    }
//    if (condition.base_wisdom != null) {
//        val value = setting.umaStatus.wisdom >= condition.base_wisdom
//        result += { value }
//    }
//    if (condition.course_distance != null) {
//        val value = setting.courseLength == condition.course_distance
//        result += { value }
//    }
//    return result
}

private fun SkillCondition.preChecked(target: Int): RaceState.() -> Boolean {
    val result = check(target)
    return { result }
}

private fun SkillCondition.checkInRace(target: RaceState.() -> Int): RaceState.() -> Boolean {
    return { check(target()) }
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

private fun RaceSetting.toPosition(distanceLeft: Double): Double {
    return trackDetail.distance - distanceLeft
}

private class RandomEntry(start: Double, end: Double) : ClosedFloatingPointRange<Double> by start..end

private fun RaceSetting.chooseRandom(zoneStart: Double, zoneEnd: Double): RandomEntry {
    val rate = when (randomPosition) {
        0 -> Random.nextDouble()
        1 -> 0.0
        2 -> 0.25
        3 -> 0.5
        4 -> 0.75
        else -> 0.98
    }

    val start = rate * (zoneEnd - zoneStart) + zoneStart
    val end = min(start + 10.0, zoneEnd)
    return RandomEntry(start, end)
}

private fun RaceSetting.initCornerRandom(values: List<Int>): List<RandomEntry> {
    val ret = mutableListOf<RandomEntry>()
    val corners: MutableList<Corner?> = trackDetail.corners.takeLast(4).toMutableList()
    repeat(4 - corners.size) {
        corners.add(0, null)
    }
    val targetCorners = values.mapNotNull { value ->
        corners.getOrNull(value - 1)
    }
    for (corner in targetCorners) {
        ret.add(chooseRandom(corner.start, corner.end))
    }
    return ret
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

private fun RaceSetting.initSlopeRandom(up: Boolean): List<RandomEntry> {
    val slopes = trackDetail.slopes.filter { slope ->
        (slope.slope > 0 && up) || (slope.slope < 0 && !up)
    }
    if (slopes.isEmpty()) {
        return emptyList()
    }
    val chosen = Random.nextInt(slopes.size)
    val slope = slopes[chosen]
    return listOf(chooseRandom(slope.start, slope.start + slope.length))
}

private fun RaceSetting.initPhaseRandom(phase: Int, options: Pair<Double, Double> = 0.0 to 1.0): List<RandomEntry> {
    val (startRate, endRate) = options
    val (zoneStart, zoneEnd) = getPhaseStartEnd(phase)
    val zoneLength = zoneEnd - zoneStart
    return listOf(chooseRandom(zoneStart + zoneLength * startRate, zoneEnd - zoneLength * (1 - endRate)))
}

private fun RaceSetting.getPhaseStartEnd(phase: Int): Pair<Double, Double> {
    return when (phase) {
        0 -> 0.0 to courseLength / 6.0
        1 -> courseLength / 6.0 to (courseLength * 2.0) / 3.0
        2 -> (courseLength * 2.0) / 3.0 to (courseLength * 5.0) / 6.0
        3 -> (courseLength * 5.0) / 6.0 to courseLength.toDouble()
        else -> throw IllegalArgumentException()
    }
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

private fun RaceSetting.initIntervalRandom(startRate: Double, endRate: Double): List<RandomEntry> {
    return listOf(chooseRandom(courseLength * startRate, courseLength * endRate))
}

private fun RaceState.isInRandom(randoms: List<RandomEntry>): Boolean {
    return randoms.any { simulation.position in it }
}

private fun RaceState.isInFinalCorner(interval: Pair<Double, Double> = 0.0 to 1.0): Boolean {
    val (startRate, endRate) = interval
    val finalCorner = setting.trackDetail.corners.lastOrNull() ?: return false
    val start = finalCorner.start + startRate * finalCorner.length
    val end = finalCorner.start + endRate * finalCorner.length
    return simulation.position in start..end
}

private fun RaceState.isInFinalStraight(): Boolean {
    val lastStraight = setting.trackDetail.straights.lastOrNull() ?: return false
    return simulation.position >= lastStraight.start
}

private fun RaceState.isInCorner(cornerNumber: Int? = null): Boolean {
    val corners = setting.trackDetail.corners
    val cornerIndex = corners.indexOfFirst { simulation.position in it.start..it.end }
    if (cornerIndex < 0) return false
    if (cornerNumber == null) return true
    return cornerIndex == corners.size + cornerNumber - 5
}

private fun RaceState.isInSpurt(): Boolean {
    val spurtParameters = simulation.spurtParameters ?: return false
    return spurtParameters.distance + simulation.position >= setting.courseLength
}

fun RaceState.checkSkillTrigger(): List<SkillEffect> {
    val skillTriggered = mutableListOf<SkillEffect>()
    simulation.invokedSkills.forEach {
        if (!simulation.coolDownMap.containsKey(it.coolDownId) && it.checkAll(this)) {
            // TODO chainTriggered
            triggerSkill(it)
            skillTriggered += it
        }
    }
    return skillTriggered
}

fun RaceState.triggerSkill(skill: SkillEffect) {
    // TODO
    // skill.trigger
    skill.heal?.let {
        doHeal(it)
    }
    skill.duration?.let {
        simulation.operatingSkills += OperatingSkill(skill, simulation.frameElapsed)
    }
    simulation.skillTriggerCount[currentPhase]++
    // 特殊スキル誘発カウント
    if (isInFinalCorner() && currentPhase >= 2) {
        simulation.skillTriggerCount[SkillTriggerCount.YUMENISHIKI]++
    }
    simulation.coolDownMap[skill.coolDownId] = simulation.frameElapsed
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
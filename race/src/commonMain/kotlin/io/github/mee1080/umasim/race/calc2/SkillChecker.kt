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
import io.github.mee1080.umasim.race.data2.SkillData
import io.github.mee1080.umasim.race.data2.ignoreConditions
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun checkCondition(
    skill: SkillData,
    conditions: List<List<SkillCondition>>,
    setting: RaceSetting,
    calculatedAreas: MutableMap<String, List<RandomEntry>>,
): RaceState.() -> Boolean {
    val checks = conditions.map { andConditions ->
        andConditions.mapNotNull { checkCondition(skill, it, setting, calculatedAreas) }
    }
    if (checks.isEmpty()) return { true }
    return {
        checks.any { andConditions ->
            andConditions.all { it.invoke(this) }
        }
    }
}

private fun checkCondition(
    skill: SkillData,
    condition: SkillCondition,
    baseSetting: RaceSetting,
    calculatedAreas: MutableMap<String, List<RandomEntry>>,
): (RaceState.() -> Boolean)? {
    return when (condition.type) {
        "motivation" -> condition.preChecked(baseSetting.umaStatus.condition.value)
        "hp_per" -> condition.checkInRace { (simulation.sp / setting.spMax * 100).toInt() }
        "activate_count_heal" -> condition.checkInRace { simulation.healTriggerCount }
        "activate_count_all" -> condition.checkInRace { simulation.skillTriggerCount.total }
        "activate_count_start" -> condition.checkInRace { simulation.skillTriggerCount.inPhase[0] }
        "activate_count_later_half" -> condition.checkInRace { simulation.skillTriggerCount.inLaterHalf }
        "activate_count_middle" -> condition.checkInRace { simulation.skillTriggerCount.inPhase[1] }
        "activate_count_end_after" -> condition.checkInRace { simulation.skillTriggerCount.inAfterPhase2 }

        "accumulatetime" -> condition.checkInRace { simulation.frameElapsed / 15 }
        "straight_front_type" -> condition.checkInRace { getStraightFrontType() }
        "is_badstart" -> condition.checkInRace { if (simulation.startDelay >= 0.08) 1 else 0 }
        "temptation_count" -> condition.checkInRace { if (simulation.hasTemptation) 1 else 0 }
        "remain_distance" -> condition.checkInRace { baseSetting.courseLength - simulation.startPosition.toInt() }
        "distance_rate_after_random" -> condition.withAssert("==") {
            checkInRandom(calculatedAreas, condition.type) { baseSetting.initIntervalRandom(value * 0.01, 1.0) }
        }

        "corner_random" -> condition.withAssert("==") {
            checkInRandom(calculatedAreas, condition.type) { baseSetting.initCornerRandom(value) }
        }

        "all_corner_random" -> condition.withAssert("==", 1) {
            checkInRandom(calculatedAreas, condition.type) { baseSetting.initAllCornerRandom() }
        }

        "slope" -> condition.checkInRace { getSlopeInt() }

        "up_slope_random" -> condition.withAssert("==", 1) {
            checkInRandom(calculatedAreas, condition.type) { baseSetting.initSlopeRandom(up = true) }
        }

        "down_slope_random" -> condition.withAssert("==", 1) {
            checkInRandom(calculatedAreas, condition.type) { baseSetting.initSlopeRandom(up = false) }
        }

        "running_style" -> condition.preChecked(baseSetting.basicRunningStyle.value)
        "rotation" -> condition.preChecked(baseSetting.trackDetail.turn)
        "ground_type" -> condition.preChecked(baseSetting.trackDetail.surface)
        "ground_condition" -> condition.preChecked(baseSetting.track.surfaceCondition)
        "distance_type" -> condition.preChecked(baseSetting.trackDetail.distanceType)
        "track_id" -> condition.preChecked(baseSetting.trackDetail.raceTrackId)
        "is_basis_distance" -> condition.preChecked(baseSetting.trackDetail.isBasisDistance)
        "distance_rate" -> condition.checkInRace { (simulation.position * 100.0 / baseSetting.courseLength).toInt() }
        "phase_random" -> checkInRandom(
            calculatedAreas,
            condition.type
        ) { baseSetting.initPhaseRandom(condition.value) }

        "phase_firsthalf_random" -> checkInRandom(
            calculatedAreas,
            condition.type
        ) { baseSetting.initPhaseRandom(condition.value, 0.0 to 0.5) }

        "phase_firstquarter_random" -> checkInRandom(calculatedAreas, condition.type) {
            baseSetting.initPhaseRandom(condition.value, 0.0 to 0.25)
        }

        "phase_laterhalf_random" -> checkInRandom(calculatedAreas, condition.type) {
            baseSetting.initPhaseRandom(condition.value, 0.5 to 1.0)
        }

        "phase_corner_random" -> checkInRandom(calculatedAreas, condition.type) {
            baseSetting.initPhaseCornerRandom(condition.value)
        }

        "is_finalcorner_random" -> condition.withAssert("==", 1) {
            checkInRandom(calculatedAreas, condition.type) { baseSetting.initFinalCornerRandom() }
        }

        "is_finalstraight_random", "last_straight_random" -> condition.withAssert("==", 1) {
            checkInRandom(calculatedAreas, condition.type) { baseSetting.initFinalStraightRandom() }
        }

        "straight_random" -> condition.withAssert("==", 1) {
            checkInRandom(calculatedAreas, condition.type) { baseSetting.initStraightRandom() }
        }

        "is_last_straight" -> condition.withAssert("==", 1) {
            checkInRaceBool { isInFinalStraight() }
        }

        "phase" -> condition.checkInRace { currentPhase }

        "is_finalcorner" -> condition.checkInRaceBool { isAfterFinalCorner }

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

        "base_speed" -> condition.preChecked(baseSetting.umaStatus.speed)
        "base_stamina" -> condition.preChecked(baseSetting.umaStatus.stamina)
        "base_power" -> condition.preChecked(baseSetting.umaStatus.power)
        "base_guts" -> condition.preChecked(baseSetting.umaStatus.guts)
        "base_wiz" -> condition.preChecked(baseSetting.umaStatus.wisdom)
        "course_distance" -> condition.preChecked(baseSetting.courseLength)

        "random_lot" -> condition.withAssert("==") {
            val result = if (baseSetting.fixRandom) true else value > Random.nextInt(100)
            return@withAssert { result }
        }

        "always" -> condition.withAssert("==", 1) { { true } }

        "is_last_straight_onetime" -> condition.withAssert("==", 1) {
            { isInFinalStraight() && !isInFinalStraight(simulation.startPosition) }
        }

        "weather" -> condition.preChecked(baseSetting.weather)

        "is_move_lane" -> condition.checkSpecialState("move_lane")

        "change_order_onetime" -> condition.checkSpecialState("change_order_onetime")

        "is_overtake" -> condition.checkSpecialState("overtake")

        "overtake_target_time" -> condition.checkSpecialState("overtaken", -1)

        "compete_fight_count" -> condition.checkInRaceBool { simulation.competeFight }

        "blocked_front" -> condition.checkSpecialState("blocked_front")

        "blocked_front_continuetime" -> condition.checkSpecialState("blocked_front", -1)

        "blocked_side_continuetime" -> condition.checkSpecialState("blocked_side", -1)

        "infront_near_lane_time" -> condition.checkSpecialState("infront_near_lane", -1)

        "behind_near_lane_time" -> condition.checkSpecialState("behind_near_lane", -1)

        "behind_near_lane_time_set1" -> condition.checkSpecialState("behind_near_lane", -1)

        "near_count" -> condition.checkSpecialState("near_count")

        "is_surrounded" -> condition.checkSpecialState("is_surrounded")

        "temptation_opponent_count_behind" -> condition.checkSpecialState("temptation_opponent_count_behind")

        "is_other_character_activate_advantage_skill" -> condition.withAssert("==") {
            val key = "is_other_character_activate_advantage_skill${condition.value}"
            { (simulation.specialState[key] ?: 0) > 0 }
        }

        "change_order_up_end_after" -> condition.checkSpecialState("change_order_up_end_after")

        "change_order_up_finalcorner_after" -> condition.checkSpecialState("change_order_up_finalcorner_after")

        "is_activate_other_skill_detail" -> condition.withAssert("==", 1) {
            { simulation.coolDownMap.containsKey(skill.id) }
        }

        "popularity" -> condition.preChecked(baseSetting.popularity)

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

//private inline fun checkInRandom(
//    calculated: MutableMap<String, List<RandomEntry>>,
//    key: String,
//    calcArea: () -> RandomEntry?,
//): RaceState.() -> Boolean {
//    val area = calculated.getOrPut(key) { calcArea()?.let { listOf(it) } ?: emptyList() }
//    return if (area.isEmpty()) {
//        { false }
//    } else {
//        { simulation.position in area[0] }
//    }
//}

private inline fun checkInRandom(
    calculated: MutableMap<String, List<RandomEntry>>,
    key: String,
    calcAreas: () -> List<RandomEntry>,
): RaceState.() -> Boolean {
    val areas = calculated.getOrPut(key, calcAreas)
    return { areas.any { simulation.position in it } }
}

private fun SkillCondition.checkSpecialState(key: String, adjust: Int = 0): RaceState.() -> Boolean {
    return { check((simulation.specialState[key] ?: 0) + adjust) }
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

class RandomEntry(start: Double, end: Double) : ClosedFloatingPointRange<Double> by start..end

private fun RaceSetting.chooseRandom(zoneStart: Double, zoneEnd: Double): List<RandomEntry> {
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
    return listOf(RandomEntry(start, end))
}

private fun RaceSetting.initCornerRandom(value: Int): List<RandomEntry> {
    val corners: MutableList<Corner?> = trackDetail.corners.takeLast(4).toMutableList()
    repeat(4 - corners.size) {
        corners.add(0, null)
    }
    val corner = corners.getOrNull(value - 1) ?: return emptyList()
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
    return chooseRandom(straight.start, straight.end)
}

private fun RaceSetting.initSlopeRandom(up: Boolean): List<RandomEntry> {
    val slopes = trackDetail.slopes.filter { slope ->
        (slope.slope > 0 && up) || (slope.slope < 0 && !up)
    }
    val slope = slopes.randomOrNull() ?: return emptyList()
    return chooseRandom(slope.start, slope.start + slope.length)
}

private fun RaceSetting.initPhaseRandom(phase: Int, options: Pair<Double, Double> = 0.0 to 1.0): List<RandomEntry> {
    val (startRate, endRate) = options
    val (zoneStart, zoneEnd) = getPhaseStartEnd(phase)
    val zoneLength = zoneEnd - zoneStart
    return chooseRandom(zoneStart + zoneLength * startRate, zoneEnd - zoneLength * (1 - endRate))
}

private fun RaceSetting.initFinalCornerRandom(): List<RandomEntry> {
    val finalCorner = trackDetail.corners.lastOrNull() ?: return emptyList()
    return chooseRandom(finalCorner.start, finalCorner.end)
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
    return chooseRandom(chosen.first, chosen.second)
}

private fun RaceSetting.initFinalStraightRandom(): List<RandomEntry> {
    val finalCorner = trackDetail.corners.lastOrNull() ?: return emptyList()
    return chooseRandom(finalCorner.end, courseLength.toDouble())
}

private fun RaceSetting.initIntervalRandom(startRate: Double, endRate: Double): List<RandomEntry> {
    return chooseRandom(courseLength * startRate, courseLength * endRate)
}

private fun RaceState.isInSpurt(): Boolean {
    val spurtParameters = simulation.spurtParameters ?: return false
    return spurtParameters.distance + simulation.position >= setting.courseLength
}

fun RaceState.checkSkillTrigger(): List<InvokedSkill> {
    val skillTriggered = mutableListOf<InvokedSkill>()
    val coolDownMap = simulation.coolDownMap
    simulation.invokedSkills.forEach {
        if (!it.preChecked) {
            it.preChecked = it.preCheck(this)
            if (!it.preChecked) {
                return@forEach
            }
        }
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
    if (skill.invoke.isHeal) {
        doHeal(skill.invoke.heal(this))
    }
    if (skill.invoke.duration > 0.0) {
        simulation.operatingSkills += OperatingSkill(
            skill,
            simulation.frameElapsed,
            skill.invoke.totalSpeed(this),
            skill.invoke.currentSpeed(this),
            skill.invoke.acceleration(this),
            skill.invoke.calcDuration(this),
        )
    }
    if (skill.invoke.isSpeedWithDecel) {
        simulation.currentSpeed += skill.invoke.speedWithDecel(this)
    }
    simulation.skillTriggerCount.increment(this)
    simulation.coolDownMap[skill.invoke.coolDownId] = simulation.frameElapsed
}

fun RaceState.doHeal(value: Double): Pair<Double, Double> {
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
    if (simulation.staminaKeep) {
        applyPositionCompetition()
    }
    return heal to waste
}
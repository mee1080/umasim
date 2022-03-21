package io.github.mee1080.umasim.rotation

import io.github.mee1080.umasim.data.*
import kotlin.math.max

class RaceRotation(
    val list: Array<RaceEntry?> = Array(79) { null },
) {
    fun getRace(turn: Int) = list.getOrNull(turn)

    val raceNames by lazy { list.mapNotNull { it?.name }.toSet() }

    val raceType = list
        .slice(0..72)
        .filterNotNull()
        .groupBy { it.ground to it.distanceType }
        .mapValues { it.value.size }
        .toList()
        .sortedByDescending { it.second * 10000 - it.first.first.ordinal * 100 - it.first.second.ordinal }

    init {
        val type = raceType.getOrNull(0)?.first ?: (RaceGround.TURF to RaceDistance.SHORT)
        val distance = when (type.second) {
            RaceDistance.SHORT -> 1400
            RaceDistance.MILE -> 1800
            RaceDistance.MIDDLE -> 2000
            else -> 2600
        }
        list[74] = RaceEntry(74, "クライマックス第1戦", 0, 7000, RaceGrade.FINALS, distance, type.first, "")
        list[76] = RaceEntry(76, "クライマックス第2戦", 0, 10000, RaceGrade.FINALS, distance, type.first, "")
        list[78] = RaceEntry(78, "クライマックス第3戦", 0, 30000, RaceGrade.FINALS, distance, type.first, "")

    }

    operator fun plus(raceEntry: RaceEntry) =
        RaceRotation(list = list.copyOf().also { it[raceEntry.turn] = raceEntry })

    operator fun plus(raceEntries: Collection<RaceEntry>) = RaceRotation(list = list.copyOf().also {
        raceEntries.forEach { raceEntry ->
            it[raceEntry.turn] = raceEntry
        }
    })

    operator fun minus(turn: Int) = RaceRotation(list = list.copyOf().also { it[turn] = null })

    fun checkAchievement(achievements: List<RaceAchievement>): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        achievements.forEach { achievement ->
            result[achievement.name] = achievement.conditions.sumOf { condition ->
                when (condition) {
                    is RaceNameCondition -> {
                        val count = condition.raceNames.count { raceNames.contains(it) }
                        max(0, condition.count - count)
                    }
                    is RaceNameEndCondition -> {
                        val count = raceNames.count { it.endsWith(condition.raceNameEnd) }
                        max(0, condition.count - count)
                    }
                    is RaceCondition -> {
                        val check = condition.condition
                        val count = list.count { it?.check() ?: false }
                        max(0, condition.count - count)
                    }
                    is AnotherAchievementCondition -> {
                        condition.condition.minOf { result[it] ?: Int.MAX_VALUE }
                    }
                }
            }
        }
        return result
    }

    override fun toString(): String {
        return list.mapIndexedNotNull { index, raceEntry ->
            raceEntry?.let { "$index: ${it.name}" }
        }.joinToString()
    }
}
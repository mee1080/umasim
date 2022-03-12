package io.github.mee1080.umasim.rotation

import io.github.mee1080.umasim.data.*
import kotlin.math.max

class RaceRotation(
    private val list: Array<RaceEntry?> = Array(60) { null },
) {
    fun getRace(turn: Int) = list.getOrNull(turn - 13)

    val selectedRace by lazy { (0..78).map { getRace(it) } }

    val selectedRaceName by lazy { selectedRace.map { it?.name } }

    val raceNames by lazy { list.mapNotNull { it?.name }.toSet() }

    operator fun plus(raceEntry: RaceEntry) =
        RaceRotation(list = list.copyOf().also { it[raceEntry.turn - 13] = raceEntry })

    operator fun plus(raceEntries: Collection<RaceEntry>) = RaceRotation(list = list.copyOf().also {
        raceEntries.forEach { raceEntry ->
            it[raceEntry.turn - 13] = raceEntry
        }
    })

    operator fun minus(turn: Int) = RaceRotation(list = list.copyOf().also { it[turn - 13] = null })

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
            raceEntry?.let { "${index + 13}: ${it.name}" }
        }.joinToString()
    }
}
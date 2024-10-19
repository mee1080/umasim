package io.github.mee1080.umasim.scenario.climax

import io.github.mee1080.umasim.data.RaceEntry

class RaceAchievement(
    val name: String,
    val status: Int = 0,
    val skill: String? = null,
    vararg val conditions: AchievementCondition,
) {
    constructor(name: String, skill: String, vararg conditions: AchievementCondition)
            : this(name, 0, skill, *conditions)

    constructor(name: String, status: Int, vararg conditions: AchievementCondition)
            : this(name, status, null, *conditions)
}

sealed interface AchievementCondition

class RaceNameCondition(
    val count: Int,
    vararg val raceNames: String,
) : AchievementCondition {
    constructor(vararg raceNames: String) : this(raceNames.size, *raceNames)
}

class RaceNameEndCondition(
    val count: Int = 1,
    val raceNameEnd: String,
) : AchievementCondition

class RaceCondition(
    val count: Int = 1,
    val condition: RaceEntry.() -> Boolean,
) : AchievementCondition

class AnotherAchievementCondition(
    vararg val condition: String,
) : AchievementCondition
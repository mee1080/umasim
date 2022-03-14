package io.github.mee1080.umasim.rotation

import io.github.mee1080.umasim.data.*

class RaceRotationCalculator(
    private val ground: Map<RaceGround, Rank>,
    private val distance: Map<RaceDistance, Rank>,
) {

    companion object {
        val displayRankList = Rank.values().map { it.displayName }
        fun getRank(displayName: String) = Rank.values().first { it.displayName == displayName }
    }

    enum class Rank(
        val displayName: String,
        val rate: Int,
    ) {
        A("A", 100), B("B", 80), C("C", 40), NONE("なし", 0),
    }

    data class State(
        val rotation: RaceRotation,
        val currentAchievement: Map<String, Int?>,
        val totalStatus: Int,
        val recommendation: List<Triple<RaceEntry, Int, List<Pair<String, Int?>>>>,
    )

    var state: State

    private val ignoreGround = ground.filter { it.value == Rank.NONE }.map { it.key }

    private val ignoreDistance = distance.filter { it.value == Rank.NONE }.map { it.key }

    val raceSelections = Store.raceMap.map { list ->
        list.filter { !ignoreGround.contains(it.ground) && !ignoreDistance.contains(it.distanceType) }
    }

    val achievements: List<RaceAchievement>

    private val achievementMap: Map<String, RaceAchievement>

    init {
        val allAchievements = Store.Climax.raceAchievement
        val rotation = RaceRotation()
        val cannotAchieve = checkCanAchieve(allAchievements, raceSelections, emptyList()).map { it.name }
        val currentAchievement = rotation.checkAchievement(allAchievements).mapValues {
            if (cannotAchieve.contains(it.key)) null else it.value
        }
        achievements = allAchievements.filter { !cannotAchieve.contains(it.name) }
        achievementMap = achievements.associateBy { it.name }
        val recommendation = recommend(rotation, currentAchievement, achievements)
        state = State(
            rotation,
            currentAchievement,
            0,
            recommendation,
        )
    }

    fun add(turn: Int, name: String) {
        val race = raceSelections[turn].firstOrNull { it.name == name }
        state = if (race == null) {
            state.copy(rotation = state.rotation - turn)
        } else {
            state.copy(rotation = state.rotation + race)
        }
        calculate()
    }

    private fun calculate() {
        val newAchievement = state.rotation.checkAchievement(achievements)
        val cannotAchieve =
            checkCanAchieve(achievements, raceSelections, state.rotation.selectedRace).map { it.name }.toSet()
        val currentAchievement = state.currentAchievement.mapValues {
            if (cannotAchieve.contains(it.key)) null else newAchievement[it.key]
        }
        val totalStatus =
            2 * currentAchievement.filter { it.value == 0 }.map { achievementMap[it.key]?.status ?: 0 }.sum()
        val availableAchievements = achievements.filter { !cannotAchieve.contains(it.name) }
        val recommendation = recommend(state.rotation, currentAchievement, availableAchievements)
        state = state.copy(
            currentAchievement = currentAchievement,
            totalStatus = totalStatus,
            recommendation = recommendation,
        )
    }

    private fun recommend(
        rotation: RaceRotation,
        currentAchievement: Map<String, Int?>,
        availableAchievement: List<RaceAchievement>,
    ): List<Triple<RaceEntry, Int, List<Pair<String, Int?>>>> {
        return raceSelections.flatMapIndexed { index, raceEntries ->
            if (rotation.getRace(index) == null) {
                raceEntries.mapNotNull { raceEntry ->
                    val conditionScore =
                        (ground[raceEntry.ground]?.rate ?: 0) + (distance[raceEntry.distanceType]?.rate ?: 0)
                    if (conditionScore == 0) return@mapNotNull null
                    val nextRotation = rotation + raceEntry
                    val nextAchievement = nextRotation.checkAchievement(availableAchievement)
                    // TODO 動作速度が大幅に遅くなるため、達成できなくなる称号の判定は除外
//                    val nextCannotAchieve =
//                        checkCanAchieve(availableAchievement, raceSelections, nextRotation.selectedRace)
                    val diff = nextAchievement.mapNotNull { (name, count) ->
                        if (currentAchievement[name] != count) name to count else null
                    }
                    val diffScore = 10 * diff.sumOf { (name, _) ->
                        achievementMap[name]?.status ?: 5
                    }
                    val gradeScore = when (raceEntry.grade) {
                        RaceGrade.G3 -> 20
                        RaceGrade.G2 -> 20
                        RaceGrade.G1 -> 40
                        else -> 0
                    }
                    var beforeRaceCount = 0
                    var afterRaceCount = 0
                    for (i in 1..3) {
                        if (rotation.getRace(index - i) == null) break
                        beforeRaceCount++
                    }
                    for (i in 1..3) {
                        if (rotation.getRace(index + i) == null) break
                        afterRaceCount++
                    }
                    var raceCount = beforeRaceCount + afterRaceCount
                    if (index >= 25 && (index + afterRaceCount) % 24 == 0) {
                        raceCount -= 1
                    }
                    val continueScore = when (raceCount) {
                        0, -1 -> 80
                        1 -> 60
                        2 -> 40
                        else -> 0
                    }
                    val totalScore = conditionScore + diffScore + gradeScore + continueScore
                    Triple(raceEntry, totalScore, diff)
                }
            } else emptyList()
        }.sortedByDescending { it.second }
    }

    private fun checkCanAchieve(
        achievements: List<RaceAchievement>,
        raceSelections: List<List<RaceEntry>>,
        selectedRace: List<RaceEntry?>
    ): List<RaceAchievement> {
        val cannotAchieveSet = mutableSetOf<String>()
        val restRace = raceSelections.mapIndexed { index, list ->
            selectedRace.getOrNull(index)?.let { listOf(it) } ?: list
        }
        val restRaceList = restRace.flatten()
        val restRaceNames = restRaceList.map { it.name }.toSet()
        achievements.forEach { achievement ->
            val cannotAchieve = achievement.conditions.any { condition ->
                when (condition) {
                    is RaceNameCondition -> {
                        condition.count > condition.raceNames.count { restRaceNames.contains(it) }
                    }
                    is RaceNameEndCondition -> {
                        // 同一ターンに複数は無しのため、該当名称数をカウント
                        condition.count > restRaceNames.count { it.endsWith(condition.raceNameEnd) }
                    }
                    is RaceCondition -> {
                        val check = condition.condition
                        condition.count > restRace.count { list ->
                            list.any { it.check() }
                        }
                    }
                    is AnotherAchievementCondition -> {
                        condition.condition.any { cannotAchieveSet.contains(it) }
                    }
                }
            }
            if (cannotAchieve) {
                cannotAchieveSet.add(achievement.name)
            }
        }
        return achievements.filter { cannotAchieveSet.contains(it.name) }
    }
}
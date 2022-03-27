package io.github.mee1080.umasim.rotation

import io.github.mee1080.umasim.data.*
import kotlinx.serialization.Serializable

class RaceRotationCalculator(
    private val ground: Map<RaceGround, Rank>,
    private val distance: Map<RaceDistance, Rank>,
    var option: Option = Option(),
    selectedRaceName: List<String?> = emptyList(),
    charaId: Int,
) {

    companion object {
        val displayRankList = Rank.values().map { it.displayName }
        fun getRank(displayName: String) = Rank.values().first { it.displayName == displayName }
    }

    enum class Rank(
        val displayName: String,
    ) {
        A("A"), B("B"), C("C"), NONE("なし"),
    }

    data class State(
        val rotation: RaceRotation,
        val currentAchievement: Map<String, Int?>,
        val totalStatus: Int,
        val recommendation: List<Triple<RaceEntry, Int, List<Pair<String, Int?>>>>,
        val charaId: Int,
    )

    @Serializable
    data class Option(
        val rankA: Int = 100,
        val rankB: Int = 80,
        val rankC: Int = 40,
        val gradeG1: Int = 40,
        val gradeG2G3: Int = 20,
        val gradeOpenTurf: Int = -40,
        val gradeOpenDirt: Int = 0,
        val continue2: Int = 10,
        val continue3: Int = -30,
        val continue4: Int = -70,
    ) {
        fun rank(rank: Rank?) = when (rank) {
            Rank.A -> rankA
            Rank.B -> rankB
            Rank.C -> rankC
            else -> 0
        }

        fun grade(grade: RaceGrade, ground: RaceGround) = when (grade) {
            RaceGrade.G1 -> gradeG1
            RaceGrade.G2, RaceGrade.G3 -> gradeG2G3
            else -> if (ground == RaceGround.TURF) gradeOpenTurf else gradeOpenDirt
        }

        fun continueCount(count: Int) = when (count) {
            0, -1 -> 0
            1 -> continue2
            2 -> continue3
            else -> continue4
        }
    }

    var state: State
        private set

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
        val cannotAchieve = checkCanAchieve(allAchievements, raceSelections, emptyArray()).map { it.name }
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
            charaId,
        )
        setChara(charaId)
        if (selectedRaceName.isNotEmpty()) {
            setRace(selectedRaceName)
        }
    }

    fun setChara(charaId: Int) {
        state = state.copy(charaId = charaId)
        val debut = Store.getGoalRaceList(charaId).getOrNull(0)
        add(12, debut)
    }

    fun add(turn: Int, name: String) {
        val race = raceSelections[turn].firstOrNull { it.name == name }
        add(turn, race)
    }

    fun add(turn: Int, race: RaceEntry?) {
        state = if (race == null) {
            state.copy(rotation = state.rotation - turn)
        } else {
            state.copy(rotation = state.rotation + race)
        }
        calculate()
    }

    private fun setRace(selectedRaceName: List<String?>) {
        selectedRaceName.forEachIndexed { turn, name ->
            if (turn in 13..72) {
                val race = raceSelections[turn].firstOrNull { it.name == name }
                state = if (race == null) {
                    state.copy(rotation = state.rotation - turn)
                } else {
                    state.copy(rotation = state.rotation + race)
                }
            }
        }
        calculate()
    }

    fun calculate() {
        val newAchievement = state.rotation.checkAchievement(achievements)
        val cannotAchieve =
            checkCanAchieve(achievements, raceSelections, state.rotation.list).map { it.name }.toSet()
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
                raceEntries.map { raceEntry ->
                    val conditionScore =
                        option.rank(ground[raceEntry.ground]) + option.rank(distance[raceEntry.distanceType])
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
                    val gradeScore = option.grade(raceEntry.grade, raceEntry.ground)
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
                    val continueScore = option.continueCount(raceCount)
                    val totalScore = conditionScore + diffScore + gradeScore + continueScore
                    Triple(raceEntry, totalScore, diff)
                }
            } else emptyList()
        }.sortedByDescending { it.second }
    }

    private fun checkCanAchieve(
        achievements: List<RaceAchievement>,
        raceSelections: List<List<RaceEntry>>,
        selectedRace: Array<RaceEntry?>
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
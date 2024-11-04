package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.RaceDistance
import io.github.mee1080.umasim.data.RaceEntry
import io.github.mee1080.umasim.data.RaceGround
import io.github.mee1080.umasim.data.turnToString
import io.github.mee1080.umasim.rotation.RaceRotationCalculator
import io.github.mee1080.umasim.scenario.climax.RaceAchievement

data class RotationState(
    val calcState: RaceRotationCalculator.State,
    val raceSelection: List<List<RaceEntry>>,
    val achievementList: List<RaceAchievement>,
    val groundSetting: Map<RaceGround, RaceRotationCalculator.Rank>,
    val distanceSetting: Map<RaceDistance, RaceRotationCalculator.Rank>,
    val option: RaceRotationCalculator.Option,
    val charaSelection: List<Pair<Int, String>>,
    val recommendFilter: RecommendFilter = NoFilter,
    val rotationSaveName: String = "",
    val rotationLoadList: List<String> = emptyList(),
    val rotationLoadName: String = "",
) {
    val selectedChara = calcState.charaId
    val rotation = calcState.rotation
    val selectedRace = rotation.list
    val raceCount = selectedRace.count { it != null }
    val raceType = rotation.raceType
    val recommendation = calcState.recommendation.filter { recommendFilter(it) }
}

sealed interface RecommendFilter {
    operator fun invoke(entry: Triple<RaceEntry, Int, List<Pair<String, Int?>>>): Boolean
}

object NoFilter : RecommendFilter {
    override fun invoke(entry: Triple<RaceEntry, Int, List<Pair<String, Int?>>>) = true
    override fun toString() = "全て"
}

class TurnJustFilter(val turn: Int) : RecommendFilter {
    override fun invoke(entry: Triple<RaceEntry, Int, List<Pair<String, Int?>>>) = entry.first.turn == turn
    override fun toString() = turnToString(turn)
}

class TurnAfterFilter(val turn: Int) : RecommendFilter {
    override fun invoke(entry: Triple<RaceEntry, Int, List<Pair<String, Int?>>>) = entry.first.turn >= turn
    override fun toString() = turnToString(turn) + " 以降"
}

class AchievementFilter(val name: String) : RecommendFilter {
    override fun invoke(entry: Triple<RaceEntry, Int, List<Pair<String, Int?>>>) = entry.third.any { it.first == name }
    override fun toString() = name
}

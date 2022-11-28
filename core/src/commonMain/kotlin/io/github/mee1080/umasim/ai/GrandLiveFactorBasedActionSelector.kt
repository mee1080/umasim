/*
 * Copyright 2021 mee1080
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
package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

@Suppress("unused")
class GrandLiveFactorBasedActionSelector(val option: Option = Option()) : ActionSelector {

    companion object {
        private const val DEBUG = false
        private const val DEBUG_LESSON = false

        val speed2Power1Wisdom2Friend1 = Option().copy(
            speedFactor = 1.75,
            staminaFactor = 0.8,
            powerFactor = 1.25,
            gutsFactor = 0.25,
            wisdomFactor = 1.0,
            hpFactor = 0.85,
            motivationFactor = 25.0,
            relationFactor = { type, rank, _ ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        1 -> 6.8
                        else -> 15.0
                    }

                    StatusType.POWER -> 1.25

                    else -> when (rank) {
                        1 -> 10.7
                        else -> 14.5
                    }
                }
            }
        )

        val speed3Wisdom2Friend1 = Option().copy(
            speedFactor = 2.0,
            staminaFactor = 1.25,
            powerFactor = 1.4,
            gutsFactor = 0.25,
            wisdomFactor = 1.2,
            hpFactor = 1.2,
            motivationFactor = 25.0,
            relationFactor = { type, rank, _ ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        1 -> 8.3
                        2 -> 18.2
                        else -> 16.6
                    }

                    else -> when (rank) {
                        1 -> 15.0
                        else -> 18.6
                    }
                }
            }
        )

        val speed3Stamina1Wisdom1Friend1 = Option().copy(
            speedFactor = 1.59,
            staminaFactor = 1.89,
            powerFactor = 1.02,
            gutsFactor = 0.91,
            wisdomFactor = 0.88,
            skillPtFactor = 0.97,
            hpFactor = 1.24,
            motivationFactor = 25.0,
            relationFactor = { type, rank, _ ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        1 -> 11.1
                        2 -> 11.8
                        else -> 17.6
                    }

                    StatusType.STAMINA -> 3.53

                    StatusType.WISDOM -> 17.5

                    else -> 0.0
                }
            }
        )
    }

    @Serializable
    data class Option(
        val speedFactor: Double = 1.0,
        val staminaFactor: Double = 1.0,
        val powerFactor: Double = 1.0,
        val gutsFactor: Double = 0.4,
        val wisdomFactor: Double = 0.6,
        val skillPtFactor: Double = 0.4,
        val hpFactor: Double = 0.5,
        val motivationFactor: Double = 15.0,
        val relationFactor: (type: StatusType, rank: Int, count: Int) -> Double = { _: StatusType, rank: Int, count: Int ->
            when (count) {
                1 -> 3.0
                2 -> if (rank == 0) 5.5 else 7.0
                else -> when (rank) {
                    0 -> 7.0
                    1 -> 9.0
                    else -> 12.0
                }
            }
        },
        val performanceFactor: Double = 0.0,
    ) : ActionSelectorGenerator {
        override fun generateSelector() = GrandLiveFactorBasedActionSelector(this)
    }

    private var reservedLesson: Lesson? = null

    private var waitLesson: Boolean = false

    override fun init(state: SimulationState) {
        reservedLesson = null
        waitLesson = false
    }

    override fun select(state: SimulationState, selection: List<Action>): Action {
        return selection
            .filterNot { it is Race }
            .maxByOrNull { calcScore(state, it) } ?: selection.first()
    }

    override fun selectWithItem(state: SimulationState, selection: List<Action>): SelectedAction {
        val liveStatus = state.liveStatus
        if (liveStatus != null) {
            if (DEBUG_LESSON) println("${state.turn}: ${state.status.performance} ${liveStatus.lessonSelection.joinToString { it.displayName }}")
            if (reservedLesson == null && !waitLesson) {
                waitLesson = checkWaitLesson(state)
                if (DEBUG_LESSON && waitLesson) println("${state.turn}: wait lesson ${liveStatus.lessonSelection.first().displayName} <- ${liveStatus.newLesson.joinToString { it.displayName }}")
                reservedLesson = if (waitLesson) null else if (liveStatus.lessonSelection.first() is SongLesson) {
                    // 楽曲の場合最も評価の高いものを予約
                    liveStatus.lessonSelection.maxByOrNull { calcLessonScore(state, it) }
                } else {
                    // 楽曲以外はその場で選択
                    selectLessonImmediately(state)
                }
            }
            reservedLesson?.let { lesson ->
                val rest = state.status.performance!! - lesson.cost
                if (rest.valid) {
                    if (DEBUG_LESSON) println("${state.turn}: in turn purchase ${lesson.displayName}")
                    reservedLesson = null
                    return SelectedAction(lesson = lesson)
                }
                if (DEBUG_LESSON) println("${state.turn}: reserved ${lesson.displayName} ${state.status.performance}")
            }
        }
        return SelectedAction(action = select(state, selection))
    }

    override fun selectBeforeLiveLesson(state: SimulationState): Lesson? {
        if (waitLesson) {
            waitLesson = false
            return null
        }
        reservedLesson = null
        if (DEBUG_LESSON) println("${state.turn}: ${state.status.performance} ${state.liveStatus!!.lessonSelection.joinToString { it.displayName }}")
        if (checkWaitLesson(state)) {
            if (DEBUG_LESSON) println("${state.turn}: wait lesson ${state.liveStatus!!.lessonSelection.first().displayName} <- ${state.liveStatus.newLesson.joinToString { it.displayName }}")
            return null
        }
        val lesson = selectLessonImmediately(state)
        if (lesson != null) {
            if (DEBUG_LESSON) println("${state.turn}: before live purchase ${lesson.displayName}")
        }
        return lesson
    }

    private fun selectLessonImmediately(state: SimulationState): Lesson? {
        val liveStatus = state.liveStatus ?: return null
        val period = LivePeriod.turnToPeriod(state.turn)
        return liveStatus.lessonSelection.mapNotNull {
            val rest = (state.status.performance!! - it.cost)
            if (rest.valid) it to rest else null
        }.minByOrNull {
            calcLessonScore(state, it.first) + calcLessonCostScore(it.second, period.lessonPeriod.baseCost)
        }?.first
    }

    private fun calcLessonScore(state: SimulationState, lesson: Lesson): Int {
        return if (lesson is SongLesson) {
            when (lesson.liveBonus) {
                LiveBonus.FriendTraining10 -> 40000
                LiveBonus.FriendTraining5 -> 30000
                else -> {
                    val learnBonus = lesson.learnBonus
                    if (learnBonus is TrainingBonus) {
                        when (learnBonus.type) {
                            StatusType.SPEED -> 18000 * learnBonus.value
                            StatusType.SKILL -> 10000 * learnBonus.value
                            StatusType.POWER -> 500
                            else -> 0
                        }
                    } else 0
                }
            }
        } else {
            when (val learnBonus = lesson.learnBonus) {
                is StatusBonus -> if (learnBonus.status.hp > 0) return 1000 else 0
                else -> 0
            }
        }
    }

    private fun calcLessonCostScore(rest: Performance, baseCost: Int): Int {
        return rest.countOver(baseCost) * 1000 + rest.totalValue
    }

    private fun checkWaitLesson(state: SimulationState): Boolean {
        val liveStatus = state.liveStatus ?: return false
        return when (LivePeriod.turnToPeriod(state.turn)) {
            LivePeriod.Junior -> {
                return liveStatus.lessonSelection.first() is SongLesson && liveStatus.newSongCount >= 4 && liveStatus.friendTrainingUpAfterLive >= 10
            }

            LivePeriod.Classic1 -> {
                return liveStatus.lessonSelection.first() is SongLesson && liveStatus.newSongCount >= 3 && liveStatus.friendTrainingUpAfterLive >= 15
            }

            LivePeriod.Classic2 -> {
                return liveStatus.newSongCount >= 3 && liveStatus.friendTrainingUpAfterLive >= 15
            }

            LivePeriod.Senior2 -> {
                return LessonProvider.isSong(
                    LivePeriod.Senior2,
                    liveStatus.lessonCount + 1
                ) && liveStatus.friendTrainingUpAfterLive >= 55
            }

            else -> false
        }
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (DEBUG) println("${state.turn}: $action")
        val total = action.resultCandidate.sumOf { it.second }.toDouble()
        val score = action.resultCandidate.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            (calcScore(calcExpectedHintStatus(action) + it.first)) * it.second / total
        } + calcRelationScore(state, action)
        if (DEBUG) println("total $score")
        return score
    }

    private fun calcExpectedHintStatus(action: Action): ExpectedStatus {
        if (action !is Training) return ExpectedStatus()
        val target = action.member.filter { it.hint }.map { it.card.hintStatus }
        if (target.isEmpty()) return ExpectedStatus()
        val rate = 1.0 / target.size
        return target.fold(ExpectedStatus()) { acc, status -> acc.add(rate, status) }
    }

    private fun calcScore(status: StatusValues): Double {
        val score = status.speed.toDouble() * option.speedFactor +
                status.stamina.toDouble() * option.staminaFactor +
                status.power.toDouble() * option.powerFactor +
                status.guts.toDouble() * option.gutsFactor +
                status.wisdom.toDouble() * option.wisdomFactor +
                status.skillPt.toDouble() * option.skillPtFactor +
                status.hp.toDouble() * option.hpFactor +
                status.motivation.toDouble() * option.motivationFactor +
                status.performanceValue.toDouble() * option.performanceFactor
        if (DEBUG) println("  $score $status")
        return score
    }

    private fun calcRelationScore(state: SimulationState, action: Action): Double {
        if (action !is Training) return 0.0
        val supportRank = mutableMapOf<StatusType, MutableList<Pair<MemberState, Int>>>()
        state.support.forEach {
            supportRank.getOrPut(it.card.type) { mutableListOf() }.add(it to it.relation)
        }
        supportRank.values.forEach { list -> list.sortByDescending { it.second } }
        val score = action.support.sumOf { support ->
            if (support.relation >= support.card.requiredRelation) return@sumOf 0.0
            val list = supportRank[support.card.type]!!
            val rank = list.indexOfFirst { it.first == support }
            option.relationFactor(support.card.type, rank, list.size)
        }
        if (DEBUG) println("  relation $score")
        return score
    }

    override fun toString(): String {
        return "ClimaxFactorBasedActionSelector $option"
    }
}
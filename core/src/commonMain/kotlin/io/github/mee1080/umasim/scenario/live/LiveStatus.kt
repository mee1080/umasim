/*
 * Copyright 2022 mee1080
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
package io.github.mee1080.umasim.scenario.live

import io.github.mee1080.umasim.data.RatedSet
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.simulation2.ScenarioStatus

interface TrainingLiveStatus : ScenarioStatus {
    fun trainingUp(type: StatusType): Int
    val friendTrainingUp: Int
    val specialityRateUp: Int
}

data class LiveStatus(
    val currentPeriod: LivePeriod = LivePeriod.Junior,
    val lessonCount: Int = 1,
    val learnedLesson: List<Lesson> = emptyList(),
    val livedLesson: List<Lesson> = emptyList(),
    val lessonSelection: List<Lesson> = emptyList(),
) : ScenarioStatus, TrainingLiveStatus {
    private val learnedBonusList by lazy { learnedLesson.map { it.learnBonus } }

    private val livedBonusList by lazy { livedLesson.mapNotNull { it.liveBonus } }

    val learnedSongs by lazy { learnedLesson.mapNotNull { it as? SongLesson } }

    val newLesson by lazy { learnedLesson.subList(livedLesson.size + 1, learnedLesson.size) }

    val newSongCount by lazy { newLesson.count { it is SongLesson } }

    override fun trainingUp(type: StatusType): Int {
        return learnedBonusList.mapNotNull { it as? TrainingBonus }.sumOf { it.valueOf(type) }
    }

    override val friendTrainingUp by lazy { livedBonusList.sumOf { it.friendTraining } }

    val friendTrainingUpAfterLive by lazy { friendTrainingUp + newLesson.sumOf { it.liveBonus?.friendTraining ?: 0 } }

    override val specialityRateUp by lazy { livedBonusList.sumOf { it.specialityRate } }

    val specialityRateUpAfterLive by lazy { friendTrainingUp + newLesson.sumOf { it.liveBonus?.specialityRate ?: 0 } }
}

enum class LivePeriod(val lessonPeriod: LessonPeriod) {
    Junior(LessonPeriod.Junior),
    Classic1(LessonPeriod.Classic),
    Classic2(LessonPeriod.Classic),
    Senior1(LessonPeriod.Senior),
    Senior2(LessonPeriod.Senior),
    Finals(LessonPeriod.Senior);

    companion object {
        fun turnToPeriod(turn: Int) = when {
            turn <= 24 -> Junior
            turn <= 36 -> Classic1
            turn <= 48 -> Classic2
            turn <= 60 -> Senior1
            turn <= 72 -> Senior2
            else -> Finals
        }
    }
}

enum class PerformanceType(
    val asPerformance: (Int) -> Performance,
    val getValue: (Performance) -> Int,
) {
    Dance({ Performance(dance = it) }, { it.dance }),
    Passion({ Performance(passion = it) }, { it.passion }),
    Vocal({ Performance(vocal = it) }, { it.vocal }),
    Visual({ Performance(visual = it) }, { it.visual }),
    Mental({ Performance(mental = it) }, { it.mental }),
}

data class Performance(
    val dance: Int = 0,
    val passion: Int = 0,
    val vocal: Int = 0,
    val visual: Int = 0,
    val mental: Int = 0,
) {

    operator fun plus(other: Performance?): Performance {
        return if (other == null) this else Performance(
            dance + other.dance,
            passion + other.passion,
            vocal + other.vocal,
            visual + other.visual,
            mental + other.mental,
        )
    }

    operator fun minus(other: Performance?): Performance {
        return if (other == null) this else Performance(
            dance - other.dance,
            passion - other.passion,
            vocal - other.vocal,
            visual - other.visual,
            mental - other.mental,
        )
    }

    operator fun unaryMinus() = Performance(-dance, -passion, -vocal, -visual, -mental)

    val totalValue by lazy { dance + passion + vocal + visual + mental }

    fun countOver(value: Int) = (if (dance >= value) 1 else 0) + (if (passion >= value) 1 else 0) +
            (if (vocal >= value) 1 else 0) + (if (visual >= value) 1 else 0) + (if (mental >= value) 1 else 0)

    val valid by lazy { dance >= 0 && passion >= 0 && vocal >= 0 && visual >= 0 && mental >= 0 }

    val minimumType by lazy {
        val minimumValue = PerformanceType.entries.minOf { it.getValue(this) }
        PerformanceType.entries.filter { it.getValue(this) == minimumValue }
    }
}

enum class LessonPeriod(
    val displayName: String,
    val baseCost: Int,
) {
    Junior("ジュニア", 10),
    Classic("クラシック", 16),
    Senior("シニア/ファイナルズ", 24),
}

sealed interface Lesson {
    val displayName: String
    val cost: Performance
    val learnBonus: LearnBonus
    val liveBonus: LiveBonus?
}

data class SongLesson(
    val name: String,
    override val cost: Performance,
    override val learnBonus: LearnBonus,
    override val liveBonus: LiveBonus,
    val specialSong: Boolean = false,
) : Lesson {
    override val displayName get() = "楽曲:$name"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SongLesson

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

enum class TechniqueLessonCategory(
    val single: Boolean = true
) {
    Status(single = false), DualStatus, SkillPt, SkillHint, Rest,
}

data class TechniqueLesson(
    override val cost: Performance,
    override val learnBonus: TechniqueLearnBonus,
) : Lesson {

    constructor(cost: Performance, status: Status) : this(cost, StatusBonus(status))

    val category get() = learnBonus.category
    val level get() = learnBonus.level
    override val displayName get() = "テクニック:${learnBonus.displayName}"
    override val liveBonus: LiveBonus? = null
}

sealed interface LearnBonus {
    val displayName: String
}

sealed interface TechniqueLearnBonus : LearnBonus {
    val category: TechniqueLessonCategory
    val level: Int
}

data class PerformanceBonus(
    val performance: Performance,
) : LearnBonus {
    override val displayName: String get() = "すべてのパフォーマンス +10"
}

data class StatusBonus(
    val status: Status,
) : TechniqueLearnBonus {

    override val category by lazy {
        if (status.hp > 0) {
            TechniqueLessonCategory.Rest
        } else if (status.skillPt > 0) {
            if (status.countOver(1) == 0) {
                TechniqueLessonCategory.SkillPt
            } else {
                TechniqueLessonCategory.DualStatus
            }
        } else {
            if (status.countOver(1) == 1) {
                TechniqueLessonCategory.Status
            } else {
                TechniqueLessonCategory.DualStatus
            }
        }
    }

    override val displayName by lazy {
        if (category == TechniqueLessonCategory.Rest) {
            "体力+${status.hp}"
        } else {
            (trainingType + StatusType.SKILL).mapNotNull {
                val value = status.get(it)
                if (value == 0) null else "${it.displayName}+$value"
            }.joinToString(",")
        }
    }

    override val level by lazy {
        if (category == TechniqueLessonCategory.Rest) {
            when (status.hp) {
                40 -> 3
                30 -> 2
                else -> 1
            }
        } else 1
    }
}

data class SkillHintBonus(
    override val level: Int,
) : TechniqueLearnBonus {
    override val category get() = TechniqueLessonCategory.SkillHint
    override val displayName by lazy { "スキルヒントLv+$level" }
}

data class TrainingBonus(
    val type: StatusType,
    val value: Int,
) : LearnBonus {
    override val displayName get() = "トレーニングの${type.displayName}上昇量+$value"
    fun valueOf(targetType: StatusType) = if (targetType == type) value else 0
}

enum class LiveBonus(
    val displayName: String,
    val friendTraining: Int = 0,
    val specialityRate: Int = 0,
) {
    ContinuousEvent("連続イベ率アップLv+1"),
    FriendTraining5("友情トレ獲得量+5%", friendTraining = 5),
    FriendTraining10("友情トレ獲得量+10%", friendTraining = 10),
    SpecialtyRate("得意率+5", specialityRate = 5),
}

class TechniqueLessonSet(
    list: List<Pair<TechniqueLesson, Int>>,
) : RatedSet<TechniqueLesson>(list) {
    companion object {
        private val cache = mutableMapOf<List<Pair<TechniqueLesson, Int>>, TechniqueLessonSet>()
    }

    init {
        cache[list] = this
    }

    override fun getInstance(newList: List<Pair<TechniqueLesson, Int>>): RatedSet<TechniqueLesson> {
        return cache.getOrPut(newList) { TechniqueLessonSet(newList) }
    }
}
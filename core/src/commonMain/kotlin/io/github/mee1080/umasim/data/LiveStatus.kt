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
package io.github.mee1080.umasim.data

interface TrainingLiveStatus {
    fun trainingUp(type: StatusType): Int
    val friendTrainingUp: Int
    val specialityRateUp: Int
}

data class LiveStatus(
    val learnedLesson: List<Lesson> = emptyList(),
    val livedLesson: List<Lesson> = emptyList(),
    val lessonSelection: List<Lesson> = emptyList(),
) : TrainingLiveStatus {
    private val learnedBonusList by lazy { learnedLesson.map { it.learnBonus } }

    private val livedBonusList by lazy { livedLesson.mapNotNull { it.liveBonus } }

    fun applyLive() = copy(
        livedLesson = livedLesson + learnedLesson.subList(livedLesson.size + 1, learnedLesson.size)
    )

    override fun trainingUp(type: StatusType): Int {
        return learnedBonusList.mapNotNull { it as? TrainingBonus }.sumOf { it.valueOf(type) }
    }

    override val friendTrainingUp by lazy { livedBonusList.count { it == LiveBonus.FriendTraining } * 5 }

    override val specialityRateUp by lazy { livedBonusList.count { it == LiveBonus.SpecialtyRate } * 5 }
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

    val totalValue by lazy { dance + passion + vocal + visual + mental }

    fun countOver(value: Int) = (if (dance >= value) 1 else 0) + (if (passion >= value) 1 else 0) +
            (if (vocal >= value) 1 else 0) + (if (visual >= value) 1 else 0) + (if (mental >= value) 1 else 0)

    val valid by lazy { dance >= 0 && passion >= 0 && vocal >= 0 && visual >= 0 && mental >= 0 }
}

enum class LessonPeriod(val displayName: String) {
    Junior("ジュニア"),
    Classic("クラシック"),
    Senior("シニア/ファイナルズ"),
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
) : Lesson {
    override val displayName get() = "楽曲:$name"
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
) {
    ContinuousEvent("連続イベ率アップLv+1"),
    FriendTraining("友情トレ獲得量+5%"),
    SpecialtyRate("得意率+5"),
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
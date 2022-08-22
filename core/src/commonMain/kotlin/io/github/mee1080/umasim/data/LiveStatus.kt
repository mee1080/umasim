package io.github.mee1080.umasim.data

interface TrainingLiveStatus {
    fun trainingUp(type: StatusType): Int
    val friendTrainingUp: Int
    val specialityRateUp: Int
}

data class LiveStatus(
    val performance: Performance = Performance(),

    val liveExpectations: Int = 0,

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
)

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

data class TechniqueLesson(
    val name: String,
    override val cost: Performance,
    override val learnBonus: LearnBonus,
) : Lesson {
    override val displayName get() = "テクニック:$name"
    override val liveBonus: LiveBonus? = null
}

sealed interface LearnBonus {
    val displayName: String
}

data class StatusUpBonus(
    val list: List<Pair<StatusType, Int>>,
) : LearnBonus {
    override val displayName get() = list.joinToString(",") { "${it.first.displayName}+${it.second}" }
}

data class DistanceSkillHintBonus(
    val target: RaceDistance,
    val value: Int,
) : LearnBonus {
    override val displayName get() = "<${target.displayName}>のスキルヒントLv+$value"
}

data class RunningStyleSkillHintBonus(
    val target: RaceRunningStyle,
    val value: Int,
) : LearnBonus {
    override val displayName get() = "<${target.displayName}>のスキルヒントLv+$value"
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
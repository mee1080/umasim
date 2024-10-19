package io.github.mee1080.umasim.scenario.live

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store.supportList
import io.github.mee1080.umasim.data.TrainingBase
import io.github.mee1080.umasim.data.toSupportType
import io.github.mee1080.umasim.scenario.Scenario

class LiveStore {
    private val notLinkSupportList by lazy {
        supportList.filter {
            it.rarity == 1 && it.talent == 0 && !it.type.outingType && !Scenario.GRAND_LIVE.scenarioLink.contains(
                it.chara
            )
        }
    }

    fun getShuffledGuest() = notLinkSupportList.shuffled()
}

val firstPerformanceRate = mapOf(
    StatusType.SPEED to listOf(
        PerformanceType.Dance to 65,
        PerformanceType.Passion to 3,
        PerformanceType.Vocal to 3,
        PerformanceType.Visual to 26,
        PerformanceType.Mental to 3,
    ),
    StatusType.STAMINA to listOf(
        PerformanceType.Dance to 3,
        PerformanceType.Passion to 65,
        PerformanceType.Vocal to 26,
        PerformanceType.Visual to 3,
        PerformanceType.Mental to 3,
    ),
    StatusType.POWER to listOf(
        PerformanceType.Dance to 3,
        PerformanceType.Passion to 3,
        PerformanceType.Vocal to 65,
        PerformanceType.Visual to 3,
        PerformanceType.Mental to 26,
    ),
    StatusType.GUTS to listOf(
        PerformanceType.Dance to 26,
        PerformanceType.Passion to 3,
        PerformanceType.Vocal to 3,
        PerformanceType.Visual to 65,
        PerformanceType.Mental to 3,
    ),
    StatusType.WISDOM to listOf(
        PerformanceType.Dance to 3,
        PerformanceType.Passion to 26,
        PerformanceType.Vocal to 3,
        PerformanceType.Visual to 3,
        PerformanceType.Mental to 65,
    ),
)

val specialSongs = listOf(
    SongLesson(
        "Make debut!",
        Performance(),
        PerformanceBonus(Performance(10, 10, 10, 10, 10)),
        LiveBonus.SpecialtyRate,
        true,
    ),
    SongLesson(
        "GIRLS' LEGEND U (通常)",
        Performance(),
        StatusBonus(Status(10, 10, 10, 10, 10)),
        LiveBonus.FriendTraining10,
        true,
    ),
    SongLesson(
        "GIRLS' LEGEND U (特別)",
        Performance(),
        StatusBonus(Status(10, 10, 10, 10, 10)),
        LiveBonus.FriendTraining10,
        true,
    ),
)

val liveSongLesson by lazy {
    val junior = listOf(
        SongLesson(
            "青春が待ってる",
            Performance(vocal = 32, mental = 12),
            StatusBonus(Status(power = 22)),
            LiveBonus.FriendTraining5,
        ),
        SongLesson(
            "全速！前進！ウマドルパワー☆",
            Performance(dance = 32, visual = 12),
            StatusBonus(Status(speed = 22)),
            LiveBonus.FriendTraining5,
        ),
        SongLesson(
            "RUN×RUN！",
            Performance(dance = 14, visual = 16, mental = 14),
            StatusBonus(Status(skillPt = 22)),
            LiveBonus.FriendTraining5,
        ),
        SongLesson(
            "Go This Way",
            Performance(vocal = 21, mental = 21),
            TrainingBonus(StatusType.POWER, 1),
            LiveBonus.ContinuousEvent,
        ),
        SongLesson(
            "奇跡を信じて！",
            Performance(passion = 21, mental = 21),
            TrainingBonus(StatusType.WISDOM, 1),
            LiveBonus.SpecialtyRate,
        ),
        SongLesson(
            "逃げ切りっ！Fallin' Love",
            Performance(vocal = 21, visual = 21),
            TrainingBonus(StatusType.GUTS, 1),
            LiveBonus.ContinuousEvent,
        ),
        SongLesson(
            "Ring Ring ダイアリー",
            Performance(passion = 21, visual = 21),
            TrainingBonus(StatusType.STAMINA, 1),
            LiveBonus.ContinuousEvent,
        ),
        SongLesson(
            "立ち位置ゼロ番！順位は一番！",
            Performance(dance = 21, visual = 21),
            TrainingBonus(StatusType.SPEED, 1),
            LiveBonus.ContinuousEvent,
        ),
    )
    val classic1 = listOf(
        SongLesson(
            "ユメヲカケル！",
            Performance(passion = 21, visual = 21),
            TrainingBonus(StatusType.SKILL, 2),
            LiveBonus.SpecialtyRate,
        ),
        SongLesson(
            "ぼくらのブルーバードデイズ",
            Performance(dance = 21, visual = 42),
            TrainingBonus(StatusType.SPEED, 2),
            LiveBonus.SpecialtyRate,
        ),
        SongLesson(
            "A・NO・NE",
            Performance(dance = 42, visual = 21),
            TrainingBonus(StatusType.GUTS, 2),
            LiveBonus.SpecialtyRate,
        ),
    )
    val classic2 = listOf(
        SongLesson(
            "グロウアップ・シャイン！",
            Performance(dance = 21, vocal = 21, mental = 21),
            TrainingBonus(StatusType.SKILL, 3),
            LiveBonus.ContinuousEvent,
        ),
        SongLesson(
            "木漏れ日のエール",
            Performance(passion = 42, mental = 21),
            TrainingBonus(StatusType.WISDOM, 2),
            LiveBonus.ContinuousEvent,
        ),
        SongLesson(
            "七色の景色",
            Performance(vocal = 21, mental = 42),
            TrainingBonus(StatusType.POWER, 2),
            LiveBonus.SpecialtyRate,
        ),
        SongLesson(
            "ぴょいっと♪はれるや！",
            Performance(passion = 42, vocal = 21),
            TrainingBonus(StatusType.STAMINA, 2),
            LiveBonus.SpecialtyRate,
        ),
    )
    val senior = listOf(
        SongLesson(
            "世界は僕らの言いなりさ",
            Performance(passion = 32, vocal = 12),
            StatusBonus(Status(stamina = 22)),
            LiveBonus.FriendTraining5,
        ),
        SongLesson(
            "春空BLUE",
            Performance(dance = 12, visual = 32),
            StatusBonus(Status(guts = 22)),
            LiveBonus.FriendTraining5,
        ),
        SongLesson(
            "ユメゾラ",
            Performance(passion = 22, mental = 22),
            StatusBonus(Status(wisdom = 22)),
            LiveBonus.FriendTraining5,
        ),
        SongLesson(
            "PRESENT MARCH♪",
            Performance(vocal = 22, mental = 22),
            StatusBonus(Status(power = 22)),
            LiveBonus.FriendTraining5,
        ),
        SongLesson(
            "Fanfare for Future!",
            Performance(dance = 26, visual = 42),
            StatusBonus(Status(guts = 26)),
            LiveBonus.FriendTraining10,
        ),
        SongLesson(
            "大好きのタカラバコ",
            Performance(dance = 42, visual = 26),
            StatusBonus(Status(speed = 26)),
            LiveBonus.FriendTraining10,
        ),
    )
    mapOf(
        LivePeriod.Junior to junior,
        LivePeriod.Classic1 to junior + classic1,
        LivePeriod.Classic2 to junior + classic1 + classic2,
        LivePeriod.Senior1 to junior + classic1 + classic2 + senior,
        LivePeriod.Senior2 to junior + classic1 + classic2 + senior,
        LivePeriod.Finals to emptyList(),
    )
}

val liveTechniqueLesson by lazy {
    mapOf(
        LessonPeriod.Junior to listOf(
            TechniqueLesson(Performance(dance = 10), Status(speed = 5)),
            TechniqueLesson(Performance(passion = 10), Status(stamina = 5)),
            TechniqueLesson(Performance(vocal = 10), Status(power = 5)),
            TechniqueLesson(Performance(visual = 10), Status(guts = 5)),
            TechniqueLesson(Performance(mental = 10), Status(wisdom = 5)),

            TechniqueLesson(Performance(dance = 10), Status(skillPt = 5)),
            TechniqueLesson(Performance(passion = 10), Status(skillPt = 5)),
            TechniqueLesson(Performance(vocal = 10), Status(skillPt = 5)),
            TechniqueLesson(Performance(visual = 10), Status(skillPt = 5)),
            TechniqueLesson(Performance(mental = 10), Status(skillPt = 5)),

            TechniqueLesson(Performance(dance = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(passion = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(vocal = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(visual = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(mental = 15), SkillHintBonus(1)),

            TechniqueLesson(Performance(dance = 25), Status(hp = 20)),
            TechniqueLesson(Performance(passion = 25), Status(hp = 20)),
            TechniqueLesson(Performance(vocal = 25), Status(hp = 20)),
            TechniqueLesson(Performance(visual = 25), Status(hp = 20)),
            TechniqueLesson(Performance(mental = 25), Status(hp = 20)),

            TechniqueLesson(Performance(dance = 30), Status(hp = 30)),
            TechniqueLesson(Performance(passion = 30), Status(hp = 30)),
            TechniqueLesson(Performance(vocal = 30), Status(hp = 30)),
            TechniqueLesson(Performance(visual = 30), Status(hp = 30)),
            TechniqueLesson(Performance(mental = 30), Status(hp = 30)),
        ).groupBy { it.category }.mapValues { entry ->
            TechniqueLessonSet(entry.value.map {
                it to when (it.category) {
                    TechniqueLessonCategory.Rest -> when (it.level) {
                        1 -> 60
                        else -> 40
                    }

                    else -> 1
                }
            })
        },
        LessonPeriod.Classic to listOf(
            TechniqueLesson(Performance(dance = 16), Status(speed = 8)),
            TechniqueLesson(Performance(passion = 16), Status(stamina = 8)),
            TechniqueLesson(Performance(vocal = 16), Status(power = 8)),
            TechniqueLesson(Performance(visual = 16), Status(guts = 8)),
            TechniqueLesson(Performance(mental = 16), Status(wisdom = 8)),

            TechniqueLesson(Performance(dance = 16), Status(skillPt = 8)),
            TechniqueLesson(Performance(passion = 16), Status(skillPt = 8)),
            TechniqueLesson(Performance(vocal = 16), Status(skillPt = 8)),
            TechniqueLesson(Performance(visual = 16), Status(skillPt = 8)),
            TechniqueLesson(Performance(mental = 16), Status(skillPt = 8)),

            TechniqueLesson(Performance(dance = 8, passion = 8), Status(speed = 4, stamina = 4)),
            TechniqueLesson(Performance(dance = 8, vocal = 8), Status(speed = 4, power = 4)),
            TechniqueLesson(Performance(dance = 8, visual = 8), Status(speed = 4, guts = 4)),
            TechniqueLesson(Performance(dance = 8, mental = 8), Status(speed = 4, wisdom = 4)),
            TechniqueLesson(Performance(dance = 10, visual = 6), Status(speed = 4, skillPt = 4)),
            TechniqueLesson(Performance(passion = 8, vocal = 8), Status(stamina = 4, power = 4)),
            TechniqueLesson(Performance(passion = 8, visual = 8), Status(stamina = 4, guts = 4)),
            TechniqueLesson(Performance(passion = 8, mental = 8), Status(stamina = 4, wisdom = 4)),
            TechniqueLesson(Performance(passion = 10, vocal = 6), Status(stamina = 4, skillPt = 4)),
            TechniqueLesson(Performance(vocal = 8, visual = 8), Status(power = 4, guts = 4)),
            TechniqueLesson(Performance(vocal = 8, mental = 8), Status(power = 4, wisdom = 4)),
            TechniqueLesson(Performance(vocal = 10, mental = 6), Status(power = 4, skillPt = 4)),
            TechniqueLesson(Performance(visual = 8, mental = 8), Status(guts = 4, wisdom = 4)),
            TechniqueLesson(Performance(visual = 10, dance = 6), Status(guts = 4, skillPt = 4)),
            TechniqueLesson(Performance(mental = 10, passion = 6), Status(wisdom = 4, skillPt = 4)),

            TechniqueLesson(Performance(dance = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(passion = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(vocal = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(visual = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(mental = 15), SkillHintBonus(1)),

            TechniqueLesson(Performance(dance = 25), SkillHintBonus(2)),
            TechniqueLesson(Performance(passion = 25), SkillHintBonus(2)),
            TechniqueLesson(Performance(vocal = 25), SkillHintBonus(2)),
            TechniqueLesson(Performance(visual = 25), SkillHintBonus(2)),
            TechniqueLesson(Performance(mental = 25), SkillHintBonus(2)),

            TechniqueLesson(Performance(dance = 25), Status(hp = 20)),
            TechniqueLesson(Performance(passion = 25), Status(hp = 20)),
            TechniqueLesson(Performance(vocal = 25), Status(hp = 20)),
            TechniqueLesson(Performance(visual = 25), Status(hp = 20)),
            TechniqueLesson(Performance(mental = 25), Status(hp = 20)),

            TechniqueLesson(Performance(dance = 30), Status(hp = 30)),
            TechniqueLesson(Performance(passion = 30), Status(hp = 30)),
            TechniqueLesson(Performance(vocal = 30), Status(hp = 30)),
            TechniqueLesson(Performance(visual = 30), Status(hp = 30)),
            TechniqueLesson(Performance(mental = 30), Status(hp = 30)),

            TechniqueLesson(Performance(dance = 35), Status(hp = 40)),
            TechniqueLesson(Performance(passion = 35), Status(hp = 40)),
            TechniqueLesson(Performance(vocal = 35), Status(hp = 40)),
            TechniqueLesson(Performance(visual = 35), Status(hp = 40)),
            TechniqueLesson(Performance(mental = 35), Status(hp = 40)),
        ).groupBy { it.category }.mapValues { entry ->
            TechniqueLessonSet(entry.value.map {
                it to when (it.category) {
                    TechniqueLessonCategory.Rest -> when (it.level) {
                        1 -> 55
                        2 -> 40
                        else -> 5
                    }

                    TechniqueLessonCategory.SkillHint -> when (it.level) {
                        1 -> 60
                        else -> 40
                    }

                    else -> 1
                }
            })
        },
        LessonPeriod.Senior to listOf(
            TechniqueLesson(Performance(dance = 24), Status(speed = 12)),
            TechniqueLesson(Performance(passion = 24), Status(stamina = 12)),
            TechniqueLesson(Performance(vocal = 24), Status(power = 12)),
            TechniqueLesson(Performance(visual = 24), Status(guts = 12)),
            TechniqueLesson(Performance(mental = 24), Status(wisdom = 12)),

            TechniqueLesson(Performance(dance = 24), Status(skillPt = 12)),
            TechniqueLesson(Performance(passion = 24), Status(skillPt = 12)),
            TechniqueLesson(Performance(vocal = 24), Status(skillPt = 12)),
            TechniqueLesson(Performance(visual = 24), Status(skillPt = 12)),
            TechniqueLesson(Performance(mental = 24), Status(skillPt = 12)),

            TechniqueLesson(Performance(dance = 12, passion = 12), Status(speed = 6, stamina = 6)),
            TechniqueLesson(Performance(dance = 12, vocal = 12), Status(speed = 6, power = 6)),
            TechniqueLesson(Performance(dance = 12, visual = 12), Status(speed = 6, guts = 6)),
            TechniqueLesson(Performance(dance = 12, mental = 12), Status(speed = 6, wisdom = 6)),
            TechniqueLesson(Performance(dance = 14, visual = 10), Status(speed = 6, skillPt = 6)),
            TechniqueLesson(Performance(passion = 12, vocal = 12), Status(stamina = 6, power = 6)),
            TechniqueLesson(Performance(passion = 12, visual = 12), Status(stamina = 6, guts = 6)),
            TechniqueLesson(Performance(passion = 12, mental = 12), Status(stamina = 6, wisdom = 6)),
            TechniqueLesson(Performance(passion = 14, vocal = 10), Status(stamina = 6, skillPt = 6)),
            TechniqueLesson(Performance(vocal = 12, visual = 12), Status(power = 6, guts = 6)),
            TechniqueLesson(Performance(vocal = 12, mental = 12), Status(power = 6, wisdom = 6)),
            TechniqueLesson(Performance(vocal = 14, mental = 10), Status(power = 6, skillPt = 6)),
            TechniqueLesson(Performance(visual = 12, mental = 12), Status(guts = 6, wisdom = 6)),
            TechniqueLesson(Performance(visual = 14, dance = 10), Status(guts = 6, skillPt = 6)),
            TechniqueLesson(Performance(mental = 14, passion = 10), Status(wisdom = 6, skillPt = 6)),

            TechniqueLesson(Performance(dance = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(passion = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(vocal = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(visual = 15), SkillHintBonus(1)),
            TechniqueLesson(Performance(mental = 15), SkillHintBonus(1)),

            TechniqueLesson(Performance(dance = 25), SkillHintBonus(2)),
            TechniqueLesson(Performance(passion = 25), SkillHintBonus(2)),
            TechniqueLesson(Performance(vocal = 25), SkillHintBonus(2)),
            TechniqueLesson(Performance(visual = 25), SkillHintBonus(2)),
            TechniqueLesson(Performance(mental = 25), SkillHintBonus(2)),

            TechniqueLesson(Performance(dance = 30), SkillHintBonus(3)),
            TechniqueLesson(Performance(passion = 30), SkillHintBonus(3)),
            TechniqueLesson(Performance(vocal = 30), SkillHintBonus(3)),
            TechniqueLesson(Performance(visual = 30), SkillHintBonus(3)),
            TechniqueLesson(Performance(mental = 30), SkillHintBonus(3)),

            TechniqueLesson(Performance(dance = 25), Status(hp = 20)),
            TechniqueLesson(Performance(passion = 25), Status(hp = 20)),
            TechniqueLesson(Performance(vocal = 25), Status(hp = 20)),
            TechniqueLesson(Performance(visual = 25), Status(hp = 20)),
            TechniqueLesson(Performance(mental = 25), Status(hp = 20)),

            TechniqueLesson(Performance(dance = 30), Status(hp = 30)),
            TechniqueLesson(Performance(passion = 30), Status(hp = 30)),
            TechniqueLesson(Performance(vocal = 30), Status(hp = 30)),
            TechniqueLesson(Performance(visual = 30), Status(hp = 30)),
            TechniqueLesson(Performance(mental = 30), Status(hp = 30)),

            TechniqueLesson(Performance(dance = 35), Status(hp = 40)),
            TechniqueLesson(Performance(passion = 35), Status(hp = 40)),
            TechniqueLesson(Performance(vocal = 35), Status(hp = 40)),
            TechniqueLesson(Performance(visual = 35), Status(hp = 40)),
            TechniqueLesson(Performance(mental = 35), Status(hp = 40)),
        ).groupBy { it.category }.mapValues { entry ->
            TechniqueLessonSet(entry.value.map {
                it to when (it.category) {
                    TechniqueLessonCategory.Rest -> when (it.level) {
                        1 -> 55
                        2 -> 40
                        else -> 5
                    }

                    TechniqueLessonCategory.SkillHint -> when (it.level) {
                        1 -> 40
                        2 -> 30
                        else -> 25
                    }

                    else -> 1
                }
            })
        },
    )
}

val liveTechniqueCategoryRate = mapOf(
    LessonPeriod.Junior to arrayOf(
        listOf(
            TechniqueLessonCategory.Status to 80,
            TechniqueLessonCategory.SkillPt to 6,
            TechniqueLessonCategory.SkillHint to 12,
            TechniqueLessonCategory.Rest to 2,
        ),
        listOf(
            TechniqueLessonCategory.Status to 23,
            TechniqueLessonCategory.SkillPt to 2,
            TechniqueLessonCategory.SkillHint to 73,
            TechniqueLessonCategory.Rest to 2,
        ),
        listOf(
            TechniqueLessonCategory.Status to 55,
            TechniqueLessonCategory.SkillPt to 7,
            TechniqueLessonCategory.SkillHint to 27,
            TechniqueLessonCategory.Rest to 11,
        ),
    ),
    LessonPeriod.Classic to arrayOf(
        listOf(
            TechniqueLessonCategory.Status to 76,
            TechniqueLessonCategory.SkillPt to 9,
            TechniqueLessonCategory.SkillHint to 10,
            TechniqueLessonCategory.Rest to 5,
        ),
        listOf(
            TechniqueLessonCategory.Status to 28,
            TechniqueLessonCategory.SkillPt to 3,
            TechniqueLessonCategory.SkillHint to 63,
            TechniqueLessonCategory.Rest to 6,
        ),
        listOf(
            TechniqueLessonCategory.Status to 15,
            TechniqueLessonCategory.DualStatus to 50,
            TechniqueLessonCategory.SkillPt to 3,
            TechniqueLessonCategory.SkillHint to 20,
            TechniqueLessonCategory.Rest to 12,
        ),
    ),
    LessonPeriod.Senior to arrayOf(
        listOf(
            TechniqueLessonCategory.Status to 76,
            TechniqueLessonCategory.SkillPt to 9,
            TechniqueLessonCategory.SkillHint to 10,
            TechniqueLessonCategory.Rest to 5,
        ),
        listOf(
            TechniqueLessonCategory.Status to 28,
            TechniqueLessonCategory.SkillPt to 3,
            TechniqueLessonCategory.SkillHint to 63,
            TechniqueLessonCategory.Rest to 6,
        ),
        listOf(
            TechniqueLessonCategory.Status to 15,
            TechniqueLessonCategory.DualStatus to 50,
            TechniqueLessonCategory.SkillPt to 3,
            TechniqueLessonCategory.SkillHint to 20,
            TechniqueLessonCategory.Rest to 12,
        ),
    ),
)

internal val liveTrainingData = listOf(
    TrainingBase(toSupportType("S"), 1, 520, Status(8, 0, 4, 0, 0, 4, -19)),
    TrainingBase(toSupportType("S"), 2, 524, Status(9, 0, 4, 0, 0, 4, -20)),
    TrainingBase(toSupportType("S"), 3, 528, Status(10, 0, 4, 0, 0, 4, -21)),
    TrainingBase(toSupportType("S"), 4, 532, Status(11, 0, 5, 0, 0, 4, -23)),
    TrainingBase(toSupportType("S"), 5, 536, Status(12, 0, 6, 0, 0, 4, -25)),
    TrainingBase(toSupportType("P"), 1, 516, Status(0, 4, 9, 0, 0, 4, -20)),
    TrainingBase(toSupportType("P"), 2, 520, Status(0, 4, 10, 0, 0, 4, -21)),
    TrainingBase(toSupportType("P"), 3, 524, Status(0, 4, 11, 0, 0, 4, -22)),
    TrainingBase(toSupportType("P"), 4, 528, Status(0, 5, 12, 0, 0, 4, -24)),
    TrainingBase(toSupportType("P"), 5, 532, Status(0, 6, 13, 0, 0, 4, -26)),
    TrainingBase(toSupportType("G"), 1, 532, Status(2, 0, 2, 7, 0, 4, -20)),
    TrainingBase(toSupportType("G"), 2, 536, Status(2, 0, 2, 8, 0, 4, -21)),
    TrainingBase(toSupportType("G"), 3, 540, Status(2, 0, 2, 9, 0, 4, -22)),
    TrainingBase(toSupportType("G"), 4, 544, Status(3, 0, 2, 10, 0, 4, -24)),
    TrainingBase(toSupportType("G"), 5, 548, Status(3, 0, 3, 11, 0, 4, -26)),
    TrainingBase(toSupportType("H"), 1, 507, Status(0, 8, 0, 6, 0, 4, -20)),
    TrainingBase(toSupportType("H"), 2, 511, Status(0, 9, 0, 6, 0, 4, -21)),
    TrainingBase(toSupportType("H"), 3, 515, Status(0, 10, 0, 6, 0, 4, -22)),
    TrainingBase(toSupportType("H"), 4, 519, Status(0, 11, 0, 7, 0, 4, -24)),
    TrainingBase(toSupportType("H"), 5, 523, Status(0, 12, 0, 8, 0, 4, -26)),
    TrainingBase(toSupportType("W"), 1, 320, Status(2, 0, 0, 0, 6, 5, 5)),
    TrainingBase(toSupportType("W"), 2, 321, Status(2, 0, 0, 0, 7, 5, 5)),
    TrainingBase(toSupportType("W"), 3, 322, Status(2, 0, 0, 0, 8, 5, 5)),
    TrainingBase(toSupportType("W"), 4, 323, Status(3, 0, 0, 0, 9, 5, 5)),
    TrainingBase(toSupportType("W"), 5, 324, Status(4, 0, 0, 0, 10, 5, 5)),
)

package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.RaceEntry
import io.github.mee1080.umasim.data.RaceGrade
import io.github.mee1080.umasim.data.RaceGround

// TODO 目標レース
enum class BCRoute(
    val displayName: String,
    val skill: String,
    val goalRace: List<RaceEntry>,
) {
    TurfSprint(
        "BCターフスプリント", "電迅速攻",
        listOf(
            RaceEntry(69, "BCターフスプリント", 0, 0, RaceGrade.G1, 1000, RaceGround.TURF, "デルマー"),
        ),
    ),

    Mile(
        "BCマイル", "豪風円刃",
        listOf(
            RaceEntry(69, "BCマイル", 0, 0, RaceGrade.G1, 1600, RaceGround.TURF, "デルマー"),
        ),
    ),

    FillyMareTurf(
        "BCフィリー＆メアターフ", "光芒一閃",
        listOf(
            RaceEntry(69, "BCフィリー＆メアターフ", 0, 0, RaceGrade.G1, 2000, RaceGround.TURF, "デルマー"),
        ),
    ),

    Turf(
        "BCターフ", "時中の妙",
        listOf(
            RaceEntry(69, "BCターフ", 0, 0, RaceGrade.G1, 2400, RaceGround.TURF, "デルマー"),
        ),
    ),

    Sprint(
        "BCスプリント", "紫電円刃",
        listOf(
            RaceEntry(69, "BCスプリント", 0, 0, RaceGrade.G1, 1200, RaceGround.DIRT, "デルマー"),
        ),
    ),

    FillyMareSprint(
        "BCフィリー＆メアスプリント", "紫電一閃",
        listOf(
            RaceEntry(69, "BCフィリー＆メアスプリント", 0, 0, RaceGrade.G1, 1400, RaceGround.DIRT, "デルマー"),
        ),
    ),

    DirtMile(
        "BCダートマイル", "風翔",
        listOf(
            RaceEntry(69, "BCダートマイル", 0, 0, RaceGrade.G1, 1600, RaceGround.DIRT, "デルマー"),
        ),
    ),

    Distaff(
        "BCディスタフ", "豪風一閃",
        listOf(
            RaceEntry(69, "BCディスタフ", 0, 0, RaceGrade.G1, 1800, RaceGround.DIRT, "デルマー"),
        ),
    ),

    Classic(
        "BCクラシック", "閃光",
        listOf(
            RaceEntry(69, "BCクラシック", 0, 0, RaceGrade.G1, 2000, RaceGround.DIRT, "デルマー"),
        ),
    ),
}

val rankToString = listOf(
    "G", "F", "E", "D", "C", "B", "A", "S", "SS",
    "UG", "UF", "UE", "UD", "UC", "UB", "UA", "US",
)

// TODO メンバーランクボーナス
val memberRankEffects = listOf(
    BCMemberRankEffect(0, 0),
    BCMemberRankEffect(10, 10),
    BCMemberRankEffect(10, 10),
    BCMemberRankEffect(10, 10),
    BCMemberRankEffect(10, 10),
    BCMemberRankEffect(10, 10),
    BCMemberRankEffect(10, 10),
    BCMemberRankEffect(24, 30),
)

// TODO チームランクボーナス
val teamRankEffects = listOf(
    BCTeamRankEffect(0, 0, 0),
    BCTeamRankEffect(10, 10, 10),
    BCTeamRankEffect(10, 10, 10),
    BCTeamRankEffect(10, 10, 10),
    BCTeamRankEffect(10, 10, 10),
    BCTeamRankEffect(10, 10, 10),
    BCTeamRankEffect(10, 10, 10),
    BCTeamRankEffect(10, 10, 10),
)

val physicalFriendBonus = listOf(0, 10, 20, 25, 35, 40, 50, 55, 65)

val physicalSubParameter = listOf(0, 15, 25, 30, 35, 40, 45, 50, 55)

val physicalHpCostDown = listOf(0, 0, 40, 70, 100, 100, 100, 100, 100)

val techniqueSkillPtEffect = listOf(0, 10, 15, 20, 25, 30, 30, 30, 30)

val techniqueMinHintCount = listOf(0, 0, 0, 1, 2, 2, 3, 4, 5)

val techniqueHintAll = listOf(0, 0, 0, 0, 0, 1, 1, 1, 1)

val memtalRelationUp = listOf(0, 3, 5, 7, 7, 7, 7, 7, 7)

val memtalFailureRateDown = listOf(0, 5, 50, 100, 100, 100, 100, 100, 100)

val mentalMainLimitUp = listOf(0, 0, 15, 25, 35, 40, 45, 50, 60)

val mentalSkillPtLimitUp = listOf(0, 0, 0, 40, 60, 80, 100, 110, 120)

val dpInitial = listOf(5, 8, 8, 10)

val dpMemberRankUp = listOf(1, 1, 1, 2)

val dpMeeting = listOf(3, 4, 4, 5)

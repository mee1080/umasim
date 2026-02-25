package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.RaceEntry
import io.github.mee1080.umasim.data.toRaceGrade
import io.github.mee1080.umasim.data.toRaceGround

enum class BCRoute(
    val displayName: String,
    val skill: String,
    val goalRace: List<RaceEntry>,
) {
    TurfSprint(
        "BCターフスプリント", "電迅速攻",
        listOf(
            RaceEntry(21, "京王杯ジュニアステークス", 375, 3800, toRaceGrade(200), 1400, toRaceGround(1), "東京"),
            RaceEntry(34, "葵ステークス", 1250, 3800, toRaceGrade(300), 1200, toRaceGround(1), "京都"),
            RaceEntry(42, "スプリンターズステークス", 15000, 13000, toRaceGrade(100), 1200, toRaceGround(1), "中山"),
            RaceEntry(54, "高松宮記念", 15000, 13000, toRaceGrade(100), 1200, toRaceGround(1), "中京"),
            RaceEntry(69, "BCターフスプリント", 0, 8200, toRaceGrade(100), 1000, toRaceGround(1), "デルマー"),
        ),
    ),

    Mile(
        "BCマイル", "豪風円刃",
        listOf(
            RaceEntry(
                23, "朝日杯フューチュリティステークス", 1000, 7000,
                toRaceGrade(100), 1600, toRaceGround(1), "阪神"
            ),
            RaceEntry(33, "NHKマイルカップ", 5000, 10500, toRaceGrade(100), 1600, toRaceGround(1), "東京"),
            RaceEntry(46, "マイルチャンピオンシップ", 15000, 11000, toRaceGrade(100), 1600, toRaceGround(1), "京都"),
            RaceEntry(59, "安田記念", 15000, 13000, toRaceGrade(100), 1600, toRaceGround(1), "東京"),
            RaceEntry(69, "BCマイル", 0, 16300, toRaceGrade(100), 1600, toRaceGround(1), "デルマー"),
        ),
    ),

    FillyMareTurf(
        "BCフィリー＆メアターフ", "光芒一閃",
        listOf(
            RaceEntry(23, "阪神ジュベナイルフィリーズ", 1000, 6500, toRaceGrade(100), 1600, toRaceGround(1), "阪神"),
            RaceEntry(34, "オークス", 6000, 11000, toRaceGrade(100), 2400, toRaceGround(1), "東京"),
            RaceEntry(45, "エリザベス女王杯", 10000, 10500, toRaceGrade(100), 2200, toRaceGround(1), "京都"),
            RaceEntry(57, "ヴィクトリアマイル", 10000, 10500, toRaceGrade(100), 1600, toRaceGround(1), "東京"),
            RaceEntry(69, "BCフィリー＆メアターフ", 0, 16300, toRaceGrade(100), 2200, toRaceGround(1), "デルマー"),
        ),
    ),

    Turf(
        "BCターフ", "時中の妙",
        listOf(
            RaceEntry(24, "ホープフルステークス", 1000, 7000, toRaceGrade(100), 2000, toRaceGround(1), "中山"),
            RaceEntry(34, "東京優駿（日本ダービー）", 6000, 20000, toRaceGrade(100), 2400, toRaceGround(1), "東京"),
            RaceEntry(46, "ジャパンカップ", 25000, 30000, toRaceGrade(100), 2400, toRaceGround(1), "東京"),
            RaceEntry(60, "宝塚記念", 20000, 15000, toRaceGrade(100), 2200, toRaceGround(1), "阪神"),
            RaceEntry(69, "BCターフ", 0, 40800, toRaceGrade(100), 2400, toRaceGround(1), "デルマー"),
        ),
    ),

    Sprint(
        "BCスプリント", "紫電円刃",
        listOf(
            RaceEntry(21, "オキザリス賞", 350, 1000, toRaceGrade(700), 1400, toRaceGround(2), "東京"),
            RaceEntry(29, "昇竜ステークス", 350, 1800, toRaceGrade(400), 1400, toRaceGround(2), "中京"),
            RaceEntry(45, "JBCスプリント", 12000, 6000, toRaceGrade(100), 1200, toRaceGround(2), "大井"),
            RaceEntry(50, "根岸ステークス", 1000, 3800, toRaceGrade(300), 1400, toRaceGround(2), "東京"),
            RaceEntry(69, "BCスプリント", 0, 16300, toRaceGrade(100), 1200, toRaceGround(2), "デルマー"),
        ),
    ),

    FillyMareSprint(
        "BCフィリー＆メアスプリント", "紫電一閃",
        listOf(
            RaceEntry(21, "オキザリス賞", 350, 1000, toRaceGrade(700), 1400, toRaceGround(2), "東京"),
            RaceEntry(29, "昇竜ステークス", 350, 1800, toRaceGrade(400), 1400, toRaceGround(2), "中京"),
            RaceEntry(45, "JBCスプリント", 12000, 6000, toRaceGrade(100), 1200, toRaceGround(2), "大井"),
            RaceEntry(50, "根岸ステークス", 1000, 3800, toRaceGrade(300), 1400, toRaceGround(2), "東京"),
            RaceEntry(69, "BCフィリー&メアスプリント", 0, 8200, toRaceGrade(100), 1400, toRaceGround(2), "デルマー"),
        ),
    ),

    DirtMile(
        "BCダートマイル", "風翔",
        listOf(
            RaceEntry(24, "全日本ジュニア優駿", 1000, 4200, toRaceGrade(100), 1600, toRaceGround(2), "川崎"),
            RaceEntry(36, "ユニコーンステークス", 750, 3500, toRaceGrade(300), 1600, toRaceGround(2), "東京"),
            RaceEntry(
                43, "マイルチャンピオンシップ南部杯", 12000, 6000,
                toRaceGrade(100), 1600, toRaceGround(2), "盛岡"
            ),
            RaceEntry(52, "フェブラリーステークス", 12000, 10000, toRaceGrade(100), 1600, toRaceGround(2), "東京"),
            RaceEntry(69, "BCダートマイル", 0, 8200, toRaceGrade(100), 1600, toRaceGround(2), "デルマー"),
        ),
    ),

    Distaff(
        "BCディスタフ", "豪風一閃",
        listOf(
            RaceEntry(24, "全日本ジュニア優駿", 1000, 4200, toRaceGrade(100), 1600, toRaceGround(2), "川崎"),
            RaceEntry(35, "関東オークス", 1800, 3500, toRaceGrade(200), 2100, toRaceGround(2), "川崎"),
            RaceEntry(45, "JBCレディスクラシック", 12000, 4100, toRaceGrade(100), 1800, toRaceGround(2), "大井"),
            RaceEntry(50, "TCK女王盃", 1000, 2200, toRaceGrade(300), 1800, toRaceGround(2), "大井"),
            RaceEntry(69, "BCディスタフ", 0, 16300, toRaceGrade(100), 1800, toRaceGround(2), "デルマー"),
        ),
    ),

    Classic(
        "BCクラシック", "閃光",
        listOf(
            RaceEntry(24, "全日本ジュニア優駿", 1000, 4200, toRaceGrade(100), 1600, toRaceGround(2), "川崎"),
            RaceEntry(37, "ジャパンダートダービー", 4000, 4500, toRaceGrade(100), 2000, toRaceGround(2), "大井"),
            RaceEntry(45, "JBCクラシック", 12000, 8000, toRaceGrade(100), 2000, toRaceGround(2), "大井"),
            RaceEntry(60, "帝王賞", 12000, 6000, toRaceGrade(100), 2000, toRaceGround(2), "大井"),
            RaceEntry(69, "BCクラシック", 0, 57200, toRaceGrade(100), 2000, toRaceGround(2), "デルマー"),
        ),
    ),
}

val rankToString = listOf(
    "G", "F", "E", "D", "C", "B", "A", "S", "SS",
    "UG", "UF", "UE", "UD", "UC", "UB", "UA", "US",
)

val memberTrainingEffect = listOf(10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30)

val teamFriendBonus = listOf(0, 10, 15, 20, 25, 30, 35, 40, 45, 50)

val teamSpecialityRateUp = listOf(0, 0, 10, 20, 30, 40, 55, 70, 85, 100)

val teamHintFrequencyUp = listOf(0, 20, 30, 40, 50, 60, 70, 80, 90, 100, 105, 110, 115, 120, 125, 130, 135)

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

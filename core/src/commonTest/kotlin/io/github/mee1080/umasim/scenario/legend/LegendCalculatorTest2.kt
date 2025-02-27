package io.github.mee1080.umasim.scenario.legend

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class LegendCalculatorTest2 : LegendCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[Devilish Whispers]スティルインラブ" to 4,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[Cocoon]エアシャカール" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[百花の願いをこの胸に]サトノダイヤモンド" to 4,
        "[導きの光]伝説の体現者" to 4,
    )
) {

    @Test
    fun test() {
        val state = state.setLegendMastery(LegendMember.Red)

        var baseCalcInfo = state.baseCalcInfo
            .copy(motivation = 2, member = state.member)
            .setRelation(0, 100)
            .setRelation(1, 100)
            .setRelation(2, 100)
            .setRelation(3, 100)
            .setRelation(4, 100)
            .setRelation(5, 100)
            .addBuff("溢れるバイタリティ")
            .addBuff("素敵なハーモニー")
            .addBuff("一緒に輝きましょう！")
            .setBestFriendLevel(0, 2)
            .setBestFriendLevel(1, 2)
            .setBestFriendLevel(6, 2)
            .setBestFriendGauge(6, 0)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0, 3,
            base = Status(23, 0, 6, 0, 0, 11),
            scenario = Status(1, 0, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 0, 0, 2, 7,
            base = Status(0, 37, 0, 23, 0, 27),
            scenario = Status(0, 27, 0, 17, 0, 18),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 0, 8,
            base = Status(0, 7, 21, 0, 0, 8),
            scenario = Status(0, 5, 13, 0, 0, 6),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 0, 3, 9,
            base = Status(9, 0, 6, 33, 0, 15),
            scenario = Status(6, 0, 5, 23, 0, 10),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 1, 4, 6, 10,
            base = Status(18, 0, 0, 0, 34, 18),
            scenario = Status(15, 0, 0, 0, 29, 15),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(4, 2)
            .setBestFriendLevel(10, 2)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0, 5, 6,
            base = Status(23, 0, 8, 0, 0, 11),
            scenario = Status(3, 0, 1, 0, 0, 2),
        )

        // SkillPt: 8*2.0115*1.29*1.27*1.1 = 28.99987596 → 29になる
        println(8f * 2.0115f * 1.29f * 1.27f * 1.1f)
        println(8 * 2.0115 * 1.29 * 1.27 * 1.1)
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 0, 2, 7,
            base = Status(0, 30, 0, 18, 0, 17),
            scenario = Status(0, 20, 0, 13, 0, 12),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 0, 0, 4, 8, 10,
            base = Status(0, 12, 36, 0, 0, 20),
            scenario = Status(0, 10, 31, 0, 0, 17),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 0, 1, 3, 4, 9,
            base = Status(19, 0, 13, 49, 0, 27),
            scenario = Status(14, 0, 10, 35, 0, 21),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0,
            base = Status(6, 0, 0, 0, 13, 6),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(3, 2)
            .setBestFriendLevel(9, 2)

        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 0, 1, 2, 8,
            base = Status(0, 15, 35, 0, 0, 17),
            scenario = Status(0, 10, 23, 0, 0, 12),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 3, 4, 6,
            base = Status(16, 0, 0, 0, 32, 16),
            scenario = Status(9, 0, 0, 0, 18, 9),
        )

        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0, 0, 9,
            base = Status(34, 0, 12, 0, 0, 18),
            scenario = Status(18, 0, 6, 0, 0, 9),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 2, 5, 7, 10,
            base = Status(0, 35, 0, 22, 0, 22),
            scenario = Status(0, 29, 0, 18, 0, 19),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 3, 0, 0, 8,
            base = Status(0, 6, 25, 0, 0, 14),
            scenario = Status(0, 4, 17, 0, 0, 10),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 0, 6,
            base = Status(2, 0, 3, 16, 0, 8),
            scenario = Status(0, 0, 0, 3, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 1, 3, 4,
            base = Status(24, 0, 0, 0, 38, 21),
            scenario = Status(10, 0, 0, 0, 16, 10),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(1, 3)
            .setBestFriendLevel(4, 3)
            .setBestFriendLevel(8, 2)
            .addBuff("トレーニングの約束", false)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0, 0, 1, 8, 9,
            base = Status(64, 0, 24, 0, 0, 33),
            scenario = Status(44, 0, 17, 0, 0, 24),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 2, 5, 7,
            base = Status(0, 34, 0, 21, 0, 22),
            scenario = Status(0, 23, 0, 14, 0, 14),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 4, 0, 2, 4,
            base = Status(0, 12, 28, 0, 0, 16),
            scenario = Status(0, 1, 3, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 0, 3, 6, 10,
            base = Status(7, 0, 4, 30, 0, 15),
            scenario = Status(6, 0, 3, 20, 0, 11),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0,
            base = Status(6, 0, 0, 0, 13, 6),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(7, 2)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 1, 6, 8,
            base = Status(0, 18, 0, 12, 0, 13),
            scenario = Status(0, 5, 0, 4, 0, 3),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 0, 3, 7, 9,
            base = Status(7, 0, 4, 30, 0, 15),
            scenario = Status(8, 0, 5, 28, 0, 15),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(0, 3)
            .setBestFriendLevel(2, 2)
            .setBestFriendGauge(8, 0)
            .setBuffEnabled("トレーニングの約束", true)
            .addBuff("絆が奏でるハーモニー", true)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 1, 3, 10,
            base = Status(0, 21, 0, 17, 0, 16),
            scenario = Status(0, 11, 0, 10, 0, 8),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 4, 0, 3, 4, 8,
            base = Status(0, 10, 27, 0, 0, 15),
            scenario = Status(0, 6, 16, 0, 0, 9),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 0, 1, 6, 7, 9,
            base = Status(6, 0, 6, 22, 0, 13),
            scenario = Status(12, 0, 10, 38, 0, 23),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 2, 5,
            base = Status(10, 0, 0, 0, 20, 12),
            scenario = Status(3, 0, 0, 0, 7, 4),
        )

        baseCalcInfo = baseCalcInfo
            .setBuffEnabled("トレーニングの約束", false)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 2,
            base = Status(0, 26, 0, 15, 0, 16),
            scenario = Status(0, 15, 0, 9, 0, 9),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 4, 0, 1, 5, 8,
            base = Status(0, 10, 32, 0, 0, 16),
            scenario = Status(0, 4, 12, 0, 0, 6),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendGauge(7, 0)
            .setBestFriendLevel(2, 3)
            .addBuff("怪物チャンスマイル♪", true)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 2, 6, 7,
            base = Status(0, 29, 0, 17, 0, 17),
            scenario = Status(0, 43, 0, 25, 0, 27),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(3, 3)
            .setBestFriendLevel(4, 4)
            .setBuffEnabled("トレーニングの約束", true)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 3, 4,
            base = Status(0, 20, 0, 16, 0, 15),
            scenario = Status(0, 15, 0, 13, 0, 11),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(2, 3)
            .setBuffEnabled("トレーニングの約束", false)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 2,
            base = Status(0, 26, 0, 15, 0, 16),
            scenario = Status(0, 25, 0, 15, 0, 15),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(10, 3)
            .addBuff("心繋がるパフォーマンス", false)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 0, 2, 10,
            base = Status(0, 30, 0, 18, 0, 17),
            scenario = Status(0, 37, 0, 24, 0, 21),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendGauge(7, 20)
            .setBestFriendLevel(9, 3)
            .addBuff("心繋がるパフォーマンス", true)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 0, 2, 7, 9,
            base = Status(0, 31, 0, 19, 0, 17),
            scenario = Status(0, 65, 0, 41, 0, 37),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(1, 4)
            .setBestFriendLevel(8, 3)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 0, 1, 2, 7, 8,
            base = Status(0, 38, 0, 24, 0, 24),
            scenario = Status(0, 81, 0, 51, 0, 52),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 1)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0,
            base = Status(0, 12, 0, 8, 0, 7),
            scenario = Status(0, 2, 0, 2, 0, 2),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)
            .setBestFriendLevel(2, 4)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 2,
            base = Status(0, 26, 0, 15, 0, 16),
            scenario = Status(0, 29, 0, 18, 0, 18),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 2,
            base = Status(0, 26, 0, 15, 0, 16),
            scenario = Status(0, 29, 0, 18, 0, 18),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(7, 3)
            .setBestFriendGauge(7, 0)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 2, 5, 7,
            base = Status(0, 34, 0, 21, 0, 22),
            scenario = Status(0, 50, 0, 31, 0, 32),
        )

        baseCalcInfo = baseCalcInfo
            .setBestFriendLevel(6, 3)

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 2, 6,
            base = Status(0, 27, 0, 16, 0, 17),
            scenario = Status(0, 40, 0, 23, 0, 24),
        )

    }
}

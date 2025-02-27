package io.github.mee1080.umasim.scenario.legend

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class LegendCalculatorTest1 : LegendCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[Devilish Whispers]スティルインラブ" to 4,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[朝焼け苺の畑にて]ニシノフラワー" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[百花の願いをこの胸に]サトノダイヤモンド" to 4,
        "[導きの光]伝説の体現者" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 3,
            base = Status(12, 0, 2, 0, 0, 8),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0, 0,
            base = Status(0, 9, 0, 7, 0, 9),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 1, 5,
            base = Status(0, 5, 18, 0, 0, 10),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0,
            base = Status(2, 0, 2, 11, 0, 7),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 4,
            base = Status(3, 0, 0, 0, 10, 5),
            scenario = Status(),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 1, totalRelation = 300)
            .setPassion(5, true)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 1, 2, 5,
            base = Status(29, 0, 11, 0, 0, 17),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 0,
            base = Status(2, 0, 2, 13, 0, 7),
            scenario = Status(),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2, totalRelation = 480)
            .addBuff("リーダーシップ")
            .setRelation(1, 80)
            .setRelation(5, 100)
            .setPassion(5, false)

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 5,
            base = Status(0, 6, 18, 0, 0, 11),
            scenario = Status(0, 1, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 0, 4, 5,
            base = Status(27, 0, 10, 0, 0, 19),
            scenario = Status(1, 0, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 9, 0, 7, 0, 8),
            scenario = Status(0, 0, 0, 1, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 1, 2,
            base = Status(9, 0, 0, 0, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 0, 5,
            base = Status(4, 0, 4, 18, 0, 11),
            scenario = Status(0, 0, 1, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 0,
            base = Status(0, 6, 20, 0, 0, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setPassion(5, true)

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 1, 5,
            base = Status(0, 11, 32, 0, 0, 20),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0, 0, 2,
            base = Status(0, 15, 0, 13, 0, 15),
            scenario = Status(0, 1, 0, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 1,
            base = Status(0, 6, 19, 0, 0, 12),
            scenario = Status(0, 0, 1, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 0, 5,
            base = Status(5, 0, 6, 23, 0, 14),
            scenario = Status(0, 0, 0, 1, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .addBuff("たゆまぬ鍛錬")
            .setPassion(5, false)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 0, 1, 2, 3,
            base = Status(47, 0, 20, 0, 0, 30),
            scenario = Status(3, 0, 1, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0, 5,
            base = Status(0, 12, 0, 10, 0, 11),
            scenario = Status(0, 1, 0, 1, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 4,
            base = Status(0, 5, 17, 0, 0, 10),
            scenario = Status(0, 1, 2, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 0,
            base = Status(2, 0, 2, 14, 0, 8),
            scenario = Status(0, 0, 1, 1, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(3, 0, 0, 0, 8, 6),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(0, 80)
            .setRelation(2, 80)
            .setRelation(3, 80)
            .copy(totalRelation = 510)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 0, 1,
            base = Status(46, 0, 14, 0, 0, 30),
            scenario = Status(3, 0, 1, 0, 0, 3),
        )

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 1, 4,
            base = Status(30, 0, 8, 0, 0, 19),
            scenario = Status(3, 0, 1, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 0, 2, 3,
            base = Status(0, 12, 47, 0, 0, 30),
            scenario = Status(0, 0, 3, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 0, 1,
            base = Status(27, 0, 7, 0, 0, 15),
            scenario = Status(2, 0, 0, 0, 0, 1),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(4, 80)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 0, 4,
            base = Status(17, 0, 3, 0, 0, 11),
            scenario = Status(2, 0, 0, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 2, 3, 5,
            base = Status(12, 0, 0, 0, 18, 14),
            scenario = Status(1, 0, 0, 0, 1, 1),
        )

        baseCalcInfo = baseCalcInfo
            .addBuff("高潔な矜持")
            .setPassion(5, false)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 3, 0, 4,
            base = Status(19, 0, 3, 0, 0, 11),
            scenario = Status(3, 0, 1, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 0, 0, 3, 5,
            base = Status(0, 19, 0, 19, 0, 21),
            scenario = Status(0, 3, 0, 3, 0, 4),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 2, 0, 2,
            base = Status(0, 8, 31, 0, 0, 15),
            scenario = Status(0, 2, 5, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 0, 1,
            base = Status(6, 0, 5, 19, 0, 12),
            scenario = Status(1, 0, 1, 4, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 2, 0,
            base = Status(3, 0, 0, 0, 9, 6),
            scenario = Status(1, 0, 0, 0, 2, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.SPEED, 3, 0,
            base = Status(15, 0, 2, 0, 0, 8),
            scenario = Status(3, 0, 1, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 0, 0, 1,
            base = Status(0, 17, 0, 12, 0, 19),
            scenario = Status(0, 2, 0, 2, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 3, 0, 3,
            base = Status(0, 5, 21, 0, 0, 11),
            scenario = Status(0, 1, 4, 0, 0, 1),
        )

        baseCalcInfo = baseCalcInfo
            .copy(totalRelation = 540)

        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 0, 2, 5,
            base = Status(5, 0, 8, 26, 0, 14),
            scenario = Status(1, 0, 2, 4, 0, 2),
        )
        testTraining(
            baseCalcInfo, StatusType.SPEED, 3, 0, 3, 4,
            base = Status(25, 0, 6, 0, 0, 15),
            scenario = Status(4, 0, 1, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 0, 1,
            base = Status(0, 13, 0, 9, 0, 12),
            scenario = Status(0, 2, 0, 2, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 3, 0, 0, 2,
            base = Status(0, 10, 45, 0, 0, 24),
            scenario = Status(0, 2, 7, 0, 0, 4),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 0,
            base = Status(2, 0, 2, 15, 0, 8),
            scenario = Status(0, 0, 1, 3, 0, 1),
        )

        baseCalcInfo = baseCalcInfo
            .addBuff("飽くなき挑戦心")
            .copy(totalRelation = 570)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 3, 0, 0,
            base = Status(28, 0, 6, 0, 0, 17),
            scenario = Status(12, 0, 3, 0, 0, 7),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 0, 2, 3,
            base = Status(0, 16, 0, 16, 0, 14),
            scenario = Status(0, 6, 0, 5, 0, 5),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 3, 0, 1,
            base = Status(0, 6, 23, 0, 0, 12),
            scenario = Status(0, 2, 9, 0, 0, 4),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 4, 0, 4,
            base = Status(4, 0, 3, 21, 0, 11),
            scenario = Status(2, 0, 2, 8, 0, 5),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 3, 0, 5,
            base = Status(5, 0, 0, 0, 13, 8),
            scenario = Status(2, 0, 0, 0, 6, 3),
        )

        baseCalcInfo = baseCalcInfo
            .copy(totalRelation = 600)
            .setPassion(5, true)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 3, 0, 0, 1, 2,
            base = Status(67, 0, 23, 0, 0, 39),
            scenario = Status(21, 0, 8, 0, 0, 12),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 0, 3, 4,
            base = Status(0, 15, 0, 14, 0, 15),
            scenario = Status(0, 6, 0, 6, 0, 6),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 3, 0,
            base = Status(0, 4, 17, 0, 0, 8),
            scenario = Status(0, 2, 8, 0, 0, 4),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 4, 0,
            base = Status(3, 0, 2, 17, 0, 8),
            scenario = Status(2, 0, 2, 7, 0, 4),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 3, 0, 5,
            base = Status(7, 0, 0, 0, 18, 10),
            scenario = Status(3, 0, 0, 0, 7, 5),
        )

        baseCalcInfo = baseCalcInfo
            .addBuff("高潔なる魂")
            .updateLegendStatus { setMastery(LegendMember.Green) }

        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0, 1, 3, 5,
            base = Status(65, 0, 27, 0, 0, 32),
            scenario = Status(58, 0, 24, 0, 0, 29),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 0, 0,
            base = Status(0, 18, 0, 13, 0, 13),
            scenario = Status(0, 18, 0, 13, 0, 14),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 0, 2, 4,
            base = Status(0, 15, 47, 0, 0, 21),
            scenario = Status(0, 14, 41, 0, 0, 18),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 0,
            base = Status(3, 0, 4, 18, 0, 8),
            scenario = Status(4, 0, 5, 20, 0, 9),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0,
            base = Status(6, 0, 0, 0, 13, 6),
            scenario = Status(6, 0, 0, 0, 14, 6),
        )

        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0, 1, 5,
            base = Status(52, 0, 20, 0, 0, 26),
            scenario = Status(48, 0, 20, 0, 0, 24),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 0,
            base = Status(0, 14, 0, 10, 0, 8),
            scenario = Status(0, 16, 0, 12, 0, 9),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 0,
            base = Status(0, 7, 20, 0, 0, 8),
            scenario = Status(0, 8, 22, 0, 0, 9),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 0, 4,
            base = Status(4, 0, 5, 22, 0, 11),
            scenario = Status(4, 0, 5, 23, 0, 12),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 0, 2,
            base = Status(14, 0, 0, 0, 22, 14),
            scenario = Status(12, 0, 0, 0, 19, 12),
        )

        baseCalcInfo = baseCalcInfo
            .setPassion(5, false)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 4, 0, 3,
            base = Status(22, 0, 6, 0, 0, 11),
            scenario = Status(23, 0, 7, 0, 0, 11),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 3, 0,
            base = Status(0, 12, 0, 7, 0, 8),
            scenario = Status(0, 13, 0, 9, 0, 9),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 4, 0, 0, 4,
            base = Status(0, 9, 31, 0, 0, 18),
            scenario = Status(0, 8, 28, 0, 0, 17),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 0, 1, 2,
            base = Status(9, 0, 11, 30, 0, 15),
            scenario = Status(9, 0, 10, 26, 0, 14),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 4, 0, 5,
            base = Status(6, 0, 0, 0, 15, 8),
            scenario = Status(8, 0, 0, 0, 16, 8),
        )

        baseCalcInfo = baseCalcInfo
            .addBuff("共に切り開く未来")
            .addBuff("アピール大成功！")

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 4,
            base = Status(0, 16, 0, 11, 0, 11),
            scenario = Status(0, 16, 0, 11, 0, 12),
        )

        baseCalcInfo = baseCalcInfo
            .addBuff("君となら、もっと！")

        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0, 0, 1, 2,
            base = Status(74, 0, 33, 0, 0, 39),
            scenario = Status(88, 0, 38, 0, 0, 46),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0,
            base = Status(0, 13, 0, 9, 0, 8),
            scenario = Status(0, 14, 0, 10, 0, 9),
        )
    }
}

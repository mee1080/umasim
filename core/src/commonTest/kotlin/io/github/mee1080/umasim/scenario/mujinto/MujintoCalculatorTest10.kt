package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest10 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[大望は飛んでいく]エルコンドルパサー" to 4,
        "[The frontier]ジャングルポケット" to 4,
        "[掲げよ、燃え盛る灯を]カツラギエース" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.FRIEND, 1)
            .copy(motivation = 2)

        // 0 アイ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(1, 2, 3, 4, 5), 0),
            ),
            base = Status(14, 9, 10, 7, 13, 20),
            scenario = Status(0, 0, 0, 0, 0, 1),
        )

        // 3 ヴィブ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 3, 4, 5), 0),
            ),
            base = Status(14, 9, 8, 7, 13, 22),
            scenario = Status(0, 0, 0, 0, 0, 1),
        )

        // 1 エル以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(14, 9, 10, 7, 13, 21),
            scenario = Status(0, 0, 0, 0, 0, 1),
        )

        // 4 ポケ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 5), 0),
            ),
            base = Status(16, 9, 8, 7, 13, 21),
            scenario = Status(0, 0, 0, 0, 0, 1),
        )

        // 2 エース以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 4), 0),
            ),
            base = Status(16, 9, 10, 7, 13, 22),
            scenario = Status(0, 0, 0, 0, 0, 1),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(3, 100)
            .setRelation(4, 100)

        // 6 アイ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(1, 2, 3, 4, 5), 0),
            ),
            base = Status(20, 10, 12, 9, 15, 22),
            scenario = Status(1, 0, 0, 0, 0, 1),
        )

        // 7 ヴィブ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 3, 4, 5), 0),
            ),
            base = Status(20, 10, 10, 9, 15, 24),
            scenario = Status(1, 0, 0, 0, 0, 1),
        )

        // 5 エル以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(18, 9, 10, 7, 13, 21),
            scenario = Status(0, 0, 0, 0, 0, 1),
        )

        // 8 エース以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 4), 0),
            ),
            base = Status(22, 11, 12, 9, 15, 24),
            scenario = Status(1, 0, 0, 0, 0, 1),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.FRIEND, 2)

        // 9 エル以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(18, 9, 10, 7, 13, 21),
            scenario = Status(1, 0, 1, 0, 1, 2),
        )

        // 10 ポケ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 5), 0),
            ),
            base = Status(17, 11, 10, 9, 15, 23),
            scenario = Status(1, 1, 1, 0, 1, 2),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 2)
            .setFacility(StatusType.STAMINA, 2)
            .setFacility(StatusType.POWER, 1)
            .setFacility(StatusType.GUTS, 1)
            .setFacility(StatusType.WISDOM, 1)
            .setRelation(0, 100)
            .setRelation(1, 100)
            .setRelation(2, 100)

        // 11
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2, 5), 1),
                Triple(StatusType.STAMINA, listOf(1), 4),
                Triple(StatusType.POWER, listOf(), 3),
                Triple(StatusType.GUTS, listOf(3), 1),
                Triple(StatusType.WISDOM, listOf(4), 0),
                Triple(StatusType.FRIEND, listOf(), 0),
            ),
            base = Status(69, 21, 33, 17, 25, 61),
            scenario = Status(10, 3, 4, 2, 2, 12),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(5, 80)

        // 12
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 1),
                Triple(StatusType.STAMINA, listOf(5), 3),
                Triple(StatusType.POWER, listOf(1), 0),
                Triple(StatusType.GUTS, listOf(3), 1),
                Triple(StatusType.WISDOM, listOf(2), 2),
                Triple(StatusType.FRIEND, listOf(0), 2),
            ),
            base = Status(51, 19, 26, 15, 26, 49),
            scenario = Status(7, 2, 3, 2, 2, 9),
        )

        // 13
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 3, 5), 1),
                Triple(StatusType.STAMINA, listOf(), 2),
                Triple(StatusType.POWER, listOf(), 1),
                Triple(StatusType.GUTS, listOf(1), 3),
                Triple(StatusType.WISDOM, listOf(4), 2),
                Triple(StatusType.FRIEND, listOf(0), 0),
            ),
            base = Status(84, 20, 36, 16, 26, 66),
            scenario = Status(12, 3, 5, 2, 2, 13),
        )

    }
}

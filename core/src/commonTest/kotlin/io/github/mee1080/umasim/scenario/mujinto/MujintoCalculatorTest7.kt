package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest7 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Devilish Whispers]スティルインラブ" to 4,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[大望は飛んでいく]エルコンドルパサー" to 4,
        "[The frontier]ジャングルポケット" to 4,
        "[夢は掲げるものなのだっ！]トウカイテイオー" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.WISDOM, 1)
            .copy(motivation = 2)

        // 11 不在+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(), 1),
            ),
            base = Status(7, 7, 5, 5, 13, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4 不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(), 1),
            ),
            base = Status(7, 7, 5, 5, 13, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 15 不在+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(), 2),
            ),
            base = Status(7, 7, 5, 5, 13, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 16 不在+4
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(), 4),
            ),
            base = Status(7, 7, 6, 5, 14, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 18 アイ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(0), 0),
            ),
            base = Status(9, 7, 6, 5, 16, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 12 アイ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(0), 2),
            ),
            base = Status(9, 7, 6, 5, 17, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 スティル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1), 1),
            ),
            base = Status(9, 7, 7, 5, 17, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 スティル+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1), 1),
            ),
            base = Status(9, 7, 7, 5, 17, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 0 ヴィブロス+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(2), 1),
            ),
            base = Status(9, 7, 7, 5, 16, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 14 ヴィブロス+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(2), 2),
            ),
            base = Status(9, 7, 7, 5, 17, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 20 エル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(3), 4),
            ),
            base = Status(9, 7, 6, 5, 16, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 13 エル+4
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(3), 4),
            ),
            base = Status(9, 7, 6, 5, 18, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 20 ポケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(4), 1),
            ),
            base = Status(7, 7, 7, 5, 15, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 3 テイオー+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(5), 0),
            ),
            base = Status(7, 7, 7, 5, 15, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 22 テイオー+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(5), 0),
            ),
            base = Status(7, 7, 7, 5, 15, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 19 ポケ+テイオー
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(4, 5), 0),
            ),
            base = Status(8, 7, 9, 5, 17, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 24 ポケ+テイオー+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(4, 5), 2),
            ),
            base = Status(8, 8, 9, 6, 18, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.WISDOM, 2)

        // 7 不在+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(), 0),
            ),
            base = Status(7, 7, 5, 5, 14, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 5 不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(), 1),
            ),
            base = Status(7, 7, 5, 5, 14, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 8 不在+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(), 2),
            ),
            base = Status(7, 7, 5, 5, 14, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 6 アイ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(0), 1),
            ),
            base = Status(9, 7, 6, 5, 18, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 9 エル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(3), 1),
            ),
            base = Status(9, 7, 6, 5, 18, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 10 ポケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(4), 1),
            ),
            base = Status(7, 7, 7, 5, 16, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.WISDOM, 1)
            .setRelation(0, 80)
            .setRelation(1, 80)
            .setRelation(2, 80)
            .setRelation(3, 100)

        // 1 強化スティル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1), 0),
            ),
            base = Status(10, 7, 7, 5, 17, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 23 強化エル+テイオー
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(3, 5), 0),
            ),
            base = Status(11, 9, 9, 7, 20, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

    }
}

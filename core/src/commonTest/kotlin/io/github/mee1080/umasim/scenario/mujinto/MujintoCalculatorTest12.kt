package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest12 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Devilish Whispers]スティルインラブ" to 4,
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
            .setFacility(StatusType.POWER, 1)
            .copy(motivation = 2)

        // 9 不在+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(), 0),
            ),
            base = Status(9, 7, 7, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 7 不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(), 1),
            ),
            base = Status(9, 7, 7, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 12 不在+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(), 2),
            ),
            base = Status(9, 7, 7, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 0 不在+4
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(), 4),
            ),
            base = Status(10, 7, 7, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 25 アイ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(0), 0),
            ),
            base = Status(11, 8, 8, 5, 12, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 23 アイ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(0), 1),
            ),
            base = Status(11, 8, 8, 5, 12, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 16 スティル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(1), 0),
            ),
            base = Status(11, 8, 10, 5, 12, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 17 スティル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(1), 1),
            ),
            base = Status(11, 8, 10, 5, 12, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 18 スティル+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(1), 2),
            ),
            base = Status(11, 8, 10, 5, 13, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 ヴィブ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(2), 0),
            ),
            base = Status(11, 8, 10, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 24 ヴィブ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(2), 1),
            ),
            base = Status(11, 8, 10, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 ヴィブ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(2), 2),
            ),
            base = Status(11, 8, 10, 5, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 15 エル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(3), 0),
            ),
            base = Status(11, 7, 8, 5, 12, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 27 エル+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(3), 2),
            ),
            base = Status(11, 8, 8, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 3 ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(4), 0),
            ),
            base = Status(10, 7, 9, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 10 ポケ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(4), 2),
            ),
            base = Status(10, 8, 9, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 21 エース+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(5), 0),
            ),
            base = Status(10, 7, 7, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 14 エース+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(5), 1),
            ),
            base = Status(10, 7, 7, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4 エース+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(5), 2),
            ),
            base = Status(10, 7, 8, 5, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 13 アイ+エース+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(0, 5), 1),
            ),
            base = Status(12, 8, 9, 5, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 20 アイ+ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(0, 4), 0),
            ),
            base = Status(11, 8, 11, 5, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 31 スティル+ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(1, 4), 0),
            ),
            base = Status(11, 8, 13, 5, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 26 スティル+ポケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(1, 4), 1),
            ),
            base = Status(12, 8, 13, 5, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 28 スティル+ポケ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(1, 4), 2),
            ),
            base = Status(12, 8, 13, 6, 13, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 6 ヴィブ+エル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(2, 3), 1),
            ),
            base = Status(13, 8, 11, 5, 13, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 11 ヴィブ+エル+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(2, 3), 2),
            ),
            base = Status(13, 9, 12, 6, 13, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 30 エル+ポケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(3,4), 1),
            ),
            base = Status(12, 8, 11, 5, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )
        // 29 ポケ+エース+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(4, 5), 2),
            ),
            base = Status(10, 8, 10, 6, 13, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 22 アイ+ヴィブ+ポケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(0, 2, 4), 1),
            ),
            base = Status(14, 9, 15, 6, 14, 17),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 5 スティル+エル+ポケ+エース+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.POWER, listOf(1, 3, 4, 5), 0),
            ),
            base = Status(14, 10, 16, 6, 14, 22),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )
    }
}

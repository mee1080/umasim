package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest16 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[Devilish Whispers]スティルインラブ" to 4,
        "[たどり着いた景色]イクノディクタス" to 4,
        "[王を統べて覇す者]テイエムオペラオー" to 4,
        "[幸福の匂いにまどろむ]セイウンスカイ" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .setFacility(StatusType.POWER, 1)
            .copy(motivation = 2)

        // 7 不在+1/不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 1),
                Triple(StatusType.POWER, listOf(), 1),
            ),
            base = Status(11, 7, 7, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 0 不在+0/ヴィブ+イクノ+オペ+スカイ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 0),
                Triple(StatusType.POWER, listOf(1, 3, 4, 5), 0),
            ),
            base = Status(14, 13, 18, 6, 14, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 21 不在+0/スティル+オペ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 0),
                Triple(StatusType.POWER, listOf(2, 4), 0),
            ),
            base = Status(13, 8, 11, 5, 13, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 21 不在+0/イクノ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 0),
                Triple(StatusType.POWER, listOf(3), 1),
            ),
            base = Status(11, 7, 9, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 3 アイ+1/スティル+スカイ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 1),
                Triple(StatusType.POWER, listOf(2, 5), 1),
            ),
            base = Status(17, 12, 15, 6, 14, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 9 アイ+1/スカイ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 1),
                Triple(StatusType.POWER, listOf(5), 1),
            ),
            base = Status(15, 11, 11, 6, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 6 アイ+ヴィブ+イクノ+スカイ+0/不在+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1, 3, 5), 0),
                Triple(StatusType.POWER, listOf(), 1),
            ),
            base = Status(22, 11, 17, 6, 14, 17),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 アイ+ヴィブ+オペ+1/スカイ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1, 4), 1),
                Triple(StatusType.POWER, listOf(5), 0),
            ),
            base = Status(20, 12, 15, 6, 14, 17),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 16 アイ+スティル+0/イクノ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2), 0),
                Triple(StatusType.POWER, listOf(3), 0),
            ),
            base = Status(19, 8, 14, 6, 13, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 19 アイ+スティル+オペ+1/イクノ+スカイ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2, 4), 1),
                Triple(StatusType.POWER, listOf(3, 5), 2),
            ),
            base = Status(22, 13, 20, 6, 15, 22),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 ヴィブ+2/オペ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 2),
                Triple(StatusType.POWER, listOf(4), 0),
            ),
            base = Status(15, 8, 11, 6, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 11 ヴィブ+0/イクノ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 0),
                Triple(StatusType.POWER, listOf(3), 2),
            ),
            base = Status(15, 8, 13, 6, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 12 ヴィブ+オペ+2/スティル+イクノ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 4), 2),
                Triple(StatusType.POWER, listOf(2, 3), 0),
            ),
            base = Status(19, 9, 18, 6, 14, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4 ヴィブ+スカイ+1/イクノ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 5), 1),
                Triple(StatusType.POWER, listOf(3), 1),
            ),
            base = Status(16, 11, 16, 6, 14, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 27 スティル+0/不在+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2), 0),
                Triple(StatusType.POWER, listOf(), 2),
            ),
            base = Status(14, 7, 10, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 13 スティル+0/イクノ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2), 0),
                Triple(StatusType.POWER, listOf(3), 1),
            ),
            base = Status(15, 8, 12, 5, 13, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 14 スティル+イクノ+2/アイ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 3), 2),
                Triple(StatusType.POWER, listOf(0), 0),
            ),
            base = Status(18, 9, 15, 6, 14, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 10 スティル+イクノ+スカイ+1/不在+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 3, 5), 1),
                Triple(StatusType.POWER, listOf(), 0),
            ),
            base = Status(17, 11, 15, 6, 14, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 20 イクノ+オペ+0/スカイ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3, 4), 0),
                Triple(StatusType.POWER, listOf(5), 1),
            ),
            base = Status(14, 11, 13, 6, 14, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 8 イクノ+スカイ+0/スティル+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3, 5), 0),
                Triple(StatusType.POWER, listOf(2), 2),
            ),
            base = Status(15, 12, 16, 6, 14, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 23 オペ+1/アイ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 1),
                Triple(StatusType.POWER, listOf(0), 2),
            ),
            base = Status(14, 8, 9, 6, 13, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 5 オペ+1/ヴィブ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 1),
                Triple(StatusType.POWER, listOf(1), 2),
            ),
            base = Status(14, 8, 11, 6, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 25 スカイ+0/不在+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 0),
                Triple(StatusType.POWER, listOf(), 0),
            ),
            base = Status(12, 10, 9, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 18 スカイ+2/スティル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 2),
                Triple(StatusType.POWER, listOf(2), 0),
            ),
            base = Status(14, 11, 13, 6, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(1, 80)

        // 22 アイ+ヴィブ+0/オペ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1), 0),
                Triple(StatusType.POWER, listOf(4), 1),
            ),
            base = Status(24, 8, 13, 6, 14, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 26 アイ+ヴィブ+イクノ+オペ+スカイ+0/不在+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1, 3, 4, 5), 0),
                Triple(StatusType.POWER, listOf(), 3),
            ),
            base = Status(31, 12, 21, 6, 15, 24),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 17 アイ+ヴィブ+スカイ+0/イクノ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1, 5), 0),
                Triple(StatusType.POWER, listOf(3), 1),
            ),
            base = Status(27, 12, 19, 6, 14, 22),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 5 オペ+1/アイ+ヴィブ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 1),
                Triple(StatusType.POWER, listOf(0, 1), 0),
            ),
            base = Status(17, 9, 12, 6, 14, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 2)
            .setFacility(StatusType.POWER, 2)
            .setRelation(0, 80)
            .setRelation(2, 80)
            .setRelation(3, 80)
            .setRelation(4, 80)
            .setRelation(5, 80)

        // 34 アイ+0/ヴィブ+スティル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 0),
                Triple(StatusType.POWER, listOf(1, 2), 1),
            ),
            base = Status(28, 9, 23, 6, 14, 27),
            scenario = Status(1, 0, 2, 0, 0, 2),
        )

        // 35 アイ+2/スティル+スカイ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 2),
                Triple(StatusType.POWER, listOf(2, 5), 1),
            ),
            base = Status(25, 13, 33, 6, 14, 32),
            scenario = Status(1, 0, 3, 0, 0, 3),
        )

        // 38 アイ+ヴィブ+0/不在+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1), 0),
                Triple(StatusType.POWER, listOf(), 2),
            ),
            base = Status(31, 8, 19, 6, 13, 22),
            scenario = Status(1, 0, 1, 0, 0, 2),
        )

        // 36 アイ+イクノ+1/オペ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 3), 1),
                Triple(StatusType.POWER, listOf(4), 1),
            ),
            base = Status(22, 14, 23, 6, 14, 20),
            scenario = Status(1, 0, 2, 0, 0, 2),
        )

        // 32 ヴィブ+1/スティル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 1),
                Triple(StatusType.POWER, listOf(2), 1),
            ),
            base = Status(25, 8, 17, 6, 13, 19),
            scenario = Status(1, 0, 1, 0, 0, 1),
        )

        // 29 ヴィブ+スカイ+1/不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 5), 1),
                Triple(StatusType.POWER, listOf(), 1),
            ),
            base = Status(22, 10, 20, 6, 13, 16),
            scenario = Status(1, 0, 2, 0, 0, 1),
        )

        // 37 スティル+1/不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2), 1),
                Triple(StatusType.POWER, listOf(), 1),
            ),
            base = Status(20, 7, 13, 5, 13, 14),
            scenario = Status(1, 0, 1, 0, 0, 1),
        )

        // 28 スティル+2/オペ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2), 2),
                Triple(StatusType.POWER, listOf(4), 0),
            ),
            base = Status(21, 10, 18, 6, 13, 18),
            scenario = Status(1, 0, 1, 0, 0, 1),
        )

        // 30 スティル+イクノ+オペ+0/アイ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 3, 4), 0),
                Triple(StatusType.POWER, listOf(0), 2),
            ),
            base = Status(27, 14, 25, 6, 14, 24),
            scenario = Status(1, 0, 2, 0, 0, 2),
        )

        // 31 イクノ+スカイ+0/オペ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3, 5), 0),
                Triple(StatusType.POWER, listOf(4), 1),
            ),
            base = Status(15, 16, 23, 6, 14, 14),
            scenario = Status(0, 0, 2, 0, 0, 1),
        )

        // 33 オペ+1/イクノ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 1),
                Triple(StatusType.POWER, listOf(3), 1),
            ),
            base = Status(14, 13, 16, 6, 13, 12),
            scenario = Status(0, 0, 1, 0, 0, 1),
        )
    }
}

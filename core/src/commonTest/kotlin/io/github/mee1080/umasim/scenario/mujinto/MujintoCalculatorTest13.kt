package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest13 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[王を統べて覇す者]テイエムオペラオー" to 4,
        "[夢はホントに叶うんだ！]ウイニングチケット" to 4,
        "[たどり着いた景色]イクノディクタス" to 4,
        "[スノウクリスタル・デイ]マーベラスサンデー" to 4,
        "[Burning!!]バンブーメモリー" to 4,
        "[幸福の匂いにまどろむ]セイウンスカイ" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .copy(motivation = 2)

        // 6 オペ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 0),
            ),
            base = Status(12, 7, 6, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 16 オペ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 1),
            ),
            base = Status(12, 7, 6, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 オペ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 2),
            ),
            base = Status(12, 7, 6, 5, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 11 チケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 1),
            ),
            base = Status(12, 7, 8, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 5 チケ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 2),
            ),
            base = Status(12, 7, 8, 5, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4 イクノ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2), 0),
            ),
            base = Status(11, 7, 7, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 20 イクノ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2), 1),
            ),
            base = Status(12, 7, 7, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 25 マベ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 0),
            ),
            base = Status(11, 8, 6, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 24 マベ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 1),
            ),
            base = Status(11, 8, 6, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 12 マベ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 2),
            ),
            base = Status(11, 9, 6, 5, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 8 バンブー+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 0),
            ),
            base = Status(11, 8, 6, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 0 バンブー+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 1),
            ),
            base = Status(12, 8, 6, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 10 バンブー+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 2),
            ),
            base = Status(12, 9, 6, 5, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 7 スカイ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 0),
            ),
            base = Status(12, 10, 7, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 14 スカイ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 1),
            ),
            base = Status(12, 10, 8, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 19 スカイ+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 3),
            ),
            base = Status(12, 10, 8, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 23 オペ+イクノ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2), 1),
            ),
            base = Status(13, 8, 8, 5, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 18 オペ+マベ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 3), 0),
            ),
            base = Status(12, 9, 6, 5, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 17 オペ+マベ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 3), 2),
            ),
            base = Status(13, 9, 6, 6, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 22 オペ+バンブー+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 4), 2),
            ),
            base = Status(13, 9, 7, 6, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 9 チケ+マベ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 3), 1),
            ),
            base = Status(12, 9, 8, 5, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 イクノ+マベ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 3), 0),
            ),
            base = Status(12, 9, 8, 5, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 3 イクノ+バンブー+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 4), 0),
            ),
            base = Status(13, 9, 8, 5, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 15 マベ+スカイ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3, 5), 0),
            ),
            base = Status(12, 11, 8, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 13 イクノ+マベ+スカイ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 3, 5), 1),
            ),
            base = Status(14, 12, 10, 6, 14, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )
    }
}

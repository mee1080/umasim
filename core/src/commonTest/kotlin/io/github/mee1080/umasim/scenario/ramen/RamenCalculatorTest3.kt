package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class RamenCalculatorTest3 : RamenCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[トレセン学園]ゴールドシップ" to 4,
        "[トレセン学園]テイエムオペラオー" to 4,
        "[トレセン学園]メジロマックイーン" to 4,
        "[トレセン学園]セイウンスカイ" to 4,
        "[トレセン学園]タマモクロス" to 4,
        "[トレセン学園]マヤノトップガン" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        // WS000002.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 1,
            base = Status(12, 0, 2, 0, 0, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000003.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 2, 0,
            base = Status(3, 0, 0, 0, 8, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000004.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 0,
            base = Status(2, 0, 2, 13, 0, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000005.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 3, 1,
            base = Status(3, 0, 0, 0, 9, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000006.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 3, 0, 2,
            base = Status(13, 0, 2, 0, 0, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000007.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 0,
            base = Status(2, 0, 2, 14, 0, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000008.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 4, 0, 2,
            base = Status(4, 0, 0, 0, 10, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)

        // WS000009.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 4, 0, 0, 5,
            base = Status(4, 0, 5, 24, 0, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000010.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 4, 0, 4,
            base = Status(19, 0, 4, 0, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000011.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 1, 3,
            base = Status(6, 0, 0, 0, 15, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000012.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0, 1,
            base = Status(20, 0, 6, 0, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000013.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 2, 1, 4,
            base = Status(5, 0, 6, 28, 0, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )
    }
}

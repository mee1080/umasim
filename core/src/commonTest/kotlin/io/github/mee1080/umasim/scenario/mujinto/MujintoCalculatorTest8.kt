package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest8 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[トレセン学園]ナリタトップロード" to 4,
        "[トレセン学園]ジャングルポケット" to 4,
        "[トレセン学園]カツラギエース" to 4,
        "[U & Me]ミホノブルボン" to 4,
        "[比翼のワルツ]トウカイテイオー" to 4,
        "[Operation: Escort]シンボリクリスエス" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.WISDOM, 1)
            .copy(motivation = 2)

        // 1 不在+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(), 2),
            ),
            base = Status(7, 7, 5, 5, 13, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 6 不在+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(), 3),
            ),
            base = Status(7, 7, 6, 5, 13, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 8 ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1), 0),
            ),
            base = Status(7, 7, 7, 5, 13, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 0 テイオー+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(4), 1),
            ),
            base = Status(8, 7, 9, 5, 14, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 3 テイオー+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(4), 2),
            ),
            base = Status(9, 7, 9, 5, 14, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 3 クリス+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(5), 1),
            ),
            base = Status(8, 7, 6, 5, 14, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 エース+クリス+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(2, 3), 0),
            ),
            base = Status(9, 7, 6, 5, 14, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 エース+ブルボン+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(2, 3), 1),
            ),
            base = Status(10, 8, 6, 5, 14, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 7 トプロ+ポケ+テイオー
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(0, 1, 4), 0),
            ),
            base = Status(9, 8, 13, 6, 15, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 7 トプロ+ブルボン+テイオー+クリス
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(0, 3, 4, 5), 0),
            ),
            base = Status(14, 8, 12, 6, 15, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )
    }
}

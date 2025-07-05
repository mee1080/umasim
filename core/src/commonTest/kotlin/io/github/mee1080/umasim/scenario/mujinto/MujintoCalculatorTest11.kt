package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest11 : MujintoCalculatorTest(
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
            .setFacility(StatusType.FRIEND, 1)
            .copy(motivation = 2)

        // 0 トプロ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(1, 2, 3, 4, 5), 0),
            ),
            base = Status(15, 7, 10, 5, 12, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4 ポケ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 4, 5), 0),
            ),
            base = Status(15, 7, 10, 5, 12, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 3 エース以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 3, 4, 5), 0),
            ),
            base = Status(15, 7, 12, 5, 12, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 テイオー以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 5), 0),
            ),
            base = Status(13, 7, 9, 5, 12, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 クリス以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 4), 0),
            ),
            base = Status(13, 7, 12, 5, 12, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .setRelation(1, 80)
            .setRelation(2, 80)
            .setRelation(3, 80)
            .setRelation(4, 80)
            .setRelation(5, 80)

        // 5
        // TODO
//        testIslandTraining(
//            baseCalcInfo,
//            listOf(
//                Triple(StatusType.SPEED, listOf(2, 3), 0),
//                Triple(StatusType.FRIEND, listOf(0, 1, 4, 5), 1),
//            ),
//            base = Status(26, 9, 17, 6, 15, 21),
//            scenario = Status(1, 0, 0, 0, 0, 1),
//        )

        // 6
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 1),
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 4), 0),
            ),
            base = Status(16, 7, 12, 5, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 7
        // TODO
//        testIslandTraining(
//            baseCalcInfo,
//            listOf(
//                Triple(StatusType.SPEED, listOf(0, 5), 2),
//                Triple(StatusType.FRIEND, listOf(1, 2, 3, 4), 1),
//            ),
//            base = Status(25, 9, 17, 6, 15, 20),
//            scenario = Status(1, 0, 0, 0, 0, 1),
//        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.FRIEND, 2)

        // 8
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 0),
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(22, 8, 15, 6, 14, 18),
            scenario = Status(2, 0, 1, 0, 1, 1),
        )

        // 9
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 1),
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 4, 5), 0),
            ),
            base = Status(21, 8, 15, 6, 14, 18),
            scenario = Status(2, 0, 1, 0, 1, 1),
        )
    }
}

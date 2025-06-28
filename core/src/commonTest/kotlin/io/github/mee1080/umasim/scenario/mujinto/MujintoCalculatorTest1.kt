package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest1 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Cocoon]エアシャカール" to 4,
        "[朝焼け苺の畑にて]ニシノフラワー" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[無垢の白妙]デアリングタクト" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setRelation(0, 30)
            .setRelation(1, 30)
            .setRelation(2, 35)
            .setRelation(3, 35)
            .setRelation(4, 30)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 0,
            base = Status(16, 0, 1, 0, 0, 8),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0, 2,
            base = Status(0, 10, 0, 6, 0, 7),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 3, 13, 0, 0, 6),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0, 1, 3,
            base = Status(2, 0, 3, 16, 0, 9),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 4,
            base = Status(3, 0, 0, 0, 9, 8),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(12, 0, 1, 0, 0, 6),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 9, 0, 5, 0, 6),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 0,
            base = Status(0, 3, 16, 0, 0, 8),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0, 1, 2, 4,
            base = Status(4, 0, 5, 17, 0, 14),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 3,
            base = Status(2, 0, 0, 0, 9, 5),
            scenario = Status(),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .setFacility(StatusType.STAMINA, 1)
            .copy(motivation = 1)

        // 34
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(1), 1),
            ),
            base = Status(8, 8, 5, 5, 11, 10),
            scenario = Status(),
            pioneerPt = 66,
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)

        // 41
//        testIslandTraining(
//            baseCalcInfo,
//            listOf(
//                Triple(StatusType.SPEED, listOf(0, 5), 1),
//                Triple(StatusType.STAMINA, listOf(1), 0),
//            ),
//            base = Status(13, 12, 7, 8, 14, 16),
//            scenario = Status(),
//            pioneerPt = 72,
//        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.NONE, 1)
            .setRelation(0, 60)
            .setRelation(1, 80)
            .setRelation(2, 90)
            .setRelation(3, 70)
            .setRelation(4, 75)
            .setRelation(5, 40)

        // 58
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 3), 3),
                Triple(StatusType.NONE, listOf(1, 2, 4, 5), 1),
            ),
            // TODO
            base = Status(13, 17, 9, 11, 15, 26),
            scenario = Status(0, 0, 0, 0, 0, 1),
            pioneerPt = 90,
        )
    }

    @Test
    fun single() {
        val info = state.baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .setFacility(StatusType.STAMINA, 1)
            .setFacility(StatusType.NONE, 1)
            .setRelation(0, 60)
            .setRelation(1, 80)
            .setRelation(2, 90)
            .setRelation(3, 70)
            .setRelation(4, 75)
            .setRelation(5, 40)

        testIslandTraining(
            info,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 3), 3),
                Triple(StatusType.NONE, listOf(1, 2, 4, 5), 1),
            ),
            // TODO
            base = Status(13, 17, 9, 11, 15, 26),
            scenario = Status(0, 0, 0, 0, 0, 1),
            pioneerPt = 90,
        )
    }
}

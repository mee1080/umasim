package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class RamenCalculatorTest2 : RamenCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[共に同じ道を！]桐生院葵" to 4,
        "[謹製ッ！特大夢にんじん！]秋川理事長" to 4,
        "[American Dream]カジノドライヴ" to 4,
        "[from the GROUND UP]ライトハロー" to 4,
        "[共に描くキラメキ]都留岐涼花" to 4,
        "[ようこそ、トレセン学園へ！]駿川たづな" to 2,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo
            .copy(motivation = 2)

        // WS000000.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 1, 0,
            base = Status(15, 0, 3, 0, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000001.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 2, 1,
            base = Status(0, 15, 0, 5, 0, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000002.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 2, 2,
            base = Status(0, 9, 17, 0, 0, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000003.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0, 3,
            base = Status(2, 0, 3, 16, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000004.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 4, 5,
            base = Status(4, 0, 0, 0, 11, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo.updateRamenStatus {
            copy(activeTastingRegion = RamenRegion.FUKUSHIMA)
        }

        // WS000005.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 3, 1,
            base = Status(4, 0, 3, 19, 0, 13),
            scenario = Status(1, 0, 1, 7, 0, 4),
        )

        baseCalcInfo = baseCalcInfo.updateRamenStatus {
            copy(activeTastingRegion = null)
        }

        // WS000006.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 3, 3,
            base = Status(3, 0, 3, 20, 0, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000007.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 2, 2, 5,
            base = Status(0, 9, 18, 0, 0, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(2, 60)

        // WS000008.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 2, 1, 2,
            base = Status(4, 0, 0, 0, 12, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000009.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 1, 0,
            base = Status(0, 15, 0, 6, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000010.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 1,
            base = Status(15, 0, 3, 0, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000011.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 3, 0, 0,
            base = Status(0, 15, 0, 5, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000012.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 0, 3,
            base = Status(0, 18, 0, 7, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 1)

        // WS000013.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 3, 2,
            base = Status(0, 20, 0, 9, 0, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)

        // WS000014.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 3, 3, 5,
            base = Status(0, 9, 20, 0, 0, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000015.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 4, 0, 3,
            base = Status(0, 9, 19, 0, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000016.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 0, 5,
            base = Status(0, 11, 21, 0, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )
    }
}

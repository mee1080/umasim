package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class RamenCalculatorTest4 : RamenCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[一杯のノスタルジア]駿川たづな" to 0,
        "[心覚えし、京の華]エアグルーヴ" to 4,
        "[その執念は怒濤が如く]メイショウドトウ" to 4,
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Innovator]フォーエバーヤング" to 4,
        "[全てに挑む勇ましき者]アグネスデジタル" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo
            .copy(motivation = 2)
            .setPeriod(3, true)
            .setActiveTastingRegion(RamenRegion.FINALS2)
            .setRelation(0, 100)
            .setRelation(1, 100)
            .setRelation(2, 100)
            .setRelation(3, 100)
            .setRelation(4, 100)
            .setRelation(5, 100)

        // WS000002.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 4, 1, 1, 3, 4,
            base = Status(69, 0, 27, 0, 0, 60),
            scenario = Status(120, 0, 47, 0, 0, 200),
        )

        // WS000003.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 3, 2,
            base = Status(0, 37, 0, 19, 0, 22),
            scenario = Status(0, 64, 0, 33, 0, 99),
        )

        // WS000004.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 3, 0, 0, 1, 5,
            base = Status(0, 22, 49, 0, 0, 38),
            scenario = Status(0, 38, 85, 0, 0, 171),
        )

        // WS000005.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 2, 0, 3, 5,
            base = Status(7, 0, 11, 35, 0, 29),
            scenario = Status(0, 0, 0, 0, 0, 29),
        )

        // WS000006.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 2, 4,
            base = Status(13, 0, 0, 0, 37, 29),
            scenario = Status(22, 0, 0, 0, 64, 130),
        )

        // WS000008.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 4, 1, 0, 1, 3, 5,
            base = Status(82, 0, 39, 0, 0, 77),
            scenario = Status(143, 0, 68, 0, 0, 200),
        )

        // WS000009.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 1, 2, 3,
            base = Status(0, 41, 0, 21, 0, 30),
            scenario = Status(0, 71, 0, 36, 0, 135),
        )

        // WS000010.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 3, 1, 1, 5,
            base = Status(0, 17, 46, 0, 0, 32),
            scenario = Status(0, 29, 80, 0, 0, 144),
        )

        // WS000011.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 1, 0,
            base = Status(2, 0, 3, 20, 0, 11),
            scenario = Status(0, 0, 0, 0, 0, 11),
        )

        // WS000012.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 2, 4,
            base = Status(13, 0, 0, 0, 37, 29),
            scenario = Status(22, 0, 0, 0, 64, 130),
        )

        // WS000013.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 4, 2, 3,
            base = Status(33, 0, 10, 0, 0, 20),
            scenario = Status(57, 0, 17, 0, 0, 90),
        )

        // WS000014.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 1, 2, 3,
            base = Status(0, 41, 0, 21, 0, 30),
            scenario = Status(0, 71, 0, 36, 0, 135),
        )

        // WS000015.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 3, 1, 1, 2, 5,
            base = Status(0, 25, 56, 0, 0, 47),
            scenario = Status(0, 43, 98, 0, 0, 200),
        )

        // WS000016.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 3, 0, 0, 4, 5,
            base = Status(4, 0, 8, 32, 0, 27),
            scenario = Status(0, 0, 0, 0, 0, 27),
        )

        // WS000017.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 1, 1, 4,
            base = Status(14, 0, 0, 0, 41, 32),
            scenario = Status(24, 0, 0, 0, 71, 144),
        )
    }
}

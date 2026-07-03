package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class RamenCalculatorTest1 : RamenCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[一杯のノスタルジア]駿川たづな" to 0,
        "[心覚えし、京の華]エアグルーヴ" to 4,
        "[その執念は怒濤が如く]メイショウドトウ" to 0,
        "[永久の誓い、永久の輝き]サトノダイヤモンド" to 4,
        "[Innovator]フォーエバーヤング" to 4,
        "[全てに挑む勇ましき者]アグネスデジタル" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo
            .copy(motivation = 1)

        // WS000002.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 2, 1, 4,
            base = Status(19, 0, 4, 0, 0, 19),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000003.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 1, 2,
            base = Status(0, 13, 0, 4, 0, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000004.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 0,
            base = Status(0, 7, 13, 0, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000005.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 1, 3,
            base = Status(2, 0, 3, 18, 0, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000006.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 1,
            base = Status(3, 0, 0, 0, 8, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000007.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 2, 1, 2, 3,
            base = Status(24, 0, 5, 0, 0, 26),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000008.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 1,
            base = Status(0, 11, 0, 3, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000009.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 1, 4, 5,
            base = Status(0, 10, 21, 0, 0, 19),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(RamenRegion.SAPPORO)
            .setExcitementPt(300)

        // WS000012.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 2, 1, 2, 3,
            base = Status(24, 0, 5, 0, 0, 26),
            scenario = Status(9, 0, 1, 0, 0, 9),
        )

        // WS000013.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 1,
            base = Status(0, 11, 0, 3, 0, 8),
            scenario = Status(0, 1, 0, 0, 0, 1),
        )

        // WS000014.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 1, 4, 5,
            base = Status(0, 10, 21, 0, 0, 19),
            scenario = Status(0, 1, 3, 0, 0, 3),
        )

        // WS000015.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 1,
            base = Status(2, 0, 2, 13, 0, 8),
            scenario = Status(0, 0, 0, 2, 0, 1),
        )

        // WS000016.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 0,
            base = Status(3, 0, 0, 0, 8, 8),
            scenario = Status(0, 0, 0, 0, 1, 1),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(null)

        // TODO

        // WS000017.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(15, 0, 8, 0, 0, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000018.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 14, 0, 4, 0, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000019.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 11, 22, 0, 0, 21),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000020.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0,
            base = Status(2, 0, 52, 18, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000021.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(4, 0, 0, 0, 13, 12),
            scenario = Status(0, 0, 2, 0, 0, 0),
        )

        // WS000022.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 70, 0, 0, 3, 0),
            scenario = Status(0, 4, 0, 7, 0, 0),
        )

        // WS000023.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(35, 0, 10, 0, 0, 82),
            scenario = Status(1, 0, 0, 0, 0, 0),
        )

        // WS000024.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 7087, 0, 0, 3, 0),
            scenario = Status(0, 4, 0, 7, 0, 0),
        )

        // WS000025.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(35, 0, 10, 0, 0, 88),
            scenario = Status(1, 0, 0, 0, 0, 0),
        )

        // WS000026.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 12, 0, 4, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000027.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(0, 0, 16, 0, 0, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000028.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0,
            base = Status(2, 0, 3, 16, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000029.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(8, 0, 0, 0, 8, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000030.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(0, 0, 0, 0, 0, 0),
            scenario = Status(0, 7, 0, 28, 0, 0),
        )

        // WS000031.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(0, 0, 0, 0, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000032.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(115, 0, 0, 0, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 1),
        )

        // WS000033.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 14, 0, 4, 0, 10),
            scenario = Status(0, 2, 0, 0, 0, 2),
        )

        // WS000034.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 7, 12, 0, 0, 0),
            scenario = Status(0, 1, 2, 0, 0, 1),
        )

        // WS000035.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0,
            base = Status(4, 0, 7, 24, 0, 22),
            scenario = Status(0, 0, 1, 4, 0, 4),
        )

        // WS000036.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(47, 0, 0, 0, 18, 31),
            scenario = Status(0, 0, 0, 0, 4, 18),
        )

        // WS000037.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(25, 0, 10, 0, 0, 19),
            scenario = Status(1, 0, 0, 0, 0, 0),
        )

        // WS000038.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 41, 0, 516, 0, 52),
            scenario = Status(0, 2, 0, 0, 0, 2),
        )

        // WS000039.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(0, 0, 0, 0, 0, 0),
            scenario = Status(0, 7, 0, 28, 0, 0),
        )

        // WS000040.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(0, 0, 0, 0, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000041.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 0, 0, 0, 5, 0),
            scenario = Status(0, 4, 0, 0, 0, 0),
        )

        // WS000042.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(45, 0, 0, 0, 14, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000043.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(6, 10, 0, 0, 0, 0),
            scenario = Status(89, 30, 0, 0, 0, 0),
        )

        // WS000044.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(26, 0, 10, 0, 0, 20),
            scenario = Status(18, 0, 5, 0, 0, 10),
        )

        // WS000045.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 7, 0, 0, 10, 0),
            scenario = Status(0, 4, 0, 0, 8, 0),
        )

        // WS000046.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(87, 30, 0, 0, 0, 0),
            scenario = Status(0, 7, 15, 0, 0, 0),
        )

        // WS000047.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(7, 0, 0, 0, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000048.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(115, 0, 2, 0, 0, 0),
            scenario = Status(4, 0, 0, 0, 0, 2),
        )

        // WS000049.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 0, 0, 0, 84, 34),
            scenario = Status(5, 0, 0, 0, 0, 19),
        )

        // WS000050.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 707, 0, 0, 120, 0),
            scenario = Status(0, 4, 0, 7, 0, 0),
        )

        // WS000051.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 10, 0, 0, 0, 0),
            scenario = Status(0, 30, 0, 0, 0, 0),
        )

        // WS000052.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 33, 0, 13, 0, 36),
            scenario = Status(0, 1, 0, 4, 0, 21),
        )

        // WS000053.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 0, 0, 0, 3, 1),
            scenario = Status(0, 4, 0, 0, 0, 0),
        )

        // WS000054.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 70, 0, 0, 0, 0),
            scenario = Status(0, 4, 0, 0, 0, 0),
        )

        // WS000055.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(32, 0, 14, 0, 0, 21),
            scenario = Status(1, 0, 0, 0, 0, 1),
        )

        // WS000056.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 18, 0, 57, 0, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000057.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 9, 119, 0, 0, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000058.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0,
            base = Status(3, 0, 6, 21, 0, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000059.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(4, 0, 0, 0, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000060.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(0, 0, 0, 0, 0, 0),
            scenario = Status(0, 50, 0, 0, 0, 0),
        )

        // WS000061.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(30, 0, 12, 0, 0, 19),
            scenario = Status(18, 0, 4, 0, 0, 1),
        )

        // WS000062.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 18, 0, 7, 0, 1),
            scenario = Status(0, 8, 0, 1, 0, 2),
        )

        // WS000063.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 18, 85, 0, 0, 26),
            scenario = Status(0, 10, 20, 0, 0, 15),
        )

        // WS000064.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0,
            base = Status(2, 0, 3, 16, 0, 0),
            scenario = Status(0, 0, 0, 42, 0, 1),
        )

        // WS000065.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 0, 0, 0, 39, 39),
            scenario = Status(10, 0, 0, 0, 46, 46),
        )

        // WS000066.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 70, 0, 0, 5, 0),
            scenario = Status(0, 4, 0, 7, 0, 0),
        )

        // WS000067.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(37, 0, 15, 0, 0, 29),
            scenario = Status(22, 0, 0, 0, 0, 18),
        )

        // WS000068.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 29, 0, 13, 0, 24),
            scenario = Status(0, 35, 0, 15, 0, 29),
        )

        // WS000069.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 24, 50, 0, 0, 41),
            scenario = Status(0, 2, 61, 0, 0, 50),
        )

        // WS000070.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 7087, 0, 0, 5, 0),
            scenario = Status(0, 4, 0, 7, 0, 0),
        )

        // WS000071.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(43, 0, 15, 0, 0, 34),
            scenario = Status(4, 0, 1, 0, 0, 8),
        )

        // WS000072.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 13, 0, 5, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000073.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 12, 8, 0, 0, 19),
            scenario = Status(0, 1, 8, 0, 0, 1),
        )

        // WS000074.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0,
            base = Status(2, 0, 3, 17, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000075.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 0, 0, 0, 84, 31),
            scenario = Status(0, 0, 0, 0, 3, 8),
        )

        // WS000076.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 707, 0, 0, 18, 0),
            scenario = Status(0, 4, 0, 7, 8, 0),
        )

        // WS000077.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(0, 0, 0, 0, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000078.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 9, 0, 0, 10, 0),
            scenario = Status(0, 4, 0, 7, 0, 0),
        )

        // WS000079.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 37, 0, 1, 0, 37),
            scenario = Status(0, 58, 0, 4, 0, 58),
        )

        // WS000080.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 907, 0, 0, 16, 0),
            scenario = Status(0, 4, 0, 7, 0, 0),
        )

        // WS000081.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(0, 0, 0, 0, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000082.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 33, 0, 14, 0, 25),
            scenario = Status(0, 25, 0, 10, 0, 19),
        )

        // WS000083.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 0, 0, 0, 35, 32),
            scenario = Status(12, 0, 0, 0, 58, 49),
        )

        // WS000084.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 70, 0, 0, 18, 0),
            scenario = Status(0, 4, 0, 7, 0, 0),
        )

        // WS000085.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 7, 0, 0, 18, 0),
            scenario = Status(0, 4, 0, 0, 0, 0),
        )

        // WS000086.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(43, 0, 1, 0, 0, 31),
            scenario = Status(10, 0, 4, 0, 0, 4),
        )

        // WS000087.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 33, 0, 16, 0, 18),
            scenario = Status(0, 0, 0, 8, 0, 4),
        )

        // WS000088.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 23, 55, 0, 0, 31),
            scenario = Status(0, 5, 18, 0, 0, 4),
        )

        // WS000089.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0,
            base = Status(3, 0, 4, 21, 0, 0),
            scenario = Status(0, 0, 0, 4, 0, 1),
        )

        // WS000090.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(6, 0, 0, 0, 13, 0),
            scenario = Status(1, 0, 0, 0, 2, 1),
        )

        // WS000091.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 7, 0, 0, 18, 0),
            scenario = Status(0, 4, 0, 0, 0, 0),
        )

        // WS000092.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(0, 0, 0, 0, 0, 0),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000093.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(87, 0, 55, 0, 0, 81),
            scenario = Status(95, 0, 6, 0, 0, 0),
        )

        // WS000094.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 23, 0, 0, 0, 14),
            scenario = Status(0, 0, 0, 8, 0, 4),
        )

        // WS000095.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 17, 88, 0, 0, 19),
            scenario = Status(0, 5, 11, 0, 0, 0),
        )

        // WS000096.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0,
            base = Status(5, 0, 0, 81, 0, 18),
            scenario = Status(1, 0, 2, 10, 0, 4),
        )

        // WS000097.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(14, 0, 0, 0, 41, 32),
            scenario = Status(15, 0, 0, 0, 44, 84),
        )

        // WS000098.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(0, 707, 0, 0, 20, 0),
            scenario = Status(0, 4, 0, 7, 0, 0),
        )

        // WS000099.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 25, 0, 12, 0, 14),
            scenario = Status(0, 2, 0, 1, 0, 1),
        )

        // WS000100.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(0, 0, 0, 0, 0, 0),
            scenario = Status(0, 0, 0, 9474, 0, 0),
        )

    }
}

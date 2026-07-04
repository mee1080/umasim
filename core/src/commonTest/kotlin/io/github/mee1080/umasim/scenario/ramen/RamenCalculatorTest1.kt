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

        // WS000017.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 2, 2,
            base = Status(15, 0, 3, 0, 0, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000018.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 1, 1,
            base = Status(0, 14, 0, 4, 0, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000019.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 1, 3, 5,
            base = Status(0, 11, 22, 0, 0, 21),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000020.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 1,
            base = Status(2, 0, 2, 13, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000021.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 1, 4,
            base = Status(4, 0, 0, 0, 13, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(1, 80)
            .copy(motivation = 2)

        // WS000023.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 1, 1, 3,
            base = Status(35, 0, 10, 0, 0, 32),
            scenario = Status(1, 0, 0, 0, 0, 0),
        )

        // WS000026.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 1,
            base = Status(0, 12, 0, 4, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(RamenRegion.TOKYO)
            .setExcitementPt(630)
            .setRelation(3, 80)

        // WS000032.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 2,
            base = Status(15, 0, 3, 0, 0, 9),
            scenario = Status(3, 0, 0, 0, 0, 1),
        )

        // WS000033.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 1, 0,
            base = Status(0, 14, 0, 4, 0, 10),
            scenario = Status(0, 2, 0, 0, 0, 2),
        )

        // WS000034.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 7, 12, 0, 0, 8),
            scenario = Status(0, 1, 2, 0, 0, 1),
        )

        // WS000035.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 1, 4, 5,
            base = Status(4, 0, 7, 24, 0, 22),
            scenario = Status(0, 0, 1, 4, 0, 4),
        )

        // WS000036.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 2, 1, 2, 3,
            base = Status(7, 0, 0, 0, 18, 31),
            scenario = Status(3, 0, 0, 0, 7, 13),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(null)
            .setRelation(2, 80)

        // WS000037.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 0, 3,
            base = Status(25, 0, 10, 0, 0, 19),
            scenario = Status(1, 0, 0, 0, 0, 0),
        )

        // WS000038.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 1, 1, 2, 4, 5,
            base = Status(0, 41, 0, 16, 0, 52),
            scenario = Status(0, 2, 0, 0, 0, 2),
        )

        baseCalcInfo = baseCalcInfo
            .setExcitementPt(990)
            .setActiveTastingRegion(RamenRegion.SAPPORO)
            .setExcitementPt(1380)

        // WS000044.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 1, 3,
            base = Status(26, 0, 10, 0, 0, 20),
            scenario = Status(13, 0, 5, 0, 0, 10),
        )

        baseCalcInfo = baseCalcInfo
            .setExcitementPt(1800)
            .setActiveTastingRegion(RamenRegion.TOKYO)
            .setExcitementPt(2250)
            .setRelation(4, 80)

        // WS000048.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 3, 0,
            base = Status(15, 0, 2, 0, 0, 8),
            scenario = Status(4, 0, 0, 0, 0, 2),
        )

        // WS000049.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 2, 2, 4, 5,
            base = Status(9, 0, 0, 0, 34, 34),
            scenario = Status(5, 0, 0, 0, 19, 19),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(RamenRegion.HAKODATE)
            .setExcitementPt(2700)
            .setRelation(2, 80)

        // WS000052.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 2, 2, 3, 4,
            base = Status(0, 33, 0, 13, 0, 36),
            scenario = Status(0, 19, 0, 7, 0, 21),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(null)
            .setExcitementPt(0)
            .setPeriod(1, true)

        // WS000055.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 4, 2, 3,
            base = Status(32, 0, 14, 0, 0, 21),
            scenario = Status(1, 0, 0, 0, 0, 1),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(RamenRegion.KOKURA)
            .setExcitementPt(400)
            .setRelation(0, 100)
            .setRelation(5, 80)

        // WS000061.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 4, 0, 3,
            base = Status(30, 0, 12, 0, 0, 19),
            scenario = Status(17, 0, 7, 0, 0, 11),
        )

        // WS000062.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 0, 1,
            base = Status(0, 18, 0, 7, 0, 14),
            scenario = Status(0, 3, 0, 1, 0, 2),
        )

        // WS000063.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 2, 2, 0, 5,
            base = Status(0, 18, 35, 0, 0, 26),
            scenario = Status(0, 10, 20, 0, 0, 15),
        )

        // WS000064.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 1,
            base = Status(2, 0, 3, 16, 0, 8),
            scenario = Status(0, 0, 0, 2, 0, 1),
        )

        // WS000065.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 3, 1, 2, 3, 4,
            base = Status(9, 0, 0, 0, 39, 39),
            scenario = Status(10, 0, 0, 0, 46, 46),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(RamenRegion.HANSHIN)
            .setExcitementPt(840)

        // WS000067.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 4, 0, 3, 4,
            base = Status(37, 0, 15, 0, 0, 29),
            scenario = Status(22, 0, 9, 0, 0, 17),
        )

        // WS000068.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 1, 1, 2,
            base = Status(0, 29, 0, 13, 0, 24),
            scenario = Status(0, 35, 0, 15, 0, 29),
        )

        // WS000069.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 2, 2, 0, 1, 5,
            base = Status(0, 24, 50, 0, 0, 41),
            scenario = Status(0, 29, 61, 0, 0, 50),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(null)

        // WS000071.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 4, 2, 0, 1, 2,
            base = Status(43, 0, 15, 0, 0, 34),
            scenario = Status(4, 0, 1, 0, 0, 3),
        )

        // WS000072.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 1,
            base = Status(0, 13, 0, 5, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000073.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 3, 0, 5,
            base = Status(0, 12, 31, 0, 0, 19),
            scenario = Status(0, 1, 3, 0, 0, 1),
        )

        // WS000074.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 2,
            base = Status(2, 0, 3, 17, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000075.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 3, 1, 3, 4,
            base = Status(8, 0, 0, 0, 34, 31),
            scenario = Status(0, 0, 0, 0, 3, 3),
        )

        baseCalcInfo = baseCalcInfo
            .setExcitementPt(1840)
            .setActiveTastingRegion(RamenRegion.HANSHIN)
            .setExcitementPt(2400)

        // WS000079.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 1, 1, 2, 3,
            base = Status(0, 37, 0, 17, 0, 37),
            scenario = Status(0, 53, 0, 24, 0, 53),
        )

        baseCalcInfo = baseCalcInfo
            .setExcitementPt(2400)
            .setActiveTastingRegion(RamenRegion.KOKURA)
            .setExcitementPt(3000)

        // WS000082.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 3, 2, 1, 2,
            base = Status(0, 33, 0, 14, 0, 25),
            scenario = Status(0, 25, 0, 10, 0, 19),
        )

        // WS000083.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 3, 1, 1, 4,
            base = Status(8, 0, 0, 0, 35, 32),
            scenario = Status(12, 0, 0, 0, 53, 49),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(null)
            .setExcitementPt(3600)

        // WS000086.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 1, 1, 4,
            base = Status(43, 0, 17, 0, 0, 31),
            scenario = Status(10, 0, 4, 0, 0, 7),
        )

        // WS000087.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 1, 0, 2,
            base = Status(0, 33, 0, 16, 0, 18),
            scenario = Status(0, 7, 0, 3, 0, 4),
        )

        // WS000088.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 1, 3, 5,
            base = Status(0, 23, 55, 0, 0, 31),
            scenario = Status(0, 5, 13, 0, 0, 7),
        )

        // WS000089.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 2,
            base = Status(3, 0, 4, 21, 0, 9),
            scenario = Status(0, 0, 0, 3, 0, 1),
        )

        // WS000090.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 1,
            base = Status(6, 0, 0, 0, 13, 8),
            scenario = Status(1, 0, 0, 0, 2, 1),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(RamenRegion.NAKAYAMA)
            .setExcitementPt(4200)

        // WS000093.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 1, 1, 2, 3, 5,
            base = Status(87, 0, 55, 0, 0, 81),
            scenario = Status(95, 0, 60, 0, 0, 88),
        )

        // WS000094.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 1, 3,
            base = Status(0, 23, 0, 10, 0, 14),
            scenario = Status(0, 8, 0, 3, 0, 4),
        )

        // WS000095.png
        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 1, 1, 2,
            base = Status(0, 17, 33, 0, 0, 19),
            scenario = Status(0, 5, 11, 0, 0, 6),
        )

        // WS000096.png
        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 2, 0, 4,
            base = Status(5, 0, 6, 31, 0, 18),
            scenario = Status(1, 0, 2, 10, 0, 6),
        )

        // WS000097.png
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 1, 4, 5,
            base = Status(14, 0, 0, 0, 41, 32),
            scenario = Status(15, 0, 0, 0, 44, 34),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(null)
            .setExcitementPt(0)
            .setPeriod(2, true)

        // WS000099.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 1, 2,
            base = Status(0, 25, 0, 12, 0, 14),
            scenario = Status(0, 2, 0, 1, 0, 1),
        )
    }
}

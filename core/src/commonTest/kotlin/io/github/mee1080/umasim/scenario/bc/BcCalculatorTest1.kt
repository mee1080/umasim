package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class BcCalculatorTest1 : BCCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[Devilish Whispers]スティルインラブ" to 4,
        "[故郷に錦を飾るんでい！]イナリワン" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[無垢の白妙]デアリングタクト" to 4,
        "[Innovator]フォーエバーヤング" to 4,
        "[American Dream]カジノドライヴ" to 0,
    ),
    teamMemberList = listOf(
        "マルシュロレーヌ",
        "フォーエバーヤング",
        "ラブズオンリーユー",
    ),
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .initBcStatus()

        // WS000018.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            support = listOf(), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(11, 0, 2, 0, 0, 7),
        )

        // WS000019.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            support = listOf(1), guest = listOf(0),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(0, 9, 0, 7, 0, 8),
        )

        // WS000020.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 1,
            support = listOf(2, 5), guest = listOf(2),
            scenario = Status(0, 0, 1, 0, 0, 0),
            base = Status(0, 6, 16, 0, 0, 9),
        )

        // WS000021.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 1,
            support = listOf(), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(2, 0, 2, 9, 0, 7),
        )

        // WS000022.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            support = listOf(0, 3, 4), guest = listOf(1),
            scenario = Status(0, 0, 0, 0, 1, 1),
            base = Status(6, 0, 0, 0, 17, 18),
        )

        // WS000023.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            support = listOf(0), guest = listOf(0),
            scenario = Status(1, 0, 0, 0, 0, 0),
            base = Status(14, 0, 4, 0, 0, 9),
        )

        // WS000024.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            support = listOf(1, 3, 4), guest = listOf(1, 2),
            scenario = Status(0, 2, 0, 2, 0, 3),
            base = Status(0, 12, 0, 10, 0, 17),
        )

        // WS000025.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 1,
            support = listOf(2), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(0, 5, 15, 0, 0, 9),
        )

        // WS000026.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 1,
            support = listOf(), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(2, 0, 2, 9, 0, 7),
        )

        // WS000027.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            support = listOf(5), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(2, 0, 0, 0, 8, 7),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 1)

        // WS000029.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            support = listOf(0), guest = listOf(2),
            scenario = Status(1, 0, 0, 0, 0, 1),
            base = Status(16, 0, 4, 0, 0, 11),
        )

        // WS000030.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            support = listOf(1), guest = listOf(0),
            scenario = Status(0, 1, 0, 0, 0, 0),
            base = Status(0, 11, 0, 9, 0, 9),
        )

        // WS000031.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 1,
            support = listOf(), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(0, 4, 13, 0, 0, 7),
        )

        // WS000032.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 1,
            support = listOf(3, 4, 5), guest = listOf(1),
            scenario = Status(0, 0, 0, 1, 0, 1),
            base = Status(4, 0, 3, 16, 0, 17),
        )

        // WS000033.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            support = listOf(2), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(2, 0, 0, 0, 10, 10),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)
            .setMemberGaugeMax(1, true)

        // WS000034.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            support = listOf(2), guest = listOf(0),
            scenario = Status(1, 0, 0, 0, 0, 1),
            base = Status(15, 0, 4, 0, 0, 11),
        )

        // WS000035.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            support = listOf(4), guest = listOf(1),
            scenario = Status(0, 4, 0, 3, 0, 5),
            base = Status(0, 11, 0, 9, 0, 13),
        )

        // WS000036.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 1,
            support = listOf(0, 1), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(0, 8, 26, 0, 0, 16),
        )

        // WS000038.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            support = listOf(3), guest = listOf(2),
            scenario = Status(0, 0, 0, 0, 1, 1),
            base = Status(4, 0, 0, 0, 11, 12),
        )

        baseCalcInfo = baseCalcInfo
            .setTeamParameter(BCTeamParameter.Mental, 3)

        // WS000039.png
        testDreamsTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            scenario = Status(23, 0, 2, 0, 0, 27),
            base = Status(39, 0, 3, 0, 0, 39),
        )

        // WS000040.png
        testDreamsTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            scenario = Status(0, 16, 0, 2, 0, 27),
            base = Status(0, 27, 0, 4, 0, 39),
        )

        // WS000041.png
        testDreamsTraining(
            baseCalcInfo, StatusType.POWER, 1,
            scenario = Status(0, 2, 25, 0, 0, 27),
            base = Status(0, 3, 43, 0, 0, 39),
        )

        // WS000042.png
        testDreamsTraining(
            baseCalcInfo, StatusType.GUTS, 1,
            scenario = Status(1, 0, 2, 17, 0, 27),
            base = Status(3, 0, 3, 29, 0, 39),
        )

        // WS000043.png
        testDreamsTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            scenario = Status(1, 0, 0, 0, 19, 27),
            base = Status(3, 0, 0, 0, 33, 39),
        )

        baseCalcInfo = baseCalcInfo
            .setMemberGaugeMax(1, false)
            .setMemberRank(1, "F")

        // WS000045.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            support = listOf(4), guest = listOf(1),
            scenario = Status(0, 1, 0, 1, 0, 1),
            base = Status(0, 11, 0, 9, 0, 13),
        )

        // WS000050.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            support = listOf(1, 4), guest = listOf(1),
            scenario = Status(0, 1, 0, 1, 0, 2),
            base = Status(0, 15, 0, 12, 0, 17),
        )

        baseCalcInfo = baseCalcInfo
            .setMemberGaugeMax(2, true)

        // WS000053.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            support = listOf(5), guest = listOf(2),
            scenario = Status(0, 0, 0, 0, 4, 3),
            base = Status(2, 0, 0, 0, 10, 9),
        )

        // WS000054.png
        testDreamsTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            scenario = Status(24, 0, 2, 0, 0, 28),
            base = Status(39, 0, 3, 0, 0, 39),
        )

        // WS000055.png
        testDreamsTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            scenario = Status(0, 16, 0, 2, 0, 28),
            base = Status(0, 27, 0, 4, 0, 39),
        )

        // WS000056.png
        testDreamsTraining(
            baseCalcInfo, StatusType.POWER, 1,
            scenario = Status(0, 2, 26, 0, 0, 28),
            base = Status(0, 3, 43, 0, 0, 39),
        )

        // WS000057.png
        testDreamsTraining(
            baseCalcInfo, StatusType.GUTS, 1,
            scenario = Status(1, 0, 2, 17, 0, 28),
            base = Status(3, 0, 3, 29, 0, 39),
        )

        // WS000058.png
        testDreamsTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            scenario = Status(1, 0, 0, 0, 20, 28),
            base = Status(3, 0, 0, 0, 33, 39),
        )

        baseCalcInfo = baseCalcInfo
            .setMemberGaugeMax(2, false)
            .setMemberRank(0, "F")
            .setMemberRank(2, "F")
            .setRelation(0, 80)
            .setRelation(2, 80)
            .setRelation(3, 80)
            .setRelation(4, 80)

        // WS000059.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            support = listOf(1, 2, 3, 4), guest = listOf(1),
            scenario = Status(3, 0, 0, 0, 16, 15),
            base = Status(16, 0, 0, 0, 70, 65),
        )

        // WS000060.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 2,
            support = listOf(0), guest = listOf(2),
            scenario = Status(0, 0, 0, 0, 1, 1),
            base = Status(6, 0, 0, 0, 13, 13),
        )

        baseCalcInfo = baseCalcInfo
            .setTeamParameter(BCTeamParameter.Physical, 3)
            .setMemberGaugeMax(0, true)
            .setMemberGaugeMax(1, true)
            .setRelation(1, 80)
            .setRelation(5, 80)

        // WS000062.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(2, 0, 0, 0, 0, 1),
            base = Status(25, 0, 6, 0, 0, 17),
        )

        // WS000063.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 1,
            support = listOf(2, 5), guest = listOf(0, 2),
            scenario = Status(0, 7, 22, 0, 0, 13),
            base = Status(0, 11, 33, 0, 0, 20),
        )

        // WS000064.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 2,
            support = listOf(1, 3, 4), guest = listOf(1),
            scenario = Status(7, 0, 0, 0, 34, 29),
            base = Status(13, 0, 0, 0, 62, 53),
        )

        baseCalcInfo = baseCalcInfo
            .setMemberGaugeMax(0, true)
            .setMemberGaugeMax(1, false)
            .setMemberGaugeMax(2, true)
            .setMemberRank(0, "F")
            .setMemberRank(1, "E")
            .setMemberRank(2, "F")

        // WS000065.png
        testDreamsTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            scenario = Status(99, 0, 13, 0, 0, 115),
            base = Status(58, 0, 8, 0, 0, 62),
        )

        // WS000066.png
        testDreamsTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            scenario = Status(0, 68, 0, 23, 0, 124),
            base = Status(0, 40, 0, 13, 0, 67),
        )

        // WS000067.png
        testDreamsTraining(
            baseCalcInfo, StatusType.POWER, 1,
            scenario = Status(0, 15, 115, 0, 0, 130),
            base = Status(0, 9, 67, 0, 0, 70),
        )

        // WS000068.png
        testDreamsTraining(
            baseCalcInfo, StatusType.GUTS, 1,
            scenario = Status(5, 0, 5, 33, 0, 49),
            base = Status(6, 0, 5, 34, 0, 46),
        )

        // WS000069.png
        testDreamsTraining(
            baseCalcInfo, StatusType.WISDOM, 2,
            scenario = Status(22, 0, 0, 0, 125, 140),
            base = Status(12, 0, 0, 0, 91, 98),
        )

        baseCalcInfo = baseCalcInfo
            .setMemberGaugeMax(true, true, false)
            .setMemberRank("E", "D", "E")

        // WS000070.png
        testDreamsTraining(
            baseCalcInfo, StatusType.SPEED, 2,
            scenario = Status(119, 0, 15, 0, 0, 128),
            base = Status(62, 0, 8, 0, 0, 62),
        )

        // WS000071.png
        testDreamsTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            scenario = Status(0, 77, 0, 26, 0, 139),
            base = Status(0, 40, 0, 13, 0, 67),
        )

        // WS000072.png
        testDreamsTraining(
            baseCalcInfo, StatusType.POWER, 1,
            scenario = Status(0, 17, 125, 0, 0, 140),
            base = Status(0, 9, 67, 0, 0, 70),
        )

        // WS000073.png
        testDreamsTraining(
            baseCalcInfo, StatusType.GUTS, 2,
            scenario = Status(6, 0, 5, 38, 0, 52),
            base = Status(6, 0, 5, 37, 0, 46),
        )

        // WS000074.png
        testDreamsTraining(
            baseCalcInfo, StatusType.WISDOM, 2,
            scenario = Status(24, 0, 0, 0, 125, 140),
            base = Status(12, 0, 0, 0, 91, 98),
        )

        baseCalcInfo = baseCalcInfo
            .setTeamParameter(5, 3, 3)
            .setMemberGaugeMax(false, false, false)
            .setMemberRank("D", "C", "D")

        // WS000076.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 3,
            support = listOf(0), guest = listOf(),
            scenario = Status(5, 0, 1, 0, 0, 3),
            base = Status(28, 0, 6, 0, 0, 17),
        )

        // WS000077.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 2,
            support = listOf(1), guest = listOf(),
            scenario = Status(0, 3, 0, 3, 0, 3),
            base = Status(0, 19, 0, 16, 0, 17),
        )

        // WS000078.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 2,
            support = listOf(5), guest = listOf(0, 2),
            scenario = Status(0, 1, 5, 0, 0, 3),
            base = Status(0, 5, 18, 0, 0, 10),
        )

        // WS000079.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 3,
            support = listOf(3), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(4, 0, 3, 17, 0, 12),
        )

        // WS000080.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 3,
            support = listOf(2, 4), guest = listOf(1),
            scenario = Status(2, 0, 0, 0, 14, 10),
            base = Status(5, 0, 0, 0, 34, 26),
        )

        // WS000081.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 3,
            support = listOf(1, 4), guest = listOf(0, 1),
            scenario = Status(8, 0, 1, 0, 0, 6),
            base = Status(25, 0, 4, 0, 0, 19),
        )

        // WS000082.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 2,
            support = listOf(3, 5), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(0, 14, 0, 10, 0, 15),
        )

        // WS000083.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 2,
            support = listOf(2), guest = listOf(2),
            scenario = Status(0, 3, 11, 0, 0, 6),
            base = Status(0, 10, 30, 0, 0, 17),
        )

        // WS000084.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 3,
            support = listOf(), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(2, 0, 2, 14, 0, 8),
        )

        // WS000085.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 3,
            support = listOf(0), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(6, 0, 0, 0, 15, 13),
        )

        baseCalcInfo = baseCalcInfo
            .setMemberGaugeMax(true, true, true)

        // WS000086.png
        testDreamsTraining(
            baseCalcInfo, StatusType.SPEED, 3,
            scenario = Status(125, 0, 30, 0, 0, 140),
            base = Status(66, 0, 10, 0, 0, 62),
        )

        // WS000087.png
        testDreamsTraining(
            baseCalcInfo, StatusType.STAMINA, 2,
            scenario = Status(0, 125, 0, 50, 0, 140),
            base = Status(0, 45, 0, 17, 0, 67),
        )

        // WS000088.png
        testDreamsTraining(
            baseCalcInfo, StatusType.POWER, 2,
            scenario = Status(0, 34, 125, 0, 0, 140),
            base = Status(0, 11, 72, 0, 0, 70),
        )

        // WS000089.png
        testDreamsTraining(
            baseCalcInfo, StatusType.GUTS, 3,
            scenario = Status(10, 0, 9, 56, 0, 73),
            base = Status(7, 0, 7, 40, 0, 46),
        )

        // WS000090.png
        testDreamsTraining(
            baseCalcInfo, StatusType.WISDOM, 3,
            scenario = Status(48, 0, 0, 0, 125, 140),
            base = Status(16, 0, 0, 0, 98, 98),
        )

        // TODO ここから未修正

        // WS000091.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(3, 0, 12, 0, 0, 8),
            base = Status(8, 0, 0, 0, 0, 0),
        )

        // WS000092.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(8, 0, 0, 5, 0, 9),
            base = Status(23, 0, 0, 14, 0, 25),
        )

        // WS000093.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(15, 0, 2, 0, 0, 8),
        )

        // WS000094.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(9, 0, 0, 6, 0, 8),
            base = Status(15, 0, 0, 10, 0, 13),
        )

        // WS000095.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(4, 0, 17, 0, 0, 8),
        )

        // WS000096.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(2, 0, 2, 14, 0, 8),
        )

        // WS000097.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(8, 0, 0, 0, 35, 26),
            base = Status(17, 0, 0, 0, 71, 53),
        )

        // WS000098.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(125, 0, 27, 0, 0, 140),
            base = Status(66, 0, 10, 0, 0, 62),
        )

        // WS000099.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(125, 47, 0, 0, 0, 140),
            base = Status(49, 0, 0, 0, 0, 67),
        )

        // WS000100.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(31, 0, 125, 0, 0, 140),
            base = Status(11, 0, 78, 0, 0, 70),
        )

        // WS000101.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(8, 0, 8, 46, 0, 62),
            base = Status(7, 0, 7, 40, 0, 46),
        )

        // WS000102.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            support = listOf(0), guest = listOf(),
            scenario = Status(50, 0, 0, 0, 125, 140),
            base = Status(19, 0, 0, 0, 104, 98),
        )

        // WS000104.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(124, 0, 34, 0, 0, 160),
            base = Status(75, 0, 15, 0, 0, 62),
        )

        // WS000105.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(134, 50, 0, 0, 0, 160),
            base = Status(58, 22, 0, 0, 0, 67),
        )

        // WS000106.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(37, 0, 135, 0, 0, 160),
            base = Status(16, 0, 89, 0, 0, 70),
        )

        // WS000107.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(9, 0, 8, 42, 0, 55),
            base = Status(10, 0, 9, 47, 0, 46),
        )

        // WS000108.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(50, 0, 0, 0, 135, 160),
            base = Status(22, 0, 0, 0, 111, 98),
        )

        // WS000109.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(12, 0, 3, 0, 0, 7),
            base = Status(40, 0, 13, 0, 0, 24),
        )

        // WS000110.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(14, 0, 0, 10, 0, 8),
        )

        // WS000111.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 5,
            support = listOf(0), guest = listOf(),
            scenario = Status(4, 0, 13, 0, 0, 6),
            base = Status(16, 0, 44, 0, 0, 20),
        )

        // WS000112.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 5,
            support = listOf(0), guest = listOf(),
            scenario = Status(1, 0, 1, 4, 0, 2),
            base = Status(6, 0, 5, 24, 0, 12),
        )

        // WS000113.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 5,
            support = listOf(0), guest = listOf(),
            scenario = Status(6, 0, 0, 0, 26, 16),
            base = Status(8, 0, 0, 0, 31, 19),
        )

        // WS000115.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(18, 0, 13, 0, 0, 23),
            base = Status(32, 0, 14, 0, 0, 24),
        )

        // WS000116.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(4, 0, 0, 6, 0, 5),
            base = Status(18, 0, 0, 18, 0, 17),
        )

        // WS000117.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(6, 0, 22, 0, 0, 10),
        )

        // WS000118.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(3, 0, 2, 15, 0, 8),
        )

        // WS000119.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(10, 0, 0, 0, 19, 25),
            base = Status(17, 0, 0, 0, 56, 38),
        )

        // WS000120.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(75, 0, 41, 0, 0, 210),
            base = Status(35, 0, 12, 0, 0, 62),
        )

        // WS000121.png
        testBcTraining(
            baseCalcInfo, StatusType.STAMINA, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(75, 0, 0, 50, 0, 210),
            base = Status(34, 0, 0, 19, 0, 67),
        )

        // WS000122.png
        testBcTraining(
            baseCalcInfo, StatusType.POWER, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(23, 0, 88, 0, 0, 210),
            base = Status(14, 0, 84, 0, 0, 70),
        )

        // WS000123.png
        testBcTraining(
            baseCalcInfo, StatusType.GUTS, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(5, 0, 9, 57, 0, 73),
            base = Status(4, 0, 7, 44, 0, 46),
        )

        // WS000124.png
        testBcTraining(
            baseCalcInfo, StatusType.WISDOM, 5,
            support = listOf(0), guest = listOf(),
            scenario = Status(25, 0, 0, 0, 75, 210),
            base = Status(11, 0, 0, 0, 82, 98),
        )

        // WS000125.png
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 4,
            support = listOf(0), guest = listOf(),
            scenario = Status(0, 0, 0, 0, 0, 0),
            base = Status(9, 0, 5, 0, 0, 8),
        )

    }
}

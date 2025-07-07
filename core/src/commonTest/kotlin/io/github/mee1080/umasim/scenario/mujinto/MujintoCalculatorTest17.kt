package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest17 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[Cocoon]エアシャカール" to 4,
        "[大地と我らのアンサンブル]サウンズオブアース" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[優しい月]ゴールドシチー" to 4,
        "[百花の願いをこの胸に]サトノダイヤモンド" to 4,
        "[無垢の白妙]デアリングタクト" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.STAMINA, 1)
            .copy(motivation = 2)

        // 1 シャカ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0), 1),
            ),
            base = Status(10, 9, 6, 5, 12, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 3 シャカ+アース+タクト+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 1, 5), 1),
            ),
            base = Status(12, 13, 6, 6, 14, 20),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 6 シャカ+オル+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 2), 2),
            ),
            base = Status(10, 10, 6, 8, 13, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 0 シャカ+ダイヤ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 4), 2),
            ),
            base = Status(10, 11, 6, 6, 16, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4 シャカ+タクト+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 5), 1),
            ),
            base = Status(12, 10, 6, 6, 13, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 アース+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(1), 0),
            ),
            base = Status(10, 10, 6, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 5 アース+タクト+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(1, 5), 1),
            ),
            base = Status(12, 12, 6, 6, 13, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.GUTS, 1)
            .setRelation(1, 80)

        // 7 不在+1/アース+ダイヤ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(), 1),
                Triple(StatusType.GUTS, listOf(1, 4), 1),
            ),
            base = Status(11, 10, 7, 8, 16, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 7 不在+0/オル+ダイヤ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(), 0),
                Triple(StatusType.GUTS, listOf(2, 4), 0),
            ),
            base = Status(11, 9, 6, 9, 15, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 10 不在+1/アース+オル+シチー+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(), 1),
                Triple(StatusType.GUTS, listOf(1, 2, 3), 1),
            ),
            base = Status(12, 11, 9, 10, 14, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 12 不在+2/オル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(), 2),
                Triple(StatusType.GUTS, listOf(2), 0),
            ),
            base = Status(10, 9, 6, 9, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 11 シャカ+1/ダイヤ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0), 1),
                Triple(StatusType.GUTS, listOf(4), 0),
            ),
            base = Status(11, 10, 6, 8, 16, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 9 シャカ+1/ダイヤ+タクト+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0), 1),
                Triple(StatusType.GUTS, listOf(4, 5), 0),
            ),
            base = Status(13, 10, 7, 9, 16, 19),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 14 シャカ+アース+2/オル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 1), 2),
                Triple(StatusType.GUTS, listOf(2), 0),
            ),
            base = Status(11, 15, 7, 11, 14, 20),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 17 シャカ+アース+オル+0/不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 1, 2), 0),
                Triple(StatusType.GUTS, listOf(), 1),
            ),
            base = Status(11, 15, 6, 11, 14, 19),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 13 シャカ+オル+0/アース+シチー+ダイヤ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 2), 0),
                Triple(StatusType.GUTS, listOf(1, 3, 4), 1),
            ),
            base = Status(13, 13, 10, 12, 18, 20),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 16 シャカ+オル+ダイヤ+タクト+0/シチー+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 2, 4, 5), 0),
                Triple(StatusType.GUTS, listOf(3), 0),
            ),
            base = Status(13, 13, 9, 12, 18, 22),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 18 シャカ+ダイヤ+0/アース+タクト+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 4), 0),
                Triple(StatusType.GUTS, listOf(1, 5), 0),
            ),
            base = Status(13, 13, 7, 9, 17, 23),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 15 シチー+2/不在+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(3), 2),
                Triple(StatusType.GUTS, listOf(), 0),
            ),
            base = Status(10, 9, 7, 7, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.WISDOM, 1)
            .setRelation(2, 80)
            .setRelation(4, 80)
            .setRelation(5, 80)

        // 20 シャカ+オル+0/不在+0/アース+シチー+タクト+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 2), 0),
                Triple(StatusType.GUTS, listOf(), 0),
                Triple(StatusType.WISDOM, listOf(1, 3, 5), 2),
            ),
            base = Status(18, 17, 9, 14, 44, 32),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 24 シャカ+アース+0/ダイヤ+1/シチー+タクト+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 1), 0),
                Triple(StatusType.GUTS, listOf(4), 1),
                Triple(StatusType.WISDOM, listOf(3, 5), 2),
            ),
            base = Status(19, 16, 9, 11, 41, 45),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 25 シャカ+オル+タクト+0/アース+シチー+ダイヤ+0/不在+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 2, 5), 0),
                Triple(StatusType.GUTS, listOf(1, 3, 4), 0),
                Triple(StatusType.WISDOM, listOf(), 2),
            ),
            base = Status(16, 19, 10, 18, 27, 31),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 22 アース+タクト+2/シチー+2/オル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(1, 5), 2),
                Triple(StatusType.GUTS, listOf(3), 2),
                Triple(StatusType.WISDOM, listOf(2), 0),
            ),
            base = Status(14, 19, 9, 16, 25, 24),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 21 オル+1/シチー+1/タクト+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(2), 1),
                Triple(StatusType.GUTS, listOf(3), 1),
                Triple(StatusType.WISDOM, listOf(5), 0),
            ),
            base = Status(16, 13, 8, 13, 34, 20),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 19 シチー+2/不在+1/シャカ+オル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(3), 2),
                Triple(StatusType.GUTS, listOf(), 1),
                Triple(StatusType.WISDOM, listOf(0, 2), 0),
            ),
            base = Status(13, 13, 8, 13, 24, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 23 ダイヤ+0/シャカ+0/不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(4), 0),
                Triple(StatusType.GUTS, listOf(0), 0),
                Triple(StatusType.WISDOM, listOf(), 1),
            ),
            base = Status(11, 10, 6, 8, 17, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.GUTS, 2)
            .setFacility(StatusType.WISDOM, 2)
            .setRelation(0, 100)
            .setRelation(1, 100)
            .setRelation(2, 100)
            .setRelation(3, 100)
            .setRelation(4, 100)
            .setRelation(5, 100)

        // 30 不在+0/シャカ+オル+1/アース+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(), 0),
                Triple(StatusType.GUTS, listOf(0, 2), 1),
                Triple(StatusType.WISDOM, listOf(1), 0),
            ),
            base = Status(13, 16, 7, 18, 22, 19),
            scenario = Status(1, 0, 0, 0, 1, 1),
        )

        // 32 不在+1/オル+シチー+1/ダイヤ+タクト+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(), 1),
                Triple(StatusType.GUTS, listOf(2, 3), 1),
                Triple(StatusType.WISDOM, listOf(4, 5), 2),
            ),
            base = Status(26, 13, 11, 29, 67, 48),
            scenario = Status(2, 0, 0, 1, 3, 4),
        )

        // 33 不在+2/オル+1/アース+ダイヤ+タクト+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(), 2),
                Triple(StatusType.GUTS, listOf(2), 1),
                Triple(StatusType.WISDOM, listOf(1, 4, 5), 1),
            ),
            base = Status(24, 15, 8, 18, 74, 44),
            scenario = Status(2, 0, 0, 0, 3, 4),
        )

        // 26 シャカ+1/アース+オル+1/タクト+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0), 1),
                Triple(StatusType.GUTS, listOf(1, 2), 1),
                Triple(StatusType.WISDOM, listOf(5), 3),
            ),
            base = Status(20, 24, 8, 22, 39, 46),
            scenario = Status(2, 0, 0, 1, 1, 4),
        )

        // 28 シャカ+1/アース+オル+シチー+1/不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0), 1),
                Triple(StatusType.GUTS, listOf(1, 2, 3), 1),
                Triple(StatusType.WISDOM, listOf(), 1),
            ),
            base = Status(15, 23, 11, 35, 21, 33),
            scenario = Status(1, 0, 0, 1, 1, 3),
        )

        // 27 シャカ+アース+0/オル+シチー+1/不在+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 1), 0),
                Triple(StatusType.GUTS, listOf(2, 3), 1),
                Triple(StatusType.WISDOM, listOf(), 0),
            ),
            base = Status(14, 27, 11, 36, 20, 39),
            scenario = Status(1, 0, 0, 1, 1, 3),
        )

        // 31 ダイヤ+4/オル+0/タクト+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(4), 4),
                Triple(StatusType.GUTS, listOf(2), 0),
                Triple(StatusType.WISDOM, listOf(5), 1),
            ),
            base = Status(18, 14, 8, 18, 42, 30),
            scenario = Status(1, 0, 0, 0, 2, 3),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.STAMINA, 2)
            .setFacility(StatusType.WISDOM, 3)

        // 35 シャカ+1/アース+オル+シチー+0/ダイヤ+タクト+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0), 1),
                Triple(StatusType.GUTS, listOf(1, 2, 3), 0),
                Triple(StatusType.WISDOM, listOf(4, 5), 1),
            ),
            base = Status(28, 26, 12, 37, 74, 96),
            scenario = Status(4, 1, 0, 3, 7, 19),
        )

        // 36 シャカ+1/シチー+タクト+1/アース+ダイヤ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0), 1),
                Triple(StatusType.GUTS, listOf(3, 5), 1),
                Triple(StatusType.WISDOM, listOf(1, 4), 1),
            ),
            base = Status(20, 22, 10, 23, 45, 57),
            scenario = Status(3, 1, 0, 2, 4, 11),
        )

        // 37 シャカ+ダイヤ+2/オル+1/タクト+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(0, 4), 2),
                Triple(StatusType.GUTS, listOf(2), 1),
                Triple(StatusType.WISDOM, listOf(5), 1),
            ),
            base = Status(19, 25, 8, 22, 46, 51),
            scenario = Status(2, 1, 0, 2, 4, 10),
        )

        // 34 アース+0/シャカ+0/オル+シチー+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.STAMINA, listOf(1), 0),
                Triple(StatusType.GUTS, listOf(0), 0),
                Triple(StatusType.WISDOM, listOf(2, 3), 1),
            ),
            base = Status(13, 23, 9, 23, 26, 24),
            scenario = Status(1, 1, 0, 2, 2, 4),
        )

    }
}

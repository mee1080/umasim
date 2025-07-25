package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest19 : MujintoCalculatorTest(
    chara = Triple("[リアライズ・ルーン]スイープトウショウ", 5, 5),
    supportCardList = listOf(
        "[Cocoon]エアシャカール" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .setFacility(StatusType.STAMINA, 1)
            .setFacility(StatusType.POWER, 1)
            .setFacility(StatusType.WISDOM, 1)
            .setFacility(StatusType.FRIEND, 1)
            .setRelation(0, 80)
            .setRelation(1, 80)
            .setRelation(2, 80)
            .setRelation(3, 80)
            .setRelation(4, 80)
            .setRelation(5, 80)
            .copy(motivation = 2)
            .updateMujintoStatus { updatePhase(1) }

        // 0
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 1,
            base = Status(19, 0, 1, 0, 0, 10),
            scenario = Status(1, 0, 0, 0, 0, 1),
        )

        // 1
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 1, 0, 5,
            base = Status(0, 30, 0, 16, 0, 17),
            scenario = Status(0, 3, 0, 1, 0, 1),
        )

        // 2
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 2, 3,
            base = Status(0, 12, 34, 0, 0, 19),
            scenario = Status(0, 1, 3, 0, 0, 1),
        )

        // 3
        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 2,
            base = Status(2, 0, 2, 15, 0, 7),
            scenario = Status(0, 0, 0, 1, 0, 0),
        )

        // 4
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 2, 3, 4,
            base = Status(11, 0, 0, 0, 15, 13),
            scenario = Status(1, 0, 0, 0, 1, 1),
        )

        // 5
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 0),
                Triple(StatusType.STAMINA, listOf(0, 5), 1),
                Triple(StatusType.POWER, listOf(2, 3), 0),
                Triple(StatusType.WISDOM, listOf(4), 3),
                Triple(StatusType.FRIEND, listOf(), 2),
            ),
            base = Status(26, 36, 25, 17, 30, 71),
            scenario = Status(7, 10, 7, 5, 9, 21),
        )

        // 6
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 1),
                Triple(StatusType.STAMINA, listOf(0), 2),
                Triple(StatusType.POWER, listOf(2), 2),
                Triple(StatusType.WISDOM, listOf(3, 4), 0),
                Triple(StatusType.FRIEND, listOf(5), 1),
            ),
            base = Status(27, 33, 24, 16, 33, 70),
            scenario = Status(8, 9, 7, 4, 9, 21),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 2)
            .setFacility(StatusType.POWER, 2)
            .setFacility(StatusType.GUTS, 1)

        // 7
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1, 4), 0),
                Triple(StatusType.STAMINA, listOf(3), 1),
                Triple(StatusType.POWER, listOf(2), 3),
                Triple(StatusType.GUTS, listOf(), 2),
                Triple(StatusType.WISDOM, listOf(), 0),
                Triple(StatusType.FRIEND, listOf(5), 0),
            ),
            base = Status(40, 27, 36, 16, 25, 63),
            scenario = Status(14, 9, 14, 4, 7, 25),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 3)
            .setFacility(StatusType.STAMINA, 2)

        // 8
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 4), 0),
                Triple(StatusType.STAMINA, listOf(), 1),
                Triple(StatusType.POWER, listOf(2), 0),
                Triple(StatusType.GUTS, listOf(3), 3),
                Triple(StatusType.WISDOM, listOf(0, 5), 2),
                Triple(StatusType.FRIEND, listOf(), 3),
            ),
            base = Status(52, 30, 39, 20, 37, 91),
            scenario = Status(41, 24, 33, 15, 25, 81),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .setFacility(StatusType.STAMINA, 1)
            .setFacility(StatusType.POWER, 1)
            .setFacility(StatusType.GUTS, 1)
            .updateMujintoStatus { copy(facilities = facilities - StatusType.WISDOM) }
            .setFacility(StatusType.FRIEND, 1)

        // 17
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 4), 0),
                Triple(StatusType.STAMINA, listOf(), 3),
                Triple(StatusType.POWER, listOf(), 0),
                Triple(StatusType.GUTS, listOf(1, 2, 3, 5), 1),
                Triple(StatusType.FRIEND, listOf(), 2),
            ),
            base = Status(42, 21, 27, 24, 23, 61),
            scenario = Status(12, 6, 8, 7, 6, 18),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 2)
            .setFacility(StatusType.STAMINA, 1)
            .setFacility(StatusType.POWER, 1)
            .setFacility(StatusType.GUTS, 2)
            .setFacility(StatusType.WISDOM, 1)
            .setFacility(StatusType.FRIEND, 1)

        // 17
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 2), 3),
                Triple(StatusType.STAMINA, listOf(5), 0),
                Triple(StatusType.POWER, listOf(), 1),
                Triple(StatusType.GUTS, listOf(0, 3), 0),
                Triple(StatusType.WISDOM, listOf(), 1),
                Triple(StatusType.FRIEND, listOf(4), 1),
            ),
            base = Status(34, 22, 21, 23, 24, 49),
            scenario = Status(5, 1, 3, 2, 1, 7),
        )
    }
}

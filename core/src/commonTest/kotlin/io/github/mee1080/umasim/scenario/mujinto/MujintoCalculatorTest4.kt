package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest4 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[トレセン学園]桐生院葵" to 4,
        "[トレセン学園]秋川理事長" to 4,
        "[URA職員]佐岳メイ" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
        "[プロデューサー]都留岐涼花" to 4,
        "[イベントプロデューサー]ライトハロー" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.FRIEND, 1)
            .copy(motivation = 2)

        // 0 メイ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 3, 4, 5), 0),
            ),
            base = Status(7, 9, 6, 7, 12, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 75,
        )

        // 1 理事長以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 4, 5), 0),
            ),
            base = Status(7, 9, 6, 7, 12, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 75,
        )

        // 2 つるぎ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 5), 0),
            ),
            base = Status(7, 9, 6, 7, 12, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 75,
        )

        // 3 タッカー以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(7, 7, 6, 5, 12, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 75,
        )

        // 4 ハロー以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 4), 0),
            ),
            base = Status(7, 9, 6, 7, 12, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 75,
        )

        // 5 桐生院以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(1, 2, 3, 4, 5), 0),
            ),
            base = Status(7, 9, 6, 7, 12, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 75,
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .setFacility(StatusType.POWER, 1)
            .setFacility(StatusType.WISDOM, 1)

        // 7
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 2),
                Triple(StatusType.POWER, listOf(1), 4),
                Triple(StatusType.WISDOM, listOf(5), 0),
                Triple(StatusType.FRIEND, listOf(0, 2, 4), 2),
            ),
            base = Status(11, 11, 10, 8, 18, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 107,
        )

        // 8
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 2),
                Triple(StatusType.POWER, listOf(2), 2),
                Triple(StatusType.WISDOM, listOf(0), 2),
                Triple(StatusType.FRIEND, listOf(1, 3, 4, 5), 1),
            ),
            base = Status(11, 11, 9, 8, 17, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 107,
        )

        // 9
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2), 1),
                Triple(StatusType.POWER, listOf(1), 1),
                Triple(StatusType.WISDOM, listOf(4), 3),
                Triple(StatusType.FRIEND, listOf(0, 3, 5), 2),
            ),
            base = Status(11, 11, 9, 8, 18, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 107,
        )

        // 10
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 3),
                Triple(StatusType.POWER, listOf(2), 1),
                Triple(StatusType.WISDOM, listOf(), 1),
                Triple(StatusType.FRIEND, listOf(0, 3, 4, 5), 1),
            ),
            base = Status(11, 10, 9, 8, 16, 17),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 107,
        )

        // 11
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 2),
                Triple(StatusType.POWER, listOf(5), 2),
                Triple(StatusType.WISDOM, listOf(), 1),
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 4), 1),
            ),
            base = Status(11, 10, 9, 8, 16, 17),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 100,
        )

        // 18
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 0),
                Triple(StatusType.POWER, listOf(3), 3),
                Triple(StatusType.WISDOM, listOf(0), 3),
                Triple(StatusType.FRIEND, listOf(1, 2, 4), 2),
            ),
            base = Status(11, 11, 10, 8, 18, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 107,
        )

        // 19
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 2),
                Triple(StatusType.POWER, listOf(2), 3),
                Triple(StatusType.WISDOM, listOf(0), 3),
                Triple(StatusType.FRIEND, listOf(1, 4, 5), 2),
            ),
            base = Status(12, 11, 10, 8, 18, 19),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 107,
        )

        // 20
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 0),
                Triple(StatusType.POWER, listOf(5), 4),
                Triple(StatusType.WISDOM, listOf(1), 2),
                Triple(StatusType.FRIEND, listOf(0, 2, 4), 2),
            ),
            base = Status(12, 11, 10, 8, 18, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 107,
        )

        // 21
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 1),
                Triple(StatusType.POWER, listOf(1), 4),
                Triple(StatusType.WISDOM, listOf(5), 4),
                Triple(StatusType.FRIEND, listOf(0, 2, 4), 2),
            ),
            base = Status(12, 11, 10, 9, 19, 19),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 107,
        )

    }
}

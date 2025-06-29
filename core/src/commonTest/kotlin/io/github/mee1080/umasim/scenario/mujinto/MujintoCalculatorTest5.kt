package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest5 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[共に同じ道を！]桐生院葵" to 4,
        "[謹製ッ！特大夢にんじん！]秋川理事長" to 4,
        "[L'aubeは迫りて]佐岳メイ" to 4,
        "[無人島PJ責任者]タッカーブライン" to 0,
        "[共に描くキラメキ]都留岐涼花" to 4,
        "[ブスッといっとく？]安心沢刺々美" to 1,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .copy(motivation = 2)

        // 26 不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 1),
            ),
            base = Status(8, 7, 5, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 不在+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 2),
            ),
            base = Status(8, 7, 5, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 21 不在+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 2),
            ),
            base = Status(8, 7, 6, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 12 葵+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 0),
            ),
            base = Status(8, 7, 6, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 9 葵+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 1),
            ),
            base = Status(9, 7, 6, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 0 葵+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 3),
            ),
            base = Status(9, 7, 6, 5, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 7 理事長+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 0),
            ),
            base = Status(10, 7, 6, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 7 理事長+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 1),
            ),
            base = Status(10, 7, 6, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 20 理事長+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 2),
            ),
            base = Status(11, 7, 6, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 14 理事長+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 3),
            ),
            base = Status(11, 7, 6, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 27 理事長+4
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 3),
            ),
            base = Status(11, 8, 6, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 17 メイ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2), 1),
            ),
            base = Status(9, 7, 6, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 22 メイ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2), 2),
            ),
            base = Status(9, 7, 6, 5, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 11 メイ+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2), 1),
            ),
            base = Status(9, 7, 6, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 10 タッカー+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 0),
            ),
            base = Status(9, 7, 6, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 タッカー+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 1),
            ),
            base = Status(9, 7, 6, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 16 タッカー+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 3),
            ),
            base = Status(9, 7, 6, 5, 13, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 5 都留岐+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 1),
            ),
            base = Status(9, 7, 6, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4 都留岐+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 2),
            ),
            base = Status(10, 7, 6, 5, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 25 都留岐+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 3),
            ),
            base = Status(10, 7, 6, 5, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 19 都留岐+4
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 4),
            ),
            base = Status(10, 8, 6, 5, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 3 安心沢+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 0),
            ),
            base = Status(9, 7, 6, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 15 安心沢+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 1),
            ),
            base = Status(9, 7, 6, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 6 安心沢+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 3),
            ),
            base = Status(9, 7, 6, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 18 安心沢+4
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 4),
            ),
            base = Status(9, 8, 6, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )
    }
}

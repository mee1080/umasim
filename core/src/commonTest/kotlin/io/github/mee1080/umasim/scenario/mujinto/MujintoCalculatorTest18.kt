package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest18 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[ギャラルホルンを磨き上げよ]タニノギムレット" to 4,
        "[トレセン学園]ラッキーライラック" to 4,
        "[トレセン学園]デアリングタクト" to 4,
        "[トレセン学園]シーザリオ" to 4,
        "[トレセン学園]ネオユニヴァース" to 4,
        "[小さなカップに想いをこめて]ニシノフラワー" to 1,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.FRIEND, 1)
            .copy(motivation = 2)

        // 3 ララ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 4, 5), 0),
            ),
            base = Status(13, 7, 6, 5, 15, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4 タクト以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 3, 4, 5), 0),
            ),
            base = Status(14, 7, 6, 5, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 ザリオ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(14, 7, 6, 5, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 0 ユニ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 5), 0),
            ),
            base = Status(14, 7, 6, 5, 15, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 フラワー以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 4), 0),
            ),
            base = Status(13, 7, 6, 5, 15, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.WISDOM, 1)
            .setRelation(0, 80)
            .setRelation(4, 80)
            .setRelation(5, 80)

        // 5 不在+1/ララ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(), 1),
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 4, 5), 0),
            ),
            base = Status(13, 7, 6, 5, 16, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 13 ギム+0/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(0), 0),
                Triple(StatusType.FRIEND, listOf(1, 2, 3, 4, 5), 0),
            ),
            base = Status(17, 8, 6, 6, 22, 18),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        // 6 ララ+タクト+0/他+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1, 2), 0),
                Triple(StatusType.FRIEND, listOf(0, 3, 4, 5), 1),
            ),
            base = Status(16, 8, 6, 6, 19, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 8 ララ+ザリオ+フラワー+0/他+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1, 3, 5), 0),
                Triple(StatusType.FRIEND, listOf(0, 2, 4), 2),
            ),
            base = Status(19, 8, 7, 6, 28, 20),
            scenario = Status(0, 0, 0, 0, 1, 1),
        )

        // 14 ララ+ユニ+0/他+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1, 4), 0),
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 5), 1),
            ),
            base = Status(18, 8, 7, 6, 23, 18),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        // 17 ララ+ユニ+2/他+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1, 4), 2),
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 5), 1),
            ),
            base = Status(18, 8, 7, 6, 24, 19),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        // 16 ララ+フラワー+1/他+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1, 5), 1),
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 4), 1),
            ),
            base = Status(19, 8, 7, 6, 26, 20),
            scenario = Status(0, 0, 0, 0, 1, 1),
        )

        // 7 タクト+2/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(2), 2),
                Triple(StatusType.FRIEND, listOf(0, 1, 3, 4, 5), 0),
            ),
            base = Status(16, 8, 6, 6, 18, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 12 タクト+3/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(2), 3),
                Triple(StatusType.FRIEND, listOf(0, 1, 3, 4, 5), 0),
            ),
            base = Status(16, 8, 7, 6, 19, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 18 ザリオ+0/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(3), 0),
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(15, 7, 6, 5, 18, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 11 ザリオ+3/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(3), 3),
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(16, 8, 7, 6, 19, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 9 ユニ+1/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(4), 2),
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 5), 0),
            ),
            base = Status(17, 8, 7, 6, 22, 18),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        // 19 フラワー+0/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(5), 0),
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 4), 0),
            ),
            base = Status(18, 8, 6, 6, 23, 19),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        // 10 フラワー+1/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(5), 1),
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 4), 0),
            ),
            base = Status(18, 8, 7, 6, 24, 19),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(2, 80)

        // 20 ギム+タクト+1/他+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(0, 2), 1),
                Triple(StatusType.FRIEND, listOf(1, 3, 4, 5), 1),
            ),
            base = Status(21, 9, 7, 6, 30, 22),
            scenario = Status(1, 0, 0, 0, 1, 1),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(1, 80)
            .setRelation(5, 100)

        // 22 ララ+1/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1), 1),
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 4, 5), 0),
            ),
            base = Status(17, 8, 7, 6, 27, 18),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        // 21 ザリオ+1/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(3), 1),
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(15, 8, 6, 5, 22, 15),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(3, 80)

        // 24 ララ+タクト+1/他+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(1, 2), 1),
                Triple(StatusType.FRIEND, listOf(0, 3, 4, 5), 1),
            ),
            base = Status(20, 9, 7, 6, 35, 21),
            scenario = Status(1, 0, 0, 0, 1, 1),
        )

        // 24 ギム+ララ+ユニ+1/他+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(0, 1, 4), 1),
                Triple(StatusType.FRIEND, listOf(2, 3, 5), 2),
            ),
            base = Status(24, 9, 8, 7, 48, 26),
            scenario = Status(1, 0, 0, 0, 2, 1),
        )

        // 23 タクト+3/他
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.WISDOM, listOf(2), 3),
                Triple(StatusType.FRIEND, listOf(0, 1, 3, 4, 5), 0),
            ),
            base = Status(18, 8, 7, 6, 28, 18),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

    }
}

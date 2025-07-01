package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest9 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Devilish Whispers]スティルインラブ" to 4,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[大望は飛んでいく]エルコンドルパサー" to 4,
        "[The frontier]ジャングルポケット" to 4,
        "[掲げよ、燃え盛る灯を]カツラギエース" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .copy(motivation = 2)

        // 16 不在+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 1),
            ),
            base = Status(10, 7, 5, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 17 不在+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 1),
            ),
            base = Status(11, 7, 5, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 23 不在+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 2),
            ),
            base = Status(11, 7, 5, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 22 アイ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 2),
            ),
            base = Status(14, 7, 6, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 19 ヴィブ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 0),
            ),
            base = Status(14, 7, 8, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 21 ヴィブ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 1),
            ),
            base = Status(14, 7, 8, 5, 12, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4 エル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 1),
            ),
            base = Status(14, 7, 6, 5, 12, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 34 ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 0),
            ),
            base = Status(12, 7, 7, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 6 ポケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 1),
            ),
            base = Status(12, 7, 8, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 26 ポケ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 2),
            ),
            base = Status(12, 7, 8, 5, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 13 エース+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 0),
            ),
            base = Status(11, 7, 6, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 39 エース+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 0),
            ),
            base = Status(11, 7, 6, 5, 12, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 6 エース+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(5), 3),
            ),
            base = Status(12, 7, 6, 5, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 3 アイ+ヴィブ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2), 0),
            ),
            base = Status(18, 7, 9, 5, 13, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 15 スティル+エース+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 5), 0),
            ),
            base = Status(15, 7, 8, 5, 13, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 11 ヴィブ+エース+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 5), 0),
            ),
            base = Status(15, 7, 8, 5, 13, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 10 エル+エース+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3, 5), 0),
            ),
            base = Status(14, 7, 6, 5, 13, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 18 アイ+スティル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1), 1),
            ),
            base = Status(18, 8, 9, 5, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 9 スティル+エル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 3), 1),
            ),
            base = Status(18, 8, 9, 5, 13, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 5 ヴィブ+エル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 3), 1),
            ),
            base = Status(18, 8, 9, 5, 13, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 12 ヴィブ+エル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 5), 1),
            ),
            base = Status(15, 8, 10, 5, 13, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 ヴィブ+エース+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 5), 1),
            ),
            base = Status(15, 8, 8, 5, 13, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 ポケ+エース+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4, 5), 1),
            ),
            base = Status(13, 8, 8, 5, 13, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 ポケ+エース+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4, 5), 2),
            ),
            base = Status(13, 8, 8, 6, 13, 13),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 20 アイ+スティル+ヴィブ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1, 2), 0),
            ),
            base = Status(22, 8, 12, 6, 13, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 30 ヴィブ+ポケ+エース+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 4, 5), 1),
            ),
            base = Status(17, 8, 11, 6, 14, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 0 アイ以外5人
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 2, 3, 4, 5), 0),
            ),
            base = Status(26, 9, 16, 6, 15, 22),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(3, 80)

        // 24 アイ+友情エル+3
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 3), 3),
            ),
            base = Status(21, 8, 8, 6, 13, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 25 アイ+ヴィブ+友情エル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2, 3), 1),
            ),
            base = Status(25, 8, 11, 6, 14, 17),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(0, 80)

        // 28 友情アイ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 3), 3),
            ),
            base = Status(17, 7, 9, 5, 12, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 37 友情アイ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 3), 3),
            ),
            base = Status(17, 7, 9, 5, 13, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 29 友情アイ+スティル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1), 1),
            ),
            base = Status(22, 8, 12, 5, 13, 19),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 32 友情アイ+ヴィブ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2), 0),
            ),
            base = Status(21, 7, 12, 5, 13, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 40 友情アイ+ヴィブ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2), 1),
            ),
            base = Status(22, 8, 12, 5, 13, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 38 友情アイ+ポケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 4), 1),
            ),
            base = Status(19, 8, 12, 5, 13, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 33 友情アイ+ポケ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 4), 2),
            ),
            base = Status(19, 8, 12, 6, 13, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(3, 100)

        // 36 ヴィブ+絆100エル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 3), 0),
            ),
            base = Status(21, 9, 11, 7, 14, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

    }
}

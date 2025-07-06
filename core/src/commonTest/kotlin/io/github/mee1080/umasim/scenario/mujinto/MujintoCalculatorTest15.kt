package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest15 : MujintoCalculatorTest(
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
            .setRelation(0, 80)
            .setRelation(1, 80)
            .setRelation(2, 80)
            .setRelation(3, 80)
            .setRelation(4, 80)

        // 0 スティル+非友情エース+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 5), 0),
            ),
            base = Status(19, 7, 9, 5, 13, 16),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4 スティル+非友情エース+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 5), 2),
            ),
            base = Status(20, 8, 10, 6, 13, 17),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 80エル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 0),
            ),
            base = Status(15, 7, 6, 5, 12, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(3, 100)
            .setRelation(5, 80)

        // 7 80ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 0),
            ),
            base = Status(14, 7, 8, 5, 12, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1 80ポケ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 2),
            ),
            base = Status(14, 7, 9, 5, 13, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 12 アイ+80ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 4), 0),
            ),
            base = Status(22, 7, 13, 5, 13, 20),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 11 アイ+スティル+80ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1, 4), 0),
            ),
            base = Status(35, 8, 19, 6, 13, 31),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 6 アイ+ヴィブ+80ポケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2, 4), 1),
            ),
            base = Status(36, 8, 20, 6, 14, 29),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 9 スティル+80ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 4), 0),
            ),
            base = Status(23, 7, 13, 5, 13, 19),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 8 ヴィブ+80ポケ+エース+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 4, 5), 1),
            ),
            base = Status(35, 8, 15, 6, 14, 24),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setRelation(4, 100)

        // 5 アイ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 0),
            ),
            base = Status(17, 7, 9, 5, 12, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 19 アイ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 1),
            ),
            base = Status(17, 7, 9, 5, 12, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 10 アイ+2
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 2),
            ),
            base = Status(17, 7, 9, 5, 13, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 14 アイ+ヴィブ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2), 0),
            ),
            base = Status(28, 7, 14, 5, 13, 21),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2 スティル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 0),
            ),
            base = Status(18, 7, 9, 5, 12, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 20 スティル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 1),
            ),
            base = Status(18, 7, 9, 5, 12, 14),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 17 アイ+スティル+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1), 0),
            ),
            base = Status(27, 7, 13, 5, 13, 23),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 24 アイ+スティル+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1), 1),
            ),
            base = Status(28, 8, 14, 5, 13, 23),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 13 アイ+スティル+ポケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 1, 4), 1),
            ),
            base = Status(45, 8, 20, 6, 14, 32),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 25 アイ+ヴィブ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0, 2), 0),
            ),
            base = Status(28, 7, 14, 5, 13, 21),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 16 スティル+ヴィブ+エル+ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 2, 3, 4), 0),
            ),
            base = Status(66, 10, 26, 7, 15, 41),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 22 スティル+ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 4), 0),
            ),
            base = Status(29, 7, 13, 5, 13, 19),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 23 スティル+エース+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1, 5), 0),
            ),
            base = Status(26, 7, 10, 5, 13, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 18 ヴィブ+ポケ+0
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(2, 4), 0),
            ),
            base = Status(30, 7, 13, 5, 13, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 15 ポケ+1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(4), 1),
            ),
            base = Status(19, 7, 8, 5, 12, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )
    }
}

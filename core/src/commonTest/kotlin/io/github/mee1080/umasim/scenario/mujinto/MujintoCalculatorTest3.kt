package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest3 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Devilish Whispers]スティルインラブ" to 4,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[The frontier]ジャングルポケット" to 4,
        "[大望は飛んでいく]エルコンドルパサー" to 4,
        "[波間のオフショット]スマートファルコン" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)

        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 1),
            ),
            base = Status(7, 6, 4, 4, 10, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 63,
        )

        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0,1), 0),
            ),
            base = Status(11, 6, 7, 4, 11, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 63,
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 1)
            .setRelation(1, 90)

//        // 1
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(1), 1),
            ),
            base = Status(12, 7, 7, 5, 11, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 79,
        )

        // 2
//        testIslandTraining(
//            baseCalcInfo,
//            listOf(
//                Triple(StatusType.SPEED, listOf(3), 1),
//            ),
//            base = Status(8, 7, 7, 5, 11, 9),
//            scenario = Status(0, 0, 0, 0, 0, 0),
//            pioneerPt = 66,
//        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)
            .setFacility(StatusType.STAMINA, 1)
            .setFacility(StatusType.GUTS, 1)

        // 5
//        testIslandTraining(
//            baseCalcInfo,
//            listOf(
//                Triple(StatusType.SPEED, listOf(0, 1, 3, 5), 0),
//                Triple(StatusType.STAMINA, listOf(), 1),
//                Triple(StatusType.GUTS, listOf(2), 3),
//            ),
//            base = Status(29, 11, 22, 9, 15, 31),
//            scenario = Status(0, 0, 0, 0, 0, 0),
//            pioneerPt = 104,
//        )

        // 6
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 1),
                Triple(StatusType.STAMINA, listOf(), 1),
                Triple(StatusType.GUTS, listOf(3, 4), 2),
            ),
            base = Status(15, 10, 9, 9, 14, 18),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 81,
        )

    }
}

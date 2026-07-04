package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class RamenCalculatorTest5 : RamenCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[心覚えし、京の華]エアグルーヴ" to 4,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[Devilish Whispers]スティルインラブ" to 4,
        "[The frontier]ジャングルポケット" to 4,
        "[永久の誓い、永久の輝き]サトノダイヤモンド" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo
            .setPeriod(1, false)
            .setRelation(0, 100)
            .setRelation(1, 100)
            .setRelation(2, 100)
            .setRelation(3, 100)
            .setRelation(4, 100)
            .setRelation(5, 100)

        // WS000000.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 1, 0, 1, 3, 4,
            base = Status(100, 0, 45, 0, 0, 88),
            scenario = Status(3, 0, 1, 0, 0, 2),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(RamenRegion.NAKAYAMA)
            .setExcitementPt(400)

        // WS000004.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 1, 0, 1, 3, 4,
            base = Status(100, 0, 45, 0, 0, 88),
            scenario = Status(68, 0, 30, 0, 0, 60),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)
            .setActiveTastingRegion(RamenRegion.NAKAYAMA)
            .setExcitementPt(840)

        // WS000006.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 1, 1, 3, 5,
            base = Status(100, 0, 58, 0, 0, 79),
            scenario = Status(71, 0, 41, 0, 0, 56),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 0)
            .setPeriod(2, false)
            .setActiveTastingRegion(null)
            .setExcitementPt(0)

        // WS000007.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 0, 2, 3, 4, 5,
            base = Status(100, 0, 85, 0, 0, 100),
            scenario = Status(5, 0, 4, 0, 0, 5),
        )

        baseCalcInfo = baseCalcInfo
            .setActiveTastingRegion(RamenRegion.SAPPORO2)
            .setExcitementPt(500)

        // WS000009.png
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 0, 2, 3, 4, 5,
            base = Status(100, 0, 85, 0, 0, 100),
            scenario = Status(140, 0, 119, 0, 0, 140),
        )
    }
}

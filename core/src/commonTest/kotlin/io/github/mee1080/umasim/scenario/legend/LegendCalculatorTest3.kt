package io.github.mee1080.umasim.scenario.legend

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class LegendCalculatorTest3 : LegendCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[Devilish Whispers]スティルインラブ" to 4,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[朝焼け苺の畑にて]ニシノフラワー" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[百花の願いをこの胸に]サトノダイヤモンド" to 4,
        "[導きの光]伝説の体現者" to 4,
    )
) {

    @Test
    fun test() {
        val state = state.setLegendMastery(LegendMember.Blue)

        var baseCalcInfo = state.baseCalcInfo
            .copy(motivation = 3, totalRelation = 570)
            .setRelation(0, 100)
            .setRelation(1, 100)
            .setRelation(2, 100)
            .setRelation(3, 70)
            .setRelation(4, 100)
            .setRelation(5, 100)
            .addBuff("アイドルステップ")
            .addBuff("衰えぬ情熱")
            .addBuff("慈愛の微笑み", false)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0, 0, 1,
            base = Status(81, 0, 30, 0, 0, 42),
            scenario = Status(8, 0, 3, 0, 0, 5),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 0,
            base = Status(0, 18, 0, 13, 0, 10),
            scenario = Status(0, 1, 0, 1, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 0,
            base = Status(0, 9, 26, 0, 0, 10),
            scenario = Status(0, 0, 1, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 0, 3, 5,
            base = Status(8, 0, 9, 36, 0, 16),
            scenario = Status(0, 0, 1, 2, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 2, 4,
            base = Status(17, 0, 0, 0, 46, 21),
            scenario = Status(2, 0, 0, 0, 5, 2),
        )
    }
}

package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest1 : MujintoCalculatorTest(
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
        var baseCalcInfo = state.baseCalcInfo

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 3,
            base = Status(12, 0, 2, 0, 0, 8),
            scenario = Status(),
        )
    }
}

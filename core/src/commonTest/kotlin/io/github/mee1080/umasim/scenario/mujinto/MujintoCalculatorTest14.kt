package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest14 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[無垢の白妙]デアリングタクト" to 4,
        "[百花の願いをこの胸に]サトノダイヤモンド" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[緋色の君へ風が吹く]ダイワスカーレット" to 4,
        "[いつか深まる若草]エアメサイア" to 4,
        "[#夏 #新しい自分]ダイタクヘリオス" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.FRIEND, 1)
            .copy(motivation = 2)
            .copy(speedSkillCount = 3)

        // 0 タクト以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(1, 2, 3, 4, 5), 0),
            ),
            base = Status(12, 8, 6, 6, 21, 19),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        // 2 ダイヤ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 4, 5), 0),
            ),
            base = Status(14, 8, 6, 5, 19, 22),
            scenario = Status(0, 0, 0, 0, 0, 1),
        )

        // 1 タイシン以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 3, 4, 5), 0),
            ),
            base = Status(14, 8, 6, 5, 21, 20),
            scenario = Status(0, 0, 0, 0, 1, 1),
        )

        // 4 ダスカ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(14, 8, 6, 6, 17, 21),
            scenario = Status(0, 0, 0, 0, 0, 1),
        )

        // 5 メサイア以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 5), 0),
            ),
            base = Status(14, 8, 6, 6, 21, 23),
            scenario = Status(0, 0, 0, 0, 1, 1),
        )

        // 3 ヘリオス以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 4), 0),
            ),
            base = Status(12, 8, 6, 6, 20, 23),
            scenario = Status(0, 0, 0, 0, 1, 1),
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.FRIEND, 2)

        // 6 タクト以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(1, 2, 3, 4, 5), 0),
            ),
            base = Status(12, 8, 6, 6, 21, 19),
            scenario = Status(1, 0, 0, 0, 2, 1),
        )

        // 7 ダイヤ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 2, 3, 4, 5), 0),
            ),
            base = Status(14, 8, 6, 5, 19, 22),
            scenario = Status(1, 0, 0, 0, 1, 2),
        )

        // 8 タイシン以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 3, 4, 5), 0),
            ),
            base = Status(14, 8, 6, 5, 21, 20),
            scenario = Status(1, 0, 0, 0, 2, 2),
        )

        // 10 ダスカ以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(14, 8, 6, 6, 17, 21),
            scenario = Status(1, 0, 0, 0, 1, 2),
        )

        // 11 メサイア以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 5), 0),
            ),
            base = Status(14, 8, 6, 6, 21, 23),
            scenario = Status(1, 0, 0, 0, 2, 2),
        )

        // 9 ヘリオス以外
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.FRIEND, listOf(0, 1, 2, 3, 4), 0),
            ),
            base = Status(12, 8, 6, 6, 20, 23),
            scenario = Status(1, 0, 0, 0, 2, 2),
        )

    }
}

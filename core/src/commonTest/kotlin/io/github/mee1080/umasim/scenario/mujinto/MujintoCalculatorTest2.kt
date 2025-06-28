package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest2 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Cocoon]エアシャカール" to 4,
        "[Chill chill night*]マヤノトップガン" to 1,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[無垢の白妙]デアリングタクト" to 4,
        "[袖振り合えば福となる♪]マチカネフクキタル" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)

        // 2
        testIslandTraining(
            baseCalcInfo,
            listOf(
            ),
            base = Status(7, 7, 5, 5, 12, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
            pioneerPt = 60,
        )

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 1)
            .setFacility(StatusType.NONE, 1)

        // 8
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(0), 0),
                Triple(StatusType.NONE, listOf(1, 2, 3, 4, 5), 0),
            ),
            base = Status(14, 8, 9, 7, 14, 24),
            scenario = Status(0, 0, 0, 0, 0, 1),
            pioneerPt = 78,
        )

        // TODO 第1回評価会大好評

        // 10
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(3), 1),
                Triple(StatusType.NONE, listOf(0, 1, 2, 4, 5), 0),
            ),
            base = Status(13, 8, 9, 8, 14, 25),
            scenario = Status(0, 0, 0, 0, 0, 1),
            pioneerPt = 85,
        )

        // 12
        testIslandTraining(
            baseCalcInfo,
            listOf(
                Triple(StatusType.SPEED, listOf(), 1),
                Triple(StatusType.NONE, listOf(1, 2, 3, 4, 5), 0),
            ),
            base = Status(11, 8, 8, 7, 13, 20),
            scenario = Status(0, 0, 0, 0, 0, 1),
            pioneerPt = 81,
        )
    }
}

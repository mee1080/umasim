package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class ShuntaisaiTrainingTest1 : ShuntaisaiTrainingTestBase(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[いとどめでたき、まつりかな]吾妻" to 4,
        "[心覚えし、京の華]エアグルーヴ" to 4,
        "[その執念は怒濤が如く]メイショウドトウ" to 4,
        "[As if Guided]エフフォーリア" to 4,
        "[Innovator]フォーエバーヤング" to 4,
        "[Time of Departure]マヤノトップガン" to 0,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        // WS000001.png
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0, 2,
            base = Status(0, 12, 0, 3, 0, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)

        // WS000012.png
        testEnmokuTraining(
            baseCalcInfo, StatusType.SPEED, 1, 1, 0, 1, 3, 4,
            base = Status(44, 0, 10, 0, 0, 44),
            scenario = Status(11, 10, 10, 10, 10, 8),
        )
    }
}

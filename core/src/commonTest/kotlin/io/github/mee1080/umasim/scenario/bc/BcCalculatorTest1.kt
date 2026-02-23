package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class BcCalculatorTest1 : BCCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Devilish Whispers]スティルインラブ" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[無垢の白妙]デアリングタクト" to 4,
        "[百花の願いをこの胸に]サトノダイヤモンド" to 4,
        "[ゆこま旅館女将]保科健子" to 0,
    ),
    teamMemberList = listOf(
        "エスポワールシチー",
        "ラブズオンリーユー",
        "レッドディザイア",
    ),
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .initBcStatus()
            .copy(motivation = 2)

        // 39
        testBcTraining(
            baseCalcInfo, StatusType.SPEED, 1,
            support = listOf(0), guest = listOf(),
            base = Status(19, 0, 3, 0, 0, 12),
            scenario = Status(5, 2, 0, 0, 4, 1),
        )
    }
}

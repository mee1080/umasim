package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class MujintoCalculatorTest20 : MujintoCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[Chill chill night*]マヤノトップガン" to 1,
        "[無垢の白妙]デアリングタクト" to 4,
        "[Devilish Whispers]スティルインラブ" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setFacility(StatusType.SPEED, 2)
            .setFacility(StatusType.STAMINA, 3)
            .setFacility(StatusType.POWER, 3)
            .setFacility(StatusType.GUTS, 2)
            .setFacility(StatusType.WISDOM, 1)
            .setFacility(StatusType.FRIEND, 1)
            .setRelation(0, 100)
            .setRelation(1, 100)
            .setRelation(2, 100)
            .setRelation(3, 100)
            .setRelation(4, 100)
            .setRelation(5, 100)
            .copy(motivation = 2, isLevelUpTurn = true)
            .updateMujintoStatus { updatePhase(3) }

        // 0
        testCampTraining(
            baseCalcInfo, StatusType.SPEED, 2, 2, 4,
            base = Status(73, 15, 27, 12, 7, 57),
            scenario = Status(7, 1, 2, 1, 0, 5),
        )

        // 1
        testCampTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 1, 3,
            base = Status(8, 32, 12, 13, 8, 39),
            scenario = Status(2, 9, 3, 3, 2, 11),
        )

        // 2
        testCampTraining(
            baseCalcInfo, StatusType.POWER, 1,
            base = Status(5, 10, 25, 5, 3, 23),
            scenario = Status(1, 3, 7, 1, 0, 6),
        )

        // 3
        testCampTraining(
            baseCalcInfo, StatusType.GUTS, 2,
            base = Status(9, 6, 11, 23, 3, 25),
            scenario = Status(1, 1, 2, 4, 0, 5),
        )

        // 4
        testCampTraining(
            baseCalcInfo, StatusType.WISDOM, 2, 0, 5,
            base = Status(16, 11, 13, 10, 24, 37),
            scenario = Status(1, 1, 1, 1, 2, 3),
        )

        // 5
        testCampTraining(
            baseCalcInfo, StatusType.SPEED, 2, 0, 2, 3,
            base = Status(57, 15, 26, 10, 6, 47),
            scenario = Status(5, 1, 2, 1, 0, 4),
        )

        // 6
        testCampTraining(
            baseCalcInfo, StatusType.STAMINA, 2,
            base = Status(5, 22, 7, 10, 3, 25),
            scenario = Status(1, 6, 2, 3, 0, 7),
        )

        // 7
        testCampTraining(
            baseCalcInfo, StatusType.GUTS, 1, 5,
            base = Status(10, 9, 12, 28, 4, 28),
            scenario = Status(2, 1, 2, 5, 0, 5),
        )

        // 8
        testCampTraining(
            baseCalcInfo, StatusType.WISDOM, 3,
            base = Status(12, 6, 8, 6, 17, 26),
            scenario = Status(1, 0, 0, 0, 1, 2),
        )
    }
}

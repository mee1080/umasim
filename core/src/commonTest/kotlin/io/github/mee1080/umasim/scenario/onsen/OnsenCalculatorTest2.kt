package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class OnsenCalculatorTest2 : OnsenCalculatorTest(
    chara = Triple("[リアライズ・ルーン]スイープトウショウ", 5, 5),
    supportCardList = listOf(
        "[Devilish Whispers]スティルインラブ" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[今宵、我が君のために]デュランダル" to 0,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[ゆるり、ゆこま旅館]保科健子" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo
            .addGensen("疾駆の湯")
            .addGensen("明晰の湯")
            .setOnsenActive(true)
            .copy(motivation = 2)

        // 0
        testOnsenTraining(
            baseCalcInfo, StatusType.GUTS, 3, 3,
            base = Status(3, 0, 3, 30, 0, 10),
            scenario = Status(2, 1, 0, 3, 2, 1),
            digBonus = listOf(StratumType.SAND to 1),
        )

        baseCalcInfo = baseCalcInfo
            .setOnsenActive(false)

        // 2
        testOnsenTraining(
            baseCalcInfo, StatusType.GUTS, 3,
            base = Status(2, 0, 2, 23, 0, 8),
            scenario = Status(4, 2, 0, 0, 4, 0),
            digBonus = listOf(StratumType.SAND to 2),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 1)

        // 3
        testOnsenTraining(
            baseCalcInfo, StatusType.GUTS, 4,
            base = Status(3, 0, 3, 25, 0, 7),
            scenario = Status(4, 0, 2, 4, 0, 0),
            digBonus = listOf(StratumType.SOIL to 2),
        )

        // 4
        testOnsenTraining(
            baseCalcInfo, StatusType.GUTS, 4, 3,
            base = Status(4, 0, 4, 30, 0, 9),
            scenario = Status(4, 0, 2, 4, 0, 0),
            digBonus = listOf(StratumType.SOIL to 2),
        )

        baseCalcInfo = baseCalcInfo
            .addGensen("駿閃の古湯")
            .setOnsenActive(true)
            .setRelation(0, 90)
            .setRelation(2, 90)
            .setRelation(5, 75)

        // 5
        testOnsenTraining(
            baseCalcInfo, StatusType.STAMINA, 4, 1, 5,
            base = Status(0, 33, 0, 10, 0, 14),
            scenario = Status(4, 5, 0, 1, 4, 1),
            digBonus = listOf(StratumType.SAND to 2),
        )

        baseCalcInfo = baseCalcInfo
            .setOnsenActive(false)
            .copy(motivation = 2)

        // 7
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 4, 0, 2,
            base = Status(11, 0, 0, 0, 28, 17),
            scenario = Status(0, 2, 4, 0, 4, 0),
            digBonus = listOf(StratumType.ROCK to 2),
        )

        baseCalcInfo = baseCalcInfo
            .addGensen("剛脚の古湯")
            .setOnsenActive(true)
            .setRelation(1, 90)
            .setRelation(3, 90)
            .setRelation(4, 90)

        // 9
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 4, 0, 3,
            base = Status(60, 0, 15, 0, 0, 24),
            scenario = Status(36, 2, 8, 0, 4, 12),
            digBonus = listOf(StratumType.SAND to 2),
        )

        // 10
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 1, 4,
            base = Status(8, 0, 0, 0, 42, 22),
            scenario = Status(6, 2, 0, 0, 17, 7),
            digBonus = listOf(StratumType.SAND to 2),
        )

        // 11
        testOnsenTraining(
            baseCalcInfo, StatusType.STAMINA, 5,
            base = Status(0, 27, 0, 10, 0, 8),
            scenario = Status(2, 3, 0, 1, 2, 0),
            digBonus = listOf(StratumType.SAND to 1),
        )

        // 12
        testOnsenTraining(
            baseCalcInfo, StatusType.GUTS, 5, 2, 5,
            base = Status(12, 0, 10, 67, 0, 21),
            scenario = Status(10, 2, 5, 36, 4, 11),
            digBonus = listOf(StratumType.SAND to 2),
        )

        baseCalcInfo = baseCalcInfo
            .setOnsenActive(false)

        // 13
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 4, 2,
            base = Status(32, 0, 9, 0, 0, 11),
            scenario = Status(6, 3, 0, 0, 6, 0),
            digBonus = listOf(StratumType.SAND to 3),
        )

        // 15
        testOnsenTraining(
            baseCalcInfo, StatusType.POWER, 4, 1, 4,
            base = Status(0, 18, 60, 0, 0, 23),
            scenario = Status(6, 3, 0, 0, 6, 0),
            digBonus = listOf(StratumType.SAND to 3),
        )

        // 16
        testOnsenTraining(
            baseCalcInfo, StatusType.GUTS, 5, 5,
            base = Status(7, 0, 5, 42, 0, 12),
            scenario = Status(6, 3, 0, 0, 6, 0),
            digBonus = listOf(StratumType.SAND to 3),
        )

        // 17
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 3,
            base = Status(16, 0, 0, 0, 40, 25),
            scenario = Status(6, 3, 0, 0, 6, 0),
            digBonus = listOf(StratumType.SAND to 3),
        )

        baseCalcInfo = baseCalcInfo
            .setOnsenActive(true)

        // 18
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0, 2,
            base = Status(62, 0, 19, 0, 0, 22),
            scenario = Status(37, 0, 12, 4, 0, 11),
            digBonus = listOf(StratumType.SOIL to 2),
        )

        baseCalcInfo = baseCalcInfo
            .setOnsenActive(false)

        // 19
        testOnsenTraining(
            baseCalcInfo, StatusType.POWER, 5, 0, 1,
            base = Status(0, 24, 74, 0, 0, 26),
            scenario = Status(0, 2, 4, 0, 4, 0),
            digBonus = listOf(StratumType.ROCK to 2),
        )
    }
}

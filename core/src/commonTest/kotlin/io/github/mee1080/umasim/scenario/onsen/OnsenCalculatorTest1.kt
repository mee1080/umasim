package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test

class OnsenCalculatorTest1 : OnsenCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[Devilish Whispers]スティルインラブ" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[無垢の白妙]デアリングタクト" to 4,
        "[百花の願いをこの胸に]サトノダイヤモンド" to 4,
        "[ゆこま旅館女将]保科健子" to 0,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        // 0
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(12, 0, 2, 0, 0, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 1
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0, 0, 1, 2, 3,
            base = Status(0, 22, 0, 6, 0, 22),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 2
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0,
            base = Status(0, 3, 13, 0, 0, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 4
        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0, 4,
            base = Status(2, 0, 2, 15, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 5
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0,
            base = Status(2, 0, 0, 0, 11, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 7
        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0, 2,
            base = Status(0, 13, 0, 3, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 8
        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 1,
            base = Status(0, 3, 17, 0, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // 10
        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 0,
            base = Status(3, 0, 0, 0, 13, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .initOnsenState()
            .copy(motivation = 2)

        // 26
        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 0, 1, 3, 4, 5,
            base = Status(42, 0, 10, 0, 0, 31),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo
            .setOnsenActive(true)

        // 39
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            base = Status(19, 0, 3, 0, 0, 12),
            scenario = Status(5, 2, 0, 0, 4, 1),
            digBonus = listOf(StratumType.SAND to 2),
        )

        // 40
        testOnsenTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 4,
            base = Status(0, 16, 0, 4, 0, 10),
            scenario = Status(4, 3, 0, 0, 4, 1),
            digBonus = listOf(StratumType.SAND to 2),
        )

        // 41
        testOnsenTraining(
            baseCalcInfo, StatusType.POWER, 1, 2, 3,
            base = Status(0, 6, 23, 0, 0, 16),
            scenario = Status(4, 2, 2, 0, 4, 1),
            digBonus = listOf(StratumType.SAND to 2),
        )

        // 42
        testOnsenTraining(
            baseCalcInfo, StatusType.GUTS, 1, 1,
            base = Status(4, 0, 5, 20, 0, 12),
            scenario = Status(4, 2, 0, 2, 4, 1),
            digBonus = listOf(StratumType.SAND to 2),
        )

        // 43
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 1,
            base = Status(2, 0, 0, 0, 13, 8),
            scenario = Status(4, 2, 0, 0, 5, 0),
            digBonus = listOf(StratumType.SAND to 2),
        )

        baseCalcInfo = baseCalcInfo
            .addGensen("疾駆の湯")
            .setRelation(0, 80)
            .setRelation(1, 80)
            .setRelation(3, 80)
            .setRelation(4, 80)
            .setRelation(5, 80)
            .setOnsenActive(false)

        // 87
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 2, 4, 5,
            base = Status(9, 0, 0, 0, 42, 35),
            scenario = Status(0, 2, 4, 0, 4, 0),
            digBonus = listOf(StratumType.ROCK to 2),
        )

        // 91
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 2, 3,
            base = Status(21, 0, 5, 0, 0, 12),
            scenario = Status(0, 2, 4, 0, 4, 0),
            digBonus = listOf(StratumType.ROCK to 2),
        )

        baseCalcInfo = baseCalcInfo
            .setOnsenActive(true)
            .setRelation(0, 100)
            .setRelation(1, 100)
            .setRelation(2, 100)
            .setRelation(3, 100)
            .setRelation(4, 100)
            .setRelation(5, 100)

        // 95
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 2, 4,
            base = Status(3, 0, 0, 0, 27, 15),
            scenario = Status(0, 1, 2, 0, 4, 1),
            digBonus = listOf(StratumType.ROCK to 1),
        )

        // 96
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 2, 0, 1, 2,
            base = Status(66, 0, 28, 0, 0, 47),
            scenario = Status(17, 2, 11, 0, 4, 12),
            digBonus = listOf(StratumType.ROCK to 2),
        )

        // 101
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 2, 0, 3, 5,
            base = Status(41, 0, 12, 0, 0, 28),
            scenario = Status(10, 0, 3, 0, 0, 7),
            digBonus = emptyList(),
        )

        // 102
        testOnsenTraining(
            baseCalcInfo, StatusType.POWER, 1, 1, 2,
            base = Status(0, 10, 42, 0, 0, 26),
            scenario = Status(0, 3, 13, 0, 0, 8),
            digBonus = emptyList(),
        )

        // 104
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 2, 0, 1,
            base = Status(54, 0, 19, 0, 0, 35),
            scenario = Status(16, 0, 6, 2, 0, 9),
            digBonus = listOf(StratumType.SOIL to 1),
        )

        // 105
        testOnsenTraining(
            baseCalcInfo, StatusType.POWER, 2, 5,
            base = Status(0, 5, 22, 0, 0, 9),
            scenario = Status(2, 0, 3, 2, 0, 0),
            digBonus = listOf(StratumType.SOIL to 1),
        )

        // 106
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 2, 2, 3, 4,
            base = Status(11, 0, 0, 0, 63, 43),
            scenario = Status(3, 0, 1, 2, 6, 4),
            digBonus = listOf(StratumType.SOIL to 1),
        )

        baseCalcInfo = baseCalcInfo
            .addGensen("明晰の湯")
            .setOnsenActive(false)

        // 107
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 3, 0, 1,
            base = Status(64, 0, 23, 0, 0, 35),
            scenario = Status(2, 1, 0, 0, 2, 0),
            digBonus = listOf(StratumType.SAND to 1),
        )

        // 108
        testOnsenTraining(
            baseCalcInfo, StatusType.STAMINA, 2,
            base = Status(0, 16, 0, 5, 0, 8),
            scenario = Status(2, 1, 0, 0, 2, 0),
            digBonus = listOf(StratumType.SAND to 1),
        )

        // 109
        testOnsenTraining(
            baseCalcInfo, StatusType.POWER, 3,
            base = Status(0, 6, 24, 0, 0, 8),
            scenario = Status(2, 1, 0, 0, 2, 0),
            digBonus = listOf(StratumType.SAND to 1),
        )

        // 110
        testOnsenTraining(
            baseCalcInfo, StatusType.GUTS, 2, 2, 3, 4,
            base = Status(5, 0, 7, 32, 0, 21),
            scenario = Status(4, 2, 0, 0, 4, 0),
            digBonus = listOf(StratumType.SAND to 2),
        )

        // 111
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 3,
            base = Status(2, 0, 0, 0, 15, 8),
            scenario = Status(2, 1, 0, 0, 2, 0),
            digBonus = listOf(StratumType.SAND to 1),
        )

        baseCalcInfo = baseCalcInfo
            .setOnsenActive(true)

        // 107
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 3, 0,
            base = Status(36, 0, 12, 0, 0, 18),
            scenario = Status(13, 2, 3, 0, 4, 4),
            digBonus = listOf(StratumType.SAND to 2),
        )

        // 109
        testOnsenTraining(
            baseCalcInfo, StatusType.POWER, 3, 2,
            base = Status(0, 12, 46, 0, 0, 17),
            scenario = Status(4, 5, 14, 0, 4, 5),
            digBonus = listOf(StratumType.SAND to 2),
        )

        // 111
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 3, 1, 4,
            base = Status(9, 0, 0, 0, 37, 24),
            scenario = Status(6, 2, 0, 0, 15, 7),
            digBonus = listOf(StratumType.SAND to 2),
        )

        baseCalcInfo = baseCalcInfo
            .setOnsenActive(false)

        // 116
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 4, 4,
            base = Status(5, 0, 0, 0, 31, 15),
            scenario = Status(4, 2, 0, 0, 4, 0),
            digBonus = listOf(StratumType.SAND to 2),
        )

        baseCalcInfo = baseCalcInfo
            .addGensen("駿閃の古湯")
            .setOnsenActive(true)

        // 118
        testOnsenTraining(
            baseCalcInfo, StatusType.SPEED, 6, 0, 1, 2, 4,
            base = Status(100, 0, 50, 0, 0, 60),
            scenario = Status(54, 0, 27, 0, 0, 32),
        )

        // 119
        testOnsenTraining(
            baseCalcInfo, StatusType.STAMINA, 6,
            base = Status(0, 27, 0, 10, 0, 8),
            scenario = Status(0, 2, 0, 1, 0, 0),
        )

        // 120
        testOnsenTraining(
            baseCalcInfo, StatusType.POWER, 6,
            base = Status(0, 9, 33, 0, 0, 8),
            scenario = Status(0, 0, 3, 0, 0, 0),
        )

        // 120
        testOnsenTraining(
            baseCalcInfo, StatusType.GUTS, 6,
            base = Status(4, 0, 4, 31, 0, 8),
            scenario = Status(0, 0, 0, 3, 0, 0),
        )

        // 121
        testOnsenTraining(
            baseCalcInfo, StatusType.WISDOM, 6, 3,
            base = Status(8, 0, 0, 0, 34, 18),
            scenario = Status(2, 0, 0, 0, 10, 5),
        )

        baseCalcInfo = baseCalcInfo
            .addGensen("天翔の古湯")

        // 139
        testOnsenTraining(
            baseCalcInfo, StatusType.STAMINA, 3,
            base = Status(0, 20, 0, 6, 0, 8),
            scenario = Status(4, 4, 0, 0, 4, 0),
            digBonus = listOf(StratumType.SAND to 2),
        )
    }
}

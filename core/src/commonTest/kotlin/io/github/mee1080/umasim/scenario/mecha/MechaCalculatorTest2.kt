package io.github.mee1080.umasim.scenario.mecha

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation2.MechaTuningResult
import kotlin.test.Test

class MechaCalculatorTest2 : MechaCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[Rewarding Job]シンボリクリスエス" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[百花の願いをこの胸に]サトノダイヤモンド" to 4,
        "[Dear Mr. C.B.]ミスターシービー" to 4,
        "[7センチの先へ]エアシャカール" to 4,
        "[緋色の君へ風が吹く]ダイワスカーレット" to 4,
    )
) {

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        baseCalcInfo = baseCalcInfo
            .setLearningLevels(25, 20, 20, 32, 63)
            .updateMechaStatus {
                resetTuning()
                    .applyTuning(MechaTuningResult(MechaChipType.BODY, 1))
                    .applyTuning(MechaTuningResult(MechaChipType.LEG, 0))
                    .applyTuning(MechaTuningResult(MechaChipType.LEG, 0))
                    .applyTuning(MechaTuningResult(MechaChipType.LEG, 0))
                    .applyTuning(MechaTuningResult(MechaChipType.LEG, 0))
                    .applyTuning(MechaTuningResult(MechaChipType.LEG, 0))
            }
            .copy(speedSkillCount = 1)

        MechaCalculator.gearFactorValue = 300

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 1, 2, 5,
            base = Status(2, 0, 0, 0, 15, 8),
            scenario = Status(0, 0, 0, 0, 2, 1),
        )

        baseCalcInfo = baseCalcInfo.copy(motivation = 1)
            .setLearningLevels(85, 34, 34, 135, 200)
            .setRelation(1, 80)
            .setRelation(2, 80)
            .setRelation(4, 80)

        MechaCalculator.gearFactorValue = 600

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 1, 3,
            base = Status(15, 0, 3, 0, 0, 6),
            scenario = Status(2, 0, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0,
            base = Status(0, 11, 0, 4, 0, 5),
            scenario = Status(0, 1, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 1, 1, 4,
            condition = mechaGear,
            base = Status(0, 6, 19, 0, 0, 11),
            scenario = Status(0, 1, 3, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 2, 2,
            condition = mechaGear,
            base = Status(2, 0, 3, 14, 0, 8),
            scenario = Status(0, 0, 0, 3, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 1, 5,
            base = Status(5, 0, 0, 0, 17, 8),
            scenario = Status(1, 0, 0, 0, 4, 2),
        )

        baseCalcInfo = baseCalcInfo.setLearningLevels(92, 41, 41, 142, 200)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 4,
            base = Status(13, 0, 3, 0, 0, 7),
            scenario = Status(2, 0, 0, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 2, 0, 1, 3,
            condition = mechaGear,
            base = Status(0, 17, 0, 7, 0, 12),
            scenario = Status(0, 3, 0, 1, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 1,
            condition = mechaGear,
            base = Status(0, 4, 13, 0, 0, 5),
            scenario = Status(0, 0, 2, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 2,
            condition = mechaGear,
            base = Status(2, 0, 2, 11, 0, 6),
            scenario = Status(0, 0, 0, 3, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 5,
            condition = mechaGear,
            base = Status(5, 0, 0, 0, 16, 7),
            scenario = Status(1, 0, 0, 0, 5, 2),
        )

        baseCalcInfo = baseCalcInfo
            .setLearningLevels(108, 48, 48, 168, 200)
            .updateMechaStatus { resetTuning() }
            .setChipLevel(MechaChipType.BODY, 1, 5)
            .setChipLevel(MechaChipType.LEG, 0, 5)
            .setChipLevel(MechaChipType.LEG, 1, 1)
            .setRelation(5, 80)
            .copy(motivation = 2)

        MechaCalculator.gearFactorValue = 980

        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 1,
            condition = mechaGear,
            base = Status(15, 0, 3, 0, 0, 6),
            scenario = Status(4, 0, 0, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 1, 3, 5,
            base = Status(0, 18, 0, 7, 0, 10),
            scenario = Status(0, 2, 0, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 2, 0, 0, 1,
            base = Status(0, 6, 20, 0, 0, 10),
            scenario = Status(0, 0, 2, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 3,
            condition = mechaGear,
            base = Status(2, 0, 3, 15, 0, 6),
            scenario = Status(0, 0, 1, 5, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 2, 4,
            condition = mechaGear,
            base = Status(10, 0, 0, 0, 38, 19),
            scenario = Status(3, 0, 0, 0, 14, 7),
        )

        baseCalcInfo = baseCalcInfo.updateMechaStatus { applyOverdrive() }

        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 1,
            condition = mechaGear,
            base = Status(15, 0, 3, 0, 0, 6),
            scenario = Status(11, 0, 2, 0, 0, 3),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 1, 3, 5,
            base = Status(0, 18, 0, 7, 0, 10),
            scenario = Status(0, 8, 0, 3, 0, 4),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 2, 0, 0, 1,
            base = Status(0, 6, 20, 0, 0, 10),
            scenario = Status(0, 2, 10, 0, 0, 4),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 3,
            condition = mechaGear,
            base = Status(2, 0, 3, 15, 0, 6),
            scenario = Status(1, 0, 2, 11, 0, 4),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 5, 0, 2, 4,
            condition = mechaGear,
            base = Status(10, 0, 0, 0, 38, 19),
            scenario = Status(8, 0, 0, 0, 29, 14),
        )

        baseCalcInfo = baseCalcInfo
            .updateMechaStatus { copy(overdrive = false) }
            .setLearningLevels(140, 48, 48, 243, 300)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 0,
            condition = mechaGear,
            base = Status(14, 0, 2, 0, 0, 6),
            scenario = Status(4, 0, 0, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 2,
            base = Status(0, 14, 0, 5, 0, 6),
            scenario = Status(0, 1, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 2, 1, 1, 2, 4,
            base = Status(0, 8, 29, 0, 0, 17),
            scenario = Status(0, 1, 3, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 0, 3,
            condition = mechaGear,
            base = Status(4, 0, 3, 15, 0, 6),
            scenario = Status(1, 0, 1, 6, 0, 2),
        )

        baseCalcInfo = baseCalcInfo
            .setLearningLevels(144, 48, 48, 252, 300)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 2, 1,
            condition = mechaGear,
            base = Status(15, 0, 3, 0, 0, 6),
            scenario = Status(5, 0, 1, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 2, 1, 2, 5,
            condition = mechaGear,
            base = Status(0, 20, 0, 8, 0, 12),
            scenario = Status(0, 4, 0, 1, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 2, 1, 1,
            condition = mechaGear,
            base = Status(0, 6, 20, 0, 0, 9),
            scenario = Status(0, 1, 4, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 2, 1, 3, 4,
            base = Status(5, 0, 4, 18, 0, 10),
            scenario = Status(1, 0, 1, 5, 0, 3),
        )

        baseCalcInfo = baseCalcInfo
            .updateMechaStatus { resetTuning() }
            .setChipLevel(MechaChipType.HEAD, 1, 1)
            .setChipLevel(MechaChipType.HEAD, 2, 5)
            .setChipLevel(MechaChipType.BODY, 1, 5)
            .setChipLevel(MechaChipType.LEG, 0, 5)
            .setChipLevel(MechaChipType.LEG, 1, 1)
            .setLearningLevels(151, 55, 55, 259, 300)

        MechaCalculator.gearFactorValue = 1600

        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0,
            condition = mechaGear,
            base = Status(18, 0, 5, 0, 0, 6),
            scenario = Status(7, 0, 2, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 1,
            base = Status(0, 17, 0, 8, 0, 6),
            scenario = Status(0, 2, 0, 1, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 2, 1, 2, 5,
            base = Status(0, 13, 38, 0, 0, 18),
            scenario = Status(0, 1, 5, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 1, 3,
            condition = mechaGear,
            base = Status(7, 0, 5, 20, 0, 7),
            scenario = Status(3, 0, 2, 10, 0, 3),
        )

        baseCalcInfo = baseCalcInfo
            .setLearningLevels(155, 55, 55, 270, 329)
            .setRelation(0, 80)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 5, 0,
            condition = mechaGear,
            base = Status(18, 0, 5, 0, 0, 6),
            scenario = Status(7, 0, 2, 0, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 5, 0, 0, 2, 3,
            condition = mechaGear,
            base = Status(0, 26, 0, 12, 0, 13),
            scenario = Status(0, 8, 0, 3, 0, 4),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 5, 1,
            base = Status(0, 7, 21, 0, 0, 6),
            scenario = Status(0, 0, 2, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 5, 0, 1, 5,
            base = Status(7, 0, 6, 25, 0, 12),
            scenario = Status(2, 0, 1, 8, 0, 3),
        )
    }
}

package io.github.mee1080.umasim.scenario.mecha

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.MechaTuningResult
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.test.loadTestStore
import kotlin.test.Test
import kotlin.test.assertEquals

class MechaCalculatorTest {

    init {
        loadTestStore()
        Calculator.DEBUG = true
    }

    private val supportCardList = listOf(
        "[Devilish Whispers]スティルインラブ" to 3,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[大地と我らのアンサンブル]サウンズオブアース" to 4,
        "[ハネ退け魔を退け願い込め]スペシャルウィーク" to 2,
        "[牙を立て、リフレイン]ヒシアマゾン" to 4,
        "[百花の願いをこの胸に]サトノダイヤモンド" to 4,
    )

    @Test
    fun test() {
        var baseCalcInfo = state.baseCalcInfo

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0, 1, 2, 4,
            condition = mechaGear,
            base = Status(19, 0, 5, 0, 0, 11),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 3,
            condition = mechaGear,
            base = Status(0, 11, 0, 5, 0, 5),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 1, 5,
            base = Status(0, 4, 14, 0, 0, 6),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 1,
            base = Status(2, 0, 2, 10, 0, 5),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 0, 3,
            base = Status(3, 0, 0, 0, 9, 8),
            scenario = Status(),
        )

        baseCalcInfo = baseCalcInfo.copy(motivation = baseCalcInfo.motivation + 1)
            .setLearningLevels(22, 4, 7, 1, 1)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 0,
            condition = mechaGear,
            base = Status(12, 0, 2, 0, 0, 5),
            scenario = Status(1, 0, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 2, 0,
            condition = mechaGear,
            base = Status(0, 14, 0, 6, 0, 8),
            scenario = Status(0, 1, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 0, 1, 5,
            base = Status(0, 6, 21, 0, 0, 8),
            scenario = Status(0, 0, 1, 0, 0, 0),
        )

        baseCalcInfo = baseCalcInfo.updateMechaStatus {
            resetTuning()
                .applyTuning(MechaTuningResult(MechaChipType.HEAD, 1))
                .applyTuning(MechaTuningResult(MechaChipType.HEAD, 1))
                .applyTuning(MechaTuningResult(MechaChipType.LEG, 1))
                .applyTuning(MechaTuningResult(MechaChipType.LEG, 1))
                .applyTuning(MechaTuningResult(MechaChipType.LEG, 1))
        }.setLearningLevels(22, 25, 7, 4, 7)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 2,
            base = Status(13, 0, 2, 0, 0, 6),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 1, 0, 1, 2, 3,
            condition = mechaGear,
            base = Status(0, 26, 0, 12, 0, 17),
            scenario = Status(0, 2, 0, 1, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 1, 5,
            condition = mechaGear,
            base = Status(0, 5, 16, 0, 0, 6),
            scenario = Status(0, 0, 1, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 0,
            condition = mechaGear,
            base = Status(2, 0, 2, 10, 0, 5),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 1, 4,
            base = Status(2, 0, 0, 0, 10, 7),
            scenario = Status(),
        )

        baseCalcInfo = baseCalcInfo.setLearningLevels(22, 55, 7, 8, 15)

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 1,
            base = Status(12, 0, 2, 0, 0, 5),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0, 3,
            base = Status(0, 12, 0, 6, 0, 6),
            scenario = Status(0, 1, 0, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 1, 2, 5,
            condition = mechaGear,
            base = Status(0, 8, 20, 0, 0, 10),
            scenario = Status(0, 0, 1, 0, 0, 0),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 2,
            base = Status(2, 0, 2, 11, 0, 6),
            scenario = Status(),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 0, 4,
            condition = mechaGear,
            base = Status(4, 0, 0, 0, 12, 10),
            scenario = Status(0, 0, 0, 0, 1, 1),
        )

        baseCalcInfo = baseCalcInfo.updateMechaStatus { applyOverdrive() }

        testTraining(
            baseCalcInfo, StatusType.SPEED, 1, 1,
            base = Status(12, 0, 2, 0, 0, 5),
            scenario = Status(4, 0, 0, 0, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.STAMINA, 1, 0, 3,
            base = Status(0, 12, 0, 6, 0, 6),
            scenario = Status(0, 4, 0, 2, 0, 2),
        )

        testTraining(
            baseCalcInfo, StatusType.POWER, 1, 1, 2, 5,
            condition = mechaGear,
            base = Status(0, 8, 20, 0, 0, 10),
            scenario = Status(0, 2, 7, 0, 0, 3),
        )

        testTraining(
            baseCalcInfo, StatusType.GUTS, 1, 2,
            base = Status(2, 0, 2, 11, 0, 6),
            scenario = Status(0, 0, 0, 3, 0, 1),
        )

        testTraining(
            baseCalcInfo, StatusType.WISDOM, 1, 0, 0, 4,
            condition = mechaGear,
            base = Status(4, 0, 0, 0, 12, 10),
            scenario = Status(1, 0, 0, 0, 4, 3),
        )
    }

    private val state = Simulator(
        scenario = Scenario.MECHA,
        chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
        supportCardList = Store.getSupportByName(*supportCardList.toTypedArray()),
    ).initialState.let {
        MechaScenarioEvents().beforeSimulation(it)
    }

    private fun assertStatus(
        expected: Status,
        result: Status,
        message: String,
    ) {
        assertEquals(expected.speed, result.speed, message)
        assertEquals(expected.stamina, result.stamina, message)
        assertEquals(expected.power, result.power, message)
        assertEquals(expected.guts, result.guts, message)
        assertEquals(expected.wisdom, result.wisdom, message)
        assertEquals(expected.skillPt, result.skillPt, message)
    }

    private fun testTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
        level: Int,
        guestCount: Int,
        vararg supportIndices: Int,
        condition: Calculator.CalcInfo.() -> Calculator.CalcInfo = { this },
        base: Status? = null,
        scenario: Status? = null,
    ) {
        val memberNames =
            supportCardList.filterIndexed { index, _ -> supportIndices.contains(index) }.map { it.first }.toSet()
        val info = baseCalcInfo.copy(
            training = state.training[type.ordinal].base[level - 1],
            member = baseCalcInfo.member.filter { memberNames.contains(it.name) },
        ).setTeamMember(guestCount).condition()
        val result = Calculator.calcTrainingSuccessStatusSeparated(info)
        val message = buildString {
            appendLine("expected:")
            appendLine(base)
            appendLine(scenario)
            appendLine("actual:")
            appendLine(result.first.first)
            appendLine(result.second)
            appendLine("params:")
            appendLine(info.chara.name)
            appendLine(info.member.joinToString { it.name })
            appendLine(info.training)
            appendLine("motivation=${info.motivation}")
        }
        base?.let { assertStatus(it, result.first.first, message) }
        scenario?.let { assertStatus(it, result.second, message) }
    }

    private fun Calculator.CalcInfo.updateMechaStatus(action: MechaStatus.() -> MechaStatus): Calculator.CalcInfo {
        return copy(scenarioStatus = mechaStatus?.run { action() })
    }

    private val mechaGear: Calculator.CalcInfo.() -> Calculator.CalcInfo = {
        updateMechaStatus {
            copy(gearExists = gearExists.mapValues { true })
        }
    }

    private fun Calculator.CalcInfo.setLearningLevels(speed: Int, stamina: Int, power: Int, guts: Int, wisdom: Int) =
        updateMechaStatus {
            copy(
                learningLevels = mapOf(
                    StatusType.SPEED to speed,
                    StatusType.STAMINA to stamina,
                    StatusType.POWER to power,
                    StatusType.GUTS to guts,
                    StatusType.WISDOM to wisdom,
                )
            )
        }
}

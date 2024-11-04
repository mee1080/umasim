package io.github.mee1080.umasim.scenario.mecha

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.MechaTuningResult
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.test.loadTestStore
import io.github.mee1080.utility.mapIf
import kotlin.test.assertEquals

abstract class MechaCalculatorTest(
    chara: Triple<String, Int, Int>,
    private val supportCardList: List<Pair<String, Int>>,
) {

    init {
        loadTestStore()
        Calculator.DEBUG = true
    }

    protected val state = Simulator(
        scenario = Scenario.MECHA,
        chara = Store.getChara(chara.first, chara.second, chara.third),
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

    protected fun testTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
        level: Int,
        guestCount: Int,
        vararg supportIndices: Int,
        mechaGear: Boolean,
        base: Status? = null,
        scenario: Status? = null,
    ) {
        val memberNames =
            supportCardList.filterIndexed { index, _ -> supportIndices.contains(index) }.map { it.first }.toSet()
        val info = baseCalcInfo.copy(
            training = state.training[type.ordinal].base[level - 1],
            member = baseCalcInfo.member.filter { memberNames.contains(it.name) },
        ).setTeamMember(guestCount).updateMechaStatus {
            copy(gearExists = gearExists.mapValues { mechaGear })
        }
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
            appendLine(info.member.joinToString { "${it.name} ${it.relation}" })
            appendLine(info.training)
            appendLine("motivation=${info.motivation}")
        }
        base?.let { assertStatus(it, result.first.first, message) }
        scenario?.let { assertStatus(it, result.second, message) }
    }

    protected fun Calculator.CalcInfo.updateMechaStatus(action: MechaStatus.() -> MechaStatus): Calculator.CalcInfo {
        return copy(scenarioStatus = mechaStatus?.run { action() })
    }

    protected val mechaGear: Calculator.CalcInfo.() -> Calculator.CalcInfo = {
        updateMechaStatus {
            copy(gearExists = gearExists.mapValues { true })
        }
    }

    protected fun Calculator.CalcInfo.setLearningLevels(
        speed: Int, stamina: Int, power: Int, guts: Int, wisdom: Int,
    ): Calculator.CalcInfo {
        return updateMechaStatus {
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

    protected fun Calculator.CalcInfo.setRelation(index: Int, value: Int): Calculator.CalcInfo {
        val memberName = supportCardList[index].first
        return copy(
            member = member.mapIf({ it.name == memberName }) {
                it.copy(supportState = it.supportState?.copy(relation = value))
            }
        )
    }

    protected fun Calculator.CalcInfo.setChipLevel(type: MechaChipType, index: Int, level: Int): Calculator.CalcInfo {
        return updateMechaStatus {
            var result = this
            repeat(level) {
                result = result.applyTuning(MechaTuningResult(type, index))
            }
            result
        }
    }
}

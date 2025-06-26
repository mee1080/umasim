package io.github.mee1080.umasim.scenario

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.MemberState
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.simulation2.SupportState
import io.github.mee1080.umasim.test.loadTestStore
import io.github.mee1080.utility.mapIf
import kotlin.test.assertEquals

abstract class CalculatorTestBase(
    scenario: Scenario,
    chara: Triple<String, Int, Int>,
    protected val supportCardList: List<Pair<String, Int>>,
) {

    init {
        loadTestStore()
        Calculator.DEBUG = true
    }

    protected val state = Simulator(
        scenario = Scenario.LEGEND,
        chara = Store.getChara(chara.first, chara.second, chara.third),
        supportCardList = Store.getSupportByName(*supportCardList.toTypedArray()),
    ).initialState.let {
        scenario.scenarioEvents().beforeSimulation(it)
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
        base: Status? = null,
        scenario: Status? = null,
    ) {
        val memberNames = supportCardList.filterIndexed { index, _ ->
            supportIndices.contains(index)
        }.map { it.first }.toSet() + if (baseCalcInfo.member.size > 6) {
            baseCalcInfo.member.filterIndexed { index, memberState ->
                index >= 6 && supportIndices.contains(index)
            }.map { it.name }
        } else emptySet()
        println(memberNames)
        val info = baseCalcInfo.copy(
            training = state.training[type.ordinal].base[level - 1],
            member = baseCalcInfo.member.filter { memberNames.contains(it.name) },
        ).setTeamMember(guestCount)
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

    protected fun Calculator.CalcInfo.setMemberState(
        index: Int,
        action: MemberState.() -> MemberState
    ): Calculator.CalcInfo {
        val memberName = if (index < supportCardList.size) supportCardList[index].first else member[index].name
        return copy(
            member = member.mapIf({ it.name == memberName }) {
                it.action()
            }
        )
    }

    protected fun Calculator.CalcInfo.setSupportState(index: Int, action: SupportState.() -> SupportState) =
        setMemberState(index) {
            copy(supportState = supportState?.action())
        }

    protected fun Calculator.CalcInfo.setRelation(index: Int, value: Int) = setSupportState(index) {
        copy(relation = value)
    }

    protected fun Calculator.CalcInfo.setPassion(index: Int, passion: Boolean) = setSupportState(index) {
        copy(passionTurn = if (passion) 1 else 0)
    }
}

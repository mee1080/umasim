package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.CalculatorTestBase
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Calculator

abstract class BCCalculatorTest(
    chara: Triple<String, Int, Int>,
    supportCardList: List<Pair<String, Int>>,
    private val teamMemberList: List<String>,
) : CalculatorTestBase(Scenario.BC, chara, supportCardList) {

    protected fun Calculator.CalcInfo.updateBcStatus(action: BCStatus.() -> BCStatus): Calculator.CalcInfo {
        return copy(scenarioStatus = bcStatus?.run { action() })
    }

    fun Calculator.CalcInfo.initBcStatus(): Calculator.CalcInfo {
        return copy(scenarioStatus = BCStatus(teamMember = teamMemberList.map { BCTeamMember(it) }))
    }

    fun testBcTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
        level: Int,
        support: List<Int>,
        guest: List<Int>,
        base: Status,
        scenario: Status,
    ) {
        testTraining(
            baseCalcInfo.updateBcStatus {
                copy(
                    teamMember = teamMember.mapIndexed { index, member ->
                        if (guest.contains(index)) {
                            member.copy(position = baseCalcInfo.training.type)
                        } else {
                            member.copy(position = StatusType.NONE)
                        }
                    }
                )
            },
            type,
            level,
            0,
            *support.toIntArray(),
            base = base,
            scenario = scenario,
        )
    }
}

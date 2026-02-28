package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.CalculatorTestBase
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.utility.replaced

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

    fun Calculator.CalcInfo.setMemberRank(index: Int, rank: String) = updateBcStatus {
        copy(teamMember = teamMember.replaced(index) { it.copy(memberRank = rankToString.indexOf(rank)) })
    }

    fun Calculator.CalcInfo.setMemberRank(vararg rank: String) = updateBcStatus {
        copy(teamMember = teamMember.mapIndexed { index, member ->
            member.copy(memberRank = rankToString.indexOf(rank.getOrElse(index) { rank.last() }))
        })
    }

    fun Calculator.CalcInfo.setMemberGaugeMax(index: Int, max: Boolean) = updateBcStatus {
        copy(teamMember = teamMember.replaced(index) { it.copy(dreamGauge = if (max) 3 else 0) })
    }

    fun Calculator.CalcInfo.setMemberGaugeMax(vararg max: Boolean) = updateBcStatus {
        copy(teamMember = teamMember.mapIndexed { index, member -> member.copy(dreamGauge = if (max.getOrElse(index) { max.last() }) 3 else 0) })
    }

    fun Calculator.CalcInfo.setTeamParameter(type: BCTeamParameter, value: Int) = updateBcStatus {
        copy(teamParameter = teamParameter.replaced(type, value))
    }

    fun Calculator.CalcInfo.setTeamParameter(vararg value: Int) = updateBcStatus {
        copy(teamParameter = BCTeamParameter.entries.associateWith { value[it.ordinal] })
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
                            member.copy(position = type)
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

    fun testDreamsTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
        level: Int,
        support: List<Int> = supportCardList.mapIndexedNotNull { index, (name, _) ->
            if (name == "[American Dream]カジノドライヴ") null else index
        },
        base: Status,
        scenario: Status,
    ) {
        testBcTraining(
            baseCalcInfo.updateBcStatus { copy(dreamsTrainingActive = true) },
            type, level, support, listOf(0, 1, 2), base, scenario,
        )
    }
}

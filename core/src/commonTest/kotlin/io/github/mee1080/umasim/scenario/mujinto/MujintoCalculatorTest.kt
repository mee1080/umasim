package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.CalculatorTestBase
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.createTeamMemberState

abstract class MujintoCalculatorTest(
    chara: Triple<String, Int, Int>,
    supportCardList: List<Pair<String, Int>>,
) : CalculatorTestBase(Scenario.MUJINTO, chara, supportCardList) {

    protected fun Calculator.CalcInfo.updateMujintoStatus(action: MujintoStatus.() -> MujintoStatus): Calculator.CalcInfo {
        return copy(scenarioStatus = mujintoStatus?.run { action() })
    }

    protected fun Calculator.CalcInfo.setFacility(
        type: StatusType,
        level: Int,
        jukuren: Boolean = false,
    ): Calculator.CalcInfo {
        return updateMujintoStatus {
            copy(facilities = facilities + (type to mujintoFacility(type, level, jukuren)))
        }
    }

    fun testIslandTraining(
        baseCalcInfo: Calculator.CalcInfo,
        position: List<Triple<StatusType, List<Int>, Int>> = emptyList(),
        base: Status? = null,
        scenario: Status? = null,
        pioneerPt: Int = 0,
    ) {
        var info = baseCalcInfo.copy(
            member = emptyList()
        )
        position.forEach { (type, supportIndices, guestCount) ->
            val memberNames = supportCardList.filterIndexed { index, _ ->
                supportIndices.contains(index)
            }.map { it.first }.toSet()
            println("$type : $memberNames + $guestCount")
            info = info.copy(
                member = info.member + baseCalcInfo.member.filter { memberNames.contains(it.name) }.map {
                    it.copy(position = type)
                } + createTeamMemberState(guestCount, Scenario.MUJINTO).map {
                    it.copy(position = type)
                },
            )
        }
        val result = MujintoCalculator.calcIslandTrainingStatusSeparated(info)
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
}

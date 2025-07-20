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

    protected fun testCampTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
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
        val newState = state.copy(turn = 37, scenarioStatus = baseCalcInfo.mujintoStatus)
        val training = MujintoCalculator.getTraining(newState, type)!!
        val info = baseCalcInfo.copy(
            training = training,
            member = baseCalcInfo.member.filter { memberNames.contains(it.name) },
        ).setTeamMember(guestCount)
        val scenarioCalcBonus = MujintoCalculator.getScenarioCalcBonus(newState, info)

        val result = Calculator.calcTrainingSuccessStatusSeparated(info, scenarioCalcBonus)
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

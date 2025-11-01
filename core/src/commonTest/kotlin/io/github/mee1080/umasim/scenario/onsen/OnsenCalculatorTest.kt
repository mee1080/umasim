package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.CalculatorTestBase
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Calculator

abstract class OnsenCalculatorTest(
    chara: Triple<String, Int, Int>,
    supportCardList: List<Pair<String, Int>>,
) : CalculatorTestBase(Scenario.ONSEN, chara, supportCardList) {

    protected fun Calculator.CalcInfo.updateOnsenStatus(action: OnsenStatus.() -> OnsenStatus): Calculator.CalcInfo {
        return copy(scenarioStatus = onsenStatus?.run { action() })
    }

    fun Calculator.CalcInfo.initOnsenState(): Calculator.CalcInfo {
        return copy(scenarioStatus = OnsenStatus(state.support.map { it.card }, state.factor))
    }

    protected fun Calculator.CalcInfo.addGensen(name: String) = updateOnsenStatus {
        copy(excavatedGensen = excavatedGensen + gensenData[name]!!)
    }

    protected fun Calculator.CalcInfo.setOnsenActive(active: Boolean) = updateOnsenStatus {
        copy(onsenActiveTurn = if (active) 2 else 0)
    }

    fun testOnsenTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
        level: Int,
        vararg supportIndices: Int,
        base: Status,
        scenario: Status,
        digBonus: List<Pair<StratumType, Int>> = emptyList(),
    ) {
        val digBonusStatus = digBonus.fold(Status()) { acc, (type, count) ->
            acc + OnsenCalculator.calcDigBonus(type, 0, Int.MAX_VALUE, count * 30)
        }
        println("digBonus: $digBonusStatus")
        testTraining(
            baseCalcInfo,
            type,
            level,
            0,
            *supportIndices,
            base = base,
            scenario = scenario - digBonusStatus,
        )
    }
}

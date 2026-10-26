package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.CalculatorTestBase
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Calculator

abstract class ShuntaisaiTrainingTestBase(
    chara: Triple<String, Int, Int>,
    supportCardList: List<Pair<String, Int>>,
) : CalculatorTestBase(Scenario.SHUNTAISAI, chara, supportCardList) {

    protected fun Calculator.CalcInfo.updateShuntaisaiStatus(action: ShuntaisaiStatus.() -> ShuntaisaiStatus): Calculator.CalcInfo {
        return copy(scenarioStatus = shuntaisaiStatus?.run { action() })
    }

    fun Calculator.CalcInfo.initShuntaisaiStatus(): Calculator.CalcInfo {
        return copy(scenarioStatus = ShuntaisaiStatus())
    }

    fun testEnmokuTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
        level: Int,
        guestCount: Int,
        vararg supportIndices: Int,
        base: Status? = null,
        scenario: Status? = null,
    ) {
        // TODO
        testTraining(
            baseCalcInfo,
            type,
            level,
            guestCount,
            *supportIndices,
            base = base,
            scenario = scenario,
        )
    }
}

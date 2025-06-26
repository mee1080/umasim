package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.CalculatorTestBase
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.utility.mapValuesIf

abstract class MujintoCalculatorTest(
    chara: Triple<String, Int, Int>,
    supportCardList: List<Pair<String, Int>>,
) : CalculatorTestBase(Scenario.MUJINTO, chara, supportCardList) {

    protected fun Calculator.CalcInfo.updateMujintoStatus(action: MujintoStatus.() -> MujintoStatus): Calculator.CalcInfo {
        return copy(scenarioStatus = mujintoStatus?.run { action() })
    }

    protected fun Calculator.CalcInfo.setFacilityLevel(type: StatusType, level: Int): Calculator.CalcInfo {
        return updateMujintoStatus {
            copy(facilityLevel = facilityLevel.mapValuesIf({ it.key == type }) { level })
        }
    }
}

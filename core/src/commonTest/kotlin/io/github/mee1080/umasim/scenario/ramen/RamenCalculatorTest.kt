package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.CalculatorTestBase
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Calculator

abstract class RamenCalculatorTest(
    chara: Triple<String, Int, Int>,
    supportCardList: List<Pair<String, Int>>,
) : CalculatorTestBase(Scenario.RAMEN, chara, supportCardList) {

    protected fun Calculator.CalcInfo.updateRamenStatus(action: RamenStatus.() -> RamenStatus): Calculator.CalcInfo {
        return copy(scenarioStatus = ramenStatus?.run { action() })
    }

    fun Calculator.CalcInfo.initRamenStatus(): Calculator.CalcInfo {
        return copy(scenarioStatus = RamenStatus())
    }

    fun Calculator.CalcInfo.setExcitementPt(pt: Int) = updateRamenStatus {
        copy(excitementPt = pt)
    }

    fun Calculator.CalcInfo.setActiveTastingRegion(region: RamenRegion?) = updateRamenStatus {
        copy(activeTastingRegion = region?.let { it to regionRankBonus })
    }

    fun Calculator.CalcInfo.setPeriod(period: Int, success: Boolean): Calculator.CalcInfo = updateRamenStatus {
        copy(
            turn = period * 24 + 1,
            rmjBonus = ramenRmjBonus[period - 1][if (success) 1 else 0],
        )
    }

    fun Calculator.CalcInfo.setGauges(noodle: Int, soup: Int, topping: Int) = updateRamenStatus {
        copy(
            gauges = mapOf(
                RamenTipType.NOODLE to noodle,
                RamenTipType.SOUP to soup,
                RamenTipType.TOPPING to topping
            )
        )
    }

    fun Calculator.CalcInfo.setGauge(type: RamenTipType, value: Int) = updateRamenStatus {
        copy(gauges = gauges + (type to value))
    }

    fun Calculator.CalcInfo.setTips(noodle: Int, soup: Int, topping: Int, hidden: Int) = updateRamenStatus {
        copy(
            tips = mapOf(
                RamenTipType.NOODLE to noodle,
                RamenTipType.SOUP to soup,
                RamenTipType.TOPPING to topping,
            )
        )
    }

    fun Calculator.CalcInfo.setTip(type: RamenTipType, count: Int) = updateRamenStatus {
        copy(tips = tips + (type to count))
    }

    fun Calculator.CalcInfo.setTrainingTip(vararg tips: RamenTipType) = updateRamenStatus {
        copy(
            trainingTip = tips.mapIndexed { index, tip ->
                trainingType[index] to tip
            }.toMap()
        )
    }

    fun testRamenTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
        level: Int,
        support: List<Int>,
        guestCount: Int = 0,
        base: Status,
        scenario: Status,
    ) {
        testTraining(
            baseCalcInfo,
            type,
            level,
            guestCount,
            *support.toIntArray(),
            base = base,
            scenario = scenario,
        )
    }

    fun testTastingTraining(
        baseCalcInfo: Calculator.CalcInfo,
        region: RamenRegion,
        type: StatusType,
        level: Int,
        support: List<Int>,
        guestCount: Int = 0,
        base: Status,
        scenario: Status,
    ) {
        testRamenTraining(
            baseCalcInfo.setActiveTastingRegion(region),
            type,
            level,
            support,
            guestCount,
            base = base,
            scenario = scenario,
        )
    }
}

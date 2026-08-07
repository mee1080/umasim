package io.github.mee1080.umasim.race.calc2

import kotlin.test.Test
import kotlin.test.assertTrue

class RaceCalculatorTest {

    @Test
    fun testGoalSpIsRecorded() {
        val setting = RaceSetting()
        val calculator = RaceCalculator(SystemSetting())
        val (result, state) = calculator.simulate(setting)

        // Ensure that goalSp is recorded and matches the final state stamina (simulation.sp)
        assertTrue(result.goalSp >= -10000.0, "goalSp should be a valid double value")
        assertTrue(result.goalSp == state.simulation.sp, "goalSp should equal the remaining stamina at the goal")
    }
}

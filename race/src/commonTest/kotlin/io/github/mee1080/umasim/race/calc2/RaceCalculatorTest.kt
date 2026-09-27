package io.github.mee1080.umasim.race.calc2

import kotlin.test.Test
import kotlin.test.assertEquals
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

    @Test
    fun testFullSpurtFrameRecording() {
        val setting = RaceSetting(
            umaStatus = UmaStatus(speed = 2200)
        )
        val calculator = RaceCalculator(SystemSetting())
        val (_, state) = calculator.simulate(setting)

        val fullSpurtFrames = state.simulation.frames.filter { it.fullSpurt }
        if (fullSpurtFrames.isNotEmpty()) {
            fullSpurtFrames.forEach { frame ->
                assertTrue(frame.fullSpurtTargetSpeed > 0.0, "fullSpurtTargetSpeed should be recorded when fullSpurt is active")
                val normalTargetSpeed = frame.targetSpeed - frame.fullSpurtTargetSpeed
                val normalCurrentSpeed = frame.speed - frame.fullSpurtCurrentSpeed
                val speedDiff = normalTargetSpeed - normalCurrentSpeed
                assertTrue(!speedDiff.isNaN() && !speedDiff.isInfinite(), "speedDiff without fullSpurt should be a finite double")
            }
        }
    }
}

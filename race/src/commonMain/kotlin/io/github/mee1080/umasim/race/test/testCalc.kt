package io.github.mee1080.umasim.race.test

import io.github.mee1080.umasim.race.calc.RaceCalculator
import io.github.mee1080.umasim.race.calc.RaceSetting
import io.github.mee1080.umasim.race.calc.Track
import io.github.mee1080.umasim.race.data.getSkill

fun testCalc() {
    val setting = RaceSetting(
        track = Track(10001, 10104),
        skillActivateAdjustment = 2,
        randomPosition = 3,
        hasSkills = listOf(
            getSkill("右回り◎"),
//            getSkill("弧線のプロフェッサー"),
            getSkill("好転一息"),
            getSkill("昂る鼓動"),
            getSkill("レッツ・アナボリック！"),
//            getSkill("追込ためらい"),
        )
    )
    println(setting)
    println(setting.trackDetail)
    setting.hasSkills.forEach { println(it.name) }
    val calculator = RaceCalculator(setting)
    repeat(10) {
        val (result, state) = calculator.simulate()
        println("$result")
        state.simulation.invokedSkills.forEach { println(it.skill) }
    }
    val (result, state) = calculator.simulate()
    println(result)
//    state.simulation.frames.forEachIndexed { index, raceFrame ->
//        println("$index : $raceFrame")
//    }
}
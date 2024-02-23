package io.github.mee1080.umasim.race.test

import io.github.mee1080.umasim.race.calc2.RaceCalculator
import io.github.mee1080.umasim.race.calc2.RaceSetting
import io.github.mee1080.umasim.race.calc2.Track
import io.github.mee1080.umasim.race.data.RandomPosition
import io.github.mee1080.umasim.race.data.SkillActivateAdjustment
import io.github.mee1080.umasim.race.data2.getSkill
import io.github.mee1080.umasim.race.data2.getSkills

fun testCalc2() {
    val skills = listOf(
        getSkill("右回り◎"),
//        getSkill("弧線のプロフェッサー"),
//        getSkill("スリップストリーム"),
        getSkill("好転一息"),
//        getSkill("昂る鼓動"),
        getSkills("レッツ・アナボリック！").first { it.rarity == "unique" },
//            getSkill("追込ためらい"),
    )
//    val skills = skillData2
    val setting = RaceSetting(
//        track = Track(10001, 10101), // 札幌芝1200
//        track = Track(10001, 10105), // 札幌芝2600
        track = Track(10005, 10507), // 中山芝3600
        skillActivateAdjustment = SkillActivateAdjustment.ALL,
        randomPosition = RandomPosition.MIDDLE,
        hasSkills = skills,
    )
    println(setting)
    println(setting.trackDetail)
    setting.hasSkills.forEach { println("${it.name} ${it.messages}") }
    val calculator = RaceCalculator(setting)
//    repeat(10) {
//        val (result, state) = calculator.simulate()
//        println("$result")
//        state.simulation.invokedSkills.forEach { println(it.skill) }
//    }
    val (result, state) = calculator.simulate()
    println(result)
    state.simulation.frames.forEachIndexed { index, raceFrame ->
        if (raceFrame.skills.isNotEmpty()) {
            println("$index : ${raceFrame.skills.map { it.skill.name }}")
        }
//        println(raceFrame)
    }
}
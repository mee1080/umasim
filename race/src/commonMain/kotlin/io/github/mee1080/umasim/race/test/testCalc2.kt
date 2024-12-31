package io.github.mee1080.umasim.race.test

import io.github.mee1080.umasim.race.calc2.RaceCalculator
import io.github.mee1080.umasim.race.calc2.RaceSetting
import io.github.mee1080.umasim.race.calc2.Track
import io.github.mee1080.umasim.race.calc2.UmaStatus
import io.github.mee1080.umasim.race.data.PositionKeepMode
import io.github.mee1080.umasim.race.data.RandomPosition
import io.github.mee1080.umasim.race.data.SkillActivateAdjustment
import io.github.mee1080.umasim.race.data.Style
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
//        getSkill("大逃げ")
//            getSkill("追込ためらい"),
    )
//    val skills = skillData2
    val setting = RaceSetting(
//        track = Track(10001, 10101), // 札幌芝1200
//        track = Track(10001, 10105), // 札幌芝2600
        umaStatus = UmaStatus(style = Style.OI, hasSkills = skills),
        track = Track(10005, 10507), // 中山芝3600
        skillActivateAdjustment = SkillActivateAdjustment.ALL,
        randomPosition = RandomPosition.MIDDLE,
        positionKeepMode = PositionKeepMode.VIRTUAL,
        virtualLeader = UmaStatus(style = Style.NIGE, hasSkills = listOf(getSkill("先手必勝"), getSkill("大逃げ"))),
    )
    println(setting)
    println(setting.trackDetail)
    setting.umaStatus.hasSkills.forEach { println("${it.name} ${it.messages}") }
    val calculator = RaceCalculator()
//    repeat(10) {
//        val (result, state) = calculator.simulate()
//        println("$result")
//        state.simulation.invokedSkills.forEach { println(it.skill) }
//    }
    val (result, state) = calculator.simulate(setting)
    println(result)
    state.simulation.frames.forEachIndexed { index, raceFrame ->
        if (raceFrame.triggeredSkills.isNotEmpty()) {
            println("$index : ${raceFrame.triggeredSkills.map { it.invoke.skill.name }}")
        }
        println(raceFrame.startPosition - (raceFrame.paceMakerFrame?.startPosition ?: 0.0))
    }
}
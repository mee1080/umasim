package io.github.mee1080.umasim.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.int
import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.simulation2.Evaluator
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.simulation2.Summary
import kotlin.math.roundToInt

class CliMain : CliktCommand() {

    private val chara by option(help = "育成ウマ娘 レアリティ ランク").triple().required()

    private val support by option(help = "サポートカード 凸数").pair().multiple()

    private val scenario by option(help = "シナリオ").default("AOHARU")

    private val count by option(help = "実行回数").int().default(100)

    private val turn by option(help = "ターン数").int().default(78)

    private val speed by option().double().default(1.0)
    private val stamina by option().double().default(1.0)
    private val power by option().double().default(1.0)
    private val guts by option().double().default(0.0)
    private val wisdom by option().double().default(0.0)
    private val skillPt by option().double().default(0.4)
    private val hp by option().double().default(0.5)
    private val motivation by option().double().default(15.0)

    override fun run() {
        StoreLoader.load()
        val charaData = Store.getChara(chara.first, chara.second.toInt(), chara.third.toInt())
        val supportList = support.map { Store.getSupportByName(it.first, it.second.toInt()) }
        val option = FactorBasedActionSelector2.Option(
            speedFactor = speed,
            staminaFactor = stamina,
            powerFactor = power,
            gutsFactor = guts,
            wisdomFactor = wisdom,
            skillPtFactor = skillPt,
            hpFactor = hp,
            motivationFactor = motivation,
        )
        val evaluateSetting = mapOf(
            StatusType.SPEED to (1.0 to 1100),
            StatusType.STAMINA to (1.4 to 600),
            StatusType.POWER to (1.0 to 1100),
            StatusType.GUTS to (0.8 to 600),
            StatusType.WISDOM to (1.0 to 600),
            StatusType.SKILL to (0.4 to Int.MAX_VALUE),
        )
//        println(charaData)
//        supportList.forEach { println(it) }
//        println(option)
        val summaries = mutableListOf<Summary>()
        repeat(count) {
            summaries.add(
                Simulator(
                    Scenario.valueOf(scenario),
                    charaData,
                    supportList,
                    Simulator.Option(checkGoalRace = true),
                ).simulate(turn, option.generateSelector())
            )
        }
//                    summaries.map { it.status }.forEach { println(it) }
        println((Evaluator(summaries).upperSum(0.2, evaluateSetting) * 1000).roundToInt() / 1000.0)
    }
}

fun main(args: Array<String>) = CliMain().main(args)
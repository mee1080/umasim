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
import io.github.mee1080.umasim.simulation2.ApproximateSimulationEvents
import io.github.mee1080.umasim.simulation2.Evaluator
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.simulation2.Summary
import kotlin.math.roundToInt

class CliMain : CliktCommand() {

    private val chara by option(help = "育成ウマ娘 レアリティ ランク").triple().required()

    private val support by option(help = "サポートカード 凸数").pair().multiple()

    private val factor by option(help = "因子").pair().multiple()

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
    private val relation by option().triple().multiple()
    private val relationDefault by option().double().default(0.0)
    private val aoharu by option().pair().multiple()
    private val aoharuDefault by option().double().default(0.0)
    private val aoharuBurn by option().double()

    override fun run() {
        StoreLoader.load()
        val charaData = Store.getChara(chara.first, chara.second.toInt(), chara.third.toInt())
        val supportList = support.map { Store.getSupportByName(it.first, it.second.toInt()) }
        val relationFactor = if (relation.isEmpty()) {
            FactorBasedActionSelector2.Option().relationFactor
        } else {
            val relationFactors = relation
                .map { Triple(StatusType.valueOf(it.first), it.second.toInt(), it.third.toDouble()) };
            { type: StatusType, rank: Int, _: Int ->
                relationFactors.firstOrNull { type == it.first && rank == it.second }?.third ?: relationDefault
            }
        }
        val aoharuFactor = if (aoharu.isEmpty()) {
            FactorBasedActionSelector2.Option().aoharuFactor
        } else {
            val aoharuFactors = aoharu
                .map { it.first.toInt() to it.second.toDouble() };
            { turn, level, _ ->
                when {
                    level < 4 -> aoharuFactors.firstOrNull { turn <= it.first }?.second ?: aoharuDefault
                    level == 4 -> aoharuBurn ?: 10.0
                    else -> 0.0
                }
            }
        }
        val option = FactorBasedActionSelector2.Option(
            speedFactor = speed,
            staminaFactor = stamina,
            powerFactor = power,
            gutsFactor = guts,
            wisdomFactor = wisdom,
            skillPtFactor = skillPt,
            hpFactor = hp,
            motivationFactor = motivation,
            relationFactor = relationFactor,
            aoharuFactor = aoharuFactor,
        )
        val evaluateSetting = mapOf(
            StatusType.SPEED to (1.2 to 1200),
            StatusType.STAMINA to (1.2 to 600),
            StatusType.POWER to (1.0 to 1150),
            StatusType.GUTS to (0.8 to 600),
            StatusType.WISDOM to (0.9 to 1000),
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
                    factor.map { StatusType.valueOf(it.first) to it.second.toInt() },
                ).simulate(turn, option.generateSelector(), ApproximateSimulationEvents())
            )
        }
//        summaries.map { it.status }.forEach { println(it) }
        println((Evaluator(summaries).upperSum(0.2, evaluateSetting) * 1000).roundToInt() / 1000.0)
    }
}

fun main(args: Array<String>) = CliMain().main(args)
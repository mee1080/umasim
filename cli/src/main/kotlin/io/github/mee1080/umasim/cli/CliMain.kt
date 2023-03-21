package io.github.mee1080.umasim.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.int
import io.github.mee1080.umasim.ai.ClimaxFactorBasedActionSelector
import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
import io.github.mee1080.umasim.ai.GmActionSelector
import io.github.mee1080.umasim.ai.GrandLiveFactorBasedActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.simulation2.Runner

class CliMain : CliktCommand() {

    private val chara by option(help = "育成ウマ娘 レアリティ ランク").triple().required()

    private val support by option(help = "サポートカード 凸数").pair().multiple()

    private val factor by option(help = "因子").pair().multiple()

    private val scenario by option(help = "シナリオ").default("CLIMAX")

    private val count by option(help = "実行回数").int().default(100)

    private val distance by option(help = "距離(short|mile|middle|long)").default("middle")

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
    private val performance by option().double().default(0.0)
    private val knowledgeSpeed by option().double().default(0.0)
    private val knowledgeStamina by option().double().default(0.0)
    private val knowledgePower by option().double().default(0.0)
    private val knowledgeGuts by option().double().default(0.0)
    private val knowledgeWisdom by option().double().default(0.0)
    private val knowledgeSkillPt by option().double().default(0.0)
    private val knowledgeFounder by option().double().default(10.0)
    private val knowledgeCountBase by option().double().default(10.0)
    private val knowledgeCountFactor by option().double().default(2.0)
    private val passionChallenge by option().double().default(0.0)

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
        val scenarioValue = Scenario.valueOf(scenario)
        val selector = when (scenarioValue) {
            Scenario.GM -> GmActionSelector.Option(
                speedFactor = speed,
                staminaFactor = stamina,
                powerFactor = power,
                gutsFactor = guts,
                wisdomFactor = wisdom,
                skillPtFactor = skillPt,
                hpFactor = hp,
                motivationFactor = motivation,
                relationFactor = relationFactor,
                knowledgeSpeedFactor = knowledgeSpeed,
                knowledgeStaminaFactor = knowledgeStamina,
                knowledgePowerFactor = knowledgePower,
                knowledgeGutsFactor = knowledgeGuts,
                knowledgeWisdomFactor = knowledgeWisdom,
                knowledgeSkillPtFactor = knowledgeSkillPt,
                knowledgeFounderFactor = knowledgeFounder,
                knowledgeCountBase = knowledgeCountBase,
                knowledgeCountFactor = knowledgeCountFactor,
                passionChallengeFactor = passionChallenge,
            )::generateSelector

            Scenario.GRAND_LIVE -> GrandLiveFactorBasedActionSelector.Option(
                speedFactor = speed,
                staminaFactor = stamina,
                powerFactor = power,
                gutsFactor = guts,
                wisdomFactor = wisdom,
                skillPtFactor = skillPt,
                hpFactor = hp,
                motivationFactor = motivation,
                relationFactor = relationFactor,
                performanceFactor = performance,
            )::generateSelector

            Scenario.CLIMAX -> ClimaxFactorBasedActionSelector.Option(
                speedFactor = speed,
                staminaFactor = stamina,
                powerFactor = power,
                gutsFactor = guts,
                wisdomFactor = wisdom,
                skillPtFactor = skillPt,
                hpFactor = hp,
                motivationFactor = motivation,
                relationFactor = relationFactor,
            )::generateSelector

            else -> FactorBasedActionSelector2.Option(
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
            )::generateSelector
        }
        val evaluateSetting = when (distance) {

            "short" -> Runner.shortEvaluateSetting

            "mile" -> when (scenarioValue) {
                Scenario.GM -> Runner.gmMileEvaluateSetting
                Scenario.GRAND_LIVE -> Runner.grandLiveMileEvaluateSetting
                else -> Runner.mileEvaluateSetting
            }

            "middle" -> Runner.middleEvaluateSetting

            else -> if (scenarioValue == Scenario.GRAND_LIVE) {
                Runner.grandLiveLongEvaluateSetting
            } else {
                Runner.longEvaluateSetting
            }
        }
        val result = Runner.runAndEvaluate(
            count, scenarioValue, charaData, supportList,
            factor.map { StatusType.valueOf(it.first) to it.second.toInt() },
            evaluateSetting, selector = selector,
        )
        println(result.first)
    }
}

fun main(args: Array<String>) = CliMain().main(args)
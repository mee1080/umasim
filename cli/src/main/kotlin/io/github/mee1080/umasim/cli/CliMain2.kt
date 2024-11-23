package io.github.mee1080.umasim.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int
import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
import io.github.mee1080.umasim.ai.MechaActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Runner
import io.github.mee1080.utility.replaced
import kotlinx.coroutines.runBlocking

class CliMain2 : CliktCommand() {
    private val dataDir by option()

    private val chara by option(help = "育成ウマ娘 レアリティ ランク").triple().required()

    private val support by option(help = "サポートカード 凸数").pair().multiple()

    private val factor by option(help = "因子").pair().multiple()

    private val scenario by option(help = "シナリオ").default("CLIMAX")

    private val count by option(help = "実行回数").int().default(100)

    private val speed by option().int().multiple()
    private val stamina by option().int().multiple()
    private val power by option().int().multiple()
    private val guts by option().int().multiple()
    private val wisdom by option().int().multiple()
    private val skillPt by option().int().multiple()
    private val hp by option().int().multiple()
    private val motivation by option().int().multiple()
    private val relation by option().int().multiple()
    private val outingRelation by option().int().multiple()
    private val hpKeep by option().int().multiple()
    private val risk by option().int().multiple()
    private val mechaLearningLevel by option().int().multiple()
    private val mechaOverdriveGauge by option().int().multiple()
    private val mechaOverdriveThreshold by option().int().multiple()

    private val evaluate by option().triple().multiple()

    override fun run() {
        StoreLoader.load(dataDir)
        val charaData = Store.getChara(chara.first, chara.second.toInt(), chara.third.toInt())
        val supportList = support.map { Store.getSupportByName(it.first, it.second.toInt()) }
        val scenarioValue = Scenario.valueOf(scenario)
        val selector = when (scenarioValue) {

            Scenario.MECHA -> {
                {
                    val options = (0..4).map {
                        MechaActionSelector.Option(
                            speedFactor = speed.getOrElse(it) { speed.first() },
                            staminaFactor = stamina.getOrElse(it) { stamina.first() },
                            powerFactor = power.getOrElse(it) { power.first() },
                            gutsFactor = guts.getOrElse(it) { guts.first() },
                            wisdomFactor = wisdom.getOrElse(it) { wisdom.first() },
                            skillPtFactor = skillPt.getOrElse(it) { skillPt.first() },
                            hpFactor = hp.getOrElse(it) { hp.first() },
                            motivationFactor = motivation.getOrElse(it) { motivation.first() },
                            relationFactor = relation.getOrElse(it) { relation.first() },
                            outingRelationFactor = outingRelation.getOrElse(it) { outingRelation.first() },
                            hpKeepFactor = hpKeep.getOrElse(it) { hpKeep.first() },
                            riskFactor = risk.getOrElse(it) { risk.first() },
                            learningLevelFactor = mechaLearningLevel.getOrElse(it) { mechaLearningLevel.first() },
                            overdriveGaugeFactor = mechaOverdriveGauge.getOrElse(it) { mechaOverdriveGauge.first() },
                            overdriveThreshold = mechaOverdriveThreshold.getOrElse(it) { mechaOverdriveThreshold.first() },
                        )
                    }
                    MechaActionSelector(options)
                }
            }

            else -> FactorBasedActionSelector2.Option(
                speedFactor = speed.getOrElse(0) { 1 }.toDouble(),
                staminaFactor = stamina.getOrElse(0) { 1 }.toDouble(),
                powerFactor = power.getOrElse(0) { 1 }.toDouble(),
                gutsFactor = guts.getOrElse(0) { 1 }.toDouble(),
                wisdomFactor = wisdom.getOrElse(0) { 1 }.toDouble(),
                skillPtFactor = skillPt.getOrElse(0) { 1 }.toDouble(),
                hpFactor = hp.getOrElse(0) { 1 }.toDouble(),
                motivationFactor = motivation.getOrElse(0) { 1 }.toDouble(),
                relationFactor = FactorBasedActionSelector2.Option().relationFactor,
            )::generateSelector
        }
        val factorList = factor.map { StatusType.valueOf(it.first) to it.second.toInt() }
        val evaluateSetting = evaluate.fold(Runner.noLimitSetting) { acc, setting ->
            acc.replaced(StatusType.valueOf(setting.first), setting.second.toDouble() to setting.third.toInt())
        }
        val result = runBlocking {
            Runner.runAndEvaluate(
                count, scenarioValue, charaData, supportList,
                factorList, evaluateSetting, selector = selector,
            )
        }
        println(result.first)
    }
}

fun main(args: Array<String>) = CliMain2().main(args)

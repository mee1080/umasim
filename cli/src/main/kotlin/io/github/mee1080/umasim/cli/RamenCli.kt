package io.github.mee1080.umasim.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.int
import io.github.mee1080.umasim.ai.RamenActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.ramen.RamenScenarioEvents
import io.github.mee1080.umasim.simulation2.Runner
import kotlinx.coroutines.runBlocking

class RamenCli : CliktCommand() {

    private val defaultOption = RamenActionSelector.Option()

    private val dataDir by option().required()

    private val chara by option(help = "育成ウマ娘 レアリティ ランク").triple().required()

    private val support by option(help = "サポートカード 凸数").pair().multiple()

    private val factor by option(help = "因子").pair().multiple()

    private val count by option(help = "実行回数").int().default(100)

    private val evaluate by option().choice(Runner.ramenSettingTemplate.keys).required()

    private val status by option().int().multiple()
    private val wisdom by option().int().multiple()
    private val skillPt by option().int().multiple()
    private val hp by option().int().multiple()
    private val motivation by option().int().multiple()

    private val relation by option().int().multiple()
    private val outingRelation by option().int().multiple()
    private val hpKeep by option().int().multiple()
    private val risk by option().int().multiple()

    private val tastingThreashold by option().int().multiple()
    private val allTastingFactor by option().int().multiple()
    private val speedTastingFactor by option().int().multiple()
    private val staminaTastingFactor by option().int().multiple()
    private val powerTastingFactor by option().int().multiple()
    private val gutsTastingFactor by option().int().multiple()
    private val wisdomTastingFactor by option().int().multiple()
    private val tastingMinFailureRate by option().int().multiple()
    private val gaugeScore by option().int().multiple()
    private val gaugeMaxScore by option().int().multiple()

    override fun run() {
        StoreLoader.load(dataDir)
        val charaData = Store.getChara(chara.first, chara.second.toInt(), chara.third.toInt())
        val supportList = support.map { Store.getSupportByName(it.first, it.second.toInt()) }
        val options = List(4) {
            RamenActionSelector.Option(
                status = status.getOrElse(it) { defaultOption.status },
                wisdom = wisdom.getOrElse(it) { defaultOption.wisdom },
                skillPt = skillPt.getOrElse(it) { defaultOption.skillPt },
                hp = hp.getOrElse(it) { defaultOption.hp },
                motivation = motivation.getOrElse(it) { defaultOption.motivation },
                relation = relation.getOrElse(it) { defaultOption.relation },
                outingRelation = outingRelation.getOrElse(it) { defaultOption.outingRelation },
                hpKeep = hpKeep.getOrElse(it) { defaultOption.hpKeep },
                risk = risk.getOrElse(it) { defaultOption.risk },
                tastingThreashold = tastingThreashold.getOrElse(it) { defaultOption.tastingThreashold },
                allTastingFactor = allTastingFactor.getOrElse(it) { defaultOption.allTastingFactor },
                speedTastingFactor = speedTastingFactor.getOrElse(it) { defaultOption.speedTastingFactor },
                staminaTastingFactor = staminaTastingFactor.getOrElse(it) { defaultOption.staminaTastingFactor },
                powerTastingFactor = powerTastingFactor.getOrElse(it) { defaultOption.powerTastingFactor },
                gutsTastingFactor = gutsTastingFactor.getOrElse(it) { defaultOption.gutsTastingFactor },
                wisdomTastingFactor = wisdomTastingFactor.getOrElse(it) { defaultOption.wisdomTastingFactor },
                tastingMinFailureRate = tastingMinFailureRate.getOrElse(it) { defaultOption.tastingMinFailureRate },
                gaugeScore = gaugeScore.getOrElse(it) { defaultOption.gaugeScore },
                gaugeMaxScore = gaugeMaxScore.getOrElse(it) { defaultOption.gaugeMaxScore },
            )
        }
        val factorList = factor.map { StatusType.valueOf(it.first) to it.second.toInt() }
        val evaluateSetting = Runner.ramenSettingTemplate[evaluate]!!
        System.err.println(options)
        val result = runBlocking {
            Runner.runAndEvaluate(
                count, Scenario.RAMEN, charaData, supportList,
                factorList, evaluateSetting,
                scenarioEvents = { RamenScenarioEvents() },
                selector = { RamenActionSelector(*options.toTypedArray()) },
            )
        }
        println(result.first)
    }
}

fun main(args: Array<String>) = RamenCli().main(args)

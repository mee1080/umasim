package io.github.mee1080.umasim.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int
import io.github.mee1080.umasim.ai.BCActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.bc.BCRoute
import io.github.mee1080.umasim.scenario.bc.BCScenarioEvents
import io.github.mee1080.umasim.simulation2.Runner
import kotlinx.coroutines.runBlocking

class BCCli : CliktCommand() {

    private val defaultOption = BCActionSelector.Option()

    private val dataDir by option().required()

    private val chara by option(help = "育成ウマ娘 レアリティ ランク").triple().required()

    private val support by option(help = "サポートカード 凸数").pair().multiple()

    private val factor by option(help = "因子").pair().multiple()

    private val count by option(help = "実行回数").int().default(100)

    private val route by option().required()

    private val status by option().int().multiple()
    private val wisdom by option().int().multiple()
    private val skillPt by option().int().multiple()
    private val hp by option().int().multiple()
    private val motivation by option().int().multiple()

    private val relation by option().int().multiple()
    private val outingRelation by option().int().multiple()
    private val wisdomRelation by option().int().multiple()
    private val hpKeep by option().int().multiple()
    private val risk by option().int().multiple()

    private val dreamGauge1 by option().int().multiple()
    private val dreamGauge2 by option().int().multiple()
    private val dreamGauge3 by option().int().multiple()
    private val dreamGaugeMax by option().int().multiple()

    override fun run() {
        StoreLoader.load(dataDir)
        val charaData = Store.getChara(chara.first, chara.second.toInt(), chara.third.toInt())
        val supportList = support.map { Store.getSupportByName(it.first, it.second.toInt()) }
        val options = List(4) {
            val dreamGauge1 = dreamGauge1.getOrElse(it) { defaultOption.dreamGauge[0] }
            val dreamGauge2 = dreamGauge2.getOrElse(it) { defaultOption.dreamGauge[1] }
            val dreamGauge3 = dreamGauge3.getOrElse(it) { defaultOption.dreamGauge[2] }
            BCActionSelector.Option(
                status = status.getOrElse(it) { defaultOption.status },
                wisdom = wisdom.getOrElse(it) { defaultOption.wisdom },
                skillPt = skillPt.getOrElse(it) { defaultOption.skillPt },
                hp = hp.getOrElse(it) { defaultOption.hp },
                motivation = motivation.getOrElse(it) { defaultOption.motivation },
                relation = relation.getOrElse(it) { defaultOption.relation },
                outingRelation = outingRelation.getOrElse(it) { defaultOption.outingRelation },
                wisdomRelation = wisdomRelation.getOrElse(it) { defaultOption.wisdomRelation },
                hpKeep = hpKeep.getOrElse(it) { defaultOption.hpKeep },
                risk = risk.getOrElse(it) { defaultOption.risk },
                dreamGauge = listOf(
                    dreamGauge1,
                    dreamGauge1 - dreamGauge2,
                    dreamGauge1 - dreamGauge2 - dreamGauge3,
                ),
                dreamGaugeMax = dreamGaugeMax.getOrElse(it) { defaultOption.dreamGaugeMax },
            )
        }
        val factorList = factor.map { StatusType.valueOf(it.first) to it.second.toInt() }
        val evaluateSetting = Runner.bcSetting
        val route = BCRoute.valueOf(route)
        System.err.println(options)
        val result = runBlocking {
            Runner.runAndEvaluate(
                count, Scenario.BC, charaData, supportList,
                factorList, evaluateSetting,
                scenarioEvents = { BCScenarioEvents(route) },
                selector = { BCActionSelector(*options.toTypedArray()) },
            )
        }
        println(result.first)
    }
}

fun main(args: Array<String>) = BCCli().main(args)

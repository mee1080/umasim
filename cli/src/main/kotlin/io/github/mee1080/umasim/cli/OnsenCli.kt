package io.github.mee1080.umasim.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int
import io.github.mee1080.umasim.ai.OnsenActionSelector
import io.github.mee1080.umasim.ai.OnsenActionSelector2
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Runner
import kotlinx.coroutines.runBlocking

class OnsenCli : CliktCommand() {

    private val defaultOption = OnsenActionSelector2.Option()

    private val dataDir by option().required()

    private val chara by option(help = "育成ウマ娘 レアリティ ランク").triple().required()

    private val support by option(help = "サポートカード 凸数").pair().multiple()

    private val factor by option(help = "因子").pair().multiple()

    private val count by option(help = "実行回数").int().default(100)

    private val mode by option().int().default(1)

    private val status by option().int().default(defaultOption.status)
    private val skillPt by option().int().default(defaultOption.skillPt)
    private val hp by option().int().default(defaultOption.hp)
    private val motivation by option().int().default(defaultOption.motivation)

    private val relation by option().int().default(defaultOption.relation)
    private val risk by option().int().default(defaultOption.risk)
    private val keepHp by option().int().default(defaultOption.keepHp)
    private val dig by option().int().default(defaultOption.dig)

    private val bathThreshold by option().int().default(defaultOption.bathThreshold)

    override fun run() {
        StoreLoader.load(dataDir)
        val charaData = Store.getChara(chara.first, chara.second.toInt(), chara.third.toInt())
        val supportList = support.map { Store.getSupportByName(it.first, it.second.toInt()) }
        val option = when (mode) {
            2 -> OnsenActionSelector2.Option(
                status = status,
                skillPt = skillPt,
                hp = hp,
                motivation = motivation,
                relation = relation,
                risk = risk,
                keepHp = keepHp,
                dig = dig,
                bathThreshold = bathThreshold,
            )

            else -> OnsenActionSelector.Option(
                status = status,
                skillPt = skillPt,
                hp = hp,
                motivation = motivation,
                relation = relation,
                risk = risk,
                keepHp = keepHp,
                dig = dig,
            )
        }
        val factorList = factor.map { StatusType.valueOf(it.first) to it.second.toInt() }
        val evaluateSetting = Runner.onsenSetting
        System.err.println(option)
        val result = runBlocking {
            Runner.runAndEvaluate(
                count, Scenario.ONSEN, charaData, supportList,
                factorList, evaluateSetting,
                selector = option::generateSelector,
            )
        }
        println(result.first)
    }
}

fun main(args: Array<String>) = OnsenCli().main(args)

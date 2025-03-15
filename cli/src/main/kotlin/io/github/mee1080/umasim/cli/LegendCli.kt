package io.github.mee1080.umasim.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int
import io.github.mee1080.umasim.ai.LegendActionSelector
import io.github.mee1080.umasim.ai.generator
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.legend.LegendScenarioEvents
import io.github.mee1080.umasim.scenario.legend.getLegendBuff
import io.github.mee1080.umasim.simulation2.Runner
import io.github.mee1080.utility.replaced
import kotlinx.coroutines.runBlocking

class LegendCli : CliktCommand() {
    private val dataDir by option()

    private val chara by option(help = "育成ウマ娘 レアリティ ランク").triple().required()

    private val support by option(help = "サポートカード 凸数").pair().multiple()

    private val factor by option(help = "因子").pair().multiple()

    private val count by option(help = "実行回数").int().default(100)

    private val speed by option().int().multiple()
    private val stamina by option().int().multiple()
    private val power by option().int().multiple()
    private val guts by option().int().multiple()
    private val wisdom by option().int().multiple()

    private val training by option().int().multiple()
    private val hp by option().int().multiple()
    private val hpKeep by option().int().multiple()
    private val motivation by option().int().multiple()
    private val risk by option().int().multiple()

    private val relation by option().int().multiple()
    private val outingRelation by option().int().multiple()

    private val friend by option().int().multiple()
    private val friendCount by option().int().multiple()
    private val supportCount by option().int().multiple()
    private val guestCount by option().int().multiple()

    private val forcedSupportCount by option().int().multiple()
    private val supportBestFriendGauge by option().int().multiple()
    private val forcedGuestCount by option().int().multiple()

    private val buff by option()

    private val evaluate by option().triple().multiple()

    override fun run() {
        StoreLoader.load(dataDir)
        val charaData = Store.getChara(chara.first, chara.second.toInt(), chara.third.toInt())
        val supportList = support.map { Store.getSupportByName(it.first, it.second.toInt()) }
        val oprions = (0..1).map {
            LegendActionSelector.Option(
                speed = speed.getOrElse(it) { speed.first() },
                stamina = stamina.getOrElse(it) { stamina.first() },
                power = power.getOrElse(it) { power.first() },
                guts = guts.getOrElse(it) { guts.first() },
                wisdom = wisdom.getOrElse(it) { wisdom.first() },

                training = training.getOrElse(it) { training.first() },
                hp = hp.getOrElse(it) { hp.first() },
                hpKeep = hpKeep.getOrElse(it) { hpKeep.first() },
                motivation = motivation.getOrElse(it) { motivation.first() },
                risk = risk.getOrElse(it) { risk.first() },

                relation = relation.getOrElse(it) { relation.first() },
                outingRelation = outingRelation.getOrElse(it) { outingRelation.first() },

                friend = friend.getOrElse(it) { friend.first() },
                friendCount = friendCount.getOrElse(it) { friendCount.first() },
                supportCount = supportCount.getOrElse(it) { supportCount.first() },
                guestCount = guestCount.getOrElse(it) { guestCount.first() },

                forcedSupportCount = forcedSupportCount.getOrElse(it) { forcedSupportCount.first() },
                supportBestFriendGauge = supportBestFriendGauge.getOrElse(it) { supportBestFriendGauge.first() },
                forcedGuestCount = forcedGuestCount.getOrElse(it) { forcedGuestCount.first() },
            )
        }
        val generator = oprions.generator()
        val factorList = factor.map { StatusType.valueOf(it.first) to it.second.toInt() }
        val evaluateSetting = evaluate.fold(Runner.noLimitSetting) { acc, setting ->
            acc.replaced(StatusType.valueOf(setting.first), setting.second.toDouble() to setting.third.toInt())
        }
        val buffList = buff?.split(",")?.map {
            getLegendBuff(it)
        } ?: emptyList()
        oprions.forEach { System.err.println(it) }
        System.err.println(buffList.joinToString { it?.name ?: "null" })
        val result = runBlocking {
            Runner.runAndEvaluate(
                count, Scenario.LEGEND, charaData, supportList,
                factorList, evaluateSetting,
                scenarioEvents = { LegendScenarioEvents(buffList) },
                selector = generator::generateSelector,
            )
        }
        println(result.first)
    }
}

fun main(args: Array<String>) = LegendCli().main(args)

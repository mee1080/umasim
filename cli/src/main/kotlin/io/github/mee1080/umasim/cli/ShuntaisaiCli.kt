package io.github.mee1080.umasim.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import io.github.mee1080.umasim.ai.ShuntaisaiActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Evaluator
import io.github.mee1080.umasim.simulation2.Runner
import io.github.mee1080.umasim.simulation2.Simulator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import kotlin.math.roundToInt

/**
 * 駿大祭AIの評価（Runner.shuntaisaiSettingのスコア上位20%の平均を標準出力に出力）
 *
 * AIの設定はデフォルト設定（または--option-file）を、--paramで上書きする
 */
class ShuntaisaiCli : CliktCommand() {

    private val dataDir by option().required()

    private val chara by option(help = "育成ウマ娘 レアリティ ランク").triple().required()

    private val support by option(help = "サポートカード 凸数").pair().multiple()

    private val withoutSkills by option(help = "サポートカードのスキルを除外").flag()

    private val factor by option(help = "因子").pair().multiple()

    private val count by option(help = "実行回数").int().default(100)

    private val threads by option(help = "並列スレッド数").int().default(4)

    private val optionFile by option(help = "AI設定（Optionの配列のJSON）").file(mustExist = true)

    private val param by option(
        help = "AI設定の上書き（名前@期間=値、期間は0:ジュニア 1:クラシック 2:シニア 3:URAファイナルズ、省略時は全期間）",
    ).multiple()

    override fun run() {
        StoreLoader.load(dataDir)
        val charaData = Store.getChara(chara.first, chara.second.toInt(), chara.third.toInt())
        val supportList = support.map { Store.getSupportByName(it.first, it.second.toInt()) }
            .map { if (withoutSkills) it.copy(skills = emptyList()) else it }
        val factorList = factor.map { StatusType.valueOf(it.first) to it.second.toInt() }
        val options = applyParams(
            optionFile?.readText() ?: ShuntaisaiActionSelector.encodeOptions(ShuntaisaiActionSelector.defaultOptions),
        )
        System.err.println(ShuntaisaiActionSelector.encodeOptions(options))
        val setting = Runner.shuntaisaiSetting
        val summaries = runBlocking {
            (0 until threads).map { index ->
                async(Dispatchers.Default) {
                    val n = count / threads + if (index < count % threads) 1 else 0
                    List(n) {
                        Simulator(Scenario.SHUNTAISAI, charaData, supportList, factorList)
                            .simulate(ShuntaisaiActionSelector(*options))
                    }
                }
            }.awaitAll().flatten()
        }
        val evaluator = Evaluator(summaries, setting, 0.2)
        println((evaluator.upperSum(1.0, setting) * 1000).roundToInt() / 1000.0)
    }

    private fun applyParams(baseJson: String): Array<ShuntaisaiActionSelector.Option> {
        val periods = Json.parseToJsonElement(baseJson).jsonArray.map { it.jsonObject.toMutableMap() }
        param.forEach { arg ->
            val (key, value) = arg.split("=", limit = 2)
            val (name, period) = key.split("@", limit = 2).let { it[0] to it.getOrNull(1)?.toInt() }
            val targets = if (period == null) periods else listOf(periods[period])
            targets.forEach { target ->
                val current = target[name] as? JsonPrimitive ?: error("unknown param: $name")
                // Int型のパラメータは整数に丸める
                target[name] = if (current.intOrNull != null) {
                    JsonPrimitive(value.toDouble().roundToInt())
                } else {
                    JsonPrimitive(value.toDouble())
                }
            }
        }
        return ShuntaisaiActionSelector.decodeOptions(JsonArray(periods.map { JsonObject(it) }).toString())
    }
}

fun main(args: Array<String>) = ShuntaisaiCli().main(args)

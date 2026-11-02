package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.ShuntaisaiActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt
import kotlin.time.measureTime

fun debugShuntaisai() {
    val time = measureTime {
        debugShuntaisaiSingleSimulation()
//        debugShuntaisaiRunSimulation()
    }
    println("time: $time")
}

private val shuntaisaiDebugSupport
    get() = Store.getSupportByName(
        "[As if Guided]エフフォーリア" to 4,
        "[時に交わる海と空]ミスターシービー" to 4,
        "[『ずーっと一緒、だぞー！』]ブラストワンピース" to 4,
        "[Time of Departure]マヤノトップガン" to 4,
        "[冷凜なるビスクドール]アドマイヤグルーヴ" to 4,
        "[いとどめでたき、まつりかな]吾妻" to 4,
    )

private val shuntaisaiDebugFactor = listOf(
    StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
    StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
)

fun debugShuntaisaiRunSimulation() {
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val support = shuntaisaiDebugSupport
    println(chara.name)
    println(support.joinToString(", ") { it.name })
    runBlocking {
        repeat(8) { index ->
            val selector = ShuntaisaiActionSelector.defaultOption
            launch(context) {
                val summary = Runner.run(
                    1000,
                    Scenario.SHUNTAISAI,
                    chara,
                    support,
                    shuntaisaiDebugFactor,
                    selector = { selector.generateSelector() },
                )
                val evaluator = Evaluator(summary, Runner.shuntaisaiSetting, 0.2)
                val score = (evaluator.upperSum(1.0, Runner.shuntaisaiSetting) * 1000).roundToInt() / 1000.0
                println("0,$index,0,${evaluator.toSummaryString()},$score")
            }
        }
    }
}

fun debugShuntaisaiSingleSimulation() {
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val support = shuntaisaiDebugSupport
    println(chara.name)
    println(support.joinToString(", ") { it.name })
    val selector = ShuntaisaiActionSelector.defaultOption.generateSelector()
    val result = runBlocking {
        Simulator(Scenario.SHUNTAISAI, chara, support, shuntaisaiDebugFactor)
            .simulateWithHistory(selector) { RandomEvents(it) }
    }
    result.second.forEachIndexed { index, history ->
        println()
        println("${index + 1}:")
        println("  開始時: ${history.beforeActionState.status.toShortString()}")
        println("  トレLv: ${history.beforeActionState.training.map { "${it.type}${it.level} " }}")
        println("  駿大祭: ${history.beforeActionState.shuntaisaiStatus?.toShortString()}")
        history.selections.forEach { (selection, selectedAction, result) ->
            println()
            selection.forEach { action ->
                println("  ・${action.name}")
                val total = action.candidates.sumOf { it.second } / 100.0
                action.candidates.forEach {
                    println("    ${it.second / total}% ${it.first}")
                }
                action.infoToString().split("/").forEach {
                    if (it.isNotEmpty()) println("    $it")
                }
                println()
            }
            println("  -> ${selectedAction.name}")
            if (selectedAction is MultipleAction) {
                println("     結果: $result")
            }
        }
        println()
        println("  終了時: ${(history.afterTurnState.status).toShortString()}")
        println("  駿大祭: ${history.afterTurnState.shuntaisaiStatus?.toShortString()}")
    }
    println(result.first)
    println(result.first.status.toShortString())
}

package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.DeBuffActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.Evaluator
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.simulation2.Summary
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

fun deBuffSimulation(
    testCount: Int,
) {
    val chara = Store.getChara("[ポインセチア・リボン]ナイスネイチャ", 5, 5)
    val support = Store.getSupportByName(
        "[共に同じ道を！]桐生院葵",
        "[その背中を越えて]サトノダイヤモンド",
        "[マーベラス☆大作戦]マーベラスサンデー",
        "[生体Aに関する実験的研究]アグネスタキオン",
        "[トレセン学園]シンボリルドルフ",
        "[幸せは曲がり角の向こう]ライスシャワー",
    )
    println(chara.name)
    support.forEach { println(it.name) }
    println("start ${LocalDateTime.now()}")
    println(DeBuffActionSelector.deBuffSkills.joinToString(","))
    runBlocking {
        arrayOf(Scenario.URA, Scenario.AOHARU).map { scenario ->
            launch(context) {
                val summaries = mutableListOf<Summary>()
                val skillCount = DeBuffActionSelector.deBuffSkills.associateWith { 0 }.toMutableMap()
                val totalSkillCount = IntArray(DeBuffActionSelector.deBuffSkills.size + 1) { 0 }
                repeat(testCount) {
                    val summary = Simulator(scenario, chara, support).simulate(59, DeBuffActionSelector())
                    summaries.add(summary)
                    val skills =
                        summary.status.skillHint.keys.filter { DeBuffActionSelector.deBuffSkillSet.contains(it) }
                    totalSkillCount[skills.size]++
                    skills.forEach { skillCount[it] = (skillCount[it] ?: 0) + 1 }
                }
                println(",$scenario,,${Evaluator(summaries).toSummaryString()}")
                println(totalSkillCount.joinToString(","))
                println(DeBuffActionSelector.deBuffSkills.map { skillCount[it] }.joinToString(","))
            }
        }.forEach {
            it.join()
        }
    }
    println("finished ${LocalDateTime.now()}")
}

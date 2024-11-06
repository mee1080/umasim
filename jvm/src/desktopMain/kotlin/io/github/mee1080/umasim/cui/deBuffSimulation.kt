package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.SkillActionSelector
import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.simulation2.Evaluator
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.simulation2.Summary
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

fun deBuffSimulation(testCount: Int) = skillSimulation(
    testCount,
    Store.getChara("[ポインセチア・リボン]ナイスネイチャ", 5, 5),
    Store.getSupportByName(
        "[共に同じ道を！]桐生院葵",
        "[その背中を越えて]サトノダイヤモンド",
        "[マーベラス☆大作戦]マーベラスサンデー",
        "[生体Aに関する実験的研究]アグネスタキオン",
        "[トレセン学園]シンボリルドルフ",
        "[幸せは曲がり角の向こう]ライスシャワー",
    ),
    SkillActionSelector.deBuffSkills,
)

fun jigatameSimulation(testCount: Int) = skillSimulation(
    testCount,
    Store.getChara("[あおぐもサミング]セイウンスカイ", 5, 5),
    Store.getSupportByName(
        "[副会長の一刺し]エアグルーヴ",
        "[トレセン学園]シンボリルドルフ",
        "[『愛してもらうんだぞ』]オグリキャップ",
        "[ウマ王伝説・最強になった件]ゴールドシップ",
        "[迫る熱に押されて]キタサンブラック",
        "[パッションチャンピオーナ！]エルコンドルパサー",
    ),
    SkillActionSelector.jigatame,
)

fun skillSimulation(
    testCount: Int,
    chara: Chara,
    support: List<SupportCard>,
    targetSkills: Array<String>,
) {
    println(chara.name)
    support.forEach { println(it.name) }
    println("start ${LocalDateTime.now()}")
    println(targetSkills.joinToString(","))
    runBlocking {
        arrayOf(Scenario.URA, Scenario.AOHARU).map { scenario ->
            launch(context) {
                val summaries = mutableListOf<Summary>()
                val skillCount = targetSkills.associateWith { 0 }.toMutableMap()
                val totalSkillCount = IntArray(targetSkills.size + 1) { 0 }
                repeat(testCount) {
                    val summary = Simulator(scenario, chara, support).simulate(SkillActionSelector(targetSkills))
                    summaries.add(summary)
                    val skills =
                        summary.status.skillHint.keys.filter { targetSkills.contains(it) }
                    totalSkillCount[skills.size]++
                    skills.forEach { skillCount[it] = (skillCount[it] ?: 0) + 1 }
                }
                println(",$scenario,,${Evaluator(summaries).toSummaryString()}")
                println(totalSkillCount.joinToString(","))
                println(targetSkills.map { skillCount[it] }.joinToString(","))
            }
        }.forEach {
            it.join()
        }
    }
    println("finished ${LocalDateTime.now()}")
}

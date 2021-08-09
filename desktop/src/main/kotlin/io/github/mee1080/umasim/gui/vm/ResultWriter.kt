package io.github.mee1080.umasim.gui.vm

import io.github.mee1080.umasim.ai.FactorBasedActionSelector
import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.gui.Constants
import io.github.mee1080.umasim.simulation.Evaluator
import io.github.mee1080.umasim.simulation.Summary
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ResultWriter {

    companion object {
        private const val OUTPUT_DIR = "result"
    }

    private val fileNameFormatter = SimpleDateFormat("yyyyMMddHHmmss")

    fun output(
        chara: Chara,
        support: List<SupportCard>,
        option: FactorBasedActionSelector.Option,
        count: Int,
        turn: Int,
        summaries: List<Summary>,
    ): File {
        val date = Date()
        val file = generateFile(date)
        file.printWriter(Charsets.UTF_8).use { writer ->
            val evaluator = Evaluator(summaries)
            writer.println(
                """
                ウマ娘育成シミュレータ
                シミュレーション実行結果
                
                プログラムバージョン: ${Constants.VERSION}
                実行日時: ${SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date)}
                シミュレーション回数: $count
                
                育成キャラ:
                ${chara.name} (☆${chara.rarity} 覚醒${chara.rank})
                
                サポートカード:
                ${support.getOrNull(0)?.let { "${it.name} (上限解放${it.talent})" } ?: "なし"}
                ${support.getOrNull(1)?.let { "${it.name} (上限解放${it.talent})" } ?: "なし"}
                ${support.getOrNull(2)?.let { "${it.name} (上限解放${it.talent})" } ?: "なし"}
                ${support.getOrNull(3)?.let { "${it.name} (上限解放${it.talent})" } ?: "なし"}
                ${support.getOrNull(4)?.let { "${it.name} (上限解放${it.talent})" } ?: "なし"}
                ${support.getOrNull(5)?.let { "${it.name} (上限解放${it.talent})" } ?: "なし"}
                
                ターン数:
                $turn
                
                AI設定:
                  スピード ${option.speedFactor}
                  スタミナ ${option.staminaFactor}
                  パワー　 ${option.powerFactor}
                  根性　　 ${option.gutsFactor}
                  賢さ　　 ${option.wisdomFactor}
                  スキルPt ${option.skillPtFactor}
                  体力　　 ${option.hpFactor}
                  やる気　 ${option.motivationFactor}
                
                実行結果:
                　,スピード,スタミナ,パワー,根性,賢さ,スキルPt,合計
                平均, ${evaluator.average().joinToString(", ")}, ${evaluator.averageSum()}
                50%上振れ平均, ${evaluator.upper(0.5).joinToString(", ")}, ${evaluator.upperSum(0.5)}
                20%上振れ平均, ${evaluator.upper(0.2).joinToString(", ")}, ${evaluator.upperSum(0.2)}
                5%上振れ平均, ${evaluator.upper(0.05).joinToString(", ")}, ${evaluator.upperSum(0.05)}
                
                行動回数平均:
                　,合計,友情0,友情1,友情2,友情3,友情4,友情5
                スピード, ${evaluator.trainingCount(StatusType.SPEED).joinToString(", ")}
                スタミナ, ${evaluator.trainingCount(StatusType.STAMINA).joinToString(", ")}
                パワー, ${evaluator.trainingCount(StatusType.POWER).joinToString(", ")}
                根性, ${evaluator.trainingCount(StatusType.GUTS).joinToString(", ")}
                賢さ, ${evaluator.trainingCount(StatusType.WISDOM).joinToString(", ")}
                お休み, ${evaluator.averageSleepCount()}
                お出かけ, ${evaluator.averageOutingCount()}
            """.trimIndent()
            )
        }
        return file
    }

    @Synchronized
    private fun generateFile(date: Date): File {
        var file = File(OUTPUT_DIR, "result_${fileNameFormatter.format(date)}.txt")
        if (file.exists()) {
            for (i in 1..1000) {
                file = File(OUTPUT_DIR, "result_${fileNameFormatter.format(date)}_$i.txt")
                if (!file.exists()) break
            }
            if (file.exists()) throw IllegalStateException()
        }
        if (!file.parentFile.exists() && !file.parentFile.mkdirs()) throw IllegalStateException()
        if (!file.createNewFile()) throw IllegalStateException()
        return file
    }
}
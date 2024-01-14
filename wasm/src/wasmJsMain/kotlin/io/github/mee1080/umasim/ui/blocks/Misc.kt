package io.github.mee1080.umasim.ui.blocks

import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun Header() {
    Row {
        Text("ウマ娘レースエミュレータ", style = MaterialTheme.typography.h4)
    }
}

@Composable
fun Footer() {
    Row { Text("注意事項", style = MaterialTheme.typography.h6) }
    """
        あくまで目安。適当実装＆データの正確性が低いので参考までに。
        ポジションキープを始めとした他ウマ娘が絡む要素は未実装。
        それが条件になるスキルは適当にそれっぽく実装してます。
        喰らう妨害スキルは一律発動率80％としています。
        適性は直接ステータスを修正するものではないので、下の補正後ステータスには反映されません。
        各種別情報は大いに参考させて頂きました。
    """.trimIndent().split("\n").forEachIndexed { index, c ->
        Row { Text("${index + 1}: ${c.trim()}") }
    }

    Row { Text("その他", style = MaterialTheme.typography.h6) }
    Row { Text("本プログラムは、砂井裏鍵さん（X: @urakagi）作のレースエミュレータを、mee1080（X: @mee10801）がKotlinに移植しました") }
    Row { Text("オリジナル版：http://race.wf-calc.net/") }
}
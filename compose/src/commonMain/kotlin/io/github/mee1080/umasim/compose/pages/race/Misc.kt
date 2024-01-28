package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.compose.common.atoms.TextLink

@Composable
fun Header() {
    Row {
        Text("ウマ娘レースエミュレータ", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun Footer() {
    Row { Text("注意事項", style = MaterialTheme.typography.headlineSmall) }
    """
        あくまで目安。適当実装＆データの正確性が低いので参考までに。
        ポジションキープを始めとした他ウマ娘が絡む要素は未実装。
        それが条件になるスキルはその条件を無視しているため、実際より強いです。特に鋼の意志、スリスト等。
        適性は直接ステータスを修正するものではないので、下の補正後ステータスには反映されません。
        各種別情報は大いに参考させて頂きました。
    """.trimIndent().split("\n").forEachIndexed { index, c ->
        Row { Text("${index + 1}: ${c.trim()}") }
    }

    Row { Text("その他", style = MaterialTheme.typography.headlineSmall) }
    Row {
        Text("本プログラムは、砂井裏鍵さん（X: ")
        TextLink("https://twitter.com/urakagi", "@urakagi")
        Text("）作のレースエミュレータを、mee1080（X: ")
        TextLink("https://twitter.com/mee10801", "@mee10801")
        Text("）がKotlinに移植しました")
    }
    Row {
        Text("オリジナル版：")
        TextLink("http://race.wf-calc.net/")
    }
}
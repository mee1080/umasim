package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.compose.common.atoms.TextLink

@Composable
fun Header() {
    Row {
        Text("ウマ娘レースエミュレータ移植版", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun Footer() {
    HorizontalDivider()

    Column {
        Row { Text("注意事項", style = MaterialTheme.typography.headlineSmall) }
        """
        あくまで目安。適当実装＆データの正確性が低いので参考までに。
        ポジションキープを始めとした他ウマ娘が絡む要素は未実装。
        位置取り争い、追い比べ、リード確保、位置取り調整も未実装。スタミナは表示される以上に要ります。
        他ウマ娘が絡む発動条件は、毎フレーム一定確率で発動するよう近似。スリスト遊びは2回発動が多すぎるので要修正。
        順位条件その他色々無視。詳しくは該当スキルのツールチップに。
        固有のレベルは未実装。
        各種別情報は大いに参考させて頂きました。
    """.trimIndent().split("\n").forEachIndexed { index, c ->
            Row { Text("${index + 1}: ${c.trim()}") }
        }
    }

    HorizontalDivider()

    Column {
        Row { Text("本プログラムについて", style = MaterialTheme.typography.headlineSmall) }
        Row {
            Text("本プログラムは、砂井裏鍵さん（X: ")
            TextLink("https://twitter.com/urakagi", "@urakagi")
            Text("）作のレースエミュレータを、")
        }
        Row {
            Text("mee1080（X: ")
            TextLink("https://twitter.com/mee10801", "@mee10801")
            Text("）がKotlinに移植しました")
        }
        Row {
            Text("オリジナル版：")
            TextLink("http://race.wf-calc.net/")
        }
    }

    HorizontalDivider()

    Column {
        Row { Text("オープンソースライセンス", style = MaterialTheme.typography.headlineSmall) }
        Text("画面表示には、「LINE Seed JP」フォントを使用しています。")
        Text("\"LINE Seed JP\" is licensed under the SIL Open Font License 1.1 (c) LY Corporation")
        TextLink("https://seed.line.me/index_jp.html")
    }
}
package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.TextWithLink

@Composable
fun Header() {
    Row {
        Text("ウマ娘レースエミュレータ移植版", style = MaterialTheme.typography.headlineMedium)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(8.dp)
    ) {
        Text("5周年アップデート反映状況", style = MaterialTheme.typography.headlineSmall)
        """
            【未実装】「全開スパート」システムを追加
            【未実装】レース中に発動した一部スキルの効果量が賢さに応じて上昇する仕組みを追加
            【未実装】「リード確保」システムの調整
            【要修正】コースの直線やコーナーの距離を、実際のレース場の距離に合わせて調整（大井1800ダ、ロンシャン2400芝）
            【要確認】「小回り◎」「小回り○」「小回り×」が発動するように調整（新潟と中山の一部）
            【未実装】「助走距離システム」を追加
            【要確認】助走距離システムの追加に伴い、スキル詳細において発動タイミングが 「スタート時」と記載されたスキルの、発動条件の仕組みを調整
            【要修正】サンタアニタパークレース場、デルマーレース場追加
        """.trimIndent().split("\n").forEach {
            Row { Text(it.trim()) }
        }
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
        TextWithLink(
            listOf(
                "本プログラムは、砂井裏鍵さん（X: " to null,
                "@urakagi" to "https://twitter.com/urakagi",
                "）作のレースエミュレータを、mee1080（X: " to null,
                "@mee10801" to "https://twitter.com/mee10801",
                "）がKotlinに移植しました" to null,
            )
        )
        TextWithLink("オリジナル版：http://race.wf-calc.net/")
    }

    HorizontalDivider()

    Column {
        Row { Text("オープンソースライセンス", style = MaterialTheme.typography.headlineSmall) }
        TextWithLink("画面表示には、「LINE Seed JP」フォント（https://seed.line.me/index_jp.html）を使用しています。")
        Text("\"LINE Seed JP\" is licensed under the SIL Open Font License 1.1 (c) LY Corporation.")
    }
}
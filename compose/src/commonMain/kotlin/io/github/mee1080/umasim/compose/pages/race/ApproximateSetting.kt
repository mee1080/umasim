package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.race.calc2.SystemSetting
import io.github.mee1080.umasim.race.data2.ApproximateMultiCondition
import io.github.mee1080.umasim.race.data2.approximateConditions
import io.github.mee1080.umasim.race.roundPercentString

@Composable
fun ApproximateSetting() {
    HorizontalDivider()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // TODO 編集
        val systemSetting = SystemSetting()
        Column {
            Text("近似条件", style = MaterialTheme.typography.headlineSmall)
            Text("以下の項目は、シミュレーションが難しいため、近似処理を行っています")
            Text("（いずれ変更できるようにしたい）", style = MaterialTheme.typography.bodySmall)
        }

        Column {
            Text("ポジションキープ", style = MaterialTheme.typography.titleLarge)
            Text("以下のセクションで、ペースダウンモードに入ります")
            Text("逃げの各モード、およびペースアップモードは実装していません")
            Text(
                "先行：${
                    systemSetting.positionKeepSectionSen.mapIndexed { index, value -> index to value }
                        .filter { it.second }
                        .joinToString { (it.first + 1).toString() }
                }"
            )
            Text(
                "差し：${
                    systemSetting.positionKeepSectionSasi.mapIndexed { index, value -> index to value }
                        .filter { it.second }
                        .joinToString { (it.first + 1).toString() }
                }"
            )
            Text(
                "追込：${
                    systemSetting.positionKeepSectionOi.mapIndexed { index, value -> index to value }
                        .filter { it.second }
                        .joinToString { (it.first + 1).toString() }
                }"
            )
        }

        Column {
            Text("位置取り争い", style = MaterialTheme.typography.titleLarge)
            Text("逃げの場合に${systemSetting.leadCompetitionPosition}mの位置で固定発動します")
        }

        Column {
            Text("追い比べ", style = MaterialTheme.typography.titleLarge)
            Text("最終直線で1秒毎に、${systemSetting.competeFightRate.roundPercentString()}%の確率で発動します")
        }

        Column {
            Text("脚色十分", style = MaterialTheme.typography.titleLarge)
            Text("持続時間は3秒×距離係数(0.45/1.0/0.875/0.8)で固定です")
            Text("脚ためは実装していません（そもそも解析されてない認識）")
        }

        Column {
            Text("位置取り調整/持久力温存", style = MaterialTheme.typography.titleLarge)
            Text("発生率100%固定で、体力が足りていれば位置取り調整、足りていなければ持久力温存になります")
        }

        Column {
            Text("リード確保", style = MaterialTheme.typography.titleLarge)
            Text("発生率100%固定です")
        }

        Column {
            Text("スタミナ勝負", style = MaterialTheme.typography.titleLarge)
            Text("ランダムで0.95～1.02の倍率がかかるようですが1.0倍固定です")
        }

        Column {
            Text("スキル発動", style = MaterialTheme.typography.titleLarge)
            Text("他のウマ娘が関わるスキル発動条件は、1秒ごとに、以下の判定を行っています")
            Text(
                "(適当に設定してるので実態とかけ離れてるとかの意見は歓迎です)",
                style = MaterialTheme.typography.bodySmall
            )
            approximateConditions.forEach { (key, condition) ->
                Text(condition.displayName, modifier = Modifier.padding(top = 8.dp))
                if (condition is ApproximateMultiCondition) {
                    condition.conditions.forEach {
                        Text(
                            "${it.first.displayName} : ${it.first.description}",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                } else {
                    Text(condition.description, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

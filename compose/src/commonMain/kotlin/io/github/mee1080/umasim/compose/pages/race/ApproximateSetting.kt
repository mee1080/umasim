package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.race.data2.ApproximateMultiCondition
import io.github.mee1080.umasim.race.data2.approximateConditions

@Composable
fun ApproximateSetting() {
    HorizontalDivider()

    Column {
        Row { Text("近似条件", style = MaterialTheme.typography.headlineSmall) }
        Text("他のウマ娘が関わるスキル発動条件は、1秒ごとに、以下の判定を行っています")
        Text("(適当に設定してるので実態とかけ離れてるとかの意見は歓迎です)", style = MaterialTheme.typography.bodySmall)
        approximateConditions.forEach { (key, condition) ->
            Text(condition.displayName, modifier = Modifier.padding(top = 8.dp))
            if (condition is ApproximateMultiCondition) {
                condition.conditions.forEach {
                    Text("${it.first.displayName} : ${it.first.description}", modifier = Modifier.padding(start = 8.dp))
                }
            } else {
                Text(condition.description, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

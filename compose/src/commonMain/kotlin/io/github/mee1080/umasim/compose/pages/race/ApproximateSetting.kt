package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.SelectBox
import io.github.mee1080.umasim.race.calc2.SystemSetting
import io.github.mee1080.umasim.race.data.PositionKeepMode
import io.github.mee1080.umasim.race.data2.ApproximateMultiCondition
import io.github.mee1080.umasim.race.data2.approximateConditions
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.setPositionKeepMode
import io.github.mee1080.umasim.store.operation.setPositionKeepRate
import io.github.mee1080.utility.toPercentString

@Composable
fun ApproximateSetting(state: AppState, dispatch: OperationDispatcher<AppState>) {
    val positionKeepMode by derivedStateOf { state.setting.positionKeepMode }
    val positionKeepRate by derivedStateOf { state.setting.positionKeepRate }
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

            SelectBox(
                PositionKeepMode.entries, positionKeepMode,
                onSelect = { dispatch(setPositionKeepMode(it)) },
                modifier = Modifier.width(512.dp),
                label = { Text("モード") },
                itemToString = { it.label },
            )
            when (positionKeepMode) {
                PositionKeepMode.APPROXIMATE -> {
                    Text("以下のセクションで、ペースダウンモードに入ります")
                    Text("掛かり状態でも発動します（位置固定のため）")
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

                PositionKeepMode.VIRTUAL -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column {
                            Text("以下のキャラとの差で判定します")
                            Text("ただし、逃げ同士の競り合いは未実装です（仮想ペースメーカーは一定確率でスピードアップモードに入ります）")
                        }
                        Text("仮想ペースメーカーのスピードアップモード確率: $positionKeepRate %")
                        Slider(
                            value = positionKeepRate.toFloat(),
                            onValueChange = { dispatch(setPositionKeepRate(it.toInt())) },
                            valueRange = 0f..100f,
                            steps = 100,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        ImportExport(true, state, dispatch)
                        CharaInput(true, state, dispatch)
                        SkillInput(true, state, dispatch)
                    }
                }

                PositionKeepMode.SPEED_UP -> {
                    Text("一定確率でスピードアップモードに入ります（実際は設定値に加えて賢さ判定もあり）")
                    Text("確率: $positionKeepRate %")
                    Slider(
                        value = positionKeepRate.toFloat(),
                        onValueChange = { dispatch(setPositionKeepRate(it.toInt())) },
                        valueRange = 0f..100f,
                        steps = 100,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                PositionKeepMode.NONE -> {
                    Text("ポジションキープ判定を行いません")
                }
            }
        }

        Column {
            Text("走行レーン", style = MaterialTheme.typography.titleLarge)
            Text("追い越しモードによるレーン移動は未実装です")
            Text("横ブロックによる移動停止は近似処理を行っています（スキル発動の欄を参照）")
            Text("外回りロスは全てのコーナーが90度として計算しています（いつか正確に計算したい）")
        }

        Column {
            Text("位置取り争い", style = MaterialTheme.typography.titleLarge)
            Text("逃げの場合に${systemSetting.leadCompetitionPosition}mの位置で固定発動します")
        }

        Column {
            Text("追い比べ", style = MaterialTheme.typography.titleLarge)
            Text("最終直線で1秒毎に、${systemSetting.competeFightRate.toPercentString()}の確率で発動します")
        }

        Column {
            Text("脚色十分", style = MaterialTheme.typography.titleLarge)
            Text("持続時間は3秒×距離係数(0.45/1.0/0.875/0.8)で固定です")
            Text("脚ためは実装していません（そもそも解析されてない認識）")
        }

        Column {
            Text("持久力温存", style = MaterialTheme.typography.titleLarge)
            Text("体力が足りていなければ、${systemSetting.staminaKeepRate.toPercentString()}の確率で発動します")
        }

        Column {
            Text("位置取り調整", style = MaterialTheme.typography.titleLarge)
            Text("持久力温存でなければ、${systemSetting.positionCompetitionRate.toPercentString()}の確率で発動します")
        }

        Column {
            Text("リード確保", style = MaterialTheme.typography.titleLarge)
            Text("追込以外で、${systemSetting.secureLeadRate.toPercentString()}の確率で発動します")
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
                if (condition.valueOnStart > 0) {
                    Text("スタート時は判定ON", modifier = Modifier.padding(start = 8.dp))
                }
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

/*
 * Copyright 2021 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.unsetWidth
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.*

@Composable
fun SupportCardInfo(model: ViewModel, state: State) {
    Table({ classes(AppStyle.supportCardTable) }) {
        Tr {
            Th({
                style {
                    property("border", "none")
                    unsetWidth()
                }
            }) { }
            Th { Text("友情ボーナス") }
            Th { Text("やる気効果アップ") }
            Th { Text("スピードボーナス") }
            Th { Text("スタミナボーナス") }
            Th { Text("パワーボーナス") }
            Th { Text("根性ボーナス") }
            Th { Text("賢さボーナス") }
            Th { Text("トレーニング効果アップ") }
            Th { Text("初期スピードアップ") }
            Th { Text("初期スタミナアップ") }
            Th { Text("初期パワーアップ") }
            Th { Text("初期根性アップ") }
            Th { Text("初期賢さアップ") }
            Th { Text("初期絆ゲージアップ") }
            Th { Text("レースボーナス") }
            Th { Text("ファン数ボーナス") }
            Th { Text("ヒントLvアップ") }
            Th { Text("ヒント発生率アップ") }
            Th { Text("得意率アップ") }
//            Th { Text("スピード限界値アップ") }
//            Th { Text("スタミナ限界値アップ") }
//            Th { Text("パワー限界値アップ") }
//            Th { Text("根性限界値アップ") }
//            Th { Text("賢さ限界値アップ") }
            Th { Text("イベント回復量アップ") }
            Th { Text("イベント効果アップ") }
            Th { Text("失敗率ダウン") }
            Th { Text("体力消費ダウン") }
//            Th { Text("ミニゲーム効果アップ") }
            Th { Text("スキルPtボーナス") }
            Th { Text("賢さ友情回復量アップ") }
            Th { Text("特殊固有") }
        }
        state.supportSelectionList.mapNotNull { it.card }.forEach { card ->
            Tr {
                Td({ style { unsetWidth() } }) { Text(card.name) }
                StatusRow(card) { it.friend }
                StatusRow(card) { it.motivation }
                StatusRow(card) { it.speedBonus }
                StatusRow(card) { it.staminaBonus }
                StatusRow(card) { it.powerBonus }
                StatusRow(card) { it.gutsBonus }
                StatusRow(card) { it.wisdomBonus }
                StatusRow(card) { it.training }
                StatusRow(card) { it.initialSpeed }
                StatusRow(card) { it.initialStamina }
                StatusRow(card) { it.initialPower }
                StatusRow(card) { it.initialGuts }
                StatusRow(card) { it.initialWisdom }
                StatusRow(card) { it.initialRelation }
                StatusRow(card) { it.race }
                StatusRow(card) { it.fan }
                StatusRow(card) { it.hintLevel }
                StatusRow(card) { it.hintFrequency }
                StatusRow(card) { it.specialtyRate }

                StatusRow(card) { it.eventRecovery }
                StatusRow(card) { it.eventEffect }
                StatusRow(card) { it.failureRate }
                StatusRow(card) { it.hpCost }
                StatusRow(card) { it.skillPtBonus }
                StatusRow(card) { it.wisdomFriendRecovery }
                Td({ style { textAlign("unset") } }) { Text(card.specialUnique.joinToString(" / ") { it.description }) }
            }
        }
    }
}

@Composable
private fun StatusRow(card: SupportCard, getStatus: (SupportCard.SupportStatus) -> Int) {
    val status = getStatus(card.status)
    val unique = getStatus(card.unique)
    Td {
        Text(
            when {
                unique > 0 -> "$status+$unique"
                status > 0 -> "$status"
                else -> ""
            }
        )
    }
}
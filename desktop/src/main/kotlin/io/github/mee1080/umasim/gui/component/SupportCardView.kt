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
package io.github.mee1080.umasim.gui.component

import androidx.compose.foundation.BoxWithTooltip
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.SupportCard

@Composable
fun SupportCardView(
    card: SupportCard?,
    modifier: Modifier = Modifier,
    showTalent: Boolean = true,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    BoxWithTooltip(
        tooltip = {
            Surface(
                modifier = Modifier.shadow(4.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                if (card == null) {
                    Text(
                        text = "未選択",
                        modifier = Modifier.padding(10.dp)
                    )
                } else {
                    Column(Modifier.width(320.dp)) {
                        SupportStatus("友情ボーナス", card.status.friend, card.unique.friend)
                        SupportStatus("やる気効果アップ", card.status.motivation, card.unique.motivation)
                        SupportStatus("スピードボーナス", card.status.speedBonus, card.unique.speedBonus)
                        SupportStatus("スタミナボーナス", card.status.staminaBonus, card.unique.staminaBonus)
                        SupportStatus("パワーボーナス", card.status.powerBonus, card.unique.powerBonus)
                        SupportStatus("根性ボーナス", card.status.gutsBonus, card.unique.gutsBonus)
                        SupportStatus("賢さボーナス", card.status.wisdomBonus, card.unique.wisdomBonus)
                        SupportStatus("トレーニング効果アップ", card.status.training, card.unique.training)
                        SupportStatus("初期スピードアップ", card.status.initialSpeed, card.unique.initialSpeed)
                        SupportStatus("初期スタミナアップ", card.status.initialStamina, card.unique.initialStamina)
                        SupportStatus("初期パワーアップ", card.status.initialPower, card.unique.initialPower)
                        SupportStatus("初期根性アップ", card.status.initialGuts, card.unique.initialGuts)
                        SupportStatus("初期賢さアップ", card.status.initialWisdom, card.unique.initialWisdom)
                        SupportStatus("初期絆ゲージアップ", card.status.initialRelation, card.unique.initialRelation)
                        SupportStatus("レースボーナス", card.status.race, card.unique.race)
                        SupportStatus("ファン数ボーナス", card.status.fan, card.unique.fan)
                        SupportStatus("ヒントLvアップ", card.status.hintLevel, card.unique.hintLevel)
                        SupportStatus("ヒント発生率アップ", card.status.hintFrequency, card.unique.hintFrequency)
                        SupportStatus("得意率アップ", card.status.specialtyRate, card.unique.specialtyRate)
                        SupportStatus("イベント回復量アップ", card.status.eventRecovery, card.unique.eventRecovery)
                        SupportStatus("イベント効果アップ", card.status.eventEffect, card.unique.eventEffect)
                        SupportStatus("失敗率ダウン", card.status.failureRate, card.unique.failureRate)
                        SupportStatus("体力消費ダウン", card.status.hpCost, card.unique.hpCost)
                        SupportStatus("スキルPtボーナス", card.status.skillPtBonus, card.unique.skillPtBonus)
                        SupportStatus("賢さ友情回復量アップ", card.status.wisdomFriendRecovery, card.unique.wisdomFriendRecovery)
                        if (card.skills.isNotEmpty()) {
                            Text("スキルヒント", Modifier.padding(4.dp))
                            card.skills.chunked(2).forEach {
                                Row(Modifier.fillMaxWidth().padding(4.dp, 0.dp)) {
                                    Text(it[0], Modifier.weight(1f))
                                    if (it.size >= 2) Text(it[1], Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
        BaseCard(modifier, onClick, getTypeColor(card, 128), selected) {
            if (card == null) {
                Text("未選択")
            } else {
                Row {
                    Text(
                        getTypeShortText(card),
                        modifier = Modifier
                            .padding(0.dp, 4.dp, 4.dp, 4.dp)
                            .background(getTypeColor(card), RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    )
                    Text(
                        getRarityText(card),
                        modifier = Modifier
                            .padding(0.dp, 4.dp, 4.dp, 4.dp)
                            .background(getRarityColor(card), RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    )
                    Text(
                        card.name,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    if (showTalent) {
                        Text(
                            "${card.talent}凸",
                            modifier = Modifier
                                .padding(8.dp, 4.dp, 4.dp, 0.dp)
                                .background(Color(224, 224, 192), RoundedCornerShape(8.dp))
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun getTypeColor(card: SupportCard?, alpha: Int = 255) = when (card?.type) {
    StatusType.SPEED -> Color(69, 196, 255, alpha)
    StatusType.STAMINA -> Color(255, 144, 127, alpha)
    StatusType.POWER -> Color(255, 185, 21, alpha)
    StatusType.GUTS -> Color(255, 144, 186, alpha)
    StatusType.WISDOM -> Color(32, 216, 169, alpha)
    StatusType.FRIEND -> Color(255, 211, 108, alpha)
    else -> Color(128, 128, 128, alpha)
}

private fun getTypeShortText(card: SupportCard) = when (card.type) {
    StatusType.SPEED -> "スピ"
    StatusType.STAMINA -> "スタ"
    StatusType.POWER -> "パワ"
    StatusType.GUTS -> "根性"
    StatusType.WISDOM -> "賢さ"
    StatusType.FRIEND -> "友人"
    else -> "?"
}

private fun getRarityColor(card: SupportCard) = when (card.rarity) {
    1 -> Color(202, 220, 240)
    2 -> Color(252, 220, 139)
    3 -> Color(226, 198, 238)
    else -> Color.Transparent
}

private fun getRarityText(card: SupportCard) = when (card.rarity) {
    1 -> "R"
    2 -> "SR"
    3 -> "SSR"
    else -> "?"
}

@Composable
private fun ColumnScope.SupportStatus(name: String, status: Int, unique: Int) {
    if (status > 0 || unique > 0) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Divider(
                color = Color(224, 224, 224),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(0.dp, 0.dp, 0.dp, 1.dp),
                thickness = 1.dp
            )
            Text(name)
            Text(
                text = when {
                    status == 0 -> "固有"
                    unique == 0 -> status.toString()
                    else -> "固有 + $status"
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}
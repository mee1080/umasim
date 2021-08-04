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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.SupportCard

@Composable
fun SupportCardView(
    card: SupportCard?,
    modifier: Modifier = Modifier,
    showTalent: Boolean = true,
    onClick: () -> Unit = {}
) {
    BaseCard(modifier, onClick, getTypeColor(card, 128)) {
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
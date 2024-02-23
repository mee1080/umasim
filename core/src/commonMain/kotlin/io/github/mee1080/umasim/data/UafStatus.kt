/*
 * Copyright 2024 mee1080
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
package io.github.mee1080.umasim.data

import io.github.mee1080.umasim.simulation2.ScenarioMemberState

data class UafStatus(
    val athleticsLevel: Map<UafAthletic, Int> = UafAthletic.entries.associateWith { 1 },
    val trainingAthletics: Map<StatusType, UafAthletic> = emptyMap(),
    val heatUp: Map<UafGenre, Int> = UafGenre.entries.associateWith { 0 },
    val festivalBonus: Int = 0,
) {
    val genreLevel by lazy {
        athleticsLevel.entries.groupBy {
            it.key.genre
        }.mapValues { (_, values) ->
            values.sumOf { it.value }
        }
    }

    fun randomizeTraining(): UafStatus {
        return copy(
            trainingAthletics = StatusType.entries.associateWith {
                UafAthletic.byStatusType[it]!!.random()
            }
        )
    }

    fun consult(from: UafGenre, to: UafGenre): UafStatus {
        return copy(
            trainingAthletics = trainingAthletics.mapValues { (type, athletic) ->
                if (athletic.genre == from) {
                    UafAthletic.byStatusType[type]!!.first { it.genre == to }
                } else athletic
            }
        )
    }

    fun applyFestivalBonus(): UafStatus {
        val bonus = UafGenre.entries.sumOf {
            val level = genreLevel[it] ?: 0
            when {
                level >= 20 -> 17
                level >= 15 -> 12
                level >= 10 -> 7
                level >= 5 -> 3
                level >= 1 -> 1
                else -> 0
            }.toInt()
        }
        return copy(festivalBonus = bonus)
    }

    fun applyHeatUpFrom(oldStatus: UafStatus): UafStatus {
        return copy(
            heatUp = UafGenre.entries.associateWith { checkHeatUp(oldStatus, it) },
        )
    }

    private fun checkHeatUp(oldStatus: UafStatus, genre: UafGenre): Int {
        val oldRank = (oldStatus.genreLevel[genre] ?: 0) % 50
        val newRank = (genreLevel[genre] ?: 0) % 50
        return if (newRank > oldRank) 2 else heatUp[genre] ?: 0
    }
}

object UafMemberState : ScenarioMemberState {
    override fun toString() = "UAF"
}

enum class UafGenre(
    val displayName: String,
    val colorName: String,
) {
    Blue("スフィア", "青"),
    Red("ファイト", "赤"),
    Yellow("フリー", "黄");

    val longDisplayName by lazy { "$displayName($colorName)" }
}

enum class UafAthletic(
    val displayName: String,
    val genre: UafGenre,
    val type: StatusType,
) {
    BlueSpeed("マシンガンレシーブ", UafGenre.Blue, StatusType.SPEED),
    BlueStamina("ヘルスイムシュート", UafGenre.Blue, StatusType.STAMINA),
    BluePower("マウンテンダンク", UafGenre.Blue, StatusType.POWER),
    BlueGuts("ムゲンリフティング", UafGenre.Blue, StatusType.GUTS),
    BlueWisdom("スナイプボール", UafGenre.Blue, StatusType.WISDOM),
    RedSpeed("ゴッドスピードカラテ", UafGenre.Red, StatusType.SPEED),
    RedStamina("プッシュザロック", UafGenre.Red, StatusType.STAMINA),
    RedPower("ハリテバイル", UafGenre.Red, StatusType.POWER),
    RedGuts("ギガンティックスロー", UafGenre.Red, StatusType.GUTS),
    RedWisdom("ソニックフェンシング", UafGenre.Red, StatusType.WISDOM),
    YellowSpeed("ハイパージャンプ", UafGenre.Yellow, StatusType.SPEED),
    YellowStamina("ハングクライム", UafGenre.Yellow, StatusType.STAMINA),
    YellowPower("ダイナミックハンマー", UafGenre.Yellow, StatusType.POWER),
    YellowGuts("ライクアサブマリン", UafGenre.Yellow, StatusType.GUTS),
    YellowWisdom("アクロバットアロー", UafGenre.Yellow, StatusType.WISDOM);

    val longDisplayName by lazy { "$displayName(${genre.colorName}${type.displayName})" }

    companion object {
        val byStatusType = entries.groupBy { it.type }
    }
}

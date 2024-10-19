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
package io.github.mee1080.umasim.scenario.uaf

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.ScenarioMemberState
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import kotlin.math.max
import kotlin.math.min

data class UafStatus(
    val athleticsLevel: Map<UafAthletic, Int> = UafAthletic.entries.associateWith { 1 },
    val trainingAthletics: Map<StatusType, UafAthletic> = emptyMap(),
    val heatUp: Map<UafGenre, Int> = UafGenre.entries.associateWith { 0 },
    val festivalWinCount: Map<UafGenre, Int> = UafGenre.entries.associateWith { 0 },
    val festivalBonus: Int = 0,
    val athleticsLevelUp: Map<StatusType, Int> = trainingType.associateWith { 3 },
    val levelUpBonus: Boolean = false,
    val consultCount: Int = 3,
) : ScenarioStatus {
    companion object {
        private val trainingData = Scenario.UAF.trainingData
            .groupBy { it.type }
            .mapValues { it.value.associateBy { it.level } }
    }

    fun toShortString() = buildString {
        append("UafStatus(festivalBonus=")
        append(festivalBonus)
        append(", consultCount=")
        append(consultCount)
        append(", levelUpBonus=")
        append(levelUpBonus)
        append(", athleticsLevel=")
        append(UafAthletic.entries.joinToString("/") { athleticsLevel[it].toString() })
        append(", heatUp=")
        append(UafGenre.entries.joinToString("/") { heatUp[it].toString() })
    }

    val genreLevel by lazy {
        athleticsLevel.entries.groupBy {
            it.key.genre
        }.mapValues { (_, values) ->
            values.sumOf { it.value }
        }
    }

    val forceHint by lazy { heatUp[UafGenre.Yellow]!! > 0 }

    fun trainingLevel(athletic: UafAthletic) = min(5, max(1, athleticsLevel[athletic]!! / 10))

    val totalTrainingLevel by lazy { trainingAthletics.values.sumOf { trainingLevel(it) } }

    fun getTraining(statusType: StatusType, levelUpTurn: Boolean): TrainingBase {
        val athletic = trainingAthletics[statusType]!!
        val level = if (levelUpTurn) 5 else trainingLevel(athletic)
        return trainingData[statusType]!![(athletic.genre.ordinal + 1) * 10 + level]!!
    }

    fun randomizeTraining(): UafStatus {
        return copy(
            trainingAthletics = trainingType.associateWith {
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
            },
            consultCount = consultCount - 1,
        )
    }

    fun applyHeatUpFrom(oldStatus: UafStatus): UafStatus {
        return copy(
            heatUp = UafGenre.entries.associateWith { checkHeatUp(oldStatus, it) },
        )
    }

    private fun checkHeatUp(oldStatus: UafStatus, genre: UafGenre): Int {
        val oldRank = (oldStatus.genreLevel[genre] ?: 0) / 50
        val newRank = (genreLevel[genre] ?: 0) / 50
        return heatUp[genre]!! + if (newRank > oldRank) 2 else 0
    }
}

object UafMemberState : ScenarioMemberState(Scenario.UAF)

enum class UafGenre(
    val displayName: String,
    val colorName: String,
    val colorCode: String,
) {
    Blue("スフィア", "青", "#0000FF20"),
    Red("ファイト", "赤", "#FF000020"),
    Yellow("フリー", "黄", "#FFFF0020");

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
        val byGenre = entries.groupBy { it.genre }.mapValues { list -> list.value.sortedBy { it.type } }
        fun get(genre: UafGenre, type: StatusType) = byGenre[genre]!!.first { it.type == type }
    }
}

fun uafNeedWinCount(turn: Int) = when {
    turn > 72 -> 0
    turn > 60 -> 50
    turn > 48 -> 40
    turn > 36 -> 30
    turn > 24 -> 20
    else -> 10
}

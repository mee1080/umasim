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
package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.climax.MegaphoneItem
import io.github.mee1080.umasim.scenario.climax.WeightItem

object WebConstants {

    val notSelected = -1 to null as SupportCard?

    val displayStatusTypeList = listOf(
        StatusType.NONE,
        StatusType.SPEED,
        StatusType.STAMINA,
        StatusType.POWER,
        StatusType.GUTS,
        StatusType.WISDOM,
        StatusType.FRIEND,
        StatusType.GROUP,
    )

    val scenarioList = Scenario.entries.reversed()

    val charaList =
        listOf(Chara.empty()) + Store.charaList.filter { it.rank == 5 && it.rarity == 5 }.sortedBy { it.charaName }

    val supportMap = Store.supportList.groupBy { it.id }

    val displaySupportList = listOf(notSelected) + supportMap.entries
        .map { it.key to it.value.first { card -> card.talent == 4 } }
        .sortedBy { it.second.type.ordinal * 10000000 - it.second.rarity * 1000000 + it.first }

    fun getSupportList(type: StatusType) = displaySupportList.filter { it.second?.type == type }

    fun getRarityText(card: SupportCard) = when (card.rarity) {
        1 -> "R"
        2 -> "SR"
        3 -> "SSR"
        else -> "?"
    }

    val supportTalentList = listOf(0, 1, 2, 3, 4).map { it to it.toString() }

    class SortOrder<T : Comparable<T>>(
        val label: String,
        val descending: Boolean = true,
        val noInfo: Boolean = false,
        val value: SupportCard.() -> T,
    ) : Comparator<Pair<Int, SupportCard?>> {
        fun toInfo(card: SupportCard?) = if (noInfo || card == null) "" else " (${card.value()})"
        override fun compare(a: Pair<Int, SupportCard?>, b: Pair<Int, SupportCard?>): Int {
            val cardA = a.second
            val cardB = b.second
            return if (cardA == null || cardB == null) {
                if (cardA != null) 1
                else if (cardB != null) -1
                else 0
            } else if (descending) {
                cardB.value().compareTo(cardA.value())
            } else {
                cardA.value().compareTo(cardB.value())
            }
        }

        override fun equals(other: Any?): Boolean {
            return label == (other as? SortOrder<*>)?.label
        }

        override fun hashCode(): Int {
            return label.hashCode()
        }
    }

    private val SupportCard.noSpecialUniqueCondition
        get() = SpecialUniqueCondition(
            type,
            1,
            5,
            0,
            emptyMap(),
            0,
            Status(maxHp = 100, hp = 100),
            0,
            1,
            0,
            0,
            0,
            false,
            0,
        )

    private val SupportCard.withSpecialUniqueCondition
        get() = SpecialUniqueCondition(
            StatusType.NONE,
            5,
            20,
            100,
            (listOf(StatusType.FRIEND) + trainingType).associateWith { 2 },
            1000000,
            Status(maxHp = 120, hp = 30),
            600,
            5,
            10,
            10,
            10,
            true,
            10,
        )

    val supportSortOrder = listOf(
        SortOrder("デフォルト", descending = false, noInfo = true) { type.ordinal * 10000000 - rarity * 1000000 + id },
        SortOrder("ID（ほぼ新しい順）", noInfo = true) { id },
        SortOrder("名前", descending = false, noInfo = true) { name },
        SortOrder("キャラ名", descending = false, noInfo = true) { chara },
        SortOrder("初期絆") { initialRelation },
        // TODO 特殊固有対応
        SortOrder("初期ステ合計") { initialStatus(emptyList()).statusTotal },
        SortOrder("友情ボナ") { friendFactor(noSpecialUniqueCondition) },
        SortOrder("友情ボナ（特殊固有）") { friendFactor(withSpecialUniqueCondition) },
        SortOrder("やる気ボナ") { motivationFactor(noSpecialUniqueCondition) },
        SortOrder("やる気ボナ（特殊固有）") { motivationFactor(withSpecialUniqueCondition) },
        SortOrder("トレ効果") { trainingFactor(noSpecialUniqueCondition) },
        SortOrder("トレ効果（特殊固有）") { trainingFactor(withSpecialUniqueCondition) },
        SortOrder("スピボ") { getBaseBonus(StatusType.SPEED, noSpecialUniqueCondition) },
        SortOrder("スピボ（特殊固有）") { getBaseBonus(StatusType.SPEED, withSpecialUniqueCondition) },
        SortOrder("スタボ") { getBaseBonus(StatusType.STAMINA, noSpecialUniqueCondition) },
        SortOrder("スタボ（特殊固有）") { getBaseBonus(StatusType.STAMINA, withSpecialUniqueCondition) },
        SortOrder("パワボ") { getBaseBonus(StatusType.POWER, noSpecialUniqueCondition) },
        SortOrder("パワボ（特殊固有）") { getBaseBonus(StatusType.POWER, withSpecialUniqueCondition) },
        SortOrder("根性ボ") { getBaseBonus(StatusType.GUTS, noSpecialUniqueCondition) },
        SortOrder("根性ボ（特殊固有）") { getBaseBonus(StatusType.GUTS, withSpecialUniqueCondition) },
        SortOrder("賢さボ") { getBaseBonus(StatusType.WISDOM, noSpecialUniqueCondition) },
        SortOrder("賢さボ（特殊固有）") { getBaseBonus(StatusType.WISDOM, withSpecialUniqueCondition) },
        SortOrder("スキボ") { getBaseBonus(StatusType.SKILL, noSpecialUniqueCondition) },
        SortOrder("スキボ（特殊固有）") { getBaseBonus(StatusType.SKILL, withSpecialUniqueCondition) },
        SortOrder("レスボ") { race },
        SortOrder("ファンボ") { fan },
        SortOrder("得意率") { specialtyRate(0, noSpecialUniqueCondition) / 100.0 },
        SortOrder("得意率（特殊固有）") { specialtyRate(0, withSpecialUniqueCondition) / 100.0 },
        SortOrder("ヒントLv") { hintLevel },
        SortOrder("ヒント率") { hintFrequency },
        SortOrder("賢さ友情回復") { wisdomFriendRecovery(noSpecialUniqueCondition) },
        SortOrder("賢さ友情回復（特殊固有）") { wisdomFriendRecovery(withSpecialUniqueCondition) },
    )

    val trainingTypeList = trainingType.toList()

    val motivationMap = mapOf(2 to "絶好調", 1 to "好調", 0 to "普通", -1 to "不調", -2 to "絶不調")

    val trainingInfo = Scenario.entries.associateWith { Store.getTrainingInfo(it) }

    val trainingList = Scenario.entries.associateWith { it.trainingData }

    val simulationModeList = mapOf(
        Scenario.URA to listOf(
            "スピ3パワ3" to { FactorBasedActionSelector2.speedPower.generateSelector() },
            "スピ3パワ3中距離" to { FactorBasedActionSelector2.speedPowerMiddle.generateSelector() },
            "スピ3賢3" to { FactorBasedActionSelector2.speedWisdom.generateSelector() },
            "スピ3スタ3" to { FactorBasedActionSelector2.speedStamina.generateSelector() },
            "パワ3賢3" to { FactorBasedActionSelector2.aoharuPowerWisdom.generateSelector() },
            "スピ2パワ3賢1" to { FactorBasedActionSelector2.speed2Power3Wisdom1.generateSelector() },
        ),
        Scenario.AOHARU to listOf(
            "スピ2パワ3賢1" to { FactorBasedActionSelector2.aoharuSpeed2Power3Wisdom1.generateSelector() },
            "スピ2賢2代理ライス" to { FactorBasedActionSelector2.aoharuSpeed2Power1Wisdom2Friend1Optuna3.generateSelector() },
            "スピ2スタ1賢3" to { FactorBasedActionSelector2.aoharuSpeed2Stamina1Wisdom3.generateSelector() },
            "スピ1スタ1賢1代理デジタル" to { FactorBasedActionSelector2.aoharuSpeed2Stamina1Power1Wisdom1Friend1Optuna.generateSelector() },
        ),
        Scenario.CLIMAX to listOf(),
        Scenario.GRAND_LIVE to listOf(),
        Scenario.GM to listOf(),
    )

    val displaySimulationModeList =
        simulationModeList.mapValues { it.value.mapIndexed { index, pair -> index to pair.first } }

    val dummyMegaphoneItem = MegaphoneItem("なし", 0, 0, 0)

    val shopItemMegaphone = listOf(dummyMegaphoneItem) + Store.Climax.shopItem.filterIsInstance<MegaphoneItem>()

    val dummyWeightItem = WeightItem("なし", 0, 0, 0, StatusType.NONE)

    val shopItemWeight = listOf(dummyWeightItem) + Store.Climax.shopItem.filterIsInstance<WeightItem>()

    val shopItemWeightNames = listOf(-1 to "なし") + shopItemWeight.mapIndexed { index, item -> index to item.name }

    val raceItem = mapOf(
        "蹄鉄ハンマー・匠" to 1.2,
        "蹄鉄ハンマー・極" to 1.35,
    )

    val uafFestivalBonusValue = mapOf(
        0 to "0",
        1 to "1～4",
        3 to "5～9",
        7 to "10～14",
        12 to "15～19",
        17 to "20～25",
    )

    val uafFestivalBonus = listOf(0, 1, 3, 7, 12, 17)

    val cookCookPoint = listOf(0, 500, 1500, 2500, 5000, 7000, 10000, 12000)

    val cookPhase = mapOf(
        -1 to "なし", 0 to "ジュニア料理", 1 to "クラシック料理", 2 to "シニア料理", 3 to "GIプレート",
    )

    val cookResult1 = mapOf(0 to "すべて満足", 1 to "12月大満足", 2 to "6月大満足")

    val cookResult2 = mapOf(0 to "満足", 1 to "大満足", 2 to "超満足")
}

fun SupportCard?.displayName(): String {
    return if (this == null) {
        "未選択"
    } else {
        WebConstants.getRarityText(this) + " " + name
    }
}
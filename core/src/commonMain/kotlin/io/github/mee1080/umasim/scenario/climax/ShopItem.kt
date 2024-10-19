package io.github.mee1080.umasim.scenario.climax

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType

sealed interface ShopItem {
    val name: String
    val coin: Int
}

data class StatusItem(
    override val name: String,
    override val coin: Int,
    val status: Status,
) : ShopItem

data class UniqueItem(
    override val name: String,
    override val coin: Int,
) : ShopItem

data class AddConditionItem(
    override val name: String,
    override val coin: Int,
    val condition: String,
) : ShopItem

data class RemoveConditionItem(
    override val name: String,
    override val coin: Int,
    val condition: List<String>,
) : ShopItem

data class TrainingLevelItem(
    override val name: String,
    override val coin: Int,
    val type: StatusType,
) : ShopItem

data class MegaphoneItem(
    override val name: String,
    override val coin: Int,
    val trainingFactor: Int,
    val turn: Int,
) : ShopItem

data class WeightItem(
    override val name: String,
    override val coin: Int,
    val trainingFactor: Int,
    val hpFactor: Int,
    val type: StatusType,
) : ShopItem

data class RaceBonusItem(
    override val name: String,
    override val coin: Int,
    val raceFactor: Int,
) : ShopItem

data class FanBonusItem(
    override val name: String,
    override val coin: Int,
    val fanFactor: Int,
) : ShopItem
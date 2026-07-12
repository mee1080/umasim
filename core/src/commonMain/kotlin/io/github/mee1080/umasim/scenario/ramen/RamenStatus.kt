package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.simulation2.RamenActionParam
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState
import kotlin.math.min

fun SimulationState.updateRamenStatus(update: RamenStatus.() -> RamenStatus): SimulationState {
    val ramenStatus = this.ramenStatus ?: return this
    return copy(scenarioStatus = ramenStatus.update())
}

/**
 * Ramenシナリオ固有の状態を保持するクラス。
 */
data class RamenStatus(
    val turn: Int = 1,
    val selectedRegions: List<RamenRegion> = emptyList(),
    val baseGauge: RamenActionParam = RamenActionParam(),
    val gauges: Map<RamenTipType, Int> = mapOf(
        RamenTipType.NOODLE to 0,
        RamenTipType.SOUP to 0,
        RamenTipType.TOPPING to 0
    ),
    val tips: Map<RamenTipType, Int> = mapOf(
        RamenTipType.NOODLE to 0,
        RamenTipType.SOUP to 0,
        RamenTipType.TOPPING to 0,
    ),
    val hiddenTips: Int = 0,
    val tipHistory: List<RamenTipType> = emptyList(),
    val excitementPt: Int = 0,
    val activeTastingRegion: Pair<RamenRegion, Int>? = null,
    val trainingTip: Map<StatusType, RamenTipType> = emptyMap(),
    val rmjBonus: RamenBaseBonus = RamenBaseBonus(0, 0, 0, 0),
) : ScenarioStatus {

    override fun toShortString() = buildString {
        append("地域選択: ${selectedRegions.joinToString(",") { it.regionName }}")
        append("(${baseGauge.noodleGauge}/${baseGauge.soupGauge}/${baseGauge.toppingGauge})")
        append(", ゲージ: ${gauges.values.joinToString("/")}")
        append(", コツ: ${tips.values.joinToString("/")}")
        append(", 隠し味: $hiddenTips")
        append(", 盛り上がりPt: $excitementPt")
        append(", 試食会: ${activeTastingRegion?.first?.regionName ?: "なし"}")
    }

    val period = (turn - 1) / 24

    val baseEffect = if (activeTastingRegion == null) RamenBaseEffect.Empty else {
        ramenBaseEffect.getOrElse(period) { RamenBaseEffect.Empty }
    }

    val excitePtBonus by lazy { ramenExcitePtBonus(excitementPt) }

    val targetExcitePt = ramenTargetExcitePt[period]

    val regionRank = min(5, excitementPt * 5 / targetExcitePt)

    val regionRankBonus = ramenRegionRankBonus[regionRank]

    fun shuffleTrainingTip(): RamenStatus {
        val tipList = listOf(
            RamenTipType.NOODLE, RamenTipType.NOODLE,
            RamenTipType.SOUP, RamenTipType.SOUP,
            RamenTipType.TOPPING, RamenTipType.TOPPING,
        ).shuffled()
        val newTrainingTip = trainingType.indices.associate {
            trainingType[it] to tipList[it]
        }
        return copy(trainingTip = newTrainingTip)
    }

    fun clearOnYearEnd(): RamenStatus {
        return copy(
            selectedRegions = emptyList(),
            gauges = gauges.map { it.key to 0 }.toMap(),
            excitementPt = 0,
        )
    }

    fun addRegion(region: RamenRegion): RamenStatus {
        if (selectedRegions.size >= 3) {
            return copy(selectedRegions = listOf(region))
        }
        val newRegions = selectedRegions + region
        if (newRegions.size < 3) {
            return copy(selectedRegions = newRegions)
        }
        val newBaseGauge = ramenGaugeBase(newRegions)
        return copy(selectedRegions = newRegions, baseGauge = newBaseGauge)
    }

    fun addTip(type: RamenTipType): RamenStatus {
        val newTips = tips.toMutableMap()
        newTips[type] = (newTips[type]!!) + 1
        val newHistory = tipHistory + type
        if (newHistory.size <= 10) {
            return copy(tips = newTips, tipHistory = newHistory)
        }
        val first = newHistory.first()
        newTips[first] = (newTips[first]!!) - 1
        return copy(tips = newTips, tipHistory = newHistory.drop(1))
    }

    private fun removeTipOrHidden(type: RamenTipType): RamenStatus {
        if (tips[type]!! > 0) {
            val newTips = tips.toMutableMap()
            newTips[type] = (newTips[type]!!) - 1
            val newHistory = tipHistory - type
            return copy(tips = newTips, tipHistory = newHistory)
        } else {
            return addHiddenTips(-1)
        }
    }

    fun addGauge(type: RamenTipType, value: Int): RamenStatus {
        if (value <= 0) return this
        val newGauges = gauges.toMutableMap()
        newGauges[type] = (newGauges[type]!!) + value
        if (newGauges[type]!! < 7) {
            return copy(gauges = newGauges)
        }
        newGauges[type] = 0
        return copy(gauges = newGauges).addTip(type)
    }

    fun addGauges(noodle: Int = 0, soup: Int = 0, topping: Int = 0): RamenStatus {
        return addGauge(RamenTipType.NOODLE, noodle)
            .addGauge(RamenTipType.SOUP, soup)
            .addGauge(RamenTipType.TOPPING, topping)
    }

    fun addHiddenTips(amount: Int): RamenStatus {
        return copy(hiddenTips = min(4, hiddenTips + amount))
    }

    fun activateTasting(region: RamenRegion): RamenStatus {
        var state = this
        repeat(region.noodle) {
            state = state.removeTipOrHidden(RamenTipType.NOODLE)
        }
        repeat(region.soup) {
            state = state.removeTipOrHidden(RamenTipType.SOUP)
        }
        repeat(region.topping) {
            state = state.removeTipOrHidden(RamenTipType.TOPPING)
        }
        val gainPt = ramenGainExcitePt[period]
        val tastingCount = min(5, excitementPt / gainPt / 10)
        return state.copy(
            activeTastingRegion = region to state.regionRankBonus,
            excitementPt = state.excitementPt + gainPt * (10 + tastingCount),
        )
    }
}

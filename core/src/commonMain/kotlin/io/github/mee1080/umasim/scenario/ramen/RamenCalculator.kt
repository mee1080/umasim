package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*

object RamenCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean
    ): Status {
        val ramenStatus = info.ramenStatus ?: return Status()

        var bonusRate = 0

        // 試食会の基礎効果 (トレ効果15)
        if (ramenStatus.activeTastingRegion != null) {
            bonusRate += 15
        }

        // 盛り上がりPtによるボーナス (暫定: 1000ptごとにトレ効果1%?)
        val excitementBonus = (ramenStatus.excitementPt / 1000).coerceAtMost(10)
        bonusRate += excitementBonus

        var bonus = Status()
        if (bonusRate > 0) {
            bonus = base.multiplyToInt(bonusRate)
        }

        // 地域ごとの固有効果
        ramenStatus.activeTastingRegion?.let { region ->
            when (region) {
                RamenStore.Region.SAPPORO -> {
                    // スピトレ効果+20 (トレーニングタイプがスピードの場合のみ適用されるべきだが、
                    // 現状のcalcScenarioStatusの引数からは判定が難しいためTODO)
                }
                RamenStore.Region.NIIGATA -> {
                    // パワートレ効果+20
                }
                else -> {}
            }
        }

        return bonus
    }

    override fun getScenarioCalcBonus(baseInfo: Calculator.CalcInfo) = null

    fun applyScenarioAction(state: SimulationState, result: ActionResult): SimulationState {
        return when (result) {
            is RamenSelectRegionResult -> {
                state.updateRamenStatus {
                    copy(
                        selectedRegions = (selectedRegions + result.regions).distinct(),
                        // 地域選択後に全コツ+1（新たづな編成時は+2。暫定+1）
                        tips = tips.mapValues { it.value + 1 }
                    )
                }
            }
            is RamenTastingResult -> {
                val cost = result.region.tipCost
                state.updateRamenStatus {
                    val newTips = tips.toMutableMap()

                    fun consume(type: RamenStore.TipType, amount: Int) {
                        var remaining = amount
                        // まず通常のコツを消費
                        val current = newTips[type] ?: 0
                        val consumeFromNormal = minOf(current, remaining)
                        newTips[type] = current - consumeFromNormal
                        remaining -= consumeFromNormal

                        // 足りない分を隠し味から消費
                        if (remaining > 0) {
                            val currentHidden = newTips[RamenStore.TipType.HIDDEN] ?: 0
                            newTips[RamenStore.TipType.HIDDEN] = (currentHidden - remaining).coerceAtLeast(0)
                        }
                    }

                    consume(RamenStore.TipType.NOODLE, cost.noodle)
                    consume(RamenStore.TipType.SOUP, cost.soup)
                    consume(RamenStore.TipType.TOPPING, cost.topping)

                    copy(
                        tips = newTips,
                        activeTastingRegion = result.region,
                        excitementPt = excitementPt + 300 // 盛り上がりPt増加 (300Pt固定？)
                    )
                }
            }
            else -> state
        }
    }
}

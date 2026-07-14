package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.simulation2.*

class RamenScenarioEvents : BaseScenarioEvents() {

    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val current = when (state.turn) {
            // 初回地域選択
            3 -> state.copy(scenarioStatus = RamenStatus()).selectRamenRegion(selector, 0)

            // 夏合宿開始
            37, 61 -> state.addStatus(Status(hp = 15, motivation = 1)).updateRamenStatus {
                addHiddenTips(2)
            }

            // 夏合宿中
            38, 39, 40, 62, 63, 64 -> state.addStatus(Status(hp = 10)).updateRamenStatus {
                addHiddenTips(1)
            }

            else -> state
        }.updateRamenStatus { shuffleTrainingTip() }

        return current
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        var base = super.afterAction(state, selector)

        // ターン終了時に試食会効果をクリア
        if (state.turn <= 72) {
            base = base.updateRamenStatus { copy(activeTastingRegion = null) }
        }

        // RMJイベント
        base = when (base.turn) {
            24, 48, 72 -> {
                val period = (base.turn - 1) / 24
                base
                    .applyRmj(period)
                    .selectRamenRegion(selector, period + 1)
            }

            else -> base
        }

        return base
    }

    private fun SimulationState.applyRmj(period: Int): SimulationState {
        val ramenStatus = ramenStatus ?: return this
        val target = ramenStatus.targetExcitePt
        val success = ramenStatus.excitementPt >= target
        val hpPlus = when (ramenStatus.tips.values.sum()) {
            // TODO コツ溢れ回復量調査中
            0 -> 0
            1, 2 -> 3
            3, 4 -> 5
            else -> 10
        }
        val eventResult = when (period) {
            0 -> if (success) {
                addStatus(
                    Status(
                        speed = 10, stamina = 10, power = 10, guts = 10, wisdom = 10,
                        skillPt = 100, hp = 30 + hpPlus, fanCount = 3000,
                    )
                ).allTrainingLevelUp()
            } else {
                addStatus(
                    Status(
                        speed = 5, stamina = 5, power = 5, guts = 5, wisdom = 5,
                        skillPt = 50, hp = 30 + hpPlus, fanCount = 500,
                    )
                )
            }

            1 -> if (success) {
                addStatus(
                    Status(
                        speed = 15, stamina = 15, power = 15, guts = 15, wisdom = 15,
                        skillPt = 150, hp = 40 + hpPlus, fanCount = 18000, skillHint = mapOf("時中の妙" to 1),
                    )
                ).allTrainingLevelUp()
            } else {
                addStatus(
                    Status(
                        speed = 10, stamina = 10, power = 10, guts = 10, wisdom = 10,
                        skillPt = 75, hp = 30 + hpPlus, fanCount = 2000,
                    )
                )
            }

            else -> if (success) {
                addStatus(
                    Status(
                        speed = 30, stamina = 30, power = 30, guts = 30, wisdom = 30,
                        skillPt = 250, hp = 30 + hpPlus, fanCount = 45000,
                        skillHint = mapOf("ペースキープ" to 2, "深呼吸" to 2, "恩返し、召し上がれ" to 3),
                    )
                ).allTrainingLevelUp()
            } else {
                addStatus(
                    Status(
                        speed = 15, stamina = 15, power = 15, guts = 15, wisdom = 15,
                        skillPt = 150, hp = 30 + hpPlus, fanCount = 5000,
                    )
                )
            }
        }
        val rmjBonus = ramenRmjBonus[period][if (success) 1 else 0]
        return eventResult.updateRamenStatus {
            copy(rmjBonus = rmjBonus)
        }
    }

    private suspend fun SimulationState.selectRamenRegion(
        selector: ActionSelector,
        period: Int
    ): SimulationState {
        val availableRegions = ramenRegionSelection[period]
        var state = this.updateRamenStatus {
            clearOnYearEnd()
        }
        if (period == 3) {
            // RMJ極
            val action = selector.select(state, availableRegions.map { RamenSelectRegion(it) }) as RamenSelectRegion
            return RamenCalculator.applyScenarioAction(state, action.result)
        }
        val selected = mutableListOf<RamenRegion>()
        repeat(3) {
            val remain = availableRegions.filter { !selected.contains(it) }
            if (remain.isNotEmpty()) {
                val action = selector.select(state, remain.map { RamenSelectRegion(it) })
                val region = (action as RamenSelectRegion).region
                selected.add(region)
                state = RamenCalculator.applyScenarioAction(state, action.result)
            }
        }
        // コツ全て+2（たづな前提）
        state = state.updateRamenStatus {
            copy(
                tips = tips.mapValues { 2 },
                tipHistory = listOf(
                    RamenTipType.NOODLE, RamenTipType.SOUP,
                    RamenTipType.TOPPING, RamenTipType.NOODLE,
                    RamenTipType.SOUP, RamenTipType.TOPPING,
                ),
            ).addHiddenTips(2)
        }
        return state
    }
}

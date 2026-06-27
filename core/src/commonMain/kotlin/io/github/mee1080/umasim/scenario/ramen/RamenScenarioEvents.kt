package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.simulation2.ActionSelector
import io.github.mee1080.umasim.simulation2.RamenSelectRegion
import io.github.mee1080.umasim.simulation2.SimulationState
import io.github.mee1080.umasim.simulation2.addAllStatus

class RamenScenarioEvents : BaseScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        return super.beforeSimulation(state).copy(
            scenarioStatus = RamenStatus()
        )
    }

    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = state.updateRamenStatus { shuffleTrainingTip() }
        val current = when (base.turn) {
            1 -> base.selectRamenRegion(selector, 0)

            37, 38, 39, 40, 61, 62, 63, 64 -> base.updateRamenStatus {
                // TODO 夏合宿習得ゲージ処理
                copy()
            }

            else -> base
        }

        return current
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        var base = super.afterAction(state, selector)

        // ターン終了時に試食会効果をクリア
        base = base.updateRamenStatus { copy(activeTastingRegion = null) }

        // RMJイベント
        base = when (base.turn) {
            24, 48, 72 -> {
                val period = base.turn / 24 + 1
                // TODO: 盛り上がりPtに応じた報酬
                base
                    .applyRmj(period)
                    .selectRamenRegion(selector, period)
            }

            else -> base
        }

        // 12月後半終了時にコツが全て消える
        if (base.turn % 24 == 0) {
            base = base.updateRamenStatus {
                clearOnYearEnd()
            }
        }

        return base
    }

    private fun SimulationState.applyRmj(period: Int): SimulationState {
        // TODO ラーメン・ジャンボリー
        return addAllStatus(status = 10, skillPt = 30)
    }

    private suspend fun SimulationState.selectRamenRegion(
        selector: ActionSelector,
        period: Int
    ): SimulationState {
        val availableRegions = ramenRegionSelection[period]
        var state = this
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
        // TODO 順番調査
        state = state.updateRamenStatus {
            copy(
                tips = tips.mapValues { 2 },
                tipHistory = listOf(
                    RamenTipType.NOODLE, RamenTipType.NOODLE,
                    RamenTipType.SOUP, RamenTipType.SOUP,
                    RamenTipType.TOPPING, RamenTipType.TOPPING,
                ),
            )
        }
        return state
    }
}

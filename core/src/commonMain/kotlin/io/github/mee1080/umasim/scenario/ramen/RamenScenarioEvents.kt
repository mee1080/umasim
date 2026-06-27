package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.simulation2.*

class RamenScenarioEvents : BaseScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        return super.beforeSimulation(state).copy(
            scenarioStatus = RamenStatus()
        )
    }

    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.beforeAction(state, selector)
        var current = when (base.turn) {
            1 -> base.selectRamenRegion(selector, 1)
            25 -> base.selectRamenRegion(selector, 2)
            49 -> base.selectRamenRegion(selector, 3)
            else -> base
        }

        // 試食会は毎ターンTraining等と並んで選択可能にする
        current = current.offerTasting(selector)

        return current
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        var base = super.afterAction(state, selector)

        // ターン終了時に試食会効果をクリア
        base = base.updateRamenStatus { copy(activeTastingRegion = null) }

        // アクションに応じたゲージ獲得
        val action = base.lastAction

        base = when (action) {
            is Training -> {
                // 基本値: 新たづな+10、旧たづなハロー+5、なし:+3
                // 暫定+3。トレーニング配置コツ+2。
                val basic = 3
                val plus = if (action.member.any { it.hint }) 2 else 0
                val total = basic + plus

                // 地域によって割り振りが変化するが、暫定で全ゲージに加算
                base.updateRamenStatus { addGauges(total, total, total) }
            }
            is Race, is Sleep, is Outing -> {
                val basic = 3
                base.updateRamenStatus { addGauges(basic, basic, basic) }
            }
            else -> base
        }

        // RMJイベント
        base = when (base.turn) {
            24, 48, 72 -> {
                // TODO: 盛り上がりPtに応じた報酬
                base.addAllStatus(status = 10, skillPt = 30)
            }
            else -> base
        }

        // 12月後半終了時にコツが全て消える
        if (base.turn % 24 == 0) {
            base = base.updateRamenStatus {
                copy(tips = tips.mapValues { if (it.key == RamenStore.TipType.HIDDEN) it.value else 0 })
            }
        }

        return base
    }

    private suspend fun SimulationState.selectRamenRegion(
        selector: ActionSelector,
        period: Int
    ): SimulationState {
        val availableRegions = RamenStore.Region.entries.filter { it.periods.contains(period) }
        if (availableRegions.isEmpty()) return this

        // 3つ選択する。本来は1つずつ選ぶUIかもしれないが、一括で選択肢を出す
        // 暫定的に固定の組み合わせをいくつか提示するか、単一選択にするか。
        // メモには「地域を3つ選択」とあるため、3つの地域を順に選ばせる
        var state = this
        val selected = mutableListOf<RamenStore.Region>()
        repeat(3) {
            val remain = availableRegions.filter { !selected.contains(it) }
            if (remain.isNotEmpty()) {
                val action = selector.select(state, remain.map { RamenSelectRegion(listOf(it)) })
                val region = (action as RamenSelectRegion).regions.first()
                selected.add(region)
                state = io.github.mee1080.umasim.scenario.ramen.RamenCalculator.applyScenarioAction(state, action.result)
            }
        }
        return state
    }

    private suspend fun SimulationState.offerTasting(
        selector: ActionSelector
    ): SimulationState {
        val status = ramenStatus ?: return this
        val availableTasting = status.selectedRegions.filter { region ->
            val cost = region.tipCost
            val tips = status.tips
            val hidden = tips[RamenStore.TipType.HIDDEN] ?: 0

            fun canAfford(type: RamenStore.TipType, amount: Int): Int {
                return (amount - (tips[type] ?: 0)).coerceAtLeast(0)
            }

            val neededHidden = canAfford(RamenStore.TipType.NOODLE, cost.noodle) +
                               canAfford(RamenStore.TipType.SOUP, cost.soup) +
                               canAfford(RamenStore.TipType.TOPPING, cost.topping)

            hidden >= neededHidden
        }

        if (availableTasting.isEmpty()) return this

        // 試食会を行うかどうかの選択。
        // 実際にはトレーニング選択肢の中に混ぜる必要があるが、
        // ここではbeforeAction内で「試食会を行う」というActionを提示し、
        // 選択されたら適用してループする形にする
        var state = this
        while (true) {
            val tastingActions = availableTasting.map { RamenTasting(it) }
            val noneAction = NoAction // 暫定的にNoActionを終了条件とする

            val selected = selector.select(state, tastingActions + noneAction)
            if (selected is RamenTasting) {
                state = io.github.mee1080.umasim.scenario.ramen.RamenCalculator.applyScenarioAction(state, selected.result)
                // 1ターンに複数回できるかは不明だが、一旦1回で終了とする
                break
            } else {
                break
            }
        }

        return state
    }
}

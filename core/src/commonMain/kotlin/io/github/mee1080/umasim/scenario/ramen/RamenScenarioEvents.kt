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
                val period = base.turn / 24
                // TODO: 盛り上がりPtに応じた報酬
                base
                    .applyRmj(period)
                    .selectRamenRegion(selector, period + 1)
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
        val ramenStatus = ramenStatus ?: return this
        val target = ramenTargetExcitePt[period]
        val success = ramenStatus.excitementPt >= target
        val eventResult = when (period) {
            0 -> if (success) {
                addStatus(
                    Status(
                        speed = 10, stamina = 10, power = 10, guts = 10, wisdom = 10,
                        skillPt = 100, hp = 33, fanCount = 3000,
                    )
                ).allTrainingLevelUp()
            } else {
                addStatus(
                    Status(
                        speed = 5, stamina = 5, power = 5, guts = 5, wisdom = 5,
                        skillPt = 50, hp = 30, fanCount = 500,
                    )
                )
            }

            1 -> if (success) {
                addStatus(
                    Status(
                        speed = 15, stamina = 15, power = 15, guts = 15, wisdom = 15,
                        skillPt = 150, hp = 40, fanCount = 18000, skillHint = mapOf("時中の妙" to 1),
                    )
                ).allTrainingLevelUp()
            } else {
                addStatus(
                    Status(
                        speed = 10, stamina = 10, power = 10, guts = 10, wisdom = 10,
                        skillPt = 75, hp = 30, fanCount = 2000,
                    )
                )
            }

            else -> if (success) {
                addStatus(
                    Status(
                        speed = 30, stamina = 30, power = 30, guts = 30, wisdom = 30,
                        skillPt = 250, hp = 50, fanCount = 45000,
                        // TODO 極ラーメンに伴うスキルは選択時に取得する扱い
                        skillHint = mapOf("ペースキープ" to 2, "深呼吸" to 2, "恩返し、召し上がれ" to 3),
                    )
                ).allTrainingLevelUp()
            } else {
                addStatus(
                    Status(
                        speed = 15, stamina = 15, power = 15, guts = 15, wisdom = 15,
                        skillPt = 150, hp = 30, fanCount = 5000,
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
        if (period == 3) {
            // RMJ極
            val action = selector.select(this, availableRegions.map { RamenSelectRegion(it) }) as RamenSelectRegion
            return RamenCalculator.applyScenarioAction(this, action.result)
        }
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

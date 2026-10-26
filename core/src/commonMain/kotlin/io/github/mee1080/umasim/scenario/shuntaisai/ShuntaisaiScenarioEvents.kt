package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.addGuest
import io.github.mee1080.umasim.simulation2.*

class ShuntaisaiScenarioEvents : BaseScenarioEvents() {

    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.beforeAction(state, selector)
        return when (base.turn) {
            // ゲスト加入、教え伝受
            3 -> base.initShuntaisaiStatus().assignTeachings(selector, 0)

            // TODO 合宿直前の教え付け替え

            else -> base
        }
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {
            // 進捗発表会、教え伝受・転成
            24 -> base.applyPresentation(0).transformTeachings(selector, 1)
            48 -> base.applyPresentation(1).transformTeachings(selector, 2)

            // 午式駿大祭本番
            68 -> base.applyFinalPerformance()

            // TODO シニア9月前半のシナリオリンクイベント

            else -> base
        }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        // TODO 育成終了イベント
        return super.afterSimulation(state)
    }

    private fun SimulationState.initShuntaisaiStatus(): SimulationState {
        val azuma = support.firstOrNull { it.charaName == "吾妻" }
        val maxLevel = if (azuma?.card?.rarity == 3) shuntaisaiAzumaMaxLevel else shuntaisaiDefaultMaxLevel
        return copy(
            scenarioStatus = ShuntaisaiStatus(
                turn = turn,
                maxLevel = maxLevel,
                supportTypeCount = supportTypeCount,
            )
        ).addGuests()
    }

    /**
     * 各演目に編成外の演目担当が1人ずつ加わる
     */
    private fun SimulationState.addGuests(): SimulationState {
        return trainingType.fold(this) { state, type -> state.addGuest(type, Scenario.SHUNTAISAI) }
    }

    private suspend fun SimulationState.assignTeachings(selector: ActionSelector, period: Int): SimulationState {
        var state = this
        ShuntaisaiTeaching.byPeriod(period).forEach { teaching ->
            val shuntaisaiStatus = state.shuntaisaiStatus ?: return state
            val targets = trainingType.filter { !shuntaisaiStatus.teachings.containsKey(it) }
            if (targets.isEmpty()) return@forEach
            val action = selector.select(state, targets.map { ShuntaisaiAssignTeaching(teaching, it) })
            state = ShuntaisaiCalculator.applyScenarioAction(state, (action as ShuntaisaiAssignTeaching).result)
        }
        return state
    }

    /**
     * 新しい教えを伝受し、古い教えを消費して転成
     */
    private suspend fun SimulationState.transformTeachings(selector: ActionSelector, period: Int): SimulationState {
        var state = this
        ShuntaisaiTeaching.byPeriod(period).forEach { teaching ->
            val shuntaisaiStatus = state.shuntaisaiStatus ?: return state
            val targets = shuntaisaiStatus.transformTargets(teaching)
            if (targets.isEmpty()) return@forEach
            val action = selector.select(state, targets.map {
                ShuntaisaiTransformTeaching(teaching, it, shuntaisaiStatus.teachings[it]?.teaching)
            })
            state = ShuntaisaiCalculator.applyScenarioAction(state, (action as ShuntaisaiTransformTeaching).result)
        }
        return state
    }

    /**
     * 進捗発表会
     *
     * TODO 成功時の効果は仮、全トレーニングLv+1・理事長絆・スキルは未反映
     */
    private fun SimulationState.applyPresentation(index: Int): SimulationState {
        val shuntaisaiStatus = shuntaisaiStatus ?: return this
        val result = shuntaisaiStatus.judgePresentation(index)
        val eventStatus = when (index) {
            0 -> when (result) {
                ShuntaisaiPresentationResult.GREAT_SUCCESS -> Status(
                    speed = 5, stamina = 5, power = 5, guts = 5, wisdom = 5,
                    skillPt = 200, fanCount = 1000,
                )

                ShuntaisaiPresentationResult.SUCCESS -> Status(
                    speed = 3, stamina = 3, power = 3, guts = 3, wisdom = 3,
                    skillPt = 100, fanCount = 500,
                )
            }

            else -> when (result) {
                ShuntaisaiPresentationResult.GREAT_SUCCESS -> Status(
                    speed = 10, stamina = 10, power = 10, guts = 10, wisdom = 10,
                    skillPt = 300, fanCount = 20000,
                )

                ShuntaisaiPresentationResult.SUCCESS -> Status(
                    speed = 5, stamina = 5, power = 5, guts = 5, wisdom = 5,
                    skillPt = 150, fanCount = 10000,
                )
            }
        }
        return addStatus(eventStatus).updateShuntaisaiStatus {
            copy(
                presentationResults = presentationResults + result,
                presentationBonus = shuntaisaiPresentationBonus[index][result] ?: presentationBonus,
            )
        }
    }

    /**
     * 午式駿大祭本番
     *
     * TODO 成功時の効果は仮、スキルは未反映
     */
    private fun SimulationState.applyFinalPerformance(): SimulationState {
        val shuntaisaiStatus = shuntaisaiStatus ?: return this
        val result = shuntaisaiStatus.judgePresentation(2)
        val eventStatus = when (result) {
            ShuntaisaiPresentationResult.GREAT_SUCCESS -> Status(
                speed = 15, stamina = 15, power = 15, guts = 15, wisdom = 15,
                skillPt = 400, hp = 30, motivation = 1, fanCount = 45000,
            )

            ShuntaisaiPresentationResult.SUCCESS -> Status(
                speed = 10, stamina = 10, power = 10, guts = 10, wisdom = 10,
                skillPt = 200, hp = 30, motivation = 1, fanCount = 30000,
            )
        }
        return addStatus(eventStatus).updateShuntaisaiStatus {
            copy(
                presentationResults = presentationResults + result,
                presentationBonus = shuntaisaiPresentationBonus[2][result] ?: presentationBonus,
            )
        }
    }
}

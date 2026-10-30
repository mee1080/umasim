package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
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

            // 合宿直前の教え付け替え
            in shuntaisaiCampStartTurns -> base
                .addStatus(Status(hp = 15, motivation = 1))
                .reassignTeachings(selector)

            else -> base
        }
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        // 合宿終了、全演目Lv+1
        if (base.shuntaisaiStatus?.isCampEnd(base.turn) == true) {
            return base.updateShuntaisaiStatus { levelUpAll() }
        }
        return when (base.turn) {
            // 進捗発表会、教え伝受・転成、付け替え
            24 -> base.applyPresentation(0).transformTeachings(selector, 1).reassignTeachings(selector)
            48 -> base.applyPresentation(1).transformTeachings(selector, 2).reassignTeachings(selector)

            // シナリオリンクキャラのイベント
            67 -> base.applyLinkEvent()

            // 午式駿大祭本番
            68 -> base.applyFinalPerformance()

            else -> base
        }
    }

    /**
     * 育成終了イベント
     *
     * 全て大成功以外は全て成功の効果とする
     */
    override fun afterSimulation(state: SimulationState): SimulationState {
        val base = super.afterSimulation(state)
        val shuntaisaiStatus = base.shuntaisaiStatus ?: return base
        val allGreat = shuntaisaiStatus.presentationResults.size == shuntaisaiGreatTargetLevel.size &&
                shuntaisaiStatus.presentationResults.all { it == ShuntaisaiPresentationResult.GREAT_SUCCESS }
        return if (allGreat) {
            base.addAllStatus(30, skillPt = 350, skillHint = mapOf("名にし負う夢" to 2))
        } else {
            base.addAllStatus(15, skillPt = 150, skillHint = mapOf("彼方の呼び声" to 3))
        }
    }

    private fun SimulationState.initShuntaisaiStatus(): SimulationState {
        val azuma = support.firstOrNull { it.charaName == "吾妻" }
        val maxLevel = if (azuma?.card?.rarity == 3) shuntaisaiAzumaMaxLevel else shuntaisaiDefaultMaxLevel
        val performances = assignShuntaisaiPerformances(member)
        return copy(
            member = member.map { member ->
                performances[member.index]?.let { member.copy(scenarioState = ShuntaisaiMemberState(it)) } ?: member
            },
            scenarioStatus = ShuntaisaiStatus(
                turn = turn,
                campTurns = calcShuntaisaiCampTurns(goalRace.map { it.turn }.toSet()),
                maxLevel = maxLevel,
                supportTypeCount = supportTypeCount,
            )
        ).addGuests(calcShuntaisaiGuestPerformances(performances.values.groupingBy { it }.eachCount(), shuntaisaiGuestCount))
    }

    /**
     * 編成外の演目担当が加わる
     */
    private fun SimulationState.addGuests(types: List<StatusType>): SimulationState {
        return types.fold(this) { state, type -> state.addGuest(type, Scenario.SHUNTAISAI) }
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
     * 教えの付け替え
     *
     * 割り当て済みの教え（転成効果を含む）を、各演目に1つずつ割り当て直す
     */
    private suspend fun SimulationState.reassignTeachings(selector: ActionSelector): SimulationState {
        val slots = shuntaisaiStatus?.teachings ?: return this
        if (slots.isEmpty()) return this
        var state = updateShuntaisaiStatus { copy(teachings = emptyMap(), reassigningTeachings = slots) }
        trainingType.forEach { current ->
            val slot = slots[current] ?: return@forEach
            val shuntaisaiStatus = state.shuntaisaiStatus ?: return state
            // 現在の割り当て先を先頭にする
            val targets = trainingType.filter { !shuntaisaiStatus.teachings.containsKey(it) }
                .sortedBy { if (it == current) 0 else 1 }
            val action = selector.select(state, targets.map {
                ShuntaisaiAssignTeaching(slot.teaching, it, slot.transformedFrom, reassign = true)
            })
            state = ShuntaisaiCalculator.applyScenarioAction(state, (action as ShuntaisaiAssignTeaching).result)
        }
        return state.updateShuntaisaiStatus { copy(reassigningTeachings = emptyMap()) }
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
     * 理事長絆は無視する
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
                    skillPt = 300, fanCount = 20000, skillHint = mapOf("全身全霊" to 1),
                )

                ShuntaisaiPresentationResult.SUCCESS -> Status(
                    speed = 5, stamina = 5, power = 5, guts = 5, wisdom = 5,
                    skillPt = 150, fanCount = 2000,
                )
            }
        }
        val great = result == ShuntaisaiPresentationResult.GREAT_SUCCESS
        return addStatus(eventStatus)
            .let { if (great) it.allTrainingLevelUp() else it }
            // 第2回大成功で各演目に演目担当が1人ずつ追加
            .let { if (great && index == 1) it.addGuests(trainingType.toList()) else it }
            .updateShuntaisaiStatus {
                copy(
                    presentationResults = presentationResults + result,
                    presentationBonus = shuntaisaiPresentationBonus[index][result] ?: presentationBonus,
                )
            }
    }

    /**
     * シナリオリンクキャラのイベント（シニア10月前半）
     *
     * シミュレーション上は吾妻の螺旋のエキスパートLv1のみ反映する
     */
    private fun SimulationState.applyLinkEvent(): SimulationState {
        if (support.none { it.charaName == "吾妻" }) return this
        return addStatus(Status(skillHint = mapOf("螺旋のエキスパート" to 1)))
    }

    /**
     * 午式駿大祭本番
     *
     * 理事長絆は無視する、ハヤテ一文字は全て大成功の場合のみ
     */
    private fun SimulationState.applyFinalPerformance(): SimulationState {
        val shuntaisaiStatus = shuntaisaiStatus ?: return this
        val result = shuntaisaiStatus.judgePresentation(2)
        val eventStatus = when (result) {
            ShuntaisaiPresentationResult.GREAT_SUCCESS -> Status(
                speed = 15, stamina = 15, power = 15, guts = 15, wisdom = 15,
                skillPt = 400, hp = 30, motivation = 1, fanCount = 45000,
                skillHint = if (shuntaisaiStatus.presentationResults.all { it == ShuntaisaiPresentationResult.GREAT_SUCCESS }) {
                    mapOf("ハヤテ一文字" to 2, "機先の勝負" to 2, "彼方の呼び声" to 3)
                } else {
                    mapOf("機先の勝負" to 2, "彼方の呼び声" to 3)
                },
            )

            ShuntaisaiPresentationResult.SUCCESS -> Status(
                speed = 10, stamina = 10, power = 10, guts = 10, wisdom = 10,
                skillPt = 200, fanCount = 5000,
                skillHint = mapOf("序盤巧者" to 1, "彼方の呼び声" to 1),
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

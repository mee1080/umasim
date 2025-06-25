package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.utility.applyIf
import kotlin.math.min

class MujintoScenarioEvents : BaseScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        val mujintoStatus = MujintoStatus()
        return super.beforeSimulation(state).copy(scenarioStatus = mujintoStatus)
    }

    override fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        var newState = super.afterAction(state, selector)
        val mujintoStatus = newState.mujintoStatus ?: return newState
        val action = state.history.lastOrNull()?.action ?: return newState // Get the action that just occurred

        // Gain Development Points (発展Pt)
        // "トレーニング、レース等で獲得（お休みは？）"
        var developmentPointsGained = 0
        when (action) {
            is Training -> {
                // TODO: Determine actual points based on training type/level/success
                developmentPointsGained = if (action.result?.success == true) 20 else 5 // Placeholder
                if ((action.actionParam as? MujintoCalculator.MujintoActionParam)?.isIslandTraining == true) {
                    // Island training might consume a ticket
                    newState = newState.updateMujintoStatus { copy(islandTrainingTickets = islandTrainingTickets - 1) }
                    developmentPointsGained += 10 // Extra points for island training? Placeholder
                }
            }
            is Race -> {
                // TODO: Determine points based on race result/importance
                developmentPointsGained = 30 // Placeholder
            }
            is Sleep -> {
                // "お休みは？" - Assuming no points for sleep for now.
                developmentPointsGained = 0
            }
            is Outing -> {
                // Tucker Bligh outings: "共通：お出かけ後に発展ポイント"
                // TODO: Determine specific points from Tucker Bligh outings
                developmentPointsGained = 15 // Placeholder for generic outing, Tucker specific below
            }
            // TODO: Add other actions that might grant development points
        }

        if (developmentPointsGained > 0) {
            newState = newState.updateMujintoStatus {
                val newDevPoints = this.developmentPoints + developmentPointsGained
                // Handle construction progress
                // "建設進捗度が上がる 100Ptで1マス"
                // This logic might be too simple. Construction might be an explicit action.
                // For now, let's assume points directly contribute to some global progress or are just accumulated.
                // Facility construction logic will be handled by specific actions or events.
                copy(developmentPoints = newDevPoints)
            }
        }

        // Handle Facility Construction Progress and Island Training Tickets
        // "建設2枠と5枠？で島トレ券を獲得し、同時に施設効果発動？"
        // This requires tracking "建設枠" (construction slots/masses).
        // This is a placeholder for a more complex system.
        // Let's assume every 200 development points globally might trigger something.
        val totalMassesBuilt = mujintoStatus.developmentPoints / 100 // Simplified: 100 points = 1 mass
        var ticketsToAdd = 0
        var newEventFlags = mujintoStatus.eventFlags
        if (totalMassesBuilt >= 2 && !mujintoStatus.eventFlags.contains("TicketAt2Masses")) {
            ticketsToAdd = 1
            newEventFlags = newEventFlags + "TicketAt2Masses"
        }
        if (totalMassesBuilt >= 5 && !mujintoStatus.eventFlags.contains("TicketAt5Masses")) {
            ticketsToAdd = 1 // This would be the second ticket if the first was already gained.
            newEventFlags = newEventFlags + "TicketAt5Masses"
        }
        if (ticketsToAdd > 0) {
            newState = newState.updateMujintoStatus {
                // "特別な期間を除き1枚のみ所有可能"
                copy(
                    islandTrainingTickets = min(1, this.islandTrainingTickets + ticketsToAdd),
                    eventFlags = newEventFlags
                )
            }
            // TODO: "同時に施設効果発動？" - Implement facility effect activation.
        }


        // Evaluation Meetings (評価会)
        // "半年ごとに発生" (Turns: 12, 24, 36, 48, 60, 72 - assuming 2 turns per month)
        val evaluationTurns = setOf(12, 24, 36, 48, 60) // Not at 72, as it's end of育成
        if (evaluationTurns.contains(newState.turn)) {
            // TODO: Implement evaluation logic
            // "建設状況/発展Ptで評価"
            // "通常トレ効果アップありそう"
            // "ステータスアップ"
            var statusBonus = Status()
            var trainingEffectUp = 0 // Placeholder for %
            val currentDevPoints = mujintoStatus.developmentPoints
            val facilityLevels = mujintoStatus.facilities.values.sumOf { it.level }

            if (currentDevPoints > 500 && facilityLevels > 5) { // Example high evaluation
                statusBonus = Status(speed = 15, stamina = 15, power = 15, guts = 15, wisdom = 15, skillPt = 50)
                trainingEffectUp = 10 // 10% training effect up
            } else if (currentDevPoints > 200 && facilityLevels > 2) { // Example mid evaluation
                statusBonus = Status(speed = 8, stamina = 8, power = 8, guts = 8, wisdom = 8, skillPt = 25)
                trainingEffectUp = 5 // 5% training effect up
            } else { // Example low evaluation
                statusBonus = Status(speed = 3, stamina = 3, power = 3, guts = 3, wisdom = 3, skillPt = 10)
            }
            newState = newState.addStatus(statusBonus)
            // TODO: Store and apply `trainingEffectUp` in MujintoStatus/Calculator
            newState = newState.updateMujintoStatus {
                copy(evaluationMeetingCount = evaluationMeetingCount + 1)
                // Add a flag or a temporary buff for trainingEffectUp
            }
            // For logging/display purposes
            // newState = newState.addEventMessage("Evaluation Meeting Completed! Gained bonus: $statusBonus, Training Effect +$trainingEffectUp%")
        }

        // Tucker Bligh Events
        if (action is Outing && action.memberIndex == state.tuckerBlighIndex) { // Assuming tuckerBlighIndex is known
            newState = handleTuckerBlighOuting(newState, selector, action)
        }
        if (action is Training && action.result?.success == true && state.tuckerBlighIndex != -1 && action.member.any{it.index == state.tuckerBlighIndex}) {
             newState = handleTuckerBlighPostTraining(newState)
        }

        // Scenario Link Character Events
        // TODO: Implement based on "シナリオリンク" in memo.
        // This would involve checking if linked characters are in the deck and triggering their specific events/effects.

        // Specific Turn-based Events from memo (if any, most are TODO)
        // Example: newState.turn == X -> trigger specific event

        return newState
    }

    private fun handleTuckerBlighPostTraining(state: SimulationState): SimulationState {
        var newState = state
        // "トレーニング後イベント: 絆+5、スタ+5、編成サポカ次ターン得意率20、大成功ならやる気+1"
        newState = newState.addRelation(5, memberIndex = state.tuckerBlighIndex)
        newState = newState.addStatus(Status(stamina = 5))
        // TODO: Implement "編成サポカ次ターン得意率20" - This needs a mechanism to apply buffs to other cards for next turn.
        // This could be a temporary list in MujintoStatus or individual flags in MujintoMemberState.
        newState = newState.updateMujintoStatus { copy(tuckerSpecialtyBuffNextTurn = true, tuckerSpecialtyBuffNextTurnAmount = 20) }

        // "大成功ならやる気+1" - How is "大成功" determined for a training event?
        // Assuming a 10% chance for placeholder.
        if (newState.random.nextInt(100) < 10) { // Placeholder for "大成功"
            newState = newState.addStatus(Status(motivation = 1))
        }
        return newState
    }

    private suspend fun handleTuckerBlighOuting(state: SimulationState, selector: ActionSelector, outing: Outing): SimulationState {
        var newState = state
        // "共通：お出かけ後に発展ポイント" - Handled by general development point gain.
        // Outing count is implicitly tracked by game state or could be in MujintoMemberState for Tucker.

        // TODO: Determine which outing number this is. For now, assume it's known or passed.
        // This likely needs a persistent counter for Tucker's outings.
        val tuckerOutingCount = state.member[state.tuckerBlighIndex].outingCount // Assuming outingCount exists on member

        when (tuckerOutingCount) {
            // 1 -> { /* TODO: 1回目 */ }
            // 2 -> { /* TODO: 2回目 */ }
            3 -> {
                // "3回目: 上：絆+5、体力+50、やる気+1、編成サポカ次ターン得意率30"
                // "      下：絆+5、やる気+1、全ステ+8、スキルPt+10、編成サポカ次ターン得意率30"
                // This implies a choice. Need to model this selection.
                // For now, let's pick one path (e.g., upper choice).
                newState = newState.addRelation(5, memberIndex = state.tuckerBlighIndex)
                newState = newState.addStatus(Status(hp = 50, motivation = 1))
                // TODO: "編成サポカ次ターン得意率30"
                newState = newState.updateMujintoStatus { copy(tuckerSpecialtyBuffNextTurn = true, tuckerSpecialtyBuffNextTurnAmount = 30) }

            }
            // 4 -> { /* TODO: 4回目 */ }
            5 -> {
                // "5回目: 絆+5、体力+30、やる気+1、パワー+30、パスファインダーLv3、編成サポカ次ターン得意率120"
                newState = newState.addRelation(5, memberIndex = state.tuckerBlighIndex)
                newState = newState.addStatus(Status(hp = 30, motivation = 1, power = 30, skillHint = mapOf("パスファインダー" to 3)))
                // TODO: "編成サポカ次ターン得意率120"
                 newState = newState.updateMujintoStatus { copy(tuckerSpecialtyBuffNextTurn = true, tuckerSpecialtyBuffNextTurnAmount = 120) }
            }
        }
        // TODO: "初回", "クラシック正月", "育成終了後" events for Tucker.
        return newState
    }

    override fun updateScenarioTurn(state: SimulationState): SimulationState {
        var newState = super.updateScenarioTurn(state)
        val mujintoStatus = newState.mujintoStatus ?: return newState

        // Apply Tucker Bligh's next turn specialty rate up if flagged
        if (mujintoStatus.tuckerSpecialtyBuffNextTurn) {
            val amount = mujintoStatus.tuckerSpecialtyBuffNextTurnAmount
            // TODO: Iterate through all *other* support cards and apply this buff.
            // This requires modifying MemberState or having a global effect list.
            // For now, just log that it would happen.
            // println("Applying Tucker Bligh's specialty rate buff ($amount%) to other support cards for turn ${newState.turn}")
            newState = newState.updateMujintoStatus { copy(tuckerSpecialtyBuffNextTurn = false, tuckerSpecialtyBuffNextTurnAmount = 0) }
        }

        return newState
    }

    // Helper to get Tucker Bligh's index (placeholder)
    private val SimulationState.tuckerBlighIndex: Int
        get() = member.indexOfFirst { it.card.name == "タッカーブライン" } // This is a guess for card name

}

fun SimulationState.addRelation(amount: Int, memberIndex: Int): SimulationState {
    if (memberIndex == -1 || memberIndex >= member.size) return this
    return this.copy(
        member = this.member.mapIndexed { index, m ->
            if (index == memberIndex) m.addRelation(amount) else m
        }
    )
}

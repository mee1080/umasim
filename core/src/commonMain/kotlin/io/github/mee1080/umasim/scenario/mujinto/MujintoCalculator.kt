package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.RaceEntry
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.utility.applyIf
import kotlin.math.min

object MujintoCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        val mujintoStatus = info.mujintoStatus ?: return Status()
        var scenarioBonus = Status()

        // Apply facility bonuses
        // TODO: Refine this based on how "島トレ効果" and specific facility level effects work.
        // This is a very simplified placeholder.
        val speedFacility = mujintoStatus.facilities[FacilityType.SPEED]
        if (speedFacility != null && speedFacility.level > 0) {
            // Example: "スピードLv1: 島トレ・島スピードにスピボ1", "島スピードのトレ効果5%"
            // Assuming direct status bonus for now if it's a speed training.
            if (info.training.type == StatusType.SPEED) {
                scenarioBonus += Status(speed = speedFacility.level * 2) // Placeholder
            }
        }
        // TODO: Add similar logic for STAMINA, POWER, GUTS, WISDOM facilities.
        // TODO: Implement effects of SPECIAL facilities.

        // Apply "Island Training" (島トレ) specific bonuses if this training is an island training.
        // This requires identifying if the current `info.training` is an "Island Training".
        // We might need a new Action type or a flag in `Training` action.
        // For now, assume a generic training.
        if (isIslandTraining(info.training)) {
            // TODO: Implement "Island Training" stat calculation.
            // "全パラメータアップ"
            // "無人島の各施設にサポカとゲストを配置、得意トレの施設なら友情"
            // "サポカタイプに応じた能力が上がる、得意トレがバラバラの方が強い？"
            // This will be complex and needs access to facility levels, support card placements for island training.
            scenarioBonus += Status(speed = 5, stamina = 5, power = 5, guts = 5, wisdom = 5) // Placeholder
        }

        // TODO: Apply bonuses from "Evaluation Meetings" (評価会) if any are active.
        // "通常トレ効果アップありそう"

        // The `mujinto_memo.md` mentions "ゲストあり、最大11人？" for training.
        // This might influence existing `Calculator.calcTrainingStatus` through member count or specific guest mechanics.
        // For now, this is handled by the core calculator, but might need adjustments.

        return scenarioBonus
    }

    // Helper to check if a training is an "Island Training" - needs proper implementation
    private fun isIslandTraining(training: Training): Boolean {
        // Placeholder: This needs a proper way to identify Island Training.
        // Maybe a specific ActionParam or a new Action type.
        return (training.actionParam as? MujintoActionParam)?.isIslandTraining ?: false
    }

    override fun calcBaseRaceStatus(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean,
    ): Status? {
        // TODO: 無人島シナリオ固有のレースステータス計算があれば実装
        // "レース後能力アップ TODO" in memo.
        // This might be a fixed bonus or depend on race performance/facilities.
        if (goal) {
            return Status(skillPt = 10) // Placeholder
        }
        return super.calcBaseRaceStatus(state, race, goal)
    }

    override fun applyScenarioRaceBonus(
        state: SimulationState,
        base: Status,
    ): Status {
        // TODO: 無人島シナリオ固有のレースボーナス適用があれば実装
        // This could also be where "レース後能力アップ" is applied.
        return super.applyScenarioRaceBonus(state, base)
    }

    override fun raceScenarioActionParam(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean,
    ): ScenarioActionParam? {
        // TODO: 無人島シナリオ固有のレースアクションパラメータがあれば実装
        return super.raceScenarioActionParam(state, race, goal)
    }

    override fun predictSleep(
        state: SimulationState,
    ): Array<Action>? {
        // TODO: 無人島シナリオ固有の睡眠予測があれば実装
        // Tucker Bligh outing effects might be relevant here if outings are predicted similarly to sleep.
        return super.predictSleep(state)
    }

    override fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> {
        // This is where we might add `MujintoActionParam` for Island Training.
        val mujintoStatus = state.mujintoStatus ?: return baseActions

        return baseActions.map { action ->
            when (action) {
                is Training -> {
                    // For now, no specific params for normal training, but could be added.
                    // If Island Training is an option, it should be one of the `baseActions`
                    // or generated here based on `mujintoStatus.islandTrainingTickets > 0`.
                    action
                }
                // TODO: Add params for Tucker Bligh outings if they have choices.
                else -> action
            }
        }
    }

    override fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean,
    ): Array<Action> {
        val mujintoStatus = state.mujintoStatus ?: return emptyArray()
        val scenarioActions = mutableListOf<Action>()

        // Predict "Island Training" (島トレ) if a ticket is available
        if (mujintoStatus.islandTrainingTickets > 0) {
            // TODO: Create a proper "IslandTraining" action.
            // This needs to define how stats are gained, which depends on facility levels and support card placements.
            // For now, a placeholder Training action with a special param.
            // The actual stat calculation will happen in `calcScenarioStatus` when this action is chosen.
            val islandTrainingAction = Training(
                type = StatusType.SPEED, // Placeholder, actual Island Training affects all stats
                level = 1, // Placeholder
                base = Status(speed = 5, stamina = 5, power = 5, guts = 5, wisdom = 5), // Placeholder base gain
                failureRate = 0,
                turn = state.turn,
                actionParam = MujintoActionParam(isIslandTraining = true)
                // `member` for island training would be complex, involving all placed supports.
            )
            // scenarioActions.add(islandTrainingAction) // Needs a proper Action definition
        }

        // TODO: Predict "Facility Construction" (施設建設) actions.
        // "建設計画で順番を選ぶ" - implies an action to choose/start construction.
        // This would likely depend on `mujintoStatus.developmentPoints`.

        // TODO: Predict Tucker Bligh outing actions if she's a friend card and outings are available.
        // Her outings have choices, which would need to be modeled as different actions or results.

        return scenarioActions.toTypedArray()
    }


    override fun normalRaceBlocked(
        state: SimulationState,
    ): Boolean {
        // TODO: 無人島シナリオ固有の通常レースブロック条件があれば実装
        return super.normalRaceBlocked(state)
    }

    override fun updateScenarioTurn(
        state: SimulationState,
    ): SimulationState {
        var newState = state
        // Apply Tucker Bligh's "次のターンに得意率アップ" effect
        // This needs tracking of which support cards were affected.
        // Could be stored in `MujintoMemberState` or a temporary list in `MujintoStatus`.
        // TODO: Implement Tucker Bligh's next-turn specialty rate up.

        // Handle construction progress if facilities are being built.
        // "建設には発展Ptが必要"
        // "建設進捗度が上がる 100Ptで1マス"
        // This might be better handled in `afterAction` when development points are gained.

        return super.updateScenarioTurn(newState)
    }

    override fun updateOnAddStatus(
        state: SimulationState,
        status: Status,
    ): SimulationState {
        // TODO: 無人島シナリオ固有のステータス加算時処理があれば実装
        return super.updateOnAddStatus(state, status)
    }

    // --- Helper functions and specific logic for Mujinto ---

    fun applyFacilityConstruction(state: SimulationState, facilityType: FacilityType, pointsSpent: Int): SimulationState {
        val mujintoStatus = state.mujintoStatus ?: return state
        val facility = mujintoStatus.facilities[facilityType] ?: return state

        // Assuming 100 points for 1 "mass" of progress.
        // Facility levels might require different amounts of "masses".
        // TODO: Refine based on actual construction mechanics.
        val newProgress = facility.constructionProgress + pointsSpent
        var newLevel = facility.level
        // Placeholder: assume each level needs 100 progress points (1 mass) for simplicity
        val pointsPerLevel = 100
        if (newProgress >= pointsPerLevel) {
            // Level up logic
            // newLevel = min(5, newLevel + newProgress / pointsPerLevel) // Max level 5
            // facility.constructionProgress = newProgress % pointsPerLevel
            // This needs more details on how levels and "masses" (マス) work.
            // "Lv1～3：1マス、Lv4：2マス、Lv5：3マス"
        } else {
            // facility.constructionProgress = newProgress
        }

        // TODO: "建設2枠と5枠？で島トレ券を獲得し、同時に施設効果発動？"
        // This implies tracking total construction "枠" (slots/masses) filled.

        return state.updateMujintoStatus {
            copy(
                facilities = facilities.toMutableMap().apply {
                    this[facilityType] = facility.copy(level = newLevel, constructionProgress = facility.constructionProgress)
                },
                developmentPoints = developmentPoints - pointsSpent // Assuming points are spent from total
            )
        }
    }

    // ActionParam for Mujinto specific actions like Island Training
    data class MujintoActionParam(
        val isIslandTraining: Boolean = false,
        // TODO: Add other params like facility to construct, Tucker Bligh outing choice, etc.
    ) : ScenarioActionParam(Scenario.MUJINTO)
}

// Extension property for easier access to MujintoStatus from SimulationState
val SimulationState.mujintoStatus: MujintoStatus?
    get() = scenarioStatus as? MujintoStatus

// Extension property for easier access to MujintoMemberState from MemberState
val MemberState.mujintoMemberState: MujintoMemberState?
    get() = scenarioState as? MujintoMemberState

[end of core/src/commonMain/kotlin/io/github/mee1080/umasim/scenario/mujinto/MujintoCalculator.kt]

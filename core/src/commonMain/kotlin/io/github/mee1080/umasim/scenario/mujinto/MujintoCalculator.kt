package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.simulation2.Calculator.CalcInfo
import io.github.mee1080.umasim.simulation2.Calculator.applyMember
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
//        val speedFacility = mujintoStatus.facilities[FacilityType.SPEED]
//        if (speedFacility != null && speedFacility.level > 0) {
//            // Example: "スピードLv1: 島トレ・島スピードにスピボ1", "島スピードのトレ効果5%"
//            // Assuming direct status bonus for now if it's a speed training.
//            if (info.training.type == StatusType.SPEED) {
//                scenarioBonus += Status(speed = speedFacility.level * 2) // Placeholder
//            }
//        }
        // TODO: Add similar logic for STAMINA, POWER, GUTS, WISDOM facilities.
        // TODO: Implement effects of SPECIAL facilities.

        // TODO: Apply bonuses from "Evaluation Meetings" (評価会) if any are active.
        // "通常トレ効果アップありそう"

        // The `mujinto_memo.md` mentions "ゲストあり、最大11人？" for training.
        // This might influence existing `Calculator.calcTrainingStatus` through member count or specific guest mechanics.
        // For now, this is handled by the core calculator, but might need adjustments.

        return scenarioBonus
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
        if (mujintoStatus.islandTrainingTicket > 0) {
            // TODO: 上昇量
            // "全パラメータアップ"
            // "無人島の各施設にサポカとゲストを配置、得意トレの施設なら友情"
            // "サポカタイプに応じた能力が上がる、得意トレがバラバラの方が強い？"
            val islandTrainingAction = MujintoTraining(
                MujintoTrainingResult(state.member, Status()),
            )
            scenarioActions.add(islandTrainingAction) // Needs a proper Action definition
        }

        // TODO: Predict "Facility Construction" (施設建設) actions.
        // "建設計画で順番を選ぶ" - implies an action to choose/start construction.
        // This would likely depend on `mujintoStatus.developmentPoints`.

        // TODO: Predict Tucker Bligh outing actions if she's a friend card and outings are available.
        // Her outings have choices, which would need to be modeled as different actions or results.

        return scenarioActions.toTypedArray()
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

    fun applyScenarioAction(state: SimulationState, action: MujintoActionResult): SimulationState {
        // TODO
        return state
    }

    fun applyScenarioActionParam(
        state: SimulationState,
        action: Action,
        result: ActionResult,
        params: MujintoActionParam,
    ): SimulationState {
        val mujintoStatus = state.mujintoStatus ?: return state
        if (!result.success) return state
        return state.updateMujintoStatus { addPioneerPoint(params.pioneerPoint) }
    }

    fun calcIslandTrainingStatusSeparated(
        info: CalcInfo,
    ): Triple<Pair<Status, ExpectedStatus>, Status, Boolean> {
        val mujintoStatus = info.mujintoStatus!!
        val friendCount = info.support.count { it.isFriendTraining(info.training.type) }
        val member = if (mujintoStatus.facilities.containsKey(StatusType.FRIEND)) info.member else {
            info.member.filter { member ->
                member.positions.any {
                    mujintoStatus.facilities.containsKey(it)
                }
            }
        }
        if (Calculator.DEBUG) {
            member.forEach {
                println("${it.positions} ${it.charaName}")
            }
        }
        val raw = ExpectedStatus(
            speed = calcIslandTrainingStatus(info, member, StatusType.SPEED, friendCount),
            stamina = calcIslandTrainingStatus(info, member, StatusType.STAMINA, friendCount),
            power = calcIslandTrainingStatus(info, member, StatusType.POWER, friendCount),
            guts = calcIslandTrainingStatus(info, member, StatusType.GUTS, friendCount),
            wisdom = calcIslandTrainingStatus(info, member, StatusType.WISDOM, friendCount),
            skillPt = calcIslandTrainingStatus(info, member, StatusType.SKILL, friendCount),
        )
        val base = Status(
            speed = raw.speed.toInt(),
            stamina = raw.stamina.toInt(),
            power = raw.power.toInt(),
            guts = raw.guts.toInt(),
            wisdom = raw.wisdom.toInt(),
            skillPt = raw.skillPt.toInt(),
        )
        return Triple(
            base to raw,
            info.scenario.calculator.calcScenarioStatus(info, base, raw, friendCount > 0),
            friendCount > 0,
        )
    }

    internal fun calcIslandTrainingStatus(
        info: CalcInfo,
        member: List<MemberState>,
        targetType: StatusType,
        friendCount: Int,
    ): Double {
        val mujintoStatus = info.mujintoStatus ?: return 0.0
        val support = member.filter { !it.guest }
        val positionedSupport = support.filter { it.positions.contains(targetType) }
        val upTrainingSupport = support.filter { member ->
            member.positions.any {
                upInTraining(it, targetType)
            }
        }
        val baseStatus = mujintoIslandTrainingBase.status.get(targetType)
        val baseCondition = info.baseSpecialUniqueCondition(
            trainingSupportCount = support.size,
            friendTraining = friendCount > 0,
        )
        val base = baseStatus + support.sumOf {
            it.card.getBaseBonus(targetType, baseCondition.applyMember(it))
        } + mujintoStatus.islandTrainingBonus.get(targetType)

        val charaBonus = info.chara.getBonus(targetType) / 100.0
//        val charaBonus = 1.0

//        val friend = support.map {
//            if (info.allFriend || it.isFriendTraining(info.training.type)) {
//                it.card.friendFactor(baseCondition.applyMember(it))
//            } else 1.0
//        }.fold(1.0) { acc, d -> acc * d }
        val friend = 1.0

        val motivationBase = when (info.motivation) {
            3 -> 0.55
            else -> info.motivation / 10.0
        }
        val motivationBonus =
            1 + motivationBase * (1 + (support.sumOf {
                it.card.motivationFactor(baseCondition.applyMember(it))
            }) / 100.0)
//        val motivationBonus = 1.0

//        val trainingFactor = if (mujintoStatus.facilities.keys.any { upInTraining(it, targetType) }) 0.6 else 0.3
        val trainingFactor = 1.0
        val trainingBonus =
            1 + (support.sumOf {
                it.card.trainingFactor(baseCondition.applyMember(it))
            } * trainingFactor) / 100.0

        val count = when {
            targetType == StatusType.SKILL -> 1.0 + member.size * 0.05

            mujintoStatus.facilities.containsKey(targetType) -> {
                1 + member.count { it.positions.contains(targetType) } * 0.05
            }

            else -> 1.0
        }
//        val count = 1.0 + member.size * 0.05
        val adjust = 1.00
        val raw = base * charaBonus * friend * motivationBonus * trainingBonus * count * adjust
        if (Calculator.DEBUG) {
            println("$targetType $raw base=$baseStatus baseBonus=$base chara=$charaBonus friend=$friend motivation=$motivationBonus training=$trainingBonus count=$count")
        }
        return min(100.0, raw + 0.0002)
    }
}

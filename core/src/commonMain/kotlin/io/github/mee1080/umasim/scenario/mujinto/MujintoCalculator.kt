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
        val friendCount = info.support.count { it.isFriendTraining(it.position) }
        val member = info.member
        if (Calculator.DEBUG) {
            member.forEach {
                println("${it.position} ${it.charaName}")
            }
        }
        val raw = ExpectedStatus(
            speed = calcIslandTrainingStatus(info, StatusType.SPEED, friendCount),
            stamina = calcIslandTrainingStatus(info, StatusType.STAMINA, friendCount),
            power = calcIslandTrainingStatus(info, StatusType.POWER, friendCount),
            guts = calcIslandTrainingStatus(info, StatusType.GUTS, friendCount),
            wisdom = calcIslandTrainingStatus(info, StatusType.WISDOM, friendCount),
            skillPt = calcIslandTrainingStatus(info, StatusType.SKILL, friendCount),
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
            calcIslandTrainingBonus(info, friendCount, base),
            friendCount > 0,
        )
    }

    private fun calcIslandTrainingStatus(
        info: CalcInfo,
        targetType: StatusType,
        friendCount: Int,
    ): Double {
        val mujintoStatus = info.mujintoStatus ?: return 0.0
        val support = info.member.filter { !it.guest }
//        val mainRate = if (targetType == StatusType.SKILL) 0.5 else 0.6
//        val subRate = 0.4
//        val otherRate = 0.0
//        val mainSupport = mutableListOf<MemberState>()
//        val subSupport = mutableListOf<MemberState>()
//        val otherSupport = mutableListOf<MemberState>()
//        if (targetType == StatusType.SKILL) {
//            mainSupport.addAll(support)
//        } else {
//            support.forEach { member ->
//                when {
//                    member.position == targetType -> mainSupport.add(member)
//                    upInTraining(member.position, targetType) -> subSupport.add(member)
//                    else -> otherSupport.add(member)
//                }
//            }
//        }
        val baseStatus = mujintoIslandTrainingBase.status.get(targetType)
        val baseCondition = info.baseSpecialUniqueCondition(
            trainingSupportCount = support.size,
            friendTraining = friendCount > 0,
        )

        // ステータスボーナス
        val base = baseStatus + support.sumOf {
            it.card.getBaseBonus(targetType, baseCondition.applyMember(it))
        } + mujintoStatus.islandTrainingBonus.get(targetType)

        // キャラボーナス
        val charaBonus = info.chara.getBonus(targetType) / 100.0

        // 友情
//        val friend = mainSupport.map {
//            if (it.isFriendTraining(info.training.type)) {
//                calcFriendFactor(it.card, baseCondition.applyMember(it), friendMainRate)
//            } else 1.0
//        }.fold(1.0) { acc, d -> acc * d } * subSupport.map {
//            if (it.isFriendTraining(info.training.type)) {
//                calcFriendFactor(it.card, baseCondition.applyMember(it), friendSubRate)
//            } else 1.0
//        }.fold(1.0) { acc, d -> acc * d } * otherSupport.map {
//            if (it.isFriendTraining(info.training.type)) {
//                calcFriendFactor(it.card, baseCondition.applyMember(it), friendOtherRate)
//            } else 1.0
//        }.fold(1.0) { acc, d -> acc * d }
        val friend = support.map {
            if (it.isFriendTraining(it.position)) {
                val rate = mujintoIslandTrainingRate(it.card.type, it.position, targetType)
                calcFriendFactor(it.card, baseCondition.applyMember(it), rate)
            } else 1.0
        }.fold(1.0) { acc, d -> acc * d }

        // やる気
//        val motivationMainRate = mainRate
//        val motivationSubRate = subRate
//        val motivationOtherRate = otherRate
//        val motivationBase = info.motivation / 10.0
//        val motivationBonus =
//            1 + motivationBase * (1 + (mainSupport.sumOf {
//                (it.card.motivationFactor(baseCondition.applyMember(it)) * motivationMainRate).toInt()
//            } + subSupport.sumOf {
//                (it.card.motivationFactor(baseCondition.applyMember(it)) * motivationSubRate).toInt()
//            } + otherSupport.sumOf {
//                (it.card.motivationFactor(baseCondition.applyMember(it)) * motivationOtherRate).toInt()
//            }) / 100.0)
        val motivationBase = info.motivation / 10.0
        val motivationBonus = 1 + motivationBase * (1 + (support.sumOf {
            val rate = mujintoIslandTrainingRate(it.card.type, it.position, targetType)
            (it.card.motivationFactor(baseCondition.applyMember(it)) * rate).toInt()
        }) / 100.0)

        // トレ効果
//        val trainingMainRate = mainRate
//        val trainingSubRate = subRate
//        val trainingOtherRate = otherRate
//        val trainingBonus =
//            1 + (mainSupport.sumOf {
//                (it.card.trainingFactor(baseCondition.applyMember(it)) * trainingMainRate).toInt()
//            } + subSupport.sumOf {
//                (it.card.trainingFactor(baseCondition.applyMember(it)) * trainingSubRate).toInt()
//            } + otherSupport.sumOf {
//                (it.card.trainingFactor(baseCondition.applyMember(it)) * trainingOtherRate).toInt()
//            }) / 100.0
        val trainingBonus =
            1 + (support.sumOf {
                val rate = mujintoIslandTrainingRate(it.card.type, it.position, targetType)
                (it.card.trainingFactor(baseCondition.applyMember(it)) * rate).toInt()
            } + friendCount * mujintoStatus.trainingEffectByFriend) / 100.0

        val trainingSupportCount = support.count { it.position != StatusType.FRIEND }
        val otherSupportCount = support.size - trainingSupportCount
        val trainingGuestCount = info.member.count { it.guest && it.position != StatusType.FRIEND }
        val otherGuestCount = info.member.size - support.size - trainingGuestCount
        val count =
            1.0 + trainingSupportCount * 0.05 + trainingGuestCount * 0.02 + otherSupportCount * 0.01 + otherGuestCount * 0.01
        val raw = base * charaBonus * friend * motivationBonus * trainingBonus * count
        if (Calculator.DEBUG) {
            println("$targetType $raw base=$baseStatus baseBonus=$base chara=$charaBonus friend=$friend motivation=$motivationBonus training=$trainingBonus count=$count")
        }
        return min(100.0, raw + 0.0002)
    }

    private fun calcFriendFactor(
        card: SupportCard,
        condition: SpecialUniqueCondition,
        rate: Double,
    ) =
        (100 + (card.status.friend * rate).toInt()) * (100 + (card.unique.friend * rate).toInt()) * (100 + (card.specialUnique.sumOf {
            it.friendFactor(card, condition)
        } * rate).toInt()) / 1000000.0

    private fun calcIslandTrainingBonus(
        info: CalcInfo,
        friendCount: Int,
        base: Status,
    ): Status {
        val mujintoStatus = info.mujintoStatus ?: return Status()
        return Status(
            speed = base.speed * mujintoStatus.islandTrainingEffect(StatusType.SPEED, friendCount) / 100,
            stamina = base.stamina * mujintoStatus.islandTrainingEffect(StatusType.STAMINA, friendCount) / 100,
            power = base.power * mujintoStatus.islandTrainingEffect(StatusType.POWER, friendCount) / 100,
            guts = base.guts * mujintoStatus.islandTrainingEffect(StatusType.GUTS, friendCount) / 100,
            wisdom = base.wisdom * mujintoStatus.islandTrainingEffect(StatusType.WISDOM, friendCount) / 100,
            skillPt = base.skillPt * mujintoStatus.islandTrainingEffect(StatusType.SKILL, friendCount) / 100
        )
    }
}

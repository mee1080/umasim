package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.mapIf
import kotlin.math.min

object ShuntaisaiCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        val shuntaisaiStatus = info.shuntaisaiStatus ?: return Status()
        val type = info.training.type
        val effect = shuntaisaiStatus.teachingEffect(type)
        val practice = shuntaisaiStatus.isPractice(type)

        var totalTrainingEffect = effect.trainingEffect
        if (!friendTraining) {
            totalTrainingEffect += effect.nonFriendTrainingEffect
        }
        if (practice) {
            totalTrainingEffect += effect.practiceTrainingEffect
            if (shuntaisaiStatus.seniorAllTypeBonus) {
                totalTrainingEffect += shuntaisaiSeniorAllTypePracticeEffect
            }
        }
        val totalFriendBonus = if (friendTraining) {
            effect.friendBonus + shuntaisaiStatus.presentationBonus.friendBonus
        } else 0
        val practiceStatus = if (practice) shuntaisaiStatus.practiceStatus(type) else Status()
        fun practiceValue(value: Int) = value * (100 + effect.practiceStatusUp) / 100

        if (Calculator.DEBUG) {
            println("shuntaisai: type=$type practice=$practice trainingEffect=$totalTrainingEffect friendBonus=$totalFriendBonus effect=$effect")
        }

        val factor = (100 + totalTrainingEffect) * (100 + totalFriendBonus) / 10000.0
        val limit = 100 + effect.statusLimitUp

        fun calcSingleStatus(target: StatusType): Int {
            val baseValue = base.get(target)
            val statusUp = if (target == type) effect.mainStatusUp else effect.subStatusUp
            val total = (baseValue * factor * (100 + statusUp) / 100.0 - baseValue).toInt()
            return min(total, limit) + practiceValue(practiceStatus.get(target))
        }

        val skillPt = (base.skillPt * factor - base.skillPt).toInt()

        return Status(
            speed = calcSingleStatus(StatusType.SPEED),
            stamina = calcSingleStatus(StatusType.STAMINA),
            power = calcSingleStatus(StatusType.POWER),
            guts = calcSingleStatus(StatusType.GUTS),
            wisdom = calcSingleStatus(StatusType.WISDOM),
            skillPt = min(skillPt, limit) + practiceValue(practiceStatus.skillPt),
        )
    }

    override fun updateScenarioTurn(state: SimulationState): SimulationState {
        return state.updateShuntaisaiStatus {
            copy(turn = state.turn)
        }
    }

    override fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return baseActions
        // お休み、目標外レース等は最低+1（TODO 対象の演目不明、最も低い演目とする）
        val lowestParam = ShuntaisaiActionParam(
            gaugeUp = trainingType.minByOrNull { shuntaisaiStatus.gauge(it) }?.let { mapOf(it to 1) } ?: emptyMap(),
        )
        return baseActions.map { action ->
            when (action) {
                is Training -> {
                    val param = calcTrainingParam(shuntaisaiStatus, action)
                    action.copy(candidates = action.addScenarioActionParam(param))
                }

                is Race -> if (action.goal) action else {
                    action.copy(result = action.result.addScenarioActionParam(lowestParam))
                }

                is Sleep -> action.copy(candidates = action.addScenarioActionParam(lowestParam))
                is Outing -> action.copy(candidates = action.addScenarioActionParam(lowestParam))

                else -> action
            }
        }
    }

    /**
     * 練習に参加した演目担当1人につき+1、友情なら+2
     *
     * TODO シナリオリンクキャラのゲージ上昇
     */
    fun calcTrainingParam(shuntaisaiStatus: ShuntaisaiStatus, action: Training): ShuntaisaiActionParam {
        val gaugeUp = mutableMapOf<StatusType, Int>()
        action.member.forEach { member ->
            val type = member.card.type
            if (trainingType.contains(type)) {
                gaugeUp[type] = (gaugeUp[type] ?: 0) + if (member.isFriendTraining(action.type)) 2 else 1
            }
        }
        val practice = if (shuntaisaiStatus.isPractice(action.type)) action.type else null
        return ShuntaisaiActionParam(gaugeUp, practice)
    }

    fun applyScenarioActionParam(state: SimulationState, param: ShuntaisaiActionParam): SimulationState {
        return state.updateShuntaisaiStatus {
            (param.practice?.let { executePractice(it) } ?: this).addGauges(param.gaugeUp)
        }
    }

    fun applyScenarioAction(state: SimulationState, result: ShuntaisaiActionResult): SimulationState {
        return when (result) {
            is ShuntaisaiAssignTeachingResult -> state.updateShuntaisaiStatus {
                assignTeaching(result.teaching, result.target)
            }

            is ShuntaisaiTransformTeachingResult -> state.updateShuntaisaiStatus {
                transformTeaching(result.teaching, result.target)
            }
        }
    }

    /**
     * 協力の教え: 演目担当の編成サポカが別の練習1箇所にも追加で出現
     */
    override fun modifyShuffledMember(
        state: SimulationState,
        member: List<MemberState>,
    ): List<MemberState> {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return member
        var newMember = member
        trainingType.forEach { type ->
            repeat(shuntaisaiStatus.teachingEffect(type).addMember) {
                val target = newMember
                    .filter { !it.guest && it.card.type == type && it.position != StatusType.NONE && it.additionalPosition.isEmpty() }
                    .randomOrNull() ?: return@repeat
                val additionalType = trainingType.filter { it != target.position }.random()
                newMember = newMember.mapIf({ it.index == target.index }) {
                    it.copy(additionalPosition = it.additionalPosition + additionalType)
                }
            }
        }
        return newMember
    }

    override fun getSpecialityRateUp(state: SimulationState, cardType: StatusType): Int {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return 0
        return shuntaisaiStatus.teachingEffect(cardType).specialityRate +
                shuntaisaiStatus.presentationBonus.specialityRate
    }

    override fun getHintFrequencyUp(state: SimulationState, position: StatusType): Int {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return 0
        return shuntaisaiStatus.teachingEffect(position).hintRate +
                shuntaisaiStatus.presentationBonus.hintRate
    }

    /**
     * 不屈の教え: 残り体力に応じて失敗率ダウン
     *
     * TODO 本来は不屈の教えを割り当てた演目のみ対象、体力ごとの軽減量不明
     */
    override fun getFailureRateDown(state: SimulationState): Int {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return 0
        val target = trainingType.firstOrNull { shuntaisaiStatus.teachingEffect(it).failureRateDown } ?: return 0
        val level = shuntaisaiStatus.level(target)
        val hp = state.status.hp
        return when {
            level >= 10 -> 100
            level >= 9 && hp >= 5 -> 100
            else -> min(100, hp)
        }
    }

    /**
     * 絆の教え
     *
     * TODO 本来は絆の教えを割り当てた演目のみ対象
     */
    override fun getTrainingRelationBonus(state: SimulationState): Int {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return 0
        return trainingType.sumOf { shuntaisaiStatus.teachingEffect(it).relationBonus }
    }
}

package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.mapIf
import kotlin.math.min

object ShuntaisaiCalculator : ScenarioCalculator {

    /**
     * シナリオ上昇量（上段）
     *
     * 基礎能力: floor(下段 * (1 + 友情ボーナス合計) * (1 + トレ効果合計) * (1 + メイン/サブ獲得量アップ合計)) - 下段
     * スキルPt: floor(下段 * (1 + 友情ボーナス合計) * (1 + トレ効果合計 + スキルPtトレ効果)) - 下段
     * 演目稽古では基礎能力に+10
     */
    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        val shuntaisaiStatus = info.shuntaisaiStatus ?: return Status()
        val type = info.training.type
        val practice = shuntaisaiStatus.isPractice(type)
        val effect = shuntaisaiStatus.memberTeachingEffect(info.member)
        val practiceEffect = shuntaisaiStatus.practiceEffect(type)

        var friendBonus = 0
        var trainingEffect = shuntaisaiStatus.joryokuSupportEffect
        var mainStatusUp = effect.mainStatusUp
        var skillPtTrainingEffect = 0
        var statusLimit = 100 + effect.statusLimitUp
        // TODO 特化の獲得上限アップがスキルPtにも適用されるか不明
        var skillPtLimit = 100 + effect.statusLimitUp
        if (friendTraining) {
            friendBonus += shuntaisaiStatus.presentationBonus.friendBonus + effect.friendBonus
        } else {
            trainingEffect += effect.nonFriendTrainingEffect
        }
        if (practice) {
            if (friendTraining) {
                friendBonus += practiceEffect.friendBonus + effect.practiceFriendBonus
            }
            trainingEffect += effect.practiceTrainingEffect
            mainStatusUp += effect.practiceMainStatusUp
            if (shuntaisaiStatus.allTypeBonus) {
                skillPtTrainingEffect += practiceEffect.allTypeSkillPtTrainingEffect
                statusLimit += practiceEffect.allTypeStatusLimitUp
                skillPtLimit += practiceEffect.allTypeSkillPtLimitUp
            }
        }
        val practiceStatusUp = if (practice) practiceEffect.statusUp else 0

        if (Calculator.DEBUG) {
            println("shuntaisai: type=$type practice=$practice friendBonus=$friendBonus trainingEffect=$trainingEffect mainStatusUp=$mainStatusUp subStatusUp=${effect.subStatusUp} skillPtTrainingEffect=$skillPtTrainingEffect")
        }

        fun calcValue(baseValue: Int, rate1: Int, rate2: Int): Int {
            return (baseValue.toLong() * (100 + friendBonus) * rate1 * rate2 / 1000000L).toInt() - baseValue
        }

        fun calcSingleStatus(target: StatusType): Int {
            val statusUp = if (target == type) mainStatusUp else effect.subStatusUp
            val value = calcValue(base.get(target), 100 + trainingEffect, 100 + statusUp)
            return min(value, statusLimit) + practiceStatusUp
        }

        return Status(
            speed = calcSingleStatus(StatusType.SPEED),
            stamina = calcSingleStatus(StatusType.STAMINA),
            power = calcSingleStatus(StatusType.POWER),
            guts = calcSingleStatus(StatusType.GUTS),
            wisdom = calcSingleStatus(StatusType.WISDOM),
            skillPt = min(calcValue(base.skillPt, 100 + trainingEffect + skillPtTrainingEffect, 100), skillPtLimit),
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
        val practice = shuntaisaiStatus.isPractice(action.type)
        return ShuntaisaiActionParam(
            gaugeUp = gaugeUp,
            practice = if (practice) action.type else null,
            training = true,
            joryokuSupport = practice && shuntaisaiStatus.memberTeachingEffect(action.member).joryokuSupport,
        )
    }

    fun applyScenarioActionParam(state: SimulationState, param: ShuntaisaiActionParam): SimulationState {
        return state.updateShuntaisaiStatus {
            (param.practice?.let { executePractice(it) } ?: this)
                .addGauges(param.gaugeUp)
                .let { if (param.training) it.updateJoryokuSupport(param.joryokuSupport) else it }
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
     * 配置の調整
     *
     * * 演目稽古に各演目担当が現れる
     * * 吾妻は4人以下の演目稽古に現れる（TODO 現れる条件の詳細不明、ランダムな1箇所とする）
     * * 協力の教え: 演目担当の編成サポカが別の練習1箇所にも追加で出現
     * * TODO 絆の教えの転成効果（合宿中に追加で1人出現）は未実装
     */
    override fun modifyShuffledMember(
        state: SimulationState,
        member: List<MemberState>,
    ): List<MemberState> {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return member
        var newMember = member
        fun count(type: StatusType) = newMember.count { it.positions.contains(type) }
        fun removePosition(target: MemberState, type: StatusType) {
            newMember = newMember.mapIf({ it.index == target.index }) {
                if (it.position == type) {
                    it.copy(position = StatusType.NONE)
                } else {
                    it.copy(additionalPosition = it.additionalPosition - type)
                }
            }
        }

        val practiceTypes = trainingType.filter { shuntaisaiStatus.isPractice(it) }
        practiceTypes.forEach { type ->
            newMember = newMember.mapIf({ it.card.type == type }) {
                it.copy(position = type, additionalPosition = it.additionalPosition - type)
            }
        }
        practiceTypes.forEach { type ->
            while (count(type) > 5) {
                val positioned = newMember.filter { it.positions.contains(type) }
                removePosition(positioned.firstOrNull { it.card.type != type } ?: positioned.last(), type)
            }
        }

        val azuma = newMember.firstOrNull { !it.guest && it.charaName == "吾妻" }
        if (azuma != null) {
            practiceTypes.filter { !azuma.positions.contains(it) && count(it) <= 4 }.randomOrNull()?.let { type ->
                newMember = newMember.mapIf({ it.index == azuma.index }) {
                    it.copy(additionalPosition = it.additionalPosition + type)
                }
            }
        }

        trainingType.forEach { type ->
            repeat(shuntaisaiStatus.teachingEffect(type).addMember) {
                val target = newMember
                    .filter { !it.guest && it.card.type == type && it.position != StatusType.NONE && it.additionalPosition.isEmpty() }
                    .randomOrNull() ?: return@repeat
                val additionalType = trainingType.filter { !target.positions.contains(it) && count(it) < 5 }
                    .randomOrNull() ?: return@repeat
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

    /**
     * TODO 本来はヒントの教えを割り当てた演目の担当が対象と思われるが、配置先の演目で判定している
     */
    override fun getHintFrequencyUp(state: SimulationState, position: StatusType): Int {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return 0
        return shuntaisaiStatus.teachingEffect(position).hintRate +
                shuntaisaiStatus.presentationBonus.hintRate
    }

    /**
     * 演目稽古（サポカタイプ5種以上）: 全サポカのヒントイベント発生
     *
     * TODO 演目Lv4以上の「全ヒントイベントの効果が発動」との違いは未反映
     */
    override fun isAllSupportHint(state: SimulationState, position: StatusType): Boolean {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return false
        return shuntaisaiStatus.allTypeBonus && shuntaisaiStatus.isPractice(position)
    }

    /**
     * 不屈の教え: 演目担当が参加するトレーニングで、体力が一定以上なら失敗率100%ダウン
     */
    override fun getFailureRateDown(state: SimulationState, training: TrainingBase, member: List<MemberState>): Int {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return 0
        val hp = shuntaisaiStatus.memberTeachingEffect(member).failureRateDownHp ?: return 0
        return if (state.status.hp >= hp) 100 else 0
    }

    /**
     * 体力の教え: 演目担当が参加するトレーニングの体力消費ダウン
     */
    override fun getHpCostDown(info: Calculator.CalcInfo): Int {
        val shuntaisaiStatus = info.shuntaisaiStatus ?: return 0
        val effect = shuntaisaiStatus.memberTeachingEffect(info.member)
        return effect.hpCostDown + if (shuntaisaiStatus.isPractice(info.training.type)) effect.practiceHpCostDown else 0
    }

    /**
     * 絆の教え
     *
     * TODO 本来は絆の教えの演目担当が参加するトレーニングで、全編成サポカの絆ゲージ上昇
     */
    override fun getTrainingRelationBonus(state: SimulationState): Int {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return 0
        return trainingType.maxOf { shuntaisaiStatus.teachingEffect(it).relationBonus }
    }
}

package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.RaceEntry
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.scenario.addSupportToTrainings
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.mapIf
import kotlin.math.min
import kotlin.random.Random

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
        // 目標レース+4、お休み、お出かけ、目標外レースは+1（演目Lvとゲージが低い演目から順）
        val lowestParam = ShuntaisaiActionParam(gaugeUp = shuntaisaiStatus.calcLowestGaugeUp(1))
        val goalRaceParam = ShuntaisaiActionParam(gaugeUp = shuntaisaiStatus.calcLowestGaugeUp(4))
        return baseActions.map { action ->
            when (action) {
                is Training -> {
                    val param = calcTrainingParam(shuntaisaiStatus, action)
                    val candidates = action.addScenarioActionParam(param)
                    action.copy(
                        candidates = if (shuntaisaiStatus.isFinals) {
                            // URAファイナルズ: トレーニングで体力+30、やる気+1
                            candidates.mapIf({ it.first.success }) {
                                (it.first as StatusActionResult).copy(
                                    status = it.first.status + shuntaisaiFinalsTrainingStatus
                                ) to it.second
                            }
                        } else candidates
                    )
                }

                is Race -> action.copy(
                    result = action.result.addScenarioActionParam(if (action.goal) goalRaceParam else lowestParam)
                )

                is Sleep -> action.copy(candidates = action.addScenarioActionParam(lowestParam))
                is Outing -> action.copy(candidates = action.addScenarioActionParam(lowestParam))

                else -> action
            }
        }
    }

    /**
     * 練習に参加した演目担当1人につき担当演目のゲージ+1、友情なら+2（シナリオリンクキャラは固定の担当演目）
     *
     * 合宿中はゲージが上昇しない
     */
    fun calcTrainingParam(shuntaisaiStatus: ShuntaisaiStatus, action: Training): ShuntaisaiActionParam {
        val gaugeUp = mutableMapOf<StatusType, Int>()
        if (!shuntaisaiStatus.isCamp) action.member.forEach { member ->
            val type = member.shuntaisaiPerformance ?: return@forEach
            gaugeUp[type] = (gaugeUp[type] ?: 0) + if (member.isFriendTraining(action.type)) 2 else 1
        }
        val practice = shuntaisaiStatus.isPractice(action.type)
        val effect = shuntaisaiStatus.memberTeachingEffect(action.member)
        return ShuntaisaiActionParam(
            gaugeUp = gaugeUp,
            practice = if (practice) action.type else null,
            training = true,
            joryokuSupport = practice && effect.joryokuSupport,
            relationUp = effect.relationBonus,
            additionalPracticeTarget = action.type,
            additionalPracticeRate = min(100, effect.additionalPracticeRate),
        )
    }

    fun applyScenarioActionParam(state: SimulationState, param: ShuntaisaiActionParam): SimulationState {
        var newState = state.updateShuntaisaiStatus {
            (param.practice?.let { executePractice(it) } ?: this)
                .addGauges(param.gaugeUp)
                .let { if (param.training) it.updateJoryokuSupport(param.joryokuSupport) else it }
        }
        // 絆の教え: 全編成サポカの絆ゲージ上昇
        if (param.relationUp > 0) {
            newState = newState.addRelation(param.relationUp) { !it.guest }
        }
        val additionalPracticeTarget = param.additionalPracticeTarget
        if (additionalPracticeTarget != null && Random.nextInt(100) < param.additionalPracticeRate) {
            newState = newState.applyAdditionalPractice(additionalPracticeTarget)
        }
        return newState
    }

    /**
     * 研鑽の教え: 追加の稽古
     *
     * 上の選択肢（体力-5、直前トレ対象+20、SP+20、演目Lvが低い演目のゲージ+1）を選択する
     * 理事長絆、バッドコンディション解除は無視する
     */
    private fun SimulationState.applyAdditionalPractice(target: StatusType): SimulationState {
        return addStatus(
            Status(skillPt = shuntaisaiAdditionalPracticeSkillPt, hp = shuntaisaiAdditionalPracticeHp)
                .add(target to shuntaisaiAdditionalPracticeStatus)
        ).updateShuntaisaiStatus { addLowestGauge() }
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
     * * 吾妻は4人以下の演目稽古のうち、ランダムな1箇所に現れる
     * * 協力の教え: 演目担当の編成サポカが別の練習1箇所にも追加で出現
     * * 絆の教えの転成効果: 合宿中に演目担当の編成サポカが別の練習1箇所にも追加で出現
     * * URAファイナルズ: 全ての演目稽古に編成サポカ+1
     * * 演目稽古（サポカタイプ5種以上）: 友人とグループを除く全ての編成サポカにヒント発生
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
            newMember = newMember.mapIf({ it.shuntaisaiPerformance == type }) {
                it.copy(position = type, additionalPosition = it.additionalPosition - type)
            }
        }
        practiceTypes.forEach { type ->
            while (count(type) > 5) {
                val positioned = newMember.filter { it.positions.contains(type) }
                removePosition(positioned.firstOrNull { it.shuntaisaiPerformance != type } ?: positioned.last(), type)
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
            val effect = shuntaisaiStatus.teachingEffect(type)
            val addMember = effect.addMember + if (shuntaisaiStatus.isCamp) effect.campAddMember else 0
            repeat(addMember) {
                val target = newMember
                    .filter { !it.guest && it.shuntaisaiPerformance == type && it.position != StatusType.NONE && it.additionalPosition.isEmpty() }
                    .randomOrNull() ?: return@repeat
                val additionalType = trainingType.filter { !target.positions.contains(it) && count(it) < 5 }
                    .randomOrNull() ?: return@repeat
                newMember = newMember.mapIf({ it.index == target.index }) {
                    it.copy(additionalPosition = it.additionalPosition + additionalType)
                }
            }
        }

        if (shuntaisaiStatus.isFinals) {
            newMember = addSupportToTrainings(newMember, practiceTypes)
        }

        if (shuntaisaiStatus.allTypeBonus) {
            newMember = newMember.mapIf({ member ->
                !member.guest && !member.outingType && member.positions.any { practiceTypes.contains(it) }
            }) {
                it.copy(supportState = it.supportState?.copy(hintIcon = true))
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
     * ヒントの教え: 教えを割り当てた演目の担当のヒント発生率アップ
     */
    override fun getHintFrequencyUp(state: SimulationState, member: MemberState): Int {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return 0
        val teachingHintRate = member.shuntaisaiPerformance?.let { shuntaisaiStatus.teachingEffect(it).hintRate } ?: 0
        return teachingHintRate + shuntaisaiStatus.presentationBonus.hintRate
    }

    /**
     * ヒントの教えの転成効果: 演目稽古のヒントイベントで獲得するヒントLv+1
     */
    override fun getHintLevelUp(state: SimulationState, position: StatusType, member: List<MemberState>): Int {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return 0
        if (!shuntaisaiStatus.isPractice(position)) return 0
        return shuntaisaiStatus.memberTeachingEffect(member).practiceHintPlus
    }

    /**
     * 演目稽古（サポカタイプ5種以上、演目Lv4以上）: 全ヒントイベントの効果が発動
     *
     * 演目Lv3以下はヒントイベントの発生のみ（[modifyShuffledMember]でヒントアイコンを表示）
     */
    override fun isAllSupportHint(state: SimulationState, position: StatusType): Boolean {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return false
        return shuntaisaiStatus.allTypeBonus && shuntaisaiStatus.isPractice(position) &&
                shuntaisaiStatus.practiceEffect(position).allTypeAllHintEffect
    }

    /**
     * * URAファイナルズ: 失敗率100%ダウン
     * * 不屈の教え: 演目担当が参加するトレーニングで、体力が一定以上なら失敗率100%ダウン
     */
    override fun getFailureRateDown(state: SimulationState, training: TrainingBase, member: List<MemberState>): Int {
        val shuntaisaiStatus = state.shuntaisaiStatus ?: return 0
        if (shuntaisaiStatus.isFinals) return 100
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
     * 駿大祭ボーナス効果のレースボーナス
     */
    override fun calcBaseRaceStatus(state: SimulationState, race: RaceEntry, goal: Boolean): Status? {
        val raceBonus = state.shuntaisaiStatus?.presentationBonus?.raceBonus ?: 0
        if (raceBonus == 0) return null
        return state.defaultBaseRaceStatus(race, goal, state.totalRaceBonus + raceBonus)
    }
}

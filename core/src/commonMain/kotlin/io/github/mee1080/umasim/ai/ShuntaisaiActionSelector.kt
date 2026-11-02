package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.shuntaisai.*
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.max
import kotlin.math.min

/**
 * 駿大祭シナリオのAI
 *
 * 行動ごとに、結果候補の期待値を評価値（Runner.shuntaisaiSettingと同じ単位）で算出して最大のものを選択する
 */
open class ShuntaisaiActionSelector(
    vararg val options: Option = defaultOptions,
) : ActionSelector {

    companion object {
        const val DEBUG = false

        private val S = StatusType.SPEED
        private val H = StatusType.STAMINA
        private val P = StatusType.POWER
        private val G = StatusType.GUTS
        private val W = StatusType.WISDOM

        val defaultOptions = arrayOf(
            // ジュニア
            Option(
                stamina = 1.47,
                levelUrgency = 147.0,
                teachings = listOf(
                    ShuntaisaiTeaching.JORYOKU,
                    ShuntaisaiTeaching.TAIRYOKU,
                    ShuntaisaiTeaching.HINT,
                    ShuntaisaiTeaching.TOKUI,
                    ShuntaisaiTeaching.KIZUNA,
                ),
            ),
            // クラシック
            Option(
                speed = 1.4,
                wisdom = 1.372,
                skillPt = 1.47,
                level = 12.005,
                levelUrgency = 73.5,
                teachings = listOf(
                    ShuntaisaiTeaching.TOKKA,
                    ShuntaisaiTeaching.HINT,
                    ShuntaisaiTeaching.TAIRYOKU,
                    ShuntaisaiTeaching.JORYOKU,
                    ShuntaisaiTeaching.KENSAN,
                ),
                campTeachings = listOf(
                    ShuntaisaiTeaching.TOKKA,
                    ShuntaisaiTeaching.TAIRYOKU,
                    ShuntaisaiTeaching.KENSAN,
                    ShuntaisaiTeaching.HINT,
                    ShuntaisaiTeaching.JORYOKU,
                ),
                transforms = mapOf(
                    ShuntaisaiTeaching.TOKKA to ShuntaisaiTeaching.TOKUI,
                    ShuntaisaiTeaching.KENSAN to ShuntaisaiTeaching.KIZUNA,
                ),
                holdPractice = listOf(StatusType.GUTS),
                holdPracticePenalty = 500.0,
            ),
            // シニア
            Option(
                speed = 1.4,
                stamina = 0.735,
                hp = 4.2,
                gauge = 75.295,
                teachings = listOf(
                    ShuntaisaiTeaching.KYORYOKU,
                    ShuntaisaiTeaching.TOKKA,
                    ShuntaisaiTeaching.RYODO,
                    ShuntaisaiTeaching.FUKUTSU,
                    ShuntaisaiTeaching.KENSAN,
                ),
                transforms = mapOf(
                    ShuntaisaiTeaching.FUKUTSU to ShuntaisaiTeaching.JORYOKU,
                    ShuntaisaiTeaching.RYODO to ShuntaisaiTeaching.HINT,
                    ShuntaisaiTeaching.KYORYOKU to ShuntaisaiTeaching.TAIRYOKU,
                ),
                holdPractice = listOf(StatusType.GUTS),
                holdPracticePenalty = 500.0,
            ),
            // URAファイナルズ
            Option(
                stamina = 1.5,
                wisdom = 1.0,
                skillPt = 3.0,
                motivation = 30.0,
                risk = 1.5,
                relation = 0.0,
                outingRelation = 0.0,
                gauge = 0.0,
                level = 0.0,
                levelUrgency = 0.0,
            ),
        )

        val defaultOption = defaultOptions[0]

        private val json = Json {
            ignoreUnknownKeys = false
            encodeDefaults = true
        }

        fun decodeOptions(serialized: String) = json.decodeFromString<Array<Option>>(serialized)

        fun encodeOptions(options: Array<Option>) = json.encodeToString(options)

        val defaultGenerator = object : ActionSelectorGenerator {
            override fun generateSelector() = ShuntaisaiActionSelector()
        }
    }

    /**
     * 評価値はRunner.shuntaisaiSettingと同じ単位（スピード1=2.0など）
     *
     * @param statusCapMargin 評価上限からこの値を引いた値を超える分のステータスは評価しない
     * @param statusCapMarginPerTurn 残りターン1あたりのstatusCapMarginの追加分（今後の上昇見込み）
     * @param hp 体力1あたりの評価値（最大体力を超える分は評価しない）
     * @param hpKeep 行動後の体力がhpKeepThreshold未満になる場合の、不足1あたりの減点
     * @param risk 評価値が負の結果に掛ける倍率
     * @param relation 絆が友情トレーニング可能値未満のサポカ1人あたりの評価値（トレーニング参加時）
     * @param gauge 演目ゲージ1あたりの評価値
     * @param level 演目Lv1あたりの評価値
     * @param levelUrgency 次の進捗発表会の大成功に足りない演目Lv1あたりの追加評価値
     * @param race 目標外レースの追加評価値
     * @param joryokuSupport 助力の支え獲得の評価値
     * @param hpDecayTurns URAファイナルズ（トレーニングで体力回復、失敗率0）直前のこのターン数で体力の評価を0に近づける
     * @param holdPractice 演目稽古を実行せずゲージ最大で止めておく演目（目標レース等のゲージ上昇を他の演目に回すため、大成功に演目Lvが足りない場合を除く）
     * @param holdPracticePenalty holdPracticeの演目稽古を実行する場合の減点
     * @param teachings 教えの割り当て（トレーニングの種類順）
     * @param campTeachings 合宿直前の付け替えでの教えの割り当て（nullならteachingsと同じ）
     * @param transforms 転成の組み合わせ（新しい教え→消費する教え）
     */
    @Serializable
    data class Option(
        val speed: Double = 2.0,
        val stamina: Double = 1.05,
        val power: Double = 1.0,
        val guts: Double = 0.5,
        val wisdom: Double = 0.98,
        val skillPt: Double = 2.1,
        val speedCap: Int = 3200,
        val staminaCap: Int = 2500,
        val powerCap: Int = 2400,
        val gutsCap: Int = 2200,
        val wisdomCap: Int = 2600,
        val statusCapMargin: Int = 100,
        val statusCapMarginPerTurn: Double = 0.0,
        val hp: Double = 3.0,
        val hpKeepThreshold: Int = 62,
        val hpKeep: Double = 5.6,
        val motivation: Double = 42.0,
        val risk: Double = 2.1,
        val relation: Double = 20.0,
        val outingRelation: Double = 30.0,
        val gauge: Double = 53.782,
        val level: Double = 17.15,
        val levelUrgency: Double = 105.0,
        val race: Double = -100.0,
        val joryokuSupport: Double = 30.0,
        val hpDecayTurns: Int = 4,
        val holdPractice: List<StatusType> = emptyList(),
        val holdPracticePenalty: Double = 0.0,
        val teachings: List<ShuntaisaiTeaching> = emptyList(),
        val campTeachings: List<ShuntaisaiTeaching>? = null,
        val transforms: Map<ShuntaisaiTeaching, ShuntaisaiTeaching> = emptyMap(),
    ) : SerializableActionSelectorGenerator {

        fun weight(type: StatusType) = when (type) {
            StatusType.SPEED -> speed
            StatusType.STAMINA -> stamina
            StatusType.POWER -> power
            StatusType.GUTS -> guts
            StatusType.WISDOM -> wisdom
            StatusType.SKILL -> skillPt
            else -> 0.0
        }

        fun cap(type: StatusType) = when (type) {
            StatusType.SPEED -> speedCap
            StatusType.STAMINA -> staminaCap
            StatusType.POWER -> powerCap
            StatusType.GUTS -> gutsCap
            StatusType.WISDOM -> wisdomCap
            else -> Int.MAX_VALUE
        }

        override fun generateSelector() = ShuntaisaiActionSelector(this)
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    private fun optionAt(index: Int) = options[min(index, options.lastIndex)]

    private fun getOption(state: SimulationState) = optionAt(
        when {
            state.turn <= 24 -> 0
            state.turn <= 48 -> 1
            state.turn <= 72 -> 2
            else -> 3
        }
    )

    /**
     * 教えの割り当てに使用する設定（年末の伝受・付け替えは翌年の設定）
     */
    private fun getTeachingOption(state: SimulationState) = optionAt(
        when {
            state.turn < 24 -> 0
            state.turn < 48 -> 1
            else -> 2
        }
    )

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        return selectWithScore(state, selection).first
    }

    override suspend fun selectWithScore(
        state: SimulationState,
        selection: List<Action>,
    ): Triple<Action, List<Double>, Double> {
        val option = getOption(state)
        val context = Context(option, state)
        val scores = selection.map { calcScore(context, it) }
        var selectedIndex = scores.indices.maxBy { scores[it] }
        val selected = selection[selectedIndex]

        // お休みで友人お出かけが可能な場合は友人お出かけ
        if (selected is Sleep) {
            val outingIndex = selection.indexOfFirst { it is Outing && it.support != null }
            if (outingIndex >= 0) selectedIndex = outingIndex
        }
        if (DEBUG) {
            println("turn ${state.turn}")
            selection.forEachIndexed { index, action -> println("  ${scores[index]} ${action.toShortString()}") }
            println("  -> ${selection[selectedIndex].name}")
        }
        return Triple(selection[selectedIndex], scores, scores[selectedIndex])
    }

    private class Context(
        val option: Option,
        val state: SimulationState,
    ) {
        val shuntaisaiStatus = state.shuntaisaiStatus

        val capMargin = option.statusCapMargin + (option.statusCapMarginPerTurn * (78 - state.turn)).toInt()

        val hpFactor = ((shuntaisaiFinalsStartTurn - 1 - state.turn).toDouble() / option.hpDecayTurns).coerceIn(0.0, 1.0)

        /**
         * 次の進捗発表会の大成功に不足している演目Lv（以降の合宿終了による上昇を考慮）
         */
        val levelDeficit: Map<StatusType, Int> = run {
            val status = shuntaisaiStatus ?: return@run emptyMap()
            val index = status.presentationResults.size
            if (index >= shuntaisaiGreatTargetLevel.size) return@run emptyMap()
            val deadline = listOf(24, 48, 68)[index]
            val target = shuntaisaiGreatTargetLevel[index]
            val campBonus = status.campTurns.count { status.isCampEnd(it) && it >= state.turn && it <= deadline }
            trainingType.associateWith { max(0, target - status.level(it) - campBonus) }
        }
    }

    private fun calcScore(context: Context, action: Action): Double {
        return when (action) {
            is ShuntaisaiAssignTeaching -> calcAssignTeachingScore(context, action)
            is ShuntaisaiTransformTeaching -> calcTransformTeachingScore(context, action)
            else -> {
                if (!action.turnChange) return 0.0
                val total = action.candidates.sumOf { it.second }.toDouble()
                action.candidates.sumOf { (result, rate) ->
                    calcResultScore(context, action, result) * rate / total
                } + calcActionBonus(context, action)
            }
        }
    }

    private fun calcAssignTeachingScore(context: Context, action: ShuntaisaiAssignTeaching): Double {
        val option = getTeachingOption(context.state)
        val status = context.shuntaisaiStatus
        val teachings = if (status?.isCampStartReassign(context.state.turn) == true) {
            option.campTeachings ?: option.teachings
        } else option.teachings
        val index = trainingType.indexOf(action.target)
        return if (teachings.getOrNull(index) == action.teaching) 1.0 else 0.0
    }

    private fun ShuntaisaiStatus.isCampStartReassign(turn: Int) = shuntaisaiCampStartTurns.contains(turn)

    private fun calcTransformTeachingScore(context: Context, action: ShuntaisaiTransformTeaching): Double {
        val option = getTeachingOption(context.state)
        return if (option.transforms[action.teaching] == action.consumed) 1.0 else 0.0
    }

    private fun calcResultScore(context: Context, action: Action, result: ActionResult): Double {
        val (option, state) = context.option to context.state
        val status = result.status
        var score = calcStatusScore(context, state.status, status + expectedHintStatus(context, action))
        score += calcHpScore(option, state.status, status.hp) * context.hpFactor
        score += status.motivation * option.motivation
        if (result.success && action is Training) {
            score += action.support.sumOf {
                if (it.relation >= it.card.requiredRelation) 0.0 else {
                    if (it.outingType) option.outingRelation else option.relation
                }
            }
        }
        val param = result.scenarioActionParam as? ShuntaisaiActionParam
        if (param != null) {
            score += calcScenarioScore(context, action, param, result.success)
        }
        return if (score < 0) score * option.risk else score
    }

    private fun calcStatusScore(context: Context, current: Status, diff: Status): Double {
        val option = context.option
        var score = diff.skillPt * option.skillPt
        trainingType.forEach { type ->
            val value = diff.get(type)
            if (value <= 0) {
                score += value * option.weight(type)
            } else {
                val rest = max(0, option.cap(type) - context.capMargin - current.get(type))
                score += min(value, rest) * option.weight(type)
            }
        }
        return score
    }

    private fun calcHpScore(option: Option, current: Status, diff: Int): Double {
        val after = min(current.maxHp, current.hp + diff)
        var score = (after - current.hp) * option.hp
        if (after < option.hpKeepThreshold) {
            score -= (option.hpKeepThreshold - after) * option.hpKeep
        }
        return score
    }

    private fun expectedHintStatus(context: Context, action: Action): Status {
        if (action !is Training) return Status()
        val target = action.member.filter { it.hint }
        if (target.isEmpty()) return Status()
        val status = context.shuntaisaiStatus
        val allHint = status != null && status.allTypeBonus && status.isPractice(action.type) &&
                status.practiceEffect(action.type).allTypeAllHintEffect
        return if (allHint) {
            target.filter { !it.guest && !it.outingType }.fold(Status()) { acc, member -> acc + member.card.hintStatus }
        } else {
            // ランダムに1人分（期待値は切り捨て）
            target.fold(Status()) { acc, member -> acc + member.card.hintStatus }.let {
                Status(
                    speed = it.speed / target.size,
                    stamina = it.stamina / target.size,
                    power = it.power / target.size,
                    guts = it.guts / target.size,
                    wisdom = it.wisdom / target.size,
                    skillPt = it.skillPt / target.size,
                )
            }
        }
    }

    private fun calcScenarioScore(
        context: Context,
        action: Action,
        param: ShuntaisaiActionParam,
        success: Boolean,
    ): Double {
        val option = context.option
        val status = context.shuntaisaiStatus ?: return 0.0
        var score = 0.0
        param.gaugeUp.forEach { (type, value) ->
            val up = min(value, shuntaisaiMaxGauge - status.gauge(type))
            if (up > 0 && status.level(type) < status.maxLevel) {
                score += up * (option.gauge + (context.levelDeficit[type] ?: 0).coerceAtMost(1) * option.levelUrgency / shuntaisaiMaxGauge)
            }
        }
        val practice = param.practice
        if (practice != null && !status.isCamp && !status.isFinals &&
            option.holdPractice.contains(practice) && (context.levelDeficit[practice] ?: 0) == 0
        ) {
            score -= option.holdPracticePenalty
        }
        if (success && practice != null && !status.isCamp && status.level(practice) < status.maxLevel) {
            score += option.level
            if ((context.levelDeficit[practice] ?: 0) > 0) score += option.levelUrgency
        }
        if (success && param.joryokuSupport) {
            score += option.joryokuSupport
        }
        val additionalTarget = param.additionalPracticeTarget
        if (success && additionalTarget != null && param.additionalPracticeRate > 0) {
            val additional = calcStatusScore(
                context, context.state.status,
                Status(skillPt = shuntaisaiAdditionalPracticeSkillPt).add(additionalTarget to shuntaisaiAdditionalPracticeStatus),
            ) + shuntaisaiAdditionalPracticeHp * option.hp * context.hpFactor + option.gauge
            score += additional * param.additionalPracticeRate / 100.0
        }
        if (success && param.relationUp > 0) {
            val members = (action as? Training)?.support?.map { it.index } ?: emptyList()
            score += context.state.support.count {
                !members.contains(it.index) && it.relation < it.card.requiredRelation
            } * option.relation * 0.3
        }
        return score
    }

    /**
     * 結果候補に含まれない効果
     */
    private fun calcActionBonus(context: Context, action: Action): Double {
        val option = context.option
        if (action is Race && !action.goal) return option.race
        val status = context.shuntaisaiStatus ?: return 0.0
        // 吾妻のお出かけ: 最も低い演目Lv+1
        if (action is Outing && action.support?.charaName == "吾妻") {
            val target = trainingType.minWithOrNull(compareBy({ status.level(it) }, { status.gauge(it) }))
            if (target != null && status.level(target) < status.maxLevel) {
                return option.level + if ((context.levelDeficit[target] ?: 0) > 0) option.levelUrgency else 0.0
            }
        }
        return 0.0
    }
}

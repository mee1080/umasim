package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.scenario.ramen.RamenRegion
import io.github.mee1080.umasim.scenario.ramen.RamenTipType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable
import kotlin.math.max

open class RamenActionSelector(
    vararg val options: Option = deafultOptions.toTypedArray(),
) : BaseActionSelector3<RamenActionSelector.Option, RamenActionSelector.Context>() {

    companion object {
        const val DEBUG = false
        const val FORCE = 10000000.0

        val deafultOptions = buildList {
            val base = Option(
                status = 100,
                wisdom = 90,
                skillPt = 200,
                hp = 60,
                motivation = 1000,
                relation = 10000,
                outingRelation = 24000,
                hpKeep = 1000,
                risk = 350,
                region = setOf(RamenRegion.SAPPORO, RamenRegion.HAKODATE, RamenRegion.TOKYO),
            )
            add(base)
            add(
                base.copy(
                    speedOverride = 50,
                    wisdom = 200,
                    hp = 70,
                    hpKeep = 700,
                    risk = 300,
                    region = setOf(RamenRegion.NAKAYAMA, RamenRegion.HANSHIN, RamenRegion.KOKURA),
                )
            )
            add(
                base.copy(
                    wisdom = 90,
                    hp = 70,
                    hpKeep = 1200,
                    risk = 350,
                    region = setOf(RamenRegion.HAKODATE2, RamenRegion.TOKYO2, RamenRegion.HANSHIN2),
                )
            )
            add(
                base.copy(
                    wisdom = 100,
                    hp = 80,
                    hpKeep = 1100,
                    risk = 400,
                    region = setOf(RamenRegion.SAPPORO, RamenRegion.HAKODATE, RamenRegion.TOKYO),
                )
            )
        }

        val defaultGenerator = object : ActionSelectorGenerator {
            override fun generateSelector() = RamenActionSelector()
        }
    }

    @Serializable
    data class Option(
        val status: Int = 100,
        val speedOverride: Int? = null,
        override val wisdom: Int = 80,
        override val skillPt: Int = 1000,
        override val hp: Int = 400,
        override val motivation: Int = 1000,
        override val relation: Int = 10000,
        override val outingRelation: Int = 20000,
        override val hpKeep: Int = 900,
        override val risk: Int = 300,

        val region: Set<RamenRegion> = setOf(
            // Junior (Period 0)
            RamenRegion.SAPPORO,
            RamenRegion.HAKODATE,
            RamenRegion.TOKYO,
            // Classic (Period 1)
            RamenRegion.NAKAYAMA,
            RamenRegion.HANSHIN,
            RamenRegion.KOKURA,
            // Senior (Period 2)
            RamenRegion.HAKODATE2,
            RamenRegion.TOKYO2,
            RamenRegion.HANSHIN2,
            // Finals (Period 3)
            RamenRegion.FINALS2,
        ),
        val tastingScoreThreshold: Double = 15.0,
        val tastingSupportCountThreshold: Int = 2,
        val tastingFriendshipCountThreshold: Int = 1,
        val gaugeScore: Double = 200.0,
        val gaugeMaxScore: Double = 200.0,
    ) : SerializableActionSelectorGenerator, BaseOption {
        override val speed get() = speedOverride ?: status
        override val stamina get() = status
        override val power get() = status
        override val guts get() = status
        override val maxSleep get() = 0

        override fun generateSelector() = RamenActionSelector(this)
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    class Context(
        option: Option,
        state: SimulationState,
    ) : BaseContext<Option>(option, state)

    override fun getContext(state: SimulationState): Context {
        val option = when {
            state.turn <= 24 -> options[0]
            state.turn <= 48 -> options.getOrElse(1) { options[0] }
            state.turn <= 72 -> options.getOrElse(2) { options[0] }
            else -> options.getOrElse(3) { options[0] }
        }
        return Context(option, state)
    }

    override fun calcScenarioScore(
        context: Context,
        action: Action,
        scenarioActionParam: ScenarioActionParam?
    ): Double {
        val param = scenarioActionParam as? RamenActionParam ?: return 0.0
        val option = context.option
        val ramenStatus = context.state.ramenStatus ?: return 0.0

        val baseNoodle = ramenStatus.baseGauge.noodleGauge
        val baseSoup = ramenStatus.baseGauge.soupGauge
        val baseTopping = ramenStatus.baseGauge.toppingGauge

        var score = (param.noodleGauge + param.soupGauge + param.toppingGauge) * option.gaugeScore
        if (baseNoodle + param.noodleGauge >= 7) score += option.gaugeMaxScore
        if (baseSoup + param.soupGauge >= 7) score += option.gaugeMaxScore
        if (baseTopping + param.toppingGauge >= 7) score += option.gaugeMaxScore

        return score
    }

    override fun selectFromScore(context: Context): Pair<Action, Double> {
        val state = context.state
        var selected = context.selectionWithScore.maxBy { it.second }

        // レースで体力50以下になる場合はお休み
        (selected.first as? Race)?.let { race ->
            if (state.status.hp + race.result.status.hp <= 50) {
                context.selectionWithScore.firstOrNull { it.first is Sleep }?.let {
                    if (it.second > 0.0) {
                        selected = it
                    }
                }
            }
        }

        // お休みで友人お出かけが可能な場合は友人お出かけ
        if (selected.first is Sleep && !isFriendOutingBlocked(state)) {
            val supportOuting = context.selectionWithScore.firstOrNull {
                (it.first as? Outing)?.support != null
            }
            if (supportOuting != null) {
                selected = supportOuting
            }
        }
        return selected
    }

    private fun isFriendOutingBlocked(state: SimulationState): Boolean {
        val ramenStatus = state.ramenStatus ?: return true
        // 隠し味がある場合は回避
        if (ramenStatus.hiddenTips > 2) return true
        val turn = state.turn
        // ジュニアでは友人お出かけは行わない
        if (turn <= 24) return true
        if (turn in 25..48) {
            // クラシックでは友人お出かけは3回まで
            val count = state.member.filter { it.outingType }.sumOf {
                maxOf(0, (it.supportState?.outingStep ?: 0) - 2)
            }
            if (count >= 3) return true
        }
        return false
    }

    override suspend fun calcScenarioActionScore(
        context: Context,
        action: Action
    ): Double? {
        return when (action) {
            is RamenSelectRegion -> {
                if (context.option.region.contains(action.region)) 10000.0 else 0.0
            }

            is RamenTasting -> calcTastingScore(context, action)

            else -> null
        }
    }

    private fun calcTastingScore(context: Context, action: RamenTasting): Double {
        val (option, state) = context
        val ramenStatus = state.ramenStatus ?: return 0.0
        val region = action.region
        val turn = state.turn

        // ジュニアでは隠し味を使わない
        if (turn <= 24 && action.changeHiddenTips.isNotEmpty()) return 0.0

        val maxActionPair = context.maxTurnChangeAction ?: return 0.0
        val maxAction = maxActionPair.first
        val maxScore = maxActionPair.second

        val isYearEnd = turn == 24 || turn == 48 || turn == 72
        val isSummerCamp = turn in 37..40 || turn in 61..64
        val isSenior = turn in 49..72

        var shouldTaste = false
        var reasonScore = 0.0

        // FIXME maxActionをそのまま使用するのではなく、試食会の失敗率低下やキャラ追加を踏まえて、再計算が必要
        // FIXME 1枚編成のトレーニングは優先する、特にクラシックの賢さ
        if (maxAction is Training) {
            val isMatch = region.targetAll || region.targetTypes.contains(maxAction.type)
            if (isMatch) {
                val supportCount = maxAction.member.size
                val friendCount = maxAction.friendCount

                val isStrongTraining = maxScore >= option.tastingScoreThreshold ||
                        supportCount >= option.tastingSupportCountThreshold ||
                        friendCount >= option.tastingFriendshipCountThreshold ||
                        maxAction.friendTraining

                if (isStrongTraining) {
                    shouldTaste = true
                    reasonScore = 1000.0 + maxScore
                } else if (isSummerCamp) {
                    shouldTaste = true
                    reasonScore = 800.0 + maxScore
                } else if (isSenior && state.status.hp <= 50) {
                    shouldTaste = true
                    reasonScore = 900.0 + maxScore
                } else if (isYearEnd) {
                    shouldTaste = true
                    reasonScore = 500.0 + maxScore
                }
            }
        }

        val overHiddenTips = max(0, ramenStatus.hiddenTips - 2)
        if (!shouldTaste && isYearEnd && action.changeHiddenTips.size <= overHiddenTips) {
            shouldTaste = true
            reasonScore = 300.0
        }

        return if (shouldTaste) {
            val hiddenTipPenalty = action.changeHiddenTips.size * 10.0

            var baseScore = 100000.0 + reasonScore - hiddenTipPenalty + action.region.friendBonus

            // 試食会で隠し味は残り2個までは優先的に使う
            if (action.changeHiddenTips.size == overHiddenTips) {
                baseScore += 50.0
            }

            // 隠し味を使う場合、残りが少なくなるコツを変換する
            if (action.changeHiddenTips.isNotEmpty()) {
                val restTips = mutableMapOf(
                    RamenTipType.NOODLE to ramenStatus.noodle - action.region.noodle,
                    RamenTipType.SOUP to ramenStatus.soup - action.region.soup,
                    RamenTipType.TOPPING to ramenStatus.topping - action.region.topping,
                )
                action.changeHiddenTips.forEach {
                    val valueOrder = restTips.values.sorted().indexOf(restTips[it])
                    baseScore -= valueOrder * 10.0
                    restTips[it] = restTips[it]!! + 1
                }
            }

            baseScore
        } else {
            0.0
        }
    }
}

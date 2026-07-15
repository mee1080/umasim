package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.scenario.ramen.RamenCalculator
import io.github.mee1080.umasim.scenario.ramen.RamenRegion
import io.github.mee1080.umasim.scenario.ramen.RamenTipType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

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
                skillPt = 1000,
                hp = 600,
                motivation = 1000,
                relation = 10000,
                outingRelation = 24000,
                hpKeep = 1000,
                risk = 350,
            )
            add(base)
            add(
                base.copy(
                    speedOverride = 50,
                    wisdom = 90,
                    hp = 750,
                    hpKeep = 700,
                    risk = 300,
                )
            )
            add(
                base.copy(
                    wisdom = 90,
                    hp = 750,
                    hpKeep = 1200,
                    risk = 350,
                )
            )
            add(
                base.copy(
                    wisdom = 100,
                    hp = 600,
                    hpKeep = 1100,
                    risk = 400,
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

        val regionPriority: List<RamenRegion> = listOf(
            // Junior (Period 0)
            RamenRegion.SAPPORO,
            RamenRegion.HAKODATE,
            RamenRegion.TOKYO,
            RamenRegion.FUKUSHIMA,
            RamenRegion.NIIGATA,
            // Classic (Period 1)
            RamenRegion.KOKURA,
            RamenRegion.HANSHIN,
            RamenRegion.KYOTO,
            RamenRegion.CHUKYO,
            RamenRegion.NAKAYAMA,
            // Senior (Period 2)
            RamenRegion.SAPPORO2,
            RamenRegion.HAKODATE2,
            RamenRegion.KYOTO2,
            RamenRegion.HANSHIN2,
            RamenRegion.KOKURA2,
            RamenRegion.TOKYO2,
            RamenRegion.NIIGATA2,
            RamenRegion.FUKUSHIMA2,
            RamenRegion.CHUKYO2,
            RamenRegion.NAKAYAMA2,
            // Finals (Period 3)
            RamenRegion.FINALS1,
            RamenRegion.FINALS2,
            RamenRegion.FINALS3
        ),
        val tastingScoreThreshold: Double = 15.0,
        val tastingSupportCountThreshold: Int = 2,
        val tastingFriendshipCountThreshold: Int = 1,
        val gaugeScore: Double = 10.0,
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

        val diffNoodle = param.noodleGauge - baseNoodle
        val diffSoup = param.soupGauge - baseSoup
        val diffTopping = param.toppingGauge - baseTopping

        val totalGained = diffNoodle + diffSoup + diffTopping
        return totalGained * option.gaugeScore
    }

    override fun selectFromScore(context: Context): Pair<Action, Double> {
        val state = context.state
        var selected = context.selectionWithScore.maxBy { it.second }

        // レースで体力50以下になる場合はお休み
        (selected.first as? Race)?.let { race ->
            if (state.status.hp + race.result.status.hp <= 50) {
                context.selectionWithScore.firstOrNull { it.first is Sleep }?.let {
                    if (it.second > Double.MIN_VALUE) {
                        selected = it
                    }
                }
            }
        }

        // お休みで友人お出かけが可能な場合は友人お出かけ
        if (selected.first is Sleep) {
            val supportOuting = context.selectionWithScore.firstOrNull {
                (it.first as? Outing)?.support != null
            }
            if (supportOuting != null && supportOuting.second > Double.MIN_VALUE) {
                selected = supportOuting
            }
        }
        return selected
    }

    private fun isFriendOutingBlocked(state: SimulationState, action: Outing): Boolean {
        if (action.support == null) return false
        val turn = state.turn
        if (turn <= 24) return true // ジュニアでは友人お出かけは行わない
        if (turn in 25..48) {
            // クラシックでは友人お出かけは3回まで
            val count = state.member.filter { it.outingType }.sumOf {
                maxOf(0, (it.supportState?.outingStep ?: 0) - 2)
            }
            if (count >= 3) return true
        }
        return false
    }

    private fun isSleepOrOutingBlockedByHp(state: SimulationState, action: Action): Boolean {
        if (state.status.hp >= 70) {
            if (action is Sleep || action is Outing) {
                return true
            }
        }
        return false
    }

    override suspend fun calcScenarioActionScore(
        context: Context,
        action: Action
    ): Double? {
        val state = context.state
        if (action is Outing && isFriendOutingBlocked(state, action)) {
            return Double.MIN_VALUE
        }
        if (isSleepOrOutingBlockedByHp(state, action)) {
            return Double.MIN_VALUE
        }

        val turn = state.turn
        val ramenStatus = state.ramenStatus
        if (ramenStatus != null) {
            val totalTips = ramenStatus.tips.values.sum()
            val hiddenTips = ramenStatus.hiddenTips

            val isBeforeCamp = turn in 33..36 || turn in 57..60
            val isClassicEnd = turn in 45..48

            // ジュニアでは隠し味を使わない
            if (turn <= 24 && action is RamenTasting && action.changeHiddenTips.isNotEmpty()) {
                return Double.MIN_VALUE
            }

            if (isBeforeCamp || isClassicEnd) {
                if (action is RamenTasting) {
                    val usedHidden = action.changeHiddenTips.size
                    val remainingHidden = hiddenTips - usedHidden

                    if (hiddenTips >= 3) {
                        if (remainingHidden > 2) {
                            return Double.MIN_VALUE
                        }
                    } else {
                        if (usedHidden > 0) {
                            return Double.MIN_VALUE
                        }
                    }

                    if (isBeforeCamp) {
                        val spentNormalTips = action.region.noodle + action.region.soup + action.region.topping - usedHidden
                        val remainingNormalTips = totalTips - spentNormalTips
                        if (remainingNormalTips < 7) {
                            if (hiddenTips >= 3 && remainingHidden <= 2) {
                                // 隠し味を減らすことを優先し許容する
                            } else {
                                return Double.MIN_VALUE
                            }
                        }
                    }
                } else {
                    if (hiddenTips >= 3) {
                        val tastings = RamenCalculator.predictScenarioAction(state, false)
                        val hasValidTasting = tastings.any { act ->
                            act is RamenTasting && (hiddenTips - act.changeHiddenTips.size <= 2)
                        }
                        if (hasValidTasting) {
                            return Double.MIN_VALUE
                        }
                    }
                }
            }
        }

        if (action is RamenSelectRegion) {
            val option = context.option
            val region = action.region
            val index = option.regionPriority.indexOf(region)
            return if (index >= 0) {
                10000.0 - index
            } else {
                0.0
            }
        }
        if (action is RamenTasting) {
            val score = calcTastingScore(context, action)
            return if (score == Double.MIN_VALUE) null else score
        }
        return null
    }

    private fun calcTastingScore(context: Context, action: RamenTasting): Double {
        val (option, state) = context
        val region = action.region
        val turn = state.turn

        if (turn >= 73) return Double.MIN_VALUE

        val maxActionPair = context.maxTurnChangeAction ?: return Double.MIN_VALUE
        val maxAction = maxActionPair.first
        val maxScore = maxActionPair.second

        val isYearEnd = turn == 24 || turn == 48 || turn == 72
        val isSummerCamp = turn in 37..40 || turn in 61..64
        val isSenior = turn in 49..72

        var shouldTaste = false
        var reasonScore = 0.0

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

        if (!shouldTaste && isYearEnd) {
            shouldTaste = true
            reasonScore = 300.0
        }

        return if (shouldTaste) {
            val hiddenTipPenalty = action.changeHiddenTips.size * 10.0
            val regionPriorityIndex = option.regionPriority.indexOf(region)
            val priorityBonus = if (regionPriorityIndex >= 0) (100 - regionPriorityIndex) * 0.1 else 0.0

            var baseScore = reasonScore - hiddenTipPenalty + priorityBonus

            // 試食会で隠し味は残り2個までは優先的に使う
            val remainingHidden = (state.ramenStatus?.hiddenTips ?: 0) - action.changeHiddenTips.size
            if (action.changeHiddenTips.isNotEmpty() && remainingHidden >= 2) {
                baseScore += 50.0
            }

            // 隠し味を使う場合、残りが少なくなるコツを変換する
            if (action.changeHiddenTips.isNotEmpty()) {
                val sumRemainingConverted = action.changeHiddenTips.distinct().sumOf { type ->
                    val totalTips = state.ramenStatus?.tips?.get(type) ?: 0
                    val convertCount = action.changeHiddenTips.count { it == type }
                    totalTips - convertCount
                }
                baseScore += (10.0 - sumRemainingConverted * 1.0)
            }

            baseScore
        } else {
            Double.MIN_VALUE
        }
    }
}

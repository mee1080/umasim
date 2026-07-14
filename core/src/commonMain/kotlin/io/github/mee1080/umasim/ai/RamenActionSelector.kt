package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.ramen.RamenRegion
import io.github.mee1080.umasim.scenario.ramen.RamenTipType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

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
    }

    @Serializable
    data class Option(
        val status: Int = 100,
        override val wisdom: Int = 80,
        override val skillPt: Int = 1000,
        override val hp: Int = 400,
        override val motivation: Int = 1000,
        override val relation: Int = 10000,
        override val outingRelation: Int = 20000,
        override val hpKeep: Int = 900,
        override val risk: Int = 300,

        val regionPriority: List<String> = listOf(
            // Junior (Period 0)
            "SAPPORO", "HAKODATE", "TOKYO", "FUKUSHIMA", "NIIGATA",
            // Classic (Period 1)
            "KOKURA", "HANSHIN", "KYOTO", "CHUKYO", "NAKAYAMA",
            // Senior (Period 2)
            "SAPPORO2", "HAKODATE2", "KYOTO2", "HANSHIN2", "KOKURA2", "TOKYO2", "NIIGATA2", "FUKUSHIMA2", "CHUKYO2", "NAKAYAMA2",
            // Finals (Period 3)
            "FINALS1", "FINALS2", "FINALS3"
        ),
        val tastingScoreThreshold: Double = 15.0,
        val tastingSupportCountThreshold: Int = 2,
        val tastingFriendshipCountThreshold: Int = 1,
        val gaugeScore: Double = 10.0,
    ) : SerializableActionSelectorGenerator, BaseOption {
        override val speed get() = status
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
                    selected = it
                }
            }
        }

        // お休みで友人お出かけが可能な場合は友人お出かけ
        if (selected.first is Sleep) {
            val supportOuting = context.selectionWithScore.firstOrNull {
                (it.first as? Outing)?.support != null
            }
            if (supportOuting != null) {
                selected = supportOuting
            }
        }
        return selected
    }

    override suspend fun calcScenarioActionScore(
        context: Context,
        action: Action
    ): Double? {
        if (action is RamenSelectRegion) {
            val option = context.option
            val region = action.region
            val index = option.regionPriority.indexOf(region.name)
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
            val regionPriorityIndex = option.regionPriority.indexOf(region.name)
            val priorityBonus = if (regionPriorityIndex >= 0) (100 - regionPriorityIndex) * 0.1 else 0.0

            reasonScore - hiddenTipPenalty + priorityBonus
        } else {
            Double.MIN_VALUE
        }
    }
}

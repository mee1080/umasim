package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.ramen.RamenRegion
import io.github.mee1080.umasim.scenario.ramen.RamenStatus
import io.github.mee1080.umasim.scenario.ramen.RamenTipType
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.averageOf
import kotlinx.serialization.Serializable
import kotlin.math.max

open class RamenActionSelector(
    vararg val options: Option = s2h2w1.toTypedArray(),
) : BaseActionSelector3<RamenActionSelector.Option, RamenActionSelector.Context>() {

    companion object {
        const val DEBUG = false

        val s2h2w1 = buildList {
            val base = Option(
                status = 100,
                wisdom = 90,
                skillPt = 200,
                hp = 100,
                motivation = 1000,
                relation = 9000,
                outingRelation = 9000,
                hpKeep = 500,
                risk = 225,
                tastingThreashold = 500,
                speedTastingFactor = 120,
                staminaTastingFactor = 120,
                wisdomTastingFactor = 130,
                tastingMinFailureRate = 10,
                gaugeScore = 100,
                gaugeMaxScore = 7500,
                regions = setOf(
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
            )
            add(base)
            add(
                base.copy(
                    wisdom = 60,
                    hp = 90,
                    hpKeep = 250,
                    risk = 175,
                    tastingThreashold = 500,
                    allTastingFactor = 70,
                    staminaTastingFactor = 70,
                    wisdomTastingFactor = 100,
                    tastingMinFailureRate = 30,
                    gaugeScore = 0,
                    gaugeMaxScore = 0,
                )
            )
            add(
                base.copy(
                    wisdom = 75,
                    hp = 95,
                    hpKeep = 50,
                    risk = 50,
                    tastingThreashold = 600,
                    speedTastingFactor = 100,
                    staminaTastingFactor = 90,
                    wisdomTastingFactor = 140,
                    tastingMinFailureRate = 100,
                    gaugeScore = 700,
                    gaugeMaxScore = 1500,
                )
            )
            add(
                base.copy(
                    skillPt = 1000,
                    hp = 0,
                    motivation = 0,
                    hpKeep = 0,
                    risk = 0,
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
        override val wisdom: Int = 80,
        override val skillPt: Int = 100,
        override val hp: Int = 400,
        override val motivation: Int = 1000,
        override val relation: Int = 10000,
        override val outingRelation: Int = 20000,
        override val hpKeep: Int = 900,
        override val risk: Int = 300,
        val tastingThreashold: Int = 700,
        val allTastingFactor: Int = 70,
        val speedTastingFactor: Int = 100,
        val staminaTastingFactor: Int = 80,
        val powerTastingFactor: Int = 0,
        val gutsTastingFactor: Int = 0,
        val wisdomTastingFactor: Int = 150,
        val tastingMinFailureRate: Int = 10,
        val gaugeScore: Int = 200,
        val gaugeMaxScore: Int = 2000,
        val regions: Set<RamenRegion> = emptySet(),
    ) : SerializableActionSelectorGenerator, BaseOption {
        override val speed get() = status
        override val stamina get() = status
        override val power get() = status
        override val guts get() = status
        override val maxSleep get() = 0

        val typeTastingFactor = mapOf(
            StatusType.SPEED to speedTastingFactor,
            StatusType.STAMINA to staminaTastingFactor,
            StatusType.POWER to powerTastingFactor,
            StatusType.GUTS to gutsTastingFactor,
            StatusType.WISDOM to wisdomTastingFactor,
        )

        override fun generateSelector() = RamenActionSelector(this)
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    class Context(
        option: Option,
        state: SimulationState,
    ) : BaseContext<Option>(option, state) {
        val noPositionMember = state.support.filter { it.position == StatusType.NONE }
        val friendOutingCount = state.member.first { it.outingType }.supportState?.let {
            maxOf(0, it.outingStep - 2)
        } ?: 0
        val tastingScore = mutableMapOf<RamenRegion, Double>()
        var tastingThreasholdFactor = -1.0
    }

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

        return score.toDouble()
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
        if (selected.first is Sleep && !isFriendOutingBlocked(context)) {
            val supportOuting = context.selectionWithScore.firstOrNull {
                (it.first as? Outing)?.support != null
            }
            if (supportOuting != null) {
                selected = supportOuting
            }
        }

        // お出かけ消費
        val ramenStatus = state.scenarioStatus as? RamenStatus ?: return selected
        val ramenCanOuting = !context.lastTurnOfYear &&
                ramenStatus.hiddenTips <= 2 && ramenStatus.activeTastingRegion == null
        if (ramenCanOuting && ((selected.first as? Race)?.goal == false || (selected.first is Training && state.status.hp < 65))) {
            val minOuting = when {
                state.turn >= 65 -> 5
                state.turn >= 55 -> 4
                state.turn >= 49 -> 3
                state.turn >= 41 -> 2
                state.turn >= 31 -> 1
                else -> 0
            }
            if (context.friendOutingCount < minOuting) {
                val supportOuting = context.selectionWithScore.firstOrNull {
                    (it.first as? Outing)?.support != null
                }
                if (supportOuting != null) {
                    selected = supportOuting
                }
            }
        }

        return selected
    }

    private fun isFriendOutingBlocked(context: Context): Boolean {
        val ramenStatus = context.state.ramenStatus ?: return true
        // 隠し味がある場合は回避
        if (ramenStatus.hiddenTips > 2) return true
        val turn = context.state.turn
        // ジュニアでは友人お出かけは行わない
        if (turn <= 24) return true
        // クラシックでは友人お出かけは3回まで
        if (turn in 25..48) context.friendOutingCount >= 3
        return false
    }

    override suspend fun calcScenarioActionScore(
        context: Context,
        action: Action
    ): Double? {
        val tasting = context.state.ramenStatus?.activeTastingRegion?.first
        if (tasting != null) {
            when (action) {
                is Training -> {
                    if (!tasting.targetAll && !tasting.targetTypes.contains(action.type)) {
                        return 0.0
                    }
                }

                else -> return 0.0
            }
        }
        return when (action) {
            is RamenSelectRegion -> {
                if (context.option.regions.contains(action.region)) 10000.0 else 0.0
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

        if (context.tastingThreasholdFactor < 0.0) {
            val currentTips = ramenStatus.tips.values.sum()
            val actionTips = context.maxTurnChangeAction?.let {
                val param = it.first.candidates.first().first.scenarioActionParam as? RamenActionParam ?: return@let 0
                var count = 0
                if (ramenStatus.noodle + param.noodleGauge >= 7) count++
                if (ramenStatus.soup + param.soupGauge >= 7) count++
                if (ramenStatus.topping + param.toppingGauge >= 7) count++
                return@let count
            } ?: 0
            val noTastingTotalTips = currentTips + actionTips
            context.tastingThreasholdFactor = when {
                // 年末または溢れる場合は試食会
                context.lastTurnOfYear || noTastingTotalTips > 10 -> 0.0

                // 次のターンが年末の場合は溢れないよう調整
                context.isLastTurnOfYear(turn + 1) && noTastingTotalTips >= 5 -> 0.0

                // 次のターンが夏合宿の場合は調整
                context.lastTurnBeforeLevelUp -> {
                    when {
                        // 隠し味が溢れるなら試食会
                        ramenStatus.hiddenTips > 2 -> 0.0

                        // コツ7以下ならば条件を厳しくする
                        noTastingTotalTips <= 7 -> 1.5

                        else -> 1.0
                    }
                }

                // 夏合宿中に隠し味が溢れる場合は試食会
                turn in 37..39 || turn in 61..63 -> {
                    if (ramenStatus.hiddenTips > 2) 0.0 else 1.0
                }

                // 絆不足中は条件を緩くする
                turn <= 24 && state.member.count { it.relation < it.card.requiredRelation } >= 3 -> 0.3

                else -> 1.0
            }
            if (DEBUG) println("tastingThreasholdFactor: ${context.tastingThreasholdFactor}")
        }
        var score = context.tastingScore.getOrPut(region) {
            val needRelation = if (turn <= 24) 70 else 80
            var maxEntry = StatusType.NONE to option.tastingThreashold * context.tastingThreasholdFactor - 0.0001
            (if (region.targetAll) trainingType.asList() else region.targetTypes).forEachIndexed { index, type ->
                val training = context.selectionWithScore.map { it.first }
                    .firstOrNull { it is Training && it.type == type } as? Training ?: return@forEachIndexed
                if (training.failureRate > option.tastingMinFailureRate) return@forEachIndexed
                var typeScore = training.member.filter { !it.guest }.sumOf {
                    if (it.card.type == type && it.relation >= needRelation) 3.0 else 1.0
                }
                val memberCandidates = when {
                    region.addMember == 0 -> emptyList()
                    index < context.noPositionMember.size -> context.noPositionMember
                    else -> state.support.filter { it.position != type && !it.outingType }
                }
                if (memberCandidates.isNotEmpty()) {
                    typeScore += memberCandidates.averageOf {
                        if (it.card.type == type && it.relation >= needRelation) 3.0 else 1.0
                    }
                }
                val factor = if (region.targetAll) option.allTastingFactor else option.typeTastingFactor[type]!!
                typeScore *= factor
                if (DEBUG) println("${region.regionName} ${type.displayName} $typeScore")
                if (typeScore > maxEntry.second) maxEntry = type to typeScore
            }
            if (maxEntry.first == StatusType.NONE) -1.0 else {
                maxEntry.second * 5000.0 + region.friendBonus
            }
        }
        if (score < 0.0) return 0.0

        // お出かけが残っていれば隠し味2個までは優先的に使用
        if (context.friendOutingCount < 5 && action.changeHiddenTips.size == max(0, ramenStatus.hiddenTips - 2)) {
            score += 100.0
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
                score -= (valueOrder + 1) * 10.0
                restTips[it] = restTips[it]!! + 1
            }
        }

        return score
    }
}

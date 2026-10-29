package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.MemberState
import io.github.mee1080.umasim.simulation2.ScenarioMemberState
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState
import kotlin.math.max
import kotlin.math.min

/**
 * @param performance 担当演目（未割り当ての場合はnull）
 */
data class ShuntaisaiMemberState(
    val performance: StatusType? = null,
) : ScenarioMemberState(Scenario.SHUNTAISAI) {
    override fun toShortString() = "ShuntaisaiMemberState(performance=$performance)"
}

/**
 * 担当演目
 *
 * 割り当て済みならその演目、未割り当てならシナリオリンクキャラは固定の演目、それ以外はサポカのタイプ
 */
val MemberState.shuntaisaiPerformance: StatusType?
    get() = (scenarioState as? ShuntaisaiMemberState)?.performance ?: defaultShuntaisaiPerformance

private val MemberState.defaultShuntaisaiPerformance: StatusType?
    get() = (if (isScenarioLink) shuntaisaiLinkPerformance[charaName] else null)
        ?: card.type.takeIf { trainingType.contains(it) }

/**
 * 編成サポカの担当演目の割り当て
 *
 * * シナリオリンクキャラは固定の演目
 * * それ以外はサポカのタイプの演目、ただし1演目に[shuntaisaiMaxSupportPerPerformance]人まで
 * * 溢れたサポカは、[shuntaisaiOverflowPerformances]の順に空きがある演目に割り振る
 *   * 溢れるサポカの選択はランダム
 */
fun assignShuntaisaiPerformances(member: List<MemberState>): Map<Int, StatusType> {
    val result = mutableMapOf<Int, StatusType>()
    member.forEach {
        if (it.guest || !it.isScenarioLink) return@forEach
        val performance = shuntaisaiLinkPerformance[it.charaName] ?: return@forEach
        result[it.index] = performance
    }
    fun count(type: StatusType) = result.values.count { it == type }
    member.filter { !it.guest && !result.containsKey(it.index) && trainingType.contains(it.card.type) }
        .shuffled()
        .forEach {
            val candidates = listOf(it.card.type) + shuntaisaiOverflowPerformances.getValue(it.card.type)
            val target = candidates.firstOrNull { type -> count(type) < shuntaisaiMaxSupportPerPerformance }
                ?: trainingType.firstOrNull { type -> count(type) < shuntaisaiMaxSupportPerPerformance }
                ?: it.card.type
            result[it.index] = target
        }
    return result
}

/**
 * ゲスト（編成外の演目担当）の担当演目
 *
 * 各演目に1人ずつ割り振り、残りは担当人数が少ない演目（同数の場合は先頭）から順に割り振る
 */
fun calcShuntaisaiGuestPerformances(currentCounts: Map<StatusType, Int>, guestCount: Int): List<StatusType> {
    val counts = trainingType.associateWith { currentCounts[it] ?: 0 }.toMutableMap()
    val result = mutableListOf<StatusType>()
    repeat(guestCount) { index ->
        val target = if (index < trainingType.size) trainingType[index] else {
            trainingType.minBy { counts.getValue(it) }
        }
        counts[target] = counts.getValue(target) + 1
        result += target
    }
    return result
}

fun SimulationState.updateShuntaisaiStatus(update: ShuntaisaiStatus.() -> ShuntaisaiStatus): SimulationState {
    val shuntaisaiStatus = this.shuntaisaiStatus ?: return this
    return copy(scenarioStatus = shuntaisaiStatus.update())
}

/**
 * 演目に割り当てられた教え
 *
 * @param transformedFrom 転成で消費した教え（効果は転成効果に変化する）
 */
data class ShuntaisaiTeachingSlot(
    val teaching: ShuntaisaiTeaching,
    val transformedFrom: ShuntaisaiTeaching? = null,
) {
    fun effect(level: Int) =
        teaching.effect(level) + (transformedFrom?.transformEffect ?: ShuntaisaiTeachingEffect.Empty)

    val displayName
        get() = if (transformedFrom == null) teaching.displayName else {
            "${teaching.displayName}(${transformedFrom.displayName})"
        }
}

/**
 * 駿大祭シナリオ固有の状態を保持するクラス。
 *
 * @param joryokuSupportCount 助力の支えの残り回数
 * @param campTurns 駿大祭合宿のターン（目標レースによる延長を含む）
 */
data class ShuntaisaiStatus(
    val turn: Int = 1,
    val campTurns: List<Int> = shuntaisaiCampTurns,
    val maxLevel: Int = shuntaisaiDefaultMaxLevel,
    val gauges: Map<StatusType, Int> = trainingType.associateWith { 0 },
    val levels: Map<StatusType, Int> = trainingType.associateWith { 1 },
    val teachings: Map<StatusType, ShuntaisaiTeachingSlot> = emptyMap(),
    val presentationResults: List<ShuntaisaiPresentationResult> = emptyList(),
    val presentationBonus: ShuntaisaiPresentationBonus = ShuntaisaiPresentationBonus.Initial,
    val supportTypeCount: Int = 0,
    val joryokuSupportCount: Int = 0,
) : ScenarioStatus {

    override fun toShortString() = buildString {
        append("演目Lv: ${trainingType.joinToString("/") { "${levels[it]}" }}")
        append(", ゲージ: ${trainingType.joinToString("/") { "${gauges[it]}" }}")
        append(", 教え: ${trainingType.joinToString("/") { teachings[it]?.displayName ?: "-" }}")
        if (joryokuSupportCount > 0) {
            append(", 助力の支え: $joryokuSupportCount")
        }
        if (presentationResults.isNotEmpty()) {
            append(", 発表会: ${presentationResults.joinToString("/") { it.displayName }}")
        }
    }

    val period = (turn - 1) / 24

    val isCamp get() = campTurns.contains(turn)

    /**
     * 合宿最終ターンか
     */
    fun isCampEnd(turn: Int) = campTurns.contains(turn) && !campTurns.contains(turn + 1)

    val isFinals get() = turn >= shuntaisaiFinalsStartTurn

    val minLevel get() = levels.values.minOrNull() ?: 0

    fun gauge(type: StatusType) = gauges[type] ?: 0

    fun level(type: StatusType) = levels[type] ?: 0

    fun isGaugeMax(type: StatusType) = gauge(type) >= shuntaisaiMaxGauge

    /**
     * 演目稽古になっているか
     */
    fun isPractice(type: StatusType) = trainingType.contains(type) && (isCamp || isFinals || isGaugeMax(type))

    /**
     * 演目に割り当てられた教えの効果（演目Lvに応じた値）
     */
    fun teachingEffect(type: StatusType) =
        teachings[type]?.effect(level(type)) ?: ShuntaisaiTeachingEffect.Empty

    /**
     * トレーニング参加メンバーによる教えの効果
     *
     * 演目担当1人ごとに、担当演目に割り当てられた教えの効果が加算される
     */
    fun memberTeachingEffect(member: List<MemberState>): ShuntaisaiTeachingEffect {
        return member.fold(ShuntaisaiTeachingEffect.Empty) { acc, memberState ->
            val type = memberState.shuntaisaiPerformance
            if (type != null) acc + teachingEffect(type) else acc
        }
    }

    fun practiceEffect(type: StatusType) = shuntaisaiPracticeEffect(level(type))

    /**
     * 演目稽古の基本上昇量（時期で決まる、表示上のトレーニングLvは演目Lv）
     */
    fun practiceTraining(type: StatusType) =
        shuntaisaiPracticeTraining(type, period, level(type))

    /**
     * サポカのタイプを5種類以上編成している場合の演目稽古効果が有効か
     */
    val allTypeBonus get() = supportTypeCount >= 5

    val joryokuSupportEffect get() = if (joryokuSupportCount > 0) shuntaisaiJoryokuSupportEffect else 0

    fun teachingTarget(teaching: ShuntaisaiTeaching) = teachings.entries.firstOrNull { it.value.teaching == teaching }?.key

    /**
     * 演目ゲージ上昇（合宿中は上昇しない）
     */
    fun addGauge(type: StatusType, value: Int): ShuntaisaiStatus {
        if (isCamp || value <= 0 || !trainingType.contains(type)) return this
        return copy(gauges = gauges + (type to min(shuntaisaiMaxGauge, gauge(type) + value)))
    }

    fun addGauges(values: Map<StatusType, Int>): ShuntaisaiStatus {
        return values.entries.fold(this) { status, (type, value) -> status.addGauge(type, value) }
    }

    /**
     * 演目Lvとゲージが最も低い演目（同値の場合は先頭）、ゲージ最大の演目は対象外
     */
    fun lowestGaugeTarget(): StatusType? {
        return trainingType.filter { !isGaugeMax(it) }.minWithOrNull(compareBy({ level(it) }, { gauge(it) }))
    }

    /**
     * 演目Lvとゲージが低い演目から順に1ずつゲージ上昇
     */
    fun addLowestGauge(count: Int = 1): ShuntaisaiStatus {
        var status = this
        repeat(count) {
            val target = status.lowestGaugeTarget() ?: return status
            status = status.addGauge(target, 1)
        }
        return status
    }

    /**
     * [addLowestGauge]によるゲージ上昇量
     */
    fun calcLowestGaugeUp(count: Int): Map<StatusType, Int> {
        val added = addLowestGauge(count)
        return trainingType.associateWith { added.gauge(it) - gauge(it) }.filterValues { it > 0 }
    }

    fun levelUp(type: StatusType, value: Int = 1): ShuntaisaiStatus {
        return copy(levels = levels + (type to min(maxLevel, level(type) + value)))
    }

    /**
     * 最も低い演目Lv+1（吾妻お出かけ）
     */
    fun levelUpLowest(): ShuntaisaiStatus {
        val target = trainingType.minByOrNull { level(it) } ?: return this
        return levelUp(target)
    }

    /**
     * 全演目Lv+1（合宿終了時）
     */
    fun levelUpAll(): ShuntaisaiStatus {
        return trainingType.fold(this) { status, type -> status.levelUp(type) }
    }

    /**
     * 演目稽古実行
     *
     * 合宿中は演目Lvが上昇せず、ゲージもリセットされない（合宿終了後に全演目Lv+1）
     */
    fun executePractice(type: StatusType): ShuntaisaiStatus {
        if (!isPractice(type) || isCamp) return this
        return levelUp(type).copy(gauges = gauges + (type to 0))
    }

    /**
     * トレーニング実行時の助力の支えの消費と獲得
     */
    fun updateJoryokuSupport(gain: Boolean): ShuntaisaiStatus {
        return copy(
            joryokuSupportCount = if (gain) shuntaisaiJoryokuSupportCount else max(0, joryokuSupportCount - 1),
        )
    }

    fun assignTeaching(
        teaching: ShuntaisaiTeaching,
        target: StatusType,
        transformedFrom: ShuntaisaiTeaching? = null,
    ): ShuntaisaiStatus {
        return copy(teachings = teachings + (target to ShuntaisaiTeachingSlot(teaching, transformedFrom)))
    }

    fun transformTeaching(teaching: ShuntaisaiTeaching, target: StatusType): ShuntaisaiStatus {
        val consumed = teachings[target]?.teaching
        return copy(teachings = teachings + (target to ShuntaisaiTeachingSlot(teaching, consumed)))
    }

    /**
     * 転成で消費可能な教えが割り当てられている演目
     *
     * 消費できるのは未転成のジュニアの教えのみ
     */
    fun transformTargets(teaching: ShuntaisaiTeaching): List<StatusType> {
        return trainingType.filter {
            val slot = teachings[it]
            slot == null || (slot.transformedFrom == null && slot.teaching.period == 0 && teaching.period > 0)
        }
    }

    fun judgePresentation(index: Int): ShuntaisaiPresentationResult {
        return if (minLevel >= shuntaisaiGreatTargetLevel[index]) {
            ShuntaisaiPresentationResult.GREAT_SUCCESS
        } else {
            ShuntaisaiPresentationResult.SUCCESS
        }
    }
}

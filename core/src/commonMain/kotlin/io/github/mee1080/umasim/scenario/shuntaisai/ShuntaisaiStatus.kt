package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState
import kotlin.math.min

fun SimulationState.updateShuntaisaiStatus(update: ShuntaisaiStatus.() -> ShuntaisaiStatus): SimulationState {
    val shuntaisaiStatus = this.shuntaisaiStatus ?: return this
    return copy(scenarioStatus = shuntaisaiStatus.update())
}

/**
 * 演目に割り当てられた教え
 *
 * @param transformedFrom 転成で消費した教え
 */
data class ShuntaisaiTeachingSlot(
    val teaching: ShuntaisaiTeaching,
    val transformedFrom: ShuntaisaiTeaching? = null,
) {
    // TODO 転成効果の内容不明のため、消費した教えの効果がそのまま残るものとして扱う
    val effect get() = teaching.effect + (transformedFrom?.effect ?: ShuntaisaiTeachingEffect.Empty)

    val displayName
        get() = if (transformedFrom == null) teaching.displayName else {
            "${teaching.displayName}(${transformedFrom.displayName})"
        }
}

/**
 * 駿大祭シナリオ固有の状態を保持するクラス。
 */
data class ShuntaisaiStatus(
    val turn: Int = 1,
    val maxLevel: Int = shuntaisaiDefaultMaxLevel,
    val gauges: Map<StatusType, Int> = trainingType.associateWith { 0 },
    // TODO 演目Lvの初期値不明
    val levels: Map<StatusType, Int> = trainingType.associateWith { 1 },
    val teachings: Map<StatusType, ShuntaisaiTeachingSlot> = emptyMap(),
    val presentationResults: List<ShuntaisaiPresentationResult> = emptyList(),
    val presentationBonus: ShuntaisaiPresentationBonus = ShuntaisaiPresentationBonus.Empty,
    val supportTypeCount: Int = 0,
) : ScenarioStatus {

    override fun toShortString() = buildString {
        append("演目Lv: ${trainingType.joinToString("/") { "${levels[it]}" }}")
        append(", ゲージ: ${trainingType.joinToString("/") { "${gauges[it]}" }}")
        append(", 教え: ${trainingType.joinToString("/") { teachings[it]?.displayName ?: "-" }}")
        if (presentationResults.isNotEmpty()) {
            append(", 発表会: ${presentationResults.joinToString("/") { it.displayName }}")
        }
    }

    val period = (turn - 1) / 24

    val isCamp get() = shuntaisaiCampTurns.contains(turn)

    val minLevel get() = levels.values.minOrNull() ?: 0

    fun gauge(type: StatusType) = gauges[type] ?: 0

    fun level(type: StatusType) = levels[type] ?: 0

    fun isGaugeMax(type: StatusType) = gauge(type) >= shuntaisaiMaxGauge

    /**
     * 演目稽古になっているか
     */
    fun isPractice(type: StatusType) = trainingType.contains(type) && (isCamp || isGaugeMax(type))

    fun teachingEffect(type: StatusType) = teachings[type]?.effect ?: ShuntaisaiTeachingEffect.Empty

    fun practiceStatus(type: StatusType) = shuntaisaiPracticeStatus(period, level(type))

    /**
     * シニアでサポカのタイプを5種類以上編成している場合のバフ
     */
    val seniorAllTypeBonus get() = period >= 2 && supportTypeCount >= 5

    fun teachingTarget(teaching: ShuntaisaiTeaching) = teachings.entries.firstOrNull { it.value.teaching == teaching }?.key

    fun addGauge(type: StatusType, value: Int): ShuntaisaiStatus {
        if (value <= 0 || !trainingType.contains(type)) return this
        return copy(gauges = gauges + (type to min(shuntaisaiMaxGauge, gauge(type) + value)))
    }

    fun addGauges(values: Map<StatusType, Int>): ShuntaisaiStatus {
        return values.entries.fold(this) { status, (type, value) -> status.addGauge(type, value) }
    }

    /**
     * 最も低い演目ゲージ+1
     *
     * TODO お休み、目標外レース時にどの演目が上昇するか不明
     */
    fun addLowestGauge(): ShuntaisaiStatus {
        val target = trainingType.minByOrNull { gauge(it) } ?: return this
        return addGauge(target, 1)
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
     * 演目稽古実行
     *
     * TODO 合宿中の演目稽古でゲージがリセットされるか不明
     */
    fun executePractice(type: StatusType): ShuntaisaiStatus {
        if (!isPractice(type)) return this
        return levelUp(type).copy(gauges = gauges + (type to 0))
    }

    fun assignTeaching(teaching: ShuntaisaiTeaching, target: StatusType): ShuntaisaiStatus {
        return copy(teachings = teachings + (target to ShuntaisaiTeachingSlot(teaching)))
    }

    fun transformTeaching(teaching: ShuntaisaiTeaching, target: StatusType): ShuntaisaiStatus {
        val consumed = teachings[target]?.teaching
        return copy(teachings = teachings + (target to ShuntaisaiTeachingSlot(teaching, consumed)))
    }

    /**
     * 転成で消費可能な教えが割り当てられている演目
     */
    fun transformTargets(teaching: ShuntaisaiTeaching): List<StatusType> {
        return trainingType.filter {
            val slot = teachings[it]
            slot == null || (slot.transformedFrom == null && slot.teaching.period < teaching.period)
        }
    }

    fun judgePresentation(index: Int): ShuntaisaiPresentationResult {
        return when {
            minLevel >= shuntaisaiGreatTargetLevel[index] -> ShuntaisaiPresentationResult.GREAT_SUCCESS
            minLevel >= shuntaisaiTargetLevel[index] -> ShuntaisaiPresentationResult.SUCCESS
            else -> ShuntaisaiPresentationResult.FAILURE
        }
    }
}

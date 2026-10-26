package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.simulation2.MemberState
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState
import kotlin.math.max
import kotlin.math.min

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
 */
data class ShuntaisaiStatus(
    val turn: Int = 1,
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

    val isCamp get() = shuntaisaiCampTurns.contains(turn)

    val minLevel get() = levels.values.minOrNull() ?: 0

    fun gauge(type: StatusType) = gauges[type] ?: 0

    fun level(type: StatusType) = levels[type] ?: 0

    fun isGaugeMax(type: StatusType) = gauge(type) >= shuntaisaiMaxGauge

    /**
     * 演目稽古になっているか
     */
    fun isPractice(type: StatusType) = trainingType.contains(type) && (isCamp || isGaugeMax(type))

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
            val type = memberState.card.type
            if (trainingType.contains(type)) acc + teachingEffect(type) else acc
        }
    }

    fun practiceEffect(type: StatusType) = shuntaisaiPracticeEffect(level(type))

    /**
     * サポカのタイプを5種類以上編成している場合の演目稽古効果が有効か
     */
    val allTypeBonus get() = supportTypeCount >= 5

    val joryokuSupportEffect get() = if (joryokuSupportCount > 0) shuntaisaiJoryokuSupportEffect else 0

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

    /**
     * トレーニング実行時の助力の支えの消費と獲得
     */
    fun updateJoryokuSupport(gain: Boolean): ShuntaisaiStatus {
        return copy(
            joryokuSupportCount = if (gain) shuntaisaiJoryokuSupportCount else max(0, joryokuSupportCount - 1),
        )
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

package io.github.mee1080.umasim.race.data2

import io.github.mee1080.umasim.race.calc2.RaceState
import io.github.mee1080.umasim.race.data.skillLevelValueDefault
import io.github.mee1080.umasim.race.data.skillLevelValueSpeed
import io.github.mee1080.utility.toPercentString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

@OptIn(ExperimentalSerializationApi::class)
private val jsonParser = Json { allowTrailingComma = true }

val skillData2 by lazy {
    jsonParser.decodeFromString<List<SkillData>>(rawSkillData)
}

val skillDataMap2 by lazy {
    skillData2.groupBy { it.name }
}

fun getSkills(name: String) = skillDataMap2[name]!!

fun getSkill(name: String): SkillData {
    val skills = getSkills(name)
    if (skills.size != 1) {
        throw IllegalArgumentException("skill $name duplicated")
    }
    return skills.first()
}

interface ApproximateCondition {
    fun update(state: RaceState, value: Int): Int
    val displayName: String
    val description: String
}

class ApproximateMultiCondition(
    override val displayName: String,
    val conditions: List<Pair<ApproximateCondition, ((RaceState) -> Boolean)?>>,
) : ApproximateCondition {
    override fun update(state: RaceState, value: Int): Int {
        for (condition in conditions) {
            if (condition.second?.invoke(state) != false) {
                return condition.first.update(state, value)
            }
        }
        throw IllegalArgumentException()
    }

    override val description = conditions.joinToString("\n") { it.first.description }
}

class ApproximateStartContinue(
    override val displayName: String,
    val start: Double,
    val continuation: Double,
) : ApproximateCondition {
    override fun update(state: RaceState, value: Int): Int {
        return if (value == 0) {
            if (Random.nextDouble() < start) 1 else 0
        } else {
            if (Random.nextDouble() < continuation) value + 1 else 0
        }
    }

    override val description = buildString {
        append(start.toPercentString(1))
        append("の確率で開始、")
        append(continuation.toPercentString(1))
        append("の確率で継続")
    }
}

class ApproximateRandomRates(
    override val displayName: String,
    val rates: List<Pair<Int, Double>>,
) : ApproximateCondition {
    override fun update(state: RaceState, value: Int): Int {
        val check = Random.nextDouble()
        var total = 0.0
        for (rate in rates) {
            total += rate.second
            if (check < total) return rate.first
        }
        return 0
    }

    override val description = buildString {
        rates.forEach {
            append(it.second.toPercentString(1))
            append("の確率で")
            append(it.first)
            append("、")
        }
        append("残りは0")
    }
}

val approximateConditions = mapOf(
    "move_lane" to ApproximateStartContinue("横移動(軽やかステップなど)", 0.1, 0.1),
    "change_order_onetime" to ApproximateRandomRates(
        "追い抜き/追い抜かれ(アガッてきたなど多数)",
        listOf(-1 to 0.2, 1 to 0.2)
    ),
    "overtake" to ApproximateStartContinue("追い抜きモード(電光石火など多数)", 0.20, 0.50),
    "overtaken" to ApproximateStartContinue("詰め寄られ(勝利への執念など)", 0.15, 0.50),
    "blocked_front" to ApproximateStartContinue("前方ブロック(鋼の意志など)", 0.07, 0.50),
    "blocked_side" to ApproximateStartContinue("横ブロック(つぼみなど)", 0.07, 0.50),
    "infront_near_lane" to ApproximateMultiCondition(
        "前にウマ娘(ノンストなど)",
        listOf(
            ApproximateStartContinue("序盤", 0.05, 0.50) to {
                it.currentPhase == 0
            },
            ApproximateStartContinue("中盤", 0.10, 0.50) to {
                it.currentPhase == 1
            },
            ApproximateStartContinue("終盤最終コーナー前", 0.20, 0.30) to {
                !it.isAfterFinalCorner
            },
            ApproximateStartContinue("終盤最終コーナー後", 0.07, 0.40) to null,
        ),
    ),
    "behind_near_lane" to ApproximateStartContinue("後にウマ娘(お先など)", 0.15, 0.50),
    "near_count" to ApproximateMultiCondition(
        "近くにウマ娘(ウマ好み/ワクワククライマックスなど)",
        listOf(
            ApproximateRandomRates("序盤", listOf(1 to 0.1, 2 to 0.2, 3 to 0.3, 4 to 0.2, 5 to 0.1)) to {
                it.currentPhase == 0
            },
            ApproximateRandomRates("中盤", listOf(1 to 0.3, 2 to 0.2, 3 to 0.1)) to {
                it.currentPhase == 1
            },
            ApproximateRandomRates("終盤", listOf(1 to 0.3, 2 to 0.3, 3 to 0.2)) to null,
        ),
    ),
    "is_surrounded" to ApproximateStartContinue("周囲にウマ娘(どこ吹く風など)", 0.05, 0.40),
    "temptation_opponent_count_behind" to ApproximateStartContinue(
        "後ろのウマ娘掛かり(トリック&トリートなど、自身への効果のみ反映)",
        0.07, 0.20,
    ),
    "is_other_character_activate_advantage_skill31" to ApproximateMultiCondition(
        "他のウマ娘が加速スキル発動(トランセンド固有)",
        listOf(
            ApproximateRandomRates("序盤", listOf(1 to 0.9)) to {
                it.currentPhase == 0
            },
            ApproximateRandomRates("中盤前半", listOf(1 to 0.01)) to {
                it.simulation.position in it.setting.phase1Start..it.setting.phase1Half
            },
            ApproximateRandomRates("中盤後半", listOf(1 to 0.05)) to {
                it.currentPhase == 1
            },
            ApproximateRandomRates("終盤", listOf(1 to 0.9)) to null,
        )
    )
)

val ignoreConditions = mapOf(
    "grade" to "GI条件は無視",
    "time" to "ナイター条件は無視",
    "season" to "季節条件は無視",
    "post_number" to "枠条件は無視",
    "is_dirtgrade" to "交流重賞条件は無視",
    "popularity" to "人気条件は満たしている前提、満たさない場合は未実装",

    "is_used_skill_id" to "他スキル発動条件は未実装",

    "order" to "順位条件は無視",
    "order_rate" to "順位条件は無視",
    "order_rate_in10_continue" to "順位条件は無視",
    "order_rate_in20_continue" to "順位条件は無視",
    "order_rate_in30_continue" to "順位条件は無視",
    "order_rate_in40_continue" to "順位条件は無視",
    "order_rate_in50_continue" to "順位条件は無視",
    "order_rate_in60_continue" to "順位条件は無視",
    "order_rate_in70_continue" to "順位条件は無視",
    "order_rate_in80_continue" to "順位条件は無視",
    "order_rate_in90_continue" to "順位条件は無視",
    "order_rate_out10_continue" to "順位条件は無視",
    "order_rate_out20_continue" to "順位条件は無視",
    "order_rate_out30_continue" to "順位条件は無視",
    "order_rate_out40_continue" to "順位条件は無視",
    "order_rate_out50_continue" to "順位条件は無視",
    "order_rate_out60_continue" to "順位条件は無視",
    "order_rate_out70_continue" to "順位条件は無視",
    "order_rate_out80_continue" to "順位条件は無視",
    "order_rate_out90_continue" to "順位条件は無視",

    "distance_diff_rate" to "相対位置条件は無視",

    "change_order_up_finalcorner_after" to "最終コーナー以降追い抜き条件は無視",
    "change_order_up_end_after" to "終盤追い抜き条件は無視",

    "bashin_diff_infront" to "他のウマ娘との距離条件は無視",
    "bashin_diff_behind" to "他のウマ娘との距離条件は無視",
    "distance_diff_top" to "他のウマ娘との距離条件は無視",
    "distance_diff_top_float" to "他のウマ娘との距離条件は無視",

    "same_skill_horse_count" to "他のウマ娘のスキル条件は無視",

    "is_behind_in" to "内外条件は無視",
    "lane_type" to "内外条件は無視",

    "running_style_equal_popularity_one" to "他のウマ娘の作戦条件は無視",
    "running_style_count_same" to "他のウマ娘の作戦条件は無視",
    "running_style_count_same_rate" to "他のウマ娘の作戦条件は無視",

    "visiblehorse" to "視界内のウマ娘条件は満たしている前提",

    "activate_count_all_team" to "チームのスキル発動数条件は無視",
)

@Serializable
data class SkillData(
    val id: String,
    val name: String,
    val rarity: String,
    val type: String,
    val sp: Int = 0,
    val activateLot: Int = 1,
    val holder: String? = null,
    val invokes: List<Invoke> = emptyList(),
    val info: List<String> = emptyList(),
    val description: List<String> = emptyList(),
) {
    fun applyLevel(level: Int): SkillData {
        return copy(
            invokes = invokes.map { it.applyLevel(level) },
        )
    }

    val messages by lazy {
        (info + description + invokes.flatMap { it.messages }).distinct()
    }
}

@Serializable
data class Invoke(
    val skillId: String,
    val index: Int,
    val conditions: List<List<SkillCondition>> = emptyList(),
    val preConditions: List<List<SkillCondition>> = emptyList(),
    val effects: List<SkillEffect> = emptyList(),
    val cd: Double = 500.0,
    val duration: Double = 0.0,
) {

    fun applyLevel(level: Int): Invoke {
        return copy(
            effects = effects.map { it.applyLevel(level) },
        )
    }

    val messages by lazy {
        toMessages(preConditions) + toMessages(conditions)
    }

    private fun toMessages(target: List<List<SkillCondition>>): Set<String> {
        return buildSet {
            target.forEach { andConditions ->
                andConditions.forEach { condition ->
                    ignoreConditions[condition.type]?.let { add(it) }
                }
            }
        }
    }

    val targetRunningStyle by lazy { createConditionValuesSet("running_style") }

    val targetRotation by lazy { createConditionValuesSet("rotation") }

    val targetGroundType by lazy { createConditionValuesSet("ground_type") }

    val targetDistanceType by lazy { createConditionValuesSet("distance_type") }

    val targetTrackId by lazy { createConditionValuesSet("track_id") }

    val targetBasisDistance by lazy { createConditionValuesSet("is_basis_distance") }

    private fun createConditionValuesSet(type: String): Set<Int> {
        return buildSet {
            conditions.forEach { list ->
                list.forEach {
                    if (it.type == type) {
                        add(it.value)
                    }
                }
            }
        }
    }

    val coolDownId by lazy {
        if (conditions.any { list -> list.any { it.type == "is_activate_other_skill_detail" } }) {
            "$skillId-$index"
        } else {
            skillId
        }
    }

    val isPassive by lazy {
        passiveSpeed > 0 || passiveStamina > 0 || passivePower > 0 || passiveGuts > 0 || passiveWisdom > 0 || temptationRate > 0
    }

    val isStart by lazy {
        startAdd > 0.0 || startMultiply > 0.0
    }

    val passiveSpeed by lazy {
        effects.filter { it.type == "passiveSpeed" || it.type == "passiveAll" }.sumOf { it.value } / 10000
    }

    val passiveStamina by lazy {
        effects.filter { it.type == "passiveStamina" || it.type == "passiveAll" }.sumOf { it.value } / 10000
    }

    val passivePower by lazy {
        effects.filter { it.type == "passivePower" || it.type == "passiveAll" }.sumOf { it.value } / 10000
    }

    val passiveGuts by lazy {
        effects.filter { it.type == "passiveGuts" || it.type == "passiveAll" }.sumOf { it.value } / 10000
    }

    val passiveWisdom by lazy {
        effects.filter { it.type == "passiveWisdom" || it.type == "passiveAll" }.sumOf { it.value } / 10000
    }

    val oonige by lazy {
        effects.any { it.type == "oonige" }
    }

    val heal by lazy {
        effects.filter { it.type == "heal" }.sumOf { it.value }
    }

    val startMultiply by lazy {
        effects.filter { it.type == "startMultiply" }.sumOf { it.value } / 10000.0
    }

    val startAdd by lazy {
        effects.filter { it.type == "startAdd" }.sumOf { it.value } / 10000.0
    }

    val currentSpeed by lazy {
        effects.filter { it.type == "currentSpeed" }.sumOf { it.value } / 10000.0
    }

    val speedWithDecel by lazy {
        effects.filter { it.type == "speedWithDecel" }.sumOf { it.value } / 10000.0
    }

    val targetSpeed by lazy {
        effects.filter { it.type == "targetSpeed" }.sumOf { it.value } / 10000.0
    }

    val temptationRate by lazy {
        effects.filter { it.type == "temptationRate" }.sumOf { it.value } / 10000
    }

    val acceleration by lazy {
        effects.filter { it.type == "acceleration" }.sumOf { it.value } / 10000.0
    }

    val rareSkill by lazy {
        effects.filter { it.type == "rareSkill" }.sumOf { it.value } / 10000.0
    }

    val totalSpeed by lazy {
        targetSpeed + speedWithDecel
    }
}

@Serializable
data class SkillCondition(
    val type: String,
    val operator: String,
    val value: Int,
) {
    val check: (Int) -> Boolean by lazy {
        when (operator) {
            "==" -> { target -> target == value }
            "!=" -> { target -> target != value }
            ">=" -> { target -> target >= value }
            "<=" -> { target -> target <= value }
            ">" -> { target -> target > value }
            "<" -> { target -> target < value }
            else -> { _ -> false }
        }
    }
}

@Serializable
data class SkillEffect(
    val type: String,
    val value: Int,
    val special: String = "",
    val additional: String = "",
) {
    fun applyLevel(level: Int): SkillEffect {
        val values = if (type == "targetSpeed") skillLevelValueSpeed else skillLevelValueDefault
        return copy(value = (value * values[level]).toInt())
    }
}

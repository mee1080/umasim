package io.github.mee1080.umasim.race.data2

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

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

val approximateRate = mapOf(
    "is_move_lane" to (0.01 to "横移動"),
    "change_order_onetime" to (0.005 to "追い抜き/追い抜かれ"),
    "is_overtake" to (0.02 to "追い抜きモード"),
    "overtake_target_time" to (0.015 to "詰め寄られ"),
    "compete_fight_count" to (0.03 to "追い比べ"),
    "blocked_front" to (0.005 to "前方ブロック"),
    "blocked_front_continuetime" to (0.001 to "前方ブロック"),
    "blocked_side_continuetime" to (0.005 to "横ブロック"),
    "infront_near_lane_time" to (0.01 to "前にウマ娘条件"),
    "behind_near_lane_time" to (0.01 to "後にウマ娘条件"),
    "behind_near_lane_time_set1" to (0.015 to "後にウマ娘条件"),
    "near_count" to (0.01 to "近くにウマ娘条件"),
    "is_surrounded" to (0.003 to "囲まれ条件"),
    "temptation_opponent_count_behind" to (0.005 to "後ろのウマ娘掛かり"),
)

fun approximateRateString(info: Pair<Double, String>, value: Int): String {
    val rate = info.first / 2.0.pow(value)
    val rateString = (rate * 100).roundString(1)
    val secondRate = 1.0 - (1.0 - rate).pow(15)
    val secondRateString = (secondRate * 100).roundString(1)
    return "${info.second}は毎フレーム${rateString}%の確率で発動するものとする（1秒以内発動率${secondRateString}%）"
}

private fun Double.roundString(position: Int = 0): String {
    return if (isNaN()) "-" else if (position == 0) roundToInt().toString() else {
        val minus = if (this < 0) "-" else ""
        val factor = 10.0.pow(position).roundToInt()
        val intValue = (abs(this) * factor).roundToInt()
        return "$minus${intValue / factor}.${intValue % factor}"
    }
}

val ignoreConditions = mapOf(
    "grade" to "GI条件は無視",
    "time" to "ナイター条件は無視",
    "season" to "季節条件は無視",
    "post_number" to "枠条件は無視",
    "is_dirtgrade" to "交流重賞条件は無視",
    "popularity" to "人気条件は満たしている前提、満たさない場合は未実装",

    "is_activate_other_skill_detail" to "一段目が発動しなくても二段目が発動",
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
    val description: List<String> = emptyList(),
) {
    val messages by lazy {
        (description + invokes.flatMap { it.messages }).distinct()
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
    val messages by lazy {
        effects.map { "${it.type} ${it.value}" } + toMessages(preConditions) + toMessages(conditions)
    }

    private fun toMessages(target: List<List<SkillCondition>>): Set<String> {
        return buildSet {
            target.forEach { andConditions ->
                andConditions.forEach { condition ->
                    approximateRate[condition.type]?.let { add(approximateRateString(it, condition.value)) }
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
)
package io.github.mee1080.umasim.race.data2

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

// FIXME
// condition type=weather: 実装
// condition type=post_number: 実装
// condition type=popularity: 人気あり/なしの両パターンのスキルを実装

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

val ignoreConditions = mapOf(
    "grade" to "GI条件は無視",
    "time" to "ナイター条件は無視",
    "season" to "季節条件は無視",
    "weather" to "天候条件は無視",
    "post_number" to "枠条件は無視",
    "is_dirtgrade" to "交流重賞条件は無視",
    "popularity" to "人気条件は満たしている前提、満たさない場合は未実装",

    "activate_count_later_half" to "他スキル発動条件は未実装",
    "activate_count_middle" to "他スキル発動条件は未実装",
    "activate_count_end_after" to "他スキル発動条件は未実装",
    "is_activate_other_skill_detail" to "他スキル発動条件は未実装",
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

    "is_move_lane" to "横移動条件は無視",

    "change_order_up_finalcorner_after" to "追い抜き条件は無視",
    "change_order_onetime" to "追い抜き/追い抜かれ条件は無視",
    "is_overtake" to "追い抜き条件は無視",
    "change_order_up_end_after" to "追い抜き条件は無視",
    "overtake_target_time" to "追い抜かれ条件は無視",

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

    "compete_fight_count" to "他のウマ娘が関わる条件は無視",
    "blocked_front" to "他のウマ娘が関わる条件は無視",
    "blocked_front_continuetime" to "他のウマ娘が関わる条件は無視",
    "blocked_side_continuetime" to "他のウマ娘が関わる条件は無視",
    "infront_near_lane_time" to "他のウマ娘が関わる条件は無視",
    "behind_near_lane_time" to "他のウマ娘が関わる条件は無視",
    "behind_near_lane_time_set1" to "他のウマ娘が関わる条件は無視",
    "near_count" to "他のウマ娘が関わる条件は無視",
    "is_surrounded" to "他のウマ娘が関わる条件は無視",
    "temptation_opponent_count_behind" to "他のウマ娘が関わる条件は無視",
    "visiblehorse" to "他のウマ娘が関わる条件は無視",

    "activate_count_all_team" to "チームのスキル発動数条件は無視",
)

@Serializable
data class SkillData(
    val id: Int,
    val name: String,
    val rarity: String,
    val type: String,
    val sp: Int = 0,
    val activateLot: Int = 1,
    val holder: String? = null,
    val invokes: List<Invoke> = emptyList(),
    val description: List<String> = emptyList(),
) {
    val messages = (description + invokes.flatMap { it.messages }).distinct()
}

@Serializable
data class Invoke(
    val conditions: List<List<SkillCondition>> = emptyList(),
    val preConditions: List<List<SkillCondition>> = emptyList(),
    val effects: List<SkillEffect> = emptyList(),
    val cd: Double = 500.0,
    val duration: Double = 0.0,
) {
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

    val coolDownId by lazy {
        Random.nextInt().toString()
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

    val temptationRate by lazy {
        effects.filter { it.type == "temptationRate" }.sumOf { it.value } / 10000
    }

    val targetSpeed by lazy {
        effects.filter { it.type == "targetSpeed" }.sumOf { it.value } / 10000.0
    }

    val speedWithDecel by lazy {
        effects.filter { it.type == "speedWithDecel" }.sumOf { it.value } / 10000.0
    }

    val speed by lazy {
        effects.filter { it.type == "currentSpeed" }.sumOf { it.value } / 10000.0
    }

    val acceleration by lazy {
        effects.filter { it.type == "acceleration" }.sumOf { it.value } / 10000.0
    }

    val startAdd by lazy {
        effects.filter { it.type == "startAdd" }.sumOf { it.value } / 10000.0
    }

    val startMultiply by lazy {
        effects.filter { it.type == "startMultiply" }.sumOf { it.value } / 10000.0
    }

    val heal by lazy {
        effects.filter { it.type == "heal" }.sumOf { it.value }
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

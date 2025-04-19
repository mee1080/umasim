package io.github.mee1080.umasim.race.data2

import io.github.mee1080.umasim.race.calc2.RaceState
import io.github.mee1080.umasim.race.data.horseLane
import io.github.mee1080.umasim.race.data.skillLevelValueDefault
import io.github.mee1080.umasim.race.data.skillLevelValueSpeed
import io.github.mee1080.utility.fetchFromUrl
import io.github.mee1080.utility.toPercentString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.min
import kotlin.random.Random

@OptIn(ExperimentalSerializationApi::class)
private val jsonParser = Json { allowTrailingComma = true }

suspend fun loadSkillData() {
//    val skillDataString = HttpClient()
//        .get("https://raw.githubusercontent.com/mee1080/umasim/refs/heads/main/data/skill_data.txt")
//        .bodyAsText()
    val skillDataString =
        fetchFromUrl("https://raw.githubusercontent.com/mee1080/umasim/refs/heads/main/data/skill_data.txt")
    skillData2 = jsonParser.decodeFromString<List<SkillData>>(skillDataString)
}

lateinit var skillData2: List<SkillData>
    private set

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
    val valueOnStart: Int
}

class ApproximateMultiCondition(
    override val displayName: String,
    val conditions: List<Pair<ApproximateCondition, ((RaceState) -> Boolean)?>>,
    override val valueOnStart: Int = 0,
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
    override val valueOnStart: Int = 0,
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
    override val valueOnStart: Int = 0,
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

class ApproximateCountUp(
    override val displayName: String,
    val rate: Double,
    override val valueOnStart: Int = 0,
) : ApproximateCondition {
    override fun update(state: RaceState, value: Int): Int {
        return value + if (Random.nextDouble() < rate) 1 else 0
    }

    override val description = buildString {
        append(rate.toPercentString(1))
        append("の確率で+1")
    }
}

class ApproximateNone(
    override val displayName: String,
    override val valueOnStart: Int = 0,
) : ApproximateCondition {
    override fun update(state: RaceState, value: Int) = value
    override val description = "なし"
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
    "blocked_side" to ApproximateMultiCondition(
        "横ブロック(つぼみなど)",
        listOf(
            ApproximateStartContinue("序盤1/4以降かつ走行レーンが外側", 0.0, 0.0) to {
                it.currentSection in 1..3 && it.simulation.currentLane > 3.0 * horseLane
            },
            ApproximateStartContinue("上記以外の序盤", 0.1, 0.85) to {
                it.currentPhase == 0
            },
            ApproximateStartContinue("中盤", 0.08, 0.75) to {
                it.currentPhase == 1
            },
            ApproximateStartContinue("終盤", 0.07, 0.50) to null,
        ),
        valueOnStart = 1,
    ),
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
        "近くのウマ娘人数(ウマ好み/ワクワククライマックスなど)",
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
    "near_infront_count" to ApproximateRandomRates("前方近くのウマ娘人数(無二/無三)", listOf(1 to 0.05)),
    "is_surrounded" to ApproximateStartContinue("周囲にウマ娘(どこ吹く風など)", 0.05, 0.40),
    "temptation_opponent_count_behind" to ApproximateStartContinue(
        "後ろのウマ娘掛かり(トリック&トリートなど、自身への効果のみ反映)",
        0.07, 0.20,
    ),
    "is_other_character_activate_advantage_skill22" to ApproximateMultiCondition(
        "他のウマ娘が速度スキル発動(後の先など)",
        listOf(
            ApproximateRandomRates("序盤", listOf(1 to 0.1)) to {
                it.currentPhase == 0
            },
            ApproximateRandomRates("中盤", listOf(1 to 0.15)) to {
                it.currentPhase == 1
            },
            ApproximateRandomRates("終盤", listOf(1 to 0.2)) to null,
        )
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
    ),
    "change_order_up_middle" to ApproximateMultiCondition(
        "中盤追い抜き(クラウン固有/嫁アマ固有など)",
        listOf(
            ApproximateCountUp("中盤", 0.05) to {
                it.currentPhase == 1
            },
            ApproximateNone("その他") to null,
        )
    ),
    "change_order_up_end_after" to ApproximateMultiCondition(
        "終盤追い抜き(ルドルフ固有/デジタル固有など)",
        listOf(
            ApproximateCountUp("終盤", 0.15) to {
                it.currentPhase >= 2
            },
            ApproximateNone("その他") to null,
        )
    ),
    "change_order_up_finalcorner_after" to ApproximateMultiCondition(
        "最終コーナー以降追い抜き(チョコフラッシュ固有)",
        listOf(
            ApproximateCountUp("最終コーナー以降", 0.15) to {
                it.isAfterFinalCorner
            },
            ApproximateNone("その他") to null,
        )
    ),
    "overtake_target_no_order_up_time" to ApproximateStartContinue(
        "追い抜き対象ウマ娘順位変動なし(絶ボク、追い抜きモード条件と同時設定されるためこちらの条件は緩め)",
        0.80, 0.60,
    ),
)

val approximateTypeToState = mapOf(
    "is_move_lane" to "move_lane",
    "change_order_onetime" to "change_order_onetime",
    "is_overtake" to "overtake",
    "overtake_target_time" to "overtaken",
    "blocked_front" to "blocked_front",
    "blocked_front_continuetime" to "blocked_front",
    "blocked_side_continuetime" to "blocked_side",
    "infront_near_lane_time" to "infront_near_lane",
    "behind_near_lane_time" to "behind_near_lane",
    "behind_near_lane_time_set1" to "behind_near_lane",
    "near_count" to "near_count",
    "near_infront_count" to "near_infront_count",
    "is_surrounded" to "is_surrounded",
    "temptation_opponent_count_behind" to "temptation_opponent_count_behind",
    "change_order_up_middle" to "change_order_up_middle",
    "change_order_up_end_after" to "change_order_up_end_after",
    "change_order_up_finalcorner_after" to "change_order_up_finalcorner_after",
    "overtake_target_no_order_up_time" to "overtake_target_no_order_up_time",
)

val ignoreConditions = mapOf(
    "grade" to "GI条件は無視",
    "time" to "ナイター条件は無視",
    "season" to "季節条件は無視",
    "weather" to "天候条件は無視",
    "is_dirtgrade" to "交流重賞条件は無視",

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
    val group: Int,
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
        info + notice
    }

    val notice by lazy {
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
    val durationSpecial: Int = 1,
) {

    fun applyLevel(level: Int): Invoke {
        return copy(
            effects = effects.map { it.applyLevel(level) },
        )
    }

    val messages by lazy {
        toMessages(preConditions) + toMessages(conditions) + durationMessage + effects.flatMap { it.messages }
    }

    private fun toMessages(target: List<List<SkillCondition>>): Set<String> {
        return buildSet {
            target.forEach { andConditions ->
                andConditions.forEach { condition ->
                    if (approximateTypeToState.containsKey(condition.type)) add("発動条件近似")
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

    val targetCornerCount by lazy {
        createConditionValuesSet("corner_count") + if (needCorner()) (1..10).toSet() else emptySet()
    }

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

    private val cornerConditions = setOf(
        "corner_random",
        "all_corner_random",
        "phase_corner_random",
        "is_finalcorner_random",
        "is_finalcorner",
        "is_finalcorner_laterhalf",
        "change_order_up_finalcorner_after",
    )

    private fun needCorner(): Boolean {
        return conditions.any { list ->
            list.any {
                when (it.type) {
                    "corner" -> !it.check(0)
                    else -> cornerConditions.contains(it.type)
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

    private val effectsByTypeMap by lazy {
        effects.groupBy { it.type }
    }

    private fun effectsByType(type: String): List<SkillEffect> {
        return effectsByTypeMap.getOrElse(type) { emptyList() }
    }

    private fun totalEffect(state: RaceState, type: String): Double {
        return effectsByType(type).sumOf { it.calcValue(state) }
    }

    val isPassive by lazy {
        effectsByTypeMap.containsKey("passiveAll")
                || effectsByTypeMap.containsKey("passiveSpeed")
                || effectsByTypeMap.containsKey("passiveStamina")
                || effectsByTypeMap.containsKey("passivePower")
                || effectsByTypeMap.containsKey("passiveGuts")
                || effectsByTypeMap.containsKey("passiveWisdom")
                || effectsByTypeMap.containsKey("temptationRate")
    }

    val isStart by lazy {
        effectsByTypeMap.containsKey("startAdd")
                || effectsByTypeMap.containsKey("startMultiply")
    }

    private fun passiveAll(state: RaceState): Double {
        return totalEffect(state, "passiveAll") / 10000.0
    }

    fun passiveSpeed(state: RaceState): Double {
        return passiveAll(state) + totalEffect(state, "passiveSpeed") / 10000.0
    }

    fun passiveStamina(state: RaceState): Double {
        return passiveAll(state) + totalEffect(state, "passiveStamina") / 10000.0
    }

    fun passivePower(state: RaceState): Double {
        return passiveAll(state) + totalEffect(state, "passivePower") / 10000.0
    }

    fun passiveGuts(state: RaceState): Double {
        return passiveAll(state) + totalEffect(state, "passiveGuts") / 10000.0
    }

    fun passiveWisdom(state: RaceState): Double {
        return passiveAll(state) + totalEffect(state, "passiveWisdom") / 10000.0
    }

    val oonige by lazy {
        effectsByTypeMap.containsKey("oonige")
    }

    val isHeal by lazy {
        effectsByTypeMap.containsKey("heal")
    }

    fun heal(state: RaceState): Double {
        return totalEffect(state, "heal")
    }

    fun startMultiply(state: RaceState): Double {
        return totalEffect(state, "startMultiply") / 10000.0
    }

    fun startAdd(state: RaceState): Double {
        return totalEffect(state, "startAdd") / 10000.0
    }

    fun currentSpeed(state: RaceState): Double {
        return totalEffect(state, "currentSpeed") / 10000.0
    }

    val isSpeedWithDecel by lazy {
        effectsByTypeMap.containsKey("speedWithDecel")
    }

    fun speedWithDecel(state: RaceState): Double {
        return totalEffect(state, "speedWithDecel") / 10000.0
    }

    fun targetSpeed(state: RaceState): Double {
        return totalEffect(state, "targetSpeed") / 10000.0
    }

    fun temptationRate(state: RaceState): Double {
        return totalEffect(state, "temptationRate") / 10000.0
    }

    fun acceleration(state: RaceState): Double {
        return totalEffect(state, "acceleration") / 10000.0
    }

    fun rareSkill(state: RaceState): Double {
        return totalEffect(state, "rareSkill") / 10000.0
    }

    fun totalSpeed(state: RaceState): Double {
        return targetSpeed(state) + speedWithDecel(state)
    }

    fun laneChangeSpeed(state: RaceState): Double {
        return totalEffect(state, "laneChangeSpeed") / 10000.0
    }

    val isFixLane by lazy {
        effectsByTypeMap.containsKey("fixLane")
    }

    private val durationMessage by lazy {
        buildList {
            when (durationSpecial) {
                2 -> add("先頭との距離は最大（1.6倍）固定")
                4 -> add("発動直後に2回追い抜く条件で近似")
            }
        }
    }

    fun calcDuration(state: RaceState): Double {
        return when (durationSpecial) {
            1 -> duration

            2 -> {
                // 先頭から離されているとその距離が遠いほど（113転び114起き）
                // min(0.8+戦闘からの距離 / 62.5m, 1.6)
                duration * 1.6
            }

            3 -> {
                // 残りの持久力
                val sp = state.simulation.sp
                duration * when {
                    sp < 2000 -> 1.0
                    sp < 2400 -> 1.5
                    sp < 2600 -> 2.0
                    sp < 2800 -> 2.2
                    sp < 3000 -> 2.5
                    sp < 3200 -> 3.0
                    sp < 3500 -> 3.5
                    else -> 4.0
                }
            }

            4 -> {
                // 効果中に追い抜くと3回まで効果と時間が増える
                // 1回あたり+1
                duration + 2.0
            }

            5 -> {
                // レース中盤に連続して競り合い続けた時間が長いほど（CHERRY☆スクランブル）
                // <2：1.0、<4：2.0、<6：3.0、>=6：4.0
                duration * 3.0
            }

            7 -> {
                // 残りの持久力（ごろりん！？パワードライブ）
                val sp = state.simulation.sp
                duration * when {
                    sp < 1500 -> 1.0
                    sp < 1800 -> 1.5
                    sp < 2000 -> 2.0
                    sp < 2100 -> 2.5
                    else -> 3.0
                }
            }

            else -> duration
        }
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
    private val value: Int,
    val special: Int = 1,
    val additional: Int = 0,
) {
    fun applyLevel(level: Int): SkillEffect {
        val values = if (type == "targetSpeed") skillLevelValueSpeed else skillLevelValueDefault
        return copy(value = (value * values[level]).toInt())
    }

    val messages: List<String> by lazy {
        buildList {
            when (special) {
                3, 4, 5, 6, 7 -> add("チームメンバーのステータス合計条件は最大値前提")
                10 -> add("勝利数条件は最大値前提")
                11 -> add("追い抜き回数は2回（1.1倍）固定")
                12 -> add("ファン数条件は最大値前提")
                19 -> add("先頭から離れている条件は満たす前提")
                20 -> add("中盤連続競り合いは4～6秒（3.0倍）固定")
                24 -> add("海外適性Lv合計は最大値前提")
                25 -> add("終盤開始までに取ったリードの距離は最大（1.8倍）固定")
                26 -> add("UAFは全勝前提")
                27 -> add("お料理Ptは16000以上（1.2倍）前提")
            }
            when (additional) {
                2 -> add("同時に別のスキルを2つ発動する条件で近似")
                3 -> add("同時に別のスキルを1つ発動する条件で近似")
            }
            if (type == "fixLane") {
                add("外方向移動に対する横ブロックは未実装")
            }
        }
    }

    fun calcValue(state: RaceState): Double {
        val specialValue = when (special) {
            1 -> value.toDouble()

            2 -> {
                // 獲得したスキルの数に応じて効果が高まる（叡智/加護）
                value * min(1.2, 1 + 0.01 * state.setting.umaStatus.hasSkills.size)
            }

            3, 4, 5, 6, 7 -> {
                // チームメンバーの〇〇合計が高いほど～（アオハル各種）
                value * 1.2
            }

            8, 9 -> {
                // ランダム（あやしげな作戦）
                val random = Random.nextDouble()
                value * when {
                    random < 0.6 -> 0.0
                    random < 0.9 -> 0.02
                    else -> 0.04
                }
            }

            10 -> {
                // 勝利の数だけ効果が高まる（一番星）
                value * 1.2
            }

            11 -> {
                // 終盤のコーナーで追い抜いた回数に応じて効果が増える（灯穂）
                // 2回：1.1、3回：1.2、4回：1.25
                value * 1.1
            }

            12 -> {
                // ファンが多いほど効果が高まる（キミと勝ちたい
                value * 1.2
            }

            13 -> {
                // 基礎能力の高さに応じて効果が高まる（限界の先へ）
                val maxStatus = maxOf(
                    state.setting.umaStatus.speed,
                    state.setting.umaStatus.stamina,
                    state.setting.umaStatus.power,
                    state.setting.umaStatus.guts,
                    state.setting.umaStatus.wisdom,
                )
                value * when {
                    maxStatus < 600 -> 0.8
                    maxStatus < 800 -> 0.9
                    maxStatus < 1000 -> 1.0
                    maxStatus < 1100 -> 1.1
                    else -> 1.2
                }
            }

            14 -> {
                // 能力を引き出すスキルの発動数に応じて（理運開かりて翔る）
                val count = state.simulation.passiveTriggered
                value * when {
                    count <= 2 -> 0.0
                    count <= 4 -> 1.0
                    count <= 5 -> 2.0
                    else -> 3.0
                }
            }

            19 -> {
                // 先頭から離れていると効果が増える（叙情、旅路の果てに）
                value + 1000.0
            }

            20 -> {
                // レース中盤に連続して競り合い続けた時間が長いほど（CHERRY☆スクランブル）
                // <2：1.0、<4：2.0、<6：3.0、>=6：4.0
                value * 3.0
            }

            22 -> {
                // スピードの能力に応じて（夏空ハレーション）
                val status = state.setting.modifiedSpeed
                value * when {
                    status < 1700 -> 0.0
                    status < 1800 -> 1.0
                    status < 1900 -> 2.0
                    status < 2000 -> 3.0
                    else -> 4.0
                }
            }

            23 -> {
                // スピードの能力に応じて（眩耀のルクシオン）
                val status = state.setting.modifiedSpeed
                value * when {
                    status < 1400 -> 1.0
                    status < 1600 -> 2.0
                    else -> 3.0
                }
            }

            24 -> {
                // 海外適性Lvの合計が高いほど効果が高まる（最高峰の夢）
                value * 1.2
            }

            25 -> {
                // 終盤開始までに取ったリードの距離に応じて効果が増える（Billions of stars）
                // <10：1.0、<25：1.4、>=25：1.8
                value * 1.8
            }

            26 -> {
                // U.A.F.決勝大会の優勝数に応じて効果が高まる（爆熱のキラメキ！）
                value * 1.2
            }

            27 -> {
                // お料理Ptの合計が高いほど効果が高まる（私たちの走る道程）
                value * 1.2
            }

            else -> value.toDouble()
        }
        // FIXME 最速発動で近似しているが、本来はスキル効果中に計算が必要
        return when (additional) {
            0 -> specialValue

            1 -> {
                // 効果中に追い抜くと3回まで効果と時間が増える
                // 1回あたり+1
                specialValue * 2.0
            }

            2 -> {
                // スキル発動（最大3回）
                specialValue * 2.0
            }

            3 -> {
                // スキル発動（最大2回）
                specialValue * 1.0
            }

            else -> specialValue
        }
    }
}

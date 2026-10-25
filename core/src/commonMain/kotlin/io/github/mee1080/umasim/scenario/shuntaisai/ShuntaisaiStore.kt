package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType

/**
 * 演目
 */
enum class ShuntaisaiPerformance(
    val displayName: String,
    val type: StatusType,
) {
    YABUSAME("流鏑馬", StatusType.SPEED),
    SHINKI("神旗争奪戦", StatusType.STAMINA),
    OSAMEMAI("納舞", StatusType.POWER),
    OHIKIMIKOSHI("大曳神輿", StatusType.GUTS),
    HIKAKE("飛駆", StatusType.WISDOM),
    ;

    companion object {
        fun of(type: StatusType) = entries.firstOrNull { it.type == type }
    }
}

/**
 * 教えの効果
 *
 * TODO 数値は全て仮
 */
data class ShuntaisaiTeachingEffect(
    val trainingEffect: Int = 0,
    val friendBonus: Int = 0,
    val nonFriendTrainingEffect: Int = 0,
    val specialityRate: Int = 0,
    val hintRate: Int = 0,
    val mainStatusUp: Int = 0,
    val subStatusUp: Int = 0,
    val statusLimitUp: Int = 0,
    val practiceTrainingEffect: Int = 0,
    val practiceStatusUp: Int = 0,
    val practiceHpDown: Int = 0,
    val relationBonus: Int = 0,
    val addMember: Int = 0,
    val failureRateDown: Boolean = false,
) {
    companion object {
        val Empty = ShuntaisaiTeachingEffect()
    }

    operator fun plus(other: ShuntaisaiTeachingEffect) = ShuntaisaiTeachingEffect(
        trainingEffect = trainingEffect + other.trainingEffect,
        friendBonus = friendBonus + other.friendBonus,
        nonFriendTrainingEffect = nonFriendTrainingEffect + other.nonFriendTrainingEffect,
        specialityRate = specialityRate + other.specialityRate,
        hintRate = hintRate + other.hintRate,
        mainStatusUp = mainStatusUp + other.mainStatusUp,
        subStatusUp = subStatusUp + other.subStatusUp,
        statusLimitUp = statusLimitUp + other.statusLimitUp,
        practiceTrainingEffect = practiceTrainingEffect + other.practiceTrainingEffect,
        practiceStatusUp = practiceStatusUp + other.practiceStatusUp,
        practiceHpDown = practiceHpDown + other.practiceHpDown,
        relationBonus = relationBonus + other.relationBonus,
        addMember = addMember + other.addMember,
        failureRateDown = failureRateDown || other.failureRateDown,
    )
}

/**
 * 教え
 *
 * @param period 伝受する時期（0:ジュニア3ターン目、1:クラシック年始、2:シニア年始）
 */
enum class ShuntaisaiTeaching(
    val displayName: String,
    val period: Int,
    val effect: ShuntaisaiTeachingEffect,
    val description: String,
) {
    // TODO 合宿中の追加トレ出現
    KIZUNA(
        "絆", 0,
        ShuntaisaiTeachingEffect(relationBonus = 2, practiceStatusUp = 10),
        "絆ゲージ上昇、演目稽古の基礎能力獲得量アップ",
    ),

    // TODO 転成後の演目稽古友情ボーナス
    TOKUI(
        "得意率", 0,
        ShuntaisaiTeachingEffect(specialityRate = 30),
        "得意率アップ",
    ),

    // TODO ヒントイベントでヒント+1
    HINT(
        "ヒント", 0,
        ShuntaisaiTeachingEffect(hintRate = 30),
        "ヒント発生率アップ",
    ),

    // TODO 助力の支え（3回まで効果アップ）
    JORYOKU(
        "助力", 0,
        ShuntaisaiTeachingEffect(nonFriendTrainingEffect = 15),
        "友情が出ていない時のトレ効果アップ",
    ),

    // TODO 体力消費ダウンは未実装（getHpCostDownがトレーニング種別を受け取らないため）
    TAIRYOKU(
        "体力", 0,
        ShuntaisaiTeachingEffect(practiceHpDown = 20),
        "演目稽古の体力消費ダウン",
    ),

    TOKKA(
        "特化", 1,
        ShuntaisaiTeachingEffect(mainStatusUp = 10, statusLimitUp = 20),
        "主ステ獲得量アップ、獲得上限アップ",
    ),

    // TODO 追加の稽古（該当ステ+20、演目ゲージ上昇）は未実装
    KENSAN(
        "研鑽", 1,
        ShuntaisaiTeachingEffect(),
        "追加の稽古が起きやすくなる",
    ),

    KYORYOKU(
        "協力", 2,
        ShuntaisaiTeachingEffect(friendBonus = 15, addMember = 1),
        "友情ボーナス、演目担当のサポカが別の練習にも追加出現",
    ),

    FUKUTSU(
        "不屈", 2,
        ShuntaisaiTeachingEffect(failureRateDown = true),
        "残り体力に応じて失敗率ダウン",
    ),

    RYODO(
        "両道", 2,
        ShuntaisaiTeachingEffect(subStatusUp = 10, practiceTrainingEffect = 10),
        "副ステ獲得量アップ、演目稽古のトレ効果アップ",
    ),
    ;

    companion object {
        fun byPeriod(period: Int) = entries.filter { it.period == period }
    }
}

/**
 * 演目ゲージ最大値
 */
const val shuntaisaiMaxGauge = 5

/**
 * 演目Lv上限（SSR吾妻編成時は10）
 */
const val shuntaisaiDefaultMaxLevel = 6
const val shuntaisaiAzumaMaxLevel = 10

/**
 * 駿大祭合宿（ジュニアにも合宿あり）
 *
 * TODO 夏に目標レースがあるキャラは合宿期間が延長される
 */
val shuntaisaiCampTurns = listOf(13, 14, 15, 16, 37, 38, 39, 40, 61, 62, 63, 64)

/**
 * 演目稽古の基礎能力上昇量（時期、演目Lv1～10）
 *
 * 演目Lv1で全ステ+10以上、Lv8から強化幅が大きくなる
 * TODO 数値は全て仮
 */
private val shuntaisaiPracticeStatusData = listOf(
    // ジュニア
    listOf(10, 11, 12, 13, 14, 15, 16, 19, 22, 25),
    // クラシック
    listOf(12, 13, 14, 15, 16, 17, 18, 21, 24, 27),
    // シニア以降
    listOf(14, 15, 16, 17, 18, 19, 20, 23, 26, 30),
)

/**
 * 演目稽古のスキルPt上昇量（時期、演目Lv1～10）
 *
 * TODO 数値は全て仮
 */
private val shuntaisaiPracticeSkillPtData = listOf(
    listOf(10, 11, 12, 13, 14, 15, 16, 18, 20, 22),
    listOf(12, 13, 14, 15, 16, 17, 18, 20, 22, 24),
    listOf(14, 15, 16, 17, 18, 19, 20, 22, 24, 26),
)

fun shuntaisaiPracticeStatus(period: Int, level: Int): Status {
    val periodIndex = period.coerceIn(0, 2)
    val levelIndex = (level - 1).coerceIn(0, 9)
    val value = shuntaisaiPracticeStatusData[periodIndex][levelIndex]
    return Status(
        speed = value, stamina = value, power = value, guts = value, wisdom = value,
        skillPt = shuntaisaiPracticeSkillPtData[periodIndex][levelIndex],
    )
}

/**
 * シニアでサポカのタイプを5種類以上編成している場合の演目稽古トレ効果
 *
 * TODO 内容、数値ともに不明
 */
const val shuntaisaiSeniorAllTypePracticeEffect = 10

/**
 * 進捗発表会/本番の目標演目Lv（ジュニア、クラシック、本番）
 */
val shuntaisaiTargetLevel = listOf(3, 5, 6)

/**
 * 進捗発表会/本番の大成功条件演目Lv
 *
 * TODO 大成功条件不明
 */
val shuntaisaiGreatTargetLevel = listOf(4, 6, 8)

enum class ShuntaisaiPresentationResult(val displayName: String) {
    FAILURE("失敗"),
    SUCCESS("成功"),
    GREAT_SUCCESS("大成功"),
}

/**
 * 進捗発表会ボーナス
 */
data class ShuntaisaiPresentationBonus(
    val friendBonus: Int = 0,
    val specialityRate: Int = 0,
    val hintRate: Int = 0,
    val raceBonus: Int = 0,
) {
    companion object {
        val Empty = ShuntaisaiPresentationBonus()
    }
}

/**
 * 進捗発表会ボーナス（時期、結果）
 *
 * TODO 数値は全て仮、レースボーナスは未反映
 */
val shuntaisaiPresentationBonus = listOf(
    // ジュニア12月
    mapOf(
        ShuntaisaiPresentationResult.FAILURE to ShuntaisaiPresentationBonus.Empty,
        ShuntaisaiPresentationResult.SUCCESS to ShuntaisaiPresentationBonus(3, 30, 15, 0),
        ShuntaisaiPresentationResult.GREAT_SUCCESS to ShuntaisaiPresentationBonus(5, 80, 30, 0),
    ),
    // クラシック12月
    mapOf(
        ShuntaisaiPresentationResult.FAILURE to ShuntaisaiPresentationBonus.Empty,
        ShuntaisaiPresentationResult.SUCCESS to ShuntaisaiPresentationBonus(5, 60, 30, 0),
        ShuntaisaiPresentationResult.GREAT_SUCCESS to ShuntaisaiPresentationBonus(10, 120, 75, 0),
    ),
)

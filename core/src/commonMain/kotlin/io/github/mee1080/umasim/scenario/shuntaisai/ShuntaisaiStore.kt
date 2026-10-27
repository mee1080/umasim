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
 * 教えを割り当てた演目の担当がトレーニングに参加している場合に、参加人数分加算される（絆ゲージ上昇を除く）
 *
 * @param relationBonus 全編成サポカの絆ゲージ上昇（重複しない）
 * @param practiceMainStatusUp 演目稽古でメイン基礎能力の獲得量アップ
 * @param practiceHintCountUp 演目稽古のヒントイベントで獲得するヒント個数
 * @param nonFriendTrainingEffect 友情非発生時のトレーニング効果アップ
 * @param joryokuSupport 演目稽古で「助力の支え」を獲得
 * @param hpCostDown 体力消費ダウン
 * @param practiceHpCostDown 演目稽古の体力消費ダウン
 * @param mainStatusUp メイン基礎能力の獲得量アップ
 * @param statusLimitUp 基礎能力の獲得上限アップ
 * @param additionalPracticeRate 追加の稽古の発生率
 * @param practiceFriendBonus 演目稽古の友情ボーナス
 * @param addMember 演目担当の編成サポカが追加で別のトレーニングに現れる
 * @param campAddMember 合宿中に演目担当の編成サポカから追加で1人トレーニングに現れる
 * @param failureRateDownHp 体力がこの値以上なら失敗率100%ダウン
 * @param practiceTrainingEffect 演目稽古のトレーニング効果アップ
 * @param subStatusUp サブ基礎能力の獲得量アップ
 */
data class ShuntaisaiTeachingEffect(
    val relationBonus: Int = 0,
    val practiceMainStatusUp: Int = 0,
    val specialityRate: Int = 0,
    val hintRate: Int = 0,
    val practiceHintCountUp: Int = 0,
    val nonFriendTrainingEffect: Int = 0,
    val joryokuSupport: Boolean = false,
    val hpCostDown: Int = 0,
    val practiceHpCostDown: Int = 0,
    val mainStatusUp: Int = 0,
    val statusLimitUp: Int = 0,
    val additionalPracticeRate: Int = 0,
    val friendBonus: Int = 0,
    val practiceFriendBonus: Int = 0,
    val addMember: Int = 0,
    val campAddMember: Int = 0,
    val failureRateDownHp: Int? = null,
    val practiceTrainingEffect: Int = 0,
    val subStatusUp: Int = 0,
) {
    companion object {
        val Empty = ShuntaisaiTeachingEffect()
    }

    operator fun plus(other: ShuntaisaiTeachingEffect) = ShuntaisaiTeachingEffect(
        relationBonus = maxOf(relationBonus, other.relationBonus),
        practiceMainStatusUp = practiceMainStatusUp + other.practiceMainStatusUp,
        specialityRate = specialityRate + other.specialityRate,
        hintRate = hintRate + other.hintRate,
        practiceHintCountUp = practiceHintCountUp + other.practiceHintCountUp,
        nonFriendTrainingEffect = nonFriendTrainingEffect + other.nonFriendTrainingEffect,
        joryokuSupport = joryokuSupport || other.joryokuSupport,
        hpCostDown = hpCostDown + other.hpCostDown,
        practiceHpCostDown = practiceHpCostDown + other.practiceHpCostDown,
        mainStatusUp = mainStatusUp + other.mainStatusUp,
        statusLimitUp = statusLimitUp + other.statusLimitUp,
        additionalPracticeRate = additionalPracticeRate + other.additionalPracticeRate,
        friendBonus = friendBonus + other.friendBonus,
        practiceFriendBonus = practiceFriendBonus + other.practiceFriendBonus,
        addMember = addMember + other.addMember,
        campAddMember = campAddMember + other.campAddMember,
        failureRateDownHp = listOfNotNull(failureRateDownHp, other.failureRateDownHp).minOrNull(),
        practiceTrainingEffect = practiceTrainingEffect + other.practiceTrainingEffect,
        subStatusUp = subStatusUp + other.subStatusUp,
    )
}

/**
 * 教え
 *
 * @param period 伝受する時期（0:ジュニア3ターン目、1:クラシック年始、2:シニア年始）
 * @param effects 演目Lv1～10の効果
 * @param transformEffect 転成で消費された場合の効果（転成元の効果はこの効果に変化する、演目Lvによらず固定）
 */
enum class ShuntaisaiTeaching(
    val displayName: String,
    val period: Int,
    val description: String,
    private val effects: List<ShuntaisaiTeachingEffect>,
    val transformEffect: ShuntaisaiTeachingEffect,
) {
    KIZUNA(
        "絆", 0,
        "全編成サポカの絆ゲージ+3、演目稽古でメイン基礎能力の獲得量アップ",
        listOf(3, 5, 7, 9, 10, 11, 12, 13, 14, 15).map {
            ShuntaisaiTeachingEffect(relationBonus = 3, practiceMainStatusUp = it)
        },
        ShuntaisaiTeachingEffect(practiceMainStatusUp = 15, campAddMember = 1),
    ),

    TOKUI(
        "得意率", 0,
        "得意率アップ",
        listOf(10, 13, 16, 20, 25, 30, 35, 40, 45, 50).map {
            ShuntaisaiTeachingEffect(specialityRate = it)
        },
        ShuntaisaiTeachingEffect(specialityRate = 50, practiceFriendBonus = 10),
    ),

    HINT(
        "ヒント", 0,
        "ヒント発生率アップ",
        listOf(30, 33, 36, 40, 45, 50, 55, 60, 70, 80).map {
            ShuntaisaiTeachingEffect(hintRate = it)
        },
        ShuntaisaiTeachingEffect(hintRate = 80, practiceHintCountUp = 1),
    ),

    JORYOKU(
        "助力", 0,
        "友情非発生時のトレーニング効果アップ、演目稽古で助力の支え獲得",
        listOf(20, 22, 24, 26, 28, 30, 32, 34, 37, 40).map {
            ShuntaisaiTeachingEffect(nonFriendTrainingEffect = it, joryokuSupport = true)
        },
        ShuntaisaiTeachingEffect(nonFriendTrainingEffect = 20, joryokuSupport = true),
    ),

    TAIRYOKU(
        "体力", 0,
        "体力消費ダウン",
        listOf(1, 2, 3, 4, 5, 6, 8, 10, 12, 15).map {
            ShuntaisaiTeachingEffect(hpCostDown = it)
        },
        ShuntaisaiTeachingEffect(practiceHpCostDown = 15),
    ),

    TOKKA(
        "特化", 1,
        "メイン基礎能力の獲得量アップ、基礎能力の獲得上限アップ",
        listOf(5, 6, 7, 8, 9, 10, 13, 16, 20, 25).map {
            ShuntaisaiTeachingEffect(mainStatusUp = it, statusLimitUp = it)
        },
        ShuntaisaiTeachingEffect.Empty,
    ),

    KENSAN(
        "研鑽", 1,
        "演目担当が参加するトレーニングで追加の稽古が発生",
        listOf(20, 22, 24, 27, 30, 35, 40, 50, 65, 80).map {
            ShuntaisaiTeachingEffect(additionalPracticeRate = it)
        },
        ShuntaisaiTeachingEffect.Empty,
    ),

    KYORYOKU(
        "協力", 2,
        "友情ボーナス、演目担当のサポカが別のトレーニングにも追加出現",
        listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map {
            ShuntaisaiTeachingEffect(friendBonus = it, addMember = 1)
        },
        ShuntaisaiTeachingEffect.Empty,
    ),

    FUKUTSU(
        "不屈", 2,
        "体力が一定以上なら失敗率ダウン",
        listOf(45, 40, 35, 30, 25, 20, 15, 10, 5, 0).zip(listOf(0, 0, 0, 0, 0, 0, 0, 1, 3, 5)) { hp, effect ->
            ShuntaisaiTeachingEffect(failureRateDownHp = hp, practiceTrainingEffect = effect)
        },
        ShuntaisaiTeachingEffect.Empty,
    ),

    RYODO(
        "両道", 2,
        "サブ基礎能力の獲得量アップ、演目稽古のトレーニング効果アップ",
        listOf(1, 2, 3, 4, 5, 8, 11, 15, 20, 25).zip(listOf(1, 2, 3, 4, 5, 7, 9, 11, 13, 15)) { sub, effect ->
            ShuntaisaiTeachingEffect(subStatusUp = sub, practiceTrainingEffect = effect)
        },
        ShuntaisaiTeachingEffect.Empty,
    ),
    ;

    fun effect(level: Int) = effects[(level - 1).coerceIn(0, effects.lastIndex)]

    companion object {
        fun byPeriod(period: Int) = entries.filter { it.period == period }
    }
}

/**
 * シナリオリンクキャラの担当演目（サポカのタイプによらず固定）
 */
val shuntaisaiLinkPerformance = mapOf(
    "ダイワスカーレット" to StatusType.SPEED,
    "マヤノトップガン" to StatusType.POWER,
    "ユキノビジン" to StatusType.GUTS,
    "ノーリーズン" to StatusType.STAMINA,
    "ブラストワンピース" to StatusType.WISDOM,
)


/**
 * 1演目に割り当てられる編成サポカの最大数（超えた分は別の演目に割り振られる）
 */
const val shuntaisaiMaxSupportPerPerformance = 2

/**
 * ゲスト（編成外の演目担当）の人数（ジュニア3ターン目）
 */
const val shuntaisaiGuestCount = 6

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
 * 駿大祭合宿（ジュニアにも合宿あり）の開始ターンと期間
 */
val shuntaisaiCampStartTurns = listOf(13, 37, 61)
const val shuntaisaiCampLength = 4

/**
 * 目標レースが無い場合の駿大祭合宿
 */
val shuntaisaiCampTurns = calcShuntaisaiCampTurns(emptySet())

/**
 * 駿大祭合宿のターン
 *
 * 合宿期間中の目標レース数だけ延長される
 */
fun calcShuntaisaiCampTurns(goalRaceTurns: Set<Int>): List<Int> {
    return shuntaisaiCampStartTurns.flatMap { start ->
        buildList {
            var turn = start
            var remaining = shuntaisaiCampLength
            while (remaining > 0) {
                add(turn)
                if (!goalRaceTurns.contains(turn)) remaining--
                turn++
            }
        }
    }
}

/**
 * URAファイナルズ期間の開始ターン
 */
const val shuntaisaiFinalsStartTurn = 73

/**
 * URAファイナルズ期間のトレーニングで追加される体力、やる気
 */
val shuntaisaiFinalsTrainingStatus = Status(hp = 30, motivation = 1)

/**
 * 追加の稽古（上の選択肢）の効果
 */
const val shuntaisaiAdditionalPracticeStatus = 20
const val shuntaisaiAdditionalPracticeSkillPt = 20
const val shuntaisaiAdditionalPracticeHp = -5

/**
 * 助力の支えの効果（トレーニング効果アップ）と回数
 */
const val shuntaisaiJoryokuSupportEffect = 5
const val shuntaisaiJoryokuSupportCount = 3

/**
 * 演目稽古の効果（演目Lvごと）
 *
 * allType～で始まる効果は、編成サポカのタイプが5種類以上の場合のみ有効
 *
 * @param statusUp 基礎能力の上昇量（全基礎能力）
 * @param allTypeSkillPtTrainingEffect スキルPtのトレーニング効果アップ
 * @param allTypeAllHintEffect 選択したトレーニングの全ヒントイベントの効果が発動（Lv1～3はヒントイベントの発生のみ）
 * @param allTypeStatusLimitUp 基礎能力の獲得上限アップ
 * @param allTypeSkillPtLimitUp スキルPtの獲得上限アップ
 */
data class ShuntaisaiPracticeEffect(
    val statusUp: Int,
    val friendBonus: Int,
    val allTypeSkillPtTrainingEffect: Int,
    val allTypeAllHintEffect: Boolean,
    val allTypeStatusLimitUp: Int,
    val allTypeSkillPtLimitUp: Int,
)

private val shuntaisaiPracticeEffectData = listOf(
    ShuntaisaiPracticeEffect(10, 2, 20, false, 0, 0),
    ShuntaisaiPracticeEffect(10, 3, 25, false, 0, 0),
    ShuntaisaiPracticeEffect(10, 4, 30, false, 0, 0),
    ShuntaisaiPracticeEffect(10, 5, 40, true, 0, 10),
    ShuntaisaiPracticeEffect(10, 6, 50, true, 0, 20),
    ShuntaisaiPracticeEffect(10, 7, 60, true, 0, 30),
    ShuntaisaiPracticeEffect(10, 8, 80, true, 0, 50),
    ShuntaisaiPracticeEffect(10, 10, 100, true, 0, 100),
    ShuntaisaiPracticeEffect(10, 12, 135, true, 20, 120),
    ShuntaisaiPracticeEffect(10, 15, 170, true, 25, 120),
)

fun shuntaisaiPracticeEffect(level: Int) =
    shuntaisaiPracticeEffectData[(level - 1).coerceIn(0, shuntaisaiPracticeEffectData.lastIndex)]

/**
 * 進捗発表会/本番の大成功条件演目Lv（ジュニア、クラシック、本番）
 *
 * 全演目がこのLv以上なら大成功、それ以外は成功
 */
val shuntaisaiGreatTargetLevel = listOf(3, 5, 6)

enum class ShuntaisaiPresentationResult(val displayName: String) {
    SUCCESS("成功"),
    GREAT_SUCCESS("大成功"),
}

/**
 * 駿大祭ボーナス効果
 */
data class ShuntaisaiPresentationBonus(
    val friendBonus: Int = 0,
    val specialityRate: Int = 0,
    val hintRate: Int = 0,
    val raceBonus: Int = 0,
) {
    companion object {
        val Empty = ShuntaisaiPresentationBonus()

        /**
         * 第1回進捗発表会前
         */
        val Initial = ShuntaisaiPresentationBonus(specialityRate = 20)
    }
}

/**
 * 駿大祭ボーナス効果（第1回進捗発表会、第2回進捗発表会、午式駿大祭）
 */
val shuntaisaiPresentationBonus = listOf(
    mapOf(
        ShuntaisaiPresentationResult.SUCCESS to ShuntaisaiPresentationBonus(0, 20, 20),
        ShuntaisaiPresentationResult.GREAT_SUCCESS to ShuntaisaiPresentationBonus(5, 40, 50),
    ),
    mapOf(
        ShuntaisaiPresentationResult.SUCCESS to ShuntaisaiPresentationBonus(5, 40, 50),
        ShuntaisaiPresentationResult.GREAT_SUCCESS to ShuntaisaiPresentationBonus(10, 60, 100),
    ),
    mapOf(
        ShuntaisaiPresentationResult.SUCCESS to ShuntaisaiPresentationBonus(10, 60, 100, 30),
        ShuntaisaiPresentationResult.GREAT_SUCCESS to ShuntaisaiPresentationBonus(20, 80, 150, 100),
    ),
)

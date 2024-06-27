package io.github.mee1080.umasim.data

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

data class SupportCardSpecialUnique(
    val type: Int,
    val value0: Int,
    val value1: Int,
    val value2: Int,
    val value3: Int,
    val value4: Int,
) {
    val description
        get() = when (type) {
            101 -> "絆${value0}以上で${supportEffectName[value1]}$value2" + if (value3 > 0) "、${supportEffectName[value3]}$value4" else ""
            102 -> "絆${value0}以上で非得意トレーニング効果$value1"
            103 -> "サポカタイプ数${value0}以上でトレーニング効果$value1"
            104 -> "ファン数${value0}ごとにトレーニング効果1（最大${value1}）"
            105 -> "編成サポカタイプに応じて初期ステータス上昇（得意ステ+10 or 友人/グループの場合+2×5）"
            106 -> "${supportEffectName[value1]}が友情トレーニング回数×${value2}（最大${value0}回）"
            107 -> "現在体力30で${supportEffectName[value0]}15、体力1増えるごとに-0.15（小数点以下切り捨て）（${type},${value0},${value1},${value2},${value3},${value4}）"
            108 -> "体力最大値100で${supportEffectName[value0]}${value3}、体力最大値1増えるごとに+0.$value2（最大${value4}、小数点以下切り捨て）"
            109 -> "${supportEffectName[value0]}が合計絆÷${value1}"
            110 -> "${supportEffectName[value0]}が参加サポートカード数×${value1}"
            111 -> "${supportEffectName[value0]}トレーニングLv×$value1"
            112 -> "トレーニング失敗率が$value0%の確率で0%になる"
            113 -> "友情トレーニングで${supportEffectName[value0]}$value1"
            114 -> "${supportEffectName[value0]}が${value2}-(100-体力)/${value1}（小数点以下切り捨て）、最大+20"
            115 -> "全員の${supportEffectName[value0]}+${value1}"
            116 -> "${supportEffectName[value1]}が${skillEffectName[value0] ?: "($value0)"}スキル所持数×${value2}（最大${value3}個）"
            117 -> "${supportEffectName[value0]}が合計トレーニングLv×1（最大${value2}）"
            118 -> "絆${value1}以上でトレーニング最大2箇所に配置"
            119 -> "絆${value2}以上でサポカ配置率アップ"
            120 -> "絆${value1}以上で編成サポカに応じたステータス/スキルボーナス（各最大${value3}）"
            121 -> "トレーニングの全員の絆上昇量常時+$value0、このサポカが参加した場合さらに+$value1"
            else -> if (supportEffectName.containsKey(type)) {
                "${supportEffectName[type]}$value0"
            } else "不明（${type},${value0},${value1},${value2},${value3},${value4}）"
        }

    val targetRelation = when (type) {
        101, 102 -> value0
        118, 120 -> value1
        119 -> value2
        else -> null
    }

    val needCheckFriendCount = type == 106

    /*
     * TODO
     * 9 to "初期スピードアップ"（サポートタイプ数依存のみ実装済）
     * 10 to "初期スタミナアップ"（サポートタイプ数依存のみ実装済）
     * 11 to "初期パワーアップ"（サポートタイプ数依存のみ実装済）
     * 12 to "初期根性アップ"（サポートタイプ数依存のみ実装済）
     * 13 to "初期賢さアップ"（サポートタイプ数依存のみ実装済）
     * 14 to "初期絆ゲージアップ"（全員の初期絆アップのみ実装済）
     * 15 to "レースボーナス"
     * 16 to "ファン数ボーナス"
     * 17 to "ヒントLvアップ"
     * 18 to "ヒント発生率アップ"
     * 25 to "イベント回復量アップ"
     * 26 to "イベント効果アップ"
     * 27 to "失敗率ダウン"（確率で0のみ実装済）
     */

    /**
     * 1 to "友情ボーナス"
     */
    fun friendFactor(
        card: SupportCard,
        condition: SpecialUniqueCondition,
    ): Int {
        return when (type) {
            107 -> if (value0 == 1) {
                // TODO データ解釈
                15 - ((max(30, condition.status.hp) - 30) * 15 / 100.0).toInt()
            } else 0

            else -> getValue(card, condition, 1)
        }
    }

    /**
     * 2 to "やる気効果アップ"
     */
    fun getMotivation(
        card: SupportCard,
        condition: SpecialUniqueCondition,
    ): Int {
        return getValue(card, condition, 2)
    }

    /**
     * 3 to "スピードボーナス"
     * 4 to "スタミナボーナス"
     * 5 to "パワーボーナス"
     * 6 to "根性ボーナス"
     * 7 to "賢さボーナス"
     * 30 to "スキルPtボーナス"
     * 41 to "全ステータスボーナス"
     */
    fun getBaseBonus(
        statusType: StatusType,
        card: SupportCard,
        condition: SpecialUniqueCondition,
    ): Int {
        if (type == 120) {
            if (condition.relation < value1) return 0
            val value = if (statusType == StatusType.SKILL) {
                condition.supportCount.getOrElse(StatusType.FRIEND) { 0 } + condition.supportCount.getOrElse(StatusType.GROUP) { 0 }
            } else {
                condition.supportCount.getOrElse(statusType) { 0 }
            }
            return min(2, value)
        }
        val target = when (statusType) {
            StatusType.SPEED -> 3
            StatusType.STAMINA -> 4
            StatusType.POWER -> 5
            StatusType.GUTS -> 6
            StatusType.WISDOM -> 7
            StatusType.SKILL -> 30
            else -> 0
        }
        if (target == 0) {
            println("${card.name} : 特殊固有未実装 $description")
            return 0
        }
        val allBonus = if (type == 101 && condition.relation >= value0 && statusType != StatusType.SKILL) {
            (if (value1 == 41) value2 else 0) + (if (value3 == 41) value4 else 0)
        } else 0
        return allBonus + getValue(card, condition, target)
    }

    /**
     * 8 to "トレーニング効果アップ"
     */
    fun trainingFactor(
        card: SupportCard,
        condition: SpecialUniqueCondition,
    ): Int {
        return when (type) {
            102 -> if (condition.relation >= value0 && condition.trainingType != card.type) {
                value1
            } else 0

            103 -> if (condition.supportTypeCount >= value0) {
                value1
            } else 0

            104 -> min(value1, condition.fanCount / value0)

            117 -> if (value0 == 8) {
                // TODO value1の解釈
                min(value2, condition.totalTrainingLevel)
            } else 0

            else -> getValue(card, condition, 8)
        }
    }

    /**
     * 19 to "得意率アップ"
     */
    fun specialityRate(
        card: SupportCard,
        condition: SpecialUniqueCondition,
    ) = getValue(card, condition, 19)

    /**
     * 28 to "体力消費ダウン"
     */
    fun hpCost(
        card: SupportCard,
        condition: SpecialUniqueCondition,
    ) = getValue(card, condition, 28)

    /**
     * 31 to "賢さ友情回復量アップ"
     */
    fun wisdomFriendRecovery(
        card: SupportCard,
        condition: SpecialUniqueCondition,
    ) = getValue(card, condition, 31)

    fun initialStatus(supportType: List<StatusType>): Status {
        return if (type == 105) {
            supportType.fold(Status()) { total, type ->
                when (type) {
                    StatusType.FRIEND, StatusType.GROUP -> total + Status(2, 2, 2, 2, 2)
                    else -> total.add(type to 10)
                }
            }
        } else Status()
    }

    val initialRelationAll = if (type == 115 && value0 == 14) value1 else 0

    fun failureRate(
    ): Int {
        return if (type == 112) {
            if (Random.nextInt(100) < value0) 0 else 1
        } else 1
    }

    fun hasSecondPosition(relation: Int) = type == 118 && relation >= value1

    fun positionRateUp(relation: Int) = type == 119 && relation >= value2

    private fun getValue(
        card: SupportCard,
        condition: SpecialUniqueCondition,
        target: Int,
    ): Int {
        return when (type) {
            101 -> if (condition.relation >= value0) {
                (if (value1 == target) value2 else 0) + (if (value3 == target) value4 else 0)
            } else 0

            106 -> if (value1 == target) {
                min(value0, condition.friendCount) * value2
            } else 0

            108 -> if (value0 == target) {
                min(value4, ((condition.status.maxHp - value1) * value2 / 100.0 + value3).toInt())
            } else 0

            109 -> if (value0 == target) {
                condition.totalRelation / value1
            } else 0

            110 -> if (value0 == target) {
                condition.trainingSupportCount * value1
            } else 0

            111 -> if (value0 == target) {
                min(5, condition.trainingLevel) * value1
            } else 0

            113 -> if (value0 == target && condition.friendTraining) {
                value1
            } else 0

            114 -> if (value0 == target) {
                value2 - max(0, (100 - condition.status.hp) / value1)
            } else 0

            116 -> if (value1 == target) {
                value2 * min(condition.getSkillCount(value0), value3)
            } else 0

            else -> 0
        }
    }

    private fun SpecialUniqueCondition.getSkillCount(type: Int) = when (type) {
        1 -> speedSkillCount
        2 -> accelSkillCount
        3 -> healSkillCount
        else -> 0
    }
}

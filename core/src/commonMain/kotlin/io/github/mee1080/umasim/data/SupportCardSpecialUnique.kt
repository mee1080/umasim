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
            115 -> "全員の初期絆+5（${type},${value0},${value1},${value2},${value3},${value4}）"
            else -> if (supportEffectName.containsKey(type)) {
                "${supportEffectName[type]}$value0"
            } else "不明（${type},${value0},${value1},${value2},${value3},${value4}）"
        }

    val targetRelation = when (type) {
        101, 102 -> value0
        else -> null
    }

    val needCheckFriendCount = type == 106

    fun getMotivation(
        relation: Int,
        friendTraining: Boolean,
    ): Int {
        return if (type == 101 && relation >= value0 && value1 == 2) {
            value2
        } else if (type == 113 && friendTraining && value0 == 2) {
            value1
        } else 0
    }

    fun getBaseBonus(statusType: StatusType, relation: Int): Int {
        return if (type == 101 && relation >= value0) {
            listOf(value1 to value2, value3 to value4).sumOf {
                val targetType = when (it.first) {
                    3 -> StatusType.SPEED
                    4 -> StatusType.STAMINA
                    5 -> StatusType.POWER
                    6 -> StatusType.GUTS
                    7 -> StatusType.WISDOM
                    30 -> StatusType.SKILL
                    else -> null
                }
                if (statusType == targetType) it.second else 0
            }
        } else 0
    }

    fun trainingFactor(
        cardType: StatusType,
        trainingType: StatusType,
        trainingLevel: Int,
        relation: Int,
        supportTypeCount: Int,
        fanCount: Int,
        status: Status,
        totalRelation: Int,
        trainingSupportCount: Int,
    ): Int {
        return if (type == 101 && relation >= value0 && value1 == 8) {
            value2
        } else if (type == 102 && relation >= value0 && trainingType != cardType) {
            value1
        } else if (type == 103 && supportTypeCount >= value0) {
            value1
        } else if (type == 104) {
            min(value1, fanCount / value0)
        } else if (type == 108 && value0 == 8) {
            min(value4, ((status.maxHp - value1) * value2 / 100.0 + value3).toInt())
        } else if (type == 109 && value0 == 8) {
            totalRelation / value1
        } else if (type == 110 && value0 == 8) {
            trainingSupportCount * value1
        } else if (type == 111 && value0 == 8) {
            trainingLevel * value1
        } else if (type == 114 && value0 == 8) {
            value2 - max(0, (100 - status.hp) / value1)
        } else 0
    }

    fun friendFactor(
        relation: Int,
        friendCount: Int,
        status: Status,
    ): Int {
        return if (type == 101 && relation >= value0 && value1 == 1) {
            value2
        } else if (type == 106 && value1 == 1) {
            min(value0, friendCount) * value2
        } else if (type == 107 && value0 == 1) {
            15 - ((max(30, status.hp) - 30) * 15 / 100.0).toInt()
        } else 0
    }

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

    fun failureRate(
    ): Int {
        return if (type == 112) {
            if (Random.nextInt(100) < value0) 0 else 1
        } else 1
    }

    fun hpCost(
        friendTraining: Boolean,
    ): Int {
        return if (type == 113 && friendTraining && value0 == 28) {
            value1
        } else 0
    }

    val initialRelationAll = if (type == 115) 5 else 0
}

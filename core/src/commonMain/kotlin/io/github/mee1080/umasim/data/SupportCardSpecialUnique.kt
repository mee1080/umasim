package io.github.mee1080.umasim.data

import kotlin.math.min

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
            101 -> "絆${value0}以上で${supportEffectName[value1]}$value2"
            102 -> "絆${value0}以上で非得意トレーニング効果$value1"
            103 -> "サポカタイプ数${value0}以上でトレーニング効果$value1"
            104 -> "ファン数${value0}ごとにトレーニング効果1（最大${value1}）"
            105 -> "サポカタイプに応じて初期ステータスアップ（得意ステ+10 or +2×5）？"
            106 -> "友情トレーニングのたびに友情ボーナスアップ？"
            else -> if (supportEffectName.containsKey(type)) {
                "${supportEffectName[type]}$value0"
            } else "不明（${type},${value0},${value1},${value2},${value3},${value4}）"
        }

    val targetRelation = when (type) {
        101, 102 -> value0
        else -> null
    }

    val needCheckFriendCount = type == 106

    fun getMotivation(relation: Int): Int {
        return if (type == 101 && relation >= value0 && value1 == 2) {
            value2
        } else 0
    }

    fun getBaseBonus(statusType: StatusType, relation: Int): Int {
        return if (type == 101 && relation >= value0) {
            val targetType = when (value1) {
                3 -> StatusType.SPEED
                4 -> StatusType.STAMINA
                5 -> StatusType.POWER
                6 -> StatusType.GUTS
                7 -> StatusType.WISDOM
                30 -> StatusType.SKILL
                else -> null
            }
            if (statusType == targetType) value2 else 0
        } else 0
    }

    fun trainingFactor(
        cardType: StatusType,
        trainingType: StatusType,
        relation: Int,
        supportTypeCount: Int,
        fanCount: Int,
        status: Status,
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
            status.maxHp
        } else 0
    }

    fun friendFactor(
        relation: Int,
        friendCount: Int,
        status: Status,
    ): Int {
        return if (type == 101 && relation > value0 && value1 == 1) {
            value2
        } else if (type == 106) {
            friendCount * 3
        } else if (type == 107 && value0 == 1) {
            status.hp
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
}

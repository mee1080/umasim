package io.github.mee1080.umasim.data

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
            else -> if (supportEffectName.containsKey(type)) {
                "${supportEffectName[type]}$value0"
            } else "不明（${type},${value0},${value1},${value2},${value3},${value4}）"
        }

    fun getBaseBonus(statusType: StatusType, relation: Int): Int {
        return if (type == 101 && relation >= value0) {
            val targetType = when (value1) {
                3 -> StatusType.SPEED
                4 -> StatusType.STAMINA
                5 -> StatusType.POWER
                6 -> StatusType.GUTS
                7 -> StatusType.WISDOM
                else -> null
            }
            if (statusType == targetType) value2 else 0
        } else 0
    }

    fun trainingFactor(trainingType: StatusType, relation: Int): Int {
        return if (type == 102 && relation >= value0 && trainingType != StatusType.GUTS) {
            value1
        } else 0
    }
}

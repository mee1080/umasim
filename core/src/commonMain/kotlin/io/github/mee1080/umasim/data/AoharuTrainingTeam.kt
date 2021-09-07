package io.github.mee1080.umasim.data

data class AoharuTrainingTeam(
    val type: StatusType,
    val level: Int,
    val minStatus: Status,
    val maxStatus: Status,
) {
    fun getRandomStatus(chara: Chara) = Status(
        applyBonus((minStatus.speed..maxStatus.speed).random(), chara.speedBonus),
        applyBonus((minStatus.stamina..maxStatus.stamina).random(), chara.staminaBonus),
        applyBonus((minStatus.power..maxStatus.power).random(), chara.powerBonus),
        applyBonus((minStatus.guts..maxStatus.guts).random(), chara.gutsBonus),
        applyBonus((minStatus.wisdom..maxStatus.wisdom).random(), chara.wisdomBonus),
    )

    private fun applyBonus(value: Int, bonus: Int) = value * (100 + bonus) / 100
}
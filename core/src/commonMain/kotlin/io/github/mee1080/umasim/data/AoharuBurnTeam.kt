package io.github.mee1080.umasim.data

data class AoharuBurnTeam(
    val type: StatusType,
    val status: Status,
) {
    fun getStatus(chara: Chara) = Status(
        applyBonus(status.speed, chara.speedBonus),
        applyBonus(status.stamina, chara.staminaBonus),
        applyBonus(status.power, chara.powerBonus),
        applyBonus(status.guts, chara.gutsBonus),
        applyBonus(status.wisdom, chara.wisdomBonus),
    )

    private fun applyBonus(value: Int, bonus: Int) = value * (100 + bonus) / 1000 * 10
}
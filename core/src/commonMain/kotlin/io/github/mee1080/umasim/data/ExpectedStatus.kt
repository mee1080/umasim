package io.github.mee1080.umasim.data

data class ExpectedStatus(
    override val speed: Double = 0.0,
    override val stamina: Double = 0.0,
    override val power: Double = 0.0,
    override val guts: Double = 0.0,
    override val wisdom: Double = 0.0,
    override val skillPt: Double = 0.0,
    override val hp: Double = 0.0,
    override val motivation: Double = 0.0,
    override val maxHp: Double = 0.0,
) : StatusValues {

    val statusTotal get() = speed + stamina + power + guts + wisdom

    operator fun plus(status: Status) = add(1.0, status)

    fun add(rate: Double, status: Status) = ExpectedStatus(
        speed + status.speed * rate,
        stamina + status.stamina * rate,
        power + status.power * rate,
        guts + status.guts * rate,
        wisdom + status.wisdom * rate,
        skillPt + status.skillPt * rate,
        hp + status.hp * rate,
        motivation + status.motivation * rate,
        maxHp + status.maxHp * rate,
    )

    fun get(statusType: StatusType): Double {
        return when (statusType) {
            StatusType.SPEED -> speed
            StatusType.STAMINA -> stamina
            StatusType.POWER -> power
            StatusType.GUTS -> guts
            StatusType.WISDOM -> wisdom
            StatusType.SKILL -> skillPt
            else -> 0.0
        }
    }
}
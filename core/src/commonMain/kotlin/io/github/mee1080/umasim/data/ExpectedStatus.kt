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
    val performance: ExpectedPerformance? = null,
) : StatusValues {

    val statusTotal by lazy { speed + stamina + power + guts + wisdom }

    val totalPlusSkillPt by lazy { statusTotal + skillPt }

    val totalPlusSkillPtPerformance by lazy { totalPlusSkillPt + (performance?.totalValue ?: 0.0) / 2.0 }

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
        performance?.add(rate, status.performance),
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

    operator fun plus(status: ExpectedStatus) = ExpectedStatus(
        speed + status.speed,
        stamina + status.stamina,
        power + status.power,
        guts + status.guts,
        wisdom + status.wisdom,
        skillPt + status.skillPt,
        hp + status.hp,
        motivation + status.motivation,
        maxHp + status.maxHp,
        performance?.plus(status.performance),
    )

    operator fun div(divider: Double) = ExpectedStatus(
        speed / divider,
        stamina / divider,
        power / divider,
        guts / divider,
        wisdom / divider,
        skillPt / divider,
        hp / divider,
        motivation / divider,
        maxHp / divider,
        performance?.div(divider),
    )

    fun enablePerformance() = copy(performance = ExpectedPerformance())
}

data class ExpectedPerformance(
    val dance: Double = 0.0,
    val passion: Double = 0.0,
    val vocal: Double = 0.0,
    val visual: Double = 0.0,
    val mental: Double = 0.0,
) {

    fun add(rate: Double, other: Performance?) = if (other == null) this else ExpectedPerformance(
        dance + other.dance * rate,
        passion + other.passion * rate,
        vocal + other.vocal * rate,
        visual + other.visual * rate,
        mental + other.mental * rate,
    )

    operator fun plus(other: ExpectedPerformance?) = if (other == null) this else ExpectedPerformance(
        dance + other.dance,
        passion + other.passion,
        vocal + other.vocal,
        visual + other.visual,
        mental + other.mental,
    )

    operator fun div(divider: Double) = ExpectedPerformance(
        dance / divider,
        passion / divider,
        vocal / divider,
        visual / divider,
        mental / divider,
    )

    val totalValue by lazy { dance + passion + vocal + visual + mental }
}
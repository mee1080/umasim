package io.github.mee1080.umasim.simulation

import io.github.mee1080.umasim.data.Status

data class ExpectedStatus(
    val speed: Double = 0.0,
    val stamina: Double = 0.0,
    val power: Double = 0.0,
    val guts: Double = 0.0,
    val wisdom: Double = 0.0,
    val skillPt: Double = 0.0,
    val hp: Double = 0.0,
    val motivation: Double = 0.0,
    val maxHp: Double = 0.0,
) {

    val statusTotal get() = speed + stamina + power + guts + wisdom

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
}
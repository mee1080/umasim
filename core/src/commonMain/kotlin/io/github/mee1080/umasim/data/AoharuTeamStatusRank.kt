package io.github.mee1080.umasim.data

data class AoharuTeamStatusRank(
    val rank: String,
    val threshold: Int,
    val minStatus: Int,
    val maxStatus: Int,
    val maxBonus: Int,
) {
    fun getRandomStatus() = (minStatus..maxStatus).random()

    fun getAverageStatus() = (minStatus..maxStatus).average()
}
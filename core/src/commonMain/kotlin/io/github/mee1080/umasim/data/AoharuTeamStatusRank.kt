package io.github.mee1080.umasim.data

data class AoharuTeamStatusRank(
    val ordinal: Int,
    val rank: String,
    val threshold: Int,
    val minStatus: Int,
    val maxStatus: Int,
    val maxBonus: Int,
    val trainingLevel: Int,
) {
    fun getRandomStatus() = (minStatus..maxStatus).random()

    fun getAverageStatus() = (minStatus..maxStatus).average()

    val next get() = Store.Aoharu.teamStatusRank.values.firstOrNull { it.ordinal == ordinal - 1 }
}
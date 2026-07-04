package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.scenario.ramen.*

data class RamenState(
    val turn: Int = 1,
    val excitementPt: Int = 0,
    val activeTastingRegion: RamenRegion? = null,
) {
    fun toRamenStatus(): RamenStatus {
        val period = (turn - 1) / 24
        val rmjBonus = if (period == 0) {
            RamenBaseBonus(0, 0, 0, 0)
        } else {
            ramenRmjBonus[period - 1][1]
        }
        val targetExcitePt = ramenTargetExcitePt[period]
        val regionRank = (excitementPt * 5 / targetExcitePt).coerceAtMost(5)
        val regionRankBonus = ramenRegionRankBonus[regionRank]

        return RamenStatus(
            turn = turn,
            excitementPt = excitementPt,
            activeTastingRegion = activeTastingRegion?.let { it to regionRankBonus },
            rmjBonus = rmjBonus
        )
    }
}

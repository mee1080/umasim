package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Lesson
import io.github.mee1080.umasim.data.ShopItem
import io.github.mee1080.umasim.data.Status

class SimulationHistoryItem(
    val action: Action,
    val status: Status,
    val state: SimulationState,
    val useItem: List<ShopItem>,
    val purchasedLesson: List<Lesson>,
) {
    @Deprecated("use action", ReplaceWith("action"))
    val first
        get() = action

    @Deprecated("use status", ReplaceWith("status"))
    val second
        get() = status

    @Deprecated("use state", ReplaceWith("state"))
    val third
        get() = state
}
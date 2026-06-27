package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType

object RamenStore {
    enum class TipType(val displayName: String) {
        NOODLE("麺"),
        SOUP("スープ"),
        TOPPING("トッピング"),
        HIDDEN("隠し味")
    }

    data class TipCost(
        val noodle: Int = 0,
        val soup: Int = 0,
        val topping: Int = 0,
    )

    enum class Region(
        val displayName: String,
        val ramenName: String,
        val periods: Set<Int>, // 1: Junior, 2: Classic, 3: Senior
        val tipCost: TipCost,
        val effect: Status = Status(),
        val description: String = ""
    ) {
        SAPPORO("札幌", "濃厚味噌ラーメン", setOf(1, 3), TipCost(2, 2, 1), description = "スピトレ効果+20"),
        HAKODATE("函館", "函館塩ラーメン", setOf(1, 3), TipCost(1, 1, 1)), // Placeholder
        NIIGATA("新潟", "岩のり背脂ラーメン", setOf(1, 3), TipCost(3, 1, 1), description = "パワートレ効果+20"),
        FUKUSHIMA("福島", "福島ラーメン", setOf(1, 3), TipCost(1, 1, 1)), // Placeholder
        TOKYO("東京", "ガッツリ豚ラーメン", setOf(1, 3), TipCost(1, 1, 1)), // Placeholder

        NAKAYAMA("中山", "中山ラーメン", setOf(2, 3), TipCost(1, 1, 1)), // Placeholder
        CHUKYO("中京", "中京ラーメン", setOf(2, 3), TipCost(1, 1, 1)), // Placeholder
        KYOTO("京都", "背脂ねぎラーメン", setOf(2, 3), TipCost(1, 1, 1), description = "スタ根性友情ボーナス"),
        HANSHIN("阪神", "阪神ラーメン", setOf(2, 3), TipCost(1, 1, 1)), // Placeholder
        KOKURA("小倉", "小倉ラーメン", setOf(2, 3), TipCost(1, 1, 1)), // Placeholder

        FINALS("ファイナルズ", "超RMJ極", setOf(4), TipCost(0, 0, 0))
    }
}

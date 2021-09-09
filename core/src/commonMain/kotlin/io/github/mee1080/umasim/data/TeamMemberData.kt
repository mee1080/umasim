package io.github.mee1080.umasim.data

data class TeamMemberData(
    val supportCardId: Int,
    val chara: String,
    val type: StatusType,
    val rarity: Int,
    val initialStatus: Status,
    val maxStatus: Status,
)
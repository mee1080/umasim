package io.github.mee1080.umasim.data

data class AoharuTraining(
    val type: StatusType,
    val count: Int,
    val status: Status,
) {
    companion object {
        internal const val COUNT_BURN = 6
        internal const val COUNT_BURN_LINK = 7
    }
}
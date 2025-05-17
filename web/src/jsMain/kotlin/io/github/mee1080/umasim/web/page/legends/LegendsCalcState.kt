package io.github.mee1080.umasim.web.page.legends

import io.github.mee1080.umasim.scenario.legend.*
import io.github.mee1080.utility.decodeFromStringOrNull
import io.github.mee1080.utility.encodeToString
import kotlinx.browser.localStorage
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

private const val KEY_LEGEND_CALC_STATE = "umasim.legendCalcState"

@Serializable
data class LegendsCalcState(
    @Transient
    val buffList: List<LegendsCalcBuffState> = legendBuffList.filter {
        it.effect.friendBonus > 0 || it.effect.trainingBonus > 0 || it.effect.motivationBonus > 0
    }.map { LegendsCalcBuffState(it) },

    @Transient
    val sortedBuffList: List<LegendsCalcBuffState> = emptyList(),

    @Transient
    val currentFactor: LegendCalcFactorInfo = LegendCalcFactorInfo(1.0, 1.0, 1.0),

    @Transient
    val maxFactor: Double = 1.0,

    val motivation: Int = 2,
    val supportMotivationBonus: Int = 0,
    val supportTrainingBonus: Int = 0,
    val memberCount: Int = 0,
    val friendCount: Int = 0,
    val specialState: LegendMember? = null,
    val bestFriendList: List<LegendsCalcBestFriendState> = List(5) { LegendsCalcBestFriendState() },
) {

    fun update(action: LegendsCalcState.() -> LegendsCalcState): LegendsCalcState {
        return action().calcRate()
    }

    fun calcRate(): LegendsCalcState {
        val currentBuffList = buffList.filter { it.checked }.map { LegendBuffState(it.buff, true) }
        val newCurrentFactor = calc(currentBuffList)
        val newBuffList = buffList.map {
            val factor = calc(currentBuffList + LegendBuffState(it.buff, true)).totalFactor
            it.copy(factor = factor)
        }
        val newSortedBuffList = newBuffList.sortedByDescending {
            (if (it.checked) 10000.0 else 0.0) + it.factor
        }
        val newMaxFactor = newBuffList.maxOf { it.factor }
        saveLegendsCalcState(this)
        return copy(
            buffList = newBuffList,
            sortedBuffList = newSortedBuffList,
            currentFactor = newCurrentFactor,
            maxFactor = newMaxFactor,
        )
    }

    private fun calc(list: List<LegendBuffState>): LegendCalcFactorInfo {
        val status = LegendStatus(
            buffList = list.filter {
                it.buff.condition != LegendBuffCondition.Motivation || specialState == LegendMember.Blue || motivation >= 2
            },
            mastery = specialState,
            specialStateTurn = if (specialState == LegendMember.Blue || specialState == LegendMember.Green) 1 else 0,
        )
        val bestFriend = specialState == LegendMember.Red
        val joinBestFriend = bestFriendList.filter { it.join }
        val friendBestFriend = joinBestFriend.filter { it.friend }
        val memberCount = if (bestFriend) joinBestFriend.size else memberCount
        val friendCount = if (bestFriend) friendBestFriend.size else friendCount
        val effect = status.getBuffEffect(memberCount, friendCount)

        val baseFriendBonus = effect.friendBonus
        val bestFriendFriendBonus = if (bestFriend) {
            friendBestFriend.sumOf { it.member.friendBonus }
        } else 0
        val friendFactor = (baseFriendBonus + bestFriendFriendBonus + 100) / 100.0

        val motivationBonus = effect.motivationBonus
        val motivationBase = if (specialState == LegendMember.Blue) 0.55 else motivation / 10.0
        val motivationFactor = 1.0 + motivationBase * (1 + (supportMotivationBonus + motivationBonus) / 100.0)

        val baseTrainingBonus = effect.trainingBonus
        val bestFriendTrainingBonus = if (bestFriend) {
            joinBestFriend.sumOf { it.member.trainingBonus }
        } else 0
        val trainingBonus = baseTrainingBonus + bestFriendTrainingBonus
        val trainingFactor = 1.0 + (supportTrainingBonus + trainingBonus) / 100.0

        return LegendCalcFactorInfo(friendFactor, motivationFactor, trainingFactor)
    }
}

data class LegendsCalcBuffState(
    val buff: LegendBuff,
    val checked: Boolean = false,
    val factor: Double = 0.0,
)

data class LegendCalcFactorInfo(
    val friendFactor: Double,
    val motivationFactor: Double,
    val trainingFactor: Double,
) {
    val totalFactor = friendFactor * motivationFactor * trainingFactor
}

@Serializable
data class LegendsCalcBestFriendState(
    val join: Boolean = false,
    val friend: Boolean = false,
    val guest: Boolean = false,
    val bestFriendLevel: Int = 1,
) {
    @Transient
    val member: LegendMemberState = LegendMemberState(
        guest = guest,
        bestFriendLevel = bestFriendLevel,
        bestFriendGauge = 20,
    )
}

val specialStateSelection = listOf("なし" to null) + LegendMember.entries.map { it.displayName to it }

fun loadLegendsCalcState(): LegendsCalcState {
    return localStorage.getItem(KEY_LEGEND_CALC_STATE)?.let {
        decodeFromStringOrNull<LegendsCalcState>(it)
    } ?: LegendsCalcState()
}

fun saveLegendsCalcState(state: LegendsCalcState) {
    localStorage.setItem(KEY_LEGEND_CALC_STATE, encodeToString(state))
}

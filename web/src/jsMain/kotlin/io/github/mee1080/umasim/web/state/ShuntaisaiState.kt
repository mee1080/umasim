package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.shuntaisai.*
import io.github.mee1080.umasim.simulation2.MemberState

data class ShuntaisaiState(
    val turn: Int = 1,
    val levels: Map<StatusType, Int> = trainingType.associateWith { 1 },
    val teachings: Map<StatusType, ShuntaisaiTeaching> = emptyMap(),
    val transformedFrom: Map<StatusType, ShuntaisaiTeaching> = emptyMap(),
    val presentationResult: ShuntaisaiPresentationResult = ShuntaisaiPresentationResult.GREAT_SUCCESS,
    val guestCounts: Map<StatusType, Int> = emptyMap(),
    val displayPractice: Boolean = false,
) {
    val period get() = (turn - 1) / 24

    val isFinals get() = turn >= shuntaisaiFinalsStartTurn

    val maxLevel get() = shuntaisaiAzumaMaxLevel

    fun toShuntaisaiStatus(supportTypeCount: Int, practice: StatusType? = null): ShuntaisaiStatus {
        val presentationBonus = if (period == 0) ShuntaisaiPresentationBonus.Initial else {
            shuntaisaiPresentationBonus[minOf(period, 3) - 1][presentationResult] ?: ShuntaisaiPresentationBonus.Initial
        }
        return ShuntaisaiStatus(
            turn = turn,
            maxLevel = maxLevel,
            gauges = trainingType.associateWith { if (it == practice) shuntaisaiMaxGauge else 0 },
            levels = levels.mapValues { it.value.coerceAtMost(maxLevel) },
            teachings = teachings.mapValues { (type, teaching) ->
                ShuntaisaiTeachingSlot(teaching, transformedFrom[type]?.takeIf { it.period < teaching.period })
            },
            presentationBonus = presentationBonus,
            supportTypeCount = supportTypeCount,
        )
    }

    /**
     * 練習に参加するゲスト（編成外の演目担当）
     *
     * 担当演目のシナリオリンクキャラが未編成ならそのキャラを優先し、それ以外は同タイプのキャラから選ぶ
     */
    fun createGuests(supportCharaNames: Set<String>, startIndex: Int): List<MemberState> {
        val guests = mutableListOf<MemberState>()
        trainingType.forEach { type ->
            val count = guestCounts[type] ?: 0
            if (count <= 0) return@forEach
            val linkChara = shuntaisaiLinkPerformance.entries.firstOrNull { it.value == type }?.key
            val candidates = listOfNotNull(
                linkChara?.takeIf { !supportCharaNames.contains(it) }?.let { Store.guestSupportCardMap[it] },
            ) + Store.guestSupportCardList.filter {
                it.type == type && !supportCharaNames.contains(it.chara) && !shuntaisaiLinkPerformance.containsKey(it.chara)
            }
            candidates.take(count).forEach { card ->
                guests += MemberState(
                    startIndex + guests.size, card, StatusType.NONE, null, ShuntaisaiMemberState(type),
                )
            }
        }
        return guests
    }
}

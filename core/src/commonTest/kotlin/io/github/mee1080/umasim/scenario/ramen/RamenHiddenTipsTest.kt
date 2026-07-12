package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.simulation2.RamenTasting
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RamenHiddenTipsTest : RamenCalculatorTest(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[心覚えし、京の華]エアグルーヴ" to 4,
        "[アルストロメリアの夢]ヴィブロス" to 4,
        "[Devilish Whispers]スティルインラブ" to 4,
        "[The frontier]ジャングルポケット" to 4,
        "[永久の誓い、永久の輝き]サトノダイヤモンド" to 4,
    )
) {
    @Test
    fun testPredictScenarioAction() {
        var customState = state.copy(
            scenarioStatus = RamenStatus(
                selectedRegions = listOf(RamenRegion.SAPPORO),
                tips = mapOf(
                    RamenTipType.NOODLE to 2,
                    RamenTipType.SOUP to 2,
                    RamenTipType.TOPPING to 1
                ),
                hiddenTips = 0
            )
        )

        // 1. With 0 hiddenTips and full normal tips
        var actions = RamenCalculator.predictScenarioAction(customState, false).filterIsInstance<RamenTasting>()
        println("Actions for 0 hidden tips: ${actions.map { it.toShortString() }}")
        assertEquals(1, actions.size)
        assertTrue(actions[0].changeHiddenTips.isEmpty())

        // 2. With 1 hiddenTips and full normal tips
        customState = customState.updateRamenStatus {
            copy(hiddenTips = 1)
        }
        actions = RamenCalculator.predictScenarioAction(customState, false).filterIsInstance<RamenTasting>()
        println("Actions for 1 hidden tip: ${actions.map { it.toShortString() }}")
        assertEquals(4, actions.size)
        val expectedChanges = listOf(
            emptyList(),
            listOf(RamenTipType.NOODLE),
            listOf(RamenTipType.SOUP),
            listOf(RamenTipType.TOPPING)
        )
        val actualChanges = actions.map { it.changeHiddenTips }
        assertEquals(expectedChanges.toSet(), actualChanges.toSet())

        // 3. Test activateTasting with changeHiddenTips
        val ramenStatus = customState.ramenStatus!!
        val nextRamenStatus = ramenStatus.activateTasting(RamenRegion.SAPPORO, listOf(RamenTipType.NOODLE))
        assertEquals(1, nextRamenStatus.tips[RamenTipType.NOODLE])
        assertEquals(0, nextRamenStatus.tips[RamenTipType.SOUP])
        assertEquals(0, nextRamenStatus.tips[RamenTipType.TOPPING])
        assertEquals(0, nextRamenStatus.hiddenTips)
    }
}

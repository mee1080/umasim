package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.simulation2.RamenTasting
import kotlin.test.Test
import kotlin.test.assertEquals

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
        var expectedChanges = setOf(
            emptyList<RamenTipType>()
        )
        var actions = RamenCalculator.predictScenarioAction(customState, false).filterIsInstance<RamenTasting>()
        var actualChanges = actions.map { it.changeHiddenTips }.toSet()
        assertEquals(expectedChanges, actualChanges)

        // 2. With 1 hiddenTips and full normal tips
        customState = customState.updateRamenStatus {
            copy(hiddenTips = 1)
        }
        actions = RamenCalculator.predictScenarioAction(customState, false).filterIsInstance<RamenTasting>()
        assertEquals(4, actions.size)
        expectedChanges = setOf(
            emptyList(),
            listOf(RamenTipType.NOODLE),
            listOf(RamenTipType.SOUP),
            listOf(RamenTipType.TOPPING)
        )
        actualChanges = actions.map { it.changeHiddenTips }.toSet()
        assertEquals(expectedChanges, actualChanges)

        // 3. Test activateTasting with changeHiddenTips
        val ramenStatus = customState.ramenStatus!!
        val nextRamenStatus = ramenStatus.activateTasting(RamenRegion.SAPPORO, listOf(RamenTipType.NOODLE))
        assertEquals(1, nextRamenStatus.tips[RamenTipType.NOODLE])
        assertEquals(0, nextRamenStatus.tips[RamenTipType.SOUP])
        assertEquals(0, nextRamenStatus.tips[RamenTipType.TOPPING])
        assertEquals(0, nextRamenStatus.hiddenTips)

        // 隠し味2個
        customState = customState.updateRamenStatus {
            copy(
                tips = mapOf(
                    RamenTipType.NOODLE to 2,
                    RamenTipType.SOUP to 2,
                    RamenTipType.TOPPING to 1
                ),
                hiddenTips = 2,
            )
        }
        expectedChanges = setOf(
            emptyList(),
            listOf(RamenTipType.NOODLE),
            listOf(RamenTipType.NOODLE, RamenTipType.NOODLE),
            listOf(RamenTipType.NOODLE, RamenTipType.SOUP),
            listOf(RamenTipType.NOODLE, RamenTipType.TOPPING),
            listOf(RamenTipType.SOUP),
            listOf(RamenTipType.SOUP, RamenTipType.SOUP),
            listOf(RamenTipType.SOUP, RamenTipType.TOPPING),
            listOf(RamenTipType.TOPPING)
        )
        actions = RamenCalculator.predictScenarioAction(customState, false).filterIsInstance<RamenTasting>()
        actualChanges = actions.map { it.changeHiddenTips }.toSet()
        assertEquals(expectedChanges, actualChanges)

        // 隠し味による補充
        customState = customState.updateRamenStatus {
            copy(
                tips = mapOf(
                    RamenTipType.NOODLE to 1,
                    RamenTipType.SOUP to 2,
                    RamenTipType.TOPPING to 5
                ),
                hiddenTips = 3,
            )
        }
        expectedChanges = setOf(
            listOf(RamenTipType.NOODLE),
            listOf(RamenTipType.NOODLE, RamenTipType.NOODLE),
            listOf(RamenTipType.NOODLE, RamenTipType.SOUP),
            listOf(RamenTipType.NOODLE, RamenTipType.TOPPING),
        )
        actions = RamenCalculator.predictScenarioAction(customState, false).filterIsInstance<RamenTasting>()
        actualChanges = actions.map { it.changeHiddenTips }.toSet()
        assertEquals(expectedChanges, actualChanges)

        // 不足
        customState = customState.updateRamenStatus {
            copy(
                tips = mapOf(
                    RamenTipType.NOODLE to 1,
                    RamenTipType.SOUP to 0,
                    RamenTipType.TOPPING to 7
                ),
                hiddenTips = 2,
            )
        }
        actions = RamenCalculator.predictScenarioAction(customState, false).filterIsInstance<RamenTasting>()
        assertEquals(actions, emptyList())
    }
}
